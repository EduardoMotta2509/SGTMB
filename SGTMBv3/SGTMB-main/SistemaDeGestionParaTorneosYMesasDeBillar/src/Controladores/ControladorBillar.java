package Controladores;

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
import Modelos.Miembro;
import Optimizacion.MotorAlgoritmos;
import SystemUtilities.GestorMesas; // Ajusta a Modelos.GestorMesas si lo pusiste ahí

public class ControladorBillar {
    private GestorMesas gestorMesas;
    private ArbolAVL<Miembro> arbolMiembros; // Ahora maneja objetos Miembro completos
    private ListaDobleEnlazada<String> libroDiario;
    private ColaEnlazada<String> colaEspera;
    private PilaEnlazada<String> historialTorneo;
    private Producto[] menu;
    private int[] cantidadesVendidas;

    public ControladorBillar() {
        gestorMesas = new GestorMesas();
        arbolMiembros = new ArbolAVL<>();
        libroDiario = new ListaDobleEnlazada<>();
        colaEspera = new ColaEnlazada<>();
        historialTorneo = new PilaEnlazada<>();
        inicializarMenu();
        poblarDatosPrueba();
    }

    private void inicializarMenu() {
        menu = new Producto[]{
            new Producto("Jarra de Cerveza Artesanal", 20, 12),
            new Producto("Pizza Personal", 15, 7),
            new Producto("Porción de Tequeños", 10, 8),
            new Producto("Gaseosa 1L", 8, 3),
            new Producto("Nachos con Queso", 12, 6)
        };
        cantidadesVendidas = new int[menu.length];
    }

    private void poblarDatosPrueba() {
        try {
            arbolMiembros.insert(new Miembro("MBR-005", "Carlos"));
            arbolMiembros.insert(new Miembro("MBR-002", "Ana"));
            arbolMiembros.insert(new Miembro("MBR-008", "Luis"));
        } catch (ItemDuplicated e) {
            System.err.println("Error interno inicializando datos: " + e.getMessage());
        }
    }

    // --- MÉTODOS DEL MÓDULO 1: MESAS ---
    
    public GestorMesas getGestorMesas() {
        return this.gestorMesas;
    }

    public Mesa asignarMesa(String cliente, String hora) throws ItemNoFound {
        return gestorMesas.asignarPrimeraMesaLibre(hora);
    }

    public void encolarCliente(String cliente) {
        colaEspera.enqueue(cliente);
    }

    public int getTamañoCola() {
        return colaEspera.getTamaño();
    }

    public String liberarMesa(int numeroMesa, String horaFin) throws ItemNoFound {
        gestorMesas.liberarMesa(numeroMesa, horaFin);
        libroDiario.insertarAlFinal("TXN-MESA-" + numeroMesa + ": Pago registrado a las " + horaFin);
        
        if (!colaEspera.isEmpty()) {
            try {
                return colaEspera.dequeue();
            } catch (ExceptionIsEmpty e) {
                return null;
            }
        }
        return null;
    }

    public String verSiguienteEnEspera() throws ExceptionIsEmpty {
        return colaEspera.front();
    }
    
    public boolean colaEsperaVacia() {
        return colaEspera.isEmpty();
    }

    // --- MÉTODOS DEL MÓDULO 2: MIEMBROS ---

    public void registrarMiembro(String id, String nombre) throws ItemDuplicated {
        Miembro nuevoMiembro = new Miembro(id, nombre);
        arbolMiembros.insert(nuevoMiembro);
    }

    public String buscarMiembro(String id) throws ItemNoFound {
        Miembro dummy = new Miembro(id, ""); // Se busca solo por ID según el compareTo
        Miembro encontrado = arbolMiembros.search(dummy);
        return encontrado.toString();
    }

    public String obtenerEstructuraMiembros() {
        return arbolMiembros.obtenerEstructuraVisual();
    }

    // --- MÉTODOS DEL MÓDULO 3: CAJA Y AUDITORÍA ---
    
    public Producto[] getMenu() {
        return this.menu;
    }

    public Producto venderProducto(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= menu.length) {
            throw new IndexOutOfBoundsException("Selección de producto inválida.");
        }
        Producto p = menu[index];
        cantidadesVendidas[index]++;
        libroDiario.insertarAlFinal("TXN-CAJA: Venta de " + p.getNombre() + " -> Ingreso: S/" + p.getPrecioVenta());
        return p;
    }
    
    public ListaDobleEnlazada<String> getLibroDiario() {
        return this.libroDiario;
    }

    public String generarReporteModa() {
        int maxVentas = 0;
        int indiceModa = -1;
        StringBuilder reporte = new StringBuilder("\n--- REPORTE DE FRECUENCIA DE VENTAS ---\n");

        for (int i = 0; i < menu.length; i++) {
            reporte.append(menu[i].getNombre()).append(": ").append(cantidadesVendidas[i]).append(" unidades\n");
            if (cantidadesVendidas[i] > maxVentas) {
                maxVentas = cantidadesVendidas[i];
                indiceModa = i;
            }
        }
        reporte.append("---------------------------------------\n");
        if (maxVentas > 0) {
            reporte.append("🌟 PRODUCTO ESTRELLA (MODA): ").append(menu[indiceModa].getNombre()).append(" con ").append(maxVentas).append(" ventas.\n");
            reporte.append("   [Calculado mediante barrido secuencial O(n)]");
        } else {
            reporte.append("Aún no se han registrado ventas.");
        }
        return reporte.toString();
    }

    // --- MÉTODOS DEL MÓDULO 4: OPTIMIZACIÓN ---

    public void ejecutarKnapsack(int presupuesto) {
        MotorAlgoritmos.sugerirComboOptimo(menu, presupuesto);
        libroDiario.insertarAlFinal("TXN-SISTEMA: Asistente DP calculó combo para S/" + presupuesto);
    }

    public void registrarPuntajeTorneo(String registro) {
        historialTorneo.push(registro);
    }

    public String deshacerUltimoPuntaje() throws ExceptionIsEmpty {
        return historialTorneo.pop();
    }

    public Jugador[] procesarLeaderboard(Jugador[] torneo) {
        MotorAlgoritmos.mergeSort(torneo, 0, torneo.length - 1);
        libroDiario.insertarAlFinal("TXN-TORNEO: Leaderboard ordenado procesado.");
        return torneo;
    }
}
