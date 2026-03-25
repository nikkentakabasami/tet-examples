package ru.tet.jetty.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.ee10.jsp.JettyJspServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

import jakarta.servlet.ServletContext;

public class TetJettyUtils {

	
	/**
	 * Добавляет поддержку аннотаций
	 * сканировать сервлетные аннотации вроде @WebServlet
	 * @param context
	 */
	public static void addAnnotationSupport(ContextHandler context) {
		
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/jakarta.servlet-api-[^/]*\\.jar$|.*/classes/.*$");
		
	}
	
	
	/**
	 * Setup JSP Support for ServletContextHandlers.
	 * <p>
	 * NOTE: This is not required or appropriate if using a WebAppContext.
	 * </p>
	 */	
	public static void enableEmbeddedJspSupport(
			ServletContextHandler servletContextHandler,
			boolean usingPrecompiledJsps
			)
			throws IOException {
		
		// For JSP, you MUST have a Temp Directory, even if you use precompiled JSPs
		Path webappTempDir = Files.createTempDirectory("ee10-embedded-jsp");
		if (!Files.isDirectory(webappTempDir))
			Files.createDirectories(webappTempDir);
		servletContextHandler.setAttribute(ServletContext.TEMPDIR, webappTempDir.toFile());

		// If you don't use Precompiled JSPs then the Apache Jasper JSP implementation
		// requires a defined scratch directory for JSP compilation.
		Path scratchDir = null;

		if (!usingPrecompiledJsps) {
			scratchDir = webappTempDir.resolve("scratch");

			if (!Files.isDirectory(scratchDir))
				Files.createDirectory(scratchDir);
		}

		// Set Classloader of Context to be sane (needed for JSTL)
		// JSP requires a non-System classloader, this simply wraps the
		// embedded System classloader in a way that makes it suitable
		// for JSP to use
		ClassLoader jspClassLoader = new URLClassLoader(new URL[0], TetJettyUtils.class.getClassLoader());
//		ClassLoader jspClassLoader = new URLClassLoader(new URL[0], cl);
		servletContextHandler.setClassLoader(jspClassLoader);

		// Manually call JettyJasperInitializer on context startup
		servletContextHandler.addBean(new EmbeddedJspStarter(servletContextHandler));

		// Create / Register JSP Servlet (must be named "jsp" per spec)
		ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
		holderJsp.setInitOrder(0);
		if (scratchDir != null)
			holderJsp.setInitParameter("scratchdir", scratchDir.toString());
		holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
		holderJsp.setInitParameter("fork", "false");
		holderJsp.setInitParameter("xpoweredBy", "false");
		holderJsp.setInitParameter("compilerTargetVM", "1.8");
		holderJsp.setInitParameter("compilerSourceVM", "1.8");
		holderJsp.setInitParameter("keepgenerated", "true");
		servletContextHandler.addServlet(holderJsp, "*.jsp");

		servletContextHandler.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
	}

	
	/*
	public static Resource getWebResource(Container contextHandler, String resourcePath) throws FileNotFoundException {
		
		ResourceFactory resourceFactory = ResourceFactory.of(contextHandler);
		
		Resource resource = resourceFactory.newClassLoaderResource(resourcePath);
		if (Resources.missing(resource)) {
			throw new FileNotFoundException("Unable to find resource " + resourcePath);
		}
		return resource;
	}
	*/
	
	public static Resource getClassLoaderResource(ResourceFactory resourceFactory, String resourcePath) throws FileNotFoundException {
		Resource resource = resourceFactory.newClassLoaderResource(resourcePath);
		if (Resources.missing(resource)) {
			throw new FileNotFoundException("Unable to find class loader resource " + resourcePath);
		}
		return resource;
	}
	
	public static Resource getResource(ResourceFactory resourceFactory, String resourcePath) throws FileNotFoundException {
		Resource resource = resourceFactory.newResource(resourcePath);
		if (Resources.missing(resource)) {
			throw new FileNotFoundException("Unable to find resource " + resourcePath);
		}
		return resource;
	}
	
	
}
