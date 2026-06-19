package EstructurasNoLineales;

import Exceptions.*;
import Interfaces.BinarySearchTree;

public class ArbolAVL<E extends Comparable<E>> implements BinarySearchTree<E> {
	class NodeAVL {
		public E data;
		public NodeAVL left;
		public NodeAVL right;
		public int height;
		
		public NodeAVL(E data) {
			this.data = data;
			this.left = null;
			this.right = null;
			this.height = 1;
		}
	}
	
	private NodeAVL root;
	
	public ArbolAVL() {
		this.root = null;
	}

	// SEMANA 1: MÉTODOS UTILITARIOS Y DE BÚSQUEDA
	
	private int height(NodeAVL N) {
		if (N == null) return 0;
		return N.height;
	}
	
	private int getBalance(NodeAVL N) {
		if (N == null) return 0;
		return height(N.left) - height(N.right);
	}

	@Override
	public boolean isEmpty() {
		return this.root == null;
	}

	@Override
	public E search(E data) throws ItemNoFound {
		NodeAVL res = searchRec(data, this.root);
		if (res == null)
			throw new ItemNoFound("El dato " + data + " no está en el árbol AVL");
		return res.data;
	}
	
	protected NodeAVL searchRec(E x, NodeAVL n) {
		if (n == null) return null;
		int resC = n.data.compareTo(x);
		if (resC < 0) return searchRec(x, n.right);
		else if (resC > 0) return searchRec(x, n.left);
		else return n;
	}

	// SEMANA 2: ROTACIONES Y REBALANCEO (INSERCIÓN)
	
	private NodeAVL rightRotate(NodeAVL y) {
		NodeAVL x = y.left;
		NodeAVL T2 = x.right;
		
		x.right = y;
		y.left = T2;
		
		y.height = Math.max(height(y.left), height(y.right)) + 1;
		x.height = Math.max(height(x.left), height(x.right)) + 1;
		
		return x;
	}
	
	private NodeAVL leftRotate(NodeAVL x) {
		NodeAVL y = x.right;
		NodeAVL T2 = y.left;
		
		y.left = x;
		x.right = T2;
		
		x.height = Math.max(height(x.left), height(x.right)) + 1;
		y.height = Math.max(height(y.left), height(y.right)) + 1;
		
		return y;
	}

	@Override
	public void insert(E data) throws ItemDuplicated {
		this.root = insertRec(data, this.root);
	}
	
	private NodeAVL insertRec(E x, NodeAVL actual) throws ItemDuplicated {
		if (actual == null) return new NodeAVL(x);
		
		int cmp = actual.data.compareTo(x);
		if (cmp == 0) throw new ItemDuplicated(x + " está duplicado");
		
		if (cmp < 0) actual.right = insertRec(x, actual.right);
		else actual.left = insertRec(x, actual.left);
		
		actual.height = 1 + Math.max(height(actual.left), height(actual.right));
		
		int balance = getBalance(actual);
		
		//Si el nodo se desbalancea, aplicar los 4 casos posibles
		
		// Caso Izquierda Izquierda
		if (balance > 1 && x.compareTo(actual.left.data) < 0)
			return rightRotate(actual);
			
		// Caso Derecha Derecha
		if (balance < -1 && x.compareTo(actual.right.data) > 0)
			return leftRotate(actual);
			
		// Caso Izquierda Derecha
		if (balance > 1 && x.compareTo(actual.left.data) > 0) {
			actual.left = leftRotate(actual.left);
			return rightRotate(actual);
		}
		
		// Caso Derecha Izquierda
		if (balance < -1 && x.compareTo(actual.right.data) < 0) {
			actual.right = rightRotate(actual.right);
			return leftRotate(actual);
		}
		
		return actual;
	}

	@Override
	public void delete(E data) throws ItemNoFound {
		this.root = removeRec(data, this.root);
	}

	private NodeAVL removeRec(E x, NodeAVL actual) throws ItemNoFound {
		if (actual == null) throw new ItemNoFound(x + " no está");

		int cmp = actual.data.compareTo(x);
		if (cmp < 0) actual.right = removeRec(x, actual.right);
		else if (cmp > 0) actual.left = removeRec(x, actual.left);
		else {
			// Nodo con un solo hijo o sin hijos
			if ((actual.left == null) || (actual.right == null)) {
				NodeAVL temp = null;
				if (temp == actual.left) temp = actual.right;
				else temp = actual.left;

				if (temp == null) {
					temp = actual;
					actual = null;
				} else {
					actual = temp; 
				}
			} else {
				// Nodo con dos hijos
				NodeAVL temp = minRecoverNode(actual.right);
				actual.data = temp.data;
				actual.right = removeRec(temp.data, actual.right);
			}
		}

		if (actual == null) return actual;

		// Rebalancear tras eliminar
		actual.height = Math.max(height(actual.left), height(actual.right)) + 1;
		int balance = getBalance(actual);

		if (balance > 1 && getBalance(actual.left) >= 0) return rightRotate(actual);
		if (balance > 1 && getBalance(actual.left) < 0) {
			actual.left = leftRotate(actual.left);
			return rightRotate(actual);
		}
		if (balance < -1 && getBalance(actual.right) <= 0) return leftRotate(actual);
		if (balance < -1 && getBalance(actual.right) > 0) {
			actual.right = rightRotate(actual.right);
			return leftRotate(actual);
		}

		return actual;
	}

	private NodeAVL minRecoverNode(NodeAVL node) {
		NodeAVL actual = node;
		while (actual.left != null) actual = actual.left;
		return actual;
	}
}