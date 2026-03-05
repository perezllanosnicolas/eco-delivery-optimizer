
package ecodelivery.db;

import ecodelivery.models.PuntoRecogida;
import ecodelivery.models.RutaFutura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

// Leemos la IP de la base de datos de Docker, o usamos localhost si estamos en local
    private static final String DB_HOST = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    private static final String URL = "jdbc:postgresql://" + DB_HOST + ":5432/ecodelivery";
    private static final String USUARIO = "postgres";
    private static final String PASSWORD = "admin";

    // Método privado para obtener la conexión
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // --------------------------------------------------------
    // 1. Obtener Puntos de Recogida Reales de la BD
    // --------------------------------------------------------
    public List<PuntoRecogida> obtenerPuntosDeRecogida(String zonaPostal) {
        List<PuntoRecogida> puntos = new ArrayList<>();
        String sql = "SELECT * FROM puntos_recogida WHERE zona_postal = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, zonaPostal);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                PuntoRecogida punto = new PuntoRecogida(
                    rs.getString("id_punto"),
                    rs.getString("nombre"),
                    rs.getString("zona_postal"),
                    rs.getDouble("distancia_media_km"),
                    rs.getBoolean("es_locker_24h")
                );
                puntos.add(punto);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener puntos de recogida: " + e.getMessage());
        }
        return puntos;
    }

    // --------------------------------------------------------
    // 2. Obtener Rutas Reales de la BD
    // --------------------------------------------------------
    public List<RutaFutura> obtenerRutas(String zonaPostal, LocalDate fechaActual, List<PuntoRecogida> puntosConocidos) {
        List<RutaFutura> rutas = new ArrayList<>();
        // Buscamos rutas de hoy en adelante para esa zona
        String sql = "SELECT * FROM rutas WHERE zona_postal = ? AND fecha >= ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, zonaPostal);
            pstmt.setDate(2, java.sql.Date.valueOf(fechaActual));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String idPunto = rs.getString("id_punto");
                PuntoRecogida puntoAsociado = null;

                // Si la ruta tiene un id_punto, buscamos el objeto PuntoRecogida correspondiente
                if (idPunto != null) {
                    for (PuntoRecogida p : puntosConocidos) {
                        if (p.idPunto.equals(idPunto)) {
                            puntoAsociado = p;
                            break;
                        }
                    }
                } // Si idPunto es null, significa que es ruta a Domicilio, así que puntoAsociado se queda null.

                RutaFutura ruta = new RutaFutura(
                    rs.getString("id_ruta"),
                    rs.getDate("fecha").toLocalDate(),
                    rs.getString("zona_postal"),
                    puntoAsociado,
                    rs.getInt("paquetes_actuales"),
                    rs.getInt("capacidad_maxima"),
                    rs.getDouble("costo_base_euros"),
                    rs.getDouble("emisiones_base_co2")
                );
                rutas.add(ruta);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener rutas: " + e.getMessage());
        }
        return rutas;
    }
}