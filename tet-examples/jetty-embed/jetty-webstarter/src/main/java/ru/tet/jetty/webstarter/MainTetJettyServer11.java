package ru.tet.jetty.webstarter;

import java.nio.file.Path;

import org.slf4j.bridge.SLF4JBridgeHandler;

import ru.tet.jetty.starter.TetJettyServer;
import ru.tet.jetty.starter.TetJettyServerOptions;


/**
 * Запуск через jetty embedded проекта web-app-ex11.
 * С использованием класса TetJettyServer.
 * 
 * Это простой веб-проект, который импортирует js-библиотеки из проекта web-jslibs.
 * 
 * 
 * 
 */
public class MainTetJettyServer11 {

	static {
		// Setup java.util.logging to slf4j bridge
		SLF4JBridgeHandler.install();
	}
	
	
	public static void main(String[] args) throws Exception {
		
		TetJettyServerOptions options = new TetJettyServerOptions();
		options.setPort(8081);
		options.setContextPath("/");

		//подключаем веб-приложение
		options.setWebAppProjectPath("../../web-apps/web-app-ex11-overlay2");
		options.setWebAppProjectFinalName("webapp11");
		
		
		//подключаем дополнительные статические ресурсы
		options.addAdditionalStatic(Path.of("../../web-apps/web-jslibs/src/main/resources/jslibs/"));
		
		//подключаем дополнительные классы
		//добавляем сервлет в classpath
		options.getExtraClasspathes().add(Path.of("../../web-apps/web-jslibs/target/classes"));
		
		//не работает
//		options.setWelcomeFiles(new String[] {"index.jsp"});
//		options.setWelcomeFiles(new String[] {"test1.html"});
		
		TetJettyServer main = new TetJettyServer();
		
		main.init(options);
		
		main.start();
		main.waitForInterrupt();
		

	}
	
	
	
	
}
