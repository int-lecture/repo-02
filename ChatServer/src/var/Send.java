package var;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;

@Path("/send")
public class Send {
	static HashMap<String, String[]> cache = new HashMap<String, String[]>();

	// Data-formate
	private static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";

	@PUT
	@Consumes("application/json")
	public Response put(String json) {
		try {
			JSONObject object = new JSONObject(json);
			// Check if all Request-Elements are not empty
			if (object.getString("from") != null && object.getString("to") != null && object.getString("date") != null
					&& object.getString("text") != null && object.getString("token") != null) {

				// Put the message into the list-container
				SimpleDateFormat currentTime = new SimpleDateFormat(ISO8601);
				// we would check the token, send a response

				StorageProviderMongoDB db = new StorageProviderMongoDB();

				String token = object.getString("token");
				String from = object.getString("from");
				String to = object.getString("to");
				String date = object.getString("date");
				currentTime.parse(date);
				String text = object.getString("text");
				long sequence = db.retrieveAndUpdateSequence(to);
				Message newMessage = new Message(from, to, date, sequence, text);
				
				if(token.equals(Cache.getCachedToken(from))){
					db.storeMessage(newMessage);
				} else {
					String url = "http://141.19.142.56:5001";
					String input = "{\"token\": \"" + token + "\",\"pseudonym\": \"" + from + "\"}";
					String response;
					try {
					Client client = Client.create();
			        
			            response = client.resource(url + "/auth").accept("application/json")
			                    .type("application/json").post(String.class, input);
			            client.destroy();
					} catch (Exception e) {
						return Responder.unauthorised();
					}
					 JSONObject resp = new JSONObject(response);
					 if (!resp.get("success").equals("true")) {
						return Responder.unauthorised();
					} else {
						System.out.println("try cash");
						Cache.cacheToken(from, token, resp.getString("expire-date"));
					}
				}
				
				JSONObject createdDetails = new JSONObject();
				// Current Systemtime
				createdDetails.put("date", currentTime.format(new Date())); // currentTime.toString()
				createdDetails.put("sequence", sequence);
				return Responder.created(createdDetails);
			}
			// If with the response is something wrong,
			// get oudda this try and get catched
			else {
				return Responder.badRequest();
			}
			// Send the fail-Response with statuscode 400
		} catch (Exception e) {
			return Responder.exception(e);
		}
	}

	@OPTIONS
	@Path("/send")
	public Response optionsSend() {
		return Responder.preFlight();
	}

}
