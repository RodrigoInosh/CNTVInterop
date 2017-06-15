package cl.subtel.interop.cntv.dto;

import java.util.List;

public class PostulacionDTO {

	private String userId;	
	private String codigoPostulacion;
	private List<DocumentoDTO> archivos;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<DocumentoDTO> getArchivos() {
		return archivos;
	}
	public void setArchivos(List<DocumentoDTO> archivos) {
		this.archivos = archivos;
	}
	public String getCodigoPostulacion() {
		return codigoPostulacion;
	}
	public void setCodigoPostulacion(String codigoPostulacion) {
		this.codigoPostulacion = codigoPostulacion;
	}	

}