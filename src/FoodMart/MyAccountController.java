/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Admin
 */
public class MyAccountController {

    private Customer customer;
    CustomerDAO customerDao = new CustomerDAO();
    private ChangeListener<String> nameListener;
    private ChangeListener<String> addressListener;
    private ChangeListener<String> emailListener;
    private ChangeListener<String> phoneListener;
    private ChangeListener<String> pwdListener;
    private ChangeListener<String> newPwdListener;
    private ChangeListener<String> confirmPwdListener;
    private ObservableList<Order> orders = FXCollections.observableArrayList();
    private OrderDAO orderDao = new OrderDAO();
    private OrderDetailDAO orderDetailDao = new OrderDetailDAO();
    private ProductDAO productDao = new ProductDAO();

    @FXML
    private GridPane paneEdit;

    @FXML
    private JFXTextField tfName;

    @FXML
    private JFXTextField tfEmail;

    @FXML
    private JFXPasswordField tfPwd;

    @FXML
    private JFXTextField tfAddress;

    @FXML
    private JFXTextField tfPhone;

    @FXML
    private JFXPasswordField tfNewPwd;

    @FXML
    private JFXPasswordField tfConfirmPwd;

    @FXML
    private Label noti;

    @FXML
    private VBox paneView;

    @FXML
    private Label txtName;

    @FXML
    private Label txtAddress;

    @FXML
    private Label txtPhone;

    @FXML
    private Label txtEmail;

    @FXML
    private AnchorPane paneOrders;

    @FXML
    private GridPane gpOrders;

    @FXML
    private GridPane gpOrderDetail;

    @FXML
    private VBox paneOrderDetail;

    @FXML
    private Label txtDetail;

    @FXML
    private JFXButton btnCancel;

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
    void edit(ActionEvent event) {
        noti.setText("");
        tfName.setText(customer.getFullName());
        tfEmail.setText(customer.getEmail());
        tfAddress.setText(customer.getAddress());
        tfPhone.setText(customer.getPhone());
        paneEdit.toFront();
    }

    @FXML
    void view(ActionEvent event) {
        txtName.setText(customer.getFullName());
        txtEmail.setText(customer.getEmail());
        txtAddress.setText(customer.getAddress());
        txtPhone.setText(customer.getPhone());
        paneView.toFront();

    }

    @FXML
    void viewOrders(ActionEvent event) {
        loadOrders();
    }

    @FXML
    void cancelOrder(ActionEvent event) {
        Order order = (Order) paneOrderDetail.getUserData();
        order.setStatus("Cancelled");
        order.setStatusCode(3);
        boolean result = orderDao.update(order);
        if (result) {
            btnCancel.setDisable(true);
            loadOrders();
        }
    }

    @FXML
    void save(ActionEvent event) throws NoSuchAlgorithmException {
        save();
    }

    @FXML
    void enter(KeyEvent event) throws NoSuchAlgorithmException, IOException {
        if (event.getCode() == KeyCode.ENTER) {
            save();
        }
    }

    private void loadOrders() {
        paneOrders.toFront();
        gpOrders.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) != 0);
        orders = orderDao.selectByCustomer(customer.getCustomerID());
        int row = 1;
        DecimalFormat df = new DecimalFormat("#,###");
        for (Order order : orders) {
            Label orderId = new Label("#" + Integer.toString(order.getOrderId()));
            orderId.setStyle("-fx-text-fill: #428bca");
            Label date = new Label(order.getDate().toString());
            Label status = new Label(order.getStatus());
            Label total = new Label(df.format(order.getTotal()) + " VND");
            total.setStyle("-fx-text-fill: #428bca");
            JFXButton action = new JFXButton("View");
            action.getStyleClass().addAll("btn-primary", "txt-bold");
            action.setOnAction(e -> {
                paneOrderDetail.toFront();
                gpOrderDetail.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) != 0);
                ObservableList<OrderDetail> orderDetails = orderDetailDao.selectAllbyOrderId(order.getOrderId());
                txtDetail.setText("Order #" + Integer.toString(order.getOrderId()) + " was placed on " + order.getDate().toString() + " and is currently " + order.getStatus() + ".");
                int row2 = 1;
                for (OrderDetail item : orderDetails) {
                    Product product = productDao.select(item.getProductId());
                    HBox col_1 = new HBox();
                    Label name = new Label(product.getProductName() + "     x " + item.getQuantity());
                    name.getStyleClass().add("txt-grey");
                    col_1.getChildren().addAll(name);
                    GridPane.setConstraints(col_1, 0, row2);

                    HBox col_2 = new HBox();
                    Label subtotal = new Label(df.format(item.getSubTotal()) + " VND");
                    subtotal.getStyleClass().add("txt-ocean-blue");
                    col_2.getChildren().add(subtotal);
                    GridPane.setConstraints(col_2, 1, row2);

                    gpOrderDetail.getChildren().addAll(col_1, col_2);
                    row2++;
                }
                HBox lastCell = new HBox();
                Label subtotal = new Label(df.format(order.getTotal()) + " VND");
                subtotal.getStyleClass().addAll("txt-ocean-blue", "txt-bold");
                lastCell.getChildren().add(subtotal);
                GridPane.setConstraints(lastCell, 1, row2);
                gpOrderDetail.getChildren().add(lastCell);

                gpOrderDetail.getChildren().forEach(node -> {
                    node.getStyleClass().add("cart-cell");
                    HBox hbox = (HBox) node;
                    hbox.setPrefHeight(50);
                });
                if (order.getStatus().equals("Pending")) {
                    btnCancel.setDisable(false);
                    paneOrderDetail.setUserData(order);
                }
            });
            GridPane.setConstraints(orderId, 0, row);
            GridPane.setConstraints(date, 1, row);
            GridPane.setConstraints(status, 2, row);
            GridPane.setConstraints(total, 3, row);
            GridPane.setConstraints(action, 4, row);
            gpOrders.getChildren().addAll(orderId, date, status, total, action);
            row++;
        }
        gpOrders.getChildren().forEach(node -> {
            node.getStyleClass().add("txt-medium");
        });
    }

    private String sha1(String input) throws NoSuchAlgorithmException {
        String sha1 = null;
        try {
            MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
            msdDigest.update(input.getBytes("UTF-8"), 0, input.length());
            sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
//        Logger.getLogger(Encriptacion.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("sha1 error");
        }
        return sha1;
    }

    private void addListener() {
        tfName.textProperty().addListener(nameListener);
        tfAddress.textProperty().addListener(addressListener);
        tfPhone.textProperty().addListener(phoneListener);
        tfEmail.textProperty().addListener(emailListener);
        tfPwd.textProperty().addListener(pwdListener);
        tfNewPwd.textProperty().addListener(newPwdListener);
        tfConfirmPwd.textProperty().addListener(confirmPwdListener);
    }

    private void removeListener() {
        tfName.textProperty().removeListener(nameListener);
        tfAddress.textProperty().removeListener(addressListener);
        tfPhone.textProperty().removeListener(phoneListener);
        tfEmail.textProperty().removeListener(emailListener);
        tfPwd.textProperty().removeListener(pwdListener);
        tfNewPwd.textProperty().removeListener(newPwdListener);
        tfConfirmPwd.textProperty().removeListener(confirmPwdListener);
    }

    private boolean validate() {
        RequiredFieldValidator required = new RequiredFieldValidator();
        required.setMessage("Input Required");
        NumberValidator number = new NumberValidator();
        number.setMessage("Must be number");
        RegexValidator regexEmail = new RegexValidator("Please enter valid email");
        regexEmail.setRegexPattern("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        RegexValidator regexPwd = new RegexValidator("Password must have at least 8 characters and include letters and numbers");
        regexPwd.setRegexPattern("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}$");
        tfName.getValidators().add(required);
        tfAddress.getValidators().add(required);
        tfPhone.getValidators().addAll(required, number);
        tfEmail.getValidators().addAll(required, regexEmail);
        if (!tfPwd.getText().equals("") || !tfNewPwd.getText().equals("") || !tfConfirmPwd.getText().equals("")) {
            tfPwd.getValidators().add(required);
            tfNewPwd.getValidators().addAll(required, regexPwd);
            tfConfirmPwd.getValidators().add(required);
        }
        tfName.validate();
        tfAddress.validate();
        tfEmail.validate();
        tfPhone.validate();
        tfPwd.validate();
        tfNewPwd.validate();
        tfConfirmPwd.validate();
        return tfName.validate() && tfAddress.validate() && tfEmail.validate() && tfPhone.validate() && tfPwd.validate() && tfNewPwd.validate() && tfConfirmPwd.validate();
    }

    private void save() throws NoSuchAlgorithmException {
        removeListener();
        if (validate()) {
            customer.setFullName(tfName.getText());
            customer.setEmail(tfEmail.getText());
            customer.setAddress(tfAddress.getText());
            customer.setPhone(tfPhone.getText());
            boolean valid = true;
            if (!tfPwd.getText().equals("")) {
                if (customerDao.authenticateCustomer(sha1(tfPwd.getText()))) {
                    if (tfNewPwd.getText().equals(tfConfirmPwd.getText())) {
                        customer.setPassword(sha1(tfNewPwd.getText()));
                    } else {
                        valid = false;
                        noti.setText("Passwords do not match");
                        noti.setTextFill(Paint.valueOf("red"));
                    }
                } else {
                    valid = false;
                    noti.setText("You entered the wrong password.");
                    noti.setTextFill(Paint.valueOf("red"));
                }
            }
            if (!customerDao.isUnique(tfEmail.getText()) && !tfEmail.getText().equals(customer.getEmail())) {
                valid = false;
                noti.setText("This email address already existed.");
                noti.setTextFill(Paint.valueOf("red"));
            }
            if (valid) {
                boolean result = customerDao.update(customer);
                if (result) {
                    noti.setText("Account details changed succesfully");
                    noti.setTextFill(Paint.valueOf("green"));
                } else {
                    noti.setText("Some error happened. Changes are not applied.");
                    noti.setTextFill(Paint.valueOf("red"));
                }
            }
        } else {
            addListener();
        }
    }

    public void initialize() {
        String email = CustomerSession.getInstance().getEmail();
        customer = customerDao.select(email);

        txtName.setText(customer.getFullName());
        txtEmail.setText(customer.getEmail());
        txtAddress.setText(customer.getAddress());
        txtPhone.setText(customer.getPhone());

        nameListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                tfName.validate();
            }
        };
        addressListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                tfAddress.validate();
            }
        };
        emailListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                tfEmail.validate();
            }
        };
        phoneListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                tfPhone.validate();
            }
        };
        pwdListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                tfPwd.validate();
            }
        };
        newPwdListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                tfNewPwd.validate();
            }
        };
        confirmPwdListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                tfConfirmPwd.validate();
            }
        };
    }
}
