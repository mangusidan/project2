/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.IntegerValidator;
import dao.CustomerSession;
import dao.OrderDetail;
import dao.Product;
import java.text.DecimalFormat;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 *
 * @author DELL
 */
public class DetailController {

    private Product product;

    private Integer count = 1;

    @FXML
    private Label txtProductName;

    @FXML
    private ImageView imgProduct;

    @FXML
    private Label txtPrice;

    @FXML
    private Label txtStock;

    @FXML
    private Label txtDescription;

    @FXML
    private JFXButton btnAddtoCart;

    @FXML
    private Label noti;

    @FXML
    void decrease(ActionEvent event) {
        if (count != 1) {
            count--;
            quantity.setText(Integer.toString(count));
        }

    }

    @FXML
    void increase(ActionEvent event) {
        count++;
        quantity.setText(Integer.toString(count));
    }

    @FXML
    private JFXTextField quantity;

    @FXML
    void addtoCart(ActionEvent event) {
        if (count > product.getStock()) {
            noti.setText("Quantity must be less than " + product.getStock());
        } else {
            boolean existed = false;
            for (OrderDetail item : CustomerSession.getInstance().getCart()) {
                if (item.getProductName().equals(product.getProductName())) {
                    item.setQuantity(item.getQuantity() + count);
                    existed = true;
                    break;
                }
            }
            if (!existed) {
                OrderDetail newItem = new OrderDetail();
                newItem.setProductId(product.getProductId());
                newItem.setProductName(product.getProductName());
                newItem.setQuantity(count);
                newItem.setUnitPrice(product.getPrice());
                CustomerSession.getInstance().getCart().add(newItem);
            }
            noti.setText("Added to cart.");
            FadeTransition fade = new FadeTransition(Duration.seconds(1), noti);
            fade.setDelay(Duration.seconds(2));
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setOnFinished(e -> {
                noti.setText("");
                noti.setOpacity(1);
            });
            fade.play();
        }
    }

    public void initialize(Product product) {
        this.product = product;
        txtProductName.setText(product.getProductName());

        DecimalFormat df = new DecimalFormat("#,###");
        txtPrice.setText(df.format(product.getPrice()) + " VND");

        txtStock.setText(product.getStock() + " in stock");

        txtDescription.setText(product.getDescription());
        txtDescription.setWrapText(true);

        Image img = new Image(product.getImage());
        imgProduct.setImage(img);

        quantity.setText(Integer.toString(count));
        IntegerValidator iv = new IntegerValidator();
        quantity.getValidators().add(iv);
        quantity.textProperty().addListener((obs, old, newVal) -> {
            if (!quantity.validate() || newVal.equals("0")) {
                quantity.setText(old);
            } else if (!newVal.equals("") && Integer.parseInt(newVal) > product.getStock()) {
                quantity.setText(old);
            } else if (!newVal.equals("")) {
                count = Integer.parseInt(newVal);
            }
        });

        if (product.getStock() == 0) {
            btnAddtoCart.setDisable(true);
        }
    }
}
