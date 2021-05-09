package com.example.holidayWeb.Servlets;

import com.example.holidayWeb.DBUtill.EmployeUtill;
import com.example.holidayWeb.Employee;
import com.example.holidayWeb.Holiday;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.swing.*;
import java.awt.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WebServlet(name = "UserrServlet", value = "/UserrServlet")
public class UserrServlet extends HttpServlet {
    private DataSource dataSource;
    private EmployeUtill dbUtill;
    private String name ="";
    private String password ="";

    public UserrServlet() {
        Context initCtx = null;
        try {
            initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            dataSource = (DataSource)
                    envCtx.lookup("jdbc/holiday_web");

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }



    public void init (ServletConfig config) throws ServletException {
        // Obtain our environment naming context
        super.init();

        try {

            dbUtill = new EmployeUtill(dataSource) {
            };

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

                case "ADD":
                    addHoliday(request, response);
                    break;
                case "ADDE":
                    addEmployee(request,response);
                    break;

                case "LOAD":
                    loadHoliday(request, response);
                    break;

                case "UPDATE":
                    updateHoliday(request, response);

                    break;

                case "DELETE":
                    deleteHoliday(request, response);
                    break;

                default:
                    listHoliday(request, response);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

    }

    /**
     * this method is checking if user which is looging exist in database. If not it is resetting login.html view.
     * If there exist user then it is opening myLeaves site.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        name = request.getParameter("inputEmail");
        password = request.getParameter("inputPassword");




            try {
                if (dbUtill.getPassword(name,password)==false) {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/myLeaves.jsp");
                    List<Holiday> myLeaves = null;
                    try {

                        myLeaves = dbUtill.getUserHolidays(name,password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    request.setAttribute("myLeaves", myLeaves);
                    dispatcher.forward(request, response);
                } else {
                   RequestDispatcher dispatcher = request.getRequestDispatcher("login.html");
                    dispatcher.forward(request, response);
                }
                } catch(Exception e){
                    e.printStackTrace();
                }

        }




    /**
     * This method  let users changing theirs leaves dates. It is getting params from update_holiday_form.jsp.
     * @param request
     * @param response
     * @throws Exception
     */
    private void updateHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        int id = Integer.parseInt(request.getParameter("holidayID"));
        LocalDate start = LocalDate.parse(request.getParameter("start"));
        LocalDate end = LocalDate.parse(request.getParameter("end"));
        int days  = (int) ChronoUnit.DAYS.between(start,end)+1;

        if (limitDni(name,days,password) && start.isBefore(end)){

            // uaktualnienie danych w BD
            dbUtill.updateHoliday(start, end,id);
            listHoliday(request, response);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("AddLeave.jsp");
            listHoliday(request, response);

        }


    }

    /**
     * This method is sending data of chosen holiday to update_holiday_user_form.
     * @param request
     * @param response
     * @throws Exception
     */



    private void loadHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie id urlopu z formularza
        String id = request.getParameter("holidayID");

        // pobranie  danych urlopu z BD
        Holiday holiday = dbUtill.getHoliday(id);




        LocalDate startDate = dbUtill.getStartDate(id);
        LocalDate endDate = dbUtill.getEndDate(id);




        // przekazanie urlopu do obiektu request
        request.setAttribute("HOLIDAY", holiday);
        request.setAttribute("holidayID", id);
        request.setAttribute("end", endDate);
        request.setAttribute("start", startDate);

        // wyslanie danych do formmularza JSP

        RequestDispatcher dispatcher = request.getRequestDispatcher("/update_holiday_user_form.jsp");
        dispatcher.forward(request, response);

    }

    /**
     * That method allows users to create new requests for leaves. If every thing  is okey it returns user
     * to site with his leaves, if not it is resetting form for creating new request. This function don't
     * allow users to create request with incorrect date.
     * @param request
     * @param response
     * @throws Exception
     */



    private void addHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LocalDate start = LocalDate.parse(request.getParameter("start"));
        LocalDate end = LocalDate.parse(request.getParameter("end"));
        boolean akceptacja = false;
        int days = (int) ChronoUnit.DAYS.between(start, end) + 1;
        int idEmploy = dbUtill.getId(name, password);

        if (limitDni(name,days,password) && start.isBefore(end)) {


            Holiday holiday = new Holiday(start, end, akceptacja, idEmploy, name);
            dbUtill.addHoliday(holiday);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("AddLeave.jsp");
        }
        listHoliday(request, response);

    }
    /**
     * This functions allow unregistered users to create their profile.It is getting params from register.jsp,
     * and then create employee object which is inserting to database.
     * @param request
     * @param response
     * @throws Exception
     */
    private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        int seniority = Integer.parseInt(request.getParameter("seniority"));
        String etat = request.getParameter("etat");
        int extraDays = Integer.parseInt(request.getParameter("extraDays"));
        String password = request.getParameter("password");

        // utworzenie obiektu klasy Employee
        Employee employee=new Employee(name,seniority,etat,extraDays,email,password);

        // dodanie nowego obiektu do BD
        dbUtill.addEmployee(employee);

        RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
        dispatcher.forward(request, response);


    }

    /**
     * this function is checking if user do not create leave request without any free days.
     * @param email
     * @param days
     * @param pasword
     * @return
     * @throws Exception
     */

    private boolean limitDni (String email, long days, String pasword) throws Exception {
        int usedDays = dbUtill.usedDays(email,pasword);
        boolean flaga = false;
        if (dbUtill.getStaz(email,password)>=10 && days+usedDays<=26){ // todo dokonczyć dla wszystkich urlopów
            flaga =true;
        }else if(dbUtill.getStaz(email,password)<10 && days+usedDays<=20){flaga = true;}
        return flaga;
    }


    /**
     * This method delete chosen leave.
     * @param request
     * @param response
     * @throws Exception
     */
    private void deleteHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        String id = request.getParameter("holidayID");

        // usuniecie telefonu z BD
        dbUtill.deleteHoliday(id);

        // wyslanie danych do strony
        listHoliday(request, response);

    }
    /**
     * Getting all user  leaves.
     * @param request
     * @param response
     * @throws Exception
     */


    private void listHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Holiday> holidayList = dbUtill.getUserHolidays(name,password);

        // dodanie listy do obiektu zadania
        request.setAttribute("myLeaves", holidayList);

        // dodanie request dispatcher
        RequestDispatcher dispatcher = request.getRequestDispatcher("/myLeaves.jsp");

        // przekazanie do JSP
        dispatcher.forward(request, response);

    }





}


