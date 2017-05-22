package var;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

public class Main {
	static SelectorThread threadSelector;
	public static void main(String[] args) throws IllegalArgumentException, IOException {
		final String baseUri = "http://localhost:5002/";
		startGrizzly(baseUri);
		System.out.printf("Grizzly läuft unter %s%n", baseUri);
		System.out.println("[ENTER] drücken, um Grizzly zu beenden");
		System.in.read();
		stopGrizzly();
	}

	public static void startGrizzly(String baseUri) throws IllegalArgumentException, IOException {
		final String paket = "var";
		final Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("com.sun.jersey.config.property.packages", paket);
		System.out.println("Starte grizzly...");
		threadSelector = GrizzlyWebContainerFactory.create(
		baseUri, initParams);
	}

	public static void stopGrizzly() {
		threadSelector.stopEndpoint();
		System.out.println("Grizzly wurde beendet");
		System.exit(0);
	}
}