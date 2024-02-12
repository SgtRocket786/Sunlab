/*
Hammad Ahmad
CMPSC 487W
FALL 2023
hza5260@psu.edu
Project 1
 */


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    private static volatile boolean javaFxLaunched = false;

    public static void myLaunch(Class<? extends Application> applicationClass) {
        if (!javaFxLaunched) { // First time
            Platform.setImplicitExit(false);
            new Thread(()->Application.launch(applicationClass)).start();
            javaFxLaunched = true;
        } else { // Next times
            Platform.runLater(()->{
                try {
                    Application application = applicationClass.newInstance();
                    Stage primaryStage = new Stage();
                    application.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) {


        final String student = "Student";
        final String faculty = "Faculty Member";
        final String staff = "Staff Member";
        final String janitor = "Janitor";


        while (true) {

            Scanner sin = new Scanner(System.in);

            System.out.println("Note: IDs must end with strings STUD, FCLT, STAF, JANI to be valid. These represent Student, Faculty, Staff, and Janitor respectively. The GUI can only be opened once per run and can only be opened by Faculty Members");
            System.out.println("Enter your ID: ");

            String ID = sin.nextLine().trim();

            if (ID.length() < 4) {
                System.out.println("Entry Denied. Insufficient length.");
                continue;
            }

            String typeCode = ID.substring(ID.length() - 4);

            // last 4 chars of userID used to identify usertype

            System.out.println("Entered ID: " + ID);

            String userType = null;

            // userID must end with 1 of 4 endings, else rejected

            if (typeCode.equals("STUD")) {userType = student;}
            else if (typeCode.equals("FCLT")) {userType=faculty;}
            else if (typeCode.equals("STAF")) {userType=staff;}
            else if (typeCode.equals("JANI")) {userType=janitor;}

            else {
                System.out.println("Entry Denied. Invalid usertype.");
                continue;
            }

            if (userType == null) continue;

          //  System.out.println("You are a " + userType);

            String Date = String.valueOf(LocalDate.now());
            String Time = String.valueOf(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
            String Status = "Active";

          //  System.out.println("Date: " + Date);
          //  System.out.println("Time: " + Time);

            users user = new users(ID, userType, Date,Time, Status);

            Boolean isActive = SQL.isActive(user);

            if (!isActive) {
               System.out.println("Entry denied. Your ID was previously suspended. Ask a Faculty Member to reactivate it."); continue;
            }

            Connection connection = SQL.ConnectDB();
            SQL.InsertsUser(user, connection);

            // ID, usertype, date, time, status are to be noted and then inserted into database

            System.out.println();
            System.out.println("Entry Granted");
            System.out.println();

           // SQL.PrintTable(connection);

            if (Objects.equals(userType, faculty)) {

                Application.launch(args);
                // Gui activates

            }


        }


    }




}
