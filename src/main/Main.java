package main;

import project.UserInput;

public class Main {
	public static void main(String[] argv) {
		UserInput input = new UserInput();
		input.start();
	}	
	
	/*
	 * 
    public static void main(String[] argv) {
        System.out.println("Checking if Driver is registered with DriverManager.");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Couldn't find the driver!");
            System.out.println("Let's print a stack trace, and exit.");
            cnfe.printStackTrace();
            System.exit(1);
        }

        System.out.println("Registered the driver ok, so let's make a connection.");

        Connection c = null;

        try {
            // The second and third arguments are the username and password,
            // respectively. They should be whatever is necessary to connect
            // to the database.
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/template_postgis",
                    "postgres", "admin");
        } catch (SQLException se) {
            System.out.println("Couldn't connect: print out a stack trace and exit.");
            se.printStackTrace();
            System.exit(1);
        }

        if (c != null) {
            try {
                System.out.println("Hooray! We connected to the database!");
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("SELECT sum(ST_Length(the_geom))/1000 AS km_roads FROM shapefile;");
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
                rs.close();
            } catch (SQLException se) {
                System.out.println("bla bla kazkoks exception.");
                se.printStackTrace();
                System.exit(1);
            }
        } else {
            System.out.println("We should never get here.");
        }
    }    
	 */
}
