package examples;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Элементарный сервлет, подключённый через аннотацию.
 * 
 */
@WebServlet("/okaeri4")
public class OkaeriServlet4 extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("text/html;");
    response.getWriter().println("<h1>OkaeriServlet4 from 'web-app-ex11-overlay2'</h1>Okairinasai!");
  }
}
