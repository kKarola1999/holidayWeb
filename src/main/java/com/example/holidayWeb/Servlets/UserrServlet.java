package com.example.holidayWeb.Servlets;

import com.example.holidayWeb.DBUtill.EmployeUtill;
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
    private EmployeUtill dbUtill;
    private final String db_url = "jdbc:mysql://localhost:3306/holiday?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=CET";
    private String nameUndVorname = "";
    private String emploPass ="";

        // Obtain our environment naming context
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
    @Override
    public void init (ServletConfig config) throws ServletException{
        super.init(config);
        try{
            dbUtill =  new EmployeUtill(db_url);
        }catch (Exception e ){
            throw new SecurityException(e);
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
    }



    private void updateHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        int id = Integer.parseInt(request.getParameter("holidayID"));
        LocalDate start = LocalDate.parse(request.getParameter("start"));
        LocalDate end = LocalDate.parse(request.getParameter("end"));
        int days  = (int) ChronoUnit.DAYS.between(start,end)+1;

        if (limitDni(nameUndVorname,days) && start.isBefore(end)){

            // uaktualnienie danych w BD
            dbUtill.updateHoliday(start, end,id);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("AddLeave.html");
//            dispatcher.include();
        }


        // uaktualnienie danych w BD
//        dbUtill.updateHoliday(start, end,id);

        // wyslanie danych do strony z lista telefonow
        listHoliday(request, response);

    }
    private void loadHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie id telefonu z formularza
        String id = request.getParameter("holidayID");

        // pobranie  danych telefonu z BD
        Holiday holiday = dbUtill.getHoliday(id);




        LocalDate startDate = dbUtill.getStartDate(id);
        LocalDate endDate = dbUtill.getEndDate(id);




        // przekazanie telefonu do obiektu request
        request.setAttribute("HOLIDAY", holiday);
        request.setAttribute("holidayID", id);
        request.setAttribute("end", endDate);
        request.setAttribute("start", startDate);

        // wyslanie danych do formmularza JSP (update_phone_form)

        RequestDispatcher dispatcher = request.getRequestDispatcher("/update_holiday_user_form.jsp");
        dispatcher.forward(request, response);

    }



    private void addHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception{
        LocalDate start =  LocalDate.parse(request.getParameter("start"));
        LocalDate end =  LocalDate.parse(request.getParameter("end"));
        boolean akceptacja =  false;
        int days  = (int) ChronoUnit.DAYS.between(start,end)+1;
        int idEmploy = dbUtill.getId(name,password) ;

        if (limitDni(nameUndVorname,days) && start.isBefore(end)){

        if (limitDni(name,days,password)){

            Holiday holiday = new Holiday(start,end,akceptacja,idEmploy,name);
            dbUtill.addHoliday(holiday);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("AddLeave.html");
        }
        listHoliday(request, response);

    }
    private boolean limitDni (String email, long days, String pasword) throws Exception {
        int usedDays = dbUtill.usedDays(email,pasword);
        boolean flaga = false;
        if (dbUtill.getStaz(email)>=10 && days+usedDays<=26){ // todo dokonczyć dla wszystkich urlopów
            flaga =true;
        }else if(dbUtill.getStaz(email)<10 && days+usedDays<=20){flaga = true;}
        return flaga;
    }


    private void deleteHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        String id = request.getParameter("holidayID");

        // usuniecie telefonu z BD
        dbUtill.deleteHoliday(id);

        // wyslanie danych do strony
        listHoliday(request, response);

    }



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


