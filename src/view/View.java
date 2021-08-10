package view;

import java.io.IOException;
import java.util.Scanner;

import ValueObjects.Timer;
import data_structures.Edge;
import modelo.Modelo;

public class View {    
	static Timer t = new Timer();
	static Modelo m;
	public static void main(String[] args) throws NumberFormatException, IOException {
		m = new Modelo();
		m.cargarGrafo("./data/GrafoFinal.json");	//carga el grafo
		Scanner sc = new Scanner(System.in);
		printMenu(sc);
	} 

	public static void cls(){
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	public static void printMenu(Scanner s){
		boolean fin = false;
		while(!fin) {
			System.out.println("\nRequerimientos:\t(X to exit)\n\t"
					+ "A) Problemas basados en millas\n\t"
					+ "B) Problemas basados en cantidad de comparendos\n\t"
					+ "C) Problemas de particion del grafo");
			String cat = s.next();
			cls();
			switch (cat) {
			case "A": 
				printSubMenuA(s);
				break;
			case "B": 
				printSubMenuB(s);
				break;
			case "C": 
				printSubMenuC(s);
				break;
			case "X": 
				fin = true;
				System.out.println("Bye! :)");
				break;
			}
		}
	}

	public static void printSubMenuA(Scanner sc) {
		System.out.println("Requerimientos de A:\n\t"
				+ "1) Obtener el camino de costo mínimo entre dos ubicaciones geográficas por distancia\n\t"
				+ "2) Determinar la red de comunicaciones que soporte la instalación de cámaras de video\n\t\ten los M puntos donde se presentan los comparendos de mayor gravedad.");
		int c = sc.nextInt();
		cls();
		switch (c) {
		case 1:
			System.out.println("Ingrese las coordenadas del primer punto [long,lat]");
			String coord1t = sc.next();
			String coord1[] = coord1t.split(",");
			double plon1 = Double.parseDouble(coord1[0]), plat1=Double.parseDouble(coord1[1]);
			System.out.println("Ingrese las coordenadas del segundo punto [long,lat]");
			String coord2t = sc.next();
			String coord2[] = coord2t.split(",");
			double plon2 = Double.parseDouble(coord2[0]), plat2=Double.parseDouble(coord2[1]);
			t.start();
			m.req_1A(plon1, plat1, plon2, plat2);
			t.stop(10000);
			break;
		case 2:
			System.out.println("Ingrese el valor de M");
			int M = sc.nextInt();
			t.start();
			m.req_2A(M);
			t.stop();
			break;
		default:
			System.err.println("Seleccion erronea");
		}
	}

	public static void printSubMenuB(Scanner sc) {
		System.out.println("Requerimientos de B:\n\t"
				+ "1) Obtener el camino de costo mínimo entre dos ubicaciones geográficas por numero de comparendos\n\t"
				+ "2) Determinar la red de comunicaciones que soporte la instalación de cámaras de video\n\t\ten los M puntos donde se presenta el mayor número de comparendos en la ciudad.");
		int c = sc.nextInt();
		cls();
		switch (c) {
		case 1:
			System.out.println("Ingrese las coordenadas del primer punto [long,lat]");
			String coord1t = sc.next();
			String coord1[] = coord1t.split(",");
			double plon1 = Double.parseDouble(coord1[0]), plat1=Double.parseDouble(coord1[1]);
			System.out.println("Ingrese las coordenadas del segundo punto [long,lat]");
			String coord2t = sc.next();
			String coord2[] = coord2t.split(",");
			double plon2 = Double.parseDouble(coord2[0]), plat2=Double.parseDouble(coord2[1]);
			t.start();
			m.req_1B(plon1, plat1, plon2, plat2);
			t.stop(10000);
			break;
		case 2:
			System.out.println("Ingrese el valor de M");
			int M = sc.nextInt();
			t.start();
			m.req_2B(M);
			t.stop(10000);
			break;
		default:
			System.err.println("Seleccion erronea");
			break;
		}
	}

	public static void printSubMenuC(Scanner sc) {
		System.out.println("Requerimientos de C:\n\t"
				+ "1) Obtener los caminos más cortos para que los policías puedan atender los M comparendos más graves.\n\t"
				+ "2) Identificar las zonas de impacto de las estaciones de policía.");
		int c = sc.nextInt();
		cls();
		Iterable<Iterable<Edge>> c1graph = null;
		switch (c) {
		case 1:
			System.out.println("Ingrese el valor de M");
			int M = sc.nextInt();
			t.start();
			c1graph= m.req_1C(M, true);
			t.stop(10000);
			break;
		case 2:
			System.out.println((c1graph!=null)?"Al parecer cargaste un grafo en 1C, deseas usarlo para este requerimiento? [y|n]":"No has cargado grafo en 1C (para continuar ingresa: n)");
			String cont = sc.next();
			switch (cont) {
			case "y":
				t.start();
				m.req_2C(c1graph, 0); // jajajaja, requerimiento tussi
				t.stop(10000);
				break;
			case "n":
				System.out.println("Podemos cargar el grafo por tí, introduce un número (si es 0 saldrás de este req)");
				int N = sc.nextInt();
				if(N==0)break;
				t.start();
				m.req_2C(null, N); // jajajaja, requerimiento tussi
				t.stop(10000);
				break;
			default:
				System.err.println("Seleccion erronea");
				break;
			}
			break;
		default:
			System.err.println("Seleccion erronea");
			break;
		}
	}
}
