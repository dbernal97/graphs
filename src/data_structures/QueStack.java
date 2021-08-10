package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class QueStack<T extends Comparable<T>> implements Iterable<T>, Comparable<QueStack<T>>{

	private NodeDL<T> foot, head; private int size;

	public QueStack(){	foot = head = null; size = 0;}

	public boolean isEmpty() {return (size == 0);}

	public int size() {return size;}

	public T pop() {
		T toret = null;
		if(foot != null) {size--;
		toret = foot.getItem();
		if(head == foot) foot = head = null;
		else {
			foot = foot.next();
			foot.setPrev(null);
		}}
		return toret;
	}

	public T dequeue() {
		T deq = null;
		if(head != null){	size--;
		deq = head.getItem();
		if(head == foot)
			foot = 	head = null;
		else{	head = head.previous();
		head.setNext(null);
		}}
		return deq;
	}

	public void enqueue(T item) {
		NodeDL<T> newNode = new NodeDL<T>(item);
		size++;
		if(foot== null)foot = head = newNode;
		else{	newNode.setNext(foot);
		foot.setPrev(newNode);
		foot = newNode;
		}
	}

	public void join(QueStack<T> toJoin) {
		if(foot == null)this.foot = toJoin.foot;
		if(toJoin == null || toJoin.isEmpty())return;
		this.size += toJoin.size;
		toJoin.head.setNext(this.foot);
		this.foot.setPrev(toJoin.head);
		this.foot = toJoin.foot;
	}

	public void inverseJoin(QueStack<T> toJoin) {
		if(foot == null)this.foot = toJoin.foot;
		if(toJoin == null || toJoin.isEmpty())return;
		this.size += toJoin.size;
		toJoin.foot.setPrev(this.head);
		this.head.setNext(toJoin.foot);
		this.head = toJoin.head;
	}

	public Iterator<T> iterator()  {return new myIterator<T>(foot);}

	@SuppressWarnings("hiding")
	private class myIterator<T extends Comparable<T>> implements Iterator<T> {
		private NodeDL<T> current;
		public myIterator(NodeDL<T> first) {current = first;}
		public boolean hasNext()  { return current != null;}
		public T next() {	if (!hasNext()) throw new NoSuchElementException();
		T item = current.getItem();
		current = current.next(); 
		return item;
		}
	}
	@Override public int compareTo(QueStack<T> q) {	return Integer.compare(this.size, q.size);}
}
