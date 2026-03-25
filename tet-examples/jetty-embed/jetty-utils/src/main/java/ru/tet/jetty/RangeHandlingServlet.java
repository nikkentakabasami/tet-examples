package ru.tet.jetty;

import java.io.IOException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Сервлет, который всегда кидает ошибку
 */
public class RangeHandlingServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (assertCanHandleRange(req, resp)) {
			// process range
			// we would do something useful here.
		}
	}

	private boolean assertCanHandleRange(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// Normally we would test if range is possible here.
		// But we want to demonstrate error handling, so lets remember the range that failed
		// in the request attributes for later response.
		req.setAttribute("Error.Bad-Range", "bytes */100");
		// Trigger Servlet Error Handling
		resp.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
		return false;
	}
}