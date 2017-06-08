package var;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Path("/send")
public class Send {

	// Data-formate
	private static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";

	@PUT
	@Consumes("application/json")
	public Response put(JSONObject object) {
		try {
			// Check if all Request-Elements are not empty
			if (object.getString("from") != null && object.getString("to") != null && object.getString("date") != null
					&& object.getString("text") != null && object.getString("token") != null) {

				// Put the message into the list-container
				SimpleDateFormat currentTime = new SimpleDateFormat(ISO8601);
				// we would check the token, send a response

				StorageProviderMongoDB db = new StorageProviderMongoDB();

				String token = object.getString("token");
				String from = object.getString("to");
				String to = object.getString("from");
				String date = object.getString("date");
				currentTime.parse(date);
				String text = object.getString("text");
				long sequence = db.retrieveAndUpdateSequence(to);
				Message newMessage = new Message(from, to, date, sequence, text);

				String url = "http://localhost:5001/auth";
				Client client = Client.create();
				WebResource webResource = client.resource(url);
				String input = "{\"token\": \"" + token + "\",\"pseudonym\": \"" + to + "\"}";
				System.out.println(input);
				ClientResponse response;
				try {
					response = webResource.type("application/json").post(ClientResponse.class, input);
				} catch (ClientHandlerException e) {
					return Responder.build(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage(), false);
				}
				if (response.getStatus() != 200) {
					System.out.println(response.getStatus());
					return Responder.unauthorised();
				} else {
					db.storeMessage(newMessage);
				}

				JSONObject createdDetails = new JSONObject();
				// Current Systemtime
				createdDetails.put("date", currentTime.format(new Date())); // currentTime.toString()
				createdDetails.put("sequence", sequence);
				// Test
				System.out.println("from: " + from + ", to: " + to);
				System.out.println(text);
				System.out.println("Sequence: " + sequence);
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
