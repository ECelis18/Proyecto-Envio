package org.example;

import org.example.Controller.EnvioController;
import org.example.infraestructura.ConexionBD;
import org.example.Model.Envio;
import org.h2.tools.Server;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

// Clase principal del programa - Menú de envíos
public class Main {
    // Variables globales
    private static final Scanner scanner = new Scanner(System.in);
    private static final EnvioController envioController = new EnvioController();
    private static final ConexionBD conexionBD = new ConexionBD();

    // Método principal
    public static void main(String[] args) throws Exception {
        // Iniciar base de datos
        conexionBD.inicializarTodasLasTablas();

        // Iniciar consola web de H2 (http://localhost:8082)
        Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();

        System.out.println("🚚 SISTEMA DE GESTIÓN DE ENVÍOS");
        System.out.println("================================\n");

        boolean continuar = true;

        // Menú principal
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
                    System.out.println("\n👋 ¡Gracias por usar el sistema!");
                    break;
                default:
                    System.out.println("❌ Opción no válida");
            }

            // Pausa entre operaciones
            if (continuar) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    // Mostrar menú de opciones
    private static void mostrarMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. 📦 Crear envío");
        System.out.println("2. 🔍 Buscar envío");
        System.out.println("3. 📋 Listar todos");
        System.out.println("4. ✏️ Actualizar");
        System.out.println("5. 🗑️ Eliminar");
        System.out.println("0. ❌ Salir");
        System.out.print("\nSeleccione opción: ");
    }

    // Leer opción del usuario
    private static int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Crear nuevo envío
    private static void crearEnvio() {
        System.out.println("\n=== CREAR ENVÍO ===");

        try {
            System.out.print("ID usuario: ");
            int idUsuario = Integer.parseInt(scanner.nextLine());

            System.out.print("Nombre cliente: ");
            String nombreCliente = scanner.nextLine();

            System.out.print("Dirección: ");
            String direccion = scanner.nextLine();

            System.out.print("Estado (PENDIENTE/EN_TRANSITO/ENTREGADO/CANCELADO): ");
            String estado = scanner.nextLine();
            if (estado.isEmpty()) estado = "PENDIENTE";

            System.out.print("Costo: ");
            double costo = Double.parseDouble(scanner.nextLine());

            // Crear objeto envío
            Envio nuevoEnvio = new Envio();
            nuevoEnvio.setIdUsuario(idUsuario);
            nuevoEnvio.setNombreUsuario(nombreCliente);
            nuevoEnvio.setDireccionDestino(direccion);
            nuevoEnvio.setEstado(estado);
            nuevoEnvio.setFechaEnvio(LocalDate.now());
            nuevoEnvio.setCostoEnvio(costo);

            // Guardar en base de datos
            Integer idGenerado = envioController.crearEnvio(nuevoEnvio);

            if (idGenerado != null) {
                System.out.println("\n✅ Envío creado!");
                System.out.println("ID: " + idGenerado);
            } else {
                System.out.println("\n❌ Error al crear envío");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese números válidos");
        }
    }

    // Buscar envío por ID
    private static void buscarEnvio() {
        System.out.println("\n=== BUSCAR ENVÍO ===");

        try {
            System.out.print("ID del envío: ");
            int id = Integer.parseInt(scanner.nextLine());

            Envio envio = envioController.buscarEnvio(id);

            if (envio != null) {
                mostrarDetallesEnvio(envio);
            } else {
                System.out.println("❌ No se encontró envío con ID: " + id);
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Ingrese ID válido");
        }
    }

    // Listar todos los envíos
    private static void listarTodosEnvios() {
        System.out.println("\n=== LISTA DE ENVÍOS ===");

        List<Envio> envios = envioController.listarTodosEnvios();

        if (envios.isEmpty()) {
            System.out.println("📭 No hay envíos");
        } else {
            System.out.println("Total: " + envios.size());
            System.out.println("\n" + "-".repeat(100));

            for (Envio envio : envios) {
                System.out.printf("ID: %-5d | Cliente: %-20s | Estado: %-12s\n",
                        envio.getId(), envio.getNombreUsuario(), envio.getEstado());
                System.out.printf("Destino: %-30s | Fecha: %s\n",
                        envio.getDireccionDestino(), envio.getFechaEnvio());
                System.out.println("-".repeat(100));
            }
        }
    }

    // Actualizar envío existente
    private static void actualizarEnvio() {
        System.out.println("\n=== ACTUALIZAR ENVÍO ===");

        try {
            System.out.print("ID del envío: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Buscar envío
            Envio envioExistente = envioController.buscarEnvio(id);

            if (envioExistente == null) {
                System.out.println("❌ Envío no encontrado");
                return;
            }

            System.out.println("\nEnvío actual:");
            mostrarDetallesEnvio(envioExistente);
            System.out.println("\nNuevos valores (vacío = mantener):");

            // Pedir nuevos datos
            System.out.print("ID usuario [" + envioExistente.getIdUsuario() + "]: ");
            String idUsuarioStr = scanner.nextLine();
            int idUsuario = idUsuarioStr.isEmpty() ? envioExistente.getIdUsuario() : Integer.parseInt(idUsuarioStr);

            System.out.print("Nombre [" + envioExistente.getNombreUsuario() + "]: ");
            String nombreCliente = scanner.nextLine();
            if (nombreCliente.isEmpty()) nombreCliente = envioExistente.getNombreUsuario();

            System.out.print("Dirección [" + envioExistente.getDireccionDestino() + "]: ");
            String direccion = scanner.nextLine();
            if (direccion.isEmpty()) direccion = envioExistente.getDireccionDestino();

            System.out.print("Estado [" + envioExistente.getEstado() + "]: ");
            String estado = scanner.nextLine();
            if (estado.isEmpty()) estado = envioExistente.getEstado();

            System.out.print("Costo [" + envioExistente.getCostoEnvio() + "]: ");
            String costoStr = scanner.nextLine();
            double costo = costoStr.isEmpty() ? envioExistente.getCostoEnvio() : Double.parseDouble(costoStr);

            // Crear envío actualizado
            Envio envioActualizado = new Envio();
            envioActualizado.setId(id);
            envioActualizado.setIdUsuario(idUsuario);
            envioActualizado.setNombreUsuario(nombreCliente);
            envioActualizado.setDireccionDestino(direccion);
            envioActualizado.setEstado(estado);
            envioActualizado.setFechaEnvio(envioExistente.getFechaEnvio());
            envioActualizado.setCostoEnvio(costo);

            // Si está ENTREGADO y no tenía fecha, poner fecha actual
            if (estado.equals("ENTREGADO") && envioExistente.getFechaEntrega() == null) {
                envioActualizado.setFechaEntrega(LocalDate.now());
            } else {
                envioActualizado.setFechaEntrega(envioExistente.getFechaEntrega());
            }

            // Guardar cambios
            boolean actualizado = envioController.actualizarEnvio(envioActualizado);

            if (actualizado) {
                System.out.println("\n✅ Envío actualizado");
            } else {
                System.out.println("\n❌ Error al actualizar");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Valores inválidos");
        }
    }

    // Eliminar envío
    private static void eliminarEnvio() {
        System.out.println("\n=== ELIMINAR ENVÍO ===");

        try {
            System.out.print("ID del envío: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Mostrar info del envío
            Envio envioExistente = envioController.buscarEnvio(id);
            if (envioExistente != null) {
                System.out.println("Cliente: " + envioExistente.getNombreUsuario());
            }

            // Confirmar
            System.out.print("¿Eliminar? (S/N): ");
            String confirmacion = scanner.nextLine().toUpperCase();

            if (confirmacion.equals("S")) {
                boolean eliminado = envioController.eliminarEnvio(id);

                if (eliminado) {
                    System.out.println("✅ Envío eliminado");
                } else {
                    System.out.println("❌ Error al eliminar");
                }
            } else {
                System.out.println("⚠️ Cancelado");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Error: ID inválido");
        }
    }

    // Mostrar detalles de un envío
    private static void mostrarDetallesEnvio(Envio envio) {
        if (envio == null) return;

        System.out.println("\n📄 DETALLES");
        System.out.println("===========");
        System.out.println("ID: " + envio.getId());
        System.out.println("Usuario: " + envio.getIdUsuario());
        System.out.println("Cliente: " + envio.getNombreUsuario());
        System.out.println("Dirección: " + envio.getDireccionDestino());
        System.out.println("Estado: " + envio.getEstado());
        System.out.println("Fecha envío: " + envio.getFechaEnvio());
        System.out.println("Fecha entrega: " + (envio.getFechaEntrega() != null ? envio.getFechaEntrega() : "Pendiente"));
        System.out.println("Costo: $" + String.format("%.2f", envio.getCostoEnvio()));
        System.out.println("===========");
    }
}