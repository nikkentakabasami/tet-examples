package ru.tet.jetty.webstarter;

import java.nio.file.Path;

import org.slf4j.bridge.SLF4JBridgeHandler;

import ru.tet.beans.UserDTO;
import ru.tet.jetty.starter.TetJettyServer;
import ru.tet.jetty.starter.TetJettyServerOptions;


/**
 * Запуск через jetty embedded проекта web-app-ex1.
 * С использованием класса TetJettyServer.
 * 
 * Всё работает, но проблемы с jndi: classloader UserDTO не тот.
 * 
 * 
 */
public class MainTetJettyServer1 {

	static {
		// Setup java.util.logging to slf4j bridge
		SLF4JBridgeHandler.install();
	}
	
	
	public static void main(String[] args) throws Exception {
		
		TetJettyServerOptions options = new TetJettyServerOptions();
		options.setPort(8081);
		options.setContextPath("/");

		//подключаем веб-приложение
		options.setWebAppProjectPath("../../web-apps/web-app-ex1");
		options.setWebAppProjectFinalName("webapp1");
		
		//подключаем дополнительные статические ресурсы
//		options.getFileSystemBaseResources().add(Path.of("../../web-apps/web-static-roots/src/webapps/alt-root/"));
		
		//подключаем дополнительные классы
		options.getExtraClasspathes().add(Path.of("../../tet-aux/target/classes/"));
		
		TetJettyServer main = new TetJettyServer();
		
		main.init(options);
		
		//подключение jndi
		UserDTO user = new UserDTO("nemawashi", "123");
		user.setSecondPassword("654");
		user.setEmail("nema@mail.ru");
		user.setEnabled(true);
        new org.eclipse.jetty.plus.jndi.Resource(main.getServer(), "ttt/testUser1", user);
        new org.eclipse.jetty.plus.jndi.EnvEntry(main.getServer(), "testNumberValue1", Integer.valueOf(4000), false);
        new org.eclipse.jetty.plus.jndi.EnvEntry(main.getServer(), "testStringValue1", "Sabaki kirenai", true);
		
		
		
		main.start();
		main.waitForInterrupt();
		

	}
	
	
	
	
}
