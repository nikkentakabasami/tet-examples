package ru.tet.jetty.basic;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;

import ru.tet.jetty.handlers.HelloHandler;

/**
 * Файловый сервер на основе ResourceHandler с ресурсами из classpath-а
 */
public class BasicServer4_ResourceHandler {
	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8081);
		

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(true);
        
        ResourceFactory resourceFactory = ResourceFactory.of(server);
        Resource resources = ResourceFactory.combine(
        		resourceFactory.newClassLoaderResource("static-root1"),
        		resourceFactory.newClassLoaderResource("static-root2"));
        resourceHandler.setBaseResource(resources);
        
        
        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(resourceHandler);
//        handlerSequence.addHandler(new DefaultHandler());	//для вывода списка контекстов в корне сервера
        handlerSequence.addHandler(new HelloHandler("basic 2"));
		
        server.setHandler(handlerSequence);
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}
