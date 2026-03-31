package ru.tet.jetty.wac;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Запуск веб. приложения web-app-ex2.
 * Дополнительно подключаем статические ресурсы из файловой системы через ResourceServlet.
 * 
 */
public class Main3 {
	static {
		// Setup java.util.logging to slf4j bridge
		SLF4JBridgeHandler.install();
	}

	public static void main(String[] args) throws Exception {

		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8082);
		server.addConnector(connector);

		WebAppContext context = new WebAppContext();
		context.setContextPath("/");
//        context.setWelcomeFiles(new String[]{"index.html", "welcome.html"});

		Path basePath = Paths.get("../../web-apps/web-app-ex2").toRealPath();
		
		Path classesPath = basePath.resolve("target/webapp2/WEB-INF/classes");
		if (!Files.exists(classesPath) || !Files.isDirectory(classesPath)) {
			throw new RuntimeException("not found: "+classesPath);
		}
		context.setExtraClasspath(classesPath.toAbsolutePath().toString());
		
		Path webappPath = basePath.resolve("src/main/webapp").toRealPath();
		if (!Files.exists(webappPath) || !Files.isDirectory(webappPath)) {
			throw new RuntimeException("not found: "+webappPath);
		}
		Resource baseResource = context.getResourceFactory().newResource(webappPath);
		context.setBaseResource(baseResource);
		
		
		//какие jar нужно сканировать на аннотации и TLD (необходимо для работы jsp).
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");
//		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/jakarta.servlet-api-[^/]*\\.jar$");
		
		

		

		//Подключаем доп. статические ресурсы
		Path addStaticPath1 = Path.of("../../web-apps/web-static-roots/src/main/resources/static-root1/");
		Path addStaticPath2 = Path.of("../../web-apps/web-static-roots/src/main/resources/static-root2/");
		
		ServletHolder holderAlt = new ServletHolder(ResourceServlet.class);
		holderAlt.setInitParameter("baseResource", addStaticPath1.toUri().toASCIIString());
		context.addServlet(holderAlt, "/static1/*");
		
		holderAlt = new ServletHolder(ResourceServlet.class);
		holderAlt.setInitParameter("baseResource", addStaticPath2.toUri().toASCIIString());
		context.addServlet(holderAlt, "/static-root2/*");
		

		Handler.Sequence handlers = new Handler.Sequence(context,new DefaultHandler());
        server.setHandler(handlers);

		server.start();
		server.join();

	}

}
