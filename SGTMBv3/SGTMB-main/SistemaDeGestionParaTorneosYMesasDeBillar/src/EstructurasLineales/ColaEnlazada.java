package EstructurasLineales;

import Exceptions.ExceptionIsEmpty;
import Interfaces.Queue;
import Modelos.Nodo;

// ==========================================
// Estructura: Cola (Queue) - Lógica FIFO
// Requerimiento Fase 3: Lista de Espera de Clientes
// ==========================================
public class ColaEnlazada<T extends Comparable<T>> implements Queue<T> {
    private Nodo<T> front;
    private Nodo<T> back;
    private int tamaño;

    public ColaEnlazada() {
        this.front = null;
        this.back = null;
        this.tamaño = 0;
    }

    @Override
    public void enqueue(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (isEmpty()) {
            front = nuevo;
        } else {
            back.setSiguiente(nuevo);
        }
        back = nuevo;
        tamaño++;
    }

    @Override
    public T dequeue() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("La cola de espera está vacía.");
        
        T dato = front.getDato();
        front = front.getSiguiente();
        if (front == null) {
            back = null; // Si se vacía, el back también debe ser null
        }
        tamaño--;
        return dato;
    }

    @Override
    public T front() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("La cola de espera está vacía.");
        return front.getDato();
    }

    @Override
    public T back() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("La cola de espera está vacía.");
        return back.getDato();
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }
    
    public int getTamaño() {
        return tamaño;
    }
}