package var;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lte;

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

	    private static final String MONGO_URL = "mongodb://141.19.142.56/users";

		/** URI to the MongoDB instance. */
	    private static MongoClientURI connectionString =
	            new MongoClientURI(MONGO_URL);

	    /** Client to be used. */
	    private static MongoClient mongoClient = new MongoClient(connectionString);

	    /** Mongo database. */
	    private static MongoDatabase database = mongoClient.getDatabase("users");
	    
	    /** Mongo Collection for accounts */
	    private static MongoCollection<Document> accountCollection = database.getCollection("account");

	    /** Mongo Collection for tokens which belongs to a account */
	    private static MongoCollection<Document> tokenCollection = database.getCollection("token");
	    
	    /**
	     * @see var.chat.server.persistence.StorageProvider#retrieveAndUpdateSequence(java.lang.String)
	     */
	    public synchronized long retrieveAndUpdateSequence(String userId) {
	        MongoCollection<Document> sequences = database.getCollection(
	                "sequences");

	        Document seqDoc = sequences.find(eq("user", userId)).first();
	        long sequence = 1L;

	        if (seqDoc != null) {
	            sequence = seqDoc.getLong("sequence");
	            sequence++;
	            seqDoc.replace("sequence", sequence);
	            sequences.updateOne(eq("user", seqDoc.get("user")),
	                    new Document("$set", seqDoc));
	        }
	        else {
	            sequences.insertOne(new Document("sequence", sequence)
	                    .append("user", userId));
	        }

	        return sequence;
	    }

	    /**
	     * @see var.chat.server.persistence.StorageProvider#storeMessage(var.chat.server.domain.Message)
	     */
	    public synchronized void storeMessage(Message message) {
	        MongoCollection<Document> collection = database.getCollection("messages");

	        Document doc = new Document("from", message.from)
	                .append("to", message.to)
	                .append("date", message.date)
	                .append("sequence", message.sequence)
	                .append("text", message.text);

	        collection.insertOne(doc);
	    }

	    /**
	     * @see var.chat.server.persistence.StorageProvider#retrieveMessages(java.lang.String, long, boolean)
	     */
	    public synchronized List<Message> retrieveMessages(String userId,
	            long sequenceNumber, boolean deleteOldMessages) {

	        MongoCollection<Document> collection = database.getCollection("messages");

	        // Remove Messages with seq < provided seq no
	        if (deleteOldMessages) {
	            collection.deleteMany(and(lte("sequence", sequenceNumber), eq("to", userId)));
	        }

	        // Retreive remaining documents
	        List<Document> documents = new ArrayList<>();
	        collection.find(and(gt("sequence", sequenceNumber), eq("to", userId)))
	                .forEach((Block<Document>) e -> documents.add(e));

	        // No messages for user there
	        if (documents.isEmpty()) {
	            return null;
	        }

	        List<Message> messagesForUser = new ArrayList<>();
	        Collections.sort(messagesForUser, (a,b) -> (int) (b.sequence - a.sequence));

	        for (Document document : documents) {
	            Message m = new Message();
	            m.to = document.getString("to");
	            m.from = document.getString("from");
	            m.date = document.getString("date");
	            m.sequence = document.getLong("sequence");
	            m.text = document.getString("text");
	            messagesForUser.add(m);
	        }

	        return messagesForUser;
	    }

	    /**
	     * @see var.chat.server.persistence.StorageProvider#clearForTest()
	     */
	    public void clearForTest() {
	        database.getCollection("messages").drop();
	        database.getCollection("sequences").drop();
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
		
		public static void createUser(String pseudonym, String password, String email){
			FindIterable<Document> iterable = getAccountCollection().find();
			for(Document document : iterable){
				if(document.getString("pseudonym") == pseudonym){
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
			DBMS.getAccountCollection().insertOne(doc);
		}
}
