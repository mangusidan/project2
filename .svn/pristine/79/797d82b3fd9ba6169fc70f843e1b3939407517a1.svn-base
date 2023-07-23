/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import dao.CustomerSession;

/**
 *
 * @author Admin
 */
public class AboutUsController {

    @FXML
    private JFXButton btnCart;

    @FXML
    void logout(ActionEvent event) throws IOException {
        CustomerSession.getInstance().clearCustomerSession();
        Navigator.getInstance().gotoLogin();
    }

    @FXML
    void gotoHome(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoHome();
    }

    @FXML
    void gotoAboutUs(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoAboutUs();
    }

    @FXML
    void gotoCart(ActionEvent event) throws IOException, SQLException {
        Navigator.getInstance().gotoCart();
    }

    @FXML
    void gotoMyAccount(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoMyAccount();
    }

    public void initialize() throws SQLException {
        int sum = CustomerSession.getInstance().getCart().stream().mapToInt((item) -> {
            return item.getQuantity();
        }).sum();
        btnCart.setText("Cart (" + sum + ")");
    }
}
