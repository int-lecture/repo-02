package var;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBMS {

	    private static final String MONGO_URL = "mongodb://141.19.142.56/userbase";

		/** URI to the MongoDB instance. */
	    private static MongoClientURI connectionString =
	            new MongoClientURI(MONGO_URL);

	    /** Client to be used. */
	    private static MongoClient mongoClient = new MongoClient(connectionString);

	    /** Mongo database. */
	    private static MongoDatabase database = mongoClient.getDatabase("userbase");

	    /** Mongo Collection for accounts */
	    private static MongoCollection<Document> accountCollection = database.getCollection("account");

	    /** Mongo Collection for tokens which belongs to a account */
	    private static MongoCollection<Document> tokenCollection = database.getCollection("token");




	    void addContact(String user, String contact){
		    MongoCollection<Document> contactCollection = database.getCollection("contact");
	    	Document checkContact = contactCollection.
	    			find(and(eq("pseudonym", user),
	    					eq("contact",contact)))
	    					.first();
	    	if (checkContact != null){
	    		Document newContact = new Document("pseudonym", user);
	    		newContact.append("contact", contact);
	    		contactCollection.insertOne(newContact);
	    	}
	    }

	    String[] getContacts(String user){
		    MongoCollection<Document> contactCollection = database.getCollection("contact");
	    	FindIterable<Document> contacts = contactCollection.find(eq("pseudonym", user));
	    	@SuppressWarnings("unchecked")
			long size = ((MongoCollection<Document>) contactCollection.find(eq("pseudonym", user))).count();
	    	String[] contString = new String[(int) size];
	    	int i = 0;
	    	for (Document cont : contacts) {
				contString[i] = cont.get("contact").toString();
				i++;
			}
		return contString;
	    }



	    /**
	     * @return collection of tokens which belongs to a account
	     */
		public static MongoCollection<Document> getTokenCollection() {
			return tokenCollection;
		}

		/**
		 * @return collection of accounts
		 */
		public static MongoCollection<Document> getAccountCollection() {
			return accountCollection;
		}
}
