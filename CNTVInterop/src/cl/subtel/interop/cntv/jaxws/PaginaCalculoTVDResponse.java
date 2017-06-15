package cl.subtel.interop.cntv.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "paginaCalculoTVDResponse", namespace = "http://cntv.interop.subtel.cl/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paginaCalculoTVDResponse", namespace = "http://cntv.interop.subtel.cl/")

public class PaginaCalculoTVDResponse {
	
	@XmlElement(name = "return")
	private cl.subtel.interop.cntv.dto.RespuestaDTO _return;

	public cl.subtel.interop.cntv.dto.RespuestaDTO getReturn() {
		return this._return;
	}

	public void setReturn(cl.subtel.interop.cntv.dto.RespuestaDTO new_return) {
		this._return = new_return;
	}
}
