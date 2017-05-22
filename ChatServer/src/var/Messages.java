package var;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Messages {

	/**
	 * Empfängt Anfragen und sendet alle vorhandenen Nachrichten.
	 *
	 * @return Array von neuen Nachrichten
	 */
	@GET
	@Path("/messages/{user_id}")
	@Produces("application/json")
	public JSONArray receive(@PathParam("user_id") String username, @Context HttpHeaders header) {
		return receive(username, 0, header);
	}

	/**
	 * Empfängt Nachrichten im JSON Format und sendet wenn vorhanden neue
	 * Nachrichten.
	 *
	 * @return Array von neuen Nachrichten
	 */
	@GET
	@Path("/messages/{user_id}/{sequenceNumber}")
	@Produces("application/json")
	public JSONArray receive(@PathParam("user_id") String username, @PathParam("sequenceNumber") int seqRecieved,
			@Context HttpHeaders header) {
		MultivaluedMap<String, String> map = header.getRequestHeaders();
		map.get("Authorization").get(0);
		// Feld um die Nachrichten-Listenelemente in
		// ein zusammenhängendes JSONArray zu packen
		JSONArray responseForUser = new JSONArray();
		StorageProviderMongoDB db = new StorageProviderMongoDB();
		List<Message> messageList = db.retrieveMessages(username, seqRecieved, true);
		// VIP's only!
		for (int i = messageList.size() - 1; i >= 0; i--) {
			Message m = messageList.get(i);
			JSONObject jsonMessage = new JSONObject();
			try {
				jsonMessage.put("from", m.getFrom());
				jsonMessage.put("to", m.getTo());
				jsonMessage.put("date", m.getDate());
				jsonMessage.put("text", m.getText());
				jsonMessage.put("sequence", m.getSequence());
			} catch (JSONException e) {
				System.out.println("Fehler beim Erstellen der Antwort");
			}
			responseForUser.put(jsonMessage);
		}
		return responseForUser;
	}

}
