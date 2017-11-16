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
import cl.subtel.interop.cntv.util.DBOracleDAO;
import cl.subtel.interop.cntv.util.Mail;
import cl.subtel.interop.cntv.util.TvdUtils;

@WebService(targetNamespace = "http://cntv.interop.subtel.cl/", portName = "ReparosPort", serviceName = "ReparosService")
public class Reparos {

	private static final Logger log = LogManager.getLogger();

	@WebMethod(operationName = "reparosCarpetaTecnica", action = "urn:ReparosCarpetaTecnica")
	@WebResult(name = "return")
	public RespuestaDTO reparosCarpetaTecnica(@WebParam(name = "arg0") PostulacionDTO postulacion) {
		log.info("** reparosCarpetaTecnica ha sido invocado **");

		RespuestaDTO respuesta = new RespuestaDTO();
		String userID = postulacion.getUserId();
		String codigoPostulacion = postulacion.getCodigoPostulacion();
		JSONObject userData = new JSONObject();
		Long numSolicitud = 0L;
		Long numOfiParte = 0L;

		try {
			userData = DBOracleDAO.getUserData(userID);
			numSolicitud = DBOracleDAO.getSolicitudByPostulationCode(codigoPostulacion);
			System.out.println(numSolicitud);
			numOfiParte = DBOracleDAO.getNumeroOP(userData.getJSONObject("empresa"));
			System.out.println(numOfiParte);

			if (numSolicitud != 0L && numOfiParte != 0L) {
				respuesta = TvdUtils.getDataCarpetaTecnica(postulacion, userID, numSolicitud, numOfiParte,
						codigoPostulacion, userData);
			} else {
				respuesta.setCodigo("NOK");
				respuesta.setMensaje("Error obteniendo numero de solicitud y oficina de partes.");
			}
		} catch (NullPointerException err) {
			log.error("reparos Error:" + err.getMessage());
			err.printStackTrace();
		} catch (JSONException e) {
			log.error("Reparos Error:" + e.getMessage());
			e.printStackTrace();
		}
		
		log.info("** FIN ReparosCarpetaTecnica **");
		
		String message_body = "";
		message_body += "Codigo: " + respuesta.getCodigo() + "\\n";
		message_body += "Mensaje: " + respuesta.getMensaje();
		
		Mail.sendMail("Reparos", message_body);

		return respuesta;
	}
}
