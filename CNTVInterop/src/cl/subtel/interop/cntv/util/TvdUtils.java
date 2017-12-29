package cl.subtel.interop.cntv.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.subtel.interop.cntv.calculotvd.CarpetaTecnica;
import cl.subtel.interop.cntv.calculotvd.CarpetaTecnicaFiles;
import cl.subtel.interop.cntv.calculotvd.Clientes;
import cl.subtel.interop.cntv.calculotvd.DatosEmpresa;
import cl.subtel.interop.cntv.calculotvd.Elemento;
import cl.subtel.interop.cntv.dto.DocumentoDTO;
import cl.subtel.interop.cntv.dto.PostulacionDTO;
import cl.subtel.interop.cntv.dto.RespuestaDTO;

public class TvdUtils {
	private static final Logger log = LogManager.getLogger();

	public static String getIntensityByTypeService(String type_sist) {
		String name_sist = "";

		if (type_sist.equals("ZonaServicio"))
			name_sist = "48";
		else if (type_sist.equals("ZonaCobertura"))
			name_sist = "40";
		else if (type_sist.equals("ZonaUrbana"))
			name_sist = "66";

		return name_sist;
	}

	public static String getSistRadiantTypeByAlias(String anthena_alias) {
		String anthena_name = "";

		if (anthena_alias.equals("PTx0"))
			anthena_name = "sistRadiantePrinc";
		else if (anthena_alias.equals("PTx1"))
			anthena_name = "sistRadianteAdic1";
		else if (anthena_alias.equals("PTx2"))
			anthena_name = "sistRadianteAdic2";
		// switch (anthena_alias) {
		// case "PTx0":
		// anthena_name = "sistRadiantePrinc";
		// break;
		// case "PTx1":
		// anthena_name = "sistRadianteAdic1";
		// break;
		// case "PTx2":
		// anthena_name = "sistRadianteAdic2";
		// break;
		// default:
		// break;
		// }

		return anthena_name;
	}

	public static Elemento createElementoSistPrincipal(JSONObject datos_sist_principal, String nombre_archivo,
			String tipo_elemento) {

		Elemento elemento_sist_principal = new Elemento();
		String elm_nombre = nombre_archivo;
		String rut_cliente = "";
		int tel_codigo = DBOracleDAO.getTelCodByTipoElemento(tipo_elemento);

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

	public static String getNamePolarizacionByType(double polarizacion_perc_horizontal,
			double polarizacion_perc_vertical) {
		String tipo_polarizacion = "";

		if (polarizacion_perc_horizontal > 0 && polarizacion_perc_vertical == 0) {
			tipo_polarizacion = "Horizontal";
		} else if (polarizacion_perc_vertical > 0 && polarizacion_perc_horizontal == 0) {
			tipo_polarizacion = "Vertical";
		} else {
			tipo_polarizacion = "Mixta";
		}

		return tipo_polarizacion;
	}

	public static Clientes createClienteFromJSON(JSONObject datos_cliente) {
		Clientes cliente = new Clientes();

		try {
			String rut[] = datos_cliente.get("rut").toString().split("-");
			Long rut_cliente = Long.valueOf(rut[0]);
			Long cod_comuna = Long.valueOf(datos_cliente.get("comunaId").toString());
			String razon_social = datos_cliente.get("razonSocial").toString();
			String alias = datos_cliente.get("nombreFantasia").toString();
			String direccion = datos_cliente.get("direccion").toString();

			cliente = new Clientes(rut_cliente, cod_comuna, rut[1], razon_social, alias, direccion);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		return cliente;
	}

	public static DatosEmpresa createDatosEmpresaFromJSON(Clientes datos_cliente, JSONObject datos_empresa) {
		DatosEmpresa empresa = new DatosEmpresa();

		try {
			Long rut_cliente = datos_cliente.getRut_cliente();
			String alias_cliente = datos_cliente.getAlias();
			String fono = datos_empresa.get("telefonoFijo").toString();
			String fono2 = datos_empresa.get("telefonoOtro").toString();
			String email = datos_empresa.get("email").toString();
			String nombre_contacto = datos_empresa.get("nombre").toString() + " "
					+ datos_empresa.get("apellidoPaterno").toString() + " "
					+ datos_empresa.get("apellidoMaterno").toString();
			String rut_contacto = datos_empresa.get("rut").toString();

			empresa = new DatosEmpresa(rut_cliente, fono, fono2, alias_cliente, email, nombre_contacto, rut_contacto);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		return empresa;
	}

	public static void validateExisteCliente(JSONObject user_data, String rut_empresa) throws JSONException {
		Clientes cliente = TvdUtils.createClienteFromJSON(user_data.getJSONObject("empresa"));

		DatosEmpresa empresa = TvdUtils.createDatosEmpresaFromJSON(cliente,
				user_data.getJSONObject("representanteLegal"));
		if (!DBOracleDAO.existeCliente(rut_empresa)) {
			log.debug("Cliente no existe, agregando...");
			Clientes.insertDataCliente(cliente, empresa, log);
		} else {
			log.debug("Cliente existe, actualizando...");
			Clientes.updateDataCliente(cliente, empresa, log);
		}
	}

	public static void preparedDocumentDataToInsertMatriz(Long num_ofi_parte, Long numero_solicitud, String temp_folder,
			String postulation_code, JSONObject user_data, Logger log) throws SQLException, JSONException {

		log.debug("insertDocumentDataToMatriz");

		File temp_technical_folder = new File(temp_folder);
		File[] files_list = temp_technical_folder.listFiles();
		int userID = user_data.getInt("id");

		if (numero_solicitud != 0L) {
			for (int ix = 0; ix < files_list.length; ix++) {
				String nombre_archivo = files_list[ix].getName();
				String unextension_file_name = nombre_archivo.split("\\.")[0];
				String stdo_codigo = CarpetaTecnicaFiles.getSTDOCod(unextension_file_name);
				// Solo cuando es la zona de servicio de la planta principal se inserta la info
				// en la matriz tecnologica
				if (nombre_archivo.contains("ZonaServicio_PTx0") && nombre_archivo.contains("pdf")) {
					Elemento.insertarDatosSistPrincipal(nombre_archivo, numero_solicitud, postulation_code, userID,
							stdo_codigo);
				} else {
					// Si no, solo se inserta la información del documento que se va a subir al
					// repositorio.
					if (!"".equals(stdo_codigo)) {
						Long cod_documento = DBOracleDAO.createBDCDocumento(numero_solicitud, stdo_codigo);
						System.out.println("cod:" + cod_documento);
					}
				}
				String doc_path = CarpetaTecnicaFiles.uploadFile(files_list[ix], nombre_archivo, num_ofi_parte);
				log.debug("Doc path:" + doc_path);
				if (!"".equals(stdo_codigo)) {
					DBOracleDAO.createWftDocumento(doc_path, stdo_codigo, numero_solicitud, num_ofi_parte);
				}
			}
		}
	}

	public static int getFormattedOPDate() {
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		String formatted_month = month < 10 ? "0" + month : String.valueOf(month);
		String formatted_day = day < 10 ? "0" + day : String.valueOf(day);
		String date = year + formatted_month + formatted_day;
		int formatted_date = Integer.parseInt(date);

		return formatted_date;
	}

	public static RespuestaDTO getDataCarpetaTecnica(PostulacionDTO postulacion, String userID, Long num_solicitud,
			Long num_ofi_parte, String codigoPostulacion, JSONObject user_data) {
		boolean correcto = true;
		String temp_folder = "";
		String response_message = "";

		Connection db_connection = DBOracleUtils.getSingletonInstance();

		try {
			validateExisteCliente(user_data, user_data.getJSONObject("empresa").getString("rut"));
			List<DocumentoDTO> lista = postulacion.getArchivos();
			DocumentoDTO doc = lista.get(0);
			temp_folder = CarpetaTecnica.saveFile(userID, doc);

			String response_validate_data = CarpetaTecnica.validateDataTecnica(temp_folder, codigoPostulacion,
					Integer.parseInt(userID));

			log.debug(response_validate_data);

			if ("".equals(response_validate_data)) {
				TvdUtils.preparedDocumentDataToInsertMatriz(num_ofi_parte, num_solicitud, temp_folder,
						codigoPostulacion, user_data, log);

				DBOracleUtils.commit();
				response_message = "Se recibio la carpeta tecnica";
				correcto = true;
			} else {
				response_message = response_validate_data;
			}

		} catch (JSONException e) {
			correcto = false;
			response_message = "Datos tecnicos guardados incompletos";
			log.error("recibirCarpetaTecnica:" + e.getMessage());
			DBOracleUtils.rollback();
			e.printStackTrace();
		} catch (SQLException e) {
			correcto = false;
			log.error("recibirCarpetaTecnica:" + e.getMessage());
			response_message = "Error en postulación, contactarse con: mesa.ayuda@subtel.gob.cl";
			DBOracleUtils.rollback();
			e.printStackTrace();
		} catch (NullPointerException err) {
			correcto = false;
			log.error("recibirCarpetaTecnica:" + err.getMessage());
			response_message = "No existen datos del usuario o datos técnicos guardados";
			DBOracleUtils.rollback();
			err.printStackTrace();
		} catch (IOException e) {
			correcto = false;
			log.error("recibirCarpetaTecnica: " + e.getMessage());
			response_message = "Error descomprimiendo archivo carpeta tecnica, contactarse con: mesa.ayuda@subtel.gob.cl";
			DBOracleUtils.rollback();
			e.printStackTrace();
		} catch (IllegalArgumentException ex) {
			correcto = false;
			log.error("recibirCarpetaTecnica: " + ex.getMessage());
			response_message = "Error descomprimiendo archivo carpeta tecnica, contactarse con: mesa.ayuda@subtel.gob.cl";
			DBOracleUtils.rollback();
			ex.printStackTrace();
		} finally {
			DBOracleUtils.closeConnection(db_connection);
		}

		CarpetaTecnica.deleteTempFolder(temp_folder);
		RespuestaDTO respuesta = new RespuestaDTO();
		String response_code = correcto ? "OK" : "NOK";

		respuesta.setCodigo(response_code);
		respuesta.setMensaje(response_message);

		log.debug("Cod:" + response_code);
		log.debug("Msg:" + response_message);
		log.info("** FIN CarpetaTecnica **");

		return respuesta;
	}
}
