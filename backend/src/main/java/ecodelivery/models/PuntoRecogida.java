package ecodelivery.models;

public class PuntoRecogida {
   public String idPunto;
   public String nombre;
   public String zonaPostal;
   public double distanciaMediaZonaKm;
   public boolean esLocker24h;

   public PuntoRecogida(String var1, String var2, String var3, double var4, boolean var6) {
      this.idPunto = var1;
      this.nombre = var2;
      this.zonaPostal = var3;
      this.distanciaMediaZonaKm = var4;
      this.esLocker24h = var6;
   }

   public String toString() {
      return this.nombre + " (" + this.distanciaMediaZonaKm + " km de tu casa)";
   }
}
