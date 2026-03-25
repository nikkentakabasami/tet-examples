package ru.tet.jakarta.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet {
	final String greeting;

	public HelloServlet() {
		this("Hello");
	}

	public HelloServlet(String greeting) {
		this.greeting = greeting;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
			IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(
				"<h1>" + greeting + " from HelloServlet</h1>");
	}
}
