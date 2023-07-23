/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import dao.Customer;
import dao.CustomerDAO;
import dao.CustomerSession;
import dao.Order;
import dao.OrderDAO;
import dao.OrderDetail;
import dao.OrderDetailDAO;
import dao.Product;
import dao.ProductDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * @author Admin
 */
public class CheckoutController {

    ProductDAO productDao = new ProductDAO();

    OrderDAO orderDao = new OrderDAO();

    OrderDetailDAO orderDetailDao = new OrderDetailDAO();

    CustomerDAO customerDao = new CustomerDAO();

    private ObservableList<OrderDetail> cart;
    
    private Double totalPrice;

    private Customer customer;

    @FXML
    private Label txtName;

    @FXML
    private Label txtAddress;

    @FXML
    private Label txtPhone;

    @FXML
    private Label txtEmail;

    @FXML
    private GridPane gpCartItems;

    @FXML
    private Pane paneNoti;

    @FXML
    private Label txtOrderId;

    @FXML
    private Label txtDate;

    @FXML
    private Label txtTotal;

    @FXML
    void gotoHome(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoHome();
    }

    @FXML
    void gotoAboutUs(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoAboutUs();
    }

    @FXML
    void gotoMyAccount(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoMyAccount();
    }

    @FXML
    void gotoCart(ActionEvent event) throws IOException, SQLException {
        Navigator.getInstance().gotoCart();
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        CustomerSession.getInstance().clearCustomerSession();
        Navigator.getInstance().gotoLogin();
    }

    @FXML
    void shop(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoHome();
    }

    @FXML
    void confirm(ActionEvent event) throws IOException, SQLException {
        Order order = new Order();
        order.setCustomerId(customer.getCustomerID());
        order.setStatusCode(1);
        order = orderDao.insert(order);
        boolean result = true;
        if (order != null) {
            for (OrderDetail od : cart) {
                od.setOrderId(order.getOrderId());
                od = orderDetailDao.insert(od);
                if (od == null) {
                    result = false;
                    System.out.println("Some errors happened. Please try again.");
                    break;
                }
            }
        } else {
            result = false;
            System.out.println("Some errors happened. Please try again.");
        }

        if (result) {
            cart.clear();
            txtOrderId.setText(Integer.toString(order.getOrderId()));
            txtDate.setText(java.time.LocalDate.now().toString());
            DecimalFormat df = new DecimalFormat("#,###");
            txtTotal.setText(df.format(totalPrice) + " VND");
            paneNoti.toFront();
        }
    }

    private void loadCart() {
        gpCartItems.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) != 0);
        totalPrice = 0.0;
        int row = 1;
        DecimalFormat df = new DecimalFormat("#,###");
        for (OrderDetail item : cart) {
            Product product = productDao.select(item.getProductId());
            HBox col_1 = new HBox();
            Label name = new Label(product.getProductName() + "     x " + item.getQuantity());
            name.getStyleClass().add("txt-grey");
            col_1.getChildren().addAll(name);
            GridPane.setConstraints(col_1, 0, row);

            HBox col_2 = new HBox();
            Label total = new Label(df.format(item.getSubTotal()) + " VND");
            total.getStyleClass().add("txt-ocean-blue");
            col_2.getChildren().add(total);
            GridPane.setConstraints(col_2, 1, row);

            gpCartItems.getChildren().addAll(col_1, col_2);
            totalPrice += item.getUnitPrice() * item.getQuantity();
            row++;
        }
        HBox lastCell = new HBox();
        Label subtotal = new Label(df.format(totalPrice) + " VND");
        subtotal.getStyleClass().addAll("txt-ocean-blue", "txt-bold");
        lastCell.getChildren().add(subtotal);
        GridPane.setConstraints(lastCell, 1, row);
        gpCartItems.getChildren().add(lastCell);

        gpCartItems.getChildren().forEach(node -> {
            node.getStyleClass().add("cart-cell");
            HBox hbox = (HBox) node;
            hbox.setPrefHeight(50);
        });
    }

    public void initialize() throws SQLException {
        cart = CustomerSession.getInstance().getCart();
        loadCart();

        String email = CustomerSession.getInstance().getEmail();
        customer = customerDao.select(email);
        txtName.setText(customer.getFullName());
        txtEmail.setText(customer.getEmail());
        txtAddress.setText(customer.getAddress());
        txtPhone.setText(customer.getPhone());
    }
}
