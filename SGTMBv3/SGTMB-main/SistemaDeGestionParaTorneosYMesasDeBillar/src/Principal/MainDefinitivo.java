package Principal;

import EstructurasLineales.ColaEnlazada;
import EstructurasLineales.ListaDobleEnlazada;
import EstructurasLineales.PilaEnlazada;
import EstructurasNoLineales.ArbolAVL;
import Exceptions.ExceptionIsEmpty;
import Exceptions.ItemDuplicated;
import Exceptions.ItemNoFound;
import Modelos.Jugador;
import Modelos.Mesa;
import Modelos.Producto;
import Optimizacion.MotorAlgoritmos;
import SystemUtilities.GestorMesas; // Ajusta a Modelos.GestorMesas si es necesario

import java.util.Scanner;

public class MainDefinitivo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcionPrincipal = 0;

        // ==========================================
        // 1. INICIALIZACIÓN DE TODAS LAS ESTRUCTURAS
        // ==========================================
        GestorMesas gestorMesas = new GestorMesas();
        ArbolAVL<String> arbolMiembros = new ArbolAVL<>();
        ListaDobleEnlazada<String> libroDiario = new ListaDobleEnlazada<>();
        ColaEnlazada<String> colaEspera = new ColaEnlazada<>();
        PilaEnlazada<String> historialTorneo = new PilaEnlazada<>();

        Producto[] menu = {
            new Producto("Jarra de Cerveza Artesanal", 20, 12),
            new Producto("Pizza Personal", 15, 7),
            new Producto("Porción de Tequeños", 10, 8),
            new Producto("Gaseosa 1L", 8, 3),
            new Producto("Nachos con Queso", 12, 6)
        };
        
        int[] cantidadesVendidas = new int[menu.length];

        try {
            arbolMiembros.insert("MBR-005 (Carlos)");
            arbolMiembros.insert("MBR-002 (Ana)");
            arbolMiembros.insert("MBR-008 (Luis)");
        } catch (ItemDuplicated e) {
            System.out.println("Error interno inicializando datos: " + e.getMessage());
        }

        // ==========================================
        // 2. BUCLE DEL MENÚ PRINCIPAL
        // ==========================================
        do {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║            SISTEMA INTEGRAL - SALÓN DE BILLAR v3.0            ║");
            System.out.println("╠═══════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. Control de Mesas y Recepción     [Arreglos & Colas FIFO]  ║");
            System.out.println("║  2. Gestión de Miembros VIP          [Árbol AVL Balanceado]   ║");
            System.out.println("║  3. Punto de Venta y Auditoría       [Lista Doblemente Enl.]  ║");
            System.out.println("║  4. Torneos y Optimización           [Pilas, DP, Merge Sort]  ║");
            System.out.println("║  5. Apagar Sistema                                            ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");
            System.out.print(">> Seleccione un módulo: ");

            try {
                opcionPrincipal = Integer.parseInt(scanner.nextLine());

                switch (opcionPrincipal) {
                    // ----------------------------------------------------
                    // MÓDULO 1: MESAS Y COLAS DE ESPERA
                    // ----------------------------------------------------
                    case 1:
                        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
                        System.out.println("│               -- CONTROL DE MESAS Y ESPERA --               │");
                        System.out.println("├─────────────────────────────────────────────────────────────┤");
                        System.out.println("│ a. Ver estado del salón            [Lectura Lineal O(n)]    │");
                        System.out.println("│ b. Asignar primera mesa libre      [Búsqueda / Enqueue O(1)]│");
                        System.out.println("│ c. Liberar mesa y cobrar           [Acceso Índice O(1)]     │");
                        System.out.println("│ d. Ver lista de espera actual      [Lectura Cola O(1)]      │");
                        System.out.println("└─────────────────────────────────────────────────────────────┘");
                        System.out.print("Opción: ");
                        String opMesa = scanner.nextLine().toLowerCase();

                        if (opMesa.equals("a")) {
                            gestorMesas.mostrarEstadoSalon();
                        } else if (opMesa.equals("b")) {
                            System.out.print("Ingrese nombre del cliente o grupo: ");
                            String cliente = scanner.nextLine();
                            System.out.print("Ingrese hora de inicio (HH:mm): ");
                            String hora = scanner.nextLine();
                            
                            try {
                                Mesa asignada = gestorMesas.asignarPrimeraMesaLibre(hora);
                                System.out.println("✔ ÉXITO: Se asignó la Mesa N° " + asignada.getNumero() + " a " + cliente + " [Actualización Arreglo O(1)]");
                            } catch (ItemNoFound e) {
                                System.out.println("\n[!] " + e.getMessage());
                                System.out.println("-> Enviando a " + cliente + " a la lista de espera... [Cola FIFO: enqueue() O(1)]");
                                colaEspera.enqueue(cliente);
                                System.out.println("   Clientes en espera: " + colaEspera.getTamaño());
                            }
                        } else if (opMesa.equals("c")) {
                            System.out.print("Ingrese número de mesa a liberar: ");
                            int num = Integer.parseInt(scanner.nextLine());
                            System.out.print("Ingrese hora de fin (HH:mm): ");
                            String horaFin = scanner.nextLine();
                            try {
                                gestorMesas.liberarMesa(num, horaFin);
                                libroDiario.insertarAlFinal("TXN-MESA-" + num + ": Pago registrado a las " + horaFin);
                                System.out.println("   [Auditoría: Registro guardado al final de Lista Doble O(1)]");
                                
                                if (!colaEspera.isEmpty()) {
                                    String siguienteCliente = colaEspera.dequeue();
                                    System.out.println("\n🔔 AVISO: La mesa " + num + " está disponible ahora para: " + siguienteCliente + " [Cola FIFO: dequeue() O(1)]");
                                }
                                
                            } catch (ItemNoFound | ExceptionIsEmpty e) {
                                System.out.println("[!] " + e.getMessage());
                            }
                        } else if (opMesa.equals("d")) {
                             System.out.println("\n-- LISTA DE ESPERA --");
                             if (colaEspera.isEmpty()) {
                                 System.out.println("No hay clientes en espera.");
                             } else {
                                 try {
                                     System.out.println("Siguiente en pasar: " + colaEspera.front() + " [Cola FIFO: front() O(1)]");
                                     System.out.println("Total en espera: " + colaEspera.getTamaño());
                                 } catch(ExceptionIsEmpty e) {}
                             }
                        }
                        break;

                    // ----------------------------------------------------
                    // MÓDULO 2: MIEMBROS (AVL)
                    // ----------------------------------------------------
                    case 2:
                        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
                        System.out.println("│             -- BASE DE DATOS DE MIEMBROS VIP --             │");
                        System.out.println("├─────────────────────────────────────────────────────────────┤");
                        System.out.println("│ a. Registrar nuevo miembro     [Inserción & Balanceo O(log n)]│");
                        System.out.println("│ b. Buscar miembro por ID       [Búsqueda Binaria O(log n)]    │");
                        System.out.println("└─────────────────────────────────────────────────────────────┘");
                        System.out.print("Opción: ");
                        String opMiembro = scanner.nextLine().toLowerCase();

                        if (opMiembro.equals("a")) {
                            System.out.print("Ingrese ID y Nombre (ej. MBR-009 (Pedro)): ");
                            String nuevo = scanner.nextLine();
                            try {
                                arbolMiembros.insert(nuevo);
                                System.out.println("✔ ÉXITO: Miembro insertado. [Árbol AVL balanceado automáticamente]");
                            } catch (ItemDuplicated e) {
                                System.out.println("[!] Error: " + e.getMessage());
                            }
                        } else if (opMiembro.equals("b")) {
                            System.out.print("Ingrese ID exacto a buscar: ");
                            String busqueda = scanner.nextLine();
                            try {
                                String encontrado = arbolMiembros.search(busqueda);
                                System.out.println("✔ ÉXITO: Miembro encontrado -> " + encontrado + " [Ruta de búsqueda O(log n)]");
                            } catch (ItemNoFound e) {
                                System.out.println("[!] Búsqueda fallida: " + e.getMessage());
                            }
                        }
                        break;

                    // ----------------------------------------------------
                    // MÓDULO 3: CAJA Y AUDITORÍA
                    // ----------------------------------------------------
                    case 3:
                        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
                        System.out.println("│                -- PUNTO DE VENTA Y CAJA --                  │");
                        System.out.println("├─────────────────────────────────────────────────────────────┤");
                        System.out.println("│ a. Venta de Cafetería          [Arreglo Frecuencias O(1)]   │");
                        System.out.println("│ b. Recorrer Caja Adelante      [Lista Doble: Head->Tail O(n)]│");
                        System.out.println("│ c. Recorrer Caja Atrás         [Lista Doble: Tail->Head O(n)]│");
                        System.out.println("│ d. Producto Estrella (Moda)    [Cálculo Moda Est. O(n)]     │");
                        System.out.println("└─────────────────────────────────────────────────────────────┘");
                        System.out.print("Opción: ");
                        String opAuditoria = scanner.nextLine().toLowerCase();

                        try {
                            if (opAuditoria.equals("a")) {
                                System.out.println("\n--- PRODUCTOS DISPONIBLES ---");
                                for (int i = 0; i < menu.length; i++) {
                                    System.out.println((i + 1) + ". " + menu[i].getNombre() + " (S/" + menu[i].getPrecioVenta() + ")");
                                }
                                System.out.print("Seleccione el producto a vender (1-" + menu.length + "): ");
                                int index = Integer.parseInt(scanner.nextLine()) - 1;
                                
                                if (index >= 0 && index < menu.length) {
                                    Producto p = menu[index];
                                    cantidadesVendidas[index]++; // Complejidad O(1)
                                    libroDiario.insertarAlFinal("TXN-CAJA: Venta de " + p.getNombre() + " -> Ingreso: S/" + p.getPrecioVenta());
                                    System.out.println("✔ ÉXITO: S/" + p.getPrecioVenta() + " ingresados a la caja. [Freq++ O(1) | Append Node O(1)]");
                                } else {
                                    System.out.println("[!] Selección inválida.");
                                }
                            } else if (opAuditoria.equals("b")) {
                                System.out.println("\n--- HISTORIAL DE TRANSACCIONES (ASCENDENTE O(n)) ---");
                                libroDiario.imprimirHaciaAdelante();
                            } else if (opAuditoria.equals("c")) {
                                System.out.println("\n--- HISTORIAL DE TRANSACCIONES (DESCENDENTE O(n)) ---");
                                libroDiario.imprimirHaciaAtras();
                            } else if (opAuditoria.equals("d")) {
                                System.out.println("\n--- REPORTE DE FRECUENCIA DE VENTAS ---");
                                int maxVentas = 0;
                                int indiceModa = -1;
                                
                                for (int i = 0; i < menu.length; i++) {
                                    System.out.println(menu[i].getNombre() + ": " + cantidadesVendidas[i] + " unidades");
                                    if (cantidadesVendidas[i] > maxVentas) {
                                        maxVentas = cantidadesVendidas[i];
                                        indiceModa = i;
                                    }
                                }
                                System.out.println("---------------------------------------");
                                if (maxVentas > 0) {
                                    System.out.println("🌟 PRODUCTO ESTRELLA (MODA): " + menu[indiceModa].getNombre() + " con " + maxVentas + " ventas.");
                                    System.out.println("   [Calculado mediante barrido secuencial en el arreglo paralelo O(n)]");
                                } else {
                                    System.out.println("Aún no se han registrado ventas.");
                                }
                            }
                        } catch (ExceptionIsEmpty e) {
                            System.out.println("[!] El libro diario aún no tiene transacciones registradas hoy.");
                        }
                        break;

                    // ----------------------------------------------------
                    // MÓDULO 4: OPTIMIZACIÓN Y TORNEOS
                    // ----------------------------------------------------
                    case 4:
                        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
                        System.out.println("│              -- INTELIGENCIA Y OPTIMIZACIÓN --              │");
                        System.out.println("├─────────────────────────────────────────────────────────────┤");
                        System.out.println("│ a. Asistente Ventas Optimizadas  [Knapsack DP 0/1]          │");
                        System.out.println("│ b. Ingresar Puntaje de Torneo    [Pila LIFO: Push() O(1)]   │");
                        System.out.println("│ c. Deshacer Último Puntaje       [Pila LIFO: Pop() O(1)]    │");
                        System.out.println("│ d. Cerrar Torneo y Ver Ranking   [Merge Sort O(n log n)]    │");
                        System.out.println("└─────────────────────────────────────────────────────────────┘");
                        System.out.print("Opción: ");
                        String opInteligencia = scanner.nextLine().toLowerCase();

                        if (opInteligencia.equals("a")) {
                            System.out.print("Ingrese presupuesto exacto del cliente: S/ ");
                            int presupuesto = Integer.parseInt(scanner.nextLine());
                            System.out.println("\n[Ejecutando Programación Dinámica - Creando matriz de memorización...]");
                            MotorAlgoritmos.sugerirComboOptimo(menu, presupuesto);
                            
                        } else if (opInteligencia.equals("b")) {
                            System.out.print("Ingrese el nombre y puntaje del jugador (Ej: Pedro - 150): ");
                            String registro = scanner.nextLine();
                            historialTorneo.push(registro);
                            System.out.println("✔ Puntaje apilado en el historial. [Push a Pila Enlazada]");
                            
                        } else if (opInteligencia.equals("c")) {
                            try {
                                String deshecho = historialTorneo.pop();
                                System.out.println("⎌ Acción deshecha con éxito: Se eliminó -> '" + deshecho + "' [Pop de Pila LIFO]");
                            } catch (ExceptionIsEmpty e) {
                                System.out.println("[!] " + e.getMessage());
                            }
                        } else if (opInteligencia.equals("d")) {
                             System.out.println("\n[!] Simulando torneo con arreglo de 4 jugadores...");
                             Jugador[] torneo = {
                                new Jugador("ShadowSniper", 150),
                                new Jugador("ReyBillar", 300),
                                new Jugador("Zeta", 150), 
                                new Jugador("Arthur", 450)
                            };
                            
                            System.out.println("[Aplicando algoritmo Divide y Vencerás (Merge Sort O(n log n))...]");
                            MotorAlgoritmos.mergeSort(torneo, 0, torneo.length - 1);
                            
                            System.out.println("\n>> LEADERBOARD OFICIAL:");
                            for (int i = 0; i < torneo.length; i++) {
                                System.out.println("   #" + (i + 1) + " " + torneo[i]);
                            }
                            System.out.println("   [Empates resueltos garantizando estabilidad con compareTo()]");
                            libroDiario.insertarAlFinal("TXN-TORNEO: Leaderboard ordenado procesado.");
                        }
                        break;

                    case 5:
                        System.out.println("\n💾 Guardando estado en estructuras dinámicas...");
                        System.out.println("   Apagando sistema administrativo. ¡Buen turno!");
                        break;

                    default:
                        System.out.println("[!] Opción inválida. Digite un número del 1 al 5.");
                }

            } catch (NumberFormatException e) {
                System.out.println("\n[!] Formato incorrecto. Por favor ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("\n[!] Ha ocurrido un error inesperado: " + e.getMessage());
            }

        } while (opcionPrincipal != 5);

        scanner.close();
    }
}