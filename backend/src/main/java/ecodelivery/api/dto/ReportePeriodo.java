package ecodelivery.api.dto;

public class ReportePeriodo {
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

