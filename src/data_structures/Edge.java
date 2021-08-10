package data_structures;

public class Edge implements Comparable<Edge>{

	/**
	 * distancia del arco
	 */
	private double kilometros;

	/**
	 * cantidad de comparendos en el arco
	 */
	private int cantComparendos;

	/**
	 * lado inicial y final del arco
	 */
	private int lFrom, lTo;

	/**
	 * crea un nuevo arco entre plFrom y plTo con la cantidad de millas descrita
	 * @param plFrom lado inicial
	 * @param plTo lado final
	 * @param miles cantidad de millas del arco
	 */
	public Edge(int plFrom, int plTo, double miles) {
		this(plFrom, plTo, miles, 0);
	}

	/**
	 * crea un nuevo arco entre plFrom y plTo con la cantidad de millas descrita
	 * ademas configura la cantidad de comparendos del mismo
	 * @param plFrom lado inicial
	 * @param plTo lado final
	 * @param klms cantidad de kilometros del arco
	 * @param pcantComparendos cantidad de comparendos del arco
	 */
	public Edge(int plFrom, int plTo, double klms, int pcantComparendos) {
		this.kilometros = klms;
		this.lFrom = plFrom;
		this.lTo = plTo;
		this.cantComparendos = pcantComparendos;
	}

	/**
	 * setea la cantidad de kilometros de un arco
	 * @param klms
	 */
	public void setDistance(double klms) {
		this.kilometros = klms;
	}
	
	public void setComps(int comps) {
		this.cantComparendos = comps;
	}
	public void sumComps(int comps) {
		this.cantComparendos += comps;
	}
	
	/**
	 * @return lado inicial del arco (indice del nodo)
	 */
	public int from() 	{return lFrom;	}

	/**
	 * @return destino del arco (indice del nodo)
	 */
	public int to() 	{return lTo;	}

	/**
	 * @return	peso del arco, dado en millas
	 */
	public double w() 	{return kilometros;	}

	/**
	 * @return peso del arco dado en comparendos
	 */
	public int comps()	{return cantComparendos;}

	public boolean equals(Edge e) {return this.compareTo(e) == 0;}
	@Override public int compareTo(Edge e) {return (this.from()-e.from()==0)?this.to()-e.to():this.from()-e.from();}

	/**
	 * @return un vertice identico al original pero con inicio y destino inversos
	 */
	public Edge reverse() {
		return new Edge(this.lTo, this.lFrom, this.kilometros);
	}
	@Override
	public String toString() {
		return this.lFrom+" -> "+this.lTo+" >> w: "+this.kilometros+", "+this.cantComparendos ;
	}
}