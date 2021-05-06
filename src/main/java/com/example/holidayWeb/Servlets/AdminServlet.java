package com.example.holidayWeb.Servlets;

import com.example.holidayWeb.DBUtill.DBUtil;
import com.example.holidayWeb.DBUtill.HolidayUtill;
import com.example.holidayWeb.Holiday;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends HttpServlet {
    private HolidayUtill dbUtil;
    private String password;
    private String email;
    private final String db_url = "jdbc:mysql://localhost:3306/holiday?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=CET";


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {

            dbUtil = new HolidayUtill(db_url);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            // odczytanie zadania
            String command = request.getParameter("command");

            if (command == null)
                command = "LIST";

            switch (command) {

                case "LIST":
                    listHoliday(request, response);
                    break;


                case "LOAD":
                    loadHoliday(request, response);
                    break;

                case "UPDATE":
                    updateHoliday(request, response);
                    break;


                default:
                    listHoliday(request, response);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        email = request.getParameter("inputLogin");
         password = request.getParameter("inputPassword");

        dbUtil.setName(email);
        dbUtil.setPassword(password);

        if (validate(email, password)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/adminLeaves.jsp");

            List<Holiday> holidayList = null;

            try {

                holidayList = dbUtil.getHolidays();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // dodanie listy do obiektu zadania
            request.setAttribute("HOLIDAYS_LIST", holidayList);

            dispatcher.forward(request, response);
        } else {

            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
            dispatcher.include(request, response);
        }


    }

    private void updateHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        int id=Integer.parseInt(request.getParameter("holidayID"));
        boolean akceptacja= Boolean.parseBoolean(request.getParameter("akceptacja"));




        // utworzenie nowego telefonu


        // uaktualnienie danych w BD
        dbUtil.updateHoliday(akceptacja,id);

        // wyslanie danych do strony z lista telefonow
        listHoliday(request, response);

    }

    private void loadHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie id telefonu z formularza
        String id = request.getParameter("holidayID");

        // pobranie  danych telefonu z BD
        Boolean status = dbUtil.getStatus(id);

        // przekazanie telefonu do obiektu request
        request.setAttribute("holidayID", id);
        request.setAttribute("Status", status);

        // wyslanie danych do formmularza JSP (update_phone_form)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update_holiday_form.jsp");
        dispatcher.forward(request, response);

    }


    private void listHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Holiday> holidayList = dbUtil.getHolidays();

        // dodanie listy do obiektu zadania
        request.setAttribute("HOLIDAYS_LIST", holidayList);

        // dodanie request dispatcher
        RequestDispatcher dispatcher = request.getRequestDispatcher("/adminLeaves.jsp");

        // przekazanie do JSP
        dispatcher.forward(request, response);

    }
    private boolean validate(String name, String pass) {
        boolean status = false;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }

        Connection conn = null;

        try {

            conn = DriverManager.getConnection(db_url, name, pass);
            status = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }


}
