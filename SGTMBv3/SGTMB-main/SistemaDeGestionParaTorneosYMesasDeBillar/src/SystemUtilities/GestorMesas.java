package SystemUtilities;

import Exceptions.ItemNoFound;
import Modelos.Mesa;

public class GestorMesas {
    private Mesa[] arregloMesas;
    private final int TOTAL_MESAS = 15;

    public GestorMesas() {
        arregloMesas = new Mesa[TOTAL_MESAS];
        inicializarArreglo();
    }

    private void inicializarArreglo() {
        for (int i = 0; i < TOTAL_MESAS; i++) {
            arregloMesas[i] = new Mesa(i + 1);
        }
    }

    public void mostrarEstadoSalon() {
        System.out.println("\n--- PANEL DE ESTADO DE MESAS ---");
        for (Mesa mesa : arregloMesas) {
            System.out.println(mesa.toString());
        }
        System.out.println("--------------------------------");
    }

    public Mesa asignarPrimeraMesaLibre(String horaInicio) throws ItemNoFound {
        for (int i = 0; i < TOTAL_MESAS; i++) {
            if (!arregloMesas[i].isOcupada()) {
                arregloMesas[i].setOcupada(true);
                arregloMesas[i].setHoraInicio(horaInicio);
                return arregloMesas[i];
            }
        }
        throw new ItemNoFound("Todas las mesas están ocupadas. El cliente debe pasar a la cola de espera.");
    }

    public void liberarMesa(int numeroMesa, String horaFin) throws ItemNoFound {
        if (numeroMesa < 1 || numeroMesa > TOTAL_MESAS) {
            throw new ItemNoFound("El número de mesa " + numeroMesa + " no existe en el sistema.");
        }
        
        int indice = numeroMesa - 1;
        Mesa mesaActual = arregloMesas[indice];

        if (!mesaActual.isOcupada()) {
            System.out.println("La mesa " + numeroMesa + " ya se encontraba disponible.");
        } else {
            double totalAPagar = mesaActual.calcularPrecio(horaFin);
            
            System.out.println("\n=== RECIBO DE PAGO ===");
            System.out.println("Mesa N°: " + numeroMesa);
            System.out.println("Hora de inicio: " + mesaActual.getHoraInicio());
            System.out.println("Hora de fin: " + horaFin);
            System.out.printf("Total a cobrar: S/ %.2f\n", totalAPagar);
            System.out.println("======================");

            mesaActual.setOcupada(false);
            mesaActual.setHoraInicio("-");
            System.out.println("Mesa " + numeroMesa + " liberada y lista para nuevo uso.");
        }
    }
}