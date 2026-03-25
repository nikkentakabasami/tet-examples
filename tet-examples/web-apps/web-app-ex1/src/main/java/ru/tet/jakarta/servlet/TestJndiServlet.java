package ru.tet.jakarta.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tet.MiscWebServices;
import ru.tet.beans.UserDTO;

/**
 * 
 * Элементарный сервлет, показывающий jndi-ресурсы.
 * 
 */
@WebServlet(urlPatterns = "/testJndi")
public class TestJndiServlet extends HttpServlet {
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("TestJndiServlet initiated");
		super.init(config);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
		
		 MiscWebServices ws = MiscWebServices.getInstance();
		
		String testStringValue1 = ws.getJNDIVal("testStringValue1",String.class);
		Integer testNumberValue1 = ws.getJNDIVal("testNumberValue1",Integer.class);
		UserDTO user = ws.getJNDIVal("ttt/testUser1",UserDTO.class);
		
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h1>TestJndiServlet.</h1>");
		out.println("<h3>system jndi resources:</h3>");
		out.format("testStringValue1=%s, <br>testNumberValue1=%d<br>", testStringValue1,testNumberValue1);
		out.format("user=%s%n", user.toString());
		
		
		
	}
	

	
	
}