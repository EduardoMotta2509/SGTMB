package Interfaces;

import Exceptions.*;

public interface BinarySearchTree<E> {
	void insert(E data) throws ItemDuplicated;
	E search(E data) throws ItemNoFound;
	void delete(E data) throws ItemNoFound;
	boolean isEmpty();
}