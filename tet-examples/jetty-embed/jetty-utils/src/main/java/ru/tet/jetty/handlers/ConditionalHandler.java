package ru.tet.jetty.handlers;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

/**
 * Пример Handler.Wrapper.
 * Кидает ошибку при определённом адресе, иначе передаёт контроль вложенному Handler-у.
 * 
 */
public class ConditionalHandler extends Handler.Wrapper {
	
	public ConditionalHandler(Handler handler) {
		super(handler);
	}
	
	
	@Override
	public boolean handle(Request request, Response response, Callback callback) throws Exception {

		String path = request.getHttpURI().getPath();

		if (path.startsWith("/wrong")) {
			Response.writeError(request, response, callback, HttpStatus.BAD_REQUEST_400);
			return true;
		}
		
        return super.handle(request, response, callback);
	}
}