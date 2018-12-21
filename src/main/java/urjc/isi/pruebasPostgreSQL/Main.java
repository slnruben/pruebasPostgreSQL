package urjc.isi.pruebasPostgreSQL;

import static spark.Spark.*;
import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.MultipartConfigElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URISyntaxException;
import java.net.URI;


// This code is quite dirty. Use it just as a hello world example 
// to learn how to use JDBC and SparkJava to upload a file, store 
// it in a DB, and do a SQL SELECT query
public class Main {
    
    // Connection to the PostgreSQL database. Used by insert
    // and select methods.  Initialized in main
    private static Connection connection;

    private static HashMap<String, String> getRequestData(Request request) {
		//System.out.println(request.body());
		String[] aux1 = request.body().split("&");
		HashMap<String, String> params = new HashMap<>();
		String[] aux2;
		for (String item: aux1) {
			aux2 = item.split("=");
			if (aux2.length == 1) {
				params.put(aux2[0], "");
			} else {
				params.put(aux2[0], aux2[1]);
			}
		}
		return params;
	}
    
    // Used to illustrate how to route requests to methods instead of
    // using lambda expressions
    public static JSONArray doSelect(Request request, Response response) throws JSONException, SQLException {
    	String sql = ("SELECT * FROM users");
    	String sueldo;
    	String trabajo;
    	String sector1;
    	String sector2;
    	String conocimientos;
		JSONArray jsonArr = new JSONArray();
		JSONObject json;
		
		connection.setAutoCommit(true);
		HashMap<String, String> params = getRequestData(request);
		sueldo = params.get("Sueldo");
		trabajo = params.get("Trabajo");
		sector1 = params.get("Sector1");
		sector2 = params.get("Sector2");
		conocimientos = params.get("Conocimientos");
		System.out.println(sueldo + "|" + trabajo + "|" + sector1 + "|" + sector2 + "|" + conocimientos);
		if ("".equals(sueldo) && "".equals(trabajo) && "".equals(sector1) && "".equals(sector2)
				&& "".equals(conocimientos)) {
			
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					System.out.println("Damos vuelta 1");
					json = new JSONObject();
					json.put("Id", rs.getString("id"));
					json.put("Nombre", rs.getString("Nombre"));
					json.put("Apellidos", rs.getString("Apellidos"));
					json.put("Email", rs.getString("Email"));
					json.put("Telefono", rs.getString("Telefono"));
					json.put("Trabajo", rs.getString("Trabajo"));
					json.put("Empresa", rs.getString("Empresa"));
					json.put("Sueldo", rs.getString("Sueldo"));
					json.put("Universidad", rs.getString("Universidad"));
					json.put("Carrera", rs.getString("Carrera"));
					json.put("Sector1", rs.getString("Sector1"));
					json.put("Sector2", rs.getString("Sector2"));
					json.put("Experiencia", rs.getString("Experiencia"));
					json.put("Lenguajes", rs.getString("Lenguajes"));
					json.put("Conocimientos", rs.getString("Conocimientos"));
					jsonArr.put(json);	
				}
			} catch (SQLException e) {
				System.out.println("ERROR1: " + e.getMessage());
			}
		} else {
			try (PreparedStatement pstmt2 = connection.prepareStatement(sql)) {
				sql = ("SELECT * FROM users WHERE Sueldo=? OR Trabajo=? OR Sector1=? "
						+ "OR Sector2=? OR Conocimientos=?");
				pstmt2.setString(1, params.get("Sueldo"));
				pstmt2.setString(2, params.get("Trabajo"));
				pstmt2.setString(3, params.get("Sector1"));
				pstmt2.setString(4, params.get("Sector2"));
				pstmt2.setString(5, params.get("Conocimientos"));
				ResultSet rs2 = pstmt2.executeQuery();
				while (rs2.next()) {
					System.out.println("Damos vuelta 2");
					json = new JSONObject();
					json.put("Id", rs2.getString("id"));
					json.put("Nombre", rs2.getString("Nombre"));
					json.put("Apellidos", rs2.getString("Apellidos"));
					json.put("Email", rs2.getString("Email"));
					json.put("Telefono", rs2.getString("Telefono"));
					json.put("Trabajo", rs2.getString("Trabajo"));
					json.put("Empresa", rs2.getString("Empresa"));
					json.put("Sueldo", rs2.getString("Sueldo"));
					json.put("Universidad", rs2.getString("Universidad"));
					json.put("Carrera", rs2.getString("Carrera"));
					json.put("Sector1", rs2.getString("Sector1"));
					json.put("Sector2", rs2.getString("Sector2"));
					json.put("Experiencia", rs2.getString("Experiencia"));
					json.put("Lenguajes", rs2.getString("Lenguajes"));
					json.put("Conocimientos", rs2.getString("Conocimientos"));
					jsonArr.put(json);	
				}
			} catch (SQLException e) {
				System.out.println("ERROR2: " + e.getMessage());
			}
		}

		System.out.println(jsonArr.toString());
		return jsonArr;
    }
    
    // Used to illustrate how to route requests to methods instead of
    // using lambda expressions
    public static String doSelectAll(Request request, Response response) throws JSONException, SQLException {
    	String success = "0";
		JSONArray jsonArr = new JSONArray();
		JSONObject json;
		
		connection.setAutoCommit(true);
		HashMap<String, String> params = getRequestData(request);
		String sql = ("SELECT * FROM users");
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				json = new JSONObject();
				json.put("id", rs.getString("id"));
				json.put("Usuario", rs.getString("Usuario"));
				json.put("Nombre", rs.getString("Nombre"));
				json.put("Apellidos", rs.getString("Apellidos"));
				json.put("Email", rs.getString("Email"));
				json.put("Telefono", rs.getString("Telefono"));
				json.put("Trabajo", rs.getString("Trabajo"));
				json.put("Empresa", rs.getString("Empresa"));
				json.put("Sueldo", rs.getString("Sueldo"));
				json.put("Universidad", rs.getString("Universidad"));
				json.put("Carrera", rs.getString("Carrera"));
				json.put("Sector1", rs.getString("Sector1"));
				json.put("Sector2", rs.getString("Sector2"));
				json.put("Experiencia", rs.getString("Experiencia"));
				json.put("Lenguajes", rs.getString("Lenguajes"));
				json.put("Conocimientos", rs.getString("Conocimientos"));
				jsonArr.put(json);
				
			}
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		System.out.println(jsonArr.toString());
		return success;
    }
    
    public static String doUpdateUser(Request request, Response response) throws SQLException {
		String success = "0";
		
		connection.setAutoCommit(true);
		HashMap<String, String> params = getRequestData(request);
		String sql = "UPDATE users SET Nombre=?, Apellidos=?, Email=?, Telefono=?, Trabajo=?, "
						+ "Empresa=?, Sueldo=?, Universidad=?, Carrera=?, Sector1=?, Sector2=?, "
						+ "Experiencia=?, Lenguajes=?, Conocimientos=? WHERE Usuario=?";
		System.out.println(params.get("Apellidos"));
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			System.out.println("NOMBRE: " + params.get("Nombre"));
			pstmt.setString(1, params.get("Nombre"));
			pstmt.setString(2, params.get("Apellidos"));
			pstmt.setString(3, params.get("Email"));
			pstmt.setString(4, params.get("Telefono"));
			pstmt.setString(5, params.get("Trabajo"));
			pstmt.setString(6, params.get("Empresa"));
			pstmt.setString(7, params.get("Sueldo"));
			pstmt.setString(8, params.get("Universidad"));
			pstmt.setString(9, params.get("Carrera"));
			pstmt.setString(10, params.get("Sector1"));
			pstmt.setString(11, params.get("Sector2"));
			pstmt.setString(12, params.get("Experiencia"));
			pstmt.setString(13, params.get("Lenguajes"));
			pstmt.setString(14, params.get("Conocimientos"));
			pstmt.setString(15, params.get("Usuario"));
			success = Integer.toString(pstmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return success;
	}
	
    public static String doLogin(Request request, Response response) throws JSONException, SQLException {
    	String success = "0";
		
		connection.setAutoCommit(true);
		HashMap<String, String> params = getRequestData(request);
		String sql = ("SELECT count(*) FROM users WHERE Usuario=?");
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, params.get("Usuario"));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) != 0) {
					success = "1";
				} else {
					success = "0";
				}
			}
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return success;
    }
    
	public static String doRegister(Request request, Response response) throws SQLException {
		String last_inserted_id = "-1";
		
		connection.setAutoCommit(true);
		HashMap<String, String> params = getRequestData(request);
		String sql = "SELECT COUNT(*) FROM users WHERE Usuario=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, params.get("Usuario"));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(1) != 0) {
					System.out.println("REPETIDO: " + rs.getInt(1));
					last_inserted_id = "-1";
				} else {
					sql = "INSERT INTO users(Usuario, Sueldo) VALUES(?, ?)";
					try (PreparedStatement pstmt2 = connection.prepareStatement(sql)) {
						pstmt2.setString(1, params.get("Usuario"));
						pstmt2.setString(2, params.get("Sueldo").toString());
						pstmt2.executeUpdate();			
					} catch (SQLException e) {
						System.out.println("ERROR1: " + e.getMessage());
					}
					sql = "SELECT id FROM users WHERE Usuario=?";
					try (PreparedStatement pstmt3 = connection.prepareStatement(sql)) {
						pstmt3.setString(1, params.get("Usuario"));
						ResultSet rs3 = pstmt3.executeQuery();
						if (rs3.next()) {
							last_inserted_id = String.valueOf(rs3.getInt(1));
						}
					} catch (SQLException e) {
						System.out.println("ERROR2: " + e.getMessage());
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("ERROR3: " + e.getMessage());
		}
		System.out.println("INSERTED: " + last_inserted_id);
		return last_inserted_id;
	}
	
	public static String doCreateBBDD(Request request, Response response) {
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate("drop table if exists users");
			statement.executeUpdate("create table users (id SERIAL PRIMARY KEY, Usuario text, Nombre text, "
					+ "Apellidos text, Email text, Telefono text, Trabajo text,"
					+ "Empresa text, Sueldo text, Universidad text, Carrera text,"
					+ "Sector1 text, Sector2 text, Experiencia text, Lenguajes text,"
					+ "Conocimientos text)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}
		return "1";
	}

    public static String select(Connection conn, String table, String film) {
	String sql = "SELECT * FROM " + table + " WHERE film=?";

	String result = new String();
	
	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		pstmt.setString(1, film);
		ResultSet rs = pstmt.executeQuery();
                // Commit after query is executed
		connection.commit();

		while (rs.next()) {
		    // read the result set
		    result += "film = " + rs.getString("film") + "\n";
		    System.out.println("film = "+rs.getString("film") + "\n");

		    result += "actor = " + rs.getString("actor") + "\n";
		    System.out.println("actor = "+rs.getString("actor")+"\n");
		}
	    } catch (SQLException e) {
	    System.out.println(e.getMessage());
	}
	return result;
    }
    
    
    public static void insert(Connection conn, String film, String actor) {
	String sql = "INSERT INTO films(film, actor) VALUES(?,?)";

	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		pstmt.setString(1, film);
		pstmt.setString(2, actor);
		pstmt.executeUpdate();
	    } catch (SQLException e) {
	    System.out.println(e.getMessage());
	}
    }

    public static void main(String[] args) throws 
	ClassNotFoundException, SQLException, URISyntaxException {
	port(getHerokuAssignedPort());
	

	// This code only works for PostgreSQL in Heroku
	// Connect to PostgreSQL in Heroku
	URI dbUri = new URI(System.getenv("DATABASE_URL"));
	String username = dbUri.getUserInfo().split(":")[0];
	String password = dbUri.getUserInfo().split(":")[1];
	String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
	connection = DriverManager.getConnection(dbUrl, username, password);
	
	// PostgreSQL default is to auto-commit (1 transaction / statement execution)
        // Set it to false to improve performance
	connection.setAutoCommit(false);

	get("/create_bbdd", Main::doCreateBBDD);
	
	get("/search_contacts", Main::doSelect);
	
	get("/get_contacts",  Main::doSelectAll);
	
	get("/register", Main::doRegister);
	
	get("/login", Main::doLogin);
	
	post("/register", Main::doRegister);
	
	post("/login", Main::doLogin);
	
	post("/update_user", Main::doUpdateUser);
	
	post("/search_contacts", Main::doSelect);

	// In this case we use a Java 8 method reference to specify
	// the method to be called when a GET /:table/:film HTTP request
	// Main::doWork will return the result of the SQL select
	// query. It could've been programmed using a lambda
	// expression instead, as illustrated in the next sentence.
	get("/:table/:film", Main::doSelect);

	// In this case we use a Java 8 Lambda function to process the
	// GET /upload_films HTTP request, and we return a form
	get("/upload_films", (req, res) -> 
	    "<form action='/upload' method='post' enctype='multipart/form-data'>" 
	    + "    <input type='file' name='uploaded_films_file' accept='.txt'>"
	    + "    <button>Upload file</button>" + "</form>"
	    );
	// You must use the name "uploaded_films_file" in the call to
	// getPart to retrieve the uploaded file. See next call:


	// Retrieves the file uploaded through the /upload_films HTML form
	// Creates table and stores uploaded file in a two-columns table
	post("/upload", (req, res) -> {

		req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));

		try (InputStream input = req.raw().getPart("uploaded_films_file").getInputStream()){ 
			// getPart needs to use the same name "uploaded_films_file" used in the form

			// Prepare SQL to create table
			Statement statement = connection.createStatement();

			// This code only works for PostgreSQL
			statement.executeUpdate("drop table if exists films");
			statement.executeUpdate("create table films (film text, actor text)");
			    
			// Read contents of input stream that holds the uploaded file
			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(isr);
			String s;
			while ((s = br.readLine()) != null) {
			    System.out.println(s);
				
			    // Tokenize the film name and then the actors, separated by "/"
			    StringTokenizer tokenizer = new StringTokenizer(s, "/");
				
			    // First token is the film name(year)
			    String film = tokenizer.nextToken();
				
			    // Now get actors and insert them
			    while (tokenizer.hasMoreTokens()) {
				insert(connection, film, tokenizer.nextToken());
			    }
			    // Commit only once, after all the inserts are done
			    // If done after each statement performance degrades
			    connection.commit();

			    
			}
		    }
		System.out.println("File Uploaded!");
		return "File uploaded!";
	    });

    }

    static int getHerokuAssignedPort() {
	ProcessBuilder processBuilder = new ProcessBuilder();
	if (processBuilder.environment().get("PORT") != null) {
	    return Integer.parseInt(processBuilder.environment().get("PORT"));
	}
	return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
    }
}

