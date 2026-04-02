package ru.tet.jetty.basic;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

import ru.tet.jetty.handlers.ConditionalHandler;
import ru.tet.jetty.handlers.DebugHandler;
import ru.tet.jetty.handlers.HelloHandler;

/**
 * Элементарный сервер.
 */
public class BasicServer7_handlerwrapper {

	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8081);
		

        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(new DebugHandler());
        handlerSequence.addHandler(new ConditionalHandler(new HelloHandler("wrapped hello. throws exception on http://localhost:8081/wrong")));
        
        
        server.setHandler(handlerSequence);
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}
