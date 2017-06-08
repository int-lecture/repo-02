package var;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
	public Response profile(JSONObject jsonObject){
		try{
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
				}
			}
			// Probleme mit Authentifizierung oder Anfrage
		} catch (JSONException e) {
			return Responder.badRequest();
		}
		return Responder.badRequest();
	}
}