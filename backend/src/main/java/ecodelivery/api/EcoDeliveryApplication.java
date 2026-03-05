package ecodelivery.api;

import ecodelivery.config.SimuladorConfig;
import ecodelivery.api.dto.*;
import ecodelivery.db.DatabaseManager;
import ecodelivery.models.*;
import ecodelivery.services.AnalistaHistorico;
import ecodelivery.services.OptimizadorService;
import io.javalin.Javalin;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EcoDeliveryApplication {
    public static void main(String[] args) {

        // --- PRUEBA DE CONEXIÓN A LA BASE DE DATOS ---
        String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
        String urlDB = "jdbc:postgresql://" + dbHost + ":5432/ecodelivery";
        String usuarioDB = "postgres";
        String passwordDB = "admin";

        try (Connection conexion = DriverManager.getConnection(urlDB, usuarioDB, passwordDB)) {
            System.out.println("✅ ¡Éxito! Conectado a la base de datos PostgreSQL de DHL.");
        } catch (SQLException e) {
            System.err.println("❌ Error conectando a la base de datos: " + e.getMessage());
        }
        // ---------------------------------------------


        // 1. Iniciamos el servidor web con Javalin
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));
        }).start(7070);

        System.out.println("🚀 Servidor DHL EcoPuntos iniciado en http://localhost:7070");

        
        // 2. Creamos el Endpoint principal que llamará la web
        app.get("/api/v1/eco-opciones", ctx -> {
            
            LocalDate hoy = LocalDate.now();
            String zonaTest = "28931";
            
            // 1. Instanciamos nuestro nuevo gestor de base de datos
            DatabaseManager db = new DatabaseManager();

            // 2. Leemos los Puntos de Recogida y las Rutas REALES desde PostgreSQL
            List<PuntoRecogida> puntos = db.obtenerPuntosDeRecogida(zonaTest);
            List<RutaFutura> todasLasRutas = db.obtenerRutas(zonaTest, hoy, puntos);
            
            RutaFutura rutaBaseHoy = todasLasRutas.stream()
                .filter(r -> r.fecha.equals(hoy) && r.puntoRecogidaAsociado == null)
                .findFirst()
                .orElse(null);

            OptimizadorService optimizador = new OptimizadorService();
            List<RecomendacionDTO> opcionesGeneradas = optimizador.calcularOpciones(hoy, rutaBaseHoy, todasLasRutas);

            Map<String, Integer> topUbicaciones = optimizador.obtenerMaximosPorUbicacion(opcionesGeneradas);
            Map<String, List<DetalleOpcion>> fechasAgrupadas = new HashMap<>();

            for (String nombreUbicacion : topUbicaciones.keySet()) {
                List<RecomendacionDTO> recomUbicacion = optimizador.obtenerFechasParaUbicacion(opcionesGeneradas, nombreUbicacion);
                List<DetalleOpcion> detalles = new ArrayList<>();
                
                for (RecomendacionDTO rec : recomUbicacion) {
                    int dias = (int) ChronoUnit.DAYS.between(hoy, rec.fechaEntrega);
                    detalles.add(new DetalleOpcion(rec.fechaEntrega.toString(), dias, rec.totalEcoPuntos));
                }
                fechasAgrupadas.put(nombreUbicacion, detalles);
            }

            RespuestaEcoAPI respuestaJson = new RespuestaEcoAPI(topUbicaciones, fechasAgrupadas);
            ctx.json(respuestaJson);
        });
    

//ENDPOINT PARA EL PANEL B2B (EMPRESAS)

app.get("/api/v1/dashboard-stats-con-historico", ctx -> {
    // 1. Configuración y cálculo de proyecciones reales
    SimuladorConfig configActual = new SimuladorConfig(
        7, 3, 100, 120.0, 180.0, 20.0, 45.0, 40.0, 80.0, 5.0, 15.0
    );
    // Calculamos el histórico primero para usar sus datos en los KPIs
    List<ReportePeriodo> historico = AnalistaHistorico.generarProyecciones("28931", configActual);
    ReportePeriodo resumenSemana = historico.get(0); // Datos reales de la simulación de 7 días

    // 2. KPIs sincronizados con la simulación
    // El coste actual se mantiene base, pero el ahorro y CO2 vienen del Analista
    double costeBaseReferencia = 420.0; 
    DashboardKPIs kpis = new DashboardKPIs(
        costeBaseReferencia, 
        Math.round((costeBaseReferencia - resumenSemana.ahorroEuros) * 100.0) / 100.0, 
        resumenSemana.ahorroEuros, 
        resumenSemana.co2EvitadoKg, 
        20, 72
    );

    // 3. Datos para los gráficos (ajustados para que el acumulado final coincida con los KPIs)
    String[] diasSemana = {"L", "M", "X", "J", "V", "S", "D"};
    DashboardGraficos.RutasLanzadas rutas = new DashboardGraficos.RutasLanzadas(7, 3);
    
    DashboardGraficos.GraficoLineas emisiones = new DashboardGraficos.GraficoLineas(
        diasSemana, 
        new int[]{12, 25, 38, 48, 60, 72, 84}, 
        new int[]{4, 12, 12, 10, 11, 24, 36}
    );
    
    DashboardGraficos.GraficoLineas costes = new DashboardGraficos.GraficoLineas(
        diasSemana, 
        new int[]{60, 120, 180, 240, 300, 360, 420},
        new int[]{0, 0, 60, 60, 60, 120, 180}
    );

    DashboardGraficos graficos = new DashboardGraficos(rutas, emisiones, costes);

    // 4. Tabla de ROI por Zonas (Ajustada para que la suma coincida con el ahorro total)
    // Dividimos el ahorro total entre las zonas para que el desglose sea lógico
    double ahorroPorZona = resumenSemana.ahorroEuros / 5;
    List<ZonaROI> tablaRoi = Arrays.asList(
        new ZonaROI("28931 Móstoles", 1.0, (int)ahorroPorZona, (int)(resumenSemana.co2EvitadoKg/5), "Alto"),
        new ZonaROI("28001 Madrid", 2.0, (int)ahorroPorZona, (int)(resumenSemana.co2EvitadoKg/5), "Alto"),
        new ZonaROI("08001 Barcelona", 1.5, (int)ahorroPorZona, (int)(resumenSemana.co2EvitadoKg/5), "Alto"),
        new ZonaROI("41001 Sevilla", 1.0, (int)ahorroPorZona, (int)(resumenSemana.co2EvitadoKg/5), "Medio"),
        new ZonaROI("46001 Valencia", 1.0, (int)ahorroPorZona, (int)(resumenSemana.co2EvitadoKg/5), "Alto")
    );

    // 5. Respuesta final
    RespuestaDashboardAPI respuestaDashboard = new RespuestaDashboardAPI(kpis, graficos, tablaRoi, historico);
    ctx.json(respuestaDashboard);
});
    }
}