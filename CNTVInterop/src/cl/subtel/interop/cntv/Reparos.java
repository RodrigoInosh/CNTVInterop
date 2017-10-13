package cl.subtel.interop.cntv;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import cl.subtel.interop.cntv.dto.PostulacionDTO;
import cl.subtel.interop.cntv.dto.RespuestaDTO;
import cl.subtel.interop.cntv.util.OracleDBUtils;
import cl.subtel.interop.cntv.util.TvdUtils;

@WebService(targetNamespace = "http://cntv.interop.subtel.cl/", portName = "ReparosPort", serviceName = "ReparosService")
public class Reparos {

	private static final Logger log = LogManager.getLogger();

	@WebMethod(operationName = "reparosCarpetaTecnica", action = "urn:ReparosCarpetaTecnica")
	@WebResult(name = "return")
	public RespuestaDTO reparosCarpetaTecnica(@WebParam(name = "arg0") PostulacionDTO postulacion) {
		log.info("** reparosCarpetaTecnica ha sido invocado **");

		String userID = postulacion.getUserId();
		String codigoPostulacion = postulacion.getCodigoPostulacion();
		JSONObject userData = new JSONObject();
		Long numSolicitud = 0L;
		Long numOfiParte = 0L;

		try {
			userData = OracleDBUtils.getUserData(userID);
			numSolicitud = OracleDBUtils.getSolicitudByPostulationCode(codigoPostulacion);
			numOfiParte = OracleDBUtils.getNumeroOP(userData.getJSONObject("empresa"));
		} catch (NullPointerException err) {
			log.error("recibirCarpetaTecnica:" + err.getMessage());
			err.printStackTrace();
		} catch (JSONException e) {
			log.error("recibirCarpetaTecnica:" + e.getMessage());
			e.printStackTrace();
		}

		RespuestaDTO respuesta = TvdUtils.getDataCarpetaTecnica(postulacion, userID, numSolicitud, numOfiParte,
				codigoPostulacion, userData);
		return respuesta;
	}
}
