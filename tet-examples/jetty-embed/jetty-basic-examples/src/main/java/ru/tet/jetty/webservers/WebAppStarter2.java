package ru.tet.jetty.webservers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import ru.tet.jetty.handlers.HelloHandler;

/**
 * Полноценный запуск веб-приложения.
 */
public class WebAppStarter2 {

	public static WebAppContext createWebAppContext2() throws Exception {

		Path webProjectPath = Paths.get("../../web-apps/web-app-ex2").toRealPath();
        Path warPath = webProjectPath.resolve("src/main/webapp");
        Path classesPath = webProjectPath.resolve("target/webapp2/WEB-INF/classes/");
		
        WebAppContext webappContext = new WebAppContext();
        webappContext.setContextPath("/webapp2");
//        webapp.setWar("../../web-apps/web-app-ex2/src/main/webapp");
        
		//какие jar нужно сканировать на аннотации и TLD.
        //без этого объявления - не будут находиться jsp-таги
		//в данном случае мы сканируем всё.
        webappContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");

        //не очень работает
        webappContext.setWelcomeFiles(new String[]{"index.html", "index.htm", "foo.htm"});
        
        
        webappContext.setWar(warPath.toUri().toASCIIString());
		webappContext.setExtraClasspath(classesPath.toUri().toASCIIString());
		
        //добавляем дополнительные статические ресурсы
		ServletHolder holderAlt = new ServletHolder(ResourceServlet.class);
		holderAlt.setInitParameter("baseResource", Path.of("../../web-apps/web-static-roots/src/main/resources/static-root1/").toUri().toASCIIString());
		webappContext.addServlet(holderAlt, "/static1/*");
        
		holderAlt = new ServletHolder(ResourceServlet.class);
		holderAlt.setInitParameter("baseResource", Path.of("../../web-apps/web-static-roots/src/main/resources/static-root2/").toUri().toASCIIString());
		webappContext.addServlet(holderAlt, "/static-root2/*");
			
		//можно добавлять много разных слушателей
		webappContext.addEventListener(new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent sce) {
                System.out.printf("WebAppContext is started. try loading %s%n", 
                		sce.getServletContext().getContextPath());
			}
			
		});
		
		
		return webappContext;
		
	}
	
	public static void main(String[] args) throws Exception {
		
		

		
		Server server = new Server(8081);
        
        WebAppContext webappContext = createWebAppContext2();
		
        ContextHandlerCollection contextsHandlers = new ContextHandlerCollection();
        contextsHandlers.addHandler(webappContext);

        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(contextsHandlers);
        handlerSequence.addHandler(new DefaultHandler());
        handlerSequence.addHandler(new HelloHandler("basic 2"));
		
        server.setHandler(handlerSequence);
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}
