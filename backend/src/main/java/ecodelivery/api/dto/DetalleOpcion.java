package ecodelivery.api.dto;

public class DetalleOpcion {
    public String fecha;
    public int diasRetraso;
    public int puntos;

    public DetalleOpcion(String fecha, int diasRetraso, int puntos) {
        this.fecha = fecha;
        this.diasRetraso = diasRetraso;
        this.puntos = puntos;
    }
}