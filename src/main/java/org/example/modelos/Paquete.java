package org.example.modelos;

public class Paquete {

    private Integer id;
    private String descripcion;
    private String estado; // "CREADO", "ASIGNADO", etc.

    public Paquete() {}

    public Paquete(Integer id, String descripcion, String estado) {
            this.id = id;
            this.descripcion = descripcion;
            this.estado = estado;
    }

    public  Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

}
