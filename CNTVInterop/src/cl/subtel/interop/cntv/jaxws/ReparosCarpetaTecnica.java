
package cl.subtel.interop.cntv.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.7.18
 * Wed Oct 11 16:16:27 CLST 2017
 * Generated source version: 2.7.18
 */

@XmlRootElement(name = "reparosCarpetaTecnica", namespace = "http://cntv.interop.subtel.cl/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reparosCarpetaTecnica", namespace = "http://cntv.interop.subtel.cl/")

public class ReparosCarpetaTecnica {

    @XmlElement(name = "arg0")
    private cl.subtel.interop.cntv.dto.PostulacionDTO arg0;

    public cl.subtel.interop.cntv.dto.PostulacionDTO getArg0() {
        return this.arg0;
    }

    public void setArg0(cl.subtel.interop.cntv.dto.PostulacionDTO newArg0)  {
        this.arg0 = newArg0;
    }

}
