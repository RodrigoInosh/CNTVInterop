package cl.subtel.interop.cntv.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.logging.log4j.Logger;

import cl.subtel.interop.cntv.calculotvd.CarpetaTecnicaFiles;
import cl.subtel.interop.cntv.calculotvd.Clientes;
import cl.subtel.interop.cntv.calculotvd.DatosElemento;
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
			if (!DBOracleDAO.existeCliente(rut_empresa)) {
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

	public static void insertDocumentDataToMatriz(String temp_folder, String codigo_postulacion, JSONObject user_data,
			Logger log) throws SQLException, JSONException {

		System.out.println("insertDocumentDataToMatriz");

		File temp_technical_folder = new File(temp_folder);
		File[] files_list = temp_technical_folder.listFiles();
		int files_count = files_list.length;
		String user_name = user_data.get("nombre").toString();
		String nombre_archivo = "";

		Connection db_connection = DBOracleUtils.connect();
		db_connection.setAutoCommit(false);

		Long num_ofi_parte = DBOracleDAO.getNumeroOP(user_data.getJSONObject("empresa"));
		Long numero_solicitud = DBOracleDAO.createSolitudConcesiones(num_ofi_parte, user_data.getJSONObject("empresa"));

		try {
			if (numero_solicitud != 0L) {
				for (int ix = 0; ix < files_count; ix++) {
					nombre_archivo = files_list[ix].getName();
					String unextension_file_name = nombre_archivo.split("\\.")[0];
					String stdo_codigo = CarpetaTecnicaFiles.getSTDOCod(unextension_file_name);

					if (nombre_archivo.contains("ZonaServicio_PTx0") && nombre_archivo.contains("pdf")) {
						JSONObject datos_sist_principal = MongoDBUtils.getDatosTecnicosConcurso(nombre_archivo,
								codigo_postulacion, user_name);
						Elemento elemento_principal = DBOracleDAO.createElementoSistPrincipal(datos_sist_principal,
								nombre_archivo, "Planta Transmisora");
						Elemento estudios[] = DBOracleDAO.createElementosEstudios(datos_sist_principal, nombre_archivo);

						// Insertar Elemento Sistema Principal en tabla BDC_ELEMENTOS
						Long inserted_elm_codigo = DBOracleDAO.insertElemento(elemento_principal, datos_sist_principal,
								true, numero_solicitud, db_connection);

						// Una vez creado el Elemento se asocia a éste un documento en la tabla
						// BDC_DOCUMENTOS
						Long doc_codigo = DBOracleDAO.insertIntoBDCDocumento(numero_solicitud, stdo_codigo, db_connection);
						DatosElemento elemento_datos = DatosElemento.createObjectElementoDatos(inserted_elm_codigo,
								datos_sist_principal);
						DBOracleDAO.insertDatosElemento(doc_codigo, datos_sist_principal, elemento_datos,
								db_connection);

						// Si el sistema principal tiene más de un estudio (Estudios alternativos) se
						// guarda sin la información sin crear un documento
						int idx_loop = 0;
						int cant_elementos_estudios = estudios.length;
						while (idx_loop < cant_elementos_estudios) {
							if (estudios[idx_loop] != null) {
								DBOracleDAO.insertElemento(estudios[idx_loop], datos_sist_principal, false,
										numero_solicitud, db_connection);
							}
							idx_loop++;
						}
					} else {
						if (!"".equals(stdo_codigo)) {
							Long cod_documento = DBOracleDAO.insertIntoBDCDocumento(numero_solicitud, stdo_codigo, db_connection);
						}
					}

					String doc_path = CarpetaTecnicaFiles.uploadFile(files_list[ix], nombre_archivo, num_ofi_parte);
					System.out.println("Doc path:" + doc_path);
					if (!"".equals(stdo_codigo)) {
						DBOracleDAO.createWftDocumento(doc_path, stdo_codigo, numero_solicitud, num_ofi_parte);
					}
				}

			}
			db_connection.commit();
		} catch (Exception err) {
			db_connection.rollback();
		} finally {
			DBOracleUtils.closeConnection(db_connection);
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
