
package cl.subtel.interop.cntv.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.7.18
 * Thu May 11 16:17:13 GMT-03:00 2017
 * Generated source version: 2.7.18
 */

@XmlRootElement(name = "recibirCarpetaTecnica", namespace = "http://cntv.interop.subtel.cl/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recibirCarpetaTecnica", namespace = "http://cntv.interop.subtel.cl/")

public class RecibirCarpetaTecnica {

    @XmlElement(name = "carpeta")
    private cl.subtel.interop.cntv.dto.PostulacionDTO arg0;

    public cl.subtel.interop.cntv.dto.PostulacionDTO getArg0() {
        return this.arg0;
    }

    public void setArg0(cl.subtel.interop.cntv.dto.PostulacionDTO newArg0)  {
        this.arg0 = newArg0;
    }

}

