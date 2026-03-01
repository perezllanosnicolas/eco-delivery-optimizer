package hackaton;

import java.time.LocalDate;
class RecomendacionUI {
   PuntoRecogida ubicacion;
   LocalDate fechaEntrega;
   int totalEcoPuntos;
   int paquetesAgrupados;

   public RecomendacionUI(PuntoRecogida var1, LocalDate var2, int var3, int var4) {
      this.ubicacion = var1;
      this.fechaEntrega = var2;
      this.totalEcoPuntos = var3;
      this.paquetesAgrupados = var4;
   }
}

