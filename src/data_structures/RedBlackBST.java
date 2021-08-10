package data_structures;

import java.util.NoSuchElementException;

/**
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class RedBlackBST<Value extends Comparable<Value>> {

	private static final boolean RED   = true;
	private static final boolean BLACK = false;

	private Node root;

	private class Node {
		private Value val;          private Node left, right;  
		private boolean color; 		private int size;         

		public Node(Value val, boolean color, int size) {this.val = val; this.color = color; this.size = size;}
	}

	public RedBlackBST() {}

	//Métodos ayuda del nodo -------------------------------------------------

	private boolean isRed(Node x) {return(x == null)? false:x.color== RED;}

	private int size(Node x) {return(x == null)? 0: x.size;} 

	//----------Métodos del árbol principal
	public int size() {return size(root);}

	public boolean isEmpty() {return root == null;}

	public Value get(Value val) {
		if (val == null) throw new IllegalArgumentException("argument to get() is null");
		return get(root, val);
	}

	private Value get(Node x, Value val) {
		while (x != null) {
			int cmp = val.compareTo(x.val);
			if      (cmp < 0) x = x.left;
			else if (cmp > 0) x = x.right;
			else return x.val;
		}
		return null;
	}

	public boolean contains(Value val) {return get(val) != null;}

	/**
	 * this put just insert a new node in the tree
	 * @param val
	 */
	public void put(Value val) {
		if (val == null) {delete(val);	return;	}

		root = put(root, val); root.color = BLACK;
	}

	/**
	 * this put replace if find a val equals to parameter
	 * @param val
	 */
	public void putReplacing(Value val) {
		if (val == null) {delete(val);	return;	}

		root = putReplacing(root, val);
		root.color = BLACK;
	}

	// insert the key-value pair in the subtree rooted at h
	private Node put(Node h, Value val) { 
		if (h == null) return new Node(val, RED, 1);

		int cmp = val.compareTo(h.val);
		if      (cmp < 0) h.left  = put(h.left, val); 
		else if (cmp > 0) h.right = put(h.right, val); 

		// fix-up any right-leaning links
		if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
		if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
		if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
		h.size = size(h.left) + size(h.right) + 1;

		return h;
	}

	private Node putReplacing(Node h, Value val) { 
		if (h == null) return new Node(val, RED, 1);

		int cmp = val.compareTo(h.val);
		if      (cmp < 0) h.left  = put(h.left, val); 
		else if (cmp > 0) h.right = put(h.right, val); 
		else              h.val   = val;

		// fix-up any right-leaning links
		if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
		if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
		if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
		h.size = size(h.left) + size(h.right) + 1;

		return h;
	}

	public void deleteMin() {
		if (isEmpty()) throw new NoSuchElementException("BST underflow");

		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;

		root = deleteMin(root);
		if (!isEmpty()) root.color = BLACK;
		// assert check();
	}

	// delete the key-value pair with the minimum key rooted at h
	private Node deleteMin(Node h) { 
		if (h.left == null)
			return null;

		if (!isRed(h.left) && !isRed(h.left.left))
			h = moveRedLeft(h);

		h.left = deleteMin(h.left);
		return balance(h);
	}

	public void deleteMax() {
		if (isEmpty()) throw new NoSuchElementException("BST underflow");

		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;

		root = deleteMax(root);
		if (!isEmpty()) root.color = BLACK;
		// assert check();
	}
	
	public Value getMax() {
		if (isEmpty()) throw new NoSuchElementException("BST underflow");
		Node x = this.root;
		while (x.right != null)
			x=x.right;
		return x.val;
	}
	public Value getMin() {
		if (isEmpty()) throw new NoSuchElementException("BST underflow");
		Node x = this.root;
		while (x.left != null)
			x=x.left;
		return x.val;
	}

	// delete the key-value pair with the maximum key rooted at h
	private Node deleteMax(Node h) { 
		if (isRed(h.left))
			h = rotateRight(h);

		if (h.right == null)
			return null;

		if (!isRed(h.right) && !isRed(h.right.left))
			h = moveRedRight(h);

		h.right = deleteMax(h.right);

		return balance(h);
	}

	public void delete(Value val) { 
		if (val == null) throw new IllegalArgumentException("argument to delete() is null");
		if (!contains(val)) return;

		// if both children of root are black, set root to red
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;

		root = delete(root, val);
		if (!isEmpty()) root.color = BLACK;
		// assert check();
	}

	// delete the key-value pair with the given key rooted at h
	private Node delete(Node h, Value val) { 
		// assert get(h, key) != null;

		if (val.compareTo(h.val) < 0)  {
			if (!isRed(h.left) && !isRed(h.left.left))
				h = moveRedLeft(h);
			h.left = delete(h.left, val);
		}
		else {
			if (isRed(h.left))
				h = rotateRight(h);
			if (val.compareTo(h.val) == 0 && (h.right == null))
				return null;
			if (!isRed(h.right) && !isRed(h.right.left))
				h = moveRedRight(h);
			if (val.compareTo(h.val) == 0) {
				Node x = min(h.right);
				h.val = x.val;
				h.val = x.val;
				// h.val = get(h.right, min(h.right).key);
				// h.key = min(h.right).key;
				h.right = deleteMin(h.right);
			}
			else h.right = delete(h.right, val);
		}
		return balance(h);
	}

	// make a left-leaning link lean to the right
	private Node rotateRight(Node h) {
		// assert (h != null) && isRed(h.left);
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = x.right.color;
		x.right.color = RED;
		x.size = h.size;
		h.size = size(h.left) + size(h.right) + 1;
		return x;
	}

	// make a right-leaning link lean to the left
	private Node rotateLeft(Node h) {
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = x.left.color;
		x.left.color = RED;
		x.size = h.size;
		h.size = size(h.left) + size(h.right) + 1;
		return x;
	}

	// flip the colors of a node and its two children
	private void flipColors(Node h) {
		// h must have opposite color of its two children
		// assert (h != null) && (h.left != null) && (h.right != null);
		// assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
		//    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}

	// Assuming that h is red and both h.left and h.left.left
	// are black, make h.left or one of its children red.
	private Node moveRedLeft(Node h) {
		// assert (h != null);
		// assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

		flipColors(h);
		if (isRed(h.right.left)) { 
			h.right = rotateRight(h.right);
			h = rotateLeft(h);
			flipColors(h);
		}
		return h;
	}

	// Assuming that h is red and both h.right and h.right.left
	// are black, make h.right or one of its children red.
	private Node moveRedRight(Node h) {
		// assert (h != null);
		// assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
		flipColors(h);
		if (isRed(h.left.left)) { 
			h = rotateRight(h);
			flipColors(h);
		}
		return h;
	}

	// restore red-black tree invariant
	private Node balance(Node h) {
		// assert (h != null);

		if (isRed(h.right))                      h = rotateLeft(h);
		if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
		if (isRed(h.left) && isRed(h.right))     flipColors(h);

		h.size = size(h.left) + size(h.right) + 1;
		return h;
	}


	/***************************************************************************
	 *  Utility functions.
	 ***************************************************************************/

	public int height() {
		return height(root);
	}
	private int height(Node x) {
		if (x == null) return -1;
		return 1 + Math.max(height(x.left), height(x.right));
	}

	public int minheight() {
		return minheight(root);
	}
	private int minheight(Node x) {
		if (x == null) return -1;
		return 1 + Math.min(minheight(x.left), minheight(x.right));
	}

	/***************************************************************************
	 *  Ordered symbol table methods.
	 ***************************************************************************/

	/**
	 * Returns the smallest key in the symbol table.
	 * @return the smallest key in the symbol table
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public Value min() {
		if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
		return min(root).val;
	} 

	// the smallest key in subtree rooted at x; null if no such key
	private Node min(Node x) { 
		// assert x != null;
		if (x.left == null) return x; 
		else                return min(x.left); 
	} 

	/**
	 * Returns the largest key in the symbol table.
	 * @return the largest key in the symbol table
	 * @throws NoSuchElementException if the symbol table is empty
	 */
	public Value max() {
		if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
		return max(root).val;
	} 

	// the largest key in the subtree rooted at x; null if no such key
	private Node max(Node x) { 
		// assert x != null;
		if (x.right == null) return x; 
		else                 return max(x.right); 
	} 


	/**
	 * Returns the largest key in the symbol table less than or equal to {@code key}.
	 * @param key the key
	 * @return the largest key in the symbol table less than or equal to {@code key}
	 * @throws NoSuchElementException if there is no such key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public Value floor(Value val) {
		if (val == null) throw new IllegalArgumentException("argument to floor() is null");
		if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
		Node x = floor(root, val);
		if (x == null) return null;
		else           return x.val;
	}    

	// the largest key in the subtree rooted at x less than or equal to the given key
	private Node floor(Node x, Value val) {
		if (x == null) return null;
		int cmp = val.compareTo(x.val);
		if (cmp == 0) return x;
		if (cmp < 0)  return floor(x.left, val);
		Node t = floor(x.right, val);
		if (t != null) return t; 
		else           return x;
	}

	/**
	 * Returns the smallest key in the symbol table greater than or equal to {@code key}.
	 * @param val the key
	 * @return the smallest key in the symbol table greater than or equal to {@code key}
	 * @throws NoSuchElementException if there is no such key
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public Value ceiling(Value val) {
		if (val == null) throw new IllegalArgumentException("argument to ceiling() is null");
		if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
		Node x = ceiling(root, val);
		if (x == null) return null;
		else           return x.val;  
	}

	// the smallest key in the subtree rooted at x greater than or equal to the given key
	private Node ceiling(Node x, Value val) {  
		if (x == null) return null;
		int cmp = val.compareTo(x.val);
		if (cmp == 0) return x;
		if (cmp > 0)  return ceiling(x.right, val);
		Node t = ceiling(x.left, val);
		if (t != null) return t; 
		else           return x;
	}

	/**
	 * Return the key in the symbol table whose rank is {@code k}.
	 * This is the (k+1)st smallest key in the symbol table. 
	 *
	 * @param  k the order statistic
	 * @return the key in the symbol table of rank {@code k}
	 * @throws IllegalArgumentException unless {@code k} is between 0 and
	 *        <em>n</em>–1
	 */
	public Value select(int k) {
		if (k < 0 || k >= size()) {
			throw new IllegalArgumentException("argument to select() is invalid: " + k);
		}
		Node x = select(root, k);
		return x.val;
	}

	// the key of rank k in the subtree rooted at x
	private Node select(Node x, int k) {
		// assert x != null;
		// assert k >= 0 && k < size(x);
		int t = size(x.left); 
		if      (t > k) return select(x.left,  k); 
		else if (t < k) return select(x.right, k-t-1); 
		else            return x; 
	} 

	/**
	 * Return the number of keys in the symbol table strictly less than {@code key}.
	 * @param key the key
	 * @return the number of keys in the symbol table strictly less than {@code key}
	 * @throws IllegalArgumentException if {@code key} is {@code null}
	 */
	public int rank(Value val) {
		if (val == null) throw new IllegalArgumentException("argument to rank() is null");
		return rank(val, root);
	} 

	// number of keys less than key in the subtree rooted at x
	private int rank(Value val, Node x) {
		if (x == null) return 0; 
		int cmp = val.compareTo(x.val); 
		if      (cmp < 0) return rank(val, x.left); 
		else if (cmp > 0) return 1 + size(x.left) + rank(val, x.right); 
		else              return size(x.left); 
	} 

	/***************************************************************************
	 *  Range count and range search.
	 ***************************************************************************/

	/**
	 * Returns all keys in the symbol table as an {@code Iterable}.
	 * To iterate over all of the keys in the symbol table named {@code st},
	 * use the foreach notation: {@code for (Key key : st.keys())}.
	 * @return all keys in the symbol table as an {@code Iterable}
	 */
	public Iterable<Value> values() {
		if (isEmpty()) return new QueStack<Value>();
		return routeTo(min(), max());
	}

	/**
	 * Returns all keys in the symbol table in the given range,
	 * as an {@code Iterable}.
	 *
	 * @param  lo minimum endpoint
	 * @param  hi maximum endpoint
	 * @return all keys in the sybol table between {@code lo} 
	 *    (inclusive) and {@code hi} (inclusive) as an {@code Iterable}
	 * @throws IllegalArgumentException if either {@code lo} or {@code hi}
	 *    is {@code null}
	 */
	public Iterable<Value> routeTo(Value lo, Value hi) {
		if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
		if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

		QueStack<Value> queue = new QueStack<Value>();
		// if (isEmpty() || lo.compareTo(hi) > 0) return queue;
		vals(root, queue, lo, hi);
		return queue;
	} 

	// add the keys between lo and hi in the subtree rooted at x
	// to the queue
	private void vals(Node x, QueStack<Value> queue, Value lo, Value hi) { 
		if (x == null) return; 
		int cmplo = lo.compareTo(x.val); 
		int cmphi = hi.compareTo(x.val); 
		if (cmplo < 0) vals(x.left, queue, lo, hi); 
		if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.val); 
		if (cmphi > 0) vals(x.right, queue, lo, hi); 
	} 

	/**
	 * Returns the number of keys in the symbol table in the given range.
	 *
	 * @param  lo minimum endpoint
	 * @param  hi maximum endpoint
	 * @return the number of keys in the sybol table between {@code lo} 
	 *    (inclusive) and {@code hi} (inclusive)
	 * @throws IllegalArgumentException if either {@code lo} or {@code hi}
	 *    is {@code null}
	 */
	public int size(Value lo, Value hi) {
		if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
		if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

		if (lo.compareTo(hi) > 0) return 0;
		if (contains(hi)) return rank(hi) - rank(lo) + 1;
		else              return rank(hi) - rank(lo);
	}
}
