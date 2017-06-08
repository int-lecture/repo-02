package var;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

public class Main {

	public static void main(String[] args) throws IllegalArgumentException, IOException {
		final String baseUri = "http://localhost:5000/";
		final String paket = "var";
		final Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("com.sun.jersey.config.property.packages", paket);
		System.out.println("Starte grizzly...");
		GrizzlyWebContainerFactory.create(baseUri, initParams);
		System.out.printf("Grizzly läuft unter %s%n", baseUri);
	}
}