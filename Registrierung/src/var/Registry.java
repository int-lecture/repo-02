package var;

import java.security.InvalidParameterException;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/")

public class Registry {

	/**
	 * Methode die die Registrierungen neuer Nutzer entgegennimmt.
	 *
	 * @param jsonObject
	 *            pseudonym, password, user
	 * @return succes / 418 / BadRequest
	 */
	@PUT
	@Path("/register")
	@Consumes("application/json")
	@Produces("application/json")
	public Response register(JSONObject jsonObject) {
		try {
			if (jsonObject.getString("pseudonym") != null && jsonObject.getString("password") != null
					&& jsonObject.getString("user") != null) {
				String pseudonym = jsonObject.getString("pseudonym");
				String password = jsonObject.getString("password");
				String email = jsonObject.getString("user");
				try {
					//Nutzer erstellen
					UserManagement um = new UserManagement();
					um.createUser(pseudonym, email, password);
					//Rückgabe aufbauen
					JSONObject profilDetails = new JSONObject();
					profilDetails.put("succes", "true");
					System.out.println(um);
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