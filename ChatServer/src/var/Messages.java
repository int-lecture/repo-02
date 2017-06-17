package var;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Messages {

	/**
	 * Empfängt Anfragen und sendet alle vorhandenen Nachrichten.
	 *
	 * @return Array von neuen Nachrichten
	 * @throws JSONException 
	 */
	@GET
	@Path("/messages/{user_id}")
	@Produces("application/json")
	public Response receive(@PathParam("user_id") String username, @Context HttpHeaders header) throws JSONException {
		return receive(username, 0, header);
	}

	/**
	 * Empfängt Nachrichten im JSON Format und sendet wenn vorhanden neue
	 * Nachrichten.
	 *
	 * @return Array von neuen Nachrichten
	 * @throws JSONException 
	 */
	@GET
	@Path("/messages/{user_id}/{sequenceNumber}")
	@Produces("application/json")
	public Response receive(@PathParam("user_id") String username, @PathParam("sequenceNumber") int seqRecieved,
			@Context HttpHeaders header) {
		try {
			MultivaluedMap<String, String> map = header.getRequestHeaders();

			List<String> check = map.get("Authorization");
			String token = "";
			if (check != null) {
				token = check.get(0);
			} else {
				return Responder.unauthorised();
			}
			if (!token.equals(Cache.getCachedToken(username))) {
				String url = "http://141.19.142.56:5001";
				String input = "{\"token\": \"" + token + "\",\"pseudonym\": \"" + username + "\"}";
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
					Cache.cacheToken(username, token, resp.getString("expire-date"));
				}
			}

			// Feld um die Nachrichten-Listenelemente in
			// ein zusammenhängendes JSONArray zu packen
			JSONArray responseForUser = new JSONArray();
			StorageProviderMongoDB db = new StorageProviderMongoDB();
			List<Message> messageList = db.retrieveMessages(username, seqRecieved, true);
			if (messageList == null) {
				return Responder.created(new JSONObject());
			}
			for (int i = messageList.size() - 1; i >= 0; i--) {
				Message m = messageList.get(i);
				JSONObject jsonMessage = new JSONObject();
				try {
					jsonMessage.put("from", m.getFrom());
					jsonMessage.put("to", m.getTo());
					jsonMessage.put("date", m.getDate());
					jsonMessage.put("text", m.getText());
					jsonMessage.put("sequence", m.getSequence());
					jsonMessage.put("group", m.getGroup());					
				} catch (JSONException e) {
					System.out.println("Fehler beim Erstellen der Antwort");
				}
				responseForUser.put(jsonMessage);
			}
			return Responder.created(responseForUser);
		} catch (Exception e) {
			return Responder.exception(e);
		}
	}

	@OPTIONS
	@Path("/messages/{user_id}")
	public Response optionsMessage() {
		return Responder.preFlight();
	}

	@OPTIONS
	@Path("/messages/{user_id}/{sequenceNumber}")
	public Response optionsMessages() {
		return Responder.preFlight();
	}
}
