package cl.subtel.interop.cntv.calculotvd;

public class DatosEmpresa {

	private Long rut_cliente; //Dependencia de Clase Cliente
	private String fono;
	private String fono2;
	private String alias_cliente;
	private String email;
	private String nombre_contacto;
	private String rut_contacto;
	
	public DatosEmpresa(Long rut_cliente, String fono, String fono2, String alias_cliente, String email,
			String nombre_contacto, String rut_contacto) {
		super();
		this.rut_cliente = rut_cliente;
		this.fono = fono;
		this.fono2 = fono2;
		this.alias_cliente = alias_cliente;
		this.email = email;
		this.nombre_contacto = nombre_contacto;
		this.rut_contacto = rut_contacto;
	}
	
	public DatosEmpresa(){
		
	}
	
	public Long getRut_cliente() {
		return rut_cliente;
	}
	public void setRut_cliente(Long rut_cliente) {
		this.rut_cliente = rut_cliente;
	}
	public String getFono() {
		return fono;
	}
	public void setFono(String fono) {
		this.fono = fono;
	}
	public String getFono2() {
		return fono2;
	}
	public void setFono2(String fono2) {
		this.fono2 = fono2;
	}
	public String getAlias_cliente() {
		return alias_cliente;
	}
	public void setAlias_cliente(String alias_cliente) {
		this.alias_cliente = alias_cliente;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombre_contacto() {
		return nombre_contacto;
	}
	public void setNombre_contacto(String nombre_contacto) {
		this.nombre_contacto = nombre_contacto;
	}
	public String getRut_contacto() {
		return rut_contacto;
	}
	public void setRut_contacto(String rut_contacto) {
		this.rut_contacto = rut_contacto;
	}
	
}
