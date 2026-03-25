package examples;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * Клиентские запросы через java.net.http.HttpClient
 * 
 */
public class SimpleClient {

	public static void main(String[] args) throws Exception {

		test1();

	}

	public static void test1() throws Exception {
		
		
		URI uri = new URI("http://www.google.com/");

		//        URI uri = server.getURI().resolve("/rest/test");

		HttpClient httpClient = HttpClient.newHttpClient();
		HttpRequest request =
				HttpRequest.newBuilder()
						.uri(uri)
						.GET()
						.header("Accept", "application/json")
						.build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println("Response statuscode: " + response.statusCode());
		System.out.println("Response body: " + response.body());
		
	}
	
	
	public static void test2() throws Exception {

		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request =
				HttpRequest.newBuilder()
						.uri(URI.create("http://minidev:7072/enettc-web/v2/lib/moment/moment.js"))
						.GET()
						.version(HttpClient.Version.HTTP_2).build();

		CompletableFuture<Void> futureResponse =
				client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
						.thenAccept(resp -> {
							System.out.println("Response statuscode: " + resp.statusCode());
							System.out.println("Response body: " + resp.body());
						});
		//подождём получения
		futureResponse.get();

	}

}
