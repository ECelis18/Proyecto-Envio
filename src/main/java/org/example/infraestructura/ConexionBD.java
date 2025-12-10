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
}
