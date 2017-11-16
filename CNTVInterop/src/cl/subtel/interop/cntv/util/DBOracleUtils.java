package cl.subtel.interop.cntv.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBOracleUtils {

	private static final Logger log = LogManager.getLogger();
	private static Connection db_connection;
	private static String db_url_develop = "jdbc:oracle:thin:bdc_subtel/bdc@172.30.10.219:1521:dreclamo";
	private static String db_url_production = "jdbc:oracle:thin:bdc_subtel/bdc@172.30.10.50:1521:reclamos";

	private DBOracleUtils() {
	}

	public static Connection getSingletonInstance() {
		if (db_connection == null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");

				db_connection = DriverManager.getConnection(db_url_production);
				db_connection.setAutoCommit(false);
			} catch (SQLException e) {
				log.error("Connection Failed! Check output console");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return db_connection;
	}

	public static void commit() {
		try {
			log.info("Commiting changes");
			db_connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void rollback() {
		try {
			log.info("Rolling back");
			db_connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeAll(Connection connection, Statement stmt, ResultSet res) {

		if (res != null) {
			try {
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(ResultSet res, Statement stmt) {
		if (res != null) {
			try {
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeConnection(Connection connection) {
		log.info("Closing Oracle connection....");
		if (db_connection != null) {
			try {
				db_connection.close();
				db_connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeResultSet(ResultSet res) {
		if (res != null) {
			try {
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}