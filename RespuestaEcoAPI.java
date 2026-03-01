package hackaton;

import java.util.List;
import java.util.Map;

public class RespuestaEcoAPI {
    public Map<String, Integer> ubicacionesTopPuntos; 
    public Map<String, List<DetalleOpcion>> fechasPorUbicacion;

    public RespuestaEcoAPI(Map<String, Integer> top, Map<String, List<DetalleOpcion>> fechas) {
        this.ubicacionesTopPuntos = top;
        this.fechasPorUbicacion = fechas;
    }
}

