package ValueObjects;

import java.util.Date;

public class Comparendo implements Comparable<Comparendo>{
	Geo geometry;
	InfoC properties;
	
	public int id() {
		return this.properties.OBJECTID;
	}
	
	public double[] coords() {
		return this.geometry.coordinates;
	}

	public String gravedad() {
		String g = new String();
		g += ((this.properties.TIPO_SERVICIO.equals("Particular"))?0:(this.properties.TIPO_SERVICIO.equals("Oficial"))?1:2)+
				this.properties.INFRACCION;
		return g;
	}
	
	@Override
	public String toString() {
		return "\tOBJECTID: "+this.properties.OBJECTID+"\t\tFECHA_HORA: "+new Date(this.properties.FECHA_HORA)+"\n\t"
				+ "\tINFRACCION: "+this.properties.INFRACCION+"\t\t\tCLASE_VEHICULO: "+this.properties.CLASE_VEHICULO+"\n\t"
						+ "\tTIPO_SERVICIO: "+this.properties.TIPO_SERVICIO+"\tLOCALIDAD: "+this.properties.LOCALIDAD+"\n\t";
	}
	
	@Override
	public int compareTo(Comparendo o) {
		return Integer.compare(this.id(), o.id());
	}
}

class Geo{
	String type;
	double[] coordinates;
}

class InfoC{
	int 
	OBJECTID;
	long
	FECHA_HORA;
	String 
	INFRACCION,
	CLASE_VEHICULO,
	TIPO_SERVICIO,
	LOCALIDAD;
}
