package examples;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Проверка log4j.
 * 
 */
public class log4jTest {

	static Logger logger = LogManager.getLogger();
	
	public static void main(String[] args) throws Exception {
		
		
		logger.info("info message");
		logger.warn("warn message");
		logger.debug("debug message");
		
		
	}
		
	
}
