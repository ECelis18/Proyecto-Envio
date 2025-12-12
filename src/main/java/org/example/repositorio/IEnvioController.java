package org.example.repositorio;

import org.example.Model.Envio;

public interface IEnvioController {
    Integer crearEnvio(Envio envio);
    Envio buscarEnvio(Integer id);
    Boolean actualizarEnvio(Envio envio);
    Boolean eliminarEnvio(Integer id);
}