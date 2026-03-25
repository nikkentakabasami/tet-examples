package ru.tet.jetty.webstarter;

import java.nio.file.Path;

import org.slf4j.bridge.SLF4JBridgeHandler;

import ru.tet.beans.UserDTO;
import ru.tet.jetty.starter.TetJettyServer;
import ru.tet.jetty.starter.TetJettyServerOptions;


/**
 * Запуск через jetty embedded проекта web-app-ex2.
 * С использованием класса TetJettyServer.
 * 
 * Почему то не подключается HelloServlet2 (через web.xml) 
 * 
 * 
 */
public class MainTetJettyServer2 {

	static {
		// Setup java.util.logging to slf4j bridge
		SLF4JBridgeHandler.install();
	}
	
	
	public static void main(String[] args) throws Exception {
		
		TetJettyServerOptions options = new TetJettyServerOptions();
		options.setPort(8080);
		options.setContextPath("/");

		//подключаем веб-приложение
		options.setWebAppProjectPath("../../web-apps/web-app-ex2");
		options.setWebAppProjectFinalName("webapp2");
		
		//подключаем дополнительные статические ресурсы
//		options.getFileSystemBaseResources().add(Path.of("../../web-apps/web-static-roots/src/webapps/alt-root/"));
		
		//подключаем дополнительные классы
//		options.getExtraClasspathes().add(Path.of("../../tet-aux/target/classes/"));
		
		TetJettyServer main = new TetJettyServer();
		
		main.init(options);
		
		main.start();
		main.waitForInterrupt();
		

	}
	
	
	
	
}
