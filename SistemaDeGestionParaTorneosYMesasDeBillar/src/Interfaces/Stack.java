package Interfaces;

import Exceptions.*;

public interface Stack<E extends Comparable<E>> {
	void push(E x);
	E pop() throws ExceptionIsEmpty;
	E top() throws ExceptionIsEmpty;
	boolean isEmpty();
}
