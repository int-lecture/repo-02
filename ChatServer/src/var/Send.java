package var;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;




@Path("/send")
public class Send {

	//Data-formate
	public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";

	@PUT
	@Consumes("application/json")
	public Response put(JSONObject object){
		try{
		//Check if all Request-Elements are not empty
		if(object.getString("from") != null &&
			object.getString("to") != null &&
			object.getString("date") != null &&
			object.getString("text")!= null){

			//Put the message into the list-container
			LinkedList<String[]> messageList = Menu.getMessageList();
			String from = object.getString("to");
			String to = object.getString("from");
			String date = object.getString("date");
			String text = object.getString("text");
			String[] newMessage = {from, to, date, text, Menu.getSeqCounter()+ ""};
			messageList.add(newMessage);
			JSONObject createdDetails = new JSONObject();
			//Hier müsste das aktuelle Datum ins JSON geschrieben werden
			//Current Systemtime
			SimpleDateFormat currentTime = new SimpleDateFormat(ISO8601);

			createdDetails.put("date", currentTime.format(new Date())); //currentTime.toString()
			createdDetails.put("sequence", Menu.getSeqCounter());
			//Test
			System.out.println("from: " + from + ", to: " + to);
			System.out.println(currentTime.toPattern());
			System.out.println(text);
			System.out.println("Sequence: " +  Menu.getSeqCounter());
			Menu.incSeqCounter();
			return Response.status(Response.Status.CREATED).entity(createdDetails).build();
		}
		//If with the response is something wrong,
		//get oudda this try and get catched
		else {
			return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
		}

		//Send the fail-Response with statuscode 400
		}catch (Exception e){
			return Response.status(Response.Status.BAD_REQUEST).entity("Bad Request").build();
		}

	}

}
