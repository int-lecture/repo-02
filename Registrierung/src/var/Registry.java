package var;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/")

public class Registry {

	DBMS database = new DBMS();

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
	public Response register(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			if (jsonObject.getString("pseudonym") != null && jsonObject.getString("password") != null
					&& jsonObject.getString("user") != null) {
				String pseudonym = jsonObject.getString("pseudonym");
				String password = jsonObject.getString("password");
				String email = jsonObject.getString("user");
				try {
					DBMS dbms = new DBMS();
					dbms.createUser(pseudonym, password, email);
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