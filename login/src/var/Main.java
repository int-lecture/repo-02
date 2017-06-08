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

	public static void stopLoginServer() {
		// System.exit(0);
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
	@Consumes("application/json")
	public Response LoginUser(JSONObject obj) {
		String userName = "";
		String password = "";
		try {
			password = obj.getString("password");
			userName = obj.getString("user");
			System.out.println("user: " + userName);
		} catch (JSONException e) {
			System.out.println("Problem beim jsonString extrahieren");
			return Responder.badRequest();
		}
		User user = spMDB.retrieveUser(userName);
		if (user != null && user.VerifyPassword(password)) {
			obj = new JSONObject();
			user.GenerateToken();
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(Main.ISO8601);
				Calendar expireDate = user.GetTokenExpireDate();
				sdf.setTimeZone(expireDate.getTimeZone());
				obj.put("expire-date", sdf.format(expireDate.getTime()));
				obj.put("token", user.GetToken().toString());
			} catch (JSONException e) {
				System.out.println("Problem beim jsonobjekt füllen");
				e.printStackTrace();
				return Responder.build(Response.Status.INTERNAL_SERVER_ERROR, "", false);
			}
			SimpleDateFormat sdf = new SimpleDateFormat(Main.ISO8601);
			Calendar expireDate = user.GetTokenExpireDate();
			sdf.setTimeZone(expireDate.getTimeZone());
			spMDB.saveToken(user.GetToken(), sdf.format(expireDate.getTime()), user.pseudonym);
			return Responder.ok(obj);
		} else {
			return Responder.unauthorised();
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
	@Consumes("application/json")
	public Response ValidateToken(JSONObject obj) {
		String token = "";
		String pseudonym = "";
		try {
			token = obj.getString("token");
			pseudonym = obj.getString("pseudonym");
			System.out.println(token);
			System.out.println(pseudonym);
		} catch (JSONException e) {
			System.out.println("Fehler beim extrahieren des jsonObject");
			return Responder.badRequest();
		}
		String expireDate = spMDB.retrieveToken(pseudonym, token);
		if (expireDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(Main.ISO8601);
			Date date;
			try {
				date = sdf.parse(expireDate);
			} catch (ParseException e1) {
				System.out.println("invalid Date");
				return Responder.badRequest();
			}
			Calendar cal = Calendar.getInstance();
			if (cal.getTime().before(date)) {
				obj = new JSONObject();
				try {
					sdf = new SimpleDateFormat(Main.ISO8601);
					obj.put("success", "true");
					obj.put("expire-date", expireDate);
					return Responder.ok(obj);

				} catch (JSONException e) {
					System.out.println("Fehler beim jsonObject füllen");
					return Responder.unauthorised();
				}
			} else {
				// Token has expired
				spMDB.deleteToken(token);
			}
		}
		return Responder.unauthorised();
	}

	@OPTIONS
	@Path("/login")
	public Response optionsLogin() {
	    return Responder.preFlight();
	}

	@OPTIONS
	@Path("/auth")
	public Response optionsAuth() {
	    return Responder.preFlight();
	}
}