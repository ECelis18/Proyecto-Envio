package org.example.infraestructura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.h2.tools.Server;

public class ConexionBD {

    // BD en archivo: crea ~/integrador_bd.mv.db en tu usuario
    private String url = "jdbc:h2:~/integrador_bd";
    private String usuario = "sa";
    private String contraseña = "";

    // para conectar una BD.

    public Connection conectar() {
        try {
            Connection conexionAMiBD = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println(" Éxito conectandonos a la BD");
            return conexionAMiBD;

        } catch (Exception error) {
            System.out.println(" Error en la conexion" + error.getMessage());
            return null;

        }
    }

    public void crearTablaEnvios() {
        String crearTablaQUERY = """
        CREATE TABLE IF NOT EXISTS ENVIOS (
            ID INT PRIMARY KEY AUTO_INCREMENT,
            ID_USUARIO INT NOT NULL,
            NOMBRE_USUARIO VARCHAR(255) NOT NULL,  -- NUEVA COLUMNA
            DIRECCION_DESTINO VARCHAR(255) NOT NULL,
            ESTADO VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
            FECHA_ENVIO DATE NOT NULL,
            FECHA_ENTREGA DATE,
            COSTO_ENVIO DECIMAL(10, 2) NOT NULL,
            FECHA_CREACION TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
        """;

        try (Connection conexion = conectar(); Statement st = conexion.createStatement()) {
            st.execute(crearTablaQUERY);
            System.out.println("✅ Tabla ENVIOS creada/verificada con campo NOMBRE_CLIENTE");
        } catch (Exception e) {
            System.out.println("❌ Error al crear tabla ENVIOS: " + e.getMessage());
        }
    }

    // Método para inicializar todas las tablas
    public void inicializarTodasLasTablas() {
        System.out.println("\n🔧 INICIALIZANDO BASE DE DATOS...");
        crearTablaEnvios();
        System.out.println("✅ Base de datos inicializada correctamente\n");
    }

}
