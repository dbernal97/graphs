package data_structures;

public class NodeDL<T extends Comparable<T>> {

	private NodeDL<T> next;

	private NodeDL<T> previous;

	private T MyInfo;

	public NodeDL(T element) {
		next = null; previous = null; MyInfo = element;}

	public NodeDL<T> next() {return next;}
	public NodeDL<T> previous() {return previous;}

	public void setNext(NodeDL<T> next){this.next = next;}
	public void setPrev(NodeDL<T> prev) {previous = prev;}

	public T getItem(){return MyInfo;}
	public void setItem(T item){this.MyInfo = item;}

	public NodeDL<T> getNode(T element) {return(this.MyInfo.compareTo(element) == 0)?this:
		(next != null)?next.getNode(element):null;}
	public T get(T element) {return(this.MyInfo.compareTo(element) == 0)?this.getItem():
		(next != null)?next.get(element):null;}

	public T get(int pos) {return (pos == 1)? MyInfo:(next != null)?next.get(pos - 1): null;}
}
