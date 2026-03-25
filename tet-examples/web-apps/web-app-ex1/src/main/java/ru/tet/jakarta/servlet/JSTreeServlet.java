package ru.tet.jakarta.servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.tet.beans.JSTreeNode;
import ru.tet.data.JSTreeDataSamples;

@WebServlet("/jsTree/*")
public class JSTreeServlet extends HttpServlet {

	public static final String GET_NODE1_URL = "/getNode1";
	public static final String GET_NODE2_URL = "/getNode2";
	public static final String ADD_NODE1_URL = "/addNode1";

	int counter;

	JSTreeDataSamples treeData;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		treeData = JSTreeDataSamples.getInstance(); 
		treeData.init();

		super.init(config);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("id");
		System.out.println("id=" + id);

		String pathInfo = request.getPathInfo();

		//возврат json-объекта
		if (pathInfo.startsWith(GET_NODE2_URL)) {

			String nodeId = request.getParameter("id");
			List<JSTreeNode> children = treeData.findChildren(nodeId);

			ObjectMapper mapper = new ObjectMapper();
			response.setContentType("application/json");
			mapper.writeValue(response.getOutputStream(), children);
			return;
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();

		String parentId = req.getParameter("parentId");

		//по умолчанию кодировка обычно ISO-8859-1
		resp.setContentType("text/plain; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");

		//получение json через post-запрос
		if (pathInfo.startsWith(ADD_NODE1_URL)) {

			JSTreeDataSamples.getInstance().addNode(parentId);
			resp.getWriter().write("node added");

		}

	}

}