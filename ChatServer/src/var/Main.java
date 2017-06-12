package var;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

public class Main {

	public static void main(String[] args) throws IllegalArgumentException, IOException {

		String baseUri = "http://localhost:";
		String[] ports = {"4000", "4001", "4002"};
		final String paket = "var";
		final Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("com.sun.jersey.config.property.packages", paket);
		System.out.println("zeuge Grizzly Kinder...");
		for (String port : ports) {
			String uri = baseUri + port + "/";
			GrizzlyWebContainerFactory.create(uri, initParams);
			System.out.printf("Grizzls spielen unter %s%n", uri);
		}
	}
}