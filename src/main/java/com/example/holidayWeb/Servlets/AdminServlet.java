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


    /**
     * Initialize AdminServlet
     * @param config
     * @throws ServletException
     */

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
                    listHolidayToDelete(request,response);
                    break;


                case "LOAD":
                    loadHoliday(request, response);
                    listHolidayToDelete(request,response);
                    break;

                case "UPDATE":
                    updateHoliday(request, response);
//                    listHolidayToDelete(request,response);
                    break;
                case "DELETE":
                    deleteHoliday(request, response);
                    break;

                default:
                    listHoliday(request, response);
                    listHolidayToDelete(request,response);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

    }

    /**
     * this method is checking if person which is trying to login exist. If it is true then it moving to
     * adminLeaves.jsp site, if not it restart login view.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

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
            List<Holiday> toDelete = null;
            try {

                holidayList = dbUtil.getHolidays();
                toDelete = dbUtil.getHolidayToDelete();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // dodanie listy do obiektu zadania
            request.setAttribute("HOLIDAYS_LIST", holidayList);
            request.setAttribute("HOlIDAYS_DELETE", toDelete );
            dispatcher.forward(request, response);

        } else {

            RequestDispatcher dispatcher = request.getRequestDispatcher("/loginAdmin.html");
            dispatcher.include(request, response);
        }


    }
    /**
     * Allow admin to change acceptation for request of chosen holiday.
     * @param request
     * @param response
     * @throws Exception
     */

    private void updateHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        int id=Integer.parseInt(request.getParameter("holidayID"));
        boolean akceptacja= Boolean.parseBoolean(request.getParameter("akceptacja"));

        // uaktualnienie danych w BD
        dbUtil.updateHoliday(akceptacja,id);

        // wyslanie danych do strony z listą urlopów
        listHoliday(request, response);

    }
    /**
     * Loading chosen holiday data, and initialize update_holiday_form.jsp
     * @param request
     * @param response
     * @throws Exception
     */

    private void loadHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie id urlopu z formularza
        String id = request.getParameter("holidayID");

        // pobranie  danych urlopu z BD
        Boolean status = dbUtil.getStatus(id);

        // przekazanie urlopu do obiektu request
        request.setAttribute("holidayID", id);
        request.setAttribute("Status", status);

        // wyslanie danych do formmularza JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update_holiday_form.jsp");
        dispatcher.forward(request, response);

    }

    /**
     * This method call function to create List of holiday objects which is used to create table in adminLeaves site.
     * @param request
     * @param response
     * @throws Exception
     */
    private void listHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Holiday> holidayList = dbUtil.getHolidays();

        // dodanie listy do obiektu zadania
        request.setAttribute("HOLIDAYS_LIST", holidayList);

        // dodanie request dispatcher
        RequestDispatcher dispatcher = request.getRequestDispatcher("/adminLeaves.jsp");

        // przekazanie do JSP
        dispatcher.forward(request, response);

    }

    /**
     * This method call function to create List of holiday objects which is used to create table in adminLeaves site
     * holidays that were requested to be deleted.
     * @param request
     * @param response
     * @throws Exception
     */

    private  void listHolidayToDelete (HttpServletRequest request, HttpServletResponse response) throws  Exception{
        List<Holiday> holidayList = dbUtil.getHolidayToDelete();
        request.setAttribute("HOlIDAYS_DELETE", holidayList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/adminLeaves.jsp");
        dispatcher.forward(request, response);

    }
    /**
     * Allows admin to delete requested holidays.
     * @param request
     * @param response
     * @throws Exception
     */

    private void deleteHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        String id = request.getParameter("holidayID");

        // usuniecie urlopu z BD
        dbUtil.adminDeleteHoliday(id);

        // wyslanie danych do strony
        listHoliday(request, response);

    }
    /**
     * this method validate if user exist
     * @param name
     * @param pass
     * @return
     */

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
