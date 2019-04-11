<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="parser">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link rel="stylesheet" href="css/simple-sidebar.css">
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.css" rel="stylesheet">
        <title>Historial</title>
        <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    </head>
    <body>
        <div id="wrapper">
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <li><a href="<%=request.getContextPath()%>/LoginServlet">Parser</a></li>
                    <li><a href="<%=request.getContextPath()%>/ModifyServlet">Historial</a></li>
                        <% if (session.getAttribute("Admin") != null) {%>
                    <li><a href="<%=request.getContextPath()%>/AddUserPageServlet">Añadir Usuario</a></li>
                    <li><a href="<%=request.getContextPath()%>/CleanBDServlet">Limpiar BD</a></li>
                        <% }%>
                    <li><a href="<%=request.getContextPath()%>/LogoutServlet">Log Out</a></li>
                </ul>
            </div>

            <div id="page-content-wrapper">

                <div class="container-fluid">
                    <h1>Historial de frases analizadas</h1>
                    <% if (session.getAttribute("Admin") != null) {%>
                    <h2>Bienvenido/a <%=session.getAttribute("Admin")%></h2>
                    <%} else {%>
                    <h2>Bienvenido/a <%=session.getAttribute("User")%></h2><% }%>
                    <table class="table table-dark">
                        <thead>
                            <tr>
                                <th scope="col">FRASE</th>
                                <th scope="col">MODIFICADA</th>
                                <th scope="col">SPACY</th>
                                <th scope="col">UDPIPE</th>
                                 <th scope="col"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${Frases}" var="frase">
                            <form method="get" action="<%=request.getContextPath()%>/PhrasesServlet">
                                <tr>
                                    <th scope="row">${frase.frase}</th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${frase.modificada==true}">
                                                &#x1F5F9;
                                            </c:when>
                                            <c:otherwise>
                                                &#x2610;
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${frase.spacy==true}">
                                                &#x1F5F9;
                                            </c:when>
                                            <c:otherwise>
                                                &#x2610;
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${frase.udpipe==true}">
                                                &#x1F5F9;
                                            </c:when>
                                            <c:otherwise>
                                                &#x2610;
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                <input type="hidden" id="frase_original" name="frase_original" value="${frase.frase_orig_id}">
                                <td><button type="submit" class="btn btn-primary">Ver</button></td>
                                </tr>
                            </form>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div style="background-color: #000000;color: white;text-align: center;padding: 10px;margin-top: 7px;">
                        Escuela Politécnica Superior
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

