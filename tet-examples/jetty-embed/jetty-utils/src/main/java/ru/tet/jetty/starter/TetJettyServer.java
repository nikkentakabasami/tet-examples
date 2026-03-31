package ru.tet.jetty.starter;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

import ru.tet.jetty.starter.TetJettyServerOptions.AdditionalStatic;

/**
 * Вспомогательный класс, умеющий запускать веб-приложения с использованием jetty embedded.
 * 
 * Запуск может быть в dev-режиме.
 * Проект должен содержать все зависимости, что есть в запускаемом веб-проекте.
 * 
 * 
 * Для запуска использует WebAppContext.
 * 
 * 
 */
public class TetJettyServer {

	Server server;
	WebAppContext webAppContext;

	ResourceFactory resourceFactory;

	TetJettyServerOptions options;

	//Handler-ы этого сервера. Можно добавлять новые, если надо.
	Handler.Sequence handlers;
	
	public void init(TetJettyServerOptions options) throws Exception {
		this.options = options;

		server = new Server(options.getPort());

		webAppContext = new WebAppContext();
		webAppContext.setContextPath(options.getContextPath());
		webAppContext.setWelcomeFiles(options.getWelcomeFiles());
		//        context.setParentLoaderPriority(true);

		//поддержка аннотаций
		webAppContext.setConfigurationDiscovered(options.isAnnotationSupport());
		
		//какие jar нужно сканировать на аннотации и TLD.
		//по умолчанию jetty может сканировать не всё, ради большей производительности.
		//это приводит к странным глюкам.
		//в данном случае мы сканируем всё.
		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");
//		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/jakarta.servlet-api-[^/]*\\.jar$");

		
		//ограничить - какие jar в папке WEB-INF/lib должны сканироваться
		//ускоряет скорость запуска приложения
//		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.WebInfIncludeJarPattern", ".*/spring-[^/]*\\.jar$");
//		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.WebInfIncludeJarPattern", ".*/*.jar$");
		
		resourceFactory = ResourceFactory.of(webAppContext);
		
//		webAppContext.setConfigurations(new Configuration[] {
//	    new WebInfConfiguration(),
//	    new EnvConfiguration(),    // Handles jetty-env.xml and JNDI
//	    new WebXmlConfiguration(), // Handles web.xml
//	});		
		
		//получаем путь к веб-проекту
		boolean useWebInfClasses = true;
		Path webProjectPath;
		if (options.webAppProjectPath != null) {
			webProjectPath = Paths.get(options.webAppProjectPath).toRealPath();
			checkDir(webProjectPath);
		} else {
			webProjectPath = Paths.get(".").toRealPath();
			useWebInfClasses = false;
		}
		//проверяем его
		checkDir(webProjectPath);

		//находим папку webapp, подключаем её
		Path webappPath = webProjectPath.resolve("src/main/webapp").toRealPath();
		checkDir(webappPath);
		Resource baseResource = resourceFactory.newResource(webappPath);

		
		
		//доп. ресурсы для добавления в baseResources: jsp например
		//работает это странно - некоторые файлы не находит
		if (options.getFileSystemBaseResources().size()>0) {

			List<Resource> baseResources = new ArrayList<>();
			baseResources.add(baseResource);
			
			for (Path path : options.getFileSystemBaseResources()) {
				path = path.toAbsolutePath();
				System.out.println("loading resource:" + path);
				
				Resource resource = resourceFactory.newResource(path);
				if (Resources.missing(resource)) {
					throw new FileNotFoundException("Unable to find resource " + path);
				}
				baseResources.add(resource);
			}
			
			baseResource = ResourceFactory.combine(baseResources);		
		}
		

		//дополнительный статический контент
		//подключается через ResourceServlet
		for(AdditionalStatic as: options.getAdditionalStatic()) {
			checkDir(as.dirPath);
			
			String pathSpec = as.pathSpec;
			if (pathSpec==null) {
				pathSpec = "/"+as.dirPath.getFileName().toString()+"/*";
			}
			
			ServletHolder holderAlt = new ServletHolder(ResourceServlet.class);
			holderAlt.setInitParameter("baseResource", as.dirPath.toUri().toASCIIString());
			webAppContext.addServlet(holderAlt, pathSpec);
		}
		

		//дополнительные classpath ресурсы веб-приложения (классы, библиотеки)
		List<Resource> extraClasspathResources = new ArrayList<>();
		for (Path path : options.getExtraClasspathes()) {
			Resource extraResource = resourceFactory.newResource(path);
			if (Resources.missing(extraResource)) {
				throw new FileNotFoundException("Unable to find resource " + path);
			}
			extraClasspathResources.add(extraResource);
		}

		//добавляем в classpath WEB-INF/classes/
		//это надо делать только если запуск идёт не из веб-проекта
		if (useWebInfClasses) {
			Path classesPath = webProjectPath.resolve("target/" + options.webAppProjectFinalName + "/WEB-INF/classes/");
			checkDir(classesPath);
			extraClasspathResources.add(resourceFactory.newResource(classesPath.toAbsolutePath()));
		}
		
		

		webAppContext.setExtraClasspath(extraClasspathResources);

		
//		webAppContext.setWarResource(baseResource);
		webAppContext.setBaseResource(baseResource);
		
		
		//добавляем defaultHandler - чтобы генерировать страницу с контекстами при заходе в корень сервера
		DefaultHandler defaultHandler = new DefaultHandler();
		
		
//		handlers = new ContextHandlerCollection();   //так тоже можно
        handlers = new Handler.Sequence();
        handlers.addHandler(webAppContext);
        handlers.addHandler(defaultHandler);
        server.setHandler(handlers);

	}

	public void start() throws Exception {
		server.setDumpAfterStart(true);
		server.start();
	}

	private void checkDir(Path dir) {
		if (!Files.exists(dir) || !Files.isDirectory(dir)) {
			throw new RuntimeException("Directory not found: " + dir);
		}
	}

	public void stop() throws Exception {
		server.stop();
	}

	/**
	 * Cause server to keep running until it receives a Interrupt.
	 * Interrupt Signal, or SIGINT (Unix Signal), is typically seen as a result of a kill -TERM {pid} or Ctrl+C
	 */
	public void waitForInterrupt() throws InterruptedException {
		server.join();
	}

	public Server getServer() {
		return server;
	}

	public WebAppContext getWebAppContext() {
		return webAppContext;
	}
	
	public Handler.Sequence getHandlers() {
		return handlers;
	}
	
}
