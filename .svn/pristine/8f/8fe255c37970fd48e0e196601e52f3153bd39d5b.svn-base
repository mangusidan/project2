/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import com.jfoenix.controls.JFXButton;
import dao.AdminSession;
import dao.Customer;
import dao.CustomerDAO;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Admin
 */
public class CustomerController {

    private CustomerDAO customerDao = new CustomerDAO();
    private ObservableList<Customer> dataList = FXCollections.observableArrayList();
    
    @FXML
    private JFXButton admin;

    @FXML
    private TableView<Customer> tvCustomers;

//    @FXML
//    private TableColumn<Customer, Integer> tcCustomerId;

    @FXML
    private TableColumn<Customer, String> tcFullName;

    @FXML
    private TableColumn<Customer, String> tcEmail;

    @FXML
    private TableColumn<Customer, String> tcPhone;

    @FXML
    private TableColumn<Customer, String> tcAddress;

    @FXML
    private TextField filterField;
    
    @FXML
    void gotoOrder(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoOrderIndex();
    }

    @FXML
    void gotoProduct(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoProductIndex();
    }

    @FXML
    void gotoAdmin(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoAdminIndex();
    }

    @FXML
    void gotoCategory(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoCategoryIndex();
    }

    @FXML
    void gotoCustomer(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoCustomerIndex();
    }
    
    @FXML
    void gotoStatus(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoStatusIndex();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        AdminSession.getInstance().clearAdminSession();
        Navigator.getInstance().gotoLogin();
    }

    @FXML
    void refresh(ActionEvent event) {
        dataList = customerDao.selectAll();
        tvCustomers.setItems(dataList);
    }

    @FXML
    void search() {
        FilteredList<Customer> filteredData = new FilteredList<>(dataList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (customer.getFullName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return customer.getPhone().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Customer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tvCustomers.comparatorProperty());
        tvCustomers.setItems(sortedData);
    }

    public void initialize() throws SQLException {
        dataList = customerDao.selectAll();
        tvCustomers.setItems(dataList);
        tcFullName.setCellValueFactory((customer) -> {
            return customer.getValue().getFullNameProperty();
        });
        tcEmail.setCellValueFactory((customer) -> {
            return customer.getValue().getEmailProperty();
        });
        tcPhone.setCellValueFactory((customer) -> {
            return customer.getValue().getPhoneProperty();
        });
        tcAddress.setCellValueFactory((customer) -> {
            return customer.getValue().getAddressProperty();
        });
        
        admin.setText(AdminSession.getInstance().getEmail());
    }
}
