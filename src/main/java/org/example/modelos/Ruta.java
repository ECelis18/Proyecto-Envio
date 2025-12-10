package org.example.modelos;

public class Ruta {

    private Integer id;
    private String nombre;
    private Boolean activa;

    public Ruta() {}

    public Ruta(Integer id, String nombre, Boolean activa) {
            this.id = id;
            this.nombre = nombre;
            this.activa = activa;
    }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public Boolean getActiva() { return activa; }
}

