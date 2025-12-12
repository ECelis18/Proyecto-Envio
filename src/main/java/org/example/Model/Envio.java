package org.example.Model;
import java.time.LocalDate;

public class Envio {
    private Integer id;
    private Integer idPaquete;
    private Integer idUsuario;
    private String nombreUsuario;  // NUEVO CAMPO
    private String direccionDestino;
    private String estado;
    private LocalDate fechaEnvio;
    private LocalDate fechaEntrega;
    private Double costoEnvio;

    // Constructores, getters y setters
    public Envio() {}

    public Envio(Integer id, Integer idPaquete, Integer idUsuario, String nombreUsuario,
                 String direccionDestino, String estado, LocalDate fechaEnvio,
                 LocalDate fechaEntrega, Double costoEnvio) {
        this.id = id;
        this.idPaquete = idPaquete;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.direccionDestino = direccionDestino;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntrega = fechaEntrega;
        this.costoEnvio = costoEnvio;
    }



    // Getters y Setters (todos)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdPaquete() { return idPaquete; }
    public void setIdPaquete(Integer idPaquete) { this.idPaquete = idPaquete; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDate fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public Double getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(Double costoEnvio) { this.costoEnvio = costoEnvio; }



}
