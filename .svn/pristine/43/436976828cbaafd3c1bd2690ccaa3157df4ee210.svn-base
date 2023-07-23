/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import dao.AdminDAO;
import dao.AdminSession;
import dao.Customer;
import dao.CustomerDAO;
import dao.CustomerSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Admin
 */
public class LoginController {

    private CustomerDAO customerDao = new CustomerDAO();
    private AdminDAO adminDao = new AdminDAO();
    private ChangeListener<String> nameListener;
    private ChangeListener<String> addressListener;
    private ChangeListener<String> emailListener;
    private ChangeListener<String> phoneListener;
    private ChangeListener<String> pwdListener;
    private ChangeListener<String> confirmPwdListener;

    @FXML
    private ToggleGroup role;

    @FXML
    private JFXRadioButton btnAdmin;

    @FXML
    private Pane paneAdmin;

    @FXML
    private JFXTextField txtAdminEmail;

    @FXML
    private JFXPasswordField txtAdminPwd;

    @FXML
    private Pane paneCustomer;

    @FXML
    private JFXTextField txtCustomerEmail;

    @FXML
    private JFXPasswordField txtCustomerPwd;

    @FXML
    private Label errorAdmin;

    @FXML
    private Label error;

    @FXML
    private Pane paneRegister;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXPasswordField txtUserPwd;

    @FXML
    private JFXPasswordField txtConfirmPwd;

    @FXML
    private JFXTextField txtPhone;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private Label noti;

    @FXML
    void gotoSignup(ActionEvent event) {
        paneRegister.toFront();
    }

    @FXML
    void gotoLogin(ActionEvent event) {
        paneCustomer.toFront();
    }

    @FXML
    void toggleRole(ActionEvent event) {
        if (role.getSelectedToggle().equals(btnAdmin)) {
            paneAdmin.toFront();
        } else {
            paneCustomer.toFront();
        }
    }

    @FXML
    void adminLogin(ActionEvent event) throws NoSuchAlgorithmException, IOException {
        adminLogin();
    }

    @FXML
    void customerLogin(ActionEvent event) throws NoSuchAlgorithmException, IOException {
        customerLogin();
    }

    @FXML
    void signup(ActionEvent event) throws NoSuchAlgorithmException {
        signup();
    }

    @FXML
    void customerEnter(KeyEvent event) throws NoSuchAlgorithmException, IOException {
        if (event.getCode() == KeyCode.ENTER) {
            customerLogin();
        }
    }

    @FXML
    void adminEnter(KeyEvent event) throws NoSuchAlgorithmException, IOException {
        if (event.getCode() == KeyCode.ENTER) {
            adminLogin();
        }
    }

    @FXML
    void signupEnter(KeyEvent event) throws NoSuchAlgorithmException, IOException {
        if (event.getCode() == KeyCode.ENTER) {
            signup();
        }
    }

    public String sha1(String input) throws NoSuchAlgorithmException {
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

    private boolean validateAdmin() {
        RequiredFieldValidator required = new RequiredFieldValidator();
        required.setMessage("Input Required");
        txtAdminEmail.getValidators().add(required);
        txtAdminPwd.getValidators().add(required);
        txtAdminEmail.textProperty().addListener((o, oldVal, newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtAdminEmail.validate();
            }
        });
        txtAdminPwd.textProperty().addListener((o, oldVal, newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtAdminPwd.validate();
            }
        });
        return txtAdminEmail.validate() && txtAdminPwd.validate();
    }

    private boolean validateCustomer() {
        RequiredFieldValidator required = new RequiredFieldValidator();
        required.setMessage("Input Required");
        txtCustomerEmail.getValidators().add(required);
        txtCustomerPwd.getValidators().add(required);
        txtCustomerEmail.textProperty().addListener((o, oldVal, newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtCustomerEmail.validate();
            }
        });
        txtCustomerPwd.textProperty().addListener((o, oldVal, newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtCustomerPwd.validate();
            }
        });
        return txtCustomerEmail.validate() && txtCustomerPwd.validate();
    }

    private void addListener() {
        txtFullName.textProperty().addListener(nameListener);
        txtAddress.textProperty().addListener(addressListener);
        txtEmail.textProperty().addListener(emailListener);
        txtPhone.textProperty().addListener(phoneListener);
        txtUserPwd.textProperty().addListener(pwdListener);
        txtConfirmPwd.textProperty().addListener(confirmPwdListener);
    }

    private void removeListener() {
        txtFullName.textProperty().removeListener(nameListener);
        txtAddress.textProperty().removeListener(addressListener);
        txtEmail.textProperty().removeListener(emailListener);
        txtPhone.textProperty().removeListener(phoneListener);
        txtUserPwd.textProperty().removeListener(pwdListener);
        txtConfirmPwd.textProperty().removeListener(confirmPwdListener);
    }

    private boolean validateSignup() {
        RequiredFieldValidator required = new RequiredFieldValidator();
        RegexValidator regexEmail = new RegexValidator("Please enter valid email");
        regexEmail.setRegexPattern("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        RegexValidator regexPwd = new RegexValidator("Password must have at least 8 characters and include letters and numbers");
        regexPwd.setRegexPattern("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,20}$");
        NumberValidator number = new NumberValidator();
        required.setMessage("Input Required");
        number.setMessage("Must be number");
        txtFullName.getValidators().add(required);
        txtAddress.getValidators().add(required);
        txtUserPwd.getValidators().addAll(required, regexPwd);
        txtConfirmPwd.getValidators().add(required);
        txtPhone.getValidators().addAll(required, number);
        txtEmail.getValidators().addAll(required, regexEmail);
        txtFullName.validate();
        txtAddress.validate();
        txtEmail.validate();
        txtPhone.validate();
        txtUserPwd.validate();
        txtConfirmPwd.validate();
        return txtFullName.validate() && txtAddress.validate() && txtEmail.validate() && txtPhone.validate() && txtUserPwd.validate() && txtConfirmPwd.validate();
    }

    private void customerLogin() throws NoSuchAlgorithmException, IOException {
        if (validateCustomer()) {
            String email = txtCustomerEmail.getText();
            String password = sha1(txtCustomerPwd.getText());
            if (customerDao.authenticateCustomer(email, password)) {
                CustomerSession.getInstance().setEmail(email);
                CustomerSession.getInstance().setProducts();
                Navigator.getInstance().gotoHome();
            } else {
                error.setText("Authentication failed!");
            }
        }
    }

    private void adminLogin() throws NoSuchAlgorithmException, IOException {
        if (validateAdmin()) {
            String email = txtAdminEmail.getText();
            String password = sha1(txtAdminPwd.getText());
            if (adminDao.authenticateAdmin(email, password)) {
                AdminSession.getInstance().setEmail(email);
                Navigator.getInstance().gotoProductIndex();
            } else {
                errorAdmin.setText("Authentication failed!");
            }
        }
    }

    private void signup() throws NoSuchAlgorithmException {
        removeListener();
        if (validateSignup()) {
            if (!txtUserPwd.getText().equals(txtConfirmPwd.getText())) {
                noti.setText("Password and confirm password do not match.");
                noti.setTextFill(Paint.valueOf("red"));
            } else {
                if (customerDao.isUnique(txtEmail.getText())) {
                    Customer c = new Customer();
                    c.setFullName(txtFullName.getText());
                    c.setAddress(txtAddress.getText());
                    c.setEmail(txtEmail.getText());
                    c.setPhone(txtPhone.getText());
                    c.setPassword(sha1(txtUserPwd.getText()));
                    Customer newCustomer = customerDao.insert(c);
                    if (newCustomer != null) {
                        noti.setText("Register Successfully! You can login now.");
                        noti.setTextFill(Paint.valueOf("green"));
                        txtFullName.setText("");
                        txtEmail.setText("");
                        txtPhone.setText("");
                        txtUserPwd.setText("");
                        txtConfirmPwd.setText("");
                    } else {
                        noti.setText("Register unsuccesfully.");
                        noti.setTextFill(Paint.valueOf("red"));
                    }
                } else {
                    noti.setText("This email is already registered.");
                    noti.setTextFill(Paint.valueOf("red"));
                }
            }
        } else {
            addListener();
        }
    }

    public void initialize() {
        nameListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtFullName.validate();
            }
        };
        addressListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtAddress.validate();
            }
        };
        emailListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtEmail.validate();
            }
        };
        phoneListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtPhone.validate();
            }
        };
        pwdListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtUserPwd.validate();
            }
        };
        confirmPwdListener = (ObservableValue<? extends String> o, String oldVal, String newVal) -> {
            if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                txtConfirmPwd.validate();
            }
        };
    }
}
