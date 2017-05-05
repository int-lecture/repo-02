package var;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;



@Path ("/send")
public class Send {

	//Data-formate
	public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";

	@PUT
	@Consumes("application/json")
	public void put(JSONObject object){
		System.out.println("Send");
		try{
		//Check if all Request-Elements are not empty
		if(object.getString("from") != null &&
			object.getString("to") != null &&
			object.getString("date") != null &&
			object.getString("text")!= null){

			//Put the message into the list-container
			LinkedList<String[]> messageList = new LinkedList<>();
			String from = object.getString("from");
			String to = object.getString("to");
			String date = object.getString("date");
			String text = object.getString("text");

			JSONObject response = new JSONObject();
		}
		//If with the response is something wrong,
		//get oudda this try and get catched
		else {
			throw new Exception();
		}

		//Send the fail-Response with statuscode 400
		}catch (Exception e){
			JSONObject failedResponse = new JSONObject();
			try {
				failedResponse.put("Statuscode", 400);
			} catch (JSONException e1) {
				System.out.println("If you see this message, you are "
						+ "a god!");
			}
		}

	}

}
