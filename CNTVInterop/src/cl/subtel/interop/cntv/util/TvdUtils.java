package cl.subtel.interop.cntv.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.subtel.interop.cntv.calculotvd.CarpetaTecnicaFiles;
import cl.subtel.interop.cntv.calculotvd.Clientes;
import cl.subtel.interop.cntv.calculotvd.DatosElemento;
import cl.subtel.interop.cntv.calculotvd.DatosEmpresa;
import cl.subtel.interop.cntv.calculotvd.Elemento;

public class TvdUtils {

	private static final Logger log = LogManager.getLogger();

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

	public static boolean validateExisteCliente(JSONObject user_data, String rut_empresa) {

		boolean inserted_ok = false;

		Clientes cliente;
		try {
			cliente = TvdUtils.createClienteFromJSON(user_data.getJSONObject("empresa"));

			DatosEmpresa empresa = TvdUtils.createDatosEmpresaFromJSON(cliente,
					user_data.getJSONObject("representanteLegal"));

			if (!DBOracleDAO.existeCliente(rut_empresa)) {
				log.info("Cliente no existe, agregando...");
				inserted_ok = DBOracleDAO.insertDataCliente(cliente, empresa);
			} else {
				log.info("Cliente existe, actualizando...");
				inserted_ok = DBOracleDAO.updateDataCliente(cliente, empresa);
			}

		} catch (JSONException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return inserted_ok;
	}

	public static Long insertDocumentDataToMatriz(String temp_folder, String postulation_code, JSONObject user_data) throws SQLException, JSONException {

		log.info("---- INICIANDO INSERCI�N DATA DOCUMENTOS A MATRIZ ----");

		File temp_technical_folder = new File(temp_folder);
		File[] files_list = temp_technical_folder.listFiles();
		int files_count = files_list.length;

		String user_name = user_data.get("nombre").toString();
		String nombre_archivo = "";

		Long num_ofi_parte = DBOracleDAO.getNumeroOP(user_data.getJSONObject("empresa"));
		Long numero_solicitud = DBOracleDAO.createSolitudConcesiones(num_ofi_parte, user_data.getJSONObject("empresa"));

		if (numero_solicitud != 0L) {
			for (int ix = 0; ix < files_count; ix++) {
				nombre_archivo = files_list[ix].getName();
				String unextension_file_name = nombre_archivo.split("\\.")[0];
				String stdo_codigo = CarpetaTecnicaFiles.getSTDOCod(unextension_file_name);

				if (nombre_archivo.contains("ZonaServicio_PTx0") && nombre_archivo.contains("pdf")) {
					log.debug("Zona Servicio Principal");
					JSONObject datos_sist_principal = DBMongoDAO.getDatosTecnicosConcurso(nombre_archivo,
							postulation_code, user_name);
					Elemento elemento_principal = DBOracleDAO.createElementoSistPrincipal(datos_sist_principal,
							nombre_archivo, "Planta Transmisora");
					Elemento estudios[] = DBOracleDAO.createElementosEstudios(datos_sist_principal, nombre_archivo);

					// Insertar Elemento Sistema Principal en tabla BDC_ELEMENTOS
					Long inserted_elm_codigo = DBOracleDAO.insertElemento(elemento_principal, datos_sist_principal,
							true, numero_solicitud);

					log.debug("bdc_elementos id: " + inserted_elm_codigo);
					// Una vez creado el Elemento se asocia a �ste un documento en la tabla
					// BDC_DOCUMENTOS
					Long doc_codigo = DBOracleDAO.insertIntoBDCDocumento(numero_solicitud, stdo_codigo);

					log.debug("bdc_documentos id:" + doc_codigo);
					DatosElemento elemento_datos = new DatosElemento(inserted_elm_codigo, datos_sist_principal);
					DBOracleDAO.insertDatosElemento(doc_codigo, datos_sist_principal, elemento_datos);

					// Si el sistema principal tiene m�s de un estudio (Estudios alternativos) se
					// guarda sin la informaci�n sin crear un documento
					DBOracleDAO.insertElementosEstudios(estudios, datos_sist_principal, numero_solicitud);

				} else {
					if (!"".equals(stdo_codigo)) {
						Long cod_documento = DBOracleDAO.insertIntoBDCDocumento(numero_solicitud, stdo_codigo);
						log.debug(stdo_codigo + " - Doc codigo: " + cod_documento);
					}
				}

				String doc_path = CarpetaTecnicaFiles.uploadFile(files_list[ix], nombre_archivo, num_ofi_parte);
				log.debug("Doc path:" + doc_path);
				if (!"".equals(stdo_codigo)) {
					DBOracleDAO.createWftDocumento(doc_path, stdo_codigo, numero_solicitud, num_ofi_parte);
				}
			}
		}

		return num_ofi_parte;
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

}
