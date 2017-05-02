package var;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.codehaus.jettison.json.JSONObject;



@Path ("/send")
public class Send {
	public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";
	
	@PUT
	@Consumes("application/json")
	public void put(JSONObject object){
		
		try{
		if(object.getString("from") != null && object.getString("to") != null && object.getString("date") != null && object.getString("text")!= null){
			JSONObject response;
		}
		
		}catch (Exception e){
			JSONObject failedResponse;
			
		}
		
	}
	
}
