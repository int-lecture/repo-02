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

/**
 * Storage provider for a MongoDB.
 * 
 * @author Thomas Smits
 */
class StorageProviderMongoDB {

	private static final String MONGO_URL = "mongodb://141.19.142.56/chat";

	/** URI to the MongoDB instance. */
	private static MongoClientURI connectionString = new MongoClientURI(MONGO_URL);

	/** Client to be used. */
	private static MongoClient mongoClient = new MongoClient(connectionString);

	/** Mongo database. */
	private static MongoDatabase database = mongoClient.getDatabase("chat");
	
	
	private static final String MONGO_URL2 = "mongodb://141.19.142.56/userbase";

	/** URI to the MongoDB instance. */
	private static MongoClientURI connectionString2 = new MongoClientURI(MONGO_URL2);

	/** Client to be used. */
	private static MongoClient mongoClient2 = new MongoClient(connectionString2);

	/** Mongo database. */
	private static MongoDatabase database2 = mongoClient.getDatabase("userbase");
	

	/**
	 * @param group
	 * @see var.chat.server.persistence.StorageProvider#retrieveAndUpdateSequence(java.lang.String)
	 */
	public synchronized long retrieveAndUpdateSequence(String userId, String recieverId, boolean group) {
		MongoCollection<Document> sequences = database.getCollection("sequences");
		Document seqDoc;
		long sequence = 1L;
		if (group) {
			seqDoc = sequences.find(eq("recieverId", recieverId)).first();
			if (seqDoc == null) {
				sequences.insertOne(
						new Document("sequence", sequence).append("recieverId", recieverId));
			}
			sequence = seqDoc.getLong("sequence");
			sequence++;
			seqDoc.replace("sequence", sequence);
			seqDoc.remove("_id");
			sequences.updateOne(eq("recieverId", recieverId), new Document("$set", seqDoc));
		} else {
			seqDoc = sequences.find(and(eq("user", userId), eq("recieverId", recieverId))).first();
			if (seqDoc != null) {
				sequence = seqDoc.getLong("sequence");
				sequence++;
				seqDoc.replace("sequence", sequence);
				seqDoc.remove("_id");
				sequences.updateOne(and(eq("user", seqDoc.get("user")), eq("recieverId", recieverId)),
						new Document("$set", seqDoc));
			} else {
				sequences.insertOne(
						new Document("sequence", sequence).append("user", userId).append("recieverId", recieverId));
			}
		}
		return sequence;
	}

	/**
	 * @see var.chat.server.persistence.StorageProvider#storeMessage(var.chat.server.domain.Message)
	 */
	public synchronized void storeMessage(Message message) {
		MongoCollection<Document> collection = database.getCollection("messages");
		Document doc = new Document("from", message.getFrom()).append("to", message.getTo())
				.append("date", message.getDate()).append("sequence", message.getSequence())
				.append("text", message.getText()).append("group",message.getGroup());
		collection.insertOne(doc);
	}

	
	
	/**
	 * @see var.chat.server.persistence.StorageProvider#retrieveMessages(java.lang.String,
	 *      long, boolean)
	 */
	public synchronized List<Message> retrieveAll(String userId, long sequenceNumber, boolean deleteOldMessages) {
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
		Collections.sort(messagesForUser, (a, b) -> (int) (b.getSequence() - a.getSequence()));
		for (Document document : documents) {
			Message m = new Message();
			m.setTo(document.getString("to"));
			m.setForm(document.getString("from"));
			m.setDate(document.getString("date"));
			m.setSequence(document.getLong("sequence"));
			m.setText(document.getString("text"));
			if(document.getBoolean("group") != null){
				m.setGroup(document.getBoolean("group"));
			} else {
				m.setGroup(false);
			}
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

	public List<Message> retrieveMessages(String username, int seqRecieved, boolean b) {
		List<Message> messages = new ArrayList<Message>();
		
		List<Message> neu = retrieveAll(username, seqRecieved, b);
		if(neu != null){
			messages.addAll(neu);
		}
		
		MongoCollection<Document> memberCollection = database2.getCollection("members");
		FindIterable<Document> contacts = memberCollection.find(eq("member", username));
		
		for (Document cont : contacts) {
			neu = retrieveAll(cont.get("group").toString(), 0, false);
			if(neu != null){
				messages.addAll(neu);
			}
		}
		return messages;
	}
}