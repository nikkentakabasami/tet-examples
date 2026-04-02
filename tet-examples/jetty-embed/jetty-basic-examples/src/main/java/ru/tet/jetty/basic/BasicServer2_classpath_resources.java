package ru.tet.jetty.basic;

import java.io.FileNotFoundException;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

import ru.tet.jetty.handlers.HelloHandler;

/**
 * Подключение ServletContextHandler с ресурсами из classpath-а
 */
public class BasicServer2_classpath_resources {

	
	public static ServletContextHandler createServletContextHandler(String classpathFolder, String contextPath) throws Exception {
		
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(contextPath);
        context.setWelcomeFiles(new String[]{"index.html", "index.htm", "foo.htm"});
        
        ResourceFactory resourceFactory = ResourceFactory.of(context);
        Resource baseResource = resourceFactory.newClassLoaderResource(classpathFolder);
        if (!Resources.isReadableDirectory(baseResource))
            throw new FileNotFoundException("Unable to find base-resource "+baseResource.getFileName());
        context.setBaseResource(baseResource);

        //для вывода списка файлов, при заходе в корень контекста
        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        holderPwd.setInitParameter("dirAllowed", "true");
        context.addServlet(holderPwd, "/");
		
        return context;
	}
	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8081);
		
        ContextHandlerCollection contextsHandlers = new ContextHandlerCollection();

        ServletContextHandler sch1 = createServletContextHandler("static-root1","/static1");
        ServletContextHandler sch2 = createServletContextHandler("static-root2","/static2");
        contextsHandlers.addHandler(sch1);
        contextsHandlers.addHandler(sch2);
        
        
        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(contextsHandlers);
        handlerSequence.addHandler(new DefaultHandler());			//для вывода списка контекстов в корне сервера
        handlerSequence.addHandler(new HelloHandler("basic 2"));
		
        server.setHandler(handlerSequence);
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}
