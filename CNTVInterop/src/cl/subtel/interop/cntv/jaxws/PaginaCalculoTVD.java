package cl.subtel.interop.cntv.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "paginaCalculoTVD", namespace = "http://cntv.interop.subtel.cl/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paginaCalculoTVD", namespace = "http://cntv.interop.subtel.cl/", propOrder = {"arg0"})

public class PaginaCalculoTVD {

	@XmlElement(name = "paginaCalculo")
    private cl.subtel.interop.cntv.dto.PaginaCalculoDTO arg0;

	public cl.subtel.interop.cntv.dto.PaginaCalculoDTO getArg0() {
		return arg0;
	}

	public void setArg0(cl.subtel.interop.cntv.dto.PaginaCalculoDTO arg0) {
		this.arg0 = arg0;
	}
}
