package com.example.holidayWeb.Servlets;

import com.example.holidayWeb.DBUtill.EmployeUtill;
import com.example.holidayWeb.Holiday;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "UserrServlet", value = "/UserrServlet")
public class UserrServlet extends HttpServlet {
    private EmployeUtill dbUtill;
    private final String db_url = "jdbc:mysql://localhost:3306/holiday?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=CET";
    private String nameUndVorname = "";
    private String emploPass ="";

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
                    loadPhone(request, response);
                    break;

                case "UPDATE":
                    updatePhone(request, response);
                    break;

                case "DELETE":
                    deletePhone(request, response);
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

        String name = request.getParameter("inputEmail");
        String password = request.getParameter("inputPassword");

        nameUndVorname = name;
        emploPass = password;

        dbUtill.setEmail(name);
        dbUtill.setPassword(password);

        if (validate(name, password)) {
            try {
//                if (password.equals(dbUtill.getPassword(name))) {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/myLeaves.jsp");
                    List<Holiday> myLeaves = null;
                    try {

                        myLeaves = dbUtill.getUserHolidays(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    request.setAttribute("myLeaves", myLeaves);
                    dispatcher.forward(request, response);
//                } else {
//                    RequestDispatcher dispatcher = request.getRequestDispatcher("login.html");
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void updatePhone(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        int id = Integer.parseInt(request.getParameter("holidayID"));
        LocalDate startUrlopu = LocalDate.parse(request.getParameter("start"));
        LocalDate koniecUrlopu = LocalDate.parse(request.getParameter("end"));
        boolean akceptacja= Boolean.parseBoolean(request.getParameter("akceptacja"));
        int idPracownik= Integer.parseInt(request.getParameter("idPracownikaInput"));
        String email=request.getParameter("emailInput");


        // utworzenie nowego telefonu
        Holiday holiday = new Holiday(id,startUrlopu,koniecUrlopu,akceptacja,idPracownik,email);

        // uaktualnienie danych w BD
        dbUtill.updateHoliday(holiday);

        // wyslanie danych do strony z lista telefonow
        listHoliday(request, response);

    }

    private void loadPhone(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie id telefonu z formularza
        String id = request.getParameter("holidayID");

        // pobranie  danych telefonu z BD
        Holiday holiday = dbUtill.getHoliday(id);

        // przekazanie telefonu do obiektu request
        request.setAttribute("HOLIDAY", holiday);

        // wyslanie danych do formmularza JSP (update_phone_form)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update_holiday2_form.jsp");
        dispatcher.forward(request, response);

    }

    private void addHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception{
        LocalDate start =  LocalDate.parse(request.getParameter("start"));
        LocalDate end =  LocalDate.parse(request.getParameter("end"));
        boolean akceptacja =  false;
        int idEmploy = dbUtill.getId(nameUndVorname, emploPass) ;
        String name = nameUndVorname;

        Holiday holiday = new Holiday(start,end,akceptacja,idEmploy,nameUndVorname);
        dbUtill.addHoliday(holiday);

    }


    private void deletePhone(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // odczytanie danych z formularza
        String id = request.getParameter("phoneID");

        // usuniecie telefonu z BD
        dbUtill.deleteHoliday(id);

        // wyslanie danych do strony z lista telefonow
        listHoliday(request, response);

    }



    private void listHoliday(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Holiday> holidayList = dbUtill.getUserHolidays(nameUndVorname);

        // dodanie listy do obiektu zadania
        request.setAttribute("myLeaves", holidayList);

        // dodanie request dispatcher
        RequestDispatcher dispatcher = request.getRequestDispatcher("/myLeaves.jsp");

        // przekazanie do JSP
        dispatcher.forward(request, response);

    }

    private  boolean validate (String email, String password){
        boolean status =  false;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection =  null;

        try{
            connection = DriverManager.getConnection(db_url,email,password);
            status = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }return status;
    }




}


