//package cl.subtel.interop.cntv.util;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//
//import com.mongodb.BasicDBObject;
//
//import cl.subtel.interop.cntv.calculotvd.ArregloAntena;
//import cl.subtel.interop.cntv.calculotvd.DatosElemento;
//import cl.subtel.interop.cntv.calculotvd.Elemento;
//
//public class OracleDBUtils {
//
//	public static Connection connect() {
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		Connection connection = null;
//
//		try {
//			String db_url_develop = "jdbc:oracle:thin:bdc_subtel/bdc@172.30.10.219:1521:dreclamo";
//			String db_url_production = "jdbc:oracle:thin:bdc_subtel/bdc@172.30.10.50:1521:reclamos";
//			connection = DriverManager.getConnection(db_url_develop);
//		} catch (SQLException e) {
//			System.out.println("Connection Failed! Check output console");
//			e.printStackTrace();
//		}
//
//		return connection;
//	}
//
//	public static void closeAll(Connection connection, Statement stmt, ResultSet res) {
//
//		if (res != null) {
//			try {
//				res.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//
//		if (stmt != null) {
//			try {
//				stmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//
//		if (connection != null) {
//			try {
//				connection.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void closeConnection(Connection connection) {
//		if (connection != null) {
//			try {
//				connection.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void closeStatement(Statement stmt) {
//		if (stmt != null) {
//			try {
//				stmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void closeResultSet(ResultSet res) {
//		if (res != null) {
//			try {
//				res.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
