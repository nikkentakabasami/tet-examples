package ru.tet.jetty.basic;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;

import ru.tet.jetty.handlers.DebugHandler;
import ru.tet.jetty.handlers.HelloHandler;

/**
 * Элементарный сервер.
 */
public class BasicServer6_HttpConfiguration {

	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server();
		
        HttpConfiguration httpConfigOff = new HttpConfiguration();
        httpConfigOff.setSendDateHeader(false);
        httpConfigOff.setSendServerVersion(false);
        httpConfigOff.setSendXPoweredBy(false);

        ServerConnector connectorOff = new ServerConnector(server, new HttpConnectionFactory(httpConfigOff));
        connectorOff.setPort(8081);
        server.addConnector(connectorOff);

        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(new DebugHandler());
        handlerSequence.addHandler(new HelloHandler("basic 1"));
		
		
        server.setHandler(handlerSequence);
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}
