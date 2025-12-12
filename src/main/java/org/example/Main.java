package org.example;

import org.example.Controller.EnvioController;
import org.example.infraestructura.ConexionBD;
import org.example.Model.Envio;
import org.h2.tools.Server;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EnvioController envioController = new EnvioController();
    private static final ConexionBD conexionBD = new ConexionBD();

    public static void main(String[] args) throws Exception {
        // Inicializar base de datos
        conexionBD.inicializarTodasLasTablas();

        // Consola H2 en http://localhost:8082
        Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();

        System.out.println("🚚 SISTEMA DE GESTIÓN DE ENVÍOS - GLOBAL INTEGRADOR");
        System.out.println("===================================================\n");

        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            int opcion = obtenerOpcion();

            switch (opcion) {
                case 1: crearEnvio(); break;
                case 2: buscarEnvio(); break;
                case 3: listarTodosEnvios(); break;
                case 4: actualizarEnvio(); break;
                case 5: eliminarEnvio(); break;
                case 0:
                    continuar = false;
                    System.out.println("\n👋 ¡Gracias por usar el sistema de envíos!");
                    break;
                default:
                    System.out.println("❌ Opción no válida. Intente de nuevo.");
            }

            if (continuar) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. 📦 Crear nuevo envío");
        System.out.println("2. 🔍 Buscar envío por ID");
        System.out.println("3. 📋 Listar todos los envíos");
        System.out.println("4. ✏️ Actualizar envío completo");
        System.out.println("5. 🗑️ Eliminar envío por ID");
        System.out.println("0. ❌ Salir del sistema");
        System.out.print("\nSeleccione una opción: ");
    }

    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void crearEnvio() {
        System.out.println("\n=== CREAR NUEVO ENVÍO ===");

        try {
            System.out.print("ID del paquete: ");
            int idPaquete = Integer.parseInt(scanner.nextLine());

            System.out.print("ID del usuario: ");
            int idUsuario = Integer.parseInt(scanner.nextLine());

            System.out.print("Nombre y apellido del cliente: ");
            String nombreCliente = scanner.nextLine();

            System.out.print("Dirección destino: ");
            String direccion = scanner.nextLine();

            System.out.print("Estado (PENDIENTE/EN_TRANSITO/ENTREGADO/CANCELADO): ");
            String estado = scanner.nextLine();
            if (estado.isEmpty()) estado = "PENDIENTE";

            System.out.print("Costo de envío: ");
            double costo = Double.parseDouble(scanner.nextLine());

            // Crear objeto Envio
            Envio nuevoEnvio = new Envio();
            nuevoEnvio.setIdPaquete(idPaquete);
            nuevoEnvio.setIdUsuario(idUsuario);
            nuevoEnvio.setNombreUsuario(nombreCliente);
            nuevoEnvio.setDireccionDestino(direccion);
            nuevoEnvio.setEstado(estado);
            nuevoEnvio.setFechaEnvio(LocalDate.now());
            nuevoEnvio.setCostoEnvio(costo);

            // Llamar al controlador
            Integer idGenerado = envioController.crearEnvio(nuevoEnvio);

            if (idGenerado != null) {
                System.out.println("\n✅ ¡Envío creado exitosamente!");
                System.out.println("📌 ID asignado: " + idGenerado);
                System.out.println("👤 Cliente: " + nombreCliente);
            } else {
                System.out.println("\n❌ Error al crear el envío");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese valores numéricos válidos");
        }
    }

    private static void buscarEnvio() {
        System.out.println("\n=== BUSCAR ENVÍO POR ID ===");

        try {
            System.out.print("Ingrese el ID del envío: ");
            int id = Integer.parseInt(scanner.nextLine());

            Envio envio = envioController.buscarEnvio(id);

            if (envio != null) {
                mostrarDetallesEnvio(envio);
            } else {
                System.out.println("❌ No se encontró ningún envío con ID: " + id);
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese un ID válido");
        }
    }

    private static void listarTodosEnvios() {
        System.out.println("\n=== LISTADO DE TODOS LOS ENVÍOS ===");

        List<Envio> envios = envioController.listarTodosEnvios();

        if (envios.isEmpty()) {
            System.out.println("📭 No hay envíos registrados en el sistema");
        } else {
            System.out.println("📊 Total de envíos: " + envios.size());
            System.out.println("\n" + "-".repeat(100));

            for (Envio envio : envios) {
                System.out.printf("ID: %-5d | Cliente: %-20s | Paquete: %-5d | Estado: %-12s\n",
                        envio.getId(), envio.getNombreUsuario(), envio.getIdPaquete(), envio.getEstado());
                System.out.printf("Destino: %-30s | Fecha: %s | Costo: $%-8.2f\n",
                        envio.getDireccionDestino(), envio.getFechaEnvio(), envio.getCostoEnvio());
                System.out.println("-".repeat(100));
            }
        }
    }

    private static void actualizarEnvio() {
        System.out.println("\n=== ACTUALIZAR ENVÍO COMPLETO ===");

        try {
            System.out.print("ID del envío a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Primero buscar el envío existente
            Envio envioExistente = envioController.buscarEnvio(id);

            if (envioExistente == null) {
                System.out.println("❌ No se encontró el envío con ID: " + id);
                return;
            }

            System.out.println("\nEnvío actual:");
            mostrarDetallesEnvio(envioExistente);
            System.out.println("\nIngrese los nuevos valores (dejar vacío para mantener actual):");

            System.out.print("ID del paquete [" + envioExistente.getIdPaquete() + "]: ");
            String idPaqueteStr = scanner.nextLine();
            int idPaquete = idPaqueteStr.isEmpty() ? envioExistente.getIdPaquete() : Integer.parseInt(idPaqueteStr);

            System.out.print("ID del usuario [" + envioExistente.getIdUsuario() + "]: ");
            String idUsuarioStr = scanner.nextLine();
            int idUsuario = idUsuarioStr.isEmpty() ? envioExistente.getIdUsuario() : Integer.parseInt(idUsuarioStr);

            System.out.print("Nombre del cliente [" + envioExistente.getNombreUsuario() + "]: ");
            String nombreCliente = scanner.nextLine();
            if (nombreCliente.isEmpty()) nombreCliente = envioExistente.getNombreUsuario();

            System.out.print("Dirección destino [" + envioExistente.getDireccionDestino() + "]: ");
            String direccion = scanner.nextLine();
            if (direccion.isEmpty()) direccion = envioExistente.getDireccionDestino();

            System.out.print("Estado [" + envioExistente.getEstado() + "]: ");
            String estado = scanner.nextLine();
            if (estado.isEmpty()) estado = envioExistente.getEstado();

            System.out.print("Costo de envío [" + envioExistente.getCostoEnvio() + "]: ");
            String costoStr = scanner.nextLine();
            double costo = costoStr.isEmpty() ? envioExistente.getCostoEnvio() : Double.parseDouble(costoStr);

            // Crear objeto actualizado
            Envio envioActualizado = new Envio();
            envioActualizado.setId(id);
            envioActualizado.setIdPaquete(idPaquete);
            envioActualizado.setIdUsuario(idUsuario);
            envioActualizado.setNombreUsuario(nombreCliente);
            envioActualizado.setDireccionDestino(direccion);
            envioActualizado.setEstado(estado);
            envioActualizado.setFechaEnvio(envioExistente.getFechaEnvio());
            envioActualizado.setCostoEnvio(costo);

            // Si está ENTREGADO, agregar fecha de entrega
            if (estado.equals("ENTREGADO") && envioExistente.getFechaEntrega() == null) {
                envioActualizado.setFechaEntrega(LocalDate.now());
            } else {
                envioActualizado.setFechaEntrega(envioExistente.getFechaEntrega());
            }

            boolean actualizado = envioController.actualizarEnvio(envioActualizado);

            if (actualizado) {
                System.out.println("\n✅ Envío actualizado correctamente");
                System.out.println("\nEnvío actualizado:");
                Envio envioVerificado = envioController.buscarEnvio(id);
                mostrarDetallesEnvio(envioVerificado);
            } else {
                System.out.println("\n❌ Error al actualizar el envío");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese valores válidos");
        }
    }

    private static void eliminarEnvio() {
        System.out.println("\n=== ELIMINAR ENVÍO ===");

        try {
            System.out.print("ID del envío a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Primero mostrar información del envío
            Envio envioExistente = envioController.buscarEnvio(id);
            if (envioExistente != null) {
                System.out.println("Cliente: " + envioExistente.getNombreUsuario());
                System.out.println("Destino: " + envioExistente.getDireccionDestino());
            }

            System.out.print("¿Está seguro de eliminar este envío? (S/N): ");
            String confirmacion = scanner.nextLine().toUpperCase();

            if (confirmacion.equals("S")) {
                boolean eliminado = envioController.eliminarEnvio(id);

                if (eliminado) {
                    System.out.println("✅ Envío eliminado correctamente");
                } else {
                    System.out.println("❌ Error al eliminar el envío");
                }
            } else {
                System.out.println("⚠️ Operación cancelada");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese un ID válido");
        }
    }

    private static void mostrarDetallesEnvio(Envio envio) {
        if (envio == null) return;

        System.out.println("\n📄 DETALLES DEL ENVÍO");
        System.out.println("=====================");
        System.out.println("ID: " + envio.getId());
        System.out.println("ID Paquete: " + envio.getIdPaquete());
        System.out.println("ID Usuario: " + envio.getIdUsuario());
        System.out.println("Cliente: " + envio.getNombreUsuario());
        System.out.println("Dirección destino: " + envio.getDireccionDestino());
        System.out.println("Estado: " + envio.getEstado());
        System.out.println("Fecha envío: " + envio.getFechaEnvio());
        System.out.println("Fecha entrega: " +
                (envio.getFechaEntrega() != null ? envio.getFechaEntrega() : "Pendiente"));
        System.out.println("Costo envío: $" + String.format("%.2f", envio.getCostoEnvio()));
        System.out.println("=====================");
    }
}