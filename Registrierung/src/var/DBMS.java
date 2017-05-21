package var;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBMS {

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
		FindIterable<Document> iterable = accountCollection.find();
		for (Document document : iterable) {
			if (document.getString("pseudonym") == pseudonym) {
				throw new InvalidParameterException();
			}
		}
		Document doc = new Document();
		doc.append("pseudonym", pseudonym);
		String userPW = "";
		try {
			userPW = SecurityHelper.hashPassword(password);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		doc.append("password", userPW);
		doc.append("email", email);
		accountCollection.insertOne(doc);
	}

	public boolean checkToken(String pseudonym, String token) {
		MongoCollection<Document> tokenCollection = database.getCollection("token");
		// Get Token Collection
		try {
			if (tokenCollection.find(eq("pseudonym", pseudonym)).first() == null
					&& tokenCollection.find(eq("token", token)).first() == null) {
				throw new InvalidParameterException();
			}
		} catch (InvalidParameterException e) {
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
