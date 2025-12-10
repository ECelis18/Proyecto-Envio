package org.example;

import org.example.infraestructura.ConexionBD;
import org.h2.tools.Server;

public class Main {
    public static void main(String[] args) throws Exception{

        // Consola H2 en http://localhost:8082
        Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();

        //Activando la conexion
        ConexionBD conexion = new ConexionBD();
        //Creando las tablas
        //new SchemaCreator().crearTablas();
    }
}