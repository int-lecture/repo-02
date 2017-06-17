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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


/**
 * Storage provider for a MongoDB.
 * @author Thomas Smits
 */
class StorageProviderMongoDB {

    private static final String MONGO_URL = "mongodb://141.19.142.56/chat";

	/** URI to the MongoDB instance. */
    private static MongoClientURI connectionString =
            new MongoClientURI(MONGO_URL);

    /** Client to be used. */
    private static MongoClient mongoClient = new MongoClient(connectionString);

    /** Mongo database. */
    private static MongoDatabase database = mongoClient.getDatabase("chat");

    /**
     * @see var.chat.server.persistence.StorageProvider#retrieveAndUpdateSequence(java.lang.String)
     */
    public synchronized long retrieveAndUpdateSequence(String userId, String recieverId) {
        MongoCollection<Document> sequences = database.getCollection(
                "sequences");

        Document seqDoc = sequences.find(and(eq("user", userId),eq("recieverId", recieverId))).first();
        long sequence = 1L;
        if (seqDoc != null) {
            sequence = seqDoc.getLong("sequence");
            sequence++;
            seqDoc.replace("sequence", sequence);
            sequences.updateOne(and(eq("user", seqDoc.get("user")),eq("recieverId", recieverId)),
                    new Document("$set", seqDoc));
        } else {
            sequences.insertOne(new Document("sequence", sequence)
                    .append("user", userId)
                    .append("recieverId", recieverId));
        }
        return sequence;
    }

    /**
     * @see var.chat.server.persistence.StorageProvider#storeMessage(var.chat.server.domain.Message)
     */
    public synchronized void storeMessage(Message message) {
        MongoCollection<Document> collection = database.getCollection("messages");

        Document doc = new Document("from", message.getFrom())
                .append("to", message.getTo())
                .append("date", message.getDate())
                .append("sequence", message.getSequence())
                .append("text", message.getText());

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
        Collections.sort(messagesForUser, (a,b) -> (int) (b.getSequence() - a.getSequence()));
        for (Document document : documents) {
            Message m = new Message();
            m.setTo(document.getString("to"));
            m.setForm(document.getString("from"));
            m.setDate(document.getString("date"));
            m.setSequence(document.getLong("sequence"));
            m.setText(document.getString("text"));
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
}