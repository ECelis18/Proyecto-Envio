package org.example.repositorio;

import org.example.Model.Envio;

// Interfaz para las operaciones CRUD de Envío
public interface IEnvioController {
    // Crea un nuevo envío y devuelve su ID
    Integer crearEnvio(Envio envio);

    // Busca un envío por su ID
    Envio buscarEnvio(Integer id);

    // Actualiza un envío existente
    Boolean actualizarEnvio(Envio envio);

    // Elimina un envío por su ID
    Boolean eliminarEnvio(Integer id);
}