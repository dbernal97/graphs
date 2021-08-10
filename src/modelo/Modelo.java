package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.teamdev.jxmaps.CircleOptions;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.PolylineOptions;
import com.teamdev.jxmaps.Size;

import ValueObjects.Comparendo;
import ValueObjects.ComparendoWrapper;
import ValueObjects.Mapa;
import ValueObjects.Station;
import ValueObjects.StationWrapper;
import ValueObjects.VertexWrapper;
import data_structures.*;
import logic.*;

public class Modelo {

	private GrafoNoDirigido mainG;

	public Modelo() {
		mainG = new GrafoNoDirigido();
		System.gc();
	}

	public void req_1A(double plon1, double plat1,double plon2, double plat2) {
		Vertex v1 = closestTo(plon1, plat1);
		Vertex v2 = closestTo(plon2, plat2);
		Dijkstra dc = new Dijkstra(mainG, v1.indice());
		Iterable<Edge> djp = dc.pathTo(v2.indice());
		if(djp == null)
			System.out.println("No hay un camino: "+v1.indice()+" -> "+v2.indice());
		else {
			System.out.println(v1.simpleString());
			int total =0;
			for (Edge e: djp) {
				System.out.println("\t"+mainG.getById(e.to()).simpleString());
				total++;
			}
			System.out.println("Total de vertices: "+(total+1));
			System.out.println("\nCosto: "+dc.distanceTo(v2.indice()));
			System.out.println("Suma de costos: "+dc.distanceTo(v2.indice()));
			QueStack<Vertex> q = new QueStack<Vertex>();
			q.enqueue(v1);q.enqueue(v2);
			print("Dijstra", mainG, djp, q, false); // printing path without copstations
		}
	}
	public void req_2A(int M) {
		RedBlackBST<Vertex> MNodes = mMasGraves2(M);
		Prim prim = new Prim(mainG);
		GrafoNoDirigido subgraph = new GrafoNoDirigido();
		for(Edge e: prim.edges()) {
			Vertex v1 = subgraph.getById(e.from()),
					v2 = subgraph.getById(e.to());
			if(v1 == null) {
				v1 = mainG.getById(e.from());
				subgraph.addVertice(new Vertex(v1.indice(), v1.lon(), v1.lat()));
			}
			if(v2 == null) {
				v2 = mainG.getById(e.to());
				subgraph.addVertice(new Vertex(v2.indice(), v2.lon(), v2.lat()));
			}
			subgraph.addEdge(e);
		}
		prim = null;
		subgraph.reindex();
		QueStack<Edge> ans = new QueStack<Edge>();
		boolean visited[] = new boolean[subgraph.vertices()];
		Vertex init = subgraph.getById(0);
		DFS(subgraph, init, visited, MNodes, ans);
		init=null; visited = null;
		double costo = 0;
		Edge temp = ans.dequeue();
		System.out.println("Vertice: "+temp.from());
		System.out.println(temp);
		System.out.println("Vertice: "+temp.to());
		costo += temp.w();
		for(Edge e: ans) {
			costo+=e.w();
			System.out.println(e);
			System.out.println("Vertice: "+e.to());
		}
		ans.enqueue(temp);
		temp = null;
		System.gc();
		int size = ans.size();
		LatLng[] path = new LatLng[size+1];
		Edge etemp = ans.dequeue();
		Vertex vtemp1 = subgraph.getById(etemp.from()), vtemp2 = subgraph.getById(etemp.to());
		path[0] = new LatLng(vtemp1.lat(), vtemp1.lon());
		path[1] = new LatLng(vtemp2.lat(), vtemp2.lon());
		vtemp2=null;
		for(int j=2; j<path.length;j++) {
			etemp = ans.dequeue();
			vtemp1 = subgraph.getById(etemp.to());
			path[j]= new LatLng(vtemp1.lat(), vtemp1.lon());
		}
		vtemp1=null;
		etemp=null;
		ans=null;
		System.out.println("\nTotal de vertices: "+(size+1));
		System.out.println("costo total: U$ "+costo*10000);
		System.gc();
		printPath2("Pruned MST", path);
//		printPath("Pruned MST", path);			//Change for area but fast loading
	}

	public void req_1B(double plon1, double plat1, double plon2, double plat2) {
		Vertex v1 = closestTo(plon1, plat1);
		Vertex v2 = closestTo(plon2, plat2);
		DijkstraComparendos dc = new DijkstraComparendos(mainG, v1.indice());
		Iterable<Edge> djp = dc.pathTo(v2.indice());
		if(djp == null)
			System.out.println("No hay un camino: "+v1.indice()+" -> "+v2.indice());
		else {
			System.out.println(v1.simpleString());
			int total =0;
			for (Edge e: djp) {
				System.out.println("\t"+mainG.getById(e.to()).simpleString());
				total++;
			}
			System.out.println("Total de vertices: "+(total+1));
			System.out.println("\nCosto: "+dc.distanceTo(v2.indice()));
			System.out.println("Suma de costos: "+dc.distanceTo(v2.indice()));
			QueStack<Vertex> q = new QueStack<Vertex>();
			q.enqueue(v1);q.enqueue(v2);
			print("Dijstra", mainG, djp, q, false); // printing path without copstations
		}
	}
	public void req_2B(int M) {
		RedBlackBST<Vertex> MNodes = mMasComparendos(M);
		Prim prim = new Prim(mainG);
		GrafoNoDirigido subgraph = new GrafoNoDirigido();
		for(Edge e: prim.edges()) {
			Vertex v1 = subgraph.getById(e.from()),
					v2 = subgraph.getById(e.to());
			if(v1 == null) {
				v1 = mainG.getById(e.from());
				subgraph.addVertice(new Vertex(v1.indice(), v1.lon(), v1.lat()));
			}
			if(v2 == null) {
				v2 = mainG.getById(e.to());
				subgraph.addVertice(new Vertex(v2.indice(), v2.lon(), v2.lat()));
			}
			subgraph.addEdge(e);
		}
		prim = null;
		subgraph.reindex();
		QueStack<Edge> ans = new QueStack<Edge>();
		boolean visited[] = new boolean[subgraph.vertices()];
		Vertex init = subgraph.getById(0);
		DFS(subgraph, init, visited, MNodes, ans);
		init=null; visited = null;
		double costo = 0;
		Edge temp = ans.dequeue();
		System.out.println("Vertice: "+temp.from());
		System.out.println(temp);
		System.out.println("Vertice: "+temp.to());
		costo += temp.w();
		for(Edge e: ans) {
			costo+=e.w();
			System.out.println(e);
			System.out.println("Vertice: "+e.to());
		}
		ans.enqueue(temp);
		temp = null;
		System.gc();
		int size = ans.size();
		LatLng[] path = new LatLng[size+1];
		Edge etemp = ans.dequeue();
		Vertex vtemp1 = subgraph.getById(etemp.from()), vtemp2 = subgraph.getById(etemp.to());
		path[0] = new LatLng(vtemp1.lat(), vtemp1.lon());
		path[1] = new LatLng(vtemp2.lat(), vtemp2.lon());
		vtemp2=null;
		for(int j=2; j<path.length;j++) {
			etemp = ans.dequeue();
			vtemp1 = subgraph.getById(etemp.to());
			path[j]= new LatLng(vtemp1.lat(), vtemp1.lon());
		}
		vtemp1=null;
		etemp=null;
		ans=null;
		System.out.println("\nTotal de vertices: "+(size+1));
		System.out.println("costo total: U$ "+costo*10000);
		System.gc();
		printPath2("Pruned MST", path);
//		printPath("Pruned MST", path);			//Change for area but fast loading
	}

	public Iterable<Iterable<Edge>> req_1C(int M, boolean print) {
		Iterable<Vertex> MNodes = mMasGraves(M),
				estaciones = vEstaciones();
		class three{	//Tripleta de valores [distancia, comparendo, camino]
			double dist; 
			Vertex comp;
			Iterable<Edge> path;
			three(double d, Vertex c, Iterable<Edge> p){dist =d; comp=c; path=p;}
		}
		three comps[] = new three[M];
		int i =0;
		for(Vertex mg: MNodes)
			comps[i++]= new three(Double.POSITIVE_INFINITY, mg, null); // inicializo los comparendos inalcanzables
		Dijkstra dj;
		for(Vertex station: estaciones) {
			dj = new Dijkstra(mainG, station.indice()); // busco los caminos desde las estaciones
			for(i=0;i<comps.length;++i) 				// para cada comparendo busco la estacion mas cercana
				if(dj.hasPathTo(comps[i].comp.indice()) && dj.distanceTo(comps[i].comp.indice())<comps[i].dist)
					comps[i]=new three(dj.distanceTo(comps[i].comp.indice()), comps[i].comp, dj.pathTo(comps[i].comp.indice()));
		}
		Queue<Iterable<Edge>> paths = new Queue<Iterable<Edge>>();
		for(three t: comps)
			if(t.path == null) {
				System.out.println("Hay un path nulo a: "+t.comp);
				Dijkstra dtemp = new Dijkstra(mainG, t.comp.indice());
				boolean confirm = true;
				Vertex fr = null;
				for (Vertex stat : estaciones)
					if(dtemp.hasPathTo(stat.indice())) {
						confirm=false;
						fr = stat;
					}
				System.out.println((confirm)?"confirmado":"si hay path! desde: "+fr);
			}
			else
				paths.enqueue(t.path);
		int j =0; double costo1c = 0;
		for(Iterable<Edge> path: paths) {
			System.out.println("Path #"+(j++));
			for(Edge e: path) {
				costo1c += e.w();
				System.out.println("\t"+e);
			}

		}
		System.out.println("\nCosto total: "+costo1c);
		if(print)
			printMultiplePath("Estaciones a comparendos", MNodes, true, paths);
		return paths;
	}
	public void req_2C(Iterable<Iterable<Edge>> g, int n) {
		if(g == null)g = req_1C(n, false);
		GrafoNoDirigido subgraph = new GrafoNoDirigido();
		for(Iterable<Edge> path: g)
			for(Edge e: path) {
				Vertex v1 = subgraph.getById(e.from()),
						v2 = subgraph.getById(e.to());
				if(v1 == null) {
					v1 = mainG.getById(e.from());
					subgraph.addVertice(new Vertex(v1.indice(), v1.lon(), v1.lat()));
				}
				if(v2 == null) {
					v2 = mainG.getById(e.to());
					subgraph.addVertice(new Vertex(v2.indice(), v2.lon(), v2.lat()));
				}
				subgraph.addEdge(e);
			}
		subgraph.reindex();
		boolean[] visited = new boolean[subgraph.vertices()]; 
		RedBlackBST<QueStack<Vertex>> componentes = new RedBlackBST<QueStack<Vertex>>();
		int i =0;
		for(Vertex v: subgraph.allVertices())
			if(!visited[i++]){
				QueStack<Vertex> componente = new QueStack<Vertex>();
				componente.enqueue(v);
				DFS(subgraph,v.indice(),visited, componente);
				componentes.put(componente);
			}
		GuardarGrafo.escribir("./data/grafoComponentes.json", subgraph);
		System.out.println("Vertices: "+subgraph.vertices()+" Arcos: "+subgraph.edges());
		printComponents("2C con "+componentes.size()+" componentes",componentes, subgraph);
	}

	private void DFS(GrafoNoDirigido g, int v, boolean[] visited, QueStack<Vertex> memory) { 
		visited[v] = true; 
		for(Edge e: g.getById(v).edges())
			if(!visited[e.to()]) {
				memory.enqueue(g.getById(v));
				DFS(g, e.to(), visited, memory); 
			}
	} 

	private boolean DFS(GrafoNoDirigido g, Vertex v, boolean[] visited, RedBlackBST<Vertex> comps, QueStack<Edge> mem) { 
		visited[v.indice()] = true; 										// Marco el nodo actual
		boolean toRet = false;												// aún no es necesario en el camino
		if(comps.contains(v)) { 											// si se encuentra entre los vertices destino lo quito (eficicencia) y lo marco como true
			toRet = true;
			comps.delete(v);
		}
		for(Edge e: v.edges())												// Reviso los arcos de v
			if(!visited[e.to()]){											// Si el vertice no esta marcado reviso en dfs
				boolean add = DFS(g, g.getById(e.to()), visited, comps, mem);
				if(add) {	// Si resulta necesario marco el path como necesario y añano el arco
					mem.enqueue(e);
					toRet = true;
				}
			}
		return toRet;
	} 	

	class VWrapComparendos implements Comparable<VWrapComparendos>{
		Vertex value;
		public VWrapComparendos(Vertex v) {this.value=v;}
		@Override public int compareTo(VWrapComparendos w) {return this.value.comparendosSize()-w.value.comparendosSize();}
		//			@Override public int compareTo(VWrap o) {return this.value.comparendoMasGrave().gravedad().compareTo(o.value.comparendoMasGrave().gravedad());}
	}
	class VWrapGravedad implements Comparable<VWrapGravedad>{
		Vertex value;
		public VWrapGravedad(Vertex v) {this.value=v;}
		@Override public int compareTo(VWrapGravedad w) {return this.value.gravedad().compareTo(w.value.gravedad());}
		//			@Override public int compareTo(VWrap o) {return this.value.comparendoMasGrave().gravedad().compareTo(o.value.comparendoMasGrave().gravedad());}
	}
	private RedBlackBST<Vertex> mMasComparendos(int M){
		RedBlackBST<VWrapComparendos> pila = new RedBlackBST<VWrapComparendos>();
		VWrapComparendos min, temp;
		for(Vertex v: mainG.allVertices()) { // M vertices con mas comparendos
			temp = new VWrapComparendos(v);
			if(pila.size()<M)
				pila.put(temp);
			else {
				min = pila.getMin();
				if(temp.compareTo(min) >0) {
					pila.deleteMin();
					pila.put(temp);
				}			
			}
		}
		min = null; temp = null;
		System.out.println(pila.size());
		RedBlackBST<Vertex> toRet = new RedBlackBST<Vertex>();
		for(VWrapComparendos vw: pila.values())toRet.put(vw.value);
		pila = null;
		System.gc();
		return toRet;
	}
	private Iterable<Vertex> mMasGraves(int M){
		//		RedBlackBST<VWrap> pila = new RedBlackBST<VWrap>();VWrap min, temp;for(Vertex v: mainG.allVertices()) { // M vertices con mas comparendos
		//			temp = new VWrap(v);if(pila.size()<M)pila.put(temp);else {min = pila.getMin();if(temp.compareTo(min) >0) {pila.deleteMin();pila.put(temp);}}}
		//for(VWrap vw: pila.values())toRet.enqueue(vw.value);
		MaxPQ<VWrapGravedad> pila = new MaxPQ<VWrapGravedad>(mainG.vertices());
		for(Vertex v: mainG.allVertices())  // M vertices con mas comparendos
			pila.insert(new VWrapGravedad(v));
		QueStack<Vertex> toRet = new QueStack<Vertex>();
		for(int i=0; i<M;++i)
			toRet.enqueue(pila.delMax().value);
		System.out.println("M = "+M+" adquiridos: "+toRet.size());
		//hasta aquí
		return toRet;
	}
	private RedBlackBST<Vertex> mMasGraves2(int M){
		RedBlackBST<VWrapGravedad> pila = new RedBlackBST<VWrapGravedad>();
		VWrapGravedad min, temp;
		for(Vertex v: mainG.allVertices()) { // M vertices con mas comparendos
			temp = new VWrapGravedad(v);
			if(pila.size()<M)
				pila.put(temp);
			else {
				min = pila.getMin();
				if(temp.compareTo(min) >0) {
					pila.deleteMin();
					pila.put(temp);
				}			
			}
		}
		min = null; temp = null;
		System.out.println(pila.size());
		RedBlackBST<Vertex> toRet = new RedBlackBST<Vertex>();
		for(VWrapGravedad vw: pila.values())toRet.put(vw.value);
		pila = null;
		System.gc();
		return toRet;
	}
	
	// Vertices con estaciones	
	private Iterable<Vertex> vEstaciones(){
		QueStack<Vertex> ret = new QueStack<Vertex>();
		for(Vertex v: mainG.allVertices())if(v.estacionesSize()>0)ret.enqueue(v);
		return ret;
	}

	public void cargarGrafo(String ruta) {
		try {
			Gson gson = new Gson();
			JsonReader r = new JsonReader(new InputStreamReader(new FileInputStream(new File(ruta)), "UTF-8"));
			r.beginArray();

			System.out.println(" ¬ Inicio carga del grafo");
			Vertex vtemp = new Vertex(-1, 0, 0);
			int totalComparendos = 0, totalEstaciones = 0, totalArcos=0;
			Comparendo mayorComp = null;
			Station mayorStation = null;
			Edge mayorArco 		 = null;
			while (r.hasNext()) {
				VertexWrapper vc = gson.fromJson(r, VertexWrapper.class);
				Vertex newVertex = new Vertex(vc);
				if(newVertex.indice()>vtemp.indice()) vtemp = newVertex;
				if(newVertex.mayorComparendo()!=null && (mayorComp==null || newVertex.mayorComparendo().id()>mayorComp.id()))mayorComp = newVertex.mayorComparendo();
				if(newVertex.mayorEstacion()!=null &&(mayorStation ==null || newVertex.mayorEstacion().id() > mayorStation.id()))mayorStation = newVertex.mayorEstacion();
				if(mayorArco==null || newVertex.mayorArco().w()>mayorArco.w())mayorArco = newVertex.mayorArco();
				totalComparendos+= newVertex.comparendosSize();
				totalEstaciones+=newVertex.estacionesSize();
				totalArcos+=newVertex.edgesSize();
				mainG.addVertice(newVertex);
			}
			if(mainG.edges() != totalArcos)mainG.setEdgesSize(totalArcos);
			//			loadCompsInEdges();
			//			for (Edge e : mainG.allEdges()) {
			//				Vertex v1 = mainG.getById(e.from()), v2 = mainG.getById(e.to());
			//				e.setDistance(Haversine.distance(v1.lat(), v1.lon(), v2.lat(), v2.lon()));
			//			}
			//			GuardarGrafo.escribir("./data/GrafoFinal.json", mainG);
			System.out.println("[-] Finalizo la carga del grafo\n\n");
			System.out.println("Total comparendos: "+totalComparendos+"\n"
					+ "\tMayor comparendo:\n\t"+mayorComp.toString()+"\n"
					+ "Total estaciones: "+totalEstaciones+"\n"
					+ "\t mayor estacion: "+mayorStation.toString()+"\n"
					+ "Total vertices: "+mainG.vertices()+"\n"
					+ "\t mayor vertice: "+vtemp.toString()+"\n"
					+ "Total arcos: "+mainG.edges()+"\n"
					+ "\t mayor arco: "+mayorArco.toString());
		} catch (IOException e) {e.printStackTrace();}
	}

	public void cargarGrafo() throws NumberFormatException, IOException {

		Gson gson 				= new GsonBuilder().create();
		int	totalComparendos 	= 0,
				totalEstaciones = 0;
		Queue<Edge> ejes 		= new Queue<Edge>();
		String rute_graph 		= "./data/grafo.json",
				rute_stations	= "./data/estacionpolicia.geojson",
				rute_comps		= "./data/comparendosShort.geojson";

		JsonReader r = new JsonReader(new InputStreamReader(new FileInputStream(new File(rute_graph)), "UTF-8"));
		r.beginArray();	//Se carga el archivo json con gson

		System.out.println("¬ Inicio carga vertices");

		//Carga de los vertices
		int idTemp = 0;
		while (r.hasNext()) {
			VertexWrapper vc = gson.fromJson(r, VertexWrapper.class);
			if(vc.indice>idTemp)idTemp=vc.indice;
			mainG.addVertice(vc.indice, vc.lon, vc.lat);
			for (Edge edge : vc.edges) ejes.enqueue(edge);
		}

		System.out.println("[-] Finalizo carga vertices");
		System.out.println(" ¬ Inicio carga arcos");

		//Carga de los arcos
		Edge mayorArco = ejes.peek();
		for (Edge e : ejes) {
			if(e.from() > mayorArco.from() && e.to()> mayorArco.to())
				mayorArco = e;
			mainG.addEdge(new Edge(e.from(), e.to(), e.w()));
		}

		System.out.println("[-] Finalizo carga arcos");
		System.out.println("¬ Inicio carga estaciones");

		//carga de las estaciones de policia
		r.close(); r = new JsonReader(new InputStreamReader(new FileInputStream(new File(rute_stations)), "UTF-8"));
		StationWrapper sw = gson.fromJson(r, StationWrapper.class);
		Station	mayorStat = sw.features().get(0);
		totalEstaciones = sw.features().size();
		for (Station s : sw.features()) {
			if(s.id()>mayorStat.id())
				mayorStat = s;
			closestTo(s.coords()[0], s.coords()[1]).addStation(s);
		}

		System.out.println("[-] Finalizo carga estaciones");
		System.out.println("¬ Inicio carga comparendos");

		//carga de los comparendos
		r.close(); r = new JsonReader(new InputStreamReader(new FileInputStream(new File(rute_comps)), "UTF-8"));
		ComparendoWrapper cw = gson.fromJson(r, ComparendoWrapper.class);
		totalComparendos = cw.features().size();//527655
		Comparendo	mayorComp = cw.features().get(0);
		int centinela=0;
		for (Comparendo c : cw.features()) {
			centinela++;
			if(centinela%1000==0)
				System.out.println("voy "+centinela);
			if(c.id()>mayorComp.id())
				mayorComp = c;
			closestTo(c.coords()[0], c.coords()[1]).addComparendo(c);
		}
		System.out.println("[-] Finalizo carga comparendos");

		//		GuardarGrafo.escribir("./data/grafoCompletoPrueba.json", mainG);

		System.out.println("Total comparendos: "+totalComparendos+"\n"
				+ "\t mayor comparendo: "+mayorComp.toString()+"\n"
				+ "Total estaciones: "+totalEstaciones+"\n"
				+ "\t mayor estacion: "+mayorStat.toString()+"\n"
				+ "Total vertices: "+mainG.vertices()+"\n"
				+ "\t mayor vertice: "+mainG.getById(idTemp).toString()+"\n"
				+ "Total arcos: "+ejes.size()+"\n"
				+ "\t mayor arco: "+mayorArco.toString());
		int contadorEstaciones = 0,
				contadorComparendos=0,
				contadorArcos=0;
		for (Vertex v : mainG.allVertices()) {
			contadorEstaciones += (v.estacionesSize()>0)?1:0;
			contadorComparendos+= v.comparendosSize();
			contadorArcos +=v.edgesSize();
		}
		System.out.println("Tamaños cantidades en el grafo:\n\tvertices con estaciones: "+contadorEstaciones+"\n\tarcos: "+contadorArcos+"\n\tcomparendos: "+contadorComparendos);
		loadCompsInEdges();
	}

	private Vertex closestTo(double lon, double lat) {
		Vertex temp = null;
		double minDist = Integer.MAX_VALUE;
		for (Vertex v : mainG.allVertices()) {
			double d = Haversine.distance(lat, lon, v.lat(), v.lon());
			if(d < minDist) {
				minDist = d;
				temp=v;
			}
		}
		return temp;
	}

	/**
	 * setea el peso de los arcos como la suma de los pesos entre sus vertices
	 */
	private void loadCompsInEdges() {
		for (Vertex v : mainG.allVertices())
			for(Edge e: v.edges()) {
				if(e.comps()>0)continue;
				int comps =v.comparendosSize();
				Vertex temp = mainG.getById(e.to());
				comps+=temp.comparendosSize();
				Edge other = temp.getEdge(v);
				e.setComps(comps);
				other.setComps(comps); 
			}
	}

	private void printMultiplePath(String title,Iterable<Vertex> marks, boolean stations, Iterable<Iterable<Edge>> paths) {
		Mapa map = new Mapa(title);
		if(stations) {
			System.out.println("Painting cop stations ");
			for(Vertex v: mainG.allVertices())
				if(v.estacionesSize()>0)
					map.generateMarkerWithIcon(new LatLng(v.lat(), v.lon()), "./data/cop.png", new Size(50.0,50.0));
		}

		if(marks != null)
			for(Vertex v: marks)
				map.generateMarker(new LatLng(v.lat(), v.lon()));
		if(paths != null)
			for(Iterable<Edge> es: paths) {
				String color = map.getRandomColor();
				PolylineOptions settingsLine=new PolylineOptions();
				settingsLine.setGeodesic(true);
				settingsLine.setStrokeColor(color);
				settingsLine.setStrokeOpacity(1.0);
				settingsLine.setStrokeWeight(2.0);

				map.setSettingsLine(settingsLine);
				for(Edge e: es) {
					Vertex v1= mainG.getById(e.from()), v2 = mainG.getById(e.to());
					map.generateSimplePath(new LatLng(v1.lat(),v1.lon()), new LatLng(v2.lat(),v2.lon()));
				}
			}
	}

	private void printComponents(String title, RedBlackBST<QueStack<Vertex>> componentes, GrafoNoDirigido g) {
		Mapa map = new Mapa(title);
		for (QueStack<Vertex> q: componentes.values()) {
			String color = map.getRandomColor();
			PolylineOptions settingsLine=new PolylineOptions();
			settingsLine.setGeodesic(true);
			settingsLine.setStrokeColor(color);
			settingsLine.setStrokeOpacity(1.0);
			settingsLine.setStrokeWeight(2.0);

			map.setSettingsLine(settingsLine);
			int verticesenc=0;
			for(Vertex v: q) {
				verticesenc++;
				for(Edge e: v.edges()) {
					Vertex v2 = g.getById(e.to());
					map.generateSimplePath(new LatLng(v.lat(),v.lon()), new LatLng(v2.lat(),v2.lon()));
				}
			}
			System.out.println("Vertices en el componente: "+verticesenc);
		}
		for(Vertex vs :vEstaciones()) {
			if(vs.estacionesSize() == 0)
				continue;
			String colorc = map.getRandomColor();
			double radio=((7.5)*vs.comparendosSize());
//			System.out.println("Printing station");
			CircleOptions settingsCircle=new CircleOptions();
			settingsCircle.setFillColor(colorc);
			settingsCircle.setRadius(radio);
			settingsCircle.setFillOpacity(0.35);
			settingsCircle.setStrokeColor(colorc);
			map.setSettingsCircle(settingsCircle);
			map.generateArea(new LatLng(vs.lat(), vs.lon()), radio);
			map.generateMarkerWithIcon(new LatLng(vs.lat(), vs.lon()), "./data/cop.png", new Size(50.0,50.0));
			String idestaciones = "";
			for(Station s: vs.estaciones())
				idestaciones+=s.id()+", ";
			System.out.println("color:" +colorc+" idEstacion(es): ["+idestaciones+"]");
		}
	}

	public void printPath(String title, LatLng ... path ) {
		Mapa map = new Mapa(title);
		map.generateLine(false, path);
	}
	
	public void printPath2(String title, LatLng ... path ) {
		Mapa map = new Mapa(title);
		LatLng init = path[0];
		for(int i=1; i< path.length; ++i) {
			map.generateSimplePath(init, path[i]);
			init=path[i];
		}
//		map.generateLine(false, path);
	}
	
	public void print(String title, GrafoNoDirigido g,  Iterable<Edge> es, Iterable<Vertex> marks, boolean stations) {
		Mapa map = new Mapa(title);
		if(stations) {
			System.out.println("Painting cop stations ");
			for(Vertex v: g.allVertices())
				if(v.estacionesSize()>0)
					map.generateMarkerWithIcon(new LatLng(v.lat(), v.lon()), "./data/cop.png", new Size(50.0,50.0));
		}
		if(marks != null)
			for(Vertex v: marks)
				map.generateMarker(new LatLng(v.lat(), v.lon()));
		marks = null;
		if(es != null) {
			String color = map.getRandomColor();
			map.setColorLine(color);	
			for(Edge e: es) {
				Vertex v1= g.getById(e.from()), v2 = g.getById(e.to());
				map.generateSimplePath(new LatLng(v1.lat(),v1.lon()), new LatLng(v2.lat(),v2.lon()));
			}
		}
	}
}


