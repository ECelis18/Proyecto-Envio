package org.example.infraestructura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private String url = "jdbc:h2:~/envioDB";
    private String usuario = "sa";
    private String contraseña = "";

    public Connection conectar(){
        try{
            Connection conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("✔ Conexión establecida");
            return conexion;
        }catch(Exception e){
            System.out.println("❌ Error conectando: " + e.getMessage());
            return null;
        }
    }
}
