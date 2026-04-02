package ru.tet.jetty.basic;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;

import ru.tet.jetty.handlers.DebugHandler;
import ru.tet.jetty.handlers.HelloHandler;

/**
 * Элементарный сервер.
 */
public class BasicServer1 {

	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8081);
		
        ContextHandlerCollection contextsHandlers = new ContextHandlerCollection();

        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(contextsHandlers);
        handlerSequence.addHandler(new DebugHandler());
        handlerSequence.addHandler(new HelloHandler("basic 1"));
        handlerSequence.addHandler(new DefaultHandler());
		
        server.setHandler(handlerSequence);
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}
