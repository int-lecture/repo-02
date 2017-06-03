package var;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

@Path("")
public class Main {

	private static SelectorThread threadSelector = null;

	/** String for date parsing in ISO 8601 format. */
	public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";

	private StorageProviderMongoDB spMDB = new StorageProviderMongoDB();

	public static void main(String[] args) {
		final String baseUri = "http://localhost:5001/";
		final String paket = "var";
		final Map<String, String> initParams = new HashMap<String, String>();

		initParams.put("com.sun.jersey.config.property.packages", paket);
		System.out.println("Starte grizzly...");
		try {
			threadSelector = GrizzlyWebContainerFactory.create(baseUri, initParams);
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		System.out.printf("Grizzly läuft unter %s%n", baseUri);
	}

	public static void stopLoginServer(){
		//System.exit(0);
		threadSelector.stopEndpoint();
	}

	/**
	 * Logs a user in if his credentials are valid.
	 *
	 * @param jsonString
	 *            A JSON object containing the fields user(email) and password.
	 * @return Returns a JSON object containing the fields token and
	 *         expire-date.
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response LoginUser(String jsonString) {
		String userName = "";
		String password = "";
		try {
			JSONObject obj = new JSONObject(jsonString);
			password = obj.getString("password");
			userName = obj.getString("user");
			System.out.println("user: " + userName);

		} catch (JSONException e) {
			System.out.println("Problem beim jsonString extrahieren");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		User user = spMDB.retrieveUser(userName);
		if (user != null && user.VerifyPassword(password)) {
			JSONObject obj = new JSONObject();
			user.GenerateToken();
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(Main.ISO8601);
				Calendar expireDate = user.GetTokenExpireDate();
				sdf.setTimeZone(expireDate.getTimeZone());
				obj.put("expire-date", sdf.format(expireDate.getTime()));
				obj.put("token", user.GetToken().toString());
			} catch (JSONException e) {
				System.out.println("Problem beim jasonobjekt f�llen");
				e.printStackTrace();
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
			SimpleDateFormat sdf = new SimpleDateFormat(Main.ISO8601);
			Calendar expireDate = user.GetTokenExpireDate();
			sdf.setTimeZone(expireDate.getTimeZone());
			spMDB.saveToken(user.GetToken(), sdf.format(expireDate.getTime()), user.pseudonym);
			return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(obj.toString()).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	/**
	 * Validates a user token.
	 *
	 * @param jsonString
	 *            A JSON object containing the fields token and pseudonym.
	 * @return Returns a JSON object containing the fields expire-date and
	 *         success.
	 */
	@POST
	@Path("/auth")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ValidateToken(String jsonString) {
		String token = "";
		String pseudonym = "";
		try {
			JSONObject obj = new JSONObject(jsonString);
			token = obj.getString("token");
			pseudonym = obj.getString("pseudonym");
			System.out.println(token);
			System.out.println(pseudonym);
		} catch (JSONException e) {
			System.out.println("Fehler beim extrahieren des jsonObject");
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		String expireDate= spMDB.retrieveToken(pseudonym, token);
		if (expireDate!=null) {
			SimpleDateFormat sdf = new SimpleDateFormat(Main.ISO8601);
			Date date;
			try {
				date = sdf.parse(expireDate);
			} catch (ParseException e1) {
				System.out.println("invalid Date");
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			Calendar cal = Calendar.getInstance();
			if (cal.getTime().before(date)) {
				JSONObject obj = new JSONObject();
				try {
					sdf = new SimpleDateFormat(Main.ISO8601);
					obj.put("success", "true");
					obj.put("expire-date", expireDate);
					return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*").entity(obj.toString()).build();

				} catch (JSONException e) {
					System.out.println("Fehler beim jsonObject f�llen");
					return Response.status(Response.Status.UNAUTHORIZED).build();
				}
			} else {
				// Token has expired
				spMDB.deleteToken(token);
			}
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();

	}

//	@OPTIONS
//	@Path("/login")
//	public Response optionsReg() {
//	    return Response.ok("")
//	            .header("Access-Control-Allow-Origin", "*")
//	            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
//	            .header("Access-Control-Allow-Credentials", "true")
//	            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
//	            .header("Access-Control-Max-Age", "1209600")
//	            .build();
//	}
//
//	@OPTIONS
//	@Path("/auth")
//	public Response optionsProfile() {
//	    return Response.ok("")
//	            .header("Access-Control-Allow-Origin", "*")
//	            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
//	            .header("Access-Control-Allow-Credentials", "true")
//	            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
//	            .header("Access-Control-Max-Age", "1209600")
//	            .build();
//	}
}