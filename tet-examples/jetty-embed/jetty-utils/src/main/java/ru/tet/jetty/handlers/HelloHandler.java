package ru.tet.jetty.handlers;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

/**
 * Тестовый Handler, возвращающий заданную строку.
 */
public class HelloHandler extends Handler.Abstract {
	private final String msg;

	public HelloHandler(String msg) {
		this.msg = msg;
	}

	@Override
	public boolean handle(Request request, Response response, Callback callback) throws Exception {
        response.setStatus(200);
		response.getHeaders().put(HttpHeader.CONTENT_TYPE, "text/plain; charset=utf-8");
		
        Content.Sink.write(response, true, msg, callback);
        //или так
//		response.write(true, BufferUtil.toBuffer(String.format("%s%n", msg), StandardCharsets.UTF_8), callback);
		return true;
		
		
		
	}
}
