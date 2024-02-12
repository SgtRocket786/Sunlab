import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.lang.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.*;

public class SQL {

    public static Connection ConnectDB() {

        try {

           // System.out.println("ConnectDB called");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sundtb", "root", "hammad");
          //  System.out.println("Connection Established");

            return connection;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static void InsertsUser(users user, Connection connection) {

        try {
            String query = "insert into sundtb.sunhist (id, usertype, date, time, status)" + " values (?, ?, ?, ?, ?)";
           // System.out.println("InsertUser start");
            users.printUser(user);


            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString (1, user.id);
          //  System.out.println("1 reached");
            preparedStmt.setString (2, user.usertype);
          //  System.out.println("2 reached");
            preparedStmt.setString  (3, user.date);
           // System.out.println("3 reached");
            preparedStmt.setString(4, user.time);
          //  System.out.println("4 reached");
            preparedStmt.setString (5, user.status);
           // System.out.println("5 reached");


            preparedStmt.executeUpdate();     // causing problem
          //  System.out.println("executed and inserted");

            // connection.close();


        } catch (SQLException e){
           // System.out.println("Didn't insert");
        }
    }

    public static void PrintTable(Connection connection) {

        try {

            String q1 = "select * from sundtb.sunhist";
            Statement stmt1 = connection.createStatement();
            ResultSet rset1 = stmt1.executeQuery(q1);

            System.out.println("PrintTable called");

            System.out.println();
            System.out.println("QUERY 1");
            System.out.println("SUNLAB History");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------");
            System.out.format("%-20s | %-20s | %-20s | %-25s | %-20s |\n", "ID", "User Type", "Date", "Time", "Status");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------");


            while (rset1.next()) {

                String uID = rset1.getString("id");
                String uType = rset1.getString("usertype");
                String date = rset1.getString("date");
                String time = rset1.getString("time");
                String status = rset1.getString("status");

                System.out.format("%-20s | %-20s | %-20s | %-25s | %-20s |\n", uID, uType, date, time, status);
            }

            System.out.println("-------------------------------------------------------------------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------");
            System.out.println();

        }catch (SQLException e){}


    }

    public static Boolean isExists (users user){

        try{

            String exists = "select id from sundtb.sunhist where id = '"+user.getId()+"'";
            Connection connection = ConnectDB();
            Statement stmt1 = connection.createStatement();
            ResultSet rset1 = stmt1.executeQuery(exists);

            while (rset1.next()) {

                String existence = rset1.getString("id");

                if (Objects.equals(existence, user.getId())) {
                  //  System.out.println("User previously exists.");
                    return true;
                } else {
                  //  System.out.println("User is new");
                    return false;
                }

            }

        }catch (SQLException e){
          //  System.out.println("is exist failed");
        }

        return false;
    }

    public static Boolean isActive(users user){

        try {
           // System.out.println("trying isActive");
            String findstatus = "select sunhist.status from sundtb.sunhist where id = '"+user.getId()+"'";

            if (isExists(user)) {

                Connection connection = ConnectDB();
                Statement stmt1 = connection.createStatement();
                ResultSet rset1 = stmt1.executeQuery(findstatus);

                String uStatus;

                while (rset1.next()) {

                    uStatus = rset1.getString("status");
                   // System.out.println("Received " + uStatus + " from database.");

                    if (!Objects.equals(user.getStatus(), uStatus)) {
                       // System.out.println("User was suspended");
                        return false;
                    }

                    else if (Objects.equals(user.getStatus(), uStatus)) {
                      //  System.out.println("User is permitted");
                        return true;
                    }
                }
            }

            else {
              //  System.out.println("User is permitted and is new");
                return true;
            }

        } catch (SQLException e){System.out.println("isActive failed");}

       // System.out.println("isActive outside exception");
        return false;
    }

    public static ObservableList<users> getDatauser(){ // problem in this function only showing and repeating one STUD

        Connection conn = ConnectDB();
      //  System.out.println("getDatauser called");
        ObservableList<users> list = FXCollections.observableArrayList();

        try{

            String q = "select * from sundtb.sunhist";
            PreparedStatement ps = conn.prepareStatement(q);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){

                list.add(new users(rs.getString("id"), rs.getString("usertype"), rs.getString("date"), rs.getString("time"), rs.getString("status")));
               // System.out.println((rs.getString("id") + rs.getString("usertype") + rs.getString("date") + rs.getString("time") + rs.getString("status")));
               // System.out.println(list.toString());
            }

        } catch (SQLException e) {}

        return list;
    }


}

