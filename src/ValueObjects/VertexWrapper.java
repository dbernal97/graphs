package ValueObjects;

import java.util.ArrayList;

import data_structures.Edge;
import data_structures.Vertex;

public class VertexWrapper {
	public int indice;
	public double lon, lat;
	public ArrayList<Edge> edges;
	public ArrayList<Comparendo> comparendos;
	public ArrayList<Station> estaciones;
	public VertexWrapper(Vertex v) {
		this.indice = v.indice();
		this.lon = v.lon();
		this.lat = v.lat();
		this.edges = new ArrayList<Edge>();
		this.comparendos = new ArrayList<Comparendo>();
		this.estaciones = new ArrayList<Station>();
		for(Edge e: v.edges()) 
			edges.add(e);
		for (Comparendo c : v.comparendos()) 
			comparendos.add(c);	
		for (Station s: v.estaciones())
			estaciones.add(s);
	}
}
