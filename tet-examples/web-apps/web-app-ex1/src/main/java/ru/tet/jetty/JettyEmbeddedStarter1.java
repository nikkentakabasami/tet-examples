package ru.tet.jetty;

import javax.naming.NamingException;

import ru.tet.beans.UserDTO;
import ru.tet.jetty.starter.TetJettyServer;
import ru.tet.jetty.starter.TetJettyServerOptions;


/**
 * Запуск через jetty embedded (С использованием класса TetJettyServer)
 * 
 */
public class JettyEmbeddedStarter1 {

	
	public static void main(String[] args) throws Exception {
		
		TetJettyServerOptions options = new TetJettyServerOptions();
		options.setPort(8081);
//		options.setContextPath("/");
		options.setContextPath("/webapp1");

		//подключаем веб-приложение
//		options.setWebAppProjectPath("../../web-apps/web-app-ex1");
//		options.setWebAppProjectFinalName("webapp1");
		
		//подключаем дополнительные статические ресурсы
//		options.getFileSystemBaseResources().add(Path.of("../../web-apps/web-static-roots/src/webapps/alt-root/"));
		
		//подключаем дополнительные классы
//		options.getExtraClasspathes().add(Path.of("../../tet-aux/target/classes/"));
		
		TetJettyServer main = new TetJettyServer();
		
		main.init(options);
		
		//подключение jndi
		bindWebappEx1Jndi(main);
		
		
		main.start();
		main.waitForInterrupt();
		

	}

	public static void bindWebappEx1Jndi(TetJettyServer main) throws NamingException {
		UserDTO user = new UserDTO("nemawashi", "123");
		user.setSecondPassword("654");
		user.setEmail("nema@mail.ru");
		user.setEnabled(true);
        new org.eclipse.jetty.plus.jndi.Resource(main.getServer(), "ttt/testUser1", user);
        new org.eclipse.jetty.plus.jndi.EnvEntry(main.getServer(), "testNumberValue1", Integer.valueOf(4000), false);
        new org.eclipse.jetty.plus.jndi.EnvEntry(main.getServer(), "testStringValue1", "Sabaki kirenai", true);
		
	}
	
	
	
}
