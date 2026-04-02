package ru.tet.jetty.basic;

import java.net.URI;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import ru.tet.jetty.handlers.DebugHandler;
import ru.tet.jetty.handlers.HelloHandler;

/**
 * Элементарный сервер.
 */
public class BasicServer8 {

	public static void main(String[] args) throws Exception {

		Server server = new Server();
		
		//Использовать первый доступный порт
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(0);
		server.addConnector(connector);

		Handler.Sequence handlerSequence = new Handler.Sequence();
		handlerSequence.addHandler(new DebugHandler());
		handlerSequence.addHandler(new HelloHandler("basic 1"));

		server.setHandler(handlerSequence);

		server.start();
		
		//вывести uri сервера
        URI serverUri = server.getURI().resolve("/");
        System.out.println("serverUri: "+serverUri);
		
		server.join();

		
	}

}
