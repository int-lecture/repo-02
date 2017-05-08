package var;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;


public class Menu {
	private static LinkedList<String[]> messageList = new LinkedList<>();
	private static int seqCounter = 0;
	public static void main(String[] args) throws IllegalArgumentException, IOException {
		final String baseUri = "http://localhost:5000/";
		final String paket = "var";
		final Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("com.sun.jersey.config.property.packages", paket);
		System.out.println("Starte grizzly...");
		SelectorThread threadSelector = GrizzlyWebContainerFactory.create(
		baseUri, initParams);
		System.out.printf("Grizzly läuft unter %s%n", baseUri);
		System.out.println("[ENTER] drücken, um Grizzly zu beenden");
		System.in.read();
		threadSelector.stopEndpoint();
		System.out.println("Grizzly wurde beendet");
		System.exit(0);
	}
	static LinkedList<String[]> getMessageList() {
		return messageList;
	}

	static int getSeqCounter() {
		return seqCounter;
	}
	static void incSeqCounter() {
		seqCounter++;
	}

}