package ru.tet.jetty.advanced;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.SecuredRedirectHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import ru.tet.jetty.handlers.HelloHandler;

/**
 * Demonstrates how to enable the JVM handling of Client Certificates.
 *
 * @see javax.net.ssl.SSLEngine#setWantClientAuth(boolean)
 * @see javax.net.ssl.SSLEngine#setNeedClientAuth(boolean)
 */
public class EnableClientCertificates {
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		int httpsPort = 8443;

		// Setup HTTP Connector
		HttpConfiguration httpConf = new HttpConfiguration();
		httpConf.setSecurePort(httpsPort);
		httpConf.setSecureScheme("https");

		// Setup SSL
		SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
		sslContextFactory.setKeyStoreResource(findKeyStore(ResourceFactory.of(server)));
		sslContextFactory.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
		sslContextFactory.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");
		sslContextFactory.setWantClientAuth(true); // Turn on javax.net.ssl.SSLEngine.wantClientAuth
		sslContextFactory.setNeedClientAuth(true); // Turn on javax.net.ssl.SSLEngine.needClientAuth

		// Setup HTTPS Configuration
		HttpConfiguration httpsConf = new HttpConfiguration();
		httpsConf.setSecureScheme("https");
		httpsConf.setSecurePort(httpsPort);
		httpsConf.addCustomizer(new SecureRequestCustomizer()); // adds ssl info to request object

		// Establish the HTTPS ServerConnector
		ServerConnector httpsConnector =
				new ServerConnector(server,
						new SslConnectionFactory(sslContextFactory, "http/1.1"),
						new HttpConnectionFactory(httpsConf));
		httpsConnector.setPort(httpsPort);

		server.addConnector(httpsConnector);

		// Add a Handlers for requests
		Handler.Sequence handlers = new Handler.Sequence();
		handlers.addHandler(new SecuredRedirectHandler());
		handlers.addHandler(new HelloHandler("Hello Secure World"));
		server.setHandler(handlers);

		server.start();
		server.join();
	}

	private static Resource findKeyStore(ResourceFactory resourceFactory) {
		String resourceName = "ssl/keystore";
		Resource resource = resourceFactory.newClassLoaderResource(resourceName);
		if (!Resources.isReadableFile(resource)) {
			throw new RuntimeException("Unable to read " + resourceName);
		}
		return resource;
	}
}
