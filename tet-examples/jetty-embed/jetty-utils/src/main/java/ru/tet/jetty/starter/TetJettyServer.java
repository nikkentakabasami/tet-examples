package ru.tet.jetty.starter;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

/**
 * Вспомогательный класс, умеющий запускать веб-приложения с использованием jetty embedded.
 * 
 * Запуск может быть в dev-режиме
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
		
        // Allow discovery of Jakarta Servlet API (needed for annotation scanning)
//		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/jakarta.servlet-api-[^/]*\\.jar$");
		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");

		
		//ограничить jar, которые должны сканироваться
//		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.WebInfIncludeJarPattern", ".*/spring-[^/]*\\.jar$");
		
		
		resourceFactory = ResourceFactory.of(webAppContext);

		//статические ресурсы веб-приложения
		List<Resource> baseResources = new ArrayList<>();
		for (String resourcePath : options.getClassLoaderBaseResources()) {

			System.out.println("loading class path resource:" + resourcePath);
			Resource resource = resourceFactory.newClassLoaderResource(resourcePath);
			if (Resources.missing(resource)) {
				throw new FileNotFoundException("Unable to find class loader resource " + resourcePath);
			}
			baseResources.add(resource);
		}

		for (Path path : options.getFileSystemBaseResources()) {
			path = path.toAbsolutePath(); // toRealPath();
			System.out.println("loading resource:" + path);
			Resource resource = resourceFactory.newResource(path);
			if (Resources.missing(resource)) {
				throw new FileNotFoundException("Unable to find resource " + path);
			}
			baseResources.add(resource);
		}

		//classpath ресурсы веб-приложения (классы, библиотеки)
		List<Resource> classpathResources = new ArrayList<>();
		for (Path path : options.getExtraClasspathes()) {
			Resource extraResource = resourceFactory.newResource(path);
			if (Resources.missing(extraResource)) {
				throw new FileNotFoundException("Unable to find resource " + path);
			}
			classpathResources.add(extraResource);
		}

		//задан путь к веб-проекту
		if (options.webAppProjectPath != null) {

			//проверяем его
			Path basePath = Paths.get(options.webAppProjectPath).toRealPath();
			checkDir(basePath);

			//находим папку webapp, подключаем её
			Path webappPath = basePath.resolve("src/main/webapp").toRealPath();
			checkDir(webappPath);
			Resource webappResource = resourceFactory.newResource(webappPath);
			baseResources.add(webappResource);

			//добавляем в classpath classes-файлы веб приложения
			Path classesPath = basePath.resolve("target/" + options.getWebAppProjectFinalName() + "/WEB-INF/classes/");
			checkDir(classesPath);
			classpathResources.add(resourceFactory.newResource(classesPath.toAbsolutePath()));
		}

		webAppContext.setExtraClasspath(classpathResources);
		webAppContext.setBaseResource(ResourceFactory.combine(baseResources));

		server.setHandler(webAppContext);

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
	 * <p>
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
}
