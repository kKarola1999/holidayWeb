<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="java.util.*" %>
<html>

<head>


    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title>Leaves Managment</title>

    <!-- Bootstrap CSS CDN -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
    <!-- Our Custom CSS -->


    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/table-style.css">




    <!-- Font Awesome JS -->
    <script defer src="https://use.fontawesome.com/releases/v5.0.13/js/solid.js" integrity="sha384-tzzSw1/Vo+0N5UhStP3bvwWPq+uvzCMfrN1fEFe+xBmv1C/AtVX5K0uZtmcHitFZ" crossorigin="anonymous"></script>
    <script defer src="https://use.fontawesome.com/releases/v5.0.13/js/fontawesome.js" integrity="sha384-6OIrr52G08NpOFSZdxxz1xdNSndlD4vdcf/q2myIUVO0VsqaGHJsB0RaBE01VTOY" crossorigin="anonymous"></script>
</head>

<body>
    <div class="wrapper">
        <!-- Sidebar  -->
        <nav id="sidebar">
            <div class="sidebar-header">
                <h3>Menu</h3>
            </div>

            <ul class="list-unstyled components">
                <li>
                    <a href="index.html">Logout</a>
                </li>

            </ul>


        </nav>

        <!-- Page Content  -->
        <div id="content">

            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <div class="container-fluid">

                    <button type="button" id="sidebarCollapse" class="btn btn-info">
                        <i class="fas fa-align-left"></i>
                        <span>Menu</span>
                    </button>


                </div>
            </nav>

            <div class="agile-tables">

                <h3>Emplyees leaves requests</h3>
                <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Data początkowa</th>
                    <th scope="col">Data końcowa</th>
                    <th scope="col">Akceptacja</th>
                    <th scope="col">PracownikId</th>
                    <th scope="col">Email</th>

                </tr>
                </thead>
                <tbody>

<%--                <c:forEach var="tmpHoliday" items="${HOLIDAYS_LIST}">--%>
                <c:forEach var="tmpHoliday" items="${HOLIDAYS_LIST}">
                    <%-- definiowanie linkow--%>
                    <c:url var="updateLink" value="AdminServlet">
                        <c:param name="command" value="LOAD"></c:param>
                        <c:param name="holidayID" value="${tmpHoliday.id}"></c:param>
                    </c:url>

<%--                    <c:url var="deleteLink" value="AdminServlet">--%>
<%--                        <c:param name="command" value="DELETE"></c:param>--%>
<%--                        <c:param name="holidayID" value="${tmpHoliday.id}"></c:param>--%>
<%--                    </c:url>--%>


                    <tr>
                        <th scope="row">${tmpHoliday.id}</th>
                        <td>${tmpHoliday.startUrlopu}</td>
                        <td>${tmpHoliday.koniecUrlopu}</td>
                        <td>${tmpHoliday.akceptacja}</td>
                        <td>${tmpHoliday.pracownikId}</td>
                        <td>${tmpHoliday.email}</td>
                        <td><a href="${updateLink}">
                            <button type="button" class="btn btn-success">Zmień dane</button>
                        </a>


                    </tr>


                </c:forEach>
                </tbody>
                </table>
</br>
                <h3>Delete Requests</h3>
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Data początkowa</th>
                        <th scope="col">Data końcowa</th>
                        <th scope="col">Akceptacja</th>
                        <th scope="col">PracownikId</th>
                        <th scope="col">Email</th>

                    </tr>
                    </thead>
                    <tbody>

                    <%--                <c:forEach var="tmpHoliday" items="${HOLIDAYS_LIST}">--%>
                    <c:forEach var="tmpHoliday" items="${HOlIDAYS_DELETE}">
                        <%-- definiowanie linkow--%>
<%--                        <c:url var="updateLink" value="AdminServlet">--%>
<%--                            <c:param name="command" value="LOAD"></c:param>--%>
<%--                            <c:param name="holidayID" value="${tmpHoliday.id}"></c:param>--%>
<%--                        </c:url>--%>

                        <c:url var="deleteLink" value="AdminServlet">
                            <c:param name="command" value="DELETE"></c:param>
                            <c:param name="holidayID" value="${tmpHoliday.id}"></c:param>
                        </c:url>


                        <tr>
                            <th scope="row">${tmpHoliday.id}</th>
                            <td>${tmpHoliday.startUrlopu}</td>
                            <td>${tmpHoliday.koniecUrlopu}</td>
                            <td>${tmpHoliday.akceptacja}</td>
                            <td>${tmpHoliday.pracownikId}</td>
                            <td>${tmpHoliday.email}</td>
                            <td><a href="${deleteLink}">
                                <button type="button" class="btn btn-success">Usuń</button>
                            </a>


                        </tr>


                    </c:forEach>
                    </tbody>
                </table>
            </div>


        </div>


    <!-- jQuery CDN - Slim version (=without AJAX) -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <!-- Popper.JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js" integrity="sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ" crossorigin="anonymous"></script>
    <!-- Bootstrap JS -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js" integrity="sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm" crossorigin="anonymous"></script>

    <script type="text/javascript">
        $(document).ready(function () {
            $('#sidebarCollapse').on('click', function () {
                $('#sidebar').toggleClass('active');
            });
        });
    </script>
</body>

</html>
