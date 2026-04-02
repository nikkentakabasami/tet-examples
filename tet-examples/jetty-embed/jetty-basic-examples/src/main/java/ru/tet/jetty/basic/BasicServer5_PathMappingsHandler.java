package ru.tet.jetty.basic;

import org.eclipse.jetty.http.pathmap.PathSpec;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.PathMappingsHandler;

import ru.tet.jetty.handlers.DebugHandler;
import ru.tet.jetty.handlers.HelloHandler;

/**
 * Элементарный сервер.
 */
public class BasicServer5_PathMappingsHandler {

	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8081);
		
        
        
        PathMappingsHandler pathMappingsHandler = new PathMappingsHandler();
        pathMappingsHandler.addMapping(PathSpec.from("/query1"), new HelloHandler("handler for query1"));
        pathMappingsHandler.addMapping(PathSpec.from("/query2"), new HelloHandler("handler for query2"));
        
        
        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(new DebugHandler());
        handlerSequence.addHandler(pathMappingsHandler);
        handlerSequence.addHandler(new HelloHandler("go to  http://localhost:8081/query1"));
		
        server.setHandler(handlerSequence);
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}
