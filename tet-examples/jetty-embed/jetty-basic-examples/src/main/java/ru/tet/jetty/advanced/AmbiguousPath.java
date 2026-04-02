package ru.tet.jetty.advanced;

import java.nio.file.Path;

import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.http.UriCompliance;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;

import ru.tet.jetty.DebugServlet;
import ru.tet.jetty.handlers.DebugHandler;
import ru.tet.jetty.handlers.HelloHandler;

public class AmbiguousPath {

	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server();

		
        HttpConfiguration httpConfig = new HttpConfiguration();
        UriCompliance uriCompliance = UriCompliance.LEGACY.with("custom", UriCompliance.Violation.SUSPICIOUS_PATH_CHARACTERS);
        httpConfig.setUriCompliance(uriCompliance);

        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfig);
        ServerConnector connector = new ServerConnector(server, httpConnectionFactory);
        connector.setPort(8081);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        
        // Allow ServletContext to accept ambiguous uris.
        context.getServletHandler().setDecodeAmbiguousURIs(true);
        context.addServlet(DebugServlet.class, "/*");

        ContextHandlerCollection contextsHandlers = new ContextHandlerCollection();
        contextsHandlers.addHandler(context);
        
        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(contextsHandlers);
        handlerSequence.addHandler(new DebugHandler());
//        handlerSequence.addHandler(new HelloHandler("basic 1"));
        handlerSequence.addHandler(new DefaultHandler());
        
        server.setHandler(handlerSequence);
        
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}