package var;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.codehaus.jettison.json.JSONObject;

public class Responder {
	static Response build(Response.Status stat, String entity, boolean allowed) {
		return cors(Response.status(stat).entity(entity), allowed).build();
	}

	static Response build(int stat, String entity, boolean allowed) {
		return cors(Response.status(stat).entity(entity), allowed).build();
	}

	private static ResponseBuilder cors(ResponseBuilder resp, boolean allowed) {
		if (allowed) {
			return resp.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods",
					"GET, POST, PUT, DELETE, OPTIONS, HEAD");
		} else {
			return resp;
		}
	}

	static Response badRequest() {
		return Responder.build(Response.Status.BAD_REQUEST, "Bad Request", false);
	}

	public static Response created(JSONObject profilDetails) {
		return build(Response.Status.CREATED, profilDetails.toString(), true);
	}

	public static Response ok(JSONObject obj) {
		return Responder.build(Response.Status.OK, obj.toString(), true);
	}

	static Response unauthorised() {
		return Responder.build(Response.Status.UNAUTHORIZED, "böser bub", false);
	}

	public static Response teapot() {
		return Responder.build(418, "Pseudonym or Username taken", true);
	}

	public static Response preFlight() {
		return Response.ok("").header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600").build();
	}

	public static Response exception(Exception e) {
		return Responder.build(Response.Status.BAD_REQUEST, e.getMessage(), false);
	}
}
