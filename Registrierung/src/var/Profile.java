package var;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

@Path("/profile")

public class Profile {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response profile(JSONObject jsonObject) {
		return null;
	}

}