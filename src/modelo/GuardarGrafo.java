package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import ValueObjects.VertexWrapper;
import data_structures.Edge;
import data_structures.GrafoNoDirigido;
import data_structures.Vertex;
import logic.Haversine;

public class GuardarGrafo {

	static GrafoNoDirigido g = new GrafoNoDirigido();

	public static void main(String[] args) throws IOException {
		File file=new File("./data/Vertices.txt");    	//creates a new file instance  
		FileReader fr=new FileReader(file);   			//reads the file  
		BufferedReader br=new BufferedReader(fr);	  	//creates a buffering character input stream  
		String line;
		/*  Leo primero los vertices del grafo y los aniado al grafo  */
		while((line=br.readLine())!=null) {  
			String temp[] = line.split(",");
			g.addVertice(Integer.parseInt(temp[0]), Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));
		}  
		fr.close();    								//closes the stream and release the resources  
		File file2=new File("./data/Arcos.txt");    //creates a new file instance  
		FileReader fr2=new FileReader(file2);   	//reads the file  
		BufferedReader br2=new BufferedReader(fr2); //creates a buffering character input stream 

		double mayorDistancia =0;
		
		/* Leo ahora los arcos del grafo */
		while((line=br2.readLine())!=null){  
			if(line.contains("#"))
				continue;
			String temp[] = line.split(" ");
			int vfrom = Integer.parseInt(temp[0]);
			Vertex v1 = g.getById(vfrom), v2; 			//Obtengo el vertive con el punto inicial del arco
			for(int i = 1; i<temp.length; ++i) {
				int vto = Integer.parseInt(temp[i]);
				v2 = g.getById(vto);
				double dtemp = Haversine.distance(v1.lat(), v1.lon(), v2.lat(), v2.lon());
				g.addEdge(new Edge(vfrom, vto, dtemp));//aniado el vertice con su peso en kilometros
				if(dtemp>mayorDistancia)
					mayorDistancia=dtemp;
			}
		}
		System.out.println("La mayor distancia es: "+mayorDistancia);
		br2.close();
		String url = "./data/grafo.json";
		escribir(url, g);
	}
	
	public static void escribir(String url, GrafoNoDirigido graph) {
		System.out.println("Escribiendo");
		Gson gson = new Gson();
		try{
			FileWriter fileWriter = new FileWriter(new File(url), true);
			fileWriter.write("[");
			for (Vertex v : graph.allVertices())
				fileWriter.write(gson.toJson(new VertexWrapper(v))+",");
			fileWriter.write("]");
			fileWriter.close();
		}catch(Exception e){System.err.println("error en la escritura del archivo JSON");}
		System.out.println("Escrito");

	}
}
