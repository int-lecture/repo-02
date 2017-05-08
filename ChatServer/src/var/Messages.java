package var;

import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Messages {
	LinkedList<String[]> messageList = Menu.getMessageList();

	/**
	 * Empfängt Anfragen und sendet alle vorhandenen Nachrichten.
	 *
	 * @return Array von neuen Nachrichten
	 */
	@GET
	@Path("/messages/{user_id}")
	@Produces("application/json")
	public JSONArray receive(@PathParam("user_id") String username) {
		return receive(username, 0);
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
	public JSONArray receive(@PathParam("user_id") String username, @PathParam("sequenceNumber") int seqRecieved) {
		// Feld um die Nachrichten-Listenelemente in
		// ein zusammenhängendes JSONArray zu packen
		JSONArray responseForUser = new JSONArray();
		for (int i = messageList.size() - 1; i >= 0; i--) {
			String[] s = messageList.get(i);
			int seqMessage = Integer.parseInt(s[4]);
			if (username.equals(s[0]) && seqMessage > seqRecieved) {
				try {
					JSONObject jsonMessage = new JSONObject();
					jsonMessage.put("from", s[1]);
					jsonMessage.put("to", s[0]);
					jsonMessage.put("date", s[2]);
					jsonMessage.put("text", s[3]);
					jsonMessage.put("sequence", s[4]);
					responseForUser.put(jsonMessage);
				} catch (JSONException e) {
					System.out.println("Fehler beim Abrufen einer Nachricht");
				}
			}
			if (username.equals(s[0]) && seqMessage <= seqRecieved) {
				messageList.remove(i);
			}
		}
		return responseForUser;
	}

}
