package main;

import project.Input;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

public class Main {
	public static void main(String[] argv) {
		
		/*
	    System.out.println("Convert UTM Reference to Latitude/Longitude");
	    UTMRef utm1 = new UTMRef(626524, 6340113, 'N', 32);
	    System.out.println("UTM Reference: " + utm1.toString());
	    LatLng ll3 = utm1.toLatLng();
	    System.out.println("Converted to Lat/Long: " + ll3.toString());
	    System.out.println();
	    
	    UTMRef utm2 = new UTMRef(615642, 6351721, 'N', 32);
	    System.out.println("UTM Reference: " + utm2.toString());
	    LatLng ll5 = utm2.toLatLng();
	    System.out.println("Converted to Lat/Long: " + ll5.toString());
	    System.out.println();
		*/
		
		Input input = new Input();
		input.start();
	}
}
