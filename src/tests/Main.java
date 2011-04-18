package tests;

import java.sql.*;
import org.postgis.*;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		java.sql.Connection conn;
		
		try {
			Class.forName("org.postgresql.DRIVER");
			String url = "jdbc:postgresql://localhost:5432/laesodb";
			conn = DriverManager.getConnection(url, "postgres", "");
			
			Statement s = conn.createStatement();
			ResultSet r = s.executeQuery("SELECT name, ST_AsText(the_geom) as geom FROM laeso;");
			while(r.next()){
				String name = r.getString(1);
				PGgeometry geom = (PGgeometry) r.getObject(2);
				System.out.println("name: "+name+"| geom: "+ geom.toString());
			}
			s.close();
			conn.close();
		}
		catch( Exception e){
			e.printStackTrace();
		}
	}
}
