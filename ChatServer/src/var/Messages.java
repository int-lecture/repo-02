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
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class Messages {
	LinkedList<String[]> messageList = new LinkedList<>();
	int seqCounter = 0;

	{
	String[] s = {"bob","David","hallo David",seqCounter++ + ""};
	messageList.addFirst(s);
	String[] s2 = {"bob","David","hallo David die zweite",seqCounter++ + ""};
	messageList.addFirst(s2);
	String[] s3 = {"bob","David","hallo David die dritte",seqCounter++ + ""};
	messageList.addFirst(s3);
	String[] s4 = {"bob","David","hallo David die vierte",seqCounter++ + ""};
	messageList.addFirst(s4);
	}
	/**
	 * Empfängt Nachrichten im JSON Format und sendet wenn vorhanden neue Nachrichten.
	 * @return Array von neuen Nachrichten
	 */
	@GET
	@Path("/messages/{user_id}/{sequenceNumber}")
	@Produces("application/json")
	public JSONArray receive(@PathParam("user_id") String username, @PathParam("sequenceNumber") int seqRecieved) {
		System.out.println("aufruf name: " + username + ", seq: " + seqRecieved);
		//Feld um die Nachrichten-Listenelemente in
		//ein zusammenhängendes JSONArray zu packen
		JSONArray responseForUser = new JSONArray();
		for (int i = messageList.size() - 1; i >= 0; i--) {
			String[] s = messageList.get(i);
			int seqMessage = Integer.parseInt(s[3]);
			if(username.equals(s[0]) && seqMessage >= seqRecieved){
				try{
					JSONObject jsonMessage = new JSONObject();
					jsonMessage.put("from", s[0]);
					jsonMessage.put("to", s[1]);
					jsonMessage.put("text", s[2]);
					jsonMessage.put("sequence", s[3]);
					responseForUser.put(jsonMessage);
				} catch (JSONException e){
					System.out.println("Fehler beim Abrufen einer Nachricht");
				}
			} else {
				System.out.println(seqMessage + " ?>= " + seqRecieved);
				System.out.println(username + " ?= " +s[1]);

			}
			if(username.equals(s[0]) && seqMessage < seqRecieved){
				System.out.println("löschen von " + i);
				messageList.remove(i);
				System.out.println(messageList.size());
			}
		}
		return responseForUser;
	}

	/**
	 * Empfängt Anfragen und sendet alle vorhandenen Nachrichten.
	 * @return Array von neuen Nachrichten
	 */
	@GET
	@Path("/messages/{user_id}")
	@Produces("application/json")
	public JSONArray receive(@PathParam("user_id") String username) {
		return receive(username,0);
	}


}
