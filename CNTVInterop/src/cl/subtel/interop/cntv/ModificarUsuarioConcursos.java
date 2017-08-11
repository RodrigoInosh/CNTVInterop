package cl.subtel.interop.cntv;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.MissingResourceException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import cl.subtel.interop.cntv.dto.EmpresaDTO;
import cl.subtel.interop.cntv.dto.RepresentanteLegalDTO;
import cl.subtel.interop.cntv.dto.RespuestaDTO;
import cl.subtel.interop.cntv.dto.UsuarioDTO;
import cl.subtel.interop.cntv.util.FileProperties;

@WebService(targetNamespace = "http://cntv.interop.subtel.cl/", portName = "ModificarUsuarioConcursosPort", serviceName = "ModificarUsuarioConcursosService")
public class ModificarUsuarioConcursos {

	private static final Logger log = LogManager.getLogger();

	@WebMethod(operationName = "modificarUsuario", action = "urn:ModificarUsuario")
	@WebResult(name = "return")
	@SuppressWarnings("deprecation")
	public RespuestaDTO modificarUsuario(@WebParam(name = "arg0") UsuarioDTO usuario,
			@WebParam(name = "arg1") EmpresaDTO empresa,
			@WebParam(name = "arg2") RepresentanteLegalDTO representante) {
		
		log.info("** AgregarUsuario ha sido invocado **");
		RespuestaDTO respuesta = new RespuestaDTO();
		
		// Conecta a Mongodb
		MongoClient mongoClient = null;
		boolean modificar = false;
		try {
			DB db = null;
			DBCollection col = null;
			String dbMasPuerto = FileProperties.getParamProperties("basedatoscalculos", "Interoperabilidad");
			if (dbMasPuerto == null || dbMasPuerto.isEmpty()) {
				modificar = false;
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
					// Usuario ya existe, Proceder	
					modificar = true;
				}
				rec.close();
				
				if (modificar) {
					// Armar nuevo documento con datos de usuario
					log.debug("Modificando usuario " + usuario.getId());

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

					log.debug("Modificando... ");
					//WriteConcern wc = db.getWriteConcern();
					col.update(query, documento); //(documento, wc);
					log.debug("Modificado OK");
					respuesta.setCodigo("OK");
					respuesta.setMensaje("Se recibio y modifico el usuario");					
				} else {
					log.error("No se encontro el usuario a modificar");
					respuesta.setCodigo("ERROR");
					respuesta.setMensaje("No se encontro el usuario a modificar");
				}
				
			}

		} catch (UnknownHostException e) {
			log.error(e.getMessage());
			respuesta.setCodigo("ERROR");
			respuesta.setMensaje(e.getMessage());			
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
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}

		log.info("Respuesta: " + respuesta.getCodigo() + " - " + respuesta.getMensaje());
		log.info("** FIN AgregarUsuario **");
		return respuesta;
	}
}
