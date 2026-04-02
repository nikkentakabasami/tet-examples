package ru.tet.jetty.handlers;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.BufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Отладочный Handler.
 * Выводит инфу по запросам в лог.
 * 
 */
public class DebugHandler extends Handler.Abstract.NonBlocking {
	
	private static final Logger LOG = LoggerFactory.getLogger(DebugHandler.class);

	@Override
	public boolean handle(Request request, Response response, org.eclipse.jetty.util.Callback callback)
			throws Exception {
		
		
//		System.out.println("DebugHandler. path:"+request.getHttpURI().getPath());
		LOG.debug("DebugHandler. path:"+request.getHttpURI().getPath());
		
		
		if (request.getHttpURI().getPath().startsWith("/debug")) {
			response.setStatus(200);
			ByteBuffer buffer = BufferUtil.toBuffer("hello", StandardCharsets.UTF_8);
			response.write(true, buffer, callback);
			return true;
		}
		return false;
	}

}