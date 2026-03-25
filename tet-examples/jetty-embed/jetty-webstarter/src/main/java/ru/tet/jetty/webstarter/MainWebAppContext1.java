package ru.tet.jetty.webstarter;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

import ru.tet.beans.UserDTO;
import ru.tet.jetty.utils.TetJettyUtils;

/**
 * Базовый пример jetty embedded.
 * 
 * Подключаются сервлеты на аннотациях, статический контент, 
 * доп статический контент и сервлеты из доп-библиотеки js-libs-jar
 * 
 * 
 * Сервлеты подключаются через web.xml, но не через аннотации.
 * 
 * 
 */
public class MainWebAppContext1 {


	public static void main(String[] args) throws Exception {
		MainWebAppContext1 main = new MainWebAppContext1();
		main.start();
		main.waitForInterrupt();
	}

	private Server server;


	public void start() throws Exception {
		server = new Server();
		server.setDumpAfterStart(true);

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8081);
		server.addConnector(connector);

        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setWelcomeFiles(new String[]{"index.html", "welcome.html"});
//        context.setParentLoaderPriority(true);

        
        //сканировать сервлетные аннотации вроде @WebServlet
//        TetJettyUtils.addAnnotationSupport(context);
//        context.setExtraClasspath("target/web-app-ex1-0.0.1-SNAPSHOT/WEB-INF/classes");

		Path basePath = Paths.get("../../web-apps/web-app-ex1").toRealPath();
		
		
		ResourceFactory resourceFactory = ResourceFactory.of(context);
		
		
		Path classesPath = basePath.resolve("target/webapp1/WEB-INF/classes/");
		if (!Files.exists(classesPath) || !Files.isDirectory(classesPath)) {
			throw new RuntimeException("not found: "+classesPath);
		}
		
		Resource classesResource = resourceFactory.newResource(classesPath.toAbsolutePath());
		Resource auxClassesResource = resourceFactory.newResource(Paths.get("../../tet-aux/target/classes/").toAbsolutePath());
		context.setExtraClasspath(List.of(classesResource,auxClassesResource));
		
//		context.setExtraClasspath(classesPath.toAbsolutePath().toString());
		
		
        
		
		// Base URI for servlet context
//		Resource baseResource = TetJettyUtils.getResource(resourceFactory, "src/main/webapp");
//		Resource addRes = TetJettyUtils.getResource(resourceFactory, "/js-libs/");
		
		
//		Resource compResource = ResourceFactory.combine(baseResource,addRes);
		
		Path webappPath = basePath.resolve("src/main/webapp").toRealPath();
		if (!Files.exists(webappPath) || !Files.isDirectory(webappPath)) {
			throw new RuntimeException("not found: "+webappPath);
		}
		
		
		Resource baseResource = resourceFactory.newResource(webappPath);
		context.setBaseResource(baseResource);

		//проблемы с класс лоадером
		UserDTO user = new UserDTO("nemawashi", "123");
		user.setSecondPassword("654");
		user.setEmail("nema@mail.ru");
		user.setEnabled(true);
		
		//подключение jndi
        new org.eclipse.jetty.plus.jndi.Resource(server, "ttt/testUser1", user);
        new org.eclipse.jetty.plus.jndi.EnvEntry(server, "testNumberValue1", Integer.valueOf(4000), false);
        new org.eclipse.jetty.plus.jndi.EnvEntry(server, "testStringValue1", "Sabaki kirenai", true);
		
		// Add Application Servlets
//		context.addServlet(DateServlet.class, "/date/");
		
		// Create Example of mapping jsp to path spec
		ServletHolder holderAltMapping = new ServletHolder();
		holderAltMapping.setName("foo.jsp");
		holderAltMapping.setForcedPath("/test/foo/foo.jsp");
		context.addServlet(holderAltMapping, "/test/foo/");


		server.setHandler(context);

		
		
		
		
		// Start Server
		// server.setDumpAfterStart(true);
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
