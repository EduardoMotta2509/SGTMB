package Principal;

import Controladores.ControladorBillar;
import Exceptions.ExceptionIsEmpty;
import Exceptions.ItemDuplicated;
import Exceptions.ItemNoFound;
import Modelos.Jugador;
import Modelos.Mesa;
import Modelos.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class VistaBillarSwing extends JFrame {

    private ControladorBillar controlador;
    private JTextArea consolaVisual;

    public VistaBillarSwing() {
        controlador = new ControladorBillar();

        setTitle(" Sistema Integral - Salón de Billar (MVC & Estructuras de Datos)");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Pestañas
        JTabbedPane panelPestanas = new JTabbedPane();
        panelPestanas.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelPestanas.addTab(" Control de Mesas [Arreglos & Colas]", crearPanelMesas());
        panelPestanas.addTab(" Miembros VIP [Árbol AVL]", crearPanelMiembros());
        panelPestanas.addTab(" Caja y Auditoría [Lista Doble]", crearPanelCaja());
        panelPestanas.addTab(" Inteligencia [Pilas, DP, Merge]", crearPanelOptimizacion());

        add(panelPestanas, BorderLayout.NORTH);

        // 2. Consola Visual
        consolaVisual = new JTextArea();
        consolaVisual.setEditable(false);
        consolaVisual.setBackground(new Color(30, 30, 30));
        consolaVisual.setForeground(new Color(0, 255, 0));
        consolaVisual.setFont(new Font("Consolas", Font.PLAIN, 14));
        consolaVisual.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollConsola = new JScrollPane(consolaVisual);
        scrollConsola.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                "Terminal del Sistema (Logs y Trazas Algorítmicas)", 
                TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 12), Color.BLACK));
        
        add(scrollConsola, BorderLayout.CENTER);

        // 3. Redirigir la consola
        redirigirConsola();
        
        System.out.println("===============================================================");
        System.out.println("  SISTEMA INICIADO - ARQUITECTURA MVC APLICADA CORRECTAMENTE");
        System.out.println("  Estructuras instanciadas: Arreglos, Colas, Pilas, AVL, Listas");
        System.out.println("===============================================================\n");
    }

    private JPanel crearPanelMesas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnEstado = new JButton("Ver Estado Salón");
        JButton btnAsignar = new JButton("Asignar Mesa");
        JButton btnLiberar = new JButton("Liberar Mesa");
        JButton btnEspera = new JButton("Ver Lista de Espera");

        btnEstado.addActionListener(e -> {
            System.out.println("\n▶ [Lectura Lineal O(n)] -> Recorriendo arreglo estático...");
            controlador.getGestorMesas().mostrarEstadoSalon();
        });

        btnAsignar.addActionListener(e -> {
            String cliente = JOptionPane.showInputDialog(this, "Nombre del Cliente/Grupo:");
            if (cliente == null || cliente.trim().isEmpty()) return;
            String hora = JOptionPane.showInputDialog(this, "Hora de inicio (HH:mm):");
            if (hora == null || hora.trim().isEmpty()) return;

            try {
                System.out.println("\n▶ [Búsqueda O(n) & Actualización O(1)] -> Asignando mesa...");
                Mesa asignada = controlador.asignarMesa(cliente, hora);
                System.out.println("✔ ÉXITO: Se asignó la Mesa N° " + asignada.getNumero() + " a " + cliente);
            } catch (ItemNoFound ex) {
                System.out.println("\n[!] " + ex.getMessage());
                System.out.println("▶ [Cola FIFO: enqueue() O(1)] -> Enviando a la lista de espera.");
                controlador.encolarCliente(cliente);
                System.out.println("✔ Clientes en espera: " + controlador.getTamañoCola());
            }
        });

        btnLiberar.addActionListener(e -> {
            try {
                String numStr = JOptionPane.showInputDialog(this, "Número de Mesa a liberar:");
                if (numStr == null) return;
                int num = Integer.parseInt(numStr);
                String horaFin = JOptionPane.showInputDialog(this, "Hora de fin (HH:mm):");
                if (horaFin == null) return;

                System.out.println("\n▶ [Acceso por Índice O(1)] -> Calculando tarifa y liberando...");
                String clienteEnEspera = controlador.liberarMesa(num, horaFin);
                System.out.println("✔ [Auditoría: Nodo añadido al final de Lista Doble O(1)]");

                if (clienteEnEspera != null) {
                    System.out.println("\n🔔 AVISO [Cola FIFO: dequeue() O(1)]: La mesa " + num + " está disponible para -> " + clienteEnEspera);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número de mesa inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ItemNoFound ex) {
                System.out.println("\n[!] " + ex.getMessage());
            }
        });

        btnEspera.addActionListener(e -> {
            System.out.println("\n▶ [Lectura de Cola O(1)] -> Consultando front()...");
            if (controlador.colaEsperaVacia()) {
                System.out.println("No hay clientes en espera.");
            } else {
                try {
                    System.out.println("Siguiente en pasar: " + controlador.verSiguienteEnEspera());
                    System.out.println("Total en espera: " + controlador.getTamañoCola());
                } catch (ExceptionIsEmpty ex) {}
            }
        });

        panel.add(btnEstado);
        panel.add(btnAsignar);
        panel.add(btnLiberar);
        panel.add(btnEspera);
        return panel;
    }

    private JPanel crearPanelMiembros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnRegistrar = new JButton("Registrar Miembro");
        JButton btnBuscar = new JButton("Buscar Miembro (ID)");
        JButton btnVerArbol = new JButton("Visualizar Árbol AVL");

        btnRegistrar.addActionListener(e -> {
            JTextField txtId = new JTextField(10);
            JTextField txtNombre = new JTextField(20);
            
            JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
            panelFormulario.add(new JLabel("ID del Miembro (ej. MBR-010):"));
            panelFormulario.add(txtId);
            panelFormulario.add(new JLabel("Nombre Completo:"));
            panelFormulario.add(txtNombre);

            int resultado = JOptionPane.showConfirmDialog(this, panelFormulario, 
                    "📋 Formulario de Nuevo Miembro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (resultado == JOptionPane.OK_OPTION) {
                String id = txtId.getText().trim();
                String nombre = txtNombre.getText().trim();
                
                if (!id.isEmpty() && !nombre.isEmpty()) {
                    try {
                        System.out.println("\n▶ [Inserción & Balanceo O(log n)] -> Insertando objeto Miembro en AVL...");
                        controlador.registrarMiembro(id, nombre);
                        System.out.println("✔ ÉXITO: Miembro [" + nombre + "] insertado. Árbol re-balanceado automáticamente.");
                    } catch (ItemDuplicated ex) {
                        System.out.println("\n[!] Error: " + ex.getMessage());
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Duplicidad", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Debe llenar todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnBuscar.addActionListener(e -> {
            String busqueda = JOptionPane.showInputDialog(this, "Ingrese ID exacto a buscar:");
            if (busqueda != null && !busqueda.isEmpty()) {
                try {
                    System.out.println("\n▶ [Búsqueda Binaria O(log n)] -> Recorriendo ramificaciones del AVL...");
                    String encontrado = controlador.buscarMiembro(busqueda);
                    System.out.println("✔ ÉXITO: Encontrado -> " + encontrado);
                } catch (ItemNoFound ex) {
                    System.out.println("\n[!] Búsqueda fallida: " + ex.getMessage());
                }
            }
        });

        btnVerArbol.addActionListener(e -> {
            System.out.println("\n▶ [Recorrido In-Order Inverso O(n)] -> Generando mapa topológico del Árbol AVL...");
            String estructura = controlador.obtenerEstructuraMiembros();
            
            JTextArea areaArbol = new JTextArea(estructura);
            areaArbol.setFont(new Font("Consolas", Font.BOLD, 14));
            areaArbol.setEditable(false);
            areaArbol.setBackground(new Color(240, 248, 255));
            areaArbol.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            JScrollPane scrollArbol = new JScrollPane(areaArbol);
            scrollArbol.setPreferredSize(new Dimension(600, 450));
            
            JOptionPane.showMessageDialog(this, scrollArbol, "🗺️ Estructura Dinámica del Árbol AVL", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("✔ Mapa topológico renderizado en ventana flotante.");
        });

        panel.add(btnRegistrar);
        panel.add(btnBuscar);
        panel.add(btnVerArbol);
        return panel;
    }

    private JPanel crearPanelCaja() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnVender = new JButton("Vender Producto");
        JButton btnAdelante = new JButton("Auditoría (Apertura -> Cierre)");
        JButton btnAtras = new JButton("Auditoría (Cierre -> Apertura)");
        JButton btnModa = new JButton("Reporte Moda (Estrella)");

        btnVender.addActionListener(e -> {
            Producto[] menu = controlador.getMenu();
            String[] opciones = new String[menu.length];
            for (int i = 0; i < menu.length; i++) opciones[i] = menu[i].getNombre() + " (S/" + menu[i].getPrecioVenta() + ")";
            
            String seleccion = (String) JOptionPane.showInputDialog(this, "Seleccione producto:", "Caja", 
                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            
            if (seleccion != null) {
                for (int i = 0; i < opciones.length; i++) {
                    if (opciones[i].equals(seleccion)) {
                        Producto p = controlador.venderProducto(i);
                        System.out.println("\n▶ [Arreglo Frecuencias O(1) | Append Lista O(1)] -> Procesando Venta...");
                        System.out.println("✔ ÉXITO: S/" + p.getPrecioVenta() + " ingresados. Nodo registrado en Libro Diario.");
                        break;
                    }
                }
            }
        });

        btnAdelante.addActionListener(e -> {
            System.out.println("\n▶ [Lista Doble: Recorrido Head -> Tail O(n)]");
            try {
                controlador.getLibroDiario().imprimirHaciaAdelante();
            } catch (ExceptionIsEmpty ex) { System.out.println(ex.getMessage()); }
        });

        btnAtras.addActionListener(e -> {
            System.out.println("\n▶ [Lista Doble: Recorrido Tail -> Head O(n)]");
            try {
                controlador.getLibroDiario().imprimirHaciaAtras();
            } catch (ExceptionIsEmpty ex) { System.out.println(ex.getMessage()); }
        });

        btnModa.addActionListener(e -> {
            System.out.println("\n▶ [Cálculo Estadístico: Barrido Lineal O(n)] -> Analizando frecuencias...");
            System.out.println(controlador.generarReporteModa());
        });

        panel.add(btnVender);
        panel.add(btnAdelante);
        panel.add(btnAtras);
        panel.add(btnModa);
        return panel;
    }

    private JPanel crearPanelOptimizacion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnKnapsack = new JButton("Asistente Ventas (DP)");
        JButton btnPush = new JButton("Apilar Puntaje (Push)");
        JButton btnPop = new JButton("Deshacer Puntaje (Pop)");
        JButton btnMerge = new JButton("Generar Ranking (MergeSort)");

        btnKnapsack.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog(this, "Presupuesto exacto del cliente (S/):");
                if (input == null) return;
                int presupuesto = Integer.parseInt(input);
                System.out.println("\n▶ [Programación Dinámica: Knapsack 0/1] -> Creando matriz de memorización...");
                controlador.ejecutarKnapsack(presupuesto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido.");
            }
        });

        btnPush.addActionListener(e -> {
            String registro = JOptionPane.showInputDialog(this, "Nombre y puntaje (Ej: Pedro - 150):");
            if (registro != null && !registro.trim().isEmpty()) {
                System.out.println("\n▶ [Pila LIFO: Push() O(1)] -> Apilando acción...");
                controlador.registrarPuntajeTorneo(registro);
                System.out.println("✔ Puntaje apilado en el historial de torneo.");
            }
        });

        btnPop.addActionListener(e -> {
            System.out.println("\n▶ [Pila LIFO: Pop() O(1)] -> Revirtiendo última acción...");
            try {
                String deshecho = controlador.deshacerUltimoPuntaje();
                System.out.println("⎌ ÉXITO: Se deshizo el registro -> '" + deshecho + "'");
            } catch (ExceptionIsEmpty ex) {
                System.out.println("[!] " + ex.getMessage());
            }
        });

        btnMerge.addActionListener(e -> {
            System.out.println("\n▶ [Algoritmo Divide y Vencerás: Merge Sort O(n log n)] -> Ordenando...");
            Jugador[] torneo = {
                new Jugador("ShadowSniper", 150),
                new Jugador("ReyBillar", 300),
                new Jugador("Zeta", 150), 
                new Jugador("Arthur", 450)
            };
            Jugador[] leaderboard = controlador.procesarLeaderboard(torneo);
            System.out.println(">> LEADERBOARD OFICIAL (Empates resueltos garantizando estabilidad):");
            for (int i = 0; i < leaderboard.length; i++) {
                System.out.println("   #" + (i + 1) + " " + leaderboard[i]);
            }
        });

        panel.add(btnKnapsack);
        panel.add(btnPush);
        panel.add(btnPop);
        panel.add(btnMerge);
        return panel;
    }

    private void redirigirConsola() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                actualizarConsola(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                actualizarConsola(new String(b, off, len));
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void actualizarConsola(String texto) {
        SwingUtilities.invokeLater(() -> {
            consolaVisual.append(texto);
            consolaVisual.setCaretPosition(consolaVisual.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> {
            VistaBillarSwing ventana = new VistaBillarSwing();
            ventana.setVisible(true);
        });
    }
}