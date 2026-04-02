package ru.tet.jetty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DebugServlet extends HttpServlet {
	private final AtomicInteger count = new AtomicInteger();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		String servletPath;
		String pathInfo;
		try {
			servletPath = request.getServletPath();
		} catch (IllegalArgumentException iae) {
			servletPath = iae.toString();
		}
		try {
			pathInfo = request.getPathInfo();
		} catch (IllegalArgumentException iae) {
			pathInfo = iae.toString();
		}

		PrintWriter out = response.getWriter();
		out.printf("<h3>DebugServlet</h3>%n");
		out.printf("request.count=%d%n", count.incrementAndGet());
		out.printf("requestURI=%s%n", requestURI);
		out.printf("servletPath=%s%n", servletPath);
		out.printf("pathInfo=%s%n", pathInfo);
        out.printf("Method: %s%n", request.getMethod());
        out.printf("Request-URI: %s%n", request.getRequestURI());
        out.printf("Version: %s%n", request.getProtocol());
        out.printf("Request-URL: %s%n", request.getRequestURL());
		
		
	}
}
