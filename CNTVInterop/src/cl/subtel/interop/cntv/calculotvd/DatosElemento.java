package cl.subtel.interop.cntv.calculotvd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cl.subtel.interop.cntv.util.DBOracleDAO;

public class DatosElemento {
	private static final Logger log = LogManager.getLogger();

	private Long elm_codigo;
	private final static String DTE_MOVIMIENTO = "A"; // A: Agrega, E: Elimina,
														// M: Modifica
	private final static int DTE_VIGENCIA_ACTUAL = 1;
	private final static String UNIDAD_POTENCIA = "3"; // W
	private final static String UNIDAD_GANANCIA = "1"; // dBd
	private final static int TCR_CODIGO = 2; // Id: 2 => WGS84
	private final static int DIGITADO = 0;// Digitado por personal externo = 1,
											// sino es 0
	private final static String ACTIVIDAD_ECONOMICA = "TVE";
	private final static String VALIDACION = "FA";
	private final static String ESTADO_ELEM = "0";
	private final static String BANDA = "UHF";
	private final static int DTE_ESTADO = 1;
	private final static int UNIDAD_PERDIDAS = 16;// unidad perdidas cables,
	// unidad perdidas
	// conectores
	private final static String datum = "WGS84";
	private final static int cod_unid_frecuencia = 6;// MHz

	private String dte_direccion;
	private String comuna_nombre;
	private Long cod_comuna;
	private String localidad_nombre;
	private Long cod_localidad;
	private String tian_cod;
	private double latitud_calculada;
	private double longitud_calculada;
	private double latitud_WGS84;
	private double longitud_WGS84;
	private String polarizacion;
	private String tipo_emision;
	private double potencia;
	private String ganancia; // Ganancia Máxima
	private String punto_tx; // Se rellena con la dirección del sistema
								// radiante.
	private String altura_antena_TX;
	private String tilt;
	private String tipo_antena_nombre; // Si Perd Lobulos > 3dB ? Direccional :
										// omni
	private String perdidas_cable;
	private String perdidas_conectores;
	private String ubicacion_estudio_principal;
	private String ubicacion_estudio_alternativo;
	private String ubicacion_planta_transmisora;
	private String intensidad_campo;
	private String inicio_servicio;
	private String fecha_inicio; // Inicio de obras
	private String fecha_termino; // termino de obras
	private String potencia_max;
	private String potencia_max_tx;
	private String cantidad_elementos;
	private Long cod_region;
	private String perdidas_div_potencia;
	private String ganancia_horizontal;
	private double frecuencia;
	private double dte_latitud_sur_gr;
	private double dte_latitud_sur_min;
	private double dte_latitud_sur_sg;
	private double dte_longitud_oeste_gr;
	private double dte_longitud_oeste_min;
	private double dte_longitud_oeste_sg;
	private double otras_perdidas;
	private String zona_servicio;
	private String tipo_radiacion;

	public String getTipo_radiacion() {
		return tipo_radiacion;
	}

	public void setTipo_radiacion(String tipo_radiacion) {
		this.tipo_radiacion = tipo_radiacion;
	}

	public String getTipo_emision() {
		return tipo_emision;
	}

	public String getZona_servicio() {
		return zona_servicio;
	}

	public void setZona_servicio(String zona_servicio) {
		this.zona_servicio = zona_servicio;
	}

	public static int getCodUnidFrecuencia() {
		return cod_unid_frecuencia;
	}

	public void setTipo_antena_nombre(String tipo_antena_nombre) {
		this.tipo_antena_nombre = tipo_antena_nombre;
	}

	public void setTipo_emision(String tipo_emision) {
		this.tipo_emision = tipo_emision;
	}

	public Long getElm_codigo() {
		return elm_codigo;
	}

	public void setElm_codigo(Long elm_codigo) {
		this.elm_codigo = elm_codigo;
	}

	public String getDte_direccion() {
		return dte_direccion;
	}

	public void setDte_direccion(String dte_direccion) {
		this.dte_direccion = dte_direccion;
	}

	public String getComuna_nombre() {
		return comuna_nombre;
	}

	public void setComuna_nombre(String comuna_nombre) {
		this.comuna_nombre = comuna_nombre;
	}

	public Long getCod_comuna() {
		return cod_comuna;
	}

	public void setCod_comuna(Long cod_comuna) {
		this.cod_comuna = cod_comuna;
	}

	public String getLocalidad_nombre() {
		return localidad_nombre;
	}

	public void setLocalidad_nombre(String localidad_nombre) {
		this.localidad_nombre = localidad_nombre;
	}

	public static String getDatum() {
		return datum;
	}

	public Long getCod_localidad() {
		return cod_localidad;
	}

	public void setCod_localidad(Long cod_localidad) {
		this.cod_localidad = cod_localidad;
	}

	public String getTian_cod() {
		return tian_cod;
	}

	public void setTian_cod(String tian_cod) {
		this.tian_cod = tian_cod;
	}

	public static int getCod_unid_frecuencia() {
		return cod_unid_frecuencia;
	}

	public double getLatitud_calculada() {
		return latitud_calculada;
	}

	public void setLatitud_calculada(double latitud_calculada) {
		this.latitud_calculada = latitud_calculada;
	}

	public double getLongitud_calculada() {
		return longitud_calculada;
	}

	public void setLongitud_calculada(double longitud_calculada) {
		this.longitud_calculada = longitud_calculada;
	}

	public double getLatitud_WGS84() {
		return latitud_WGS84;
	}

	public void setLatitud_WGS84(double latitud_WGS84) {
		this.latitud_WGS84 = latitud_WGS84;
	}

	public double getLongitud_WGS84() {
		return longitud_WGS84;
	}

	public void setLongitud_WGS84(double longitud_WGS84) {
		this.longitud_WGS84 = longitud_WGS84;
	}

	public String getPolarizacion() {
		return polarizacion;
	}

	public void setPolarizacion(String polarizacion) {
		this.polarizacion = polarizacion;
	}

	public double getPotencia() {
		return potencia;
	}

	public void setPotencia(double potencia) {
		this.potencia = potencia;
	}

	public String getGanancia() {
		return ganancia;
	}

	public void setGanancia(String ganancia) {
		this.ganancia = ganancia;
	}

	public String getPunto_tx() {
		return punto_tx;
	}

	public void setPunto_tx(String punto_tx) {
		this.punto_tx = punto_tx;
	}

	public String getAltura_antena_TX() {
		return altura_antena_TX;
	}

	public void setAltura_antena_TX(String altura_antena_TX) {
		this.altura_antena_TX = altura_antena_TX;
	}

	public String getTilt() {
		return tilt;
	}

	public void setTilt(String tilt) {
		this.tilt = tilt;
	}

	public String getTipo_antena_nombre() {
		return tipo_antena_nombre;
	}

	public String getPerdidas_cable() {
		return perdidas_cable.replace(".", ",");
	}

	public void setPerdidas_cable(String perdidas_cable) {
		this.perdidas_cable = perdidas_cable;
	}

	public String getPerdidas_conectores() {
		return perdidas_conectores;
	}

	public void setPerdidas_conectores(String perdidas_conectores) {
		this.perdidas_conectores = perdidas_conectores;
	}

	public String getUbicacion_estudio_principal() {
		return ubicacion_estudio_principal;
	}

	public void setUbicacion_estudio_principal(String ubicacion_estudio_principal) {
		this.ubicacion_estudio_principal = ubicacion_estudio_principal;
	}

	public String getUbicacion_estudio_alternativo() {
		return ubicacion_estudio_alternativo;
	}

	public void setUbicacion_estudio_alternativo(String ubicacion_estudio_alternativo) {
		this.ubicacion_estudio_alternativo = ubicacion_estudio_alternativo;
	}

	public String getUbicacion_planta_transmisora() {
		return ubicacion_planta_transmisora;
	}

	public void setUbicacion_planta_transmisora(String ubicacion_planta_transmisora) {
		this.ubicacion_planta_transmisora = ubicacion_planta_transmisora;
	}

	public String getIntensidad_campo() {
		return intensidad_campo;
	}

	public void setIntensidad_campo(String intensidad_campo) {
		this.intensidad_campo = intensidad_campo;
	}

	public String getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public String getFecha_termino() {
		return fecha_termino;
	}

	public void setFecha_termino(String fecha_termino) {
		this.fecha_termino = fecha_termino;
	}

	public String getPotencia_max() {
		return potencia_max;
	}

	public void setPotencia_max(String potencia_max) {
		this.potencia_max = potencia_max;
	}

	public String getPotencia_max_tx() {
		return potencia_max_tx;
	}

	public void setPotencia_max_tx(String potencia_max_tx) {
		this.potencia_max_tx = potencia_max_tx;
	}

	public String getCantidad_elementos() {
		return cantidad_elementos;
	}

	public void setCantidad_elementos(String cantidad_elementos) {
		this.cantidad_elementos = cantidad_elementos;
	}

	public Long getCod_region() {
		return cod_region;
	}

	public void setCod_region(Long cod_region) {
		this.cod_region = cod_region;
	}

	public String getPerdidas_div_potencia() {
		return perdidas_div_potencia.replace(".", ",");
	}

	public void setPerdidas_div_potencia(String perdidas_div_potencia) {
		this.perdidas_div_potencia = perdidas_div_potencia;
	}

	public String getGanancia_horizontal() {
		return ganancia_horizontal;
	}

	public void setGanancia_horizontal(String ganancia_horizontal) {
		this.ganancia_horizontal = ganancia_horizontal;
	}

	public double getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(double frecuencia) {
		this.frecuencia = frecuencia;
	}

	public double getDte_latitud_sur_gr() {
		return dte_latitud_sur_gr;
	}

	public void setDte_latitud_sur_gr(double dte_latitud_sur_gr) {
		this.dte_latitud_sur_gr = dte_latitud_sur_gr;
	}

	public double getDte_latitud_sur_min() {
		return dte_latitud_sur_min;
	}

	public void setDte_latitud_sur_min(double dte_latitud_sur_min) {
		this.dte_latitud_sur_min = dte_latitud_sur_min;
	}

	public double getDte_latitud_sur_sg() {
		return dte_latitud_sur_sg;
	}

	public void setDte_latitud_sur_sg(double dte_latitud_sur_sg) {
		this.dte_latitud_sur_sg = dte_latitud_sur_sg;
	}

	public double getDte_longitud_oeste_gr() {
		return dte_longitud_oeste_gr;
	}

	public void setDte_longitud_oeste_gr(double dte_longitud_oeste_gr) {
		this.dte_longitud_oeste_gr = dte_longitud_oeste_gr;
	}

	public double getDte_longitud_oeste_min() {
		return dte_longitud_oeste_min;
	}

	public void setDte_longitud_oeste_min(double dte_longitud_oeste_min) {
		this.dte_longitud_oeste_min = dte_longitud_oeste_min;
	}

	public double getDte_longitud_oeste_sg() {
		return dte_longitud_oeste_sg;
	}

	public void setDte_longitud_oeste_sg(double dte_longitud_oeste_sg) {
		this.dte_longitud_oeste_sg = dte_longitud_oeste_sg;
	}

	public double getOtras_perdidas() {
		return otras_perdidas;
	}

	public void setOtras_perdidas(double otras_perdidas) {
		this.otras_perdidas = otras_perdidas;
	}

	public static String getDteMovimiento() {
		return DTE_MOVIMIENTO;
	}

	public static int getDteVigenciaActual() {
		return DTE_VIGENCIA_ACTUAL;
	}

	public static String getUnidadPotencia() {
		return UNIDAD_POTENCIA;
	}

	public static String getUnidadGanancia() {
		return UNIDAD_GANANCIA;
	}

	public static int getTcrCodigo() {
		return TCR_CODIGO;
	}

	public static int getDigitado() {
		return DIGITADO;
	}

	public static String getActividadEconomica() {
		return ACTIVIDAD_ECONOMICA;
	}

	public static String getValidacion() {
		return VALIDACION;
	}

	public static String getEstadoElem() {
		return ESTADO_ELEM;
	}

	public static String getBanda() {
		return BANDA;
	}

	public static int getDteEstado() {
		return DTE_ESTADO;
	}

	public static int getUnidadPerdidas() {
		return UNIDAD_PERDIDAS;
	}

	public String getInicio_servicio() {
		return inicio_servicio;
	}

	public void setInicio_servicio(String inicio_servicio) {
		this.inicio_servicio = inicio_servicio;
	}

	public static DatosElemento createObjectElementoDatos(Long elemento_id, JSONObject datos_sist_principal) {
		log.debug("createObjectElementoDatos()");
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
			String comuna = caract_tecnicas.get("comunaNamePTx").toString();
			String nombre_tipo_antena = getNombreTipoAntena(caract_tecnicas.get("tipo_antena").toString());
			String tipo_radiacion = getTipoRadiacion(calculos);
			String tian_cod = DBOracleDAO.getTianCod(nombre_tipo_antena);
			double latitud = getDoubleValue(calculos, "latitud");
			double longitud = getDoubleValue(calculos, "longitud");
			double polarizacion_perc_horizontal = getDoubleValue(caract_tecnicas, "perc_horizontal");
			double polarizacion_perc_vertical = getDoubleValue(caract_tecnicas, "perc_vertical");
			String polarizacion_cod = DBOracleDAO.getPolarizacionCod(polarizacion_perc_horizontal,
					polarizacion_perc_vertical);
			String tipo_emision_cod = DBOracleDAO.getTipoEmisionCod(datos_concurso.get("tipo_emision").toString());
			double potencia = getDoubleValue(calculos, "pPotencia");
			String ganacia_max = caract_tecnicas.get("ganancia_max").toString();
			String altura_antena_tx = calculos.get("pAlturaAntenaTx").toString();
			String tilt = caract_tecnicas.get("angulo_tilt").toString();
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

			Long cod_comuna = getLongValue(caract_tecnicas.get("comunaPTx").toString());// OracleDBUtils.getCodigoComuna(comuna);
			Long cod_region = getLongValue(caract_tecnicas.get("regionPTx").toString());// OracleDBUtils.getCodigoRegion(cod_comuna);
			Long cod_localidad = DBOracleDAO.getCodigoLocalidad(cod_comuna);

			String perdidas_div_potencia = calculos.get("pDivisorPotencia").toString();
			String ganancia_plano_horizontal = calculos.get("pGanancia").toString();
			double frecuencia = getDoubleValue(calculos, "pFrecuencia");
			double otras_perdidas = getDoubleValue(calculos, "pOtrasPerdidas");
			double latitud_grados = getDoubleValue(calculos, "pLatitudDegress");
			double latitud_minutos = getDoubleValue(calculos, "pLatitudMinutes");
			double latitud_segundos = getDoubleValue(calculos, "pLatitudSeconds");
			double longitud_grados = getDoubleValue(calculos, "pLongitudDegress");
			double longitud_minutos = getDoubleValue(calculos, "pLongitudMinutes");
			double longitud_segundos = getDoubleValue(calculos, "pLongitudSeconds");
			String zona_servicio = datos_sist_principal.get("identificador").toString();

			datos_elemento_object.setElm_codigo(elemento_id);
			datos_elemento_object.setDte_direccion(direccion);
			datos_elemento_object.setPunto_tx(direccion);
			datos_elemento_object.setLocalidad_nombre(localidad);
			datos_elemento_object.setComuna_nombre(comuna);
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
			datos_elemento_object.setZona_servicio(zona_servicio);
			datos_elemento_object.setTipo_radiacion(tipo_radiacion);
			datos_elemento_object.setTipo_antena_nombre(nombre_tipo_antena);

		} catch (JSONException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return datos_elemento_object;
	}

	public static Long getLongValue(String value) {
		Long cast_value = 0L;
		try {
			cast_value = Long.parseLong(value);
		} catch (NumberFormatException e) {
			// e.printStackTrace();
		}

		return cast_value;
	}

	public static double getDoubleValue(JSONObject object, String name_field) {
		double value = 0;
		try {
			value = Double.parseDouble(object.get(name_field).toString());
		} catch (NumberFormatException e) {
			// e.printStackTrace();
		} catch (JSONException e) {
			// e.printStackTrace();
		}

		return value;
	}

	public static String getNombreTipoAntena(String tipo_antena) {
		String nombre_tipo_antena = "";

		// switch (tipo_antena) {
		// case "ranura":
		// nombre_tipo_antena = "RANURA";
		// break;
		// case "princ":
		// nombre_tipo_antena = "PANEL/DIRECCIONAL";
		// break;
		// case "supert":
		// nombre_tipo_antena = "SUPERTURSTILE";
		// break;
		// case "yagi":
		// nombre_tipo_antena = "YAGI";
		// break;
		// case "logP":
		// nombre_tipo_antena = "LOG PERIODICA";
		// break;
		// case "otro":
		// nombre_tipo_antena = "OTRO";
		// break;
		// default:
		// nombre_tipo_antena = "";
		// break;
		// }
		if (tipo_antena.equals("ranura"))
			nombre_tipo_antena = "RANURA";
		else if (tipo_antena.equals("princ"))
			nombre_tipo_antena = "PANEL/DIRECCIONAL";
		else if (tipo_antena.equals("supert"))
			nombre_tipo_antena = "SUPERTURSTILE";
		else if (tipo_antena.equals("yagi"))
			nombre_tipo_antena = "YAGI";
		else if (tipo_antena.equals("logP"))
			nombre_tipo_antena = "LOG PERIODICA";
		else if (tipo_antena.equals("otro"))
			nombre_tipo_antena = "OTRO";
		else
			nombre_tipo_antena = "";

		return nombre_tipo_antena;
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

	public String validateData() {
		log.debug("validateData");
		String message_response = "";

		if ("".equals(comuna_nombre)) {
			message_response = "Comuna de Sistema Principal vacía";
		} else if (cod_comuna == 0) {
			message_response = "Comuna de Sistema Principal incorrecta (revise tildes)";
		} else if ("".equals(localidad_nombre)) {
			message_response = "Localidad de Sistema Principal vacía";
		} else if (cod_localidad == 0) {
			message_response = "Localidad de Sistema Principal incorrecta (revise tildes)";
		} else if ("".equals(tian_cod)) {
			message_response = "Tipo de Antena inválido";
		}

		return message_response;
	}

	public static String getTipoRadiacion(JSONObject calculos) {
		String tipo_radiacion = "";
		double min_perdida_lobulo = Integer.MAX_VALUE;
		double max_perdida_lobulo = 0;

		try {
			int radiales = Integer.parseInt(calculos.get("radiales").toString());
			int grados = 360 / radiales;

			double perd_lobulo_actual;

			for (int ix = 0; ix < radiales; ix++) {
				perd_lobulo_actual = Double.parseDouble(calculos.get("M" + radiales + "PL" + (grados * ix)).toString());
				min_perdida_lobulo = getMinValue(min_perdida_lobulo, perd_lobulo_actual);
				max_perdida_lobulo = getMaxValue(max_perdida_lobulo, perd_lobulo_actual);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		double diferencia = max_perdida_lobulo - min_perdida_lobulo;
		tipo_radiacion = getNameTipoRadiacion(diferencia);

		return tipo_radiacion;
	}

	private static double getMinValue(double min_perdida_lobulo, double perd_lobulo_actual) {
		double minimo = 0;

		if (min_perdida_lobulo > perd_lobulo_actual) {
			minimo = perd_lobulo_actual;
		} else {
			minimo = min_perdida_lobulo;
		}

		return minimo;
	}

	private static double getMaxValue(double max_perdida_lobulo, double perd_lobulo_actual) {
		double minimo = 0;

		if (max_perdida_lobulo < perd_lobulo_actual) {
			minimo = perd_lobulo_actual;
		} else {
			minimo = max_perdida_lobulo;
		}

		return minimo;
	}

	private static String getNameTipoRadiacion(double diferencia) {
		String tipo_radiacion = "";

		if (diferencia > 3) {
			tipo_radiacion = "DIR";
		} else {
			tipo_radiacion = "OMN";
		}

		return tipo_radiacion;
	}
}
