package var.chat.registrierung.test;

import static io.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import var.DBMS;
import var.Main;

/**
 * Test the login.
 */
public class TestRegistrierungsServer {

	@Before
	public void setUp() throws IOException {
		RestAssured.baseURI = "http://localhost";
		RestAssured.basePath = "/";
		RestAssured.port = 5999;
		Main.startGrizzly(RestAssured.baseURI + ":" + RestAssured.port + "/");
		DBMS dbms = new DBMS();
		dbms.clearForTest();
	}

//	@After
//	public void tearDown() throws IOException {
//		Main.stopGrizzly();
//		DBMS dbms = new DBMS();
//		dbms.clearForTest();
//	}

	@Test
	public void testRegistrierung() {
		expect().statusCode(200).contentType(MediaType.APPLICATION_JSON).body("token", notNullValue()).given()
				.contentType(MediaType.APPLICATION_JSON)
				.body(("{'pseudonym': 'bob','password': 'halloIchbinBob', 'user': 'bob@web.de'}").replace('\'', '"'))
				.when().put("/profile");
		expect().statusCode(418).given().contentType(MediaType.APPLICATION_JSON)
				.body(("{'pseudonym': 'bob','password': 'halloIchbinBob', 'user': 'bob@web.de'}").replace('\'', '"'))
				.when().put("/profile");

		expect().statusCode(400).given().contentType(MediaType.APPLICATION_JSON)
				.body(("{'password': 'halloIchbinBob', 'user': 'bob@web.de'}").replace('\'', '"'))
				.when().put("/profile");

		expect().statusCode(400).given().contentType(MediaType.APPLICATION_JSON)
		.body(("{'pseudonym': 'bob', 'user': 'bob@web.de'}").replace('\'', '"'))
		.when().put("/profile");

		expect().statusCode(400).given().contentType(MediaType.APPLICATION_JSON)
		.body(("{'pseudonym': 'bob','password': 'halloIchbinBob'}").replace('\'', '"'))
		.when().put("/profile");
	}

	@Test
	public void testProfilanfragen() {
		Response resp = expect().statusCode(200).contentType(MediaType.APPLICATION_JSON).body("token", notNullValue())
				.body("expire-date", notNullValue()).given().contentType(MediaType.APPLICATION_JSON)
				.body(("{ 'user': 'alfred@example.com', 'password': 'abcd1234', 'pseudonym': 'alfi'}".replace('\'',
						'"')))
				.when().post("/login");

		String token = resp.path("token").toString();

		JSONObject json = new JSONObject();
		json.put("token", token);
		json.put("pseudonym", "alfi");

		expect().statusCode(200).contentType(MediaType.APPLICATION_JSON).body("success", notNullValue())
				.body("expire-date", notNullValue()).given().contentType(MediaType.APPLICATION_JSON)
				.body(json.toString()).when().post("/auth");

		json = new JSONObject();
		json.put("token", token);
		json.put("pseudonym", "alfiX");

		expect().statusCode(403).given().contentType(MediaType.APPLICATION_JSON).body(json.toString()).when()
				.post("/auth");

		json = new JSONObject();
		json.put("token", token + "x");
		json.put("pseudonym", "alfi");

		expect().statusCode(403).given().contentType(MediaType.APPLICATION_JSON).body(json.toString()).when()
				.post("/auth");
	}
}