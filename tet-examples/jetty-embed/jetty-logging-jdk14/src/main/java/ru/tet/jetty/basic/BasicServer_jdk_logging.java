package ru.tet.jetty.basic;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

import ru.tet.jetty.LoggingUtil;
import ru.tet.jetty.handlers.DebugHandler;
import ru.tet.jetty.handlers.HelloHandlerWithLog;

/**
 * Элементарный сервер.
 */
public class BasicServer_jdk_logging {

	
	public static void main(String[] args) throws Exception {
		
		//конфигурирование JUL
        LoggingUtil.config();
		
		Server server = new Server(8081);
		

        Handler.Sequence handlerSequence = new Handler.Sequence();
        handlerSequence.addHandler(new DebugHandler());
        handlerSequence.addHandler(new HelloHandlerWithLog("basic 1"));
		
        server.setHandler(handlerSequence);
		
		server.start();
		server.join();		
		
		
	}
	
	
	
}
