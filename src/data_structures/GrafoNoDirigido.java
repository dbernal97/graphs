package data_structures;

public class GrafoNoDirigido {

	/**
	 * Retorna la cantidad de arcos que hay en el grafo
	 */
	private int edges;
	public void setEdgesSize(int s) {
		this.edges = s;
	}

	/**
	 * lista de adjacencia basada en hash para almacenar lor vertices del grafo
	 */
	private SeparateChainingHashST<Integer, Vertex> g;

	/**
	 * crea un nuevo grafo con espacio para <b>capacity</b> vertices
	 * @param capacity cantidad de espacios para la isersión de vértices
	 */
	public GrafoNoDirigido(int capacity) {g = new SeparateChainingHashST<>(capacity);}
	/**
	 * Crea un grafo con una capacidad por defecto
	 */
	public GrafoNoDirigido() {this(1000);}

	/**
	 * @return cantidad de arcos en el grafo, excluye las direccionalidades
	 */
	public int edges() 		 {return edges;			}

	/**
	 * @returnCantidad de vertices en el grafo
	 */
	public int vertices() 	 {return g.size();		}

	/**
	 * @return true si el grafo no tiene ningun vertice, de resto false
	 */
	public boolean isEmpty() {return g.size() == 0;	}

	/**
	 * Aniade un vertice al grafo, se asume que el vertice no comparte id
	 * @param id id del vertice a aniadir, se puede ver como el indice
	 * @param lat latitud del punto
	 * @param lon longitud del punto
	 */
	public void addVertice(int id, double lat, double lon) {
		g.put(id, new Vertex(id, lat, lon));
	}
	public void addVertice(Vertex v) {
		g.put(v.indice(), v);
	}

	/**
	 * Aniade un arco entre dos vertices, el arco ha de ser el parametro
	 * Se asume que el arco no existia en el grafo y que los vertices ya han sido cargados
	 * @param newEdge arco a aniadir en el grafo
	 */
	public void addEdge(Edge newEdge) {
		Vertex v1 = g.get(newEdge.from()), v2=g.get(newEdge.to());
		v1.addEdge(newEdge); 
		v2.addEdge(new Edge(newEdge.to(), newEdge.from(), newEdge.w()));
		edges++;
	}

	/**
	 * Utilidad que transforma el grafo en de una lista a un arreglo
	 * @return
	 */
	public Vertex[] indexedArray() {
		Vertex[] toRet = new Vertex[vertices()];
		for (Vertex v : g.vals())
			toRet[v.indice()] = v;
		return toRet;
	}

	/**
	 * Actualiza los indices de todos los vertices y arcos del grafo
	 */
	public void reindex() {
		class analogo implements Comparable<analogo>{
			int v, 		//Valor antiguo
			newv;	//nuevo valor
			public analogo(int prev, int newi) {this.v=prev; this.newv=newi;}
			@Override public int compareTo(analogo o) {return this.v-o.v;};
		}
		SeparateChainingHashST<Integer,Vertex> newg = new SeparateChainingHashST<Integer, Vertex>(vertices());
		RedBlackBST<analogo> analog = new RedBlackBST<analogo>(); 
		int i = 0;
		for(Vertex v: this.g.vals()) {
			analog.put(new analogo(v.indice(), i));
			newg.put(i, new Vertex(i, v.lon(), v.lat()));
			i++;
		}//System.out.println("Vertices re-indexados");
		for (Vertex v : this.g.vals()) {
			int ind = analog.get(new analogo(v.indice(), 0)).newv;
			Vertex vanalogo = newg.get(ind);
			for(Edge e: v.edges()) {
				int to= e.to();
				to = analog.get(new analogo(to, to)).newv;
				vanalogo.addEdge(new Edge(ind, to, e.w()));
			}
		}//System.out.println("Arcos re-indexados");
		this.g= newg;
	}

	/**
	 * consulta todos los arcos que salen de un vertice determinado
	 * @param vertice identificador del vertice
	 * @return iterable con los arcos
	 */
	public Iterable<Edge> adyacentesA(int vertice) {return g.get(vertice).edges();}
	public Iterable<Vertex> allVertices(){return this.g.vals();}
	public QueStack<Vertex> allVerticesQueue(){return this.g.queueOfVals();}
	public Iterable<Edge> allEdges(){
		QueStack<Edge> toRet = new QueStack<>();
		for (Vertex v : g.vals()) 
			for (Edge e : v.edges()) 
				toRet.enqueue(e);
		return toRet;
	}

	/**
	 * retorna toda la informacion de un vertice por su identificador
	 * @param id identificador del vertice buscado (es a su vez el indice del mismo)
	 * @return el vertice cuyo id sea dado
	 */
	public Vertex getById(int id) {
		return this.g.get(id);
	}
}
