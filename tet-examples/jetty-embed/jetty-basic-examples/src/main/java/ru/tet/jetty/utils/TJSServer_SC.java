package ru.tet.jetty.utils;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

import ru.tet.jetty.utils.TJSOptions.AdditionalStatic;


/**
 * Более легковесная версия TetJettyServer, использующая ServletContextHandler.
 * 
 */
public class TJSServer_SC {

	Server server;
	ServletContextHandler context;
	
	ResourceFactory resourceFactory;
	
	TJSOptions options;


	//Handler-ы этого сервера. Можно добавлять новые, если надо.
	Handler.Sequence handlers;
	
	
	public void start(TJSOptions options) throws Exception {
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

		//получаем путь к веб-проекту
		Path webProjectPath;
		if (options.webAppProjectPath != null) {
			webProjectPath = Paths.get(options.webAppProjectPath).toRealPath();
			checkDir(webProjectPath);
		} else {
			webProjectPath = Paths.get(".").toRealPath();
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
			context.addServlet(holderAlt, pathSpec);
		}
		


		
//		webAppContext.setWarResource(baseResource);
		context.setBaseResource(baseResource);
		
		
		//добавляем defaultHandler - чтобы генерировать страницу с контекстами при заходе в корень сервера
		DefaultHandler defaultHandler = new DefaultHandler();
		
		
//		handlers = new ContextHandlerCollection();   //так тоже можно
        handlers = new Handler.Sequence();
        handlers.addHandler(context);
        handlers.addHandler(defaultHandler);
        server.setHandler(handlers);
		
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

	public void waitForInterrupt() throws InterruptedException {
		server.join();
	}	
	
	
}
