<%--
  Created by IntelliJ IDEA.
  User: Karolina
  Date: 06.05.2021
  Time: 10:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title>Leaves Managment</title>

    <!-- Bootstrap CSS CDN -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
    <!-- Our Custom CSS -->


    <link rel="stylesheet" href="style.css">




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
                <a href="index.html">Home</a>
            </li>
            <li>

            </li>
            <li>
                <a href="AddLeave.jsp">Apply Leave</a>
            </li>
            <li>
                <a href="myLeaves.jsp">My Leaves</a>
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
        <div class="grid-form1">

            <h3>Request For Leave</h3>
            <div class="tab-content tab-pane active" id="center-form">

                    <div class="form-group">
                        <label class="col-sm-4 control-label">From Date  :</label>
                        <form action="UserrServlet" method="get">
                            <input type="hidden" name="command" value="UPDATE"/>
                            <input type="hidden" name="holidayID" value="${holidayID}"/>
                        <div class="col-sm-6">
                            <input type="date" name = "start"  placeholder="From Date" class="form-control1 leave-date" />
                        </div>

                        <label class="alert-danger col-sm-6 col-sm-offset-4" ></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">To Date :</label>
                        <div class="col-sm-6">
                            <input type="date" name = "end" placeholder="To Date" class="form-control1 leave-date" />
                        </div>

                        <label class="alert-danger col-sm-6 col-sm-offset-4"> </label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">To Date :</label>
                        <div class="col-sm-6">
                            <input type="text" name = "akceptacja" placeholder="akceptacja" class="form-control1 akceptacja" />
                        </div>

                        <label class="alert-danger col-sm-6 col-sm-offset-4"> </label>
                    </div>

                    <div class="row">
                        <div class="col-sm-8 col-sm-offset-4">
                            <button type="submit" class="btn btn-primary">Zmień dane</button>
                            <a><button type="button" class="btn btn-default">Cancel</button></a>
                        </div>
                    </div>
                </form>
                </form>


            </div>
        </div>




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
