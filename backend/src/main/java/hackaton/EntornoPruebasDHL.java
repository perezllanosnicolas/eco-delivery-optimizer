package hackaton;

// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).


public class EntornoPruebasDHL {
   public EntornoPruebasDHL() {
   }

   public static void main(String[] var0) {
    /* 
      LocalDate var1 = LocalDate.now();
      String var2 = "28931";
      ConfiguracionGeneradorDatos var3 = new ConfiguracionGeneradorDatos(7, 3, 100, 120.0, 180.0, 20.0, 45.0, 40.0, 80.0, 5.0, 15.0);
      List var4 = GeneradorDatos.generarPuntosDeRecogida(var3, var2);
      List var5 = GeneradorDatos.generarRutas(var3, var1, var2, var4);
      RutaFutura var6 = (RutaFutura)var5.stream().filter((var1x) -> {
         return var1x.fecha.equals(var1) && var1x.puntoRecogidaAsociado == null;
      }).findFirst().orElseThrow(() -> {
         return new RuntimeException("No hay ruta base hoy");
      });
      System.out.println("--- ESCENARIO BASE (SIN ECO PUNTOS) ---");
      System.out.println("Entrega prevista: Hoy a Domicilio");
      System.out.printf("Costo empresa: %.2f€ | Emisiones: %.2f kg CO2\n\n", var6.costoPorPaquete(), var6.emisionesPorPaquete());
      OptimizadorEcoPuntosFinal var7 = new OptimizadorEcoPuntosFinal();
      System.out.println("Buscando alternativas más sostenibles...\n");
      List var8 = var7.calcularOpciones(var1, var6, var5);
      if (var8.isEmpty()) {
         System.out.println("Tu envío ya está en la ruta más óptima o no hay opciones rentables. ¡Gracias por confiar en DHL!");
      } else {
         System.out.println("=== VISTA 1: SELECCIONA UN PUNTO DE RECOGIDA ===");
         Map var9 = var7.obtenerMaximosPorUbicacion(var8);
         var9.entrySet().stream().sorted(Entry.comparingByValue().reversed()).forEach((var0x) -> {
            PrintStream var10000 = System.out;
            String var10001 = (String)var0x.getKey();
            var10000.println("- " + var10001 + "\t-> hasta +" + String.valueOf(var0x.getValue()) + " pts");
         });
         String var10 = "Domicilio";
         if (!var4.isEmpty()) {
            var10 = ((PuntoRecogida)var4.get(0)).nombre;
         }

         System.out.println("\n=== VISTA 2: ¿CUÁNDO PUEDES RECIBIRLO EN '" + var10.toUpperCase() + "'? ===");
         List var11 = var7.obtenerFechasParaUbicacion(var8, var10);
         if (var11.isEmpty()) {
            System.out.println("No hay fechas rentables para esta ubicación.");
         } else {
            var11.forEach((var1x) -> {
               long var2 = ChronoUnit.DAYS.between(var1, var1x.fechaEntrega);
               System.out.println("- " + String.valueOf(var1x.fechaEntrega) + " (+" + var2 + " días)\t| " + var1x.paquetesAgrupados + " paquetes agrupados -> +" + var1x.totalEcoPuntos + " pts");
            });
         }

      }
         */
   }
      
}