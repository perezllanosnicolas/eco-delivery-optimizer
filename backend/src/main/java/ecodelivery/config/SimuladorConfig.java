package ecodelivery.config;

public class SimuladorConfig {
   public int diasSimulacion;
   public int numPuntosRecogida;
   public int numDomicilios;
   public double minCostoDomicilio;
   public double maxCostoDomicilio;
   public double minCO2Domicilio;
   public double maxCO2Domicilio;
   public double minCostoPunto;
   public double maxCostoPunto;
   public double minCO2Punto;
   public double maxCO2Punto;

   public SimuladorConfig(int var1, int var2, int var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18) {
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
