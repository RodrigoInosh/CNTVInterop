package cl.subtel.interop.cntv.calculotvd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;

import cl.subtel.interop.cntv.util.DBOracleUtils;

public class Clientes {

	private Long rut_cliente;
	private static final int ACTIVIDAD_ECONOMICA = 3;
	private static final String TIPO_PERSONALIDAD = "J"; // Jurídica
	private Long cod_comuna;
	private String digito_verificador;
	private String razon_social;
	private String alias;
	private String direccion;

	public Clientes(Long rut_cliente, Long cod_comuna, String digito_verificador, String razon_social, String alias,
			String direccion) {
		super();
		this.rut_cliente = rut_cliente;
		this.cod_comuna = cod_comuna;
		this.digito_verificador = digito_verificador;
		this.razon_social = razon_social;
		this.alias = alias;
		this.direccion = direccion;
	}

	public Clientes() {
	}

	public Long getRut_cliente() {
		return rut_cliente;
	}

	public void setRut_cliente(Long rut_cliente) {
		this.rut_cliente = rut_cliente;
	}

	public Long getCod_comuna() {
		return cod_comuna;
	}

	public void setCod_comuna(Long cod_comuna) {
		this.cod_comuna = cod_comuna;
	}

	public String getDigito_verificador() {
		return digito_verificador;
	}

	public void setDigito_verificador(String digito_verificador) {
		this.digito_verificador = digito_verificador;
	}

	public String getRazon_social() {
		return razon_social;
	}

	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public static int getActividadEconomica() {
		return ACTIVIDAD_ECONOMICA;
	}

	public static String getTipoPersonalidad() {
		return TIPO_PERSONALIDAD;
	}

	public static void insertDataCliente(Clientes data_cliente, DatosEmpresa datos_empresa, Logger log) {

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt_insert_cliente = null;
		PreparedStatement stmt_insert_data_empresa = null;

		try {

			stmt_insert_cliente = db_connection.prepareStatement(
					"INSERT INTO BDC_SUBTEL.CLIENTES (RUT_CLIENTE, ACT_COD_ACTIVIDAD_SII, TP_COD_TIPO_PERSONALIDAD, COD_COMUNA, DV, NOMBRE_RZ, ALIAS, DIRECCION, FECHA_INGRESO) "
							+ "VALUES (?,?,?,?,?,?,?,?, SYSDATE)");

			stmt_insert_cliente.setLong(1, data_cliente.getRut_cliente());
			stmt_insert_cliente.setInt(2, Clientes.getActividadEconomica());
			stmt_insert_cliente.setString(3, Clientes.getTipoPersonalidad());
			stmt_insert_cliente.setLong(4, data_cliente.getCod_comuna());
			stmt_insert_cliente.setString(5, data_cliente.getDigito_verificador());
			stmt_insert_cliente.setString(6, data_cliente.getRazon_social());
			stmt_insert_cliente.setString(7, data_cliente.getAlias());
			stmt_insert_cliente.setString(8, data_cliente.getDireccion());

			stmt_insert_cliente.executeUpdate();

			stmt_insert_data_empresa = db_connection.prepareStatement(
					"INSERT INTO BDC_SUBTEL.DATOS_EMPRESAS (CLI_RUT_CLIENTE, FONO1, FONO2, ALIAS, EMAIL, FECHA_INGRESO, NOMBRE_CONTACTO, RUT_CONTACTO) "
							+ "VALUES (?,?,?,?,?,SYSDATE,?,?)");

			stmt_insert_data_empresa.setLong(1, data_cliente.getRut_cliente());
			stmt_insert_data_empresa.setString(2, datos_empresa.getFono());
			stmt_insert_data_empresa.setString(3, datos_empresa.getFono2());
			stmt_insert_data_empresa.setString(4, datos_empresa.getAlias_cliente());
			stmt_insert_data_empresa.setString(5, datos_empresa.getEmail());
			stmt_insert_data_empresa.setString(6, datos_empresa.getNombre_contacto());
			stmt_insert_data_empresa.setString(7, datos_empresa.getRut_contacto());

			stmt_insert_data_empresa.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeStatement(stmt_insert_cliente);
			DBOracleUtils.closeStatement(stmt_insert_data_empresa);
			DBOracleUtils.closeConnection(db_connection);
		}

		log.debug("---DATOS CLIENTE Y EMPRESA INGRESADOS CORRECTAMENTE---");
	}

	public static void updateDataCliente(Clientes data_cliente, DatosEmpresa datos_empresa, Logger log) {

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt_insert_cliente = null;
		PreparedStatement stmt_insert_data_empresa = null;

		try {

			stmt_insert_cliente = db_connection.prepareStatement(
					"UPDATE BDC_SUBTEL.CLIENTES SET ACT_COD_ACTIVIDAD_SII = ?, TP_COD_TIPO_PERSONALIDAD = ?, COD_COMUNA =?, DV = ?, NOMBRE_RZ = ?, ALIAS = ?, DIRECCION = ?, FECHA_MODIFICA = SYSDATE "
							+ "WHERE RUT_CLIENTE = ?");

			stmt_insert_cliente.setInt(1, Clientes.getActividadEconomica());
			stmt_insert_cliente.setString(2, Clientes.getTipoPersonalidad());
			stmt_insert_cliente.setLong(3, data_cliente.getCod_comuna());
			stmt_insert_cliente.setString(4, data_cliente.getDigito_verificador());
			stmt_insert_cliente.setString(5, data_cliente.getRazon_social());
			stmt_insert_cliente.setString(6, data_cliente.getAlias());
			stmt_insert_cliente.setString(7, data_cliente.getDireccion());
			stmt_insert_cliente.setLong(8, data_cliente.getRut_cliente());

			stmt_insert_cliente.executeUpdate();

			stmt_insert_data_empresa = db_connection.prepareStatement(
					"UPDATE BDC_SUBTEL.DATOS_EMPRESAS SET FONO1 = ?, FONO2 = ?, ALIAS = ?, EMAIL = ?, FECHA_MODIFICA = SYSDATE, NOMBRE_CONTACTO = ?, RUT_CONTACTO = ? WHERE CLI_RUT_CLIENTE = ?");

			stmt_insert_data_empresa.setString(1, datos_empresa.getFono());
			stmt_insert_data_empresa.setString(2, datos_empresa.getFono2());
			stmt_insert_data_empresa.setString(3, datos_empresa.getAlias_cliente());
			stmt_insert_data_empresa.setString(4, datos_empresa.getEmail());
			stmt_insert_data_empresa.setString(5, datos_empresa.getNombre_contacto());
			stmt_insert_data_empresa.setString(6, datos_empresa.getRut_contacto());
			stmt_insert_data_empresa.setLong(7, data_cliente.getRut_cliente());

			stmt_insert_data_empresa.executeUpdate();

			log.debug("---DATOS CLIENTE Y EMPRESA ACTUALIZADOS CORRECTAMENTE---");
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeStatement(stmt_insert_cliente);
			DBOracleUtils.closeStatement(stmt_insert_data_empresa);
			DBOracleUtils.closeConnection(db_connection);
		}
		
	}
}
