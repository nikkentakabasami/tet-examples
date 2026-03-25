<%@page import="ru.tet.beans.JSTreeNode"%>
<%@page import="ru.tet.data.JSTreeDataSamples"%>
<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
//предоставляет данные для jstree
//используется в демке: 610_jstree_demo.html
//Пример запроса: jstreeDemo1Nodes.jsp?id=tn1

JSTreeDataSamples data = JSTreeDataSamples.getInstance();

data.init();

String nodeId = request.getParameter("id");
List<JSTreeNode> children = data.findChildren(nodeId);

pageContext.setAttribute("children", children);
%>

<ul>

  <c:forEach var="node" items="${children}">
    <c:if test="${node.hasChilds()}">
      <li id="${node.id}" class="jstree-closed">${node.text}</li>
    </c:if>
    <c:if test="${!node.hasChilds()}">
      <li id="${node.id}">${node.text}</li>
    </c:if>

  </c:forEach>

</ul>




