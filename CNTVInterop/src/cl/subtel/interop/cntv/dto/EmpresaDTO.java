package cl.subtel.interop.cntv.dto;

public class EmpresaDTO {
	
	private String  rut;
	private String  razon_social;
	private String  nombre_fantasia;
	private String  giro;
	private Integer comuna_id;
	private String  direccion;
	
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getRazon_social() {
		return razon_social;
	}
	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}
	public String getNombre_fantasia() {
		return nombre_fantasia;
	}
	public void setNombre_fantasia(String nombre_fantasia) {
		this.nombre_fantasia = nombre_fantasia;
	}
	public String getGiro() {
		return giro;
	}
	public void setGiro(String giro) {
		this.giro = giro;
	}
	public Integer getComuna_id() {
		return comuna_id;
	}
	public void setComuna_id(Integer comuna_id) {
		this.comuna_id = comuna_id;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
}
