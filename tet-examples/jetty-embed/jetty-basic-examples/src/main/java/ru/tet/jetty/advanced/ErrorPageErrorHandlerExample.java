package ru.tet.jetty.advanced;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.util.IO;

import jakarta.servlet.http.HttpServletResponse;
import ru.tet.jetty.ErrorsServlet;
import ru.tet.jetty.RangeHandlingServlet;

/**
 * Demonstration on using the {@link ErrorPageErrorHandler} with a {@link ServletContextHandler}
 */
public class ErrorPageErrorHandlerExample {

	public static void main(String[] args) throws Exception {
		Server server = new Server(9090);
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		
		//сервлет, кидающий ошибку
		context.addServlet(RangeHandlingServlet.class, "/demo");
		
		//сервлет, обрабатывающий ошибки
		context.addServlet(ErrorsServlet.class, "/errors");
		
		//Для обслуживания статического контента из context.baseResource
		context.addServlet(DefaultServlet.class, "/");

//		ServletHolder holderDef = new ServletHolder("default", DefaultServlet.class);
//		holderDef.setInitParameter("dirAllowed", "true");
//		context.addServlet(holderDef, "/");
		
		
		
		ErrorPageErrorHandler errorPageErrorHandler = new ErrorPageErrorHandler();
		errorPageErrorHandler.addErrorPage(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE, "/errors");
		context.setErrorHandler(errorPageErrorHandler);

		Handler.Sequence handlers = new Handler.Sequence();
		handlers.addHandler(context);
		handlers.addHandler(new DefaultHandler()); // handle non-context errors
		server.setHandler(context);
		server.start();

		/*
		try {
			demonstrateErrorHandling(server.getURI().resolve("/"));
		} finally {
			server.stop();
		}
		*/
		
	}

	private static void demonstrateErrorHandling(URI serverBaseUri) throws IOException {
		HttpURLConnection http = (HttpURLConnection) serverBaseUri.resolve("/demo").toURL().openConnection();
		dumpRequestResponse(http);
		System.out.println();
		try (InputStream in = http.getInputStream()) {
			System.out.println(IO.toString(in, UTF_8));
		}
	}

	private static void dumpRequestResponse(HttpURLConnection http) throws IOException {
		System.out.println();
		System.out.println("----");
		System.out.printf("%s %s HTTP/1.1%n", http.getRequestMethod(), http.getURL());
		System.out.println("----");
		System.out.printf("%s%n", http.getHeaderField(null));
		http.getHeaderFields().entrySet().stream()
				.filter(entry -> entry.getKey() != null)
				.forEach((entry) -> System.out.printf("%s: %s%n", entry.getKey(), http.getHeaderField(entry.getKey())));
	}

}
