
package cl.subtel.interop.cntv.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.7.18
 * Tue Aug 08 12:42:38 CLT 2017
 * Generated source version: 2.7.18
 */

@XmlRootElement(name = "modificarUsuarioResponse", namespace = "http://cntv.interop.subtel.cl/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modificarUsuarioResponse", namespace = "http://cntv.interop.subtel.cl/")

public class ModificarUsuarioResponse {

    @XmlElement(name = "return")
    private cl.subtel.interop.cntv.dto.RespuestaDTO _return;

    public cl.subtel.interop.cntv.dto.RespuestaDTO getReturn() {
        return this._return;
    }

    public void setReturn(cl.subtel.interop.cntv.dto.RespuestaDTO new_return)  {
        this._return = new_return;
    }

}
