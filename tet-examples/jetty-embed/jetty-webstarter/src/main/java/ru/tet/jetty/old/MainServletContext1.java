package ru.tet.jetty.old;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;

import ru.tet.jetty.utils.TetJettyUtils;

/**
 * 
 * Не подключает jsp.
 * 
 */
public class MainServletContext1 {


	public static void main(String[] args) throws Exception {
		MainServletContext1 main = new MainServletContext1(8081);
		main.start();
		main.waitForInterrupt();
	}

	private int port;
	private Server server;

	public MainServletContext1(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		server.addConnector(connector);

		
		ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.setWelcomeFiles(new String[]{"index.html", "welcome.html","md2_index.jsp"});
//        context.setParentLoaderPriority(true);

        //сканировать сервлетные аннотации вроде @WebServlet
        TetJettyUtils.addAnnotationSupport(context);

        
        
		ResourceFactory resourceFactory = ResourceFactory.of(context);
		
		// Base URI for servlet context
		Resource baseResource = TetJettyUtils.getResource(resourceFactory, "src/main/webapp");
		Resource addRes = TetJettyUtils.getResource(resourceFactory, "../web-static-roots/src/webapps/alt-root/");

		
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

		ServletHolder holderDef = new ServletHolder("default", DefaultServlet.class);
		holderDef.setInitParameter("dirAllowed", "true");
		context.addServlet(holderDef, "/");
		
		

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
