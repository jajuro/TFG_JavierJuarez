<%@page import="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html ng-app="parser">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link rel="stylesheet" href="css/simple-sidebar.css">
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.css" rel="stylesheet">
        <title>Vista Frase</title>
        <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    </head>
    <body>
        <div id="wrapper">
            <div id="sidebar-wrapper">
                <ul class="sidebar-nav">
                    <li><a href=".">Log In</a></li>
                </ul>
            </div>

            <div id="page-content-wrapper">

                <div class="container-fluid">
                    <h1>Frase analizada</h1>
                    <h2>Bienvenido/a guest</h2>

                    <div class="table-responsive">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th scope="col">ID</th>
                                    <th scope="col">FORM</th>
                                    <th scope="col">LEMMA</th>
                                    <th scope="col">UPOS</th>
                                    <th scope="col">XPOS</th>
                                    <th scope="col">FEATS</th>
                                    <th scope="col">HEAD</th>
                                    <th scope="col">DEPREL</th>
                                    <th scope="col">DEPS</th>
                                    <th scope="col">MISC</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${Palabras}" var="word">
                                <form action="<%=request.getContextPath()%>/ModifiedServlet" method="post">
                                    <tr>
                                        <td><input type="number" id="wordId" name="wordId" value="${word.id}" style="color: black" disabled></td>
                                        <td><input type="text" id="wordForm" name="wordForm" value="${word.form}" style="color: black" disabled></td>
                                        <td><input type="text" id="wordLemma" name="wordLemma" value="${word.lemma}" style="color: black" disabled></td>
                                        <td><input type="text" id="wordUpos" name="wordUpos" value="${word.upos}" style="color: black" disabled></td>
                                        <td><input type="text" id="wordXpos" name="wordXpos" value="${word.xpos}" style="color: black" disabled></td>
                                        <td><input type="text" id="wordFeats" name="wordFeats" value="${word.feats}" style="color: black" disabled></td>
                                        <td><input type="number" id="wordHead" name="wordHead" value="${word.head}" style="color: black" disabled></td>
                                        <td><input type="text" id="wordDeprel" name="wordDeprel" value="${word.deprel}" style="color: black" disabled></td>
                                        <td><input type="text" id="wordDeps" name="wordDeps" value="${word.deps}" style="color: black" disabled></td>
                                        <td><input type="text" id="wordMisc" name="wordMisc" value="${word.misc}" style="color: black" disabled></td>
                                    <input type="hidden" id="frase_original" name="frase_original" value="${word.frase_orig_id}">
                                    <input type="hidden" id="tabla_original" name="tabla_original" value="${word.tabla_orig_id}">
                                    </tr>
                                </form>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <br>
                    <img src="${Grafo}" class="rounded mx-auto d-block" alt="Grafo">
                    <br><br><br>
                    <form action="<%=request.getContextPath()%>/HistorialServlet" method="get">
                        <button type="submit" class="btn btn-primary">Volver</button></td>
                    </form>
                    <div style="background-color: #000000;color: white;text-align: center;padding: 10px;margin-top: 7px;">
                        Escuela Polit�cnica Superior
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
