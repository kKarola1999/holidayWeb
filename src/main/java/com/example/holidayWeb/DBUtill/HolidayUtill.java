package com.example.holidayWeb.DBUtill;

import com.example.holidayWeb.Employee;
import com.example.holidayWeb.Holiday;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayUtill extends DBUtil {
    private DataSource dataSource;

    public HolidayUtill(DataSource dataSource){this.dataSource = dataSource;}

    @Override
    public List<Holiday> getHolidays() throws  Exception{
        List<Holiday> urlopy = new ArrayList<>();
        Connection conn =  null;
        Statement statement =  null;
        ResultSet resultSet =  null;

        try{
            conn = dataSource.getConnection();
            String sql =  "select * from urlopy";
            statement =  conn.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                int id = resultSet.getInt("idUrlopy");
                LocalDate start_urlopu = resultSet.getDate("start_date").toLocalDate();
                LocalDate end_urlopu = resultSet.getDate("end_date").toLocalDate();
                boolean akceptacja = resultSet.getBoolean("akceptacja");
                int idPracownika = resultSet.getInt("Pracownicy_id");
                String email = resultSet.getString("email");

                urlopy.add(new Holiday(id,start_urlopu,end_urlopu,akceptacja,idPracownika,email));
            }
        } finally {
            close(conn, statement, resultSet);
        }
        return urlopy;
    }


}
