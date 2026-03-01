package hackaton;

// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
class PuntoRecogida {
   String idPunto;
   String nombre;
   String zonaPostal;
   double distanciaMediaZonaKm;
   boolean esLocker24h;

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
