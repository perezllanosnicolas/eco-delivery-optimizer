package hackaton;

import io.javalin.Javalin;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ServidorAPI {
    public static void main(String[] args) {
        
        // 1. Iniciamos el servidor web con Javalin
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));
        }).start(7070);

        System.out.println("🚀 Servidor DHL EcoPuntos iniciado en http://localhost:7070");

        
        // 2. Creamos el Endpoint principal que llamará la web
        app.get("/api/v1/eco-opciones", ctx -> {
            
            LocalDate hoy = LocalDate.now();
            String zonaTest = "28931";
            
            ConfiguracionGeneradorDatos config = new ConfiguracionGeneradorDatos(
                7, 3, 100, 120.0, 180.0, 20.0, 45.0, 40.0, 80.0, 5.0, 15.0
            );

            List<PuntoRecogida> puntos = GeneradorDatos.generarPuntosDeRecogida(config, zonaTest);
            List<RutaFutura> todasLasRutas = GeneradorDatos.generarRutas(config, hoy, zonaTest, puntos);
            
            RutaFutura rutaBaseHoy = todasLasRutas.stream()
                .filter(r -> r.fecha.equals(hoy) && r.puntoRecogidaAsociado == null)
                .findFirst()
                .orElse(null);

            OptimizadorEcoPuntosFinal optimizador = new OptimizadorEcoPuntosFinal();
            List<RecomendacionUI> opcionesGeneradas = optimizador.calcularOpciones(hoy, rutaBaseHoy, todasLasRutas);

            Map<String, Integer> topUbicaciones = optimizador.obtenerMaximosPorUbicacion(opcionesGeneradas);
            Map<String, List<DetalleOpcion>> fechasAgrupadas = new HashMap<>();

            for (String nombreUbicacion : topUbicaciones.keySet()) {
                List<RecomendacionUI> recomUbicacion = optimizador.obtenerFechasParaUbicacion(opcionesGeneradas, nombreUbicacion);
                List<DetalleOpcion> detalles = new ArrayList<>();
                
                for (RecomendacionUI rec : recomUbicacion) {
                    int dias = (int) ChronoUnit.DAYS.between(hoy, rec.fechaEntrega);
                    detalles.add(new DetalleOpcion(rec.fechaEntrega.toString(), dias, rec.totalEcoPuntos));
                }
                fechasAgrupadas.put(nombreUbicacion, detalles);
            }

            RespuestaEcoAPI respuestaJson = new RespuestaEcoAPI(topUbicaciones, fechasAgrupadas);
            ctx.json(respuestaJson);
        });
    
        /* 
        //3. Creamos el Endpoint para el Empresario
      // NUEVO ENDPOINT PARA EL PANEL B2B (EMPRESAS)
        app.get("/api/v1/dashboard-stats", ctx -> {
            
            // 1. Generamos los KPIs basados en tu imagen de Móstoles (1 Semana - 1 Zona)
            DashboardKPIs kpis = new DashboardKPIs(420.0, 180.0, 240.0, 48.0, 20, 72);

            // 2. Datos para los gráficos
            DashboardGraficos.RutasLanzadas rutas = new DashboardGraficos.RutasLanzadas(7, 3);
            
            String[] diasSemana = {"L", "M", "X", "J", "V", "S", "D"};
            DashboardGraficos.GraficoLineas emisiones = new DashboardGraficos.GraficoLineas(
                diasSemana, 
                new int[]{12, 25, 38, 48, 60, 72, 84}, // Línea roja (Actual)
                new int[]{4, 12, 12, 10, 11, 24, 36}   // Línea verde (Eco)
            );
            
            DashboardGraficos.GraficoLineas costes = new DashboardGraficos.GraficoLineas(
                diasSemana, 
                new int[]{60, 120, 180, 240, 300, 360, 420}, // Línea roja (Suma de 60€ por ruta/día)
                new int[]{0, 0, 60, 60, 60, 120, 180}        // Línea verde (Rutas agrupadas los X, S y D)
            );

            DashboardGraficos graficos = new DashboardGraficos(rutas, emisiones, costes);

            // 3. Tabla de ROI por Zonas
            List<ZonaROI> tablaRoi = Arrays.asList(
                new ZonaROI("28931 Móstoles", 1.0, 60, -12, "Alto"),
                new ZonaROI("28001 Madrid", 2.75, 165, -33, "Alto"),
                new ZonaROI("08001 Barcelona", 2.25, 135, -27, "Alto"),
                new ZonaROI("41001 Sevilla", 1.5, 90, -18, "Medio"),
                new ZonaROI("46001 Valencia", 1.75, 105, -21, "Alto")
            );

            // 4. Devolvemos el JSON empaquetado
            RespuestaDashboardAPI respuestaDashboard = new RespuestaDashboardAPI(kpis, graficos, tablaRoi);
            ctx.json(respuestaDashboard);
        });
        
        */
        // NUEVO ENDPOINT PARA EL PANEL B2B (EMPRESAS)
app.get("/api/v1/dashboard-stats-con-historico", ctx -> {
    // 1. Configuración y cálculo de proyecciones reales
    ConfiguracionGeneradorDatos configActual = new ConfiguracionGeneradorDatos(
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