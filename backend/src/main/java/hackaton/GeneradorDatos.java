package hackaton;

// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

class GeneradorDatos {
   private static Random rand = new Random();

   GeneradorDatos() {
   }

   public static List<PuntoRecogida> generarPuntosDeRecogida(ConfiguracionGeneradorDatos var0, String var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 1; var3 <= var0.numPuntosRecogida; ++var3) {
         double var4 = 0.5 + 4.5 * rand.nextDouble();
         boolean var6 = rand.nextBoolean();
         var2.add(new PuntoRecogida("PUNTO-" + var3, "Punto Alternativo " + var3, var1, (double)Math.round(var4 * 10.0) / 10.0, var6));
      }

      return var2;
   }

   public static List<RutaFutura> generarRutas(ConfiguracionGeneradorDatos var0, LocalDate var1, String var2, List<PuntoRecogida> var3) {
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < var0.diasSimulacion; ++var5) {
         LocalDate var6 = var1.plusDays((long)var5);
         double var7 = var0.minCostoDomicilio + rand.nextDouble() * (var0.maxCostoDomicilio - var0.minCostoDomicilio);
         double var9 = var0.minCO2Domicilio + rand.nextDouble() * (var0.maxCO2Domicilio - var0.minCO2Domicilio);
         int var11 = rand.nextInt(30) + 5;
         var4.add(new RutaFutura("R-DOM-DIA" + var5, var6, var2, (PuntoRecogida)null, var11, 100, var7, var9));
         Iterator var12 = var3.iterator();

         while(var12.hasNext()) {
            PuntoRecogida var13 = (PuntoRecogida)var12.next();
            double var14 = var0.minCostoPunto + rand.nextDouble() * (var0.maxCostoPunto - var0.minCostoPunto);
            double var16 = var0.minCO2Punto + rand.nextDouble() * (var0.maxCO2Punto - var0.minCO2Punto);
            int var18 = rand.nextInt(60) + 20;
            var4.add(new RutaFutura("R-" + var13.idPunto + "-DIA" + var5, var6, var2, var13, var18, 150, var14, var16));
         }
      }

      return var4;
   }
}
