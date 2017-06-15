package cl.subtel.interop.cntv.dto;

public class PaginaCalculoDTO {

	private String token;
	private int usuario_id;
	private String codigo_postulacion;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(int usuario_id) {
		this.usuario_id = usuario_id;
	}
	public String getCodigo_postulacion() {
		return codigo_postulacion;
	}
	public void setCodigo_postulacion(String codigo_postulacion) {
		this.codigo_postulacion = codigo_postulacion;
	}
}
