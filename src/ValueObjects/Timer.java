package ValueObjects;

public class Timer {

	private long time_start;
//	private long memoryBeforeCase1;

	public void start() {
//		memoryBeforeCase1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		time_start = System.nanoTime();
	}

	public void stop() {
		long time_end = System.nanoTime();
		long total = (time_end - time_start)/(1000000);
//		long memoryAfterCase1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Tiempo en usado: " + total + " milisegundos ");
//				+ "\nMemoria utilizada: "+ ((memoryAfterCase1 - memoryBeforeCase1)/1000000.0) + " MB");
	}
	
	public void stop(long interaction) {
		long time_end = System.nanoTime();
		long total = (time_end - time_start)/(1000000) -interaction;
//		long memoryAfterCase1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Tiempo en usado: " + total + " milisegundos ");
//				+ "\nMemoria utilizada: "+ ((memoryAfterCase1 - memoryBeforeCase1)/1000000.0) + " MB");
	}
}