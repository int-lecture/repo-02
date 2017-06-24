package var;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.bson.Document;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.jersey.api.client.Client;

public class DBMS {

	public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";

	private static final String MONGO_URL = "mongodb://localhost/userbase";

	/** URI to the MongoDB instance. */
	private static MongoClientURI connectionString = new MongoClientURI(MONGO_URL);

	/** Client to be used. */
	private static MongoClient mongoClient = new MongoClient(connectionString);

	/** Mongo database. */
	private static MongoDatabase database = mongoClient.getDatabase("userbase");

	/** Mongo Collection for accounts */

	/** Mongo Collection for tokens which belongs to a account */

	/**
	 *
	 * @param user
	 * @param contact
	 */
	public void addContact(String user, String contact) throws InvalidParameterException {
		MongoCollection<Document> accountCollection = database.getCollection("account");
		Document doc = accountCollection.find(eq("pseudonym", contact)).first();
		if (doc == null) {
			throw new InvalidParameterException();
		}

		MongoCollection<Document> contactCollection = database.getCollection("contact");
		Document checkContact = contactCollection.find(and(eq("pseudonym", user), eq("contact", contact))).first();
		if (checkContact == null) {
			Document newContact = new Document("pseudonym", user);
			newContact.append("contact", contact);
			contactCollection.insertOne(newContact);
		}
	}

	public String[] getContacts(String user) {
		MongoCollection<Document> contactCollection = database.getCollection("contact");
		FindIterable<Document> contacts = contactCollection.find(eq("pseudonym", user));
		long size = contactCollection.count(eq("pseudonym", user));
		String[] contString = new String[(int) size];
		int i = 0;
		for (Document cont : contacts) {
			contString[i] = cont.get("contact").toString();
			i++;
		}
		return contString;
	}

	public synchronized boolean createUser(String pseudonym, String password, String email) {
		MongoCollection<Document> accountCollection = database.getCollection("account");
		Document doc = accountCollection.find(eq("pseudonym", pseudonym)).first();

		if (doc != null) {
			throw new InvalidParameterException();
		}

		Document newDoc = new Document();
		String userPW = "";
		try {
			userPW = SecurityHelper.hashPassword(password);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}

		newDoc.append("pseudonym", pseudonym);
		newDoc.append("password", userPW);
		newDoc.append("email", email);
		accountCollection.insertOne(newDoc);

		MongoCollection<Document> tokenCollection = database.getCollection("token");
		Document tokenDoc = new Document();
		tokenDoc.append("pseudonym", pseudonym);
		tokenDoc.append("token", "save token");
		tokenDoc.append("expire-date", new Date());
		tokenCollection.insertOne(tokenDoc);
		return true;
	}

	public boolean checkToken(String username, String token) {
		try {
			if (token.equals(Cache.getCachedToken(username))) {
				return true;
			} else {
				String url = "http://localhost:5001";
				String input = "{\"token\": \"" + token + "\",\"pseudonym\": \"" + username + "\"}";
				String response;
				try {
					Client client = Client.create();
					response = client.resource(url + "/auth").accept("application/json").type("application/json")
							.post(String.class, input);
					client.destroy();
				} catch (Exception e) {
					return false;
				}
				JSONObject resp = new JSONObject(response);
				if (resp.get("success").equals("true")) {
					System.out.println("try cash");
					Cache.cacheToken(username, token, resp.getString("expire-date"));
					return true;
				} else {
					return false;
				}
			}
		} catch (JSONException e) {
			return false;
		}
	}

	public String getEmail(String pseudonym) {
		MongoCollection<Document> accountCollection = database.getCollection("account");
		// Get Account Collection
		Document doc = accountCollection.find(eq("pseudonym", pseudonym)).first();
		return doc.getString("email");
	}

	public void addMember(String user, String group) {
		MongoCollection<Document> memberCollection = database.getCollection("members");
		Document doc = memberCollection.find(and(eq("member", user), eq("group", group))).first();
		if (doc == null) {
			Document newDoc = new Document();
			newDoc.append("member", user);
			newDoc.append("group", group);
			memberCollection.insertOne(newDoc);
		}
	}

	String[] getGroups(String user){
		MongoCollection<Document> memberCollection = database.getCollection("members");
		FindIterable<Document> contacts = memberCollection.find(eq("member", user));
		long size = memberCollection.count(eq("member", user));
		String[] memberString = new String[(int) size];
		int i = 0;
		for (Document cont : contacts) {
			memberString[i] = cont.get("group").toString();
			i++;
		}
		return memberString;
	}
}
