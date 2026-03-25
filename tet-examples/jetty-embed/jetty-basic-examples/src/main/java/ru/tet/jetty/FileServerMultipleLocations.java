package ru.tet.jetty;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

/**
 * Использование ServletContextHandler для обслуживания статического контента из 
 * разных локаций.
 *  
 * context.setBaseResource(r); - подключение статических ресурсов (из класспаза)
 * ResourceServlet - позволяет подключить дополнительно статические ресурсы из доп. локаций.
 *
 */
public class FileServerMultipleLocations {
	public static void main(String[] args) throws Exception {
		
		Path altPath = Paths.get("../../web-apps/web-static-roots/src/webapps/alt-root/").toRealPath();
		
		System.err.println("Alt Base Resource is " + altPath);

		Server server = FileServerMultipleLocations.newServer(8081, altPath);
		server.start();
		server.join();
	}

	public static Server newServer(int port, Path altPath) throws FileNotFoundException {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		server.addConnector(connector);

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
//		context.setContextPath("/test");
		
		//статические ресурсы из класспаза
		ResourceFactory resourceFactory = ResourceFactory.of(context);
		Resource baseResource = resourceFactory.newClassLoaderResource("static-root2");
		if (!Resources.isReadableDirectory(baseResource))
			throw new FileNotFoundException("Unable to find base-resource for [static-root2]");
		context.setBaseResource(baseResource);
		
		
		context.setWelcomeFiles(new String[] { "index.html", "index.htm", "foo.htm" });
		server.setHandler(context);

		//статические ресурсы файловой системы
		ServletHolder holderAlt = new ServletHolder("static-alt", ResourceServlet.class);
		holderAlt.setInitParameter("baseResource", altPath.toUri().toASCIIString());
//		holderAlt.setInitParameter("dirAllowed", "true");
//		holderAlt.setInitParameter("pathInfoOnly", "true");
		context.addServlet(holderAlt, "/alt/*");

		//Для обслуживания статического контента из context.baseResource
		ServletHolder holderDef = new ServletHolder("default", DefaultServlet.class);
//		holderDef.setInitParameter("dirAllowed", "true");
		context.addServlet(holderDef, "/");

		return server;
	}
}
