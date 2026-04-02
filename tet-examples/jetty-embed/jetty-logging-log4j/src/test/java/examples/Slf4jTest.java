package examples;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Проверка Slf4j API.
 * Реализация: log4j.
 * 
 */
public class Slf4jTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jTest.class);
	
	public static void main(String[] args) throws Exception {
		
		LOGGER.info("info message");
		LOGGER.warn("warn message");
		LOGGER.debug("debug message");
		
		
	}
		
	
}
