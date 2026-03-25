package ru.tet.jetty;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jetty.compression.gzip.GzipCompression;
import org.eclipse.jetty.compression.server.CompressionConfig;
import org.eclipse.jetty.compression.server.CompressionHandler;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

/**
 * Использование HTTP compression.
 * Клиент отправляет заголовок "Accept-Encoding: gzip, deflate".
 * А сервер, упаковав содержимое, добавляет заголовок "Content-Encoding: gzip"
 * 
 * 
 */
public class GzipRequestResponseExample {
	public static void main(String[] args) throws Exception {
		int port = 8080;
		Server server = newServer(port);

		server.start();
		server.join();
	}

	public static Server newServer(int port) throws IOException {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		server.addConnector(connector);

		CompressionHandler compressionHandler = new CompressionHandler();
		compressionHandler.putCompression(new GzipCompression());

		CompressionConfig compressionConfig =
				CompressionConfig.builder()
						.compressIncludeMethod("GET")
						.compressIncludeMethod("POST")
						.compressIncludeMimeType("text/plain")
						.compressIncludeMimeType("text/css")
						.compressIncludeMimeType("text/html")
						.compressIncludeMethod("application/javascript")
						.build();

		compressionHandler.putConfiguration("/*", compressionConfig);
		server.setHandler(compressionHandler);

		ContextHandlerCollection handlers = new ContextHandlerCollection();
		compressionHandler.setHandler(handlers);

		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		Resource baseResource = ResourceFactory.of(context).newClassLoaderResource("static-root1/");
		if (!Resources.isReadableDirectory(baseResource))
			throw new FileNotFoundException("Unable to find classloader resource static-root/");

		context.setBaseResource(baseResource);
		context.setWelcomeFiles(new String[] { "index.html" });

		// Adding Servlets
		ServletHolder defaultHolder = new ServletHolder("default", DefaultServlet.class);
		context.addServlet(defaultHolder, "/"); // always on default url-pattern of "/"

		handlers.addHandler(context);
		return server;
	}
}
