package var;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;


@Path("/profile")
public class Profile {

	/**
	 * Methode um die per Post einen Auth-Token und den eigenen Namen entgegen
	 * nimmt und das eigene Profil zurück gibt.
	 *
	 * @param jsonObject
	 *            token, name, email
	 * @return Profil Details / BadRequest
	 */
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response profile(String json){
		try{
			JSONObject jsonObject = new JSONObject(json);
			if (jsonObject.getString("token") != null && jsonObject.getString("getownprofile") != null) {
				String token = jsonObject.getString("token");
				String profile = jsonObject.getString("getownprofile");
				// Authentifizierung prüfen
				DBMS dbms = new DBMS();
				if (dbms.checkToken(profile, token)) {
					JSONObject profilDetails = new JSONObject();
					// Rückgabe aufbauen
					profilDetails.put("name", profile);
					profilDetails.put("email", dbms.getEmail(profile));
					// Kontakte abrufen
					JSONArray contacts = new JSONArray();
					for (String contact : dbms.getContacts(profile)) {
						contacts.put(contact);
					}
					profilDetails.put("contacts", contacts);
					return Responder.created(profilDetails);
				} else {
					return Responder.unauthorised();
				}
			}
			// Probleme mit Authentifizierung oder Anfrage
			return Responder.badRequest();
		} catch (Exception e) {
			return Responder.exception(e);
		}
	}

	@OPTIONS
	public Response optionsReg() {
	    return Responder.preFlight();
	}
}