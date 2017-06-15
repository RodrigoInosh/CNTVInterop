package cl.subtel.interop.cntv.dto;

import javax.activation.DataHandler;

public class DocumentoDTO {

	private DataHandler archivo;
	private String nombreArchivo;
	private String descripcionArchivo;
	private String checksum;
	
	public DataHandler  getArchivo() {
		return archivo;
	}
	public void setArchivo(DataHandler archivo) {
		this.archivo = archivo;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getDescripcionArchivo() {
		return descripcionArchivo;
	}
	public void setDescripcionArchivo(String descripcionArchivo) {
		this.descripcionArchivo = descripcionArchivo;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	
}
