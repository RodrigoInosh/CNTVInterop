package cl.subtel.interop.cntv.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBMongoDAO {
	
	private static final Logger log = LogManager.getLogger();

	public static JSONObject getData(BasicDBObject whereQuery, String collection_name) throws JSONException {
		MongoClient mongo = new MongoClient("172.30.10.203", 27017);
		MongoDatabase db = mongo.getDatabase("Calculos");
		MongoCollection<Document> collection = db.getCollection(collection_name);

		log.debug("Mongo Query conditions: "+ whereQuery);
		Document myDoc = collection.find(whereQuery).first();
		JSONObject document_json = null;

		document_json = new JSONObject(myDoc.toJson());
		mongo.close();
		log.info("mongo connection closed");
		return document_json;
	}

	public static JSONObject getUserDataById(String userID) throws JSONException {

		JSONObject user_data = new JSONObject();
		BasicDBObject query_conditions = new BasicDBObject();
		query_conditions.put("id", Integer.parseInt(userID));
		user_data = DBMongoDAO.getData(query_conditions, "usuariosTVD");

		return user_data;
	}

	public static JSONObject getDatosTecnicosConcurso(String nombre_archivo, String codigo_postulacion,
			String user_name) throws JSONException {
		
		String intensidad_campo = "", sist_radiante = "", identificador = "";
		String splitted_file_name[] = null;
		BasicDBObject whereQuery = new BasicDBObject();

		splitted_file_name = nombre_archivo.split("_");
		intensidad_campo = getIntensityByTypeService(splitted_file_name[0]);
		sist_radiante = getSistRadiantTypeByAlias(splitted_file_name[1]);
		identificador = splitted_file_name[2];

		whereQuery.put("calculos.pIntensidad", intensidad_campo);
		whereQuery.put("calculos.form_data.carac_tecnicas.sist_radiante", sist_radiante);
		whereQuery.put("definitivo", "1");
		whereQuery.put("identificador", identificador);
		whereQuery.put("codigo_postulacion", codigo_postulacion);
		whereQuery.put("user", user_name);

		JSONObject datos_sist_principal = DBMongoDAO.getData(whereQuery, "datosTecnicosCNTV");

		return datos_sist_principal;
	}

	public static String getIntensityByTypeService(String type_sist) {
		String name_sist = "";

		switch (type_sist) {
		case "ZonaServicio":
			name_sist = "48";
			break;
		case "ZonaCobertura":
			name_sist = "40";
			break;
		case "ZonaUrbana":
			name_sist = "66";
			break;
		default:
			break;
		}

		return name_sist;
	}

	public static String getSistRadiantTypeByAlias(String anthena_alias) {
		String anthena_name = "";

		switch (anthena_alias) {
		case "PTx0":
			anthena_name = "sistRadiantePrinc";
			break;
		case "PTx1":
			anthena_name = "sistRadianteAdic1";
			break;
		case "PTx2":
			anthena_name = "sistRadianteAdic2";
			break;
		default:
			break;
		}

		return anthena_name;
	}

}
