package ValueObjects;


import java.awt.BorderLayout;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;


public class Mapa extends MapView{

	private static final long serialVersionUID = 1L;

	/**
	 * The map object
	 */
	private static Map map;

	/**
	 * Editable circle options 
	 */
	private CircleOptions settingsCircle;
	/**
	 * Editable LineOptions
	 */
	private  PolylineOptions settingsLine;

	public CircleOptions getSettingsCircle() {
		return settingsCircle;
	}

	public void setSettingsCircle(CircleOptions settingsCircle) {
		this.settingsCircle = settingsCircle;
	}

	public void setSettingsLine(PolylineOptions settingsLine){
		this.settingsLine=settingsLine;
	}
	
	public void setColorLine(String color)	{
		this.settingsLine.setStrokeColor(color);
	}
	
	public String getRandomColor() {
		String colors[] = 
			{
					"#d50000", "#c51162", "#aa00ff", "#6200ea", "#304ffe", "#2962ff",
					"#0091ea", "#00b8d4", "#00bfa5", "#00c853", "#64dd17", "#aeea00",
					"#ffd600", "#ffab00", "#fffac8", "#ff6d00", "#dd2c00", "#263238", 
					"#212121", "#6d4c41", "#827717", "#000000", "#69f0ae"
			};
		return colors[(int) Math.floor(Math.random()*colors.length)];
	}
	
	/**
	 * Generate a marker on the LatLongPoint
	 * @param pos of the wanted marker
	 * @return Marker
	 */
	public Marker generateMarker(LatLng pos){
		Marker marker = null;
		marker = new Marker(map);
		marker.setPosition(pos);
		map.setCenter(pos);
		return marker;
	}
	
	/**
	 * Generate a marker on the LatLongPoint
	 * @param pos of the wanted marker
	 * @return Marker
	 */
	public Marker generateMarkerWithIcon(LatLng pos, String iconrute, Size s){
		Marker marker = new Marker(map);
		marker.setPosition(pos);
		Icon i = new Icon();
		i.loadFromFile(iconrute);
		i.setScaledSize(s);
		marker.setIcon(i);
		return marker;
	}

	/**
	 * Generate a simple nibe between two LatLong points
	 * @param start Start point of the line
	 * @param end End point of the line
	 * @param markers Do you wanna put a marker on the LatLong points
	 */
	public void generateSimplePath(LatLng start,LatLng end)	{
		LatLng[] path = {start,end};
		Polyline polyline = new Polyline(map);
		polyline.setPath(path);
		polyline.setOptions(settingsLine);
	}

	public static Map darMap(){
		return map;
	}

	/**
	 * Generate a circle area on the map
	 * @param center LatLong of the center of the map
	 * @param radius on meters
	 */
	public void generateArea(LatLng center,Double radius){
		Circle circle = new Circle(map);
		circle.setCenter(center);
		circle.setRadius(radius);
		circle.setVisible(true);
		circle.setOptions(settingsCircle);
	}

	/**
	 * Generate a line on the Map on the selected breakpoints
	 * @param markers  do you wanna put a marker on each vertex 
	 * @param path Group of points of the Line
	 */
	public void generateLine(boolean markers,LatLng... path){
		if(markers)
			for(LatLng p:path)
				generateMarker(p);
		
		Polyline polyline = new Polyline(map);
		polyline.setPath(path);
		polyline.setOptions(settingsLine);
	}

	/**
	 * Create a new Map panel whit the param Name
	 * @param pString Name for the map
	 */
	public Mapa(String pString) {

		JFrame frame = new JFrame(pString);

		settingsCircle=new CircleOptions();
		settingsCircle.setFillColor("#ab47bc");
		settingsCircle.setRadius(2000000);
		settingsCircle.setFillOpacity(0.35);
		
		settingsLine=new PolylineOptions();
		settingsLine.setGeodesic(true);
		settingsLine.setStrokeColor("#ab47bc");
		settingsLine.setStrokeOpacity(1.0);
		settingsLine.setStrokeWeight(2.0);

		setOnMapReadyHandler(new MapReadyHandler() {
			@Override
			public void onMapReady(MapStatus status) {
				// Check if the map is loaded correctly
				if (status == MapStatus.MAP_STATUS_OK) {
					// Getting the associated map object
					map = getMap();
					MapOptions mapOptions = new MapOptions();
					MapTypeControlOptions controlOptions = new MapTypeControlOptions();
					controlOptions.setPosition(ControlPosition.BOTTOM_LEFT);
					mapOptions.setMapTypeControlOptions(controlOptions);
					map.setOptions(mapOptions);
					map.setCenter(new LatLng(4.36, -74.05));
					map.setZoom(10);
				}
			}
		});
		System.out.println("Print state  >||||||||||<");
		System.out.print(  "Loading map   ");
		try 
		{
			for(int i=0;i<10;i++)
			{
				TimeUnit.SECONDS.sleep(1);
				System.out.print("*");
			}
			System.out.println();
		} 
		catch (InterruptedException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.add(this, BorderLayout.CENTER);
		frame.setSize(700, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}