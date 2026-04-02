package ru.tet.jetty.handlers;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloHandlerWithLog extends Handler.Abstract {
	private static final Logger LOG = LoggerFactory.getLogger(HelloHandlerWithLog.class);
	private final String msg;

	public HelloHandlerWithLog(String msg) {
		this.msg = msg;
	}

	@Override
	public boolean handle(Request request, Response response, Callback callback) {
		LOG.info("Got request for {}", request.getHttpURI().toString());
		response.getHeaders().put(HttpHeader.CONTENT_TYPE, "text/plain; charset=utf-8");
		Content.Sink.write(response, true, String.format("%s%n", msg), callback);
		return true;
	}
}
