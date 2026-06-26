package EstructurasLineales;

import Exceptions.ExceptionIsEmpty;

public class ListaDobleEnlazada<T extends Comparable<T>> {
	class NodoDoble {
		public T dato;
		public NodoDoble siguiente;
		public NodoDoble anterior;
		
		public NodoDoble(T dato) {
			this.dato = dato;
			this.siguiente = null;
			this.anterior = null;
		}
	}
	
	private NodoDoble cabeza;
	private NodoDoble cola;
	
	public ListaDobleEnlazada() {
		this.cabeza = null;
		this.cola = null;
	}
	
	public boolean isEmpty() {
		return cabeza == null;
	}
	
	public void insertarAlFinal(T dato) {
		NodoDoble nuevo = new NodoDoble(dato);
		if (isEmpty()) {
			cabeza = nuevo;
			cola = nuevo;
		} else {
			cola.siguiente = nuevo;
			nuevo.anterior = cola;
			cola = nuevo;
		}
	}
	
	public void imprimirHaciaAdelante() throws ExceptionIsEmpty {
		if (isEmpty()) throw new ExceptionIsEmpty("La lista doble está vacía");
		
		NodoDoble actual = cabeza;
		System.out.print("INICIO <-> ");
		while (actual != null) {
			System.out.print(actual.dato.toString() + " <-> ");
			actual = actual.siguiente;
		}
		System.out.println("FIN");
	}
	
	public void imprimirHaciaAtras() throws ExceptionIsEmpty {
		if (isEmpty()) throw new ExceptionIsEmpty("La lista doble está vacía");
		
		NodoDoble actual = cola;
		System.out.print("FIN <-> ");
		while (actual != null) {
			System.out.print(actual.dato.toString() + " <-> ");
			actual = actual.anterior;
		}
		System.out.println("INICIO");
	}
	
	public void eliminarNodo(T elemento) throws ExceptionIsEmpty {
		if (isEmpty()) throw new ExceptionIsEmpty("La lista doble está vacía");
		
		NodoDoble actual = cabeza;
		while (actual != null) {
			if (actual.dato.compareTo(elemento) == 0) {
				//Es el único nodo
				if (actual == cabeza && actual == cola) {
					cabeza = null;
					cola = null;
				} 
				//Es la cabeza
				else if (actual == cabeza) {
					cabeza = cabeza.siguiente;
					cabeza.anterior = null;
				} 
				//Es la cola
				else if (actual == cola) {
					cola = cola.anterior;
					cola.siguiente = null;
				} 
				//Está en el medio
				else {
					actual.anterior.siguiente = actual.siguiente;
					actual.siguiente.anterior = actual.anterior;
				}
				return;
			}
			actual = actual.siguiente;
		}
	}
}