package com.example.holidayWeb.Servlets;

import com.example.holidayWeb.DBUtill.EmployeUtill;
import com.example.holidayWeb.Employee;
import com.example.holidayWeb.Holiday;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/userServlet")
public class UserServlet extends HttpServlet {
    private EmployeUtill dbUtill;
    private final String db_url = "jdbc:mysql://localhost:3306/holiday";

    @Override
    public void init (ServletConfig config) throws ServletException{
        super.init(config);
        try{
            dbUtill =  new EmployeUtill(db_url);
        }catch (Exception e ){
            throw new SecurityException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException{
            response.setContentType("text/html");

            String name = request.getParameter("inputEmail");
            String password =  request.getParameter("inputPassword");

            dbUtill.setName(name);
            dbUtill.setPassword(password);

            if (validate(name, password)){
                RequestDispatcher dispatcher = request.getRequestDispatcher("/myLeaves.jsp");
                List<Holiday> myLeaves = null;
                try{

                    myLeaves = dbUtill.getUserHolidays(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
