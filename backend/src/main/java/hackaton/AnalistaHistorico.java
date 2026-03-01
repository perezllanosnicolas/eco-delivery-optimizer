package hackaton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnalistaHistorico {

    public static List<ReportePeriodo> generarProyecciones(String zonaTest, ConfiguracionGeneradorDatos configBase) {
        List<ReportePeriodo> proyecciones = new ArrayList<>();
        
        // Ejecutamos simulaciones para los tres periodos
        proyecciones.add(simularPeriodo("1 Semana", 7, zonaTest, configBase));
        proyecciones.add(simularPeriodo("1 Mes", 30, zonaTest, configBase));
        proyecciones.add(simularPeriodo("1 Año", 365, zonaTest, configBase));
        
        return proyecciones;
    }

    private static ReportePeriodo simularPeriodo(String etiqueta, int diasTotales, String zona, ConfiguracionGeneradorDatos configBase) {
        LocalDate hoy = LocalDate.now();
        
        // Configuración extendida para cubrir el rango de tiempo
        ConfiguracionGeneradorDatos configLarga = new ConfiguracionGeneradorDatos(
            diasTotales + 1, 
            configBase.numPuntosRecogida, configBase.numDomicilios,
            configBase.minCostoDomicilio, configBase.maxCostoDomicilio,
            configBase.minCO2Domicilio, configBase.maxCO2Domicilio,
            configBase.minCostoPunto, configBase.maxCostoPunto,
            configBase.minCO2Punto, configBase.maxCO2Punto
        );

        List<PuntoRecogida> puntos = GeneradorDatos.generarPuntosDeRecogida(configLarga, zona);
        List<RutaFutura> todasLasRutas = GeneradorDatos.generarRutas(configLarga, hoy, zona, puntos);
        OptimizadorEcoPuntosFinal optimizador = new OptimizadorEcoPuntosFinal();

        double ahorroTotalPeriodo = 0.0;
        double co2EvitadoPeriodo = 0.0;
        int paquetesConEcoPeriodo = 0;

        for (int i = 0; i < diasTotales; i++) {
            LocalDate diaActual = hoy.plusDays(i);

            RutaFutura rutaBaseDia = todasLasRutas.stream()
                .filter(r -> r.fecha.equals(diaActual) && r.puntoRecogidaAsociado == null)
                .findFirst()
                .orElse(null);

            if (rutaBaseDia != null) {
                List<RecomendacionUI> opciones = optimizador.calcularOpciones(diaActual, rutaBaseDia, todasLasRutas);
                
                if (!opciones.isEmpty()) {
                    // Tasa de adopción realista: 30%
                    int paquetesAceptados = (int) Math.max(1, rutaBaseDia.paquetesActuales * 0.30);
                    
                    // Seleccionamos la mejor opción disponible
                    RecomendacionUI mejorOpcion = opciones.stream()
                        .max((o1, o2) -> Integer.compare(o1.totalEcoPuntos, o2.totalEcoPuntos))
                        .get();
                    
                    // Cálculo de ahorro: (Puntos * ValorEuros) / FactorEmpresa
                    // 1 pto = 0.10€, DHL se queda con el 50% del ahorro total generado
                    double ahorroNetoPorPaquete = (mejorOpcion.totalEcoPuntos * 0.10) / 0.50;
                    
                    // Reducción de CO2 basada en métrica del 63%
                    double co2Base = rutaBaseDia.emisionesPorPaquete();
                    double co2EvitadoPorPaquete = co2Base * 0.63; 

                    ahorroTotalPeriodo += (ahorroNetoPorPaquete * paquetesAceptados);
                    co2EvitadoPeriodo += (co2EvitadoPorPaquete * paquetesAceptados);
                    paquetesConEcoPeriodo += paquetesAceptados;
                }
            }
        }

        return new ReportePeriodo(etiqueta, diasTotales, ahorroTotalPeriodo, co2EvitadoPeriodo, paquetesConEcoPeriodo);
    }
}

class ReportePeriodo {
    public String periodo;
    public int dias;
    public double ahorroEuros;
    public double co2EvitadoKg;
    public int paquetesOptimizados;

    public ReportePeriodo(String periodo, int dias, double ahorroEuros, double co2EvitadoKg, int paquetesOptimizados) {
        this.periodo = periodo;
        this.dias = dias;
        this.ahorroEuros = Math.round(ahorroEuros * 100.0) / 100.0; 
        this.co2EvitadoKg = Math.round(co2EvitadoKg * 100.0) / 100.0;
        this.paquetesOptimizados = paquetesOptimizados;
    }
}

