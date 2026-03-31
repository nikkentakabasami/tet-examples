package ru.tet.jetty.webstarter;

import java.nio.file.Path;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ru.tet.beans.UserDTO;
import ru.tet.jetty.starter.TetJettyServer;
import ru.tet.jetty.starter.TetJettyServerOptions;


/**
 * Запуск через jetty embedded проекта web-app-ex10.
 * С использованием класса TetJettyServer.
 * 
 * Всё работает, но проблемы с jndi: classloader UserDTO не тот.
 * 
 * 
 */
public class MainTetJettyServer10 {

	static {
		// Setup java.util.logging to slf4j bridge
		SLF4JBridgeHandler.install();
	}
	
	
	public static void main(String[] args) throws Exception {
		
		TetJettyServerOptions options = new TetJettyServerOptions();
		options.setPort(8081);
		options.setContextPath("/");

		//подключаем веб-приложение
		options.setWebAppProjectPath("../../web-apps/web-app-ex10-overlay");
		options.setWebAppProjectFinalName("webapp10");
		
		//подключаем дополнительноео веб-приложение
		//подключаются jsp и сервлеты - но не html файлы!
		options.getFileSystemBaseResources().add(Path.of("../../web-apps/web-app-ex1/src/main/webapp/"));
		options.getExtraClasspathes().add(Path.of("../../web-apps/web-app-ex1/target/webapp1/WEB-INF/classes"));
		
		//не работает
//		options.setWelcomeFiles(new String[] {"index.jsp"});
//		options.setWelcomeFiles(new String[] {"test1.html"});
		
		
		//подключаем дополнительные классы
//		options.getExtraClasspathes().add(Path.of("../../tet-aux/target/classes/"));
		
		TetJettyServer main = new TetJettyServer();
		
		main.init(options);
		
		//подключение jndi
		MainTetJettyServer1.bindWebappEx1Jndi(main);
		
		
		main.start();
		main.waitForInterrupt();
		

	}
	
	
	
	
}
