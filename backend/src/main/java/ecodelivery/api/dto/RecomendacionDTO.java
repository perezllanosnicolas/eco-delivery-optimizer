package ecodelivery.api.dto;

import ecodelivery.models.PuntoRecogida;


import java.time.LocalDate;

public class RecomendacionDTO {
   public PuntoRecogida ubicacion;
   public LocalDate fechaEntrega;
   public int totalEcoPuntos;
   public int paquetesAgrupados;

   public RecomendacionDTO(PuntoRecogida var1, LocalDate var2, int var3, int var4) {
      this.ubicacion = var1;
      this.fechaEntrega = var2;
      this.totalEcoPuntos = var3;
      this.paquetesAgrupados = var4;
   }
}

