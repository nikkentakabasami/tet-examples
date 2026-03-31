package ru.tet.jetty;

import java.nio.file.Path;

import ru.tet.jetty.starter.TetJettyServer;
import ru.tet.jetty.starter.TetJettyServerOptions;


/**
 * Запуск через jetty embedded (С использованием класса TetJettyServer)
 * 
 */
public class JettyEmbeddedStarter11 {

	
	public static void main(String[] args) throws Exception {
		
		TetJettyServerOptions options = new TetJettyServerOptions();
		options.setPort(8081);
//		options.setContextPath("/");
		options.setContextPath("/webapp11");

		//подключаем веб-приложение
//		options.setWebAppProjectPath("../../web-apps/web-app-ex1");
//		options.setWebAppProjectFinalName("webapp1");
		
		//подключаем js-библиотеки
		options.addAdditionalStatic(Path.of("../web-jslibs/src/main/resources/jslibs/"));
		
		//подключаем сервлеты
		options.getExtraClasspathes().add(Path.of("../web-jslibs/target/classes"));
		
		TetJettyServer main = new TetJettyServer();
		
		main.init(options);
		
		
		main.start();
		main.waitForInterrupt();
		

	}

	
	
}
