package Interfaces;

import Exceptions.*;

public interface Queue<T extends Comparable<T>> {
	void enqueue(T dato);
	T dequeue() throws ExceptionIsEmpty;
	T front() throws ExceptionIsEmpty;
	T back() throws ExceptionIsEmpty;
	boolean isEmpty();
}