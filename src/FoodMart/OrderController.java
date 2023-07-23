/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import java.util.Date;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import dao.AdminSession;
import dao.Order;
import dao.OrderDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import javafx.beans.property.SimpleStringProperty;
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

/**
 *
 * @author Admin
 */
public class OrderController {

    private ObservableList<Order> dataList = FXCollections.observableArrayList();
    private final OrderDAO orderDao = new OrderDAO();

    @FXML
    private JFXButton admin;

    @FXML
    private TableView<Order> tvOrders;

    @FXML
    private TableColumn<Order, Integer> tcOrderId;

    @FXML
    private TableColumn<Order, String> tcCustomer;

    @FXML
    private TableColumn<Order, Date> tcDate;

    @FXML
    private TableColumn<Order, String> tcStatus;

    @FXML
    private TableColumn<Order, String> tcTotal;

    @FXML
    private JFXComboBox<String> cbStatus;

    @FXML
    private JFXComboBox<String> cbDate;

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
    void gotoProduct(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoProductIndex();
    }

    @FXML
    void gotoOrder(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoOrderIndex();
    }

    @FXML
    void gotoStatus(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoStatusIndex();
    }

    @FXML
    private TextField filterField;

    @FXML
    void logout(ActionEvent event) throws IOException {
        AdminSession.getInstance().clearAdminSession();
        Navigator.getInstance().gotoLogin();
    }

    @FXML
    void refresh(ActionEvent event) {
        dataList = orderDao.selectAll();
        tvOrders.setItems(dataList);
    }

    @FXML
    void view(ActionEvent event) throws IOException, SQLException {
        Order selectedOrder = tvOrders.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            selectWarning();
        } else {
            Navigator.getInstance().gotoOrderDetail(selectedOrder);
        }
    }

    private void selectWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setHeaderText("Please select an order to perform this operation!");
        alert.showAndWait();
    }

    @FXML
    void filterByStatus(ActionEvent event) {
        filter();
    }

    @FXML
    void filterByDate(ActionEvent event) throws ParseException {
        filter();
    }

    private void filter() {
        Date date;
        switch (cbDate.getValue()) {
            default:
            case "Today":
                date = new Date();
                break;
            case "Yesterday":
                date = new Date(new Date().getTime() - 24 * 3600 * 1000);
                break;
            case "Last week":
                date = new Date(new Date().getTime() - 7 * 24 * 3600 * 1000);
                break;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        FilteredList<Order> filteredData = new FilteredList<>(dataList, b -> true);
        filteredData.setPredicate(order -> {
            Calendar orderDate = Calendar.getInstance();
            orderDate.setTime(order.getDate());
            orderDate.set(Calendar.MILLISECOND, 0);
            orderDate.set(Calendar.SECOND, 0);
            orderDate.set(Calendar.MINUTE, 0);
            orderDate.set(Calendar.HOUR_OF_DAY, 0);
            boolean flag = false;
            if (order.getStatus().equals(cbStatus.getValue()) || cbStatus.getValue().equals("All status")) {
                if (cbDate.getValue().equals("All dates")) {
                    flag = true;
                } else if (calendar.compareTo(orderDate) == 0) {
                    flag = true;
                } else if (orderDate.after(calendar) && cbDate.getValue().equals("Last week")) {
                    flag = true;
                }
            }
            return flag;
        });

        SortedList<Order> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tvOrders.comparatorProperty());
        tvOrders.setItems(sortedData);
    }

    @FXML
    void search() {
        FilteredList<Order> filteredData = new FilteredList<>(dataList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(order.getCustomerName()).toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Order> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tvOrders.comparatorProperty());
        tvOrders.setItems(sortedData);
    }

    public void initialize() throws SQLException {
        admin.setText(AdminSession.getInstance().getEmail());
        DecimalFormat df = new DecimalFormat("#,###");
        dataList = orderDao.selectAll();
        tvOrders.setItems(dataList);
        tcOrderId.setCellValueFactory((order) -> {
            return order.getValue().getOrderIdProperty();
        });
        tcCustomer.setCellValueFactory((order) -> {
            return order.getValue().getCustomerNameProperty();
        });
        tcDate.setCellValueFactory((order) -> {
            return order.getValue().getDateProperty();
        });
        tcTotal.setCellValueFactory((order) -> {
            return new SimpleStringProperty(df.format(order.getValue().getTotal()) + " VND");
        });
        tcStatus.setCellValueFactory((order) -> {
            return order.getValue().getStatusProperty();
        });

        cbStatus.getItems().addAll("All status", "Pending", "Confirmed", "Cancelled");
        cbStatus.setValue("All status");
        cbDate.getItems().addAll("All dates", "Today", "Yesterday", "Last week");
        cbDate.setValue("All dates");
    }
}
