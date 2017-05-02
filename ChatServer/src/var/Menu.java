package var;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;


public class Menu {

	
	public static void main(String[] args) throws IllegalArgumentException, IOException {
		final String baseUri = "http://localhost:9998/";
		final String paket = "var.chatserver";
		final Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("com.sun.jersey.config.property.packages", paket);
		System.out.println("Starte grizzly...");
		SelectorThread threadSelector = GrizzlyWebContainerFactory.create(
		baseUri, initParams);
		System.out.printf("Grizzly l�uft unter %s%n", baseUri);
		System.out.println("[ENTER] dr�cken, um Grizzly zu beenden");
		System.in.read();
		threadSelector.stopEndpoint();
		System.out.println("Grizzly wurde beendet");
		System.exit(0);
	}
	
	
}