package cl.subtel.interop.cntv.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.logging.log4j.Logger;

import cl.subtel.interop.cntv.calculotvd.CarpetaTecnica;
import cl.subtel.interop.cntv.calculotvd.CarpetaTecnicaFiles;
import cl.subtel.interop.cntv.calculotvd.Clientes;
import cl.subtel.interop.cntv.calculotvd.DatosEmpresa;
import cl.subtel.interop.cntv.calculotvd.Elemento;

public class TvdUtils {

	public static String getIntensityByTypeService(String type_sist) {
		String name_sist = "";

		switch (type_sist) {
		case "ZonaServicio":
			name_sist = "48";
			break;
		case "ZonaCobertura":
			name_sist = "40";
			break;
		case "ZonaUrbana":
			name_sist = "66";
			break;
		default:
			break;
		}

		return name_sist;
	}

	public static String getSistRadiantTypeByAlias(String anthena_alias) {
		String anthena_name = "";

		switch (anthena_alias) {
		case "PTx0":
			anthena_name = "sistRadiantePrinc";
			break;
		case "PTx1":
			anthena_name = "sistRadianteAdic1";
			break;
		case "PTx2":
			anthena_name = "sistRadianteAdic2";
			break;
		default:
			break;
		}

		return anthena_name;
	}

	public static Elemento createElementoSistPrincipal(JSONObject datos_sist_principal, String nombre_archivo,
			String tipo_elemento) {

		Elemento elemento_sist_principal = new Elemento();
		String elm_nombre = nombre_archivo;
		String rut_cliente = "";
		int tel_codigo = OracleDBUtils.getTelCodByTipoElemento(tipo_elemento);

		OracleDBUtils.connect();

		try {
			String datos = datos_sist_principal.getString("datos").replace("[", "").replace("]", "");
			JSONObject datos_json = new JSONObject(datos);

			rut_cliente = datos_json.getJSONObject("empresa").getString("rut");

			elemento_sist_principal.setElm_nombre(elm_nombre);
			elemento_sist_principal.setRut_cliente(rut_cliente);
			elemento_sist_principal.setTel_codigo(tel_codigo);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
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

	public static void validateExisteCliente(JSONObject user_data, String rut_empresa, Logger log) {
		try {
			Clientes cliente = TvdUtils.createClienteFromJSON(user_data.getJSONObject("empresa"));

			DatosEmpresa empresa = TvdUtils.createDatosEmpresaFromJSON(cliente,
					user_data.getJSONObject("representanteLegal"));
			if (!OracleDBUtils.existeCliente(rut_empresa)) {
				log.debug("Cliente no existe, agregando...");
				Clientes.insertDataCliente(cliente, empresa, log);
			} else {
				log.debug("Cliente existe, actualizando...");
				Clientes.updateDataCliente(cliente, empresa, log);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void insertDocumentDataToMatriz(String temp_folder, String postulation_code, JSONObject user_data,
			Logger log) throws SQLException, JSONException {

		File temp_technical_folder = new File(temp_folder);
		File[] files_list = temp_technical_folder.listFiles();
		String user_name = user_data.get("nombre").toString();
		String nombre_archivo = "";
		int files_count = files_list.length;

		Long num_ofi_parte = OracleDBUtils.getNumeroOP(user_data.getJSONObject("empresa"));
		Long numero_solicitud = OracleDBUtils.createSolitudConcesiones(num_ofi_parte, user_data.getJSONObject("empresa"));
//		Long num_ofi_parte = 12L;
//		Long numero_solicitud = 12L;
		int count_doc = 0;
		
		if (numero_solicitud != 0L) {
			for (int ix = 0; ix < files_count; ix++) {
				nombre_archivo = files_list[ix].getName();
				String unextension_file_name = nombre_archivo.split("\\.")[0];
				String stdo_codigo = CarpetaTecnicaFiles.getSTDOCod(unextension_file_name);

				if (nombre_archivo.contains("ZonaServicio_PTx0")) {
					Elemento.insertarDatosSistPrincipal(nombre_archivo, numero_solicitud, postulation_code, user_name, stdo_codigo);
				} else {
					if (!"".equals(stdo_codigo)) {
						Long cod_documento = OracleDBUtils.createBDCDocumento(numero_solicitud, stdo_codigo);
					}
				}
				
				String doc_path = CarpetaTecnicaFiles.uploadFile(files_list[ix], nombre_archivo, num_ofi_parte);
				if (!"".equals(stdo_codigo)) {
					count_doc++;
					OracleDBUtils.createWftDocumento(doc_path, stdo_codigo, numero_solicitud, num_ofi_parte);
				}
			}
			CarpetaTecnica.deleteTempFolder(temp_folder);
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

}
