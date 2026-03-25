package examples;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.ContentResponse;
import org.eclipse.jetty.client.transport.HttpClientConnectionFactory;
import org.eclipse.jetty.client.transport.HttpClientTransportDynamic;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.transport.ClientConnectionFactoryOverHTTP2;
import org.eclipse.jetty.io.ClientConnectionFactory;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class ClientWithDynamicConnection {
	public static void main(String[] args) throws Exception {
		SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
		sslContextFactory.setIncludeProtocols("TLSv1.3");
		ClientConnector clientConnector = new ClientConnector();
		clientConnector.setSslContextFactory(sslContextFactory);

		ClientConnectionFactory.Info http1Info = HttpClientConnectionFactory.HTTP11;
		HTTP2Client http2Client = new HTTP2Client(clientConnector);
		ClientConnectionFactoryOverHTTP2.HTTP2 http2Info = new ClientConnectionFactoryOverHTTP2.HTTP2(http2Client);
		HttpClientTransportDynamic dynamicTransport =
				new HttpClientTransportDynamic(clientConnector, http1Info, http2Info);

		HttpClient httpClient = new HttpClient(dynamicTransport);
		try {
			httpClient.start();
			// To see the SslContextFactory configuration, dump the client
			System.out.printf("Dump of client: %s%n", httpClient.dump());
			ContentResponse res = httpClient.GET("https://api.github.com/zen");
			System.out.printf("response status: %d%n", res.getStatus());
			res.getHeaders().forEach((field) -> {
				System.out.printf("response header [%s]: %s%n", field.getName(), field.getValue());
			});
			System.out.printf("response body: %s%n", res.getContentAsString());
		} finally {
			httpClient.stop();
		}
	}
}
