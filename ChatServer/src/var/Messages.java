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
	LinkedList<String[]> ll = new LinkedList<>();
	int seqCounter = 0;

	{
	String[] s = {"bob","David","hallo David",seqCounter++ + ""};
	ll.addFirst(s);
	String[] s2 = {"bob","David","hallo David die zweite",seqCounter++ + ""};
	ll.addFirst(s2);
	String[] s3 = {"bob","David","hallo David die dritte",seqCounter++ + ""};
	ll.addFirst(s3);
	String[] s4 = {"bob","David","hallo David die vierte",seqCounter++ + ""};
	ll.addFirst(s4);
	}
	/**
	 * Empfängt Nachrichten im JSON Format und sendet wenn vorhanden neue Nachrichten.
	 * @return Array von neuen Nachrichten
	 * @throws JSONException
	 */
	@GET
	@Produces("application/json")
	public JSONArray receive(@PathParam("user_id") String username, @PathParam("sequenceNumber") int seqRecieved) throws JSONException {
		System.out.println("aufruf name: " + username + ", seq: " + seqRecieved);
		JSONArray responseForUser = new JSONArray();


		for (int i = ll.size() - 1; i >= 0; i--) {
			String[] s = ll.get(i);
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
				ll.remove(i);
				System.out.println(ll.size());
			}
		}
		return responseForUser;
	}
}
