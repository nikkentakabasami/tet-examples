package ru.tet.jetty;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SystemOutHandler extends Handler {
	@Override
	public void publish(LogRecord record) {
		StringBuilder buf = new StringBuilder();
		buf.append("[").append(record.getLevel().getName()).append("] ");
		buf.append(record.getLoggerName());
		buf.append(": ");
		buf.append(record.getMessage());

		System.out.println(buf);
		if (record.getThrown() != null) {
			record.getThrown().printStackTrace(System.out);
		}
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}
}
