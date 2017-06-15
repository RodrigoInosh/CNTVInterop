package cl.subtel.interop.cntv.calculotvd;

public class DatosElemento {

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
	private final static int cod_unid_frecuencia = 6;//MHz

	private String dte_direccion;
	private String comuna_nombre;
	private int cod_comuna;
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
	private String fecha_inicio; //Inicio de obras
	private String fecha_termino; //termino de obras
	private String potencia_max;
	private String potencia_max_tx;
	private String cantidad_elementos;
	private int cod_region;
	private String perdidas_div_potencia;
	private String ganancia_horizontal;
	private double frecuencia;
	private int dte_latitud_sur_gr;
	private int dte_latitud_sur_min;
	private int dte_latitud_sur_sg;
	private int dte_longitud_oeste_gr;
	private int dte_longitud_oeste_min;
	private int dte_longitud_oeste_sg;
	private double otras_perdidas;

	public String getTipo_emision() {
		return tipo_emision;
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

	public int getCod_comuna() {
		return cod_comuna;
	}

	public void setCod_comuna(int cod_comuna) {
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

	public void setTipo_antena_nombre(double MAX_PERDIDA_LOBULO) {
		if (MAX_PERDIDA_LOBULO > 3) {
			this.tipo_antena_nombre = "DIRECCIONAL";
		} else {
			this.tipo_antena_nombre = "Omnidireccional";
		}
	}

	public String getPerdidas_cable() {
		return perdidas_cable;
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

	public int getCod_region() {
		return cod_region;
	}

	public void setCod_region(int cod_region) {
		this.cod_region = cod_region;
	}

	public String getPerdidas_div_potencia() {
		return perdidas_div_potencia;
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

	public int getDte_latitud_sur_gr() {
		return dte_latitud_sur_gr;
	}

	public void setDte_latitud_sur_gr(int dte_latitud_sur_gr) {
		this.dte_latitud_sur_gr = dte_latitud_sur_gr;
	}

	public int getDte_latitud_sur_min() {
		return dte_latitud_sur_min;
	}

	public void setDte_latitud_sur_min(int dte_latitud_sur_min) {
		this.dte_latitud_sur_min = dte_latitud_sur_min;
	}

	public int getDte_latitud_sur_sg() {
		return dte_latitud_sur_sg;
	}

	public void setDte_latitud_sur_sg(int dte_latitud_sur_sg) {
		this.dte_latitud_sur_sg = dte_latitud_sur_sg;
	}

	public int getDte_longitud_oeste_gr() {
		return dte_longitud_oeste_gr;
	}

	public void setDte_longitud_oeste_gr(int dte_longitud_oeste_gr) {
		this.dte_longitud_oeste_gr = dte_longitud_oeste_gr;
	}

	public int getDte_longitud_oeste_min() {
		return dte_longitud_oeste_min;
	}

	public void setDte_longitud_oeste_min(int dte_longitud_oeste_min) {
		this.dte_longitud_oeste_min = dte_longitud_oeste_min;
	}

	public int getDte_longitud_oeste_sg() {
		return dte_longitud_oeste_sg;
	}

	public void setDte_longitud_oeste_sg(int dte_longitud_oeste_sg) {
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
	
}
