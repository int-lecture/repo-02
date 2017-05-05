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

@Path("/messages/{user_id}/{sequenceNumber}")
@Produces(MediaType.APPLICATION_JSON)
public class Messages {

	int seqCounter = 0;
	/**
	 * Empfängt Nachrichten im JSON Format und sendet wenn vorhanden neue Nachrichten.
	 * @return Array von neuen Nachrichten
	 * @throws JSONException
	 */
	@GET
	@Produces("application/json")
	public JSONArray receive(@PathParam("user_id") String username, @PathParam("sequenceNumber") int seqRecieved) throws JSONException {
		
		//Feld um die Nachrichten-Listenelemente in 
		//ein zusammenhängendes JSONArray zu packen
		JSONArray responseForUser = new JSONArray();
		
		//Verkettete Liste mit einer Nachricht pro Glied<---(hihihi)
		LinkedList<String[]> messageList = new LinkedList<>();
		
		//Erstelle eine Dummy JSON-Nachricht
		{
		
		//Dummystring
		String[] s = {"bob","David","hallo David","0"};
		
		//Dummy-JSON-Nachricht
		JSONObject jsonMessage = new JSONObject();
		jsonMessage.put("from", s[0]);
		jsonMessage.put("to", s[1]);
		jsonMessage.put("text", s[2]);
		jsonMessage.put("sequence", s[3]);

		responseForUser.put(jsonMessage);
		messageList.addFirst(s);
		}
		
		//Der obrige Code nochmal mit ausprobiert mit try-catch
		//Packe außerdem die JSON-Nachricht in ein JSON-Array
		//Dieses JSON-Array bekommt der Client vor die Füße geworfen
		for (String[] s : messageList) {
			int seqMessage = Integer.parseInt(s[3]);
			if(username.equals(s[1]) && seqMessage > seqRecieved){
				try{
					System.out.println(s);
					JSONObject jsonMessage = new JSONObject();
					jsonMessage.put("from", s[0]);
					jsonMessage.put("to", s[1]);
					jsonMessage.put("text", s[2]);
					jsonMessage.put("sequence", s[3]);
					responseForUser.put(jsonMessage);
				} catch (JSONException e){
					System.out.println("Fehler beim Senden einer Nachricht");
				}
			}
		}
		return responseForUser;
	}
}
