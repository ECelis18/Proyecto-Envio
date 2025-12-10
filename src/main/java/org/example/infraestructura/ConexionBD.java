package org.example.infraestructura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConexionBD {

    private String url = "jdbc:h2:~/envioDB";
    private String usuario = "sa";
    private String contraseña = "";

    public Connection conectar(){
        try{
            Connection conexionAmiBD = DriverManager.getConnection(url,usuario,contraseña);
            System.out.println("Exito conectandonos a la BD");
            return conexionAmiBD;
        }catch(Exception error){
            System.out.println("Error en la conexión"+error.getMessage());
            return null;
        }
    }
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
        try(Connection conn = conectar();
            Statement st = conn.createStatement()){

            st.execute(sql);
            System.out.println("✔ Tablas listas");

        }catch(Exception e){
            System.out.println("❌ Error creando tablas: " + e.getMessage());
        }
    }
}
