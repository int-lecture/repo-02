package var;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

@Path("/")

public class Registry {

	@PUT
	@Path("/register")
	@Consumes("application/json")
	@Produces("application/json")
	public Response register(JSONObject jsonObject) {
		return null;
	}

}