package cl.subtel.interop.cntv.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;

import cl.subtel.interop.cntv.calculotvd.Clientes;
import cl.subtel.interop.cntv.calculotvd.DatosElemento;
import cl.subtel.interop.cntv.calculotvd.DatosEmpresa;
import cl.subtel.interop.cntv.calculotvd.Elemento;
import cl.subtel.interop.cntv.dto.DocumentoDTO;
import cl.subtel.interop.cntv.dto.PostulacionDTO;

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
		// String tipo_elemento = "Planta Transmisora";
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

			if (!"".equals(datos_json.getJSONObject("estudio_principal").getString("latitud"))) {
				estudios[0] = createElementoSistPrincipal(datos_sist_principal, nombre_archivo, "Estudio Principal");
			}

			if (!"".equals(datos_json.getJSONObject("estudio_alternativo").getString("latitud"))) {
				estudios[1] = createElementoSistPrincipal(datos_sist_principal, nombre_archivo, "Estudio Alternativo");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return estudios;
	}

	public static DatosElemento createObjectElementoDatos(Long elemento_id, JSONObject datos_sist_principal) {
		DatosElemento datos_elemento_object = new DatosElemento();
		JSONObject calculos = null;
		JSONObject caract_tecnicas = null;
		JSONObject datos_concurso = null;
		JSONObject est_principal = null;
		JSONObject est_alternativo = null;
		try {
			calculos = new JSONObject(datos_sist_principal.getString("calculos").replace("[", "").replace("]", ""));
			caract_tecnicas = calculos.getJSONObject("form_data").getJSONObject("carac_tecnicas");
			datos_concurso = calculos.getJSONObject("form_general_concurso");
			est_principal = calculos.getJSONObject("form_data").getJSONObject("estudio_principal");
			est_alternativo = calculos.getJSONObject("form_data").getJSONObject("estudio_alternativo");

			String direccion = calculos.get("pDomicilio").toString();
			String localidad = calculos.get("pLocalidad").toString();
			String comuna = calculos.get("pComuna").toString();
			String nombre_tipo_antena = getNombreTipoAntena(caract_tecnicas.get("tipo_antena").toString());
			String tian_cod = OracleDBUtils.getTianCod(nombre_tipo_antena);
			// int tcr_codigo = OracleDBUtils.getTcrCodigo();
			double latitud = Double.parseDouble(calculos.get("latitud").toString());
			double longitud = Double.parseDouble(calculos.get("longitud").toString());
			double polarizacion_perc_horizontal = Double.parseDouble(caract_tecnicas.get("perc_horizontal").toString());
			double polarizacion_perc_vertical = Double.parseDouble(caract_tecnicas.get("perc_vertical").toString());
			String polarizacion_cod = OracleDBUtils.getPolarizacionCod(polarizacion_perc_horizontal,
					polarizacion_perc_vertical);
			String tipo_emision_cod = OracleDBUtils.getTipoEmisionCod(datos_concurso.get("tipo_emision").toString());
			double potencia = Double.parseDouble(calculos.get("pPotencia").toString());
			String ganacia_max = caract_tecnicas.get("ganancia_max").toString();
			String altura_antena_tx = calculos.get("pAlturaAntenaTx").toString();
			String tilt = caract_tecnicas.get("angulo_tilt").toString();
			String tipo_antena_nombre = "";
			String perdidas_cables = calculos.get("pPerdidaCablesConectores").toString();
			String ubicacion_est_principal = getUbicacion(est_principal.get("domicilio").toString(),
					est_principal.get("comuna").toString(), est_principal.get("region").toString());
			String ubicacion_est_alternativo = getUbicacion(est_alternativo.get("domicilio").toString(),
					est_alternativo.get("comuna").toString(), est_alternativo.get("region").toString());
			String ubicacion_planta_tx = getUbicacion(caract_tecnicas.get("domicilioPtx").toString(),
					caract_tecnicas.get("comunaPTx").toString(), caract_tecnicas.get("regionPTx").toString());
			String intensidad_campo = calculos.get("pIntensidad").toString();
			String inicio_obras = datos_concurso.getJSONObject("plazos").get("ini_obras").toString();
			String fin_obras = datos_concurso.getJSONObject("plazos").get("fin_obras").toString();
			String ini_servicio = datos_concurso.getJSONObject("plazos").get("ini_serv").toString();
			String pot_max_tx = calculos.get("pPotencia").toString();
			String cantidad_elem = caract_tecnicas.get("num_elem").toString();
			Long cod_localidad = Long.parseLong(OracleDBUtils
					.getColumnCode("COD_LOCALIDAD", "LOCALIDAD", "LOCALIDAD", localidad.toUpperCase()).toString());
			System.out.println("Loc cod:" + cod_localidad);
			int cod_comuna = Integer.parseInt(OracleDBUtils
					.getColumnCode("COM_COD_COMUNA", "LOCALIDAD", "COD_LOCALIDAD", String.valueOf(cod_localidad))
					.toString());
			System.out.println("comuna cod:" + cod_comuna);
			int cod_region = Integer.parseInt(OracleDBUtils
					.getColumnCode("REG_COD_REGION", "COMUNA", "COD_COMUNA", String.valueOf(cod_comuna)).toString());
			String perdidas_div_potencia = calculos.get("pDivisorPotencia").toString();
			String ganancia_plano_horizontal = calculos.get("pGanancia").toString();
			double frecuencia = Double.parseDouble(calculos.get("pFrecuencia").toString());
			double otras_perdidas = Double.parseDouble(calculos.get("pOtrasPerdidas").toString());
			int latitud_grados = Integer.parseInt(calculos.get("pLatitudDegress").toString());
			int latitud_minutos = Integer.parseInt(calculos.get("pLatitudMinutes").toString());
			int latitud_segundos = Integer.parseInt(calculos.get("pLatitudSeconds").toString());
			int longitud_grados = Integer.parseInt(calculos.get("pLongitudDegress").toString());
			int longitud_minutos = Integer.parseInt(calculos.get("pLongitudMinutes").toString());
			int longitud_segundos = Integer.parseInt(calculos.get("pLongitudSeconds").toString());

			datos_elemento_object.setElm_codigo(elemento_id);
			datos_elemento_object.setDte_direccion(direccion);
			datos_elemento_object.setPunto_tx(direccion);
			datos_elemento_object.setLocalidad_nombre(localidad);
			datos_elemento_object.setComuna_nombre(comuna);
			// datos_elemento_object.setTcr_codigo(tcr_codigo);
			datos_elemento_object.setTian_cod(tian_cod);
			datos_elemento_object.setLatitud_calculada(latitud);
			datos_elemento_object.setLatitud_WGS84(latitud);
			datos_elemento_object.setLongitud_calculada(longitud);
			datos_elemento_object.setLongitud_WGS84(longitud);
			datos_elemento_object.setPolarizacion(polarizacion_cod);
			datos_elemento_object.setTipo_emision(tipo_emision_cod);
			datos_elemento_object.setPotencia(potencia);
			datos_elemento_object.setGanancia(ganacia_max);
			datos_elemento_object.setAltura_antena_TX(altura_antena_tx);
			datos_elemento_object.setTilt(tilt);
			datos_elemento_object.setPerdidas_cable(perdidas_cables);
			datos_elemento_object.setUbicacion_estudio_principal(ubicacion_est_principal);
			datos_elemento_object.setUbicacion_estudio_alternativo(ubicacion_est_alternativo);
			datos_elemento_object.setUbicacion_planta_transmisora(ubicacion_planta_tx);
			datos_elemento_object.setIntensidad_campo(intensidad_campo);
			datos_elemento_object.setFecha_inicio(inicio_obras);
			datos_elemento_object.setFecha_termino(fin_obras);
			datos_elemento_object.setInicio_servicio(ini_servicio);
			datos_elemento_object.setPotencia_max_tx(pot_max_tx);
			datos_elemento_object.setCantidad_elementos(cantidad_elem);
			datos_elemento_object.setCod_localidad(cod_localidad);
			datos_elemento_object.setCod_comuna(cod_comuna);
			datos_elemento_object.setCod_region(cod_region);
			datos_elemento_object.setPerdidas_div_potencia(perdidas_div_potencia);
			datos_elemento_object.setGanancia_horizontal(ganancia_plano_horizontal);
			datos_elemento_object.setFrecuencia(frecuencia);
			datos_elemento_object.setOtras_perdidas(otras_perdidas);
			datos_elemento_object.setDte_latitud_sur_gr(latitud_grados);
			datos_elemento_object.setDte_latitud_sur_min(latitud_minutos);
			datos_elemento_object.setDte_latitud_sur_sg(latitud_segundos);
			datos_elemento_object.setDte_longitud_oeste_gr(longitud_grados);
			datos_elemento_object.setDte_longitud_oeste_min(longitud_minutos);
			datos_elemento_object.setDte_longitud_oeste_sg(longitud_segundos);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return datos_elemento_object;
	}

	public static String getNombreTipoAntena(String tipo_antena) {
		String nombre_tipo_antena = "";

		switch (tipo_antena) {
		case "ranura":
			nombre_tipo_antena = "RANURA";
			break;
		case "princ":
			nombre_tipo_antena = "PANEL/DIRECCIONAL";
			break;
		case "supert":
			nombre_tipo_antena = "SUPERTURSTILE";
			break;
		case "yagi":
			nombre_tipo_antena = "YAGI";
			break;
		case "logP":
			nombre_tipo_antena = "LOG PERIODICA";
			break;
		case "otro":
			nombre_tipo_antena = "OTRO";
			break;
		default:
			nombre_tipo_antena = "OTRO";
			break;
		}

		return nombre_tipo_antena;
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

	public static String getUbicacion(String domicilio, String comuna, String region) {
		String ubicacion = "";

		if (!"".equals(domicilio)) {
			ubicacion += domicilio;
		}

		if (!"".equals(comuna)) {
			ubicacion += " - " + comuna;
		}

		if (!"".equals(region)) {
			ubicacion += " - " + region;
		}

		return ubicacion;
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
				System.out.println("Cliente no existe, agregando...");
				log.debug("Cliente no existe, agregando...");
				Clientes.insertDataCliente(cliente, empresa, log);
			} else {
				System.out.println("Cliente existe, actualizando...");
				log.debug("Cliente existe, actualizando...");
				Clientes.updateDataCliente(cliente, empresa, log);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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

		Long numero_solicitud = OracleDBUtils.createSolitudConcesiones(user_data.getJSONObject("empresa"));
		Long doc_codigo = OracleDBUtils.createBDCDocumento(numero_solicitud);
		if (numero_solicitud != 0L) {
			for (int ix = 0; ix < files_count; ix++) {
				nombre_archivo = files_list[ix].getName();
				if (nombre_archivo.contains("ZonaServicio_PTx0")) {
					log.debug("Zona Servicio");
					Elemento.insertarDatosSistPrincipal(nombre_archivo, postulation_code, user_name);
				} else {
					log.debug("Anexo");
				}
			}
		}
//		for (int ix = 0; ix < files_count; ix++) {
//			nombre_archivo = files_list[ix].getName();
//			if (nombre_archivo.contains("ZonaServicio_PTx0")) {
//				log.debug("Zona Servicio");
//				Elemento.insertarDatosSistPrincipal(nombre_archivo, postulation_code, user_name);
//			} else {
//				log.debug("Anexo");
//			}
//		}
	}
}
