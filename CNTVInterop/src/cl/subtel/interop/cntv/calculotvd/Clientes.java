package cl.subtel.interop.cntv.calculotvd;

public class Clientes {

	private static final int ACTIVIDAD_ECONOMICA = 3;
	private static final String TIPO_PERSONALIDAD = "J"; // Jurídica

	private Long rut_cliente;
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
}
