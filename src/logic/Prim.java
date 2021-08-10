package logic;

import data_structures.Edge;
import data_structures.GrafoNoDirigido;
import data_structures.IndexMinPQ;
import data_structures.Queue;

public class Prim {
    private Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public Prim(GrafoNoDirigido G) {
        edgeTo = new Edge[G.vertices()];
        distTo = new double[G.vertices()];
        marked = new boolean[G.vertices()];
        pq = new IndexMinPQ<Double>(G.vertices());
        for (int v = 0; v < G.vertices(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.vertices(); v++)      // run from each vertex to find
            if (!marked[v]) prim(G, v);      // minimum spanning forest
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(GrafoNoDirigido G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    private void scan(GrafoNoDirigido G, int v) {
        marked[v] = true;
        for (Edge e : G.getById(v).edges() ){ //G.adj(v)
            int w = e.to();
            if (marked[w]) continue;         // v-w is obsolete edge
            if (e.w() < distTo[w]) {
                distTo[w] = e.w();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     *    an iterable of edges
     */
    public Iterable<Edge> edges() {
        Queue<Edge> mst = new Queue<Edge>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.enqueue(e);
            }
        }
        return mst;
    }

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double w() {
        double weight = 0.0;
        for (Edge e : edges())
            weight += e.w();
        return weight;
    }

//    /**
//     * Unit tests the {@code PrimMST} data type.
//     *
//     * @param args the command-line arguments
//     */
//    public static void main(String[] args) {
//        In in = new In(args[0]);
//        Grafo G = new GrafoNoDirigido(in);
//        Prim mst = new Prim(G);
//        for (Edge e : mst.edges()) {
//            StdOut.println(e);
//        }
//        StdOut.printf("%.5f\n", mst.w());
//    }


}