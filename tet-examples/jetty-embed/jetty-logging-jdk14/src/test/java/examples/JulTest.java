package examples;

import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Проверка jul logger-a
 * 
 */
public class JulTest {

	private final static Logger LOGGER = Logger.getLogger(JulTest.class.getName());
	
	public static void main(String[] args) throws Exception {
		
//		LoggingUtil.config();
		
		LogManager.getLogManager().readConfiguration(JulTest.class.getResourceAsStream("/logging.properties"));
//		LogManager.getLogManager().readConfiguration(JulTest.class.getResourceAsStream("/logging2.properties"));
		
		LOGGER.info("info message");
		LOGGER.warning("warning message");
		LOGGER.fine("fine message");
		
		
	}
	
	
}
