package data_structures;

import ValueObjects.Comparendo;
import ValueObjects.Station;
import ValueObjects.VertexWrapper;

public class Vertex implements Comparable<Vertex> {


	/**
	 * Index del vértice como arreglo
	 */
	private int indice;

	/**
	 * coordenadas del vertice
	 */
	private double lon, lat;

	private Comparendo masGrave;

	private RedBlackBST<Comparendo> comparendos;

	private RedBlackBST<Station> estaciones;

	/**
	 * edges from this vertex to another
	 */
	private RedBlackBST<Edge> adj;

	public Vertex(VertexWrapper vc) {
		this(vc.indice, vc.lon, vc.lat);
		for (Edge e : vc.edges)
			this.adj.put(e);
		for (Station s: vc.estaciones)
			this.estaciones.put(s);
		if(!vc.comparendos.isEmpty())masGrave = vc.comparendos.get(0);
		for (Comparendo c: vc.comparendos) {
			if(!vc.comparendos.isEmpty() && c.gravedad().compareTo(masGrave.gravedad())>0)masGrave=c;
			this.comparendos.put(c);
		}
	}

	public Vertex(int id, double plon, double plat) {
		this(id, plon, plat,  new RedBlackBST<Edge>(),
				new RedBlackBST<Comparendo>(), new RedBlackBST<Station>());
	}

	public Vertex(int id, double plon, double plat, RedBlackBST<Edge> adj2,
			RedBlackBST<Comparendo> comparendos2, RedBlackBST<Station> estaciones2) {
		this.indice = id;
		this.lon = plon;
		this.lat = plat;
		this.adj = adj2;
		this.comparendos = comparendos2;
		this.estaciones = estaciones2;
	}

	public String gravedad() {
		return (this.masGrave==null)?"":this.masGrave.gravedad();
	}
	public Comparendo mayorComparendo() {
		if(!this.comparendos.isEmpty())
			return this.comparendos.getMax();
		return null;
	}
	public Station mayorEstacion() {
		if(!this.estaciones.isEmpty())
			return this.estaciones.getMax();
		return null;
	}
	public Edge mayorArco() {
		Edge mayor = null;
		double mayorD = 0;
		for (Edge e : adj.values())
			if(e.w() > mayorD) {
				mayorD = e.w();
				mayor = e;
			}
		
//		if(!this.adj.isEmpty())
//			return this.adj.getMax();
		return mayor;
	}

	/**
	 * identificador del vertice
	 * @return indice del nodo
	 */
	public int indice() {return this.indice;}

	/**
	 * grado del grafo
	 * @return cantidad de arcos del grafo
	 */
	public int degree() {return this.adj.size();}

	/**
	 * latitud del vertice
	 * @return la latitud del vertice
	 */
	public double lat() {
		return this.lat;
	}

	/**
	 * longitud del vertice
	 */
	public double lon() {
		return this.lon;
	}
	/**
	 * @param newEdge edge to find a existent edge
	 * @return true if exist an edge between whit the initial conditions <br> <i>initial conditions: same id</i>
	 */
	public boolean addEdge(Edge newEdge) {
		if(newEdge!=null)
			adj.put(newEdge);
		return true;
	}

//	/**
//	 * Usando la distancia haversiana retorna la distancia entre el vertice actual
//	 * u el punto coordenado dado por parametro en kilometros
//	 * @param plat latitud del punto a medir desde el vertice actual
//	 * @param plon lonhitud del punto a medir desde el vertice actual
//	 * @return la distancia entre los puntos
//	 */
//	public double distanciaCoord(double plon, double plat) {  
//		double earthRadius = 6371,
//				lat1 = Math.toRadians(this.lon()),
//				lat2 = Math.toRadians(plon),
//				long1 = Math.toRadians(this.lat()),
//				long2 = Math.toRadians(plat),
//				dlon = (long2 - long1),  dlat = (lat2 - lat1), sinlat = Math.sin(dlat / 2), sinlon = Math.sin(dlon / 2),
//				c = 2 * Math.asin (Math.min(1.0, Math.sqrt((sinlat * sinlat) + Math.cos(lat1)*Math.cos(lat2)*(sinlon*sinlon))));
//		return 	earthRadius * c;}  

	/**
	 * Listado de las estaciones del vertice actual
	 * @return Iterable con las estaciones
	 */
	public Iterable<Station> estaciones(){
		return this.estaciones.values();
	}
	public int estacionesSize() {
		return this.estaciones.size();
	}

	/**
	 * Aniade una estacion al conjunto de estaciones
	 * @param s nueva estacion
	 */
	public void addStation(Station s) {
		this.estaciones.put(s);
	}

	/**
	 * Listado de los comparendos del vertice actual
	 * @return Iterable con los comparendos
	 */
	public Iterable<Comparendo> comparendos(){
		return this.comparendos.values();
	}
	public int comparendosSize() {
		return this.comparendos.size();
	}


	/**
	 * Aniade un comparendo al conjunto de comparendos
	 * @param s nueva estacion
	 */
	public void addComparendo(Comparendo c) {
		this.comparendos.put(c);
	}

	/**
	 * Para cada vertice retorna los arcos que poseen
	 * @return todos los arcos del grafo (incluye direccionalidades)
	 */
	public Iterable<Edge> edges(){return this.adj.values();}
	public int edgesSize() {
		return this.adj.size();
	}

	public String simpleString() {
		return "Vertice: "+indice()+" coords: ["+lon()+", "+lat()+"]";
	}
	
	/**
	 * Consulta un arco entre el vertice actual (origen) y el vertice parametro (destino)
	 * @param To vertice destino del grafo
	 * @return el arco con origen {@code this} y destino {@code To} 
	 */
	public Edge getEdge(Vertex To) {
		return this.adj.get(new Edge(this.indice, To.indice, 0));}

	@Override
	public int compareTo(Vertex o) {
		return Integer.compare((this.indice()), (o.indice()));
	}
	@Override
	public String toString() {
		return "Vertice: "+indice()+"\n\t\t"
				+ ((comparendosSize()>0)?"Cantidad de comparendos: "+comparendosSize()+"\n\t\t"
						+ "Comparendo mas grave: "+masGrave:"Sin comparendos")+"\n\t\t"
						+"Estaciones relacionadas: "+estacionesSize();
	}

	public void print(){
		System.out.println("\n#"+this.indice+"\tLatitud: " + this.lat + ". Longitud: "+this.lon);
		System.out.println("Total arcos from vertex: " + this.degree() + ".");
		System.out.println("___________________________________");;
	}
}
