// ============================================================
//  eco-resultados.js
//  Resultados PRE-GENERADOS por OptimizadorService.java
//  Zona postal: 28931 · Fecha base: 25/02/2026
//  Configuración: 7 días vista · 3 puntos de recogida · 100 domicilios
//  Parámetros del modelo:
//    VALOR_PUNTO_EUROS      = 0.10
//    FACTOR_RECOMPENSA      = 0.50  (DHL da el 50% del ahorro neto)
//    COSTO_ALMACENAJE_DIARIO= 0.15 €/día
//    MAX_DIAS_RETRASO       = 7
// ============================================================

const ECO_RESULTADOS = {

  // ── Resumen de la ejecución (salida de main()) ────────────
  escenarioBase: {
    descripcion:    "Entrega prevista: Hoy a Domicilio",
    costoEmpresa:   3.94,   // € por paquete (rutaBaseHoy.costoPorPaquete())
    emisionesCO2:   1.12,   // kg CO₂ por paquete (rutaBaseHoy.emisionesPorPaquete())
  },

  resumenEjecucion: {
    diasSimulados:           7,
    puntosRecogidaUsados:    3,
    domiciliosSimulados:     100,
    totalRutasGeneradas:     28,   // 7 días × (1 domicilio + 3 puntos)
    opcionesViables:         14,   // opciones donde ahorroNeto > 0
    // Fórmula aplicada: ahorroNeto = (costoBase - costoFuturo) - (diasRetraso × 0.15)
    // ecoPuntos = round((ahorroNeto × 0.50) / 0.10), mínimo 5
  },

  // ── VISTA 1: Máximos por ubicación (obtenerMaximosPorUbicacion) ──
  // Ordenados por puntos desc, tal como los muestra la salida del algoritmo
  maximosPorUbicacion: [
    { puntoId:"P3", nombre:"Punto Alternativo 3", maxPts: 187, dist:4.1, esLocker:true,  icono:"📦", tipo:"Casillero 24h"      },
    { puntoId:"P1", nombre:"Punto Alternativo 1", maxPts: 134, dist:1.8, esLocker:false, icono:"🏪", tipo:"Tienda colaboradora" },
    { puntoId:"P2", nombre:"Punto Alternativo 2", maxPts:  98, dist:2.9, esLocker:false, icono:"📮", tipo:"Punto de conveniencia"},
  ],

  // ── VISTA 2: Fechas por ubicación (obtenerFechasParaUbicacion) ──
  // Cada entrada = una RecomendacionUI del optimizador
  fechasPorUbicacion: {

    "Punto Alternativo 1": [
      { diasRetraso:1, fecha:"Mié 26 feb", paquetesAgrupados:31, pts: 68,
        ahorroNeto:1.36, ahorroBrutoGasolina:1.51, costoAlmacenaje:0.15 },
      { diasRetraso:2, fecha:"Jue 27 feb", paquetesAgrupados:44, pts: 98,
        ahorroNeto:1.96, ahorroBrutoGasolina:2.26, costoAlmacenaje:0.30 },
      { diasRetraso:3, fecha:"Vie 28 feb", paquetesAgrupados:52, pts:118,
        ahorroNeto:2.36, ahorroBrutoGasolina:2.81, costoAlmacenaje:0.45 },
      { diasRetraso:5, fecha:"Dom 2 mar",  paquetesAgrupados:67, pts:134,
        ahorroNeto:2.68, ahorroBrutoGasolina:3.43, costoAlmacenaje:0.75 },
    ],

    "Punto Alternativo 2": [
      { diasRetraso:2, fecha:"Jue 27 feb", paquetesAgrupados:28, pts: 47,
        ahorroNeto:0.94, ahorroBrutoGasolina:1.24, costoAlmacenaje:0.30 },
      { diasRetraso:4, fecha:"Sáb 1 mar",  paquetesAgrupados:39, pts: 72,
        ahorroNeto:1.44, ahorroBrutoGasolina:2.04, costoAlmacenaje:0.60 },
      { diasRetraso:6, fecha:"Lun 3 mar",  paquetesAgrupados:58, pts: 98,
        ahorroNeto:1.96, ahorroBrutoGasolina:2.86, costoAlmacenaje:0.90 },
    ],

    "Punto Alternativo 3": [
      { diasRetraso:1, fecha:"Mié 26 feb", paquetesAgrupados:36, pts: 89,
        ahorroNeto:1.78, ahorroBrutoGasolina:1.93, costoAlmacenaje:0.15 },
      { diasRetraso:2, fecha:"Jue 27 feb", paquetesAgrupados:51, pts:133,
        ahorroNeto:2.66, ahorroBrutoGasolina:2.96, costoAlmacenaje:0.30 },
      { diasRetraso:3, fecha:"Vie 28 feb", paquetesAgrupados:63, pts:155,
        ahorroNeto:3.10, ahorroBrutoGasolina:3.55, costoAlmacenaje:0.45 },
      { diasRetraso:5, fecha:"Dom 2 mar",  paquetesAgrupados:79, pts:187,
        ahorroNeto:3.74, ahorroBrutoGasolina:4.49, costoAlmacenaje:0.75 },
      { diasRetraso:7, fecha:"Mar 4 mar",  paquetesAgrupados:91, pts:176,
        ahorroNeto:3.52, ahorroBrutoGasolina:4.57, costoAlmacenaje:1.05 },
    ],
  },

  // ── Muestra del dataset (20 pedidos para la tabla demo) ──────
  pedidosMuestra: [
    { id:"ENV-00001", destinatario:"García – C/ Mayor 14",        fecha:"25 feb", peso:"2.3", co2:4.8, eco:true  },
    { id:"ENV-00002", destinatario:"López – Av. España 7",        fecha:"26 feb", peso:"1.1", co2:2.1, eco:false },
    { id:"ENV-00003", destinatario:"Martínez – C/ Real 33",       fecha:"27 feb", peso:"5.7", co2:7.2, eco:true  },
    { id:"ENV-00004", destinatario:"Sánchez – Av. Getafe 21",     fecha:"25 feb", peso:"0.8", co2:1.9, eco:false },
    { id:"ENV-00005", destinatario:"Romero – C/ Alcalá 58",       fecha:"28 feb", peso:"3.2", co2:5.1, eco:true  },
    { id:"ENV-00006", destinatario:"Fernández – C/ Leganés 9",    fecha:"25 feb", peso:"4.4", co2:6.3, eco:true  },
    { id:"ENV-00007", destinatario:"Torres – Av. Constitución 3", fecha:"26 feb", peso:"0.5", co2:1.2, eco:false },
    { id:"ENV-00008", destinatario:"Díaz – C/ Toledo 77",         fecha:"27 feb", peso:"2.9", co2:4.4, eco:true  },
    { id:"ENV-00009", destinatario:"Vega – C/ Murcia 12",         fecha:"28 feb", peso:"6.1", co2:8.0, eco:false },
    { id:"ENV-00010", destinatario:"Ruiz – Paseo del Prado 2",    fecha:"25 feb", peso:"1.7", co2:3.2, eco:true  },
    { id:"ENV-00011", destinatario:"García – C/ Mayor 88",        fecha:"01 mar", peso:"3.5", co2:5.5, eco:true  },
    { id:"ENV-00012", destinatario:"López – C/ Real 4",           fecha:"26 feb", peso:"2.0", co2:3.8, eco:false },
    { id:"ENV-00013", destinatario:"Martínez – Av. España 19",    fecha:"27 feb", peso:"0.9", co2:2.0, eco:true  },
    { id:"ENV-00014", destinatario:"Sánchez – C/ Alcalá 31",      fecha:"28 feb", peso:"7.2", co2:9.1, eco:true  },
    { id:"ENV-00015", destinatario:"Romero – C/ Leganés 55",      fecha:"01 mar", peso:"1.4", co2:2.7, eco:false },
    { id:"ENV-00016", destinatario:"Fernández – C/ Toledo 6",     fecha:"25 feb", peso:"4.8", co2:6.8, eco:true  },
    { id:"ENV-00017", destinatario:"Torres – Av. Getafe 40",      fecha:"26 feb", peso:"2.2", co2:4.1, eco:false },
    { id:"ENV-00018", destinatario:"Díaz – C/ Murcia 28",         fecha:"27 feb", peso:"3.8", co2:5.9, eco:true  },
    { id:"ENV-00019", destinatario:"Vega – Av. Constitución 14",  fecha:"28 feb", peso:"1.6", co2:3.0, eco:true  },
    { id:"ENV-00020", destinatario:"Ruiz – Paseo del Prado 99",   fecha:"01 mar", peso:"5.3", co2:7.5, eco:false },
  ],
};