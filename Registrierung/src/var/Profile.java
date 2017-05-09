package var;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
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
	public Response profile(JSONObject jsonObject) {
		try {
			if (jsonObject.getString("token") != null && jsonObject.getString("getownprofile") != null) {
				JSONObject profilDetails = new JSONObject();
				profilDetails.put("name", "susi");
				profilDetails.put("email", "susiLiebtBob@web.de");
				JSONArray contacts = new JSONArray();
				contacts.put("bob");
				contacts.put("peter");
				contacts.put("klaus");
				profilDetails.put("contacts", contacts);
				return Response.status(Response.Status.CREATED).entity(profilDetails).build();
			}
		} catch (JSONException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
	}
}