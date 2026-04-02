package ru.tet.jetty.basic;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
 * Подключение ServletContextHandler с ресурсами из файловой системы
 */
public class BasicServer3_file_resources {

	
	public static ServletContextHandler createServletContextHandler(Path resourceFolder, String contextPath) throws Exception {
		
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(contextPath);
//        context.setWelcomeFiles(new String[]{"index.html", "index.htm", "foo.htm"});
        context.setWelcomeFiles(new String[]{"index.htm", "foo.htm"});
        
        ResourceFactory resourceFactory = ResourceFactory.of(context);
        Resource baseResource = resourceFactory.newResource(resourceFolder);
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

        Path webProjectPath = Paths.get("../../web-apps/web-static-roots").toRealPath();
        
        
        Path resourceFolder1 = webProjectPath.resolve("src/webapps/alt-root");
        Path resourceFolder2 = webProjectPath.resolve("src/webapps/webapp-a");
        Path resourceFolder3 = webProjectPath.resolve("src/webapps/webapp-b");
        
        
        ServletContextHandler sch1 = createServletContextHandler(resourceFolder1,"/static1");
        ServletContextHandler sch2 = createServletContextHandler(resourceFolder2,"/static2");
        ServletContextHandler sch3 = createServletContextHandler(resourceFolder3,"/static3");
        
        contextsHandlers.addHandler(sch1);
        contextsHandlers.addHandler(sch2);
        contextsHandlers.addHandler(sch3);
        
        
        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(contextsHandlers);
        handlerSequence.addHandler(new DefaultHandler());			//для вывода списка контекстов в корне сервера
        handlerSequence.addHandler(new HelloHandler("basic 2"));
		
        server.setHandler(handlerSequence);
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}
