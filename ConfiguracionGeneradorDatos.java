package hackaton;

class ConfiguracionGeneradorDatos {
   int diasSimulacion;
   int numPuntosRecogida;
   int numDomicilios;
   double minCostoDomicilio;
   double maxCostoDomicilio;
   double minCO2Domicilio;
   double maxCO2Domicilio;
   double minCostoPunto;
   double maxCostoPunto;
   double minCO2Punto;
   double maxCO2Punto;

   public ConfiguracionGeneradorDatos(int var1, int var2, int var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18) {
      this.diasSimulacion = var1;
      this.numPuntosRecogida = var2;
      this.numDomicilios = var3;
      this.minCostoDomicilio = var4;
      this.maxCostoDomicilio = var6;
      this.minCO2Domicilio = var8;
      this.maxCO2Domicilio = var10;
      this.minCostoPunto = var12;
      this.maxCostoPunto = var14;
      this.minCO2Punto = var16;
      this.maxCO2Punto = var18;
   }
}
