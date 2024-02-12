/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;


public class Controller implements Initializable {


    @FXML
    private TableView<users> table_sun;

    @FXML
    private TableColumn<users, String> col_id;

    @FXML
    private TableColumn<users, String> col_usertype;

    @FXML
    private TableColumn<users, String> col_date;

    @FXML
    private TableColumn<users, String> col_time;

    @FXML
    private TableColumn<users, String> col_status;

    @FXML
    private TextField txt_status;

    @FXML
    private TextField txt_time;

    @FXML
    private TextField txt_date;

    @FXML
    private TextField txt_usertype;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField filterField;

    ObservableList<users> listM;
    ObservableList<users> dataList;


    int index = -1;

    Connection conn =null;
    ResultSet rs = null;
    PreparedStatement pst = null;


    public void Add_users (){
        conn = SQL.ConnectDB();
        String sql = "insert into users (id,usertype,date,time,status)values(?,?,?,?,? )";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txt_id.getText());
            pst.setString(2, txt_usertype.getText());
            pst.setString(3, txt_date.getText());
            pst.setString(4, txt_time.getText());
            pst.setString(5, txt_status.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Users add succes");
            UpdateTable();
            search_user();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }


    //////// methode select users ///////
    @FXML
    void getSelected (MouseEvent event){
        index = table_sun.getSelectionModel().getSelectedIndex();
        if (index <= -1){

            return;
        }
        txt_id.setText(col_id.getCellData(index).toString());
        txt_usertype.setText(col_usertype.getCellData(index).toString());
        txt_date.setText(col_date.getCellData(index).toString());
        txt_time.setText(col_time.getCellData(index).toString());
        txt_status.setText(col_status.getCellData(index).toString());

    }

    public void Edit (){
        try {
            conn = SQL.ConnectDB();
            String value1 = txt_id.getText();
            String value2 = txt_usertype.getText();
            String value3 = txt_date.getText();
            String value4 = txt_time.getText();
            String value5 = txt_status.getText();
            String sql = "update sunhist set id= '"+value1+"',usertype= '"+value2+"',date= '"+
                    value3+"',time= '"+value4+"',status= '"+value5+"' where id='"+value1+"' ";
            pst= conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Updated");
            UpdateTable();
            search_user();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void Delete(){
        conn = SQL.ConnectDB();
        String sql = "delete from table_sun where id = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txt_id.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Delete");
            UpdateTable();
            search_user();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void activate () {

        try {
            conn = SQL.ConnectDB();
            String val1 = txt_id.getText();

            String activate = "UPDATE sunhist SET sunhist.status = 'Active' WHERE id = '"+val1+"'";
            pst = conn.prepareStatement(activate);
            pst.execute();
            UpdateTable();
            search_user();
            JOptionPane.showMessageDialog(null, "User ID " + val1 + " Activated");
           // System.out.println("User ID " + val1 + " activated");

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "User not activated");
        }

    }

    public void deactivate () {

        conn = SQL.ConnectDB();

        try {
            conn = SQL.ConnectDB();
            String val1 = txt_id.getText();

            String deactivate = "UPDATE sunhist SET sunhist.status = 'Deactivated' WHERE id = '"+val1+"'";
            pst = conn.prepareStatement(deactivate);
            pst.execute();
            UpdateTable();
            search_user();
            JOptionPane.showMessageDialog(null, "User ID " + val1 + " Deactivated");
          //  System.out.println("User ID " + val1 + " deactivated");

        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "User not deactivated");
        }
    }

    public void UpdateTable(){
        col_id.setCellValueFactory(new PropertyValueFactory<users,String>("id"));
        col_usertype.setCellValueFactory(new PropertyValueFactory<users,String>("usertype"));
        col_date.setCellValueFactory(new PropertyValueFactory<users,String>("date"));
        col_time.setCellValueFactory(new PropertyValueFactory<users,String>("time"));
        col_status.setCellValueFactory(new PropertyValueFactory<users,String>("status"));

        listM = SQL.getDatauser();
        table_sun.setItems(listM);
    }


    @FXML
    void search_user() {
        col_id.setCellValueFactory(new PropertyValueFactory<users,String>("id"));
        col_usertype.setCellValueFactory(new PropertyValueFactory<users,String>("usertype"));
        col_date.setCellValueFactory(new PropertyValueFactory<users,String>("date"));
        col_time.setCellValueFactory(new PropertyValueFactory<users,String>("time"));
        col_status.setCellValueFactory(new PropertyValueFactory<users,String>("status"));

        dataList = SQL.getDatauser();
        table_sun.setItems(dataList);
        FilteredList<users> filteredData = new FilteredList<>(dataList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getId().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches username
                } else if (person.getUsertype().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches password
                }else if (person.getDate().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches password
                }else if (person.getTime().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches password
                }
                else if (String.valueOf(person.getStatus()).indexOf(lowerCaseFilter)!=-1)
                    return true;// Filter matches email

                else
                    return false; // Does not match.
            });
        });
        SortedList<users> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table_sun.comparatorProperty());
        table_sun.setItems(sortedData);
    }




    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UpdateTable();
        search_user();
        // Code Source in description
    }
}
