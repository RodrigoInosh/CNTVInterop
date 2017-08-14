package cl.subtel.interop.cntv;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.List;
import java.util.MissingResourceException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import cl.subtel.interop.cntv.calculotvd.CarpetaTecnica;
import cl.subtel.interop.cntv.calculotvd.DatosElemento;
import cl.subtel.interop.cntv.dto.DocumentoDTO;
import cl.subtel.interop.cntv.dto.EmpresaDTO;
import cl.subtel.interop.cntv.dto.PaginaCalculoDTO;
import cl.subtel.interop.cntv.dto.PostulacionDTO;
import cl.subtel.interop.cntv.dto.RepresentanteLegalDTO;
import cl.subtel.interop.cntv.dto.RespuestaDTO;
import cl.subtel.interop.cntv.dto.UsuarioDTO;
import cl.subtel.interop.cntv.util.FileProperties;
import cl.subtel.interop.cntv.util.MongoDBUtils;
import cl.subtel.interop.cntv.util.TvdUtils;

@WebService(targetNamespace = "http://cntv.interop.subtel.cl/", portName = "ConcursosPort", serviceName = "ConcursosService")
public class Concursos {

	private static final Logger log = LogManager.getLogger();

	@WebMethod(operationName = "agregarUsuario", action = "urn:AgregarUsuario")
	@WebResult(name = "return")
	public RespuestaDTO agregarUsuario(@WebParam(name = "usuario") UsuarioDTO usuario,
			@WebParam(name = "empresa") EmpresaDTO empresa,
			@WebParam(name = "representante") RepresentanteLegalDTO representante) {

		log.info("** AgregarUsuario ha sido invocado **");
		RespuestaDTO respuesta = new RespuestaDTO();

		// Conecta a Mongodb
		MongoClient mongoClient = null;
		boolean agregar = true;
		try {
			DB db = null;
			DBCollection col = null;
			String dbMasPuerto = FileProperties.getParamProperties("basedatoscalculos", "Interoperabilidad");
			if (dbMasPuerto == null || dbMasPuerto.isEmpty()) {
				agregar = false;
			} else {
				// Conecta a la base de datos Mongodb
				String[] stringDb = dbMasPuerto.split(":");
				log.debug("Conectando a mongodb " + dbMasPuerto);
				if (stringDb.length < 2) {
					String str = "La direccion de base de datos es erronea ";
					log.error(str + dbMasPuerto);
					throw new Exception(str);
				}
				mongoClient = new MongoClient(stringDb[0], Integer.parseInt(stringDb[1]));
				String calculosDb = FileProperties.getParamProperties("calculosDb", "Interoperabilidad");
				String usuariosColeccion = FileProperties.getParamProperties("calculosColeccionUsuarios",
						"Interoperabilidad");
				log.debug("DB y Coleccion: " + calculosDb + "/" + usuariosColeccion);
				db = mongoClient.getDB(calculosDb);
				col = db.getCollection(usuariosColeccion);

				// Buscar usuario en mongo
				log.debug("Busca usuario existente: id = " + usuario.getId());
				BasicDBObject query = new BasicDBObject();
				query.append("id", usuario.getId());
				DBCursor rec = col.find(query);
				if (rec.hasNext()) {
					// Usuario ya existe, Error
					String str = "Usuario ya existe ";
					log.error(str + usuario.getId());
					respuesta.setCodigo("ERROR");
					respuesta.setMensaje(str);
					agregar = false;
				}
				rec.close();
			}

			// Si no existe agregarlo
			if (agregar) {
				log.debug("Agregando usuario " + usuario.getId());

				Gson gson = new Gson();
				log.debug("Usuario = " + gson.toJson(usuario));
				log.debug("Empresa = " + gson.toJson(empresa));
				log.debug("Representante = " + gson.toJson(representante));

				BasicDBObject documento = new BasicDBObject();

				documento.put("id", usuario.getId());
				documento.put("nombre", usuario.getNombre());
				documento.put("email", usuario.getEmail());
				documento.put("rut", usuario.getRut());

				BasicDBObject docEmpresa = new BasicDBObject();
				docEmpresa.put("comunaId", empresa.getComuna_id());
				docEmpresa.put("direccion", empresa.getDireccion());
				docEmpresa.put("giro", empresa.getGiro());
				docEmpresa.put("nombreFantasia", empresa.getNombre_fantasia());
				docEmpresa.put("razonSocial", empresa.getRazon_social());
				docEmpresa.put("rut", empresa.getRut());
				documento.put("empresa", docEmpresa);

				BasicDBObject docRepresentante = new BasicDBObject();
				docRepresentante.put("apellidoMaterno", representante.getApellidoMaterno());
				docRepresentante.put("apellidoPaterno", representante.getApellidoPaterno());
				docRepresentante.put("celular", representante.getCelular());
				docRepresentante.put("email", representante.getEmail());
				docRepresentante.put("nombre", representante.getNombre());
				docRepresentante.put("rut", representante.getRut());
				docRepresentante.put("telefonoFijo", representante.getTelefonoFijo());
				docRepresentante.put("telefonoOtro", representante.getTelefonoOtro());
				documento.put("representanteLegal", docRepresentante);

				log.debug("Insertando... ");
				WriteConcern wc = db.getWriteConcern();
				col.insert(documento, wc);
				log.debug("Insertado OK");
				respuesta.setCodigo("OK");
				respuesta.setMensaje("Se recibio el usuario");
			}
		} catch (UnknownHostException e) {
			log.error(e.getMessage());
			respuesta.setCodigo("ERROR");
			respuesta.setMensaje(e.getMessage());
			if (mongoClient != null) {
				mongoClient.close();
			}
			e.printStackTrace();
		} catch (MissingResourceException e) {
			log.error(e.getMessage());
			respuesta.setCodigo("ERROR");
			respuesta.setMensaje(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
			respuesta.setCodigo("ERROR");
			respuesta.setMensaje(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			respuesta.setCodigo("ERROR");
			respuesta.setMensaje(e.getMessage());
		}

		log.info("Respuesta: " + respuesta.getCodigo() + " - " + respuesta.getMensaje());
		log.info("** FIN AgregarUsuario **");
		return respuesta;
	}

	@WebMethod(operationName = "recibirCarpetaTecnica", action = "urn:RecibirCarpetaTecnica")
	@WebResult(name = "return")
	public RespuestaDTO recibirCarpetaTecnica(@WebParam(name = "carpeta") PostulacionDTO postulacion) {
		log.info("** recibirCarpetaTecnica ha sido invocado **");

		boolean correcto = false;
		Gson gson = new Gson();
//		log.debug(gson.toJson(postulacion));

		String temp_folder = "";
		String response_message = "";
		String userID = postulacion.getUserId();
		BasicDBObject query_conditions = new BasicDBObject();
		query_conditions.put("id", Integer.parseInt(userID));

		try {
			JSONObject user_data = MongoDBUtils.getData(query_conditions, "usuariosTVD");

			List<DocumentoDTO> lista = postulacion.getArchivos();
			DocumentoDTO doc = lista.get(0);
			temp_folder = CarpetaTecnica.saveFile(userID, doc);
			
			String rut_empresa = user_data.getJSONObject("empresa").get("rut").toString();
			String nombre_usuario = user_data.get("nombre").toString();
			String response_validate_data = CarpetaTecnica.validateDataTecnica(temp_folder,
					postulacion.getCodigoPostulacion(), nombre_usuario);

			log.debug(response_validate_data);
//
			if ("".equals(response_validate_data)) {
				TvdUtils.validateExisteCliente(user_data, rut_empresa, log);
				TvdUtils.insertDocumentDataToMatriz(temp_folder, postulacion.getCodigoPostulacion(), user_data, log);

				response_message = "Se recibio la carpeta tecnica";
				correcto = true;
			} else {
				response_message = response_validate_data;
			}
			
		} catch (JSONException e) {
			correcto = false;
			response_message = "Datos tecnicos guardados incompletos";
			System.out.println("recibirCarpetaTecnica:"+e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			correcto = false;
			System.out.println("recibirCarpetaTecnica:"+e.getMessage());
			response_message = "Error en postulación, contactarse con: mesa.ayuda@subtel.gob.cl";
			e.printStackTrace();
		} catch (NullPointerException err) {
			correcto = false;
			System.out.println("recibirCarpetaTecnica:"+err.getMessage());
			response_message = "No existen datos del usuario o datos técnicos guardados";
			err.printStackTrace();
		} catch (IOException e) {
			correcto = false;
			System.out.println("recibirCarpetaTecnica: "+ e.getMessage());
			response_message = "Error descomprimiendo archivo carpeta tecnica, contactarse con: mesa.ayuda@subtel.gob.cl";
			e.printStackTrace();
		}
		
		CarpetaTecnica.deleteTempFolder(temp_folder);
		RespuestaDTO respuesta = new RespuestaDTO();
		String response_code = correcto ? "OK" : "NOK";

		respuesta.setCodigo(response_code);
		respuesta.setMensaje(response_message);
		log.debug("Cod:" + response_code);
		log.debug("Msg:" + response_message);
		log.info("** FIN recibirCarpetaTecnica **");
		return respuesta;
	}

	@WebMethod(operationName = "paginaCalculoTVD", action = "urn:PaginaCalculoTVD")
	@WebResult(name = "return")
	public RespuestaDTO paginaCalculoTVD(@WebParam(name = "paginaCalculo") PaginaCalculoDTO paginaCalculo) {
		log.info("** recibirCarpetaTecnica ha sido invocado **");

		Gson gson = new Gson();
		log.debug(gson.toJson(paginaCalculo));

		RespuestaDTO respuesta = new RespuestaDTO();

		respuesta.setCodigo("OK");
		respuesta.setMensaje("Se recibio la carpeta tecnica");
		log.info("** FIN paginaCalculo **");
		return respuesta;
	}
}
