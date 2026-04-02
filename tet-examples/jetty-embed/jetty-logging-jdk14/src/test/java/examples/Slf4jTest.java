package examples;


import java.util.logging.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Проверка Slf4j API.
 * Реализация: jul.
 * 
 */
public class Slf4jTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jTest.class);
	
	public static void main(String[] args) throws Exception {
//		LoggingUtil.config();
		
		LogManager.getLogManager().readConfiguration(Slf4jTest.class.getResourceAsStream("/logging.properties"));
//		LogManager.getLogManager().readConfiguration(Slf4jTest.class.getResourceAsStream("/logging2.properties"));
		
		LOGGER.info("info message");
		LOGGER.warn("warn message");
		LOGGER.debug("debug message");
		
		
	}
		
	
}
