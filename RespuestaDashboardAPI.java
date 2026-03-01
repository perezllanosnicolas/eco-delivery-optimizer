package hackaton;

import java.util.List;

public class RespuestaDashboardAPI {
    public DashboardKPIs kpis;
    public DashboardGraficos graficos;
    public List<ZonaROI> tablaRoi;
    // NUEVO CAMPO: Lista para guardar las proyecciones (1 semana, 1 mes, 1 año)
    public List<ReportePeriodo> historicoProyecciones; 

    public RespuestaDashboardAPI(DashboardKPIs kpis, DashboardGraficos graficos, List<ZonaROI> tablaRoi, List<ReportePeriodo> historico) {
        this.kpis = kpis;
        this.graficos = graficos;
        this.tablaRoi = tablaRoi;
        this.historicoProyecciones = historico; // Asignamos el histórico
    }
}

// Clases auxiliares (solo empaquetan datos, no llevan "public" para poder convivir en el mismo archivo)
class DashboardKPIs {
    public double costeActual, costeEco, ahorroNeto, co2EvitadoKg;
    public int porcentajeOcupacionSinEco, porcentajeOcupacionConEco;
    
    public DashboardKPIs(double ca, double ce, double an, double co2, int pSin, int pCon) {
        this.costeActual = ca; this.costeEco = ce; this.ahorroNeto = an; 
        this.co2EvitadoKg = co2; this.porcentajeOcupacionSinEco = pSin; this.porcentajeOcupacionConEco = pCon;
    }
}

class DashboardGraficos {
    public RutasLanzadas rutasLanzadas;
    public GraficoLineas emisionesDiarias;
    public GraficoLineas costeDiario;

    public DashboardGraficos(RutasLanzadas rutas, GraficoLineas emisiones, GraficoLineas coste) {
        this.rutasLanzadas = rutas; this.emisionesDiarias = emisiones; this.costeDiario = coste;
    }
    
    public static class RutasLanzadas { 
        public int sinEco, conEco; 
        public RutasLanzadas(int s, int c){sinEco=s; conEco=c;} 
    }
    
    public static class GraficoLineas { 
        public String[] dias; 
        public int[] acumuladoActual; 
        public int[] acumuladoEco; 
        public GraficoLineas(String[] d, int[] a, int[] e){dias=d; acumuladoActual=a; acumuladoEco=e;} 
    }
}

class ZonaROI {
    public String zona; 
    public double rutasAhorradas; 
    public int ahorroEuros; 
    public int co2Evitado; 
    public String roi;
    
    public ZonaROI(String z, double r, int a, int co2, String roi) {
        this.zona = z; this.rutasAhorradas = r; this.ahorroEuros = a; this.co2Evitado = co2; this.roi = roi;
    }
}