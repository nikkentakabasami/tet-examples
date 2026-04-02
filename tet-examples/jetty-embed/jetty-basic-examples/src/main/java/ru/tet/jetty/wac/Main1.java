package ru.tet.jetty.wac;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;

import ru.tet.jetty.utils.TetJettyUtils;

/**
 * Объединяем статический контент из classpath через 
 * context.setBaseResource(ResourceFactory.combine(r1, r2));
 * 
 * 
 */
public class Main1 {

	public static void main(String[] args) throws Exception {

		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8082);
		server.addConnector(connector);

		WebAppContext context = new WebAppContext();
		context.setContextPath("/");
//		context.setWelcomeFiles(new String[] { "r2index.html" });

		ResourceFactory resourceFactory = ResourceFactory.of(context);

		Resource r1 = TetJettyUtils.getClassLoaderResource(resourceFactory, "static-root1");
		Resource r2 = TetJettyUtils.getClassLoaderResource(resourceFactory, "static-root2");
		
		context.setBaseResource(ResourceFactory.combine(r1, r2));

		Handler.Sequence handlers = new Handler.Sequence(context,new DefaultHandler());
        server.setHandler(handlers);

		server.start();
		server.join();

	}

}
