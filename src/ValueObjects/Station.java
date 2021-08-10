package ValueObjects;

public class Station implements Comparable<Station>{
	int id;
	Info properties;

	public int id() {
		return this.properties.OBJECTID;
	}
	
	public double[] coords() {
		return new double[] {this.properties.EPOLONGITU, this.properties.EPOLATITUD};
	}
	
	@Override
	public String toString() {
		return "- Estación: "+properties.EPODESCRIP+" ("+properties.OBJECTID+")\n"
				+ "\tdir: "+properties.EPODIR_SITIO+"; Horario: "+properties.EPOHORARIO+"\n"
				+ "\tlocalidad: "+properties.EPOIULOCAL+"; telefono: "+properties.EPOTELEFON+"\n"
				+ "\t\t coordenadas: ["+properties.EPOLONGITU+", "+properties.EPOLATITUD+"]\n"
				+ "\""+properties.EPOSERVICIO+"\"" ;
	}

	@Override
	public int compareTo(Station o) {
		return Integer.compare(this.properties.OBJECTID, o.properties.OBJECTID);
	}
}

class Info{
	public double
	EPOLONGITU,
	EPOLATITUD;
	public int
	OBJECTID;
	public String
	EPODESCRIP,
	EPODIR_SITIO,
	EPOSERVICIO,
	EPOHORARIO,
	EPOTELEFON,
	EPOIULOCAL;
}