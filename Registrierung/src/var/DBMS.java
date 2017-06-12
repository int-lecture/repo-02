package var;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class DBMS {

	public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";


	private static final String MONGO_URL = "mongodb://141.19.142.56/userbase";

	/** URI to the MongoDB instance. */
	private static MongoClientURI connectionString = new MongoClientURI(MONGO_URL);

	/** Client to be used. */
	private static MongoClient mongoClient = new MongoClient(connectionString);

	/** Mongo database. */
	private static MongoDatabase database = mongoClient.getDatabase("userbase");

	/** Mongo Collection for accounts */

	/** Mongo Collection for tokens which belongs to a account */

	public void addContact(String user, String contact) {
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

	public void createUser(String pseudonym, String password, String email) {
		MongoCollection<Document> accountCollection = database.getCollection("account");
		Document doc = accountCollection.find(eq("pseudonym", pseudonym)).first();

		if (doc != null) {
			throw new InvalidParameterException();
		}

		Document newDoc = new Document();
		newDoc.append("pseudonym", pseudonym);
		String userPW = "";
		try {
			userPW = SecurityHelper.hashPassword(password);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		newDoc.append("password", userPW);
		newDoc.append("email", email);
		accountCollection.insertOne(newDoc);

		MongoCollection<Document> tokenCollection = database.getCollection("token");
		Document tokenDoc = new Document();
		tokenDoc.append("pseudonym", pseudonym);
		tokenDoc.append("token", "save token");
		tokenDoc.append("expire-date", new Date());
		tokenCollection.insertOne(tokenDoc);
	}

	public boolean checkToken(String pseudonym, String token) {
		String url = "http://141.19.142.56:5001/auth";
		Client client = Client.create();

		WebResource webResource = client
		   .resource(url);

		String input = "{\"token\": \"" + token + "\",\"pseudonym\": \"" + pseudonym + "\"}";
		System.out.println(input);

		ClientResponse response = webResource.type("application/json")
		   .post(ClientResponse.class, input);

		if (response.getStatus() != 200) {
			System.out.println(response.getStatus());
			return false;
		}

		return true;
	}


	public String getEmail(String pseudonym) {
		MongoCollection<Document> accountCollection = database.getCollection("account");
		// Get Account Collection
		Document doc = accountCollection.find(eq("pseudonym", pseudonym)).first();
		return doc.getString("email");
	}

	public void clearForTest() {
		database.getCollection("account").drop();
		database.getCollection("contact").drop();
		database.getCollection("token").drop();
		System.out.println("Cleared");
	}
}
