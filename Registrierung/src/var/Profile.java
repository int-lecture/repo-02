package var;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import netscape.javascript.JSObject;


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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response profile(String json) {
		try {
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
					return Response.status(Response.Status.CREATED).entity(profilDetails).build();
				}
			}
			// Probleme mit Authentifizierung oder Anfrage
		} catch (JSONException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
	}
}