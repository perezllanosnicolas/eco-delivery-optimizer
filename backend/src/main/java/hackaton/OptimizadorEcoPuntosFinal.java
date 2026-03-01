// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package hackaton;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class OptimizadorEcoPuntosFinal {
   private static final double VALOR_PUNTO_EUROS = 0.1;
   private static final double FACTOR_RECOMPENSA = 0.5;
   private static final double COSTO_ALMACENAJE_DIARIO = 0.15;
   private static final int MAX_DIAS_RETRASO = 7;

   OptimizadorEcoPuntosFinal() {
   }

   public List<RecomendacionUI> calcularOpciones(LocalDate var1, RutaFutura var2, List<RutaFutura> var3) {
      ArrayList var4 = new ArrayList();
      double var5 = var2.costoPorPaquete();
      Iterator var7 = var3.iterator();

      while(var7.hasNext()) {
         RutaFutura var8 = (RutaFutura)var7.next();
         if (var8.paquetesActuales < var8.capacidadMaxima) {
            int var9 = (int)ChronoUnit.DAYS.between(var1, var8.fecha);
            if (var9 >= 0 && var9 <= 7) {
               double var10 = var8.costoPorPaquete();
               double var12 = var5 - var10;
               double var14 = (double)var9 * 0.15;
               double var16 = var12 - var14;
               if (var16 > 0.0) {
                  int var18 = (int)Math.round(var16 * 0.5 / 0.1);
                  var18 = Math.max(var18, 5);
                  var4.add(new RecomendacionUI(var8.puntoRecogidaAsociado, var8.fecha, var18, var8.paquetesActuales));
               }
            }
         }
      }

      return var4;
   }

   public Map<String, Integer> obtenerMaximosPorUbicacion(List<RecomendacionUI> var1) {
      return (Map)var1.stream().collect(Collectors.toMap((var0) -> {
         return var0.ubicacion != null ? var0.ubicacion.nombre : "Domicilio";
      }, (var0) -> {
         return var0.totalEcoPuntos;
      }, Integer::max));
   }

   public List<RecomendacionUI> obtenerFechasParaUbicacion(List<RecomendacionUI> var1, String var2) {
      return (List)var1.stream().filter((var1x) -> {
         if (var2.equals("Domicilio")) {
            return var1x.ubicacion == null;
         } else {
            return var1x.ubicacion != null && var1x.ubicacion.nombre.equals(var2);
         }
      }).sorted((var0, var1x) -> {
         return var0.fechaEntrega.compareTo(var1x.fechaEntrega);
      }).collect(Collectors.toList());
   }
}
