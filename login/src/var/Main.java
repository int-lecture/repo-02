package var;

import java.io.IOException;
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
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

@Path("")
public class Main {

	private static SelectorThread threadSelector = null;

	/** String for date parsing in ISO 8601 format. */
	public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";

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
	 * @throws JSONException
	 */
	@POST
	@Path("/login")
	@Consumes("application/json")
	@Produces("application/json")
	public Response LoginUser(String json) throws JSONException {
		String userName, password, pseudonym;
        boolean allowEmailLogin = true;
        try {
            JSONObject obj = new JSONObject(json);
            password = obj.getString("password");
            userName = obj.getString("user");
            pseudonym = obj.optString("pseudonym");
            if (pseudonym.equals("")){
            	pseudonym = null;
            }
            System.out.println("user: " + userName);
        } catch (JSONException e) {
            System.out.println("[/login] Failed to parse json request.");
            return Responder.badRequest();
        }
        // Check in settings if a login with partial login data is allow.
        if (pseudonym == null && !allowEmailLogin) {
            return  Responder.badRequest();
        }
        User user = StorageProviderMongoDB.retrieveUser(userName, pseudonym);
        if (user != null && user.VerifyPassword(password)) {
            JSONObject obj = new JSONObject();
            user.GenerateToken();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(ISO8601);
                Calendar expireDate = user.GetTokenExpireDate();
                sdf.setTimeZone(expireDate.getTimeZone());
                obj.put("expire-date", sdf.format(expireDate.getTime()));
                obj.put("token", user.GetToken());
                obj.put("pseudonym", user.pseudonym);
            } catch (JSONException e) {
                System.out.println("[/login] Error when building json response.");
                return  Responder.badRequest();
            }
            SimpleDateFormat sdf = new SimpleDateFormat(ISO8601);
            Calendar expireDate = user.GetTokenExpireDate();
            sdf.setTimeZone(expireDate.getTimeZone());
            StorageProviderMongoDB.saveToken(user.GetToken(), sdf.format(expireDate.getTime()), user.pseudonym);
            return  Responder.ok(obj);
        } else {
            return  Responder.unauthorised();
        }
	}

	 /**
     * Validates a user token.
     *
     * @param jsonString A JSON object containing the fields token and pseudonym.
     * @return Returns a JSON object containing the fields expire-date and
     * success.
     */
    @POST
    @Path("/auth")
	@Consumes("application/json")
	@Produces("application/json")
    public Response ValidateToken(String jsonString) {
        String token, pseudonym;
        try {
            JSONObject obj = new JSONObject(jsonString);
            token = obj.getString("token");
            pseudonym = obj.getString("pseudonym");
        } catch (JSONException e) {
            System.out.println("[/auth] Failed to parse json request.");
            return Responder.badRequest();
        }
        Date expireDate = StorageProviderMongoDB.retrieveToken(pseudonym, token);
        if (expireDate != null) {
            Calendar cal = Calendar.getInstance();
            if (cal.getTime().before(expireDate)) {
                JSONObject obj = new JSONObject();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(ISO8601);
                    obj.put("success", "true");
                    obj.put("expire-date", sdf.format(expireDate));
                    return Responder.ok(obj);
                } catch (JSONException e) {
                    System.out.println("[/auth] Error when building json response.");
                    return Responder.exception(e);
                }
            } else {
                StorageProviderMongoDB.deleteToken(token);
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