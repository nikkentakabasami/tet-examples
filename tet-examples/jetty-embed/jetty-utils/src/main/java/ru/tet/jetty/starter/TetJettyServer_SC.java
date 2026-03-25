package ru.tet.jetty.starter;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

import ru.tet.jetty.utils.TetJettyUtils;

public class TetJettyServer_SC {

	Server server;
	ServletContextHandler context;
	
	ResourceFactory resourceFactory;
	
	TetJettyServerOptions options;

	
//	public Resource newClassLoaderResource(String resourcePath) throws FileNotFoundException {
//		
//		System.out.println("loading class path resource:"+resourcePath);
//		Resource resource = resourceFactory.newClassLoaderResource(resourcePath);
//		if (Resources.missing(resource)) {
//			throw new FileNotFoundException("Unable to find class loader resource " + resourcePath);
//		}
//		return resource;
//	}	
	
	public void start(TetJettyServerOptions options) throws Exception {
		this.options = options;
		
		server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(options.getPort());
		server.addConnector(connector);

		
        context = new ServletContextHandler();
        context.setContextPath(options.getContextPath());
        context.setWelcomeFiles(options.getWelcomeFiles());
//        context.setParentLoaderPriority(true);

        //сканировать сервлетные аннотации вроде @WebServlet
        if (options.isAnnotationSupport()) {
            TetJettyUtils.addAnnotationSupport(context);
        }

        if (options.isJspSupport()) {
    		TetJettyUtils.enableEmbeddedJspSupport(context, false);
        }        
        
		resourceFactory = ResourceFactory.of(context);
		

		List<Resource> resources = new ArrayList<>();
		for(String resourcePath:options.getClassLoaderBaseResources()) {
			
			System.out.println("loading class path resource:"+resourcePath);
			Resource resource = resourceFactory.newClassLoaderResource(resourcePath);
			if (Resources.missing(resource)) {
				throw new FileNotFoundException("Unable to find class loader resource " + resourcePath);
			}
			
//			Resource res = newClassLoaderResource(resourcePath);
			resources.add(resource);
		}
		
		
		for(Path path:options.getFileSystemBaseResources()) {
			path = path.toRealPath();
			System.out.println("loading resource:"+path);
			Resource resource = resourceFactory.newResource(path);
			if (Resources.missing(resource)) {
				throw new FileNotFoundException("Unable to find resource " + path);
			}
			resources.add(resource);
		}
		Resource compResource = ResourceFactory.combine(resources);
		
		context.setBaseResource(compResource);

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
