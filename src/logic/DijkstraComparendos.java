package logic;

import data_structures.GrafoNoDirigido;
import data_structures.IndexMinPQ;
import data_structures.Edge;
import data_structures.QueStack;
import data_structures.Vertex;

/**
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class DijkstraComparendos {

	private double[] 	distanceTo;  // menor distancia al vertice [v]
	private Vertex[] 	graph;
	private Edge[] 		edgeTo;   	 // último arco con el camino más corto a [v]
	private IndexMinPQ<Double> pq;   // priority queue of vertices

	/**
	 * 
	 * @param G grafo
	 * @param s posición desde la cual se buscará el camino al siguiente vértice
	 */
	public DijkstraComparendos(GrafoNoDirigido G, int s) {
		graph = G.indexedArray();

		distanceTo = new double[G.vertices()];
		edgeTo = new Edge[G.vertices()];

		for (int v = 0; v < G.vertices(); v++) distanceTo[v] = Double.POSITIVE_INFINITY;
		distanceTo[s] = 0.0;

		// relax vertices in order of distance from s
		pq = new IndexMinPQ<Double>(G.vertices());
		pq.insert(s, distanceTo[s]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			for (Edge e : graph[v].edges())relax(e);
		}

		assert check(G, s);
	}

	private void relax(Edge e) {
		int v = e.from(), w = e.to();
		if (distanceTo[w] > (distanceTo[v] + e.comps())) {
			distanceTo[w] = distanceTo[v] + e.comps();
			edgeTo[w] = e;
			if (pq.contains(w)) pq.decreaseKey(w, distanceTo[w]);
			else                pq.insert(w, distanceTo[w]);
		}
	}

	public double distanceTo(int v) {return distanceTo[v];}

	public boolean hasPathTo(int v) {return distanceTo[v] < Double.POSITIVE_INFINITY;}

	/**
	 * Retorna el  shortestPath desde {@code s} hasta {@code v}.
	 * @param  v vértice destino
	 * @return el shortestPath desde {@code s} a {@code v} como un iterable de arcos
	 *         o {@code null} si no hay camino
	 */
	public Iterable<Edge> pathTo(int v) {
		if (!hasPathTo(v)) return null;
		QueStack<Edge> path = new QueStack<Edge>();
		for (Edge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) path.enqueue(e);
		return path;
	}

	/**
	 * check optimality conditions
	 * (i) for all edges e: distTo[e.to()] <= distTo[e.from()] + e.comps()
	 * (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.comps()
	 * @param G grafo
	 * @param s posición inicial para la buqueda del ShortestPath
	 * @return true si es adecuado el grafo y la posición indicada
	 */
	private boolean check(GrafoNoDirigido G, int s) {
		// revisa que distTo[v] y edgeTo[v] sean consistentes
		if (distanceTo[s] != 0.0 || edgeTo[s] != null) {
			System.err.println("distanceTo[s] y edgeTo[s] no son consistentes");
			return false;
		}
		for (int v = 0; v < G.vertices(); v++) {
			if (v == s) continue;
			if (edgeTo[v] == null && distanceTo[v] != Double.POSITIVE_INFINITY) {
				System.err.println("distanceTo[] y edgeTo[] no son consistentes");
				return false;
			}
		}

		// para todos los arcos e = v->w cumplen distanceTo[w] <= distanceTo[v] + e.comps()
		for (int v = 0; v < G.vertices(); v++) 
			for (Edge e : graph[v].edges()) {
				int w = e.to();
				if (distanceTo[v] + e.comps() < distanceTo[w]) {System.err.println("El arco " + e + " no ha sido relajado");return false;}
			}

		// para todos los arcos e = v->w on SPT cumple que distanceTo[w] == distanceTo[v] + e.comps()
		for (int w = 0; w < G.vertices(); w++) {
			if (edgeTo[w] == null) continue;
			Edge e = edgeTo[w];
			int v = e.from();
			if (w != e.to()) return false;
			if (distanceTo[v] + e.comps() != distanceTo[w]) {
				System.err.println("El arco " + e + " on shortest path not tight");
				return false;
			}
		}
		return true;
	}
}