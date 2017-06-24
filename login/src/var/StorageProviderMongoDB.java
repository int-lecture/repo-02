package var;

import static com.mongodb.client.model.Filters.eq;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.mongodb.client.model.Filters.and;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Storage provider for a MongoDB.
 */
class StorageProviderMongoDB {

	static final String dbName = "userbase";
	static final String dbAccount = "account";
	static final String dbToken = "token";
	static final boolean allowEmailLogin = true;

	private static final String MONGO_URL = "mongodb://localhost:27017";
	/** URI to the MongoDB instance. */
	private static MongoClientURI connectionString = new MongoClientURI(MONGO_URL);

	/** Client to be used. */
	private static MongoClient mongoClient = new MongoClient(connectionString);

	/** Mongo database. */
	private static MongoDatabase database = mongoClient.getDatabase(dbName);

	/**
	 * Retrieves a user's info from the database. If Config.allowEmailLogin is
	 * true pseudonym can be null.
	 *
	 * @param username
	 *            The user's email.
	 * @param pseudonym
	 *            The user's pseudonym.
	 * @return Returns the user's data or null if the user wasn't found.
	 */
	static User retrieveUser(String username, String pseudonym) {
		MongoCollection<Document> collection = database.getCollection(dbAccount);
		Document doc;
		// Retrieve the hashed password
		if (pseudonym == null && allowEmailLogin) {
			doc = collection.find(eq("email", username)).first();
		} else {
			doc = collection.find(and(eq("email", username), eq("pseudonym", pseudonym))).first();
		}
		if (doc == null) {
			return null;
		}
		return new User(username, doc.getString("password"), doc.getString("pseudonym"), true);
	}

	/**
	 *
	 */
	static void saveToken(String token, String expirationDate, String pseudonym) {
		MongoCollection<Document> collection = database.getCollection(dbToken);

		// add user to database
		Document doc = new Document("token", "" + token + "").append("expire-date", expirationDate).append("pseudonym",
				pseudonym);

		if (collection.find(eq("pseudonym", pseudonym)).first() != null) {
			collection.updateOne(eq("pseudonym", pseudonym), new Document("$set", doc));
		} else {
			collection.insertOne(doc);
		}
	}

	/**
	 * Fetches a user's current Token from the database. If the token is not
	 * found or expired null is returned.
	 *
	 * @param pseudonym
	 *            The user's pseudonym.
	 * @param token
	 *            The user's current token.
	 * @return The token's expiration date or null if the token was not found or
	 *         is expired.
	 */
	static Date retrieveToken(String pseudonym, String token) {
		MongoCollection<Document> collection = database.getCollection(dbToken);
		// Retreive the tokeninformation
		Document doc = collection.find(and(eq("pseudonym", pseudonym), eq("token", token))).first();
		if (doc == null) {
			return null;
		}
		String expDateString = doc.getString("expire-date");
		if (expDateString == null) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(Main.ISO8601);
		Date expireDate;
		try {
			expireDate = sdf.parse(expDateString);
		} catch (Exception e) {
			// The token seems to be corrupted.
			collection.deleteOne(and(eq("pseudonym", pseudonym), eq("token", token)));
			return null;
		}

		if (Calendar.getInstance().getTime().before(expireDate))
			return expireDate;
		else
			return null;
	}

	/**
	 *
	 */
	static void deleteToken(String token) {
		MongoCollection<Document> collection = database.getCollection(dbToken);
		collection.deleteOne(eq("token", token));
	}

	// public static void clearForTest(User[] newUsers) {
	// MongoCollection<Document> collection =
	// deleteCollection(Config.getSettingValue(Config.dbAccountCollection));
	// deleteCollection(Config.getSettingValue(Config.dbTokenCollection));
	//
	// for (User u :
	// newUsers) {
	// Document doc = new Document();
	// doc.append("user", u.email);
	// doc.append("pseudonym", u.pseudonym);
	// doc.append("password", u.getSecurePassword());
	// collection.insertOne(doc);
	// }
	// }
}