package examples;

import java.util.LinkedList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Получает лог-сообщения и записывает их в поле events.
 */
public class CaptureHandler extends Handler {
	
	public static CaptureHandler attach(String logname) {
		CaptureHandler handler = new CaptureHandler();
		Logger.getLogger(logname).addHandler(handler);
		return handler;
	}

	private LinkedList<LogRecord> events = new LinkedList<>();

	@Override
	public void publish(LogRecord record) {
		events.add(record);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}

	public void detach(String logname) {
		Logger.getLogger(logname).removeHandler(this);
	}

	public void assertContainsRecord(String logname, String containsString) {
		for (LogRecord record : events) {
			if (record.getLoggerName().endsWith(logname)) {
				if (record.getMessage().contains(containsString)) {
					// found it
					return;
				}
			}
		}
		fail("Unable to find record matching logname [" + logname + "] containing string [" + containsString + "]");
	}
}
