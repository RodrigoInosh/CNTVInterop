package cl.subtel.interop.cntv.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cl.subtel.interop.cntv.calculotvd.ArregloAntena;
import cl.subtel.interop.cntv.calculotvd.Clientes;
import cl.subtel.interop.cntv.calculotvd.DatosElemento;
import cl.subtel.interop.cntv.calculotvd.DatosEmpresa;
import cl.subtel.interop.cntv.calculotvd.Elemento;

public class DBOracleDAO {

	private static final Logger log = LogManager.getLogger();
	private static final int ID_TRDL_PERDIDAS_LOBULO = 4;
	private static final int ID_TRDL_ZONA_SERVICIO = 3;

	public static Elemento createElementoSistPrincipal(JSONObject datos_sist_principal, String nombre_archivo,
			String tipo_elemento) {

		Elemento elemento_sist_principal = new Elemento();
		String elm_nombre = nombre_archivo;
		String rut_cliente = "";
		int tel_codigo = getTelCodByTipoElemento(tipo_elemento);

		try {
			String datos = datos_sist_principal.getString("datos").replace("[", "").replace("]", "");
			JSONObject datos_json = new JSONObject(datos);

			rut_cliente = datos_json.getJSONObject("empresa").getString("rut");

			elemento_sist_principal.setElm_nombre(elm_nombre);
			elemento_sist_principal.setRut_cliente(rut_cliente);
			elemento_sist_principal.setTel_codigo(tel_codigo);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return elemento_sist_principal;
	}

	public static Elemento[] createElementosEstudios(JSONObject datos_sist_principal, String nombre_archivo) {
		Elemento estudios[] = new Elemento[2];

		try {
			String datos = datos_sist_principal.getString("calculos").replace("[", "").replace("]", "");
			JSONObject datos_json = new JSONObject(datos).getJSONObject("form_data");

			if (!"".equals(datos_json.getJSONObject("estudio_principal").getString("comuna"))) {
				estudios[0] = createElementoSistPrincipal(datos_sist_principal, nombre_archivo, "Estudio Principal");
			}

			if (!"".equals(datos_json.getJSONObject("estudio_alternativo").getString("comuna"))) {
				estudios[1] = createElementoSistPrincipal(datos_sist_principal, nombre_archivo, "Estudio Alternativo");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return estudios;
	}

	public static Object getColumnCode(String cod_column_name, String table_name, String where_column_name,
			String where_column_value) {

		Connection connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt = null;
		ResultSet res = null;
		Object cod_value = "";
		try {
			stmt = connection.prepareStatement(
					"SELECT " + cod_column_name + " FROM " + table_name + " WHERE " + where_column_name + " = ?");
			stmt.setString(1, where_column_value);

			res = stmt.executeQuery();
			if (res.next()) {
				cod_value = res.getObject(cod_column_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBOracleUtils.closeResultSet(res);
		DBOracleUtils.closeStatement(stmt);

		return cod_value;
	}

	public static int getTelCodByTipoElemento(String tipo_elemento) {
		int tel_cod = 0;

		Statement stmt_query_get_tel_cod = null;
		ResultSet result_tel_cod_query = null;
		try {
			Connection connection_oracledb = DBOracleUtils.getSingletonInstance();
			stmt_query_get_tel_cod = connection_oracledb.createStatement();
			result_tel_cod_query = stmt_query_get_tel_cod
					.executeQuery("select TEL_CODIGO from TIPO_ELEMENTO where TEL_NOMBRE = \'" + tipo_elemento + "\'");

			if (result_tel_cod_query.next()) {
				tel_cod = result_tel_cod_query.getInt("TEL_CODIGO");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBOracleUtils.closeResultSet(result_tel_cod_query);
		DBOracleUtils.closeStatement(stmt_query_get_tel_cod);

		return tel_cod;
	}

	public static Long createSolitudConcesiones(Long num_ofi_parte, JSONObject empresa_data) {
		log.info("----CREANDO SOLICITUD----");

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt = null;

		final String tipo_solicitud = "TRA";
		final String tipo_servicio = "STD";
		final Long tipo_tramite = 1L;

		Long numero_solicitud = 0L;

		try {
			String rut_empresa[] = empresa_data.get("rut").toString().split("-");
			int rut_solicitate = Integer.parseInt(rut_empresa[0]);
			numero_solicitud = getSequenceValue(db_connection, "SEQ_SOL.NEXTVAL");
			String create_solicitud_query = "INSERT INTO SOLICITUDES_CONCESIONES (tsol_cod_tipo_sol, tserv_cod_tipo_servicio, soli_numero_solicitud, soli_fecha_ingreso, soli_numero_op, "
					+ "soli_fecha_op, soli_rut, soli_usuario_ingreso, cod_tipo_tramite, id_ejecutivo, SOLI_FLAG, TOR_CODIGO, SOLI_CORRELATIVO) VALUES (?, ?, ?, SYSDATE, ?, SYSDATE, ?, 'ADM', ?, 'ADM', 0, 10, 0)";

			stmt = db_connection.prepareStatement(create_solicitud_query);
			stmt.setString(1, tipo_solicitud);
			stmt.setString(2, tipo_servicio);
			stmt.setLong(3, numero_solicitud);
			stmt.setLong(4, num_ofi_parte);
			stmt.setInt(5, rut_solicitate);
			stmt.setLong(6, tipo_tramite);

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		log.debug("Num Soli: " + numero_solicitud);
		DBOracleUtils.closeStatement(stmt);

		return numero_solicitud;
	}

	private static Long getSequenceValue(Connection db_connection, String sequence) throws SQLException {
		Long sequencue_value = 0L;

		PreparedStatement stmt = null;
		ResultSet res = null;

		String query = "SELECT " + sequence + " FROM DUAL";
		stmt = db_connection.prepareStatement(query);
		res = stmt.executeQuery();

		if (res.next()) {
			sequencue_value = res.getLong("NEXTVAL");
		}

		DBOracleUtils.closeResultSet(res);
		DBOracleUtils.closeStatement(stmt);

		return sequencue_value;
	}

	public static Long getNumeroOP(JSONObject empresa_data) {
		Long numero_op = 0L;

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement insert_numero_op_stmt = null;
		PreparedStatement insert_op_eventos = null;

		try {
			numero_op = getSequenceValue(db_connection, "SEQ_OP.NEXTVAL");
			String observaciones = "";
			int rut_empresa = Integer.parseInt(empresa_data.get("rut").toString().split("-")[0]);
			String nombre_empresa_remitente = empresa_data.getString("razonSocial").toString();
			int op_fecha_ing = TvdUtils.getFormattedOPDate();
			int op_fecha_recep = TvdUtils.getFormattedOPDate();
			String query_numero_op = "insert into GABINETE.OFICINA_PARTES (op_num_ing, op_fecha_ing, trm_cod, td_codigo, mat_cod, d_codigo, op_obs, op_rut_sol, op_tipo_pers, op_fecha_recep, ts_codigo, remitente, "
					+ "usuario_creacion, fecha_creacion, process_link_pdf, id_tipo_ing, id_tipo_tramite) VALUES (?, ?, 'PTECNTV', 'CTTVD', 'TVD', 'CON', ?, ?, 'J', ?, 'R2', ?, 3, SYSDATE, 'S', 0, 80)";

			insert_numero_op_stmt = db_connection.prepareStatement(query_numero_op);
			insert_numero_op_stmt.setLong(1, numero_op);
			insert_numero_op_stmt.setInt(2, op_fecha_ing);
			insert_numero_op_stmt.setString(3, observaciones);
			insert_numero_op_stmt.setInt(4, rut_empresa);
			insert_numero_op_stmt.setInt(5, op_fecha_recep);
			insert_numero_op_stmt.setString(6, nombre_empresa_remitente);

			insert_numero_op_stmt.executeUpdate();
			log.debug("OP: " + numero_op);
			String query_evento_op = "INSERT INTO GABINETE.OP_EVENTOS_ING (ID_EVENTO_ING, NRO_OP_ING, FECHA_OP_ING, ESTADO, DESTINO, PLAZO, GLOSA, VIGENCIA, FECHA_CREACION, "
					+ "USUARIO_CREACION) VALUES (?, ?, TO_CHAR(SYSDATE, 'DD/MM/YY'), 1, 'CON', 0, 'Concurso TVD', 1, TO_CHAR(SYSDATE, 'DD/MM/YY'), 'ADM')";

			boolean is_inserted_ok = false;
			int intentos = 0;
			while (!is_inserted_ok) {
				intentos++;
				try {
					Long id_evento = getIdEventoIngresoOP();
					log.debug("ID Evento:" + id_evento);
					insert_op_eventos = db_connection.prepareStatement(query_evento_op);
					insert_op_eventos.setLong(1, id_evento);
					insert_op_eventos.setLong(2, numero_op);

					insert_op_eventos.executeUpdate();
					is_inserted_ok = true;
				} catch (SQLException err) {
					log.error(err.getMessage());
					DBOracleUtils.closeStatement(insert_op_eventos);
					if (err.getSQLState().equals("23000")) {
						is_inserted_ok = false;
						continue;
					} else {
						is_inserted_ok = true;
					}
				}
			}
			log.debug("numero intentos:" + intentos);
		} catch (SQLException e) {
			numero_op = 0L;
			e.printStackTrace();
		} catch (JSONException e) {
			numero_op = 0L;
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeStatement(insert_numero_op_stmt);
		}

		return numero_op;
	}

	private static Long getIdEventoIngresoOP() {
		Long id_evento_op = 0L;

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		Statement stmt = null;
		ResultSet res = null;

		try {
			stmt = db_connection.createStatement();
			res = stmt.executeQuery("SELECT MAX(ID_EVENTO_ING)+1 AS id_evento FROM GABINETE.OP_EVENTOS_ING");

			if (res.next()) {
				id_evento_op = res.getLong("id_evento");
			}
		} catch (SQLException err) {
			log.error(err.getMessage());
		} finally {
			DBOracleUtils.closeResultSet(res);
			DBOracleUtils.closeStatement(stmt);
		}

		return id_evento_op;
	}

	public static Long insertIntoBDCDocumento(Long numero_solicitud, String stdo_codigo)
			throws SQLException {
		log.info("----CREANDO DOCUMENTO----");

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt = null;

		Long doc_codigo = 0L;

		String query_insert_document = "INSERT INTO BDC_DOCUMENTOS (doc_codigo, doc_fecha_documento, stdo_codigo, soli_numero_solicitud) VALUES (?, SYSDATE, ?, ?)";
		doc_codigo = getSequenceValue(db_connection, "SEQ_BDC_DOC.NEXTVAL");
		stmt = db_connection.prepareStatement(query_insert_document);
		stmt.setLong(1, doc_codigo);
		stmt.setString(2, stdo_codigo);
		stmt.setLong(3, numero_solicitud);

		stmt.executeUpdate();

		DBOracleUtils.closeStatement(stmt);

		return doc_codigo;
	}

	public static Long insertElemento(Elemento elemento_to_insert, JSONObject datos_sist_principal,
			boolean is_principal, Long numero_solicitud) throws SQLException {

		log.info("----INSERTANDO ELEMENTO----");

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;
		String insert_element = "INSERT INTO bdc_elementos(elm_codigo, elm_nombre, CLI_RUT_CLIENTE, COD_TIPO_SERVICIO, tel_codigo, tec_codigo, ELM_FECHA_INGRESO) values (SEQ_BDC_ELM.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";
		Long inserted_elm_codigo = 0L;

		stmt = db_connection.prepareStatement(insert_element, new String[] { "elm_codigo" });
		stmt.setString(1, elemento_to_insert.getElm_nombre());
		stmt.setInt(2, elemento_to_insert.getRut_cliente());
		stmt.setString(3, Elemento.getTipoServicio());
		stmt.setInt(4, elemento_to_insert.getTel_codigo());
		stmt.setInt(5, Elemento.TEC_CODIGO);
		stmt.executeUpdate();

		generatedKeys = stmt.getGeneratedKeys();

		if (null != generatedKeys && generatedKeys.next()) {
			inserted_elm_codigo = generatedKeys.getLong(1);
			log.debug("bdc_elementos id:" + inserted_elm_codigo);
		}

		DBOracleUtils.closeResultSet(generatedKeys);
		DBOracleUtils.closeStatement(stmt);

		return inserted_elm_codigo;
	}

	public static boolean insertDatosElemento(Long doc_codigo, JSONObject datos_sist_principal,
			DatosElemento elemento_datos) throws SQLException {

		log.info("----INSERTANDO DATOS ELEMENTO----");
		boolean inserted_ok = false;
		Connection connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;
		Long inserted_elm_codigo = 0L;

		String insert_element = "insert into bdc_datos_elementos (DTE_CODIGO, elm_codigo, dte_direccion, cod_comuna, cod_localidad, latitud_calculada, longitud_calculada,latitud_wgs84, longitud_wgs84, banda, "
				+ "polarizacion, tipoemision, POTENCIA, PUNTOTX, GANANCIA, ALTURAANTENA, TILT, PERDIDASCABLES,UBICACIONESTUDIOPRINCIPAL, UBICACIONESTUDIOALTERNATIVO, UBICACIONPLANTATRANSMISORA, "
				+ "INTENSIDADCAMPO, PLAZOINICIOOBRAS, PLAZOTERMINOOBRAS, PLAZOINICIOSERVICIO,ACTIVIDADECONOMICA, CANTIDADELEMENTOS, UNIDADPOTENCIA, UNIDADGANANCIA, COD_REGION, PERDIDASDIVPOTENCIA, FRECUENCIA, "
				+ "FECHA_CREACION, ESTADO_ELEMENTO,GANANCIA_HORIZONTAL, TCR_CODIGO, TIAN_COD, DTE_LATITUD_SUR_GR, DTE_LATITUD_SUR_MI,DTE_LATITUD_SUR_SG, DTE_LONGITUD_OESTE_GR, DTE_LONGITUD_OESTE_MI, "
				+ "DTE_LONGITUD_OESTE_SG,OTRASPERDIDAS, DTE_MOVIMIENTO, DOC_CODIGO, ZONASERVICIO, ANTENA, DIAGRAMADERADIACION) VALUES (SEQ_BDC_DELM.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		stmt = connection.prepareStatement(insert_element, new String[] { "DTE_CODIGO" });
		stmt.setLong(1, elemento_datos.getElm_codigo());
		stmt.setString(2, elemento_datos.getDte_direccion());
		stmt.setLong(3, elemento_datos.getCod_comuna());
		stmt.setLong(4, elemento_datos.getCod_localidad());
		stmt.setDouble(5, elemento_datos.getLatitud_calculada());
		stmt.setDouble(6, elemento_datos.getLongitud_calculada());
		stmt.setDouble(7, elemento_datos.getLatitud_WGS84());
		stmt.setDouble(8, elemento_datos.getLongitud_WGS84());
		stmt.setString(9, DatosElemento.getBanda());
		stmt.setString(10, elemento_datos.getPolarizacion());
		stmt.setString(11, elemento_datos.getTipo_emision());
		stmt.setString(12, elemento_datos.getPotencia_max_tx());
		stmt.setString(13, elemento_datos.getPunto_tx());
		stmt.setString(14, elemento_datos.getGanancia());
		stmt.setString(15, elemento_datos.getAltura_antena_TX());
		stmt.setString(16, elemento_datos.getTilt());
		stmt.setString(17, elemento_datos.getPerdidas_cable());
		stmt.setString(18, elemento_datos.getUbicacion_estudio_principal());
		stmt.setString(19, elemento_datos.getUbicacion_estudio_alternativo());
		stmt.setString(20, elemento_datos.getUbicacion_planta_transmisora());
		stmt.setString(21, elemento_datos.getIntensidad_campo());
		stmt.setString(22, elemento_datos.getFecha_inicio());
		stmt.setString(23, elemento_datos.getFecha_termino());
		stmt.setString(24, elemento_datos.getInicio_servicio());
		stmt.setString(25, DatosElemento.getActividadEconomica());
		stmt.setString(26, elemento_datos.getCantidad_elementos());
		stmt.setString(27, DatosElemento.getUnidadPotencia());
		stmt.setString(28, DatosElemento.getUnidadGanancia());
		stmt.setLong(29, elemento_datos.getCod_region());
		stmt.setString(30, elemento_datos.getPerdidas_div_potencia());
		stmt.setDouble(31, elemento_datos.getFrecuencia());
		stmt.setString(32, DatosElemento.getEstadoElem());
		stmt.setString(33, elemento_datos.getGanancia_horizontal());
		stmt.setInt(34, DatosElemento.getTcrCodigo());
		stmt.setString(35, elemento_datos.getTian_cod());
		stmt.setDouble(36, elemento_datos.getDte_latitud_sur_gr());
		stmt.setDouble(37, elemento_datos.getDte_latitud_sur_min());
		stmt.setDouble(38, elemento_datos.getDte_latitud_sur_sg());
		stmt.setDouble(39, elemento_datos.getDte_longitud_oeste_gr());
		stmt.setDouble(40, elemento_datos.getDte_longitud_oeste_min());
		stmt.setDouble(41, elemento_datos.getDte_longitud_oeste_sg());
		stmt.setDouble(42, elemento_datos.getOtras_perdidas());
		stmt.setString(43, DatosElemento.getDteMovimiento());
		stmt.setLong(44, doc_codigo);
		stmt.setString(45, elemento_datos.getZona_servicio());
		stmt.setString(46, elemento_datos.getTipo_antena_nombre());
		stmt.setString(47, elemento_datos.getTipo_radiacion());
		stmt.executeUpdate();

		generatedKeys = stmt.getGeneratedKeys();
		if (null != generatedKeys && generatedKeys.next()) {
			inserted_elm_codigo = generatedKeys.getLong(1);
			log.debug("bdc_datos_elementos id:" + inserted_elm_codigo);
			inserted_ok = insertarDatosDependientes(datos_sist_principal, elemento_datos.getElm_codigo(),
					inserted_elm_codigo, connection);
		}

		DBOracleUtils.closeResultSet(generatedKeys);
		DBOracleUtils.closeStatement(stmt);
		return inserted_ok;
	}

	public static boolean insertarDatosDependientes(JSONObject datos_sist_principal, Long elem_codigo,
			Long inserted_elm_data_codigo, Connection connection) {

		boolean inserted_ok = false;
		boolean inserted_perdidas_ok = insertDatosRadiales(datos_sist_principal, inserted_elm_data_codigo,
				ID_TRDL_PERDIDAS_LOBULO, "perdidas", connection);
		boolean inserted_dist_ok = insertDatosRadiales(datos_sist_principal, inserted_elm_data_codigo,
				ID_TRDL_ZONA_SERVICIO, "zona", connection);
		boolean inserted_arreglo_ok = insertArregloAntena(datos_sist_principal, elem_codigo, connection);
		boolean inserted_detalle_rtv = insertDetalleRTV(datos_sist_principal, inserted_elm_data_codigo, connection);

		if (inserted_perdidas_ok && inserted_dist_ok && inserted_arreglo_ok && inserted_detalle_rtv) {
			inserted_ok = true;
		}

		return inserted_ok;
	}

	public static boolean insertDetalleRTV(JSONObject datos_sist_principal, Long dte_codigo, Connection db_connection) {
		log.info("----INSERTANDO DATOS DETALLE RTV----");

		boolean inserted_ok = false;
		PreparedStatement stmt = null;
		try {
			JSONObject calculos = new JSONObject(
					datos_sist_principal.getString("calculos").replace("[", "").replace("]", ""));
			String canal = calculos.get("canal").toString();
			Long potencia = Long.parseLong(calculos.get("pPotencia").toString());

			stmt = db_connection.prepareStatement("INSERT INTO BDC_DETALLE_RTV "
					+ "(ID_DETRTV, DTE_CODIGO, CANAL, POTENCIAVIDEO, UNIDADPOTENCIAVIDEO) VALUES (SEQ_DETALLE_TV.NEXTVAL,?,?,?,?)");

			stmt.setLong(1, dte_codigo);
			stmt.setString(2, canal);
			stmt.setLong(3, potencia);
			stmt.setLong(4, Long.parseLong(DatosElemento.getUnidadPotencia()));

			stmt.executeUpdate();

			inserted_ok = true;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBOracleUtils.closeStatement(stmt);

		return inserted_ok;
	}

	/**
	 * @param datos_sist_principal
	 *            Corresponde a la información almacenada en Mongo de los calculos
	 *            predictivos
	 * @param datos_elemento_id
	 *            ID relación con la tabla bdc_datos_elementos
	 * @param id_tdl
	 *            ID correspondiente al tipo de radial (Perd Lob: 4, Zona Servicio:
	 *            3)
	 * @param variable_radial
	 *            Nombre del radial que se va a guardar (perdidas: Perdidas Lóbulo,
	 *            zona: Zona de Servicio)
	 */
	public static boolean insertDatosRadiales(JSONObject datos_sist_principal, Long datos_elemento_id, int id_tdl,
			String variable_radial, Connection db_connection) {

		log.info("----INSERTANDO DATOS RADIALES: " + variable_radial + "----");
		JSONObject calculos = null;
		PreparedStatement stmt = null;
		boolean inserted_ok = false;

		try {
			Long id_derad = insertDatElemRadiales(datos_elemento_id, id_tdl, db_connection);
			calculos = new JSONObject(datos_sist_principal.getString("calculos").replace("[", "").replace("]", ""));
			int radiales = Integer.parseInt(calculos.get("radiales").toString());

			int idx_loop = 0;
			int grados_secuencia = 360 / radiales;

			stmt = db_connection.prepareStatement(
					"INSERT INTO BDC_DATOS_RADIALES (ID_DRAD, ID_DERAD, DERAD_GRADO, DERAD_VALOR) VALUES (BDC_SUBTEL.SEQ_DRAD_ID.NEXTVAL, ?, ?, ?)");

			String tipo_radial_texto = getRadialTexto(variable_radial, radiales);
			int grados_texto = 0;
			String radial_texto = "";
			double radial_valor = 0;

			while (idx_loop < radiales) {
				grados_texto = grados_secuencia * idx_loop;
				radial_texto = tipo_radial_texto + grados_texto;
				radial_valor = Double.parseDouble(calculos.get(radial_texto).toString());

				stmt.setLong(1, id_derad);
				stmt.setInt(2, grados_texto);
				stmt.setDouble(3, radial_valor);

				stmt.addBatch();
				idx_loop++;
			}

			stmt.executeBatch();
			inserted_ok = true;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBOracleUtils.closeStatement(stmt);

		return inserted_ok;
	}

	public static boolean insertArregloAntena(JSONObject datos_sist_principal, Long elm_codigo,
			Connection db_connection) {

		log.info("----INSERTANDO DATOS ARREGLO ANTENAS----");

		boolean inserted_ok = false;
		PreparedStatement stmt = null;
		try {
			JSONObject form_data = new JSONObject(
					datos_sist_principal.getString("calculos").replace("[", "").replace("]", ""))
							.getJSONObject("form_data");
			JSONObject arreglo_antenas = form_data.getJSONObject("arreglos_antena");
			JSONObject ganancia = arreglo_antenas.getJSONObject("ganancia");
			JSONObject azimutV = arreglo_antenas.getJSONObject("azimutV");
			JSONObject marca = arreglo_antenas.getJSONObject("marca");
			JSONObject numero = arreglo_antenas.getJSONObject("numero");
			JSONObject modelo = arreglo_antenas.getJSONObject("modelo");
			JSONObject potencia = arreglo_antenas.getJSONObject("potencia");
			JSONObject polarizacion = arreglo_antenas.getJSONObject("polarizacion");
			JSONObject largo = arreglo_antenas.getJSONObject("largo");
			JSONObject fase = arreglo_antenas.getJSONObject("fase");
			JSONObject azimutA = arreglo_antenas.getJSONObject("azimutA");
			JSONObject altura = arreglo_antenas.getJSONObject("altura");

			int numero_elementos = Integer
					.parseInt(form_data.getJSONObject("carac_tecnicas").get("num_elem").toString());

			stmt = db_connection.prepareStatement(
					"INSERT INTO BDC_ARREGLO_ANTENA (ID_ARREGLO,ELEM_CODIGO, NUMERO_ARREGLO, ALTURA, UNIDAD_ALTURA, LARGO_VASTAGO, UNIDAD_LARGO_VASTAGO, "
							+ "AZIMUT_VASTAGO, UNIDAD_AZIMUT_VASTAGO, AZIMUT_ANTENA, UNIDAD_AZIMUT_ANTENA, GANANCIA_ANTENA, UNIDAD_GANANCIA_ANTENA, POLARIZACION, MARCA, MODELO, FASE, UNIDAD_FASE, POTENCIA) VALUES"
							+ "(BDC_SUBTEL.SEQ_BDC_AANTENA.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			int idx_loop = 0;

			while (idx_loop < numero_elementos) {
				stmt.setLong(1, elm_codigo);
				stmt.setString(2, numero.get("ant_num" + idx_loop).toString());
				stmt.setString(3, altura.get("ant_alt" + idx_loop).toString());
				stmt.setInt(4, ArregloAntena.getUnidadAltura());
				stmt.setString(5, largo.get("ant_lar" + idx_loop).toString());
				stmt.setInt(6, ArregloAntena.getUnidadLargoVastago());
				stmt.setString(7, azimutV.get("ant_aziV" + idx_loop).toString());
				stmt.setInt(8, ArregloAntena.getUnidadAzimutVastago());
				stmt.setString(9, azimutA.get("ant_aziA" + idx_loop).toString());
				stmt.setInt(10, ArregloAntena.getUnidadAzimutAntena());
				stmt.setString(11, ganancia.get("ant_gan" + idx_loop).toString());
				stmt.setInt(12, ArregloAntena.getUnidadGananciaAntena());
				stmt.setString(13, polarizacion.get("ant_pol" + idx_loop).toString());
				stmt.setString(14, marca.get("ant_marc" + idx_loop).toString());
				stmt.setString(15, modelo.get("ant_mod" + idx_loop).toString());
				stmt.setString(16, fase.get("ant_fase" + idx_loop).toString());
				stmt.setInt(17, ArregloAntena.getUnidadFase());
				stmt.setString(18, potencia.get("ant_pot" + idx_loop).toString());

				stmt.executeUpdate();
				idx_loop++;
			}

			inserted_ok = true;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBOracleUtils.closeStatement(stmt);

		return inserted_ok;
	}

	public static Long insertDatElemRadiales(Long datos_elemento_id, int id_tdl, Connection db_connection) {
		log.info("----INSERTANDO DATOS ELEMENTO RADIAL----");
		Long id_derad = 0L;
		PreparedStatement stmt = null;
		ResultSet res = null;

		try {
			stmt = db_connection.prepareStatement(
					"INSERT INTO BDC_DAT_ELEM_RADIALES (ID_DERAD, DERAD_USUARIO_CREA, DERAD_FECHA_CREA, DTE_CODIGO, ID_TRDL) "
							+ "VALUES (BDC_SUBTEL.SEQ_DE_RAD_ID.NEXTVAL, \'ADM\', SYSDATE, ?, ?)",
					new String[] { "ID_DERAD" });
			stmt.setLong(1, datos_elemento_id);
			stmt.setInt(2, id_tdl);
			stmt.executeUpdate();

			res = stmt.getGeneratedKeys();
			if (null != res && res.next()) {
				id_derad = res.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBOracleUtils.closeResultSet(res);
		DBOracleUtils.closeStatement(stmt);

		return id_derad;
	}

	public static int getTcrCodigo() {
		int tcr_cod = 0;
		tcr_cod = Integer.parseInt(
				getColumnCode("TCR_CODIGO", "SGF_TIPO_COORDENADAS", "TCR_NOMBRE", DatosElemento.getDatum()).toString());
		return tcr_cod;
	}

	public static String getTianCod(String tipo_antena) {
		String tian_cod = getColumnCode("TIAN_COD", "BDC_TIPO_ANTENA", "TIAN_GLOSA", tipo_antena).toString();
		return tian_cod;
	}

	public static String getPolarizacionCod(double polarizacion_perc_horizontal, double polarizacion_perc_vertical) {
		String tipo_polarizacion = "";
		String polarizacion_alias = "";

		tipo_polarizacion = TvdUtils.getNamePolarizacionByType(polarizacion_perc_horizontal,
				polarizacion_perc_vertical);
		polarizacion_alias = getColumnCode("cod_polarizacion", "polarizacion", "NOMBRE", tipo_polarizacion).toString();

		return polarizacion_alias;
	}

	public static String getTipoEmisionCod(String tipo_emision) {

		String tipo_emision_cod = getColumnCode("COD_TIPO_EMISION", "TIPO_EMISION", "NOMBRE",
				tipo_emision.toUpperCase()).toString();
		return tipo_emision_cod;
	}

	public static String getRadialTexto(String variable_radial, int radiales) {
		String radial_texto = "";

		switch (variable_radial) {
		case "perdidas":
			radial_texto = "M" + radiales + "PL";
			break;
		case "zona":
			radial_texto = "DIS";
			break;
		}

		return radial_texto;
	}

	public static boolean existeCliente(String rut_cliente) {
		String rut_splitted[] = rut_cliente.split("-");
		int rut = Integer.parseInt(rut_splitted[0]);
		String digito_verificador = rut_splitted[1];
		boolean existe = false;

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt = null;
		ResultSet res = null;

		try {
			stmt = db_connection
					.prepareStatement("SELECT RUT_CLIENTE FROM BDC_SUBTEL.CLIENTES WHERE RUT_CLIENTE = ? AND DV = ?");
			stmt.setInt(1, rut);
			stmt.setString(2, digito_verificador);

			res = stmt.executeQuery();

			if (res.next()) {
				log.debug("Existe cliente:" + res.getString("RUT_CLIENTE"));
				existe = true;
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeResultSet(res);
			DBOracleUtils.closeStatement(stmt);
		}

		return existe;
	}

	public static Long getCodigoLocalidad(Long cod_comuna) {
		Long codigo = 0L;

		Connection connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt = null;
		ResultSet res = null;

		try {
			stmt = connection.prepareStatement("SELECT cod_localidad FROM LOCALIDAD WHERE COM_COD_COMUNA = ?");
			stmt.setLong(1, cod_comuna);
			res = stmt.executeQuery();

			if (res.next()) {
				codigo = res.getLong("cod_localidad");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeResultSet(res);
			DBOracleUtils.closeStatement(stmt);
		}

		return codigo;
	}

	public static Long getCodigoComuna(String nombre_comuna) {
		Long codigo = 0L;

		Connection connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt = null;
		ResultSet res = null;

		try {
			stmt = connection
					.prepareStatement("SELECT COD_COMUNA FROM VIEW_STI_DPA3_COMUNAS WHERE UPPER(COMUNA) = UPPER(?)");
			stmt.setString(1, nombre_comuna);
			res = stmt.executeQuery();

			if (res.next()) {
				codigo = res.getLong("COD_COMUNA");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeResultSet(res);
			DBOracleUtils.closeStatement(stmt);
		}

		return codigo;
	}

	public static Long getCodigoRegion(Long cod_comuna) {
		Long codigo = 0L;

		Connection connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt = null;
		ResultSet res = null;

		try {
			stmt = connection.prepareStatement("SELECT REG_COD_REGION FROM COMUNA WHERE cod_comuna = ?");
			stmt.setLong(1, cod_comuna);
			res = stmt.executeQuery();

			if (res.next()) {
				codigo = res.getLong("REG_COD_REGION");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeResultSet(res);
			DBOracleUtils.closeStatement(stmt);
		}

		return codigo;
	}

	public static void createWftDocumento(String doc_path, String stdo_codigo, Long num_solicitud, Long num_ofi_parte) {
		log.info("INSERTANDO EN WFT_DOCUMENTO");

		PreparedStatement stmt_new_wft_document = null;

		try {
			Connection db_connection = DBOracleUtils.getSingletonInstance();
			stmt_new_wft_document = null;
			String query_eft_doc = "INSERT INTO wft_documentos (NUMERO_DOCUMENTO, FECHA_DOCUMENTO, NRO_OFPARTES, STDO_CODIGO, NRO_SOLICITUD, USUARIO_INGRESO, FECHA_INGRESO, PATH) "
					+ "values (BDC_SUBTEL.SEQ_OFP.NEXTVAL, SYSDATE, ?, ?, ?, 'ADM', SYSDATE, ?)";

			stmt_new_wft_document = db_connection.prepareStatement(query_eft_doc);
			stmt_new_wft_document.setLong(1, num_ofi_parte);
			stmt_new_wft_document.setString(2, stdo_codigo);
			stmt_new_wft_document.setLong(3, num_solicitud);
			stmt_new_wft_document.setString(4, doc_path);

			log.debug("---------");
			log.debug("Stdo:" + stdo_codigo + " - Num Soli: " + num_solicitud);
			log.debug("---------");
			stmt_new_wft_document.executeUpdate();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			ex.printStackTrace();
		} finally {
			DBOracleUtils.closeStatement(stmt_new_wft_document);
		}
	}

	public static boolean insertDataCliente(Clientes data_cliente, DatosEmpresa datos_empresa) {

		if (insertIntoClientes(data_cliente) && insertIntoDatosEmpresa(data_cliente, datos_empresa)) {
			return true;
		}

		log.info("---DATOS CLIENTE Y EMPRESA INGRESADOS CORRECTAMENTE---");

		return false;
	}

	private static boolean insertIntoClientes(Clientes data_cliente) {

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt_insert_cliente = null;

		boolean inserted_ok = false;

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

			inserted_ok = true;
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeStatement(stmt_insert_cliente);
		}

		return inserted_ok;
	}

	private static boolean insertIntoDatosEmpresa(Clientes data_cliente, DatosEmpresa datos_empresa) {

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt_insert_data_empresa = null;
		boolean inserted_ok = false;

		try {

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

			inserted_ok = true;
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeStatement(stmt_insert_data_empresa);
		}

		return inserted_ok;
	}

	public static boolean updateDataCliente(Clientes data_cliente, DatosEmpresa datos_empresa) {

		if (updateClientes(data_cliente) && updateDatosEmpresa(data_cliente, datos_empresa)) {
			return true;
		}

		log.info("---DATOS CLIENTE Y EMPRESA ACTUALIZADOS CORRECTAMENTE---");

		return false;
	}

	private static boolean updateClientes(Clientes data_cliente) {

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt_insert_cliente = null;

		boolean inserted_ok = false;

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

			inserted_ok = true;
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeStatement(stmt_insert_cliente);
		}

		return inserted_ok;
	}

	private static boolean updateDatosEmpresa(Clientes data_cliente, DatosEmpresa datos_empresa) {

		Connection db_connection = DBOracleUtils.getSingletonInstance();
		PreparedStatement stmt_insert_data_empresa = null;
		boolean inserted_ok = false;

		try {

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

			inserted_ok = true;
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			DBOracleUtils.closeStatement(stmt_insert_data_empresa);
		}

		return inserted_ok;
	}

	public static void insertElementosEstudios(Elemento estudios[], JSONObject datos_sist_principal,
			Long numero_solicitud) throws SQLException {

		int idx_loop = 0;
		int cant_elementos_estudios = estudios.length;
		while (idx_loop < cant_elementos_estudios) {
			if (estudios[idx_loop] != null) {
				insertElemento(estudios[idx_loop], datos_sist_principal, false, numero_solicitud);
			}
			idx_loop++;
		}
	}
}
