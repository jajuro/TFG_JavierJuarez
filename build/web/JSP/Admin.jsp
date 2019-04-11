<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="parser">
    <head>
        <title>Analizador Sintáctico de Dependencias en Español</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="css/simple-sidebar.css">
        <script>document.write('<base href="' + document.location + '" />');</script>
        <script data-require="angular.js@1.4.x" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.5/angular.min.js" data-semver="1.4.5"></script>
        <script src="https://d3js.org/d3.v4.min.js"></script>

        <script>
            var app = angular.module('parser', []);
            app.controller('MainCtrl', function ($scope) {
                $scope.text = "";
            })
                    .directive('plkrFileDropZone', [function () {
                            return {
                                restrict: 'EA',
                                scope: {content: '='},
                                link: function (scope, element, attrs) {

                                    scope.content = "";
                                    var processDragOverOrEnter;
                                    processDragOverOrEnter = function (event) {
                                        if (event !== null) {
                                            event.preventDefault();
                                        }
                                        event.dataTransfer.effectAllowed = 'copy';
                                        return false;
                                    };
                                    element.bind('dragover', processDragOverOrEnter);
                                    element.bind('dragenter', processDragOverOrEnter);
                                    element.bind('drop', handleDropEvent);
                                    function insertText(loadedFile) {

                                        scope.content = loadedFile.target.result;
                                        scope.$apply();
                                    }

                                    function handleDropEvent(event) {

                                        if (event !== null) {
                                            event.preventDefault();
                                        }
                                        var reader = new FileReader();
                                        reader.onload = insertText;
                                        reader.readAsText(event.dataTransfer.files[0]);
                                    }
                                }
                            };
                        }]);
        </script>
    </head>

    <body ng-controller="MainCtrl">
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
                    <h1>Analizador Sintáctico de Dependencias en Español</h1>
                    <% if (session.getAttribute("Admin") != null) {%>
                    <h2>Bienvenido/a <%=session.getAttribute("Admin")%></h2>
                    <%} else {%>
                    <h2>Bienvenido/a <%=session.getAttribute("User")%></h2><% }%>
                    <div style="overflow: auto">
                        <div>
                            <form class="form-group" action="<%=request.getContextPath()%>/ParseServlet" method="post" id="myForm">
                                <div class="drop-zone" plkr-file-drop-zone content='text'>
                                    <textarea class="form-control" style="width: 100%;resize: vertical;min-height: 30px;border: 2px;background-color: #f2f2f2;height: 300px;"
                                              name="text" rows="25" placeholder="Escribe aquí tu texto o arrastra tu fichero .txt">{{text}}</textarea>
                                </div>
                                <div class="col-auto my-1">
                                    <div class="form-check">
                                        <input type="checkbox" name="parser" value="udpipe" class="form-check-input" id="autoSizingCheck1">
                                        <label class="form-check-label" for="autoSizingCheck1">UDPipe</label>
                                    </div>
                                    <div class="form-check">
                                        <input type="checkbox" name="parser" value="spacy" class="form-check-input" id="autoSizingCheck2">
                                        <label class="form-check-label" for="autoSizingCheck2">Spacy</label>
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary">Parsear</button>
                            </form>  
                            <form action="<%=request.getContextPath()%>/FileServlet" method="post" enctype="multipart/form-data">
                                <div class="form-group">
                                    <label for="inputFile">Sube un fichero</label>
                                    <input type="file" name="fichero" class="form-control-file" id="inputFile">
                                    <div class="col-auto my-1">
                                        <div class="form-check">
                                            <input type="checkbox" name="parser" value="udpipe" class="form-check-input" id="autoSizingCheck1">
                                            <label class="form-check-label" for="autoSizingCheck1">UDPipe</label>
                                        </div>
                                        <div class="form-check">
                                            <input type="checkbox" name="parser" value="spacy" class="form-check-input" id="autoSizingCheck2">
                                            <label class="form-check-label" for="autoSizingCheck2">Spacy</label>
                                        </div>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Subir</button>
                                </div>
                            </form>

                        </div>

                    </div>

                    <div style="background-color: #000000;color: white;text-align: center;padding: 10px;margin-top: 7px;">
                        Escuela Politécnica Superior
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>