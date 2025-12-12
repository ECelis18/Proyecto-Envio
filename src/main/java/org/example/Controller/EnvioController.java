package org.example.Controller;

import org.example.Model.Envio;
import org.example.repositorio.IEnvioController;
import org.example.infraestructura.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Controlador que implementa operaciones CRUD para envíos
public class EnvioController implements IEnvioController {

    // Crear un nuevo envío   Sebas
    @Override
    public Integer crearEnvio(Envio envio) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            conexion = new ConexionBD().conectar();
            if (conexion == null) return null;

            // Validaciones
            if (envio.getNombreUsuario() == null || envio.getNombreUsuario().trim().isEmpty()) {
                envio.setNombreUsuario("Cliente " + envio.getIdUsuario());
            }

            if (envio.getDireccionDestino() == null || envio.getDireccionDestino().trim().isEmpty()) {
                System.out.println("Error: La dirección destino es obligatoria");
                return null;
            }

            if (envio.getEstado() == null || envio.getEstado().trim().isEmpty()) {
                envio.setEstado("PENDIENTE");
            }

            // SQL para insertar
            String sql = "INSERT INTO envios (id_usuario, nombre_usuario, direccion_destino, estado, " +
                    "fecha_envio, fecha_entrega, costo_envio) VALUES (?, ?, ?, ?, ?, ?, ?)";

            statement = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Asignar valores
            statement.setInt(1, envio.getIdUsuario());
            statement.setString(2, envio.getNombreUsuario());
            statement.setString(3, envio.getDireccionDestino());
            statement.setString(4, envio.getEstado());
            statement.setDate(5, Date.valueOf(envio.getFechaEnvio()));

            if (envio.getFechaEntrega() != null) {
                statement.setDate(6, Date.valueOf(envio.getFechaEntrega()));
            } else {
                statement.setNull(6, Types.DATE);
            }

            statement.setDouble(7, envio.getCostoEnvio());

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas > 0) {
                generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Integer idGenerado = generatedKeys.getInt(1);
                    System.out.println("✅ Envío creado con ID: " + idGenerado);
                    return idGenerado;
                }
            }

            System.out.println("⚠️ No se pudo crear el envío");
            return null;

        } catch (SQLException e) {
            System.out.println("❌ Error SQL al crear envío: " + e.getMessage());
            return null;
        } finally {
            // Cerrar recursos
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (statement != null) statement.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    // Buscar envío por ID  Sara
    @Override
    public Envio buscarEnvio(Integer id) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;

        try {
            conexion = new ConexionBD().conectar();
            if (conexion == null) return null;

            String sql = "SELECT * FROM envios WHERE id = ?";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, id);

            resultado = statement.executeQuery();

            if (resultado.next()) {
                Envio envio = mapearResultSetAEnvio(resultado);
                System.out.println("✅ Envío encontrado para: " + envio.getNombreUsuario());
                return envio;
            } else {
                System.out.println("⚠️ No se encontró envío con ID: " + id);
                return null;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error SQL al buscar envío: " + e.getMessage());
            return null;
        } finally {
            // Cerrar recursos
            try {
                if (resultado != null) resultado.close();
                if (statement != null) statement.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    // Método para convertir ResultSet a objeto Envio
    private Envio mapearResultSetAEnvio(ResultSet resultado) throws SQLException {
        Envio envio = new Envio();
        envio.setId(resultado.getInt("id"));
        envio.setIdUsuario(resultado.getInt("id_usuario"));
        envio.setNombreUsuario(resultado.getString("nombre_usuario"));
        envio.setDireccionDestino(resultado.getString("direccion_destino"));
        envio.setEstado(resultado.getString("estado"));
        envio.setFechaEnvio(resultado.getDate("fecha_envio").toLocalDate());

        Date fechaEntrega = resultado.getDate("fecha_entrega");
        if (fechaEntrega != null) {
            envio.setFechaEntrega(fechaEntrega.toLocalDate());
        }

        envio.setCostoEnvio(resultado.getDouble("costo_envio"));
        return envio;
    }

    // Actualizar envío completo  Omar
    @Override
    public Boolean actualizarEnvio(Envio envio) {
        Connection conexion = null;
        PreparedStatement statement = null;

        try {
            conexion = new ConexionBD().conectar();
            if (conexion == null) return false;

            if (envio.getId() == null) {
                System.out.println("Error: El ID del envío es requerido para actualizar");
                return false;
            }

            // Validación de nombre
            if (envio.getNombreUsuario() == null || envio.getNombreUsuario().trim().isEmpty()) {
                envio.setNombreUsuario("Cliente " + envio.getIdUsuario());
            }

            String sql = "UPDATE envios SET " +
                    "id_usuario = ?, " +
                    "nombre_usuario = ?, " +
                    "direccion_destino = ?, " +
                    "estado = ?, " +
                    "fecha_envio = ?, " +
                    "fecha_entrega = ?, " +
                    "costo_envio = ? " +
                    "WHERE id = ?";

            statement = conexion.prepareStatement(sql);

            statement.setInt(1, envio.getIdUsuario());
            statement.setString(2, envio.getNombreUsuario());
            statement.setString(3, envio.getDireccionDestino());
            statement.setString(4, envio.getEstado());
            statement.setDate(5, Date.valueOf(envio.getFechaEnvio()));

            if (envio.getFechaEntrega() != null) {
                statement.setDate(6, Date.valueOf(envio.getFechaEntrega()));
            } else {
                statement.setNull(6, Types.DATE);
            }

            statement.setDouble(7, envio.getCostoEnvio());
            statement.setInt(8, envio.getId());

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✅ Envío actualizado para: " + envio.getNombreUsuario());
                return true;
            } else {
                System.out.println("⚠️ No se encontró el envío con ID: " + envio.getId());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error SQL al actualizar envío: " + e.getMessage());
            return false;
        } finally {
            // Cerrar recursos
            try {
                if (statement != null) statement.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    // Eliminar envío por ID   Daniel
    @Override
    public Boolean eliminarEnvio(Integer id) {
        Connection conexion = null;
        PreparedStatement statement = null;

        try {
            conexion = new ConexionBD().conectar();
            if (conexion == null) return false;

            // Verificar si existe antes de eliminar
            Envio envioExistente = buscarEnvio(id);
            if (envioExistente == null) {
                System.out.println("⚠️ No se puede eliminar. Envío con ID " + id + " no existe");
                return false;
            }

            System.out.println("🗑️ Eliminando envío para: " + envioExistente.getNombreUsuario());

            String sql = "DELETE FROM envios WHERE id = ?";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, id);

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✅ Envío eliminado correctamente.");
                return true;
            } else {
                System.out.println("⚠️ No se pudo eliminar el envío con ID: " + id);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error SQL al eliminar envío: " + e.getMessage());
            return false;
        } finally {
            // Cerrar recursos
            try {
                if (statement != null) statement.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    // Método para actualizar solo el estado omar
    public Boolean actualizarEstadoEnvio(Integer idEnvio, String nuevoEstado) {
        Connection conexion = null;
        PreparedStatement statement = null;

        try {
            conexion = new ConexionBD().conectar();
            if (conexion == null) return false;

            String sql = "UPDATE envios SET estado = ? WHERE id = ?";
            statement = conexion.prepareStatement(sql);

            statement.setString(1, nuevoEstado);
            statement.setInt(2, idEnvio);

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✅ Estado actualizado a '" + nuevoEstado + "'");
                return true;
            }

            return false;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado: " + e.getMessage());
            return false;
        } finally {
            // Cerrar recursos
            try {
                if (statement != null) statement.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    // Listar todos los envíos Sara
    public List<Envio> listarTodosEnvios() {
        List<Envio> envios = new ArrayList<>();

        try (Connection conexion = new ConexionBD().conectar();
             Statement statement = conexion.createStatement();
             ResultSet resultado = statement.executeQuery("SELECT * FROM envios")) {

            if (conexion == null) return envios;

            while (resultado.next()) {
                Envio envio = mapearResultSetAEnvio(resultado);
                envios.add(envio);
            }

            System.out.println("📋 Total envíos listados: " + envios.size());

        } catch (SQLException e) {
            System.out.println("Error al listar envíos: " + e.getMessage());
        }

        return envios;
    }
}