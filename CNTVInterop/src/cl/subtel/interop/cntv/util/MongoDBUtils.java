package cl.subtel.interop.cntv.util;

import org.bson.Document;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtils {

	public static JSONObject getData(BasicDBObject whereQuery, String collection_name) throws JSONException {
		MongoClient mongo = new MongoClient("172.30.10.203", 27017);
		MongoDatabase db = mongo.getDatabase("Calculos");
		MongoCollection<Document> collection = db.getCollection(collection_name);

		Document myDoc = collection.find(whereQuery).first();
		JSONObject document_json = null;

		document_json = new JSONObject(myDoc.toJson());
		mongo.close();
		System.out.println("mongo connection closed");
		return document_json;
	}

	public static JSONObject getUserDataById(String userID) throws JSONException {

		JSONObject user_data = new JSONObject();
		BasicDBObject query_conditions = new BasicDBObject();
		query_conditions.put("id", Integer.parseInt(userID));
		user_data = MongoDBUtils.getData(query_conditions, "usuariosTVD");

		return user_data;
	}

}
