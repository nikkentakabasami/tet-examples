package ru.tet.jetty.old;

import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ru.tet.jetty.utils.TetJettyUtils;

/**
 * Базовый пример jetty embedded.
 * 
 * Подключаются сервлеты на аннотациях, статический контент, 
 * доп статический контент и сервлеты из доп-библиотеки js-libs-jar
 * 
 * 
 */
public class Main2 {
	static {
		// Setup java.util.logging to slf4j bridge
		SLF4JBridgeHandler.install();
	}


	public static void main(String[] args) throws Exception {
		Main2 main = new Main2(8081);
		main.start();
		main.waitForInterrupt();
	}

	private int port;
	private Server server;

	public Main2(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		server.addConnector(connector);

		
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setWelcomeFiles(new String[]{"index.html", "welcome.html"});
//        context.setParentLoaderPriority(true);

        //сканировать сервлетные аннотации вроде @WebServlet
        TetJettyUtils.addAnnotationSupport(context);

		ResourceFactory resourceFactory = ResourceFactory.of(context);
		
		// Base URI for servlet context
		Resource baseResource = TetJettyUtils.getClassLoaderResource(resourceFactory, "/webroot/");
		Resource addRes = TetJettyUtils.getClassLoaderResource(resourceFactory, "/js-libs/");
		
		/*
		 * Подключение статического контента из файловой системы
		Path arPath = Paths.get("../webapp-examples/js-libs-jar/src/main/resources/js-libs").toRealPath();
		Resource addRes = resourceFactory.newResource(arPath);		
		if (Resources.missing(addRes)) {
			throw new FileNotFoundException("Unable to find resource " + arPath);
		}
		*/
		
		Resource compResource = ResourceFactory.combine(baseResource,addRes);
		
		context.setBaseResource(compResource);

		// Since this is a ServletContextHandler we must manually configure JSP support.
		TetJettyUtils.enableEmbeddedJspSupport(context, false);

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
