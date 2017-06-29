package cl.subtel.interop.cntv.calculotvd;

import java.sql.SQLException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cl.subtel.interop.cntv.util.MongoDBUtils;
import cl.subtel.interop.cntv.util.OracleDBUtils;
import cl.subtel.interop.cntv.util.TvdUtils;

public class Elemento {

	public final static String TIPO_SERVICIO = "STD";
	public final static int TEC_CODIGO = 56; // Asociado a la Tecnología
	private String elm_nombre;
	private String tipo_elemento;
	private Long numero_solicitud;
	private int rut_cliente; // Sin puntos ni guión
	private int tel_codigo; // Asociado al Tipo Elemento

	public Elemento() {
		super();
	}

	public Elemento(String elm_nombre, String tipo_elemento, int rut_cliente, int tel_codigo) {
		super();
		this.elm_nombre = elm_nombre;
		this.tipo_elemento = tipo_elemento;
		this.rut_cliente = rut_cliente;
		this.tel_codigo = tel_codigo;
	}

	public Long getNumero_solicitud() {
		return numero_solicitud;
	}

	public void setNumero_solicitud(Long numero_solicitud) {
		this.numero_solicitud = numero_solicitud;
	}

	public void setRut_cliente(int rut_cliente) {
		this.rut_cliente = rut_cliente;
	}

	public String getElm_nombre() {
		return elm_nombre;
	}

	public void setElm_nombre(String elm_nombre) {
		this.elm_nombre = elm_nombre;
	}

	public String getTipo_elemento() {
		return tipo_elemento;
	}

	public void setTipo_elemento(String tipo_elemento) {
		this.tipo_elemento = tipo_elemento;
	}

	public int getRut_cliente() {
		return rut_cliente;
	}

	public void setRut_cliente(String rut_cliente_string) {
		String rut_cliente[] = rut_cliente_string.replace("\\.", "").split("-");
		this.rut_cliente = Integer.parseInt(rut_cliente[0]);
	}

	public int getTel_codigo() {
		return tel_codigo;
	}

	public void setTel_codigo(int tel_codigo) {
		this.tel_codigo = tel_codigo;
	}

	public static String getTipoServicio() {
		return TIPO_SERVICIO;
	}

	public static int getTecCodigo() {
		return TEC_CODIGO;
	}
	
	public static void insertarDatosSistPrincipal(String nombre_archivo, Long numero_solicitud, String codigo_postulacion, String user_name, String stdo_codigo) throws SQLException, JSONException {
		
		JSONObject datos_sist_principal = MongoDBUtils.getDatosTecnicosConcurso(nombre_archivo, codigo_postulacion, user_name);

		Elemento elemento_principal = TvdUtils.createElementoSistPrincipal(datos_sist_principal, nombre_archivo, "Planta Transmisora");
		Elemento estudios[] = TvdUtils.createElementosEstudios(datos_sist_principal, nombre_archivo);

		OracleDBUtils.insertElemento(elemento_principal, datos_sist_principal, true, numero_solicitud, stdo_codigo);	
		int idx_loop = 0;
		int cant_elementos_estudios = estudios.length;
		while(idx_loop < cant_elementos_estudios) {
			if(estudios[idx_loop] != null) {
				OracleDBUtils.insertElemento(estudios[idx_loop], datos_sist_principal, false, numero_solicitud, "");
			}
			idx_loop++;
		}
	}
}
