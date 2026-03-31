package ru.tet.jetty;

import java.nio.file.Path;

import ru.tet.jetty.starter.TetJettyServer;
import ru.tet.jetty.starter.TetJettyServerOptions;


/**
 * Запуск через jetty embedded (С использованием класса TetJettyServer)
 * 
 */
public class JettyEmbeddedStarter10 {

	
	public static void main(String[] args) throws Exception {
		
		TetJettyServerOptions options = new TetJettyServerOptions();
		options.setPort(8081);
//		options.setContextPath("/");
		options.setContextPath("/webapp10");

		//подключаем веб-приложение
//		options.setWebAppProjectPath("../../web-apps/web-app-ex1");
//		options.setWebAppProjectFinalName("webapp1");
		
		//подключаем дополнительные статические ресурсы
//		options.getFileSystemBaseResources().add(Path.of("../../web-apps/web-static-roots/src/webapps/alt-root/"));
		
		//подключаем дополнительные классы
//		options.getExtraClasspathes().add(Path.of("../../tet-aux/target/classes/"));
		
		
		//подключаем дополнительные статические ресурсы
//		options.addAdditionalStatic("/webapp1/*",Path.of("../web-app-ex1/src/main/webapp/"));
		
		options.getFileSystemBaseResources().add(Path.of("../web-app-ex1/src/main/webapp/"));
		
		
		
		
//		options.addAdditionalStatic(Path.of("../web-jslibs/src/main/resources/jslibs/"));
//		options.getExtraClasspathes().add(Path.of("../web-jslibs/target/classes"));
		
		
		
		
		
		TetJettyServer main = new TetJettyServer();
		
		main.init(options);
		
		//подключение jndi
		JettyEmbeddedStarter1.bindWebappEx1Jndi(main);
		
		main.start();
		main.waitForInterrupt();
		

	}

	
	
}
