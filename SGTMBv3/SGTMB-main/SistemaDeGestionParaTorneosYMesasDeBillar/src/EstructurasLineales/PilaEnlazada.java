package EstructurasLineales;

import Exceptions.ExceptionIsEmpty;
import Interfaces.Stack;
import Modelos.Nodo;

// ==========================================
// Estructura: Pila (Stack) - Lógica LIFO
// Requerimiento Fase 3: Módulo "Undo" para marcadores
// ==========================================
public class PilaEnlazada<T extends Comparable<T>> implements Stack<T> {
    private Nodo<T> top;

    public PilaEnlazada() {
        this.top = null;
    }

    @Override
    public void push(T dato) {
        Nodo<T> nuevoTop = new Nodo<>(dato);
        nuevoTop.setSiguiente(top);
        top = nuevoTop;
    }

    @Override
    public T pop() throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("No hay acciones recientes para deshacer.");
        }
        T dato = top.getDato();
        top = top.getSiguiente();
        return dato;
    }

    @Override
    public T top() throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("La pila está vacía.");
        }
        return top.getDato();
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }
    
    // Método útil para ver el historial sin desapilar
    public void imprimirHistorial() {
        if (isEmpty()) {
            System.out.println("No hay acciones en el historial.");
            return;
        }
        Nodo<T> actual = top;
        System.out.println("--- HISTORIAL RECIENTE (Tope a Base) ---");
        while (actual != null) {
            System.out.println(" -> " + actual.getDato().toString());
            actual = actual.getSiguiente();
        }
    }
}