<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="parser">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link rel="stylesheet" href="css/simple-sidebar.css">
        <title>Usuario añadido</title>
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
                    <li><a href="<%=request.getContextPath()%>/AddUserPageServlet">Añadir Usuario</a></li>
                    <li><a href="<%=request.getContextPath()%>/CleanBDServlet">Limpiar BD</a></li>
                    <li><a href="<%=request.getContextPath()%>/LogoutServlet">Log Out</a></li>
                </ul>
            </div>

            <div id="page-content-wrapper">

                <div class="container-fluid">
                    <h1>Usuario Añadido</h1>
                    <h2>Bienvenido/a <%=session.getAttribute("Admin")%></h2>
                    <div class="alert alert-success" role="alert">
                        <h4 class="alert-heading">¡Usuario añadido con éxito!</h4>
                        <p>El usuario ${Name} ha sido añadido correctamente</p>
                        <hr>
                        <p class="mb-0">Este usuario podrá parsear y modificar frases</p>
                    </div>
                        <form action="<%=request.getContextPath()%>/LoginServlet" method="get">
                            <button type="submit" class="btn btn-primary">Volver</button>
                        </form>
                    <div style="background-color: #000000;color: white;text-align: center;padding: 10px;margin-top: 7px;">
                        Escuela Politécnica Superior
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
