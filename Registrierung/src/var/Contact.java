package var;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

@Path("/contact")
public class Contact {
	
	@PUT
	@Consumes("application/json")
	public Response put(String json) {
		try {
			JSONObject object = new JSONObject(json);
			// Check if all Request-Elements are there
			if (object.getString("pseudonym") != null && object.getString("contact") != null && object.getString("token") != null) {

				DBMS db = new DBMS();

				String pseudo = object.getString("pseudonym");
				String contact = object.getString("contact");
				String token = object.getString("token");
				
				if (db.checkToken(pseudo, token)) {
					return Responder.unauthorised();
				} else {
					db.addContact(pseudo, contact);
				}

				JSONObject createdDetails = new JSONObject();
				createdDetails.put("succes", pseudo + " has a new friend now");
				return Responder.created(createdDetails);
			}
			else {
				return Responder.badRequest();
			}
		} catch (Exception e) {
			return Responder.exception(e);
		}
	}

	@OPTIONS
	@Path("/contact")
	public Response optionsContact() {
		return Responder.preFlight();
	}

}
