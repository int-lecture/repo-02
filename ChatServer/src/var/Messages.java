package var;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.codehaus.jettison.json.JSONObject;

@Path("/messages/{user_id}/{sequence_number}")
public class Messages {

	@GET
	@Produces("application/json")
	public JSONObject receive(){
		JSONObject responseForUser = null;
				
		
		
		return null;
	}
	
	
}
