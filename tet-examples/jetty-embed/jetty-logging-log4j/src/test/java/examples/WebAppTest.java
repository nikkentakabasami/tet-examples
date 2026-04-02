package examples;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.tet.jetty.handlers.HelloHandlerWithLog;

public class WebAppTest {
	private static Server server;
	private static URI serverUri;

	@BeforeEach
	public void startServer() throws Exception {

		server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(0); // let it use whatever port thats free
		server.addConnector(connector);
		server.setHandler(new HelloHandlerWithLog("Hello Basic"));
		
		server.start();
		serverUri = server.getURI().resolve("/");
	}

	@AfterEach
	public void stopServer() {
		LifeCycle.stop(server);
	}

	@Test
	public void testGet() throws IOException {
		URI getURI = serverUri.resolve("/");

		String loggername = "examples";
		CaptureHandler capture = CaptureHandler.attach(loggername);

		HttpURLConnection connection = (HttpURLConnection) getURI.toURL().openConnection();
		System.out.println("open connection for:"+connection.getURL());
		assertThat("Connection.statusCode", connection.getResponseCode(), is(HttpURLConnection.HTTP_OK));
		try (InputStream in = connection.getInputStream()) {
			String response = IO.toString(in);
			System.out.println("response:"+response);
			assertThat("response", response, containsString("Hello"));
			capture.assertContainsRecord("HelloHandlerWithLog", String.format("Got request for %s", getURI));
		} finally {
			capture.detach(loggername);
		}
	}
}
