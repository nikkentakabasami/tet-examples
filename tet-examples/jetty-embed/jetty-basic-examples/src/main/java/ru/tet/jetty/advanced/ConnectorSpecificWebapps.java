package ru.tet.jetty.advanced;

import java.util.List;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

/**
 * 
 * Свой коннектор для каждого веб-приложения.
 * localhost:8080/a
 * localhost:9090/b
 * 
 */
public class ConnectorSpecificWebapps {
	public static void main(String[] args) throws Exception {
		Server server = new Server();

		ServerConnector connectorA = new ServerConnector(server);
		connectorA.setPort(8080);
		connectorA.setName("connA"); // Give the connector a name
		ServerConnector connectorB = new ServerConnector(server);
		connectorB.setPort(9090);
		connectorB.setName("connB"); // Give the connector a name

		server.addConnector(connectorA);
		server.addConnector(connectorB);

		// Basic handler collection
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		server.setHandler(contexts);

		// WebApp A
		WebAppContext appA = new WebAppContext();
		appA.setContextPath("/a");
		appA.setWar("../../web-apps/web-static-roots/src/webapps/webapp-a/");

		appA.setVirtualHosts(List.of("@connA")); // Reference connector name
		contexts.addHandler(appA);

		// WebApp B
		WebAppContext appB = new WebAppContext();
		appB.setContextPath("/b");
		appB.setWar("../../web-apps/web-static-roots/src/webapps/webapp-b/");
		appB.setVirtualHosts(List.of("@connB")); // Reference connector name
		contexts.addHandler(appB);

		server.start();
		server.join();
	}
}
