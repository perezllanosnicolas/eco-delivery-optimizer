package hackaton;
import java.time.LocalDate;

class RutaFutura {
   String idRuta;
   LocalDate fecha;
   String zonaPostal;
   PuntoRecogida puntoRecogidaAsociado;
   int paquetesActuales;
   int capacidadMaxima;
   double costoBaseRutaEuros;
   double emisionesBaseKgCO2;

   public RutaFutura(String var1, LocalDate var2, String var3, PuntoRecogida var4, int var5, int var6, double var7, double var9) {
      this.idRuta = var1;
      this.fecha = var2;
      this.zonaPostal = var3;
      this.puntoRecogidaAsociado = var4;
      this.paquetesActuales = var5;
      this.capacidadMaxima = var6;
      this.costoBaseRutaEuros = var7;
      this.emisionesBaseKgCO2 = var9;
   }

   public double costoPorPaquete() {
      return this.paquetesActuales == 0 ? this.costoBaseRutaEuros : this.costoBaseRutaEuros / (double)this.paquetesActuales;
   }

   public double emisionesPorPaquete() {
      return this.paquetesActuales == 0 ? this.emisionesBaseKgCO2 : this.emisionesBaseKgCO2 / (double)this.paquetesActuales;
   }
}
