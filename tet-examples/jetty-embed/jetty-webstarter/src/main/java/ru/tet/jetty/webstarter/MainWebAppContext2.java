package ru.tet.jetty.webstarter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;

import ru.tet.jetty.utils.TetJettyUtils;

/**
 * 
 * Поднимает web-app-ex2.
 * Почему то не работают jsp - не находит jstl/core.
 * 
 * http://java.sun.com/jsp/jstl/core] cannot be resolved
 * 
 */
public class MainWebAppContext2 {


	public static void main(String[] args) throws Exception {
		MainWebAppContext2 main = new MainWebAppContext2();
		main.start();
		main.waitForInterrupt();
	}

	private Server server;


	public void start() throws Exception {
		server = new Server();
		server.setDumpAfterStart(true);

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8087);
		server.addConnector(connector);

		
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setWelcomeFiles(new String[]{"index.html", "welcome.html"});

        //сканировать сервлетные аннотации вроде @WebServlet
//        TetJettyUtils.addAnnotationSupport(context);
//        context.setExtraClasspath("target/web-app-ex1-0.0.1-SNAPSHOT/WEB-INF/classes");

		Path basePath = Paths.get("../../web-apps/web-app-ex2").toRealPath();
		
		
		Path classesPath = basePath.resolve("target/web-app-ex2-0.0.1-SNAPSHOT/WEB-INF/classes");
		if (!Files.exists(classesPath) || !Files.isDirectory(classesPath)) {
			throw new RuntimeException("not found: "+classesPath);
		}
		context.setExtraClasspath(classesPath.toAbsolutePath().toString());
        
        
//		ResourceFactory resourceFactory = ResourceFactory.of(context);
		
		// Base URI for servlet context
//		Resource baseResource = TetJettyUtils.getResource(resourceFactory, "src/main/webapp");
//		Resource addRes = TetJettyUtils.getResource(resourceFactory, "/js-libs/");
		
		
//		Resource compResource = ResourceFactory.combine(baseResource,addRes);
		
		Path webappPath = basePath.resolve("src/main/webapp").toRealPath();
		if (!Files.exists(webappPath) || !Files.isDirectory(webappPath)) {
			throw new RuntimeException("not found: "+webappPath);
		}
		
		
		Resource baseResource = context.getResourceFactory().newResource(webappPath);
		context.setBaseResource(baseResource);

		// Since this is a ServletContextHandler we must manually configure JSP support.
//		TetJettyUtils.enableEmbeddedJspSupport(context, false);
//		TetJettyUtils.enableEmbeddedJspSupport(context, false,MainWebAppContext2.class.getClassLoader());
//		TetJettyUtils.enableEmbeddedJspSupport(context, false,this.getClass().getClassLoader());

		

		server.setHandler(context);

		
		
		
		
		// Start Server
		server.start();
	}



	public void stop() throws Exception {
		server.stop();
	}

	/**
	 * Cause server to keep running until it receives a Interrupt.
	 * <p>
	 * Interrupt Signal, or SIGINT (Unix Signal), is typically seen as a result of a kill -TERM {pid} or Ctrl+C
	 */
	public void waitForInterrupt() throws InterruptedException {
		server.join();
	}
}
