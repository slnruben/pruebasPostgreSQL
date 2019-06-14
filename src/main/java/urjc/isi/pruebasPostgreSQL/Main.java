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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
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
    
    public static String doSelectAll(Request request, Response response) throws JSONException, SQLException {
    	String success = "0";
		JSONArray jsonArr = new JSONArray();
		JSONObject json;
		
		//HashMap<String, String> params = getRequestData(request);
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
    
    public static JSONArray doSelect(Request request, Response response) throws JSONException, SQLException, UnsupportedEncodingException {
    	String sql;
    	String usuario;
    	String sueldo;
    	String trabajo;
    	String sector1;
    	String sector2;
    	String conocimientos;
		JSONArray jsonArr = new JSONArray();
		JSONObject json;
		Boolean prev = false;
		
		HashMap<String, String> params = getRequestData(request);
		usuario = URLDecoder.decode(params.get("Usuario"), "UTF-8" );
		sueldo = URLDecoder.decode(params.get("Sueldo"), "UTF-8" );
		trabajo = URLDecoder.decode(params.get("Trabajo"), "UTF-8" );
		sector1 = URLDecoder.decode(params.get("Sector1"), "UTF-8" );
		sector2 = URLDecoder.decode(params.get("Sector2"), "UTF-8" );
		conocimientos = params.get("Conocimientos");
		if ("".equals(sueldo) && "".equals(trabajo) && "".equals(sector1) && "".equals(sector2)
				&& "".equals(conocimientos)) {
			sql = ("SELECT * FROM users WHERE Usuario NOT LIKE ?");
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setString(1, URLDecoder.decode(params.get("Usuario"), "UTF-8" ));
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					json = new JSONObject();
					json.put("Id", rs.getString("id"));
					json.put("Usuario", rs.getString("Usuario"));
					json.put("Nombre",rs.getString("Nombre"));
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
			sql = ("SELECT * FROM users WHERE");
			if (!"".equals(sueldo)) {
				sql = sql + " Sueldo LIKE '" + sueldo + "'";
				prev = true;
			}
			if (!"".equals(trabajo)) {
				if (prev) {
					sql = sql + " OR ";
				} else {
					prev = true;
				}
				sql = sql + " Trabajo ~* '" + trabajo + "'";
			}
			if (!"".equals(sector1)) {
				if (prev) {
					sql = sql + " OR ";
				} else {
					prev = true;
				}
				sql = sql + " Sector1 ~* '" + sector1 + "'";
			}
			if (!"".equals(sector2)) {
				if (prev) {
					sql = sql + " OR ";
				} else {
					prev = true;
				}
				sql = sql + " Sector2 ~* '" + sector2 + "'";
			}
			if (!"".equals(conocimientos)) {
				if (prev) {
					sql = sql + " OR ";
				} else {
					prev = true;
				}
				sql = sql + " Conocimientos ~* '" + conocimientos + "'";
			}
			sql = sql + " AND Usuario NOT LIKE '" + usuario + "'";
			try (PreparedStatement pstmt2 = connection.prepareStatement(sql)) {	
				ResultSet rs2 = pstmt2.executeQuery();
				while (rs2.next()) {
					System.out.println("USUARIO: " + rs2.getString("Usuario"));
					json = new JSONObject();
					json.put("Id", rs2.getString("id"));
					json.put("Usuario", rs2.getString("Usuario"));
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
		return jsonArr;
    }
    
    public static JSONArray doSelectAllNegotiations(Request request, Response response) throws JSONException, SQLException {
		JSONArray jsonArr = new JSONArray();
		JSONObject json;
		
		//HashMap<String, String> params = getRequestData(request);
		String sql = ("SELECT * FROM negotiations");
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				json = new JSONObject();
				json.put("Id", rs.getString("id"));
				json.put("Usuario_Creador", rs.getString("Usuario_Creador"));
				json.put("Usuario_Receptor",rs.getString("Usuario_Receptor"));
				json.put("Estado", rs.getString("Estado"));
				json.put("Ofrecido_Nombre", rs.getString("Ofrecido_Nombre"));
				json.put("Ofrecido_Apellidos", rs.getString("Ofrecido_Apellidos"));
				json.put("Ofrecido_Email", rs.getString("Ofrecido_Email"));
				json.put("Ofrecido_Telefono", rs.getString("Ofrecido_Telefono"));
				json.put("Ofrecido_Trabajo", rs.getString("Ofrecido_Trabajo"));
				json.put("Ofrecido_Empresa", rs.getString("Ofrecido_Empresa"));
				json.put("Ofrecido_Sueldo", rs.getString("Ofrecido_Sueldo"));
				json.put("Ofrecido_Universidad", rs.getString("Ofrecido_Universidad"));
				json.put("Ofrecido_Carrera", rs.getString("Ofrecido_Carrera"));
				json.put("Ofrecido_Sector1", rs.getString("Ofrecido_Sector1"));
				json.put("Ofrecido_Sector2", rs.getString("Ofrecido_Sector2"));
				json.put("Ofrecido_Experiencia", rs.getString("Ofrecido_Experiencia"));
				json.put("Ofrecido_Lenguajes", rs.getString("Ofrecido_Lenguajes"));
				json.put("Ofrecido_Conocimientos", rs.getString("Ofrecido_Conocimientos"));
				
				json.put("Requerido_Nombre", rs.getString("Requerido_Nombre"));
				json.put("Requerido_Apellidos", rs.getString("Requerido_Apellidos"));
				json.put("Requerido_Email", rs.getString("Requerido_Email"));
				json.put("Requerido_Telefono", rs.getString("Requerido_Telefono"));
				json.put("Requerido_Trabajo", rs.getString("Requerido_Trabajo"));
				json.put("Requerido_Empresa", rs.getString("Requerido_Empresa"));
				json.put("Requerido_Sueldo", rs.getString("Requerido_Sueldo"));
				json.put("Requerido_Universidad", rs.getString("Requerido_Universidad"));
				json.put("Requerido_Carrera", rs.getString("Requerido_Carrera"));
				json.put("Requerido_Sector1", rs.getString("Requerido_Sector1"));
				json.put("Requerido_Sector2", rs.getString("Requerido_Sector2"));
				json.put("Requerido_Experiencia", rs.getString("Requerido_Experiencia"));
				json.put("Requerido_Lenguajes", rs.getString("Requerido_Lenguajes"));
				json.put("Requerido_Conocimientos", rs.getString("Requerido_Conocimientos"));
				jsonArr.put(json);
				
			}
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		System.out.println(jsonArr.toString());
		return jsonArr;
    }
    
    public static JSONArray doSelectMyNegotiations(Request request, Response response) throws JSONException, SQLException, UnsupportedEncodingException {
		JSONArray jsonArr = new JSONArray();
		JSONObject json;
		String user;
		
		HashMap<String, String> params = getRequestData(request);
		user = URLDecoder.decode(params.get("Usuario"), "UTF-8" );
		String sql = ("SELECT * FROM negotiations WHERE (Usuario_Creador=? AND Estado NOT LIKE ?) OR Usuario_Receptor=?");
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, user);
			pstmt.setString(2, URLDecoder.decode(params.get("Estado"), "UTF-8" ));
			pstmt.setString(3, user);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				json = new JSONObject();
				json.put("Id", rs.getString("id"));
				json.put("Usuario_Creador", rs.getString("Usuario_Creador"));
				json.put("Usuario_Receptor",rs.getString("Usuario_Receptor"));
				json.put("Estado", rs.getString("Estado"));
				json.put("Ofrecido_Nombre", rs.getString("Ofrecido_Nombre"));
				json.put("Ofrecido_Apellidos", rs.getString("Ofrecido_Apellidos"));
				json.put("Ofrecido_Email", rs.getString("Ofrecido_Email"));
				json.put("Ofrecido_Telefono", rs.getString("Ofrecido_Telefono"));
				json.put("Ofrecido_Trabajo", rs.getString("Ofrecido_Trabajo"));
				json.put("Ofrecido_Empresa", rs.getString("Ofrecido_Empresa"));
				json.put("Ofrecido_Sueldo", rs.getString("Ofrecido_Sueldo"));
				json.put("Ofrecido_Universidad", rs.getString("Ofrecido_Universidad"));
				json.put("Ofrecido_Carrera", rs.getString("Ofrecido_Carrera"));
				json.put("Ofrecido_Sector1", rs.getString("Ofrecido_Sector1"));
				json.put("Ofrecido_Sector2", rs.getString("Ofrecido_Sector2"));
				json.put("Ofrecido_Experiencia", rs.getString("Ofrecido_Experiencia"));
				json.put("Ofrecido_Lenguajes", rs.getString("Ofrecido_Lenguajes"));
				json.put("Ofrecido_Conocimientos", rs.getString("Ofrecido_Conocimientos"));
				
				json.put("Requerido_Nombre", rs.getString("Requerido_Nombre"));
				json.put("Requerido_Apellidos", rs.getString("Requerido_Apellidos"));
				json.put("Requerido_Email", rs.getString("Requerido_Email"));
				json.put("Requerido_Telefono", rs.getString("Requerido_Telefono"));
				json.put("Requerido_Trabajo", rs.getString("Requerido_Trabajo"));
				json.put("Requerido_Empresa", rs.getString("Requerido_Empresa"));
				json.put("Requerido_Sueldo", rs.getString("Requerido_Sueldo"));
				json.put("Requerido_Universidad", rs.getString("Requerido_Universidad"));
				json.put("Requerido_Carrera", rs.getString("Requerido_Carrera"));
				json.put("Requerido_Sector1", rs.getString("Requerido_Sector1"));
				json.put("Requerido_Sector2", rs.getString("Requerido_Sector2"));
				json.put("Requerido_Experiencia", rs.getString("Requerido_Experiencia"));
				json.put("Requerido_Lenguajes", rs.getString("Requerido_Lenguajes"));
				json.put("Requerido_Conocimientos", rs.getString("Requerido_Conocimientos"));
				jsonArr.put(json);
				
			}
		} catch (SQLException e) {
			System.out.println("ERROR: " + e);
		}
		System.out.println(jsonArr.toString());
		return jsonArr;
    }
    
    public static String doRefuseNegotiation(Request request, Response response) {
		String success = "-1";
		
		HashMap<String, String> params = getRequestData(request);	
		String sql = ("UPDATE negotiations SET Estado=? WHERE id=?");
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, params.get("Estado"));
			pstmt.setInt(2, Integer.parseInt(params.get("Id")));
			success = Integer.toString(pstmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return success;
    }
    
    public static String doDeleteNegotiation(Request request, Response response) {
		String success = "-1";
		
		HashMap<String, String> params = getRequestData(request);
		String sql = ("DELETE FROM negotiations WHERE id=?");
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, Integer.parseInt(params.get("Id")));
			success = Integer.toString(pstmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return success;
    }
    
    public static String doCreateNegotiation(Request request, Response response) throws SQLException, UnsupportedEncodingException {
		String sql;
		String success = "-1";
		
		HashMap<String, String> params = getRequestData(request);	
		sql = "INSERT INTO negotiations(Usuario_Creador, Usuario_Receptor, Estado, "
				+ "Ofrecido_Nombre, Ofrecido_Apellidos, Ofrecido_Email, Ofrecido_Telefono, "
				+ "Ofrecido_Trabajo, Ofrecido_Empresa, Ofrecido_Sueldo, Ofrecido_Universidad, "
				+ "Ofrecido_Carrera, Ofrecido_Sector1, Ofrecido_Sector2, Ofrecido_Experiencia, "
				+ "Ofrecido_Lenguajes, Ofrecido_Conocimientos, Requerido_Nombre, "
				+ "Requerido_Apellidos, Requerido_Email, Requerido_Telefono, Requerido_Trabajo, "
				+ "Requerido_Empresa, Requerido_Sueldo, Requerido_Universidad, "
				+ "Requerido_Carrera, Requerido_Sector1, Requerido_Sector2, "
				+ "Requerido_Experiencia, Requerido_Lenguajes, Requerido_Conocimientos) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, URLDecoder.decode(params.get("Usuario_Creador"), "UTF-8" ));
			pstmt.setString(2, URLDecoder.decode(params.get("Usuario_Receptor"), "UTF-8" ));
			pstmt.setString(3, params.get("Estado"));
			pstmt.setString(4, URLDecoder.decode(params.get("Ofrecido_Nombre"), "UTF-8" ));
			pstmt.setString(5, URLDecoder.decode(params.get("Ofrecido_Apellidos"), "UTF-8" ));
			pstmt.setString(6, URLDecoder.decode(params.get("Ofrecido_Email"), "UTF-8" ));	
			pstmt.setString(7, URLDecoder.decode(params.get("Ofrecido_Telefono"), "UTF-8" ));
			pstmt.setString(8, URLDecoder.decode(params.get("Ofrecido_Trabajo"), "UTF-8" ));
			pstmt.setString(9, URLDecoder.decode(params.get("Ofrecido_Empresa"), "UTF-8" ));
			pstmt.setString(10, URLDecoder.decode(params.get("Ofrecido_Sueldo"), "UTF-8" ));
			pstmt.setString(11, URLDecoder.decode(params.get("Ofrecido_Universidad"), "UTF-8" ));
			pstmt.setString(12, URLDecoder.decode(params.get("Ofrecido_Carrera"), "UTF-8" ));
			pstmt.setString(13, URLDecoder.decode(params.get("Ofrecido_Sector1"), "UTF-8" ));
			pstmt.setString(14, URLDecoder.decode(params.get("Ofrecido_Sector2"), "UTF-8" ));
			pstmt.setString(15, URLDecoder.decode(params.get("Ofrecido_Experiencia"), "UTF-8" ));
			pstmt.setString(16, URLDecoder.decode(params.get("Ofrecido_Lenguajes"), "UTF-8" ));
			pstmt.setString(17, URLDecoder.decode(params.get("Ofrecido_Conocimientos"), "UTF-8" ));
			pstmt.setString(18, URLDecoder.decode(params.get("Requerido_Nombre"), "UTF-8" ));
			pstmt.setString(19, URLDecoder.decode(params.get("Requerido_Apellidos"), "UTF-8" ));
			pstmt.setString(20, URLDecoder.decode(params.get("Requerido_Email"), "UTF-8" ));
			pstmt.setString(21, URLDecoder.decode(params.get("Requerido_Telefono"), "UTF-8" ));
			pstmt.setString(22, URLDecoder.decode(params.get("Requerido_Trabajo"), "UTF-8" ));
			pstmt.setString(23, URLDecoder.decode(params.get("Requerido_Empresa"), "UTF-8" ));
			pstmt.setString(24, URLDecoder.decode(params.get("Requerido_Sueldo"), "UTF-8" ));
			pstmt.setString(25, URLDecoder.decode(params.get("Requerido_Universidad"), "UTF-8" ));
			pstmt.setString(26, URLDecoder.decode(params.get("Requerido_Carrera"), "UTF-8" ));
			pstmt.setString(27, URLDecoder.decode(params.get("Requerido_Sector1"), "UTF-8" ));
			pstmt.setString(28, URLDecoder.decode(params.get("Requerido_Sector2"), "UTF-8" ));
			pstmt.setString(29, URLDecoder.decode(params.get("Requerido_Experiencia"), "UTF-8" ));
			pstmt.setString(30, URLDecoder.decode(params.get("Requerido_Lenguajes"), "UTF-8" ));
			pstmt.setString(31, URLDecoder.decode(params.get("Requerido_Conocimientos"), "UTF-8" ));
			success = Integer.toString(pstmt.executeUpdate());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error doCreateNegotiation: " + e);
		}
		return success;
	}
    
    public static String doUpdateUser(Request request, Response response) throws SQLException, UnsupportedEncodingException {
		String success = "0";
		
		HashMap<String, String> params = getRequestData(request);
		String sql = "UPDATE users SET Nombre=?, Apellidos=?, Email=?, Telefono=?, Trabajo=?, "
						+ "Empresa=?, Sueldo=?, Universidad=?, Carrera=?, Sector1=?, Sector2=?, "
						+ "Experiencia=?, Lenguajes=?, Conocimientos=? WHERE Usuario=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, URLDecoder.decode(params.get("Nombre"), "UTF-8" ));
			pstmt.setString(2, URLDecoder.decode(params.get("Apellidos"), "UTF-8" ));
			pstmt.setString(3, URLDecoder.decode(params.get("Email"), "UTF-8" ));	
			pstmt.setString(4, URLDecoder.decode(params.get("Telefono"), "UTF-8" ));
			pstmt.setString(5, URLDecoder.decode(params.get("Trabajo"), "UTF-8" ));
			pstmt.setString(6, URLDecoder.decode(params.get("Empresa"), "UTF-8" ));	
			pstmt.setString(7, URLDecoder.decode(params.get("Sueldo"), "UTF-8" ));
			pstmt.setString(8, URLDecoder.decode(params.get("Universidad"), "UTF-8" ));
			pstmt.setString(9, URLDecoder.decode(params.get("Carrera"), "UTF-8" ));
			pstmt.setString(10, URLDecoder.decode(params.get("Sector1"), "UTF-8" ));
			pstmt.setString(11, URLDecoder.decode(params.get("Sector2"), "UTF-8" ));
			pstmt.setString(12, URLDecoder.decode(params.get("Experiencia"), "UTF-8" ));
			pstmt.setString(13, URLDecoder.decode(params.get("Lenguajes"), "UTF-8" ));
			pstmt.setString(14, URLDecoder.decode(params.get("Conocimientos"), "UTF-8" ));
			pstmt.setString(15, URLDecoder.decode(params.get("Usuario"), "UTF-8" ));
			success = Integer.toString(pstmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return success;
	}
	
    public static String doLogin(Request request, Response response) throws JSONException, SQLException {
    	String success = "0";
		
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
    
	public static String doRegister(Request request, Response response) throws SQLException, UnsupportedEncodingException {
		String last_inserted_id = "-1";
		
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
						pstmt2.setString(1, URLDecoder.decode(params.get("Usuario"), "UTF-8" ));
						pstmt2.setString(2, URLDecoder.decode(params.get("Sueldo"), "UTF-8" ));
						pstmt2.executeUpdate();			
					} catch (SQLException e) {
						System.out.println("ERROR1: " + e.getMessage());
					}
					sql = "SELECT id FROM users WHERE Usuario=?";
					try (PreparedStatement pstmt3 = connection.prepareStatement(sql)) {
						pstmt3.setString(1, URLDecoder.decode(params.get("Usuario"), "UTF-8" ));
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
			statement.executeUpdate("drop table if exists negotiations");
			statement.executeUpdate("create table negotiations (id SERIAL PRIMARY KEY, "
					+ "Usuario_Creador text, Usuario_Receptor text, "
					+ "Estado text, Ofrecido_Nombre text, "
					+ "Ofrecido_Apellidos text, Ofrecido_Email text, "
					+ "Ofrecido_Telefono text, Ofrecido_Trabajo text, "
					+ "Ofrecido_Empresa text, Ofrecido_Sueldo text, "
					+ "Ofrecido_Universidad text, Ofrecido_Carrera text, "
					+ "Ofrecido_Sector1 text, Ofrecido_Sector2 text, "
					+ "Ofrecido_Experiencia text, Ofrecido_Lenguajes text, "
					+ "Ofrecido_Conocimientos text, Requerido_Nombre text, "
					+ "Requerido_Apellidos text, Requerido_Email text, "
					+ "Requerido_Telefono text, Requerido_Trabajo text, "
					+ "Requerido_Empresa text, Requerido_Sueldo text, "
					+ "Requerido_Universidad text, Requerido_Carrera text, "
					+ "Requerido_Sector1 text, Requerido_Sector2 text, "
					+ "Requerido_Experiencia text, Requerido_Lenguajes text, "
					+ "Requerido_Conocimientos text)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}
		return "1";
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
		connection.setAutoCommit(true);
	
		get("/create_bbdd", Main::doCreateBBDD);
		
		get("/get_contacts",  Main::doSelectAll);
		
		get("/search_negotiations", Main::doSelectAllNegotiations);
		
		get("/register", Main::doRegister);
		
		get("/login", Main::doLogin);
		
		post("/register", Main::doRegister);
		
		post("/login", Main::doLogin);
		
		post("/update_user", Main::doUpdateUser);
		
		post("/search_contacts", Main::doSelect);
		
		post("/search_negotiations", Main::doSelectMyNegotiations);
		
		post("/create_negotiation", Main::doCreateNegotiation);	
		
		post("/refuse_negotiation", Main::doRefuseNegotiation);
		
		post("/delete_negotiation", Main::doDeleteNegotiation);

    }

    static int getHerokuAssignedPort() {
	ProcessBuilder processBuilder = new ProcessBuilder();
	if (processBuilder.environment().get("PORT") != null) {
	    return Integer.parseInt(processBuilder.environment().get("PORT"));
	}
	return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
    }
}

