package Optimizacion;

import Modelos.Producto;
import java.util.ArrayList;
import java.util.List;

public class MotorAlgoritmos {
    public static void sugerirComboOptimo(Producto[] menu, int presupuestoCliente) {
        int n = menu.length;
        // Matriz de memorización DP [cantidad de items][presupuesto]
        int[][] dp = new int[n + 1][presupuestoCliente + 1];

        // Llenado de la matriz aplicando subestructuras óptimas
        for (int i = 1; i <= n; i++) {
            int costo = menu[i - 1].getPrecioVenta();
            int ganancia = menu[i - 1].getMargenGanancia();

            for (int w = 1; w <= presupuestoCliente; w++) {
                if (costo <= w) {
                    // Se elige el máximo entre no incluir el producto, o incluirlo y sumar su ganancia
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - costo] + ganancia);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Backtracking para encontrar qué productos exactos conforman la solución óptima
        int gananciaMaxima = dp[n][presupuestoCliente];
        int w = presupuestoCliente;
        List<Producto> productosSeleccionados = new ArrayList<>();

        for (int i = n; i > 0 && gananciaMaxima > 0; i--) {
            if (gananciaMaxima != dp[i - 1][w]) {
                Producto p = menu[i - 1];
                productosSeleccionados.add(p);
                gananciaMaxima -= p.getMargenGanancia();
                w -= p.getPrecioVenta();
            }
        }

        System.out.println("-> Calculando combinaciones posibles con DP...");
        System.out.println("-> ¡Combinación óptima encontrada!");
        int totalCosto = 0;
        int totalGanancia = 0;
        for (Producto p : productosSeleccionados) {
            System.out.println("   * " + p.getNombre() + " (S/" + p.getPrecioVenta() + ")");
            totalCosto += p.getPrecioVenta();
            totalGanancia += p.getMargenGanancia();
        }
        System.out.println("-> Gasto del cliente: S/" + totalCosto + " / Presupuesto: S/" + presupuestoCliente);
        System.out.println("-> GANANCIA NETA PARA EL LOCAL: S/" + totalGanancia);
    }

    // =====================================================================
    // 2. ORDENAMIENTO AVANZADO: Merge Sort Genérico
    // =====================================================================
    public static <T extends Comparable<T>> void mergeSort(T[] arreglo, int izquierda, int derecha) {
        if (izquierda < derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            mergeSort(arreglo, izquierda, medio);
            mergeSort(arreglo, medio + 1, derecha);
            merge(arreglo, izquierda, medio, derecha);
        }
    }

    private static <T extends Comparable<T>> void merge(T[] arreglo, int izquierda, int medio, int derecha) {
        int n1 = medio - izquierda + 1;
        int n2 = derecha - medio;

        Object[] L = new Object[n1];
        Object[] R = new Object[n2];

        for (int i = 0; i < n1; ++i) L[i] = arreglo[izquierda + i];
        for (int j = 0; j < n2; ++j) R[j] = arreglo[medio + 1 + j];

        int i = 0, j = 0, k = izquierda;
        while (i < n1 && j < n2) {
            @SuppressWarnings("unchecked")
            T leftItem = (T) L[i];
            @SuppressWarnings("unchecked")
            T rightItem = (T) R[j];

            // IMPORTANTE: Uso estricto de compareTo para mantener la estabilidad, nunca equals
            if (leftItem.compareTo(rightItem) <= 0) {
                arreglo[k] = leftItem;
                i++;
            } else {
                arreglo[k] = rightItem;
                j++;
            }
            k++;
        }

        while (i < n1) {
            arreglo[k] = (T) L[i];
            i++; k++;
        }
        while (j < n2) {
            arreglo[k] = (T) R[j];
            j++; k++;
        }
    }
}