package org.example.modelos;

public class Envio {
    private Integer id;
    private Integer idPaquete;
    private Integer idRuta;
    private String estado;

    public Envio() {}

    public Envio(Integer idPaquete, Integer idRuta, String estado) {
        this.idPaquete = idPaquete;
        this.idRuta = idRuta;
        this.estado = estado;
    }

    public Integer getIdPaquete() { return idPaquete; }
    public Integer getIdRuta() { return idRuta; }
    public String getEstado() { return estado; }
}
