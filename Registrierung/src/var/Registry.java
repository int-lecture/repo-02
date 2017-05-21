package var;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.LinkedList;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Path("/")

public class Registry {
	
	
	/**
	 * Methode die die Registrierungen neuer Nutzer entgegennimmt.
	 *
	 * @param jsonObject
	 *            pseudonym, password, user
	 * @return success / 418 / BadRequest
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	@PUT
	@Path("/register")
	@Consumes("application/json")
	@Produces("application/json")
	public Response register(JSONObject jsonObject) throws NoSuchAlgorithmException, InvalidKeySpecException {
		try {
			if (jsonObject.getString("pseudonym") != null && jsonObject.getString("password") != null
					&& jsonObject.getString("user") != null) {
				String pseudonym = jsonObject.getString("pseudonym");
				String password = jsonObject.getString("password");
				String email = jsonObject.getString("user");
				try {
					DBMS.createUser(pseudonym,password,email);
					JSONObject profilDetails = new JSONObject();
					profilDetails.put("success", "true");
					return Response.status(Response.Status.OK).entity(profilDetails).build();
				} catch (InvalidParameterException e) {
					return Response.status(418).entity("Pseudonym or Username taken").build();
				}
			}
		} catch (JSONException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
	}
}