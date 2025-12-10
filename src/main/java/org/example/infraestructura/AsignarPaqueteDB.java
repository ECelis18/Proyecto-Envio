package org.example.infraestructura;

import java.sql.Connection;
import java.sql.Statement;

public class AsignarPaqueteDB {

    public void crearTablas(){
        String sql = """
            CREATE TABLE IF NOT EXISTS paquete(
                id INT AUTO_INCREMENT PRIMARY KEY,
                descripcion VARCHAR(100),
                estado VARCHAR(50)
            );

            CREATE TABLE IF NOT EXISTS ruta(
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100),
                activa BOOLEAN
            );

            CREATE TABLE IF NOT EXISTS envio(
                id INT AUTO_INCREMENT PRIMARY KEY,
                idPaquete INT,
                idRuta INT,
                estado VARCHAR(50)
            );
        """;

        try (Connection conn = new ConexionBD().conectar();
             Statement st = conn.createStatement()){

            st.execute(sql);
            System.out.println("✔ Esquema creado correctamente");

        } catch (Exception e) {
            System.out.println("❌ Error creando tablas: " + e.getMessage());
        }
    }
}
