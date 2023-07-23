/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.DoubleValidator;
import com.jfoenix.validation.IntegerValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import dao.Category;
import dao.CategoryDAO;
import dao.Product;
import dao.ProductDAO;
import dao.Status;
import dao.StatusDAO;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Admin
 */
public class ProductEditController {

    private ProductDAO productDao = new ProductDAO();

    private CategoryDAO categoryDao = new CategoryDAO();
    
    private StatusDAO statusDao = new StatusDAO();

    private Product editProduct = null;

    @FXML
    private Label lbEditor;

    @FXML
    private Label noti;

    @FXML
    private JFXTextField txtProductName;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXComboBox<Category> cbCategory;
    
    @FXML
    private JFXComboBox<Status> cbStatus;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private Label txtImage;

    @FXML
    private ImageView ivProductImage;

    @FXML
    void upload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image to upload");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Image image = new Image("img/" + file.getName());
            ivProductImage.setImage(image);
            txtImage.setText("img/" + file.getName());
        }
    }

    private Product extract_product_from_fields() {
        Product p = new Product();
        p.setProductName(txtProductName.getText());
        p.setPrice(Double.parseDouble(txtPrice.getText()));
        p.setStock(Integer.parseInt(txtStock.getText()));
        p.setDescription(txtDescription.getText());
        if (txtImage.getText().equals("")) {
            p.setImage("img/thumbnail.png");
        } else {
            p.setImage(txtImage.getText());
        }
        p.setStatusCode(cbStatus.getValue().getStatusCode());
        p.setCategoryId(cbCategory.getValue().getCategoryId());
        return p;
    }

    @FXML
    void clear(ActionEvent event) {
        txtProductName.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        txtDescription.setText("");
    }

    @FXML
    void save(ActionEvent event) throws IOException {
        save();
    }

    @FXML
    void enter(KeyEvent event) throws NoSuchAlgorithmException, IOException {
        if (event.getCode() == KeyCode.ENTER) {
            save();
        }
    }

    private void addListener() {
        txtProductName.textProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != oldVal) {
                txtProductName.validate();
            }
        });
        txtPrice.textProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != oldVal) {
                txtPrice.validate();
            }
        });
        txtStock.textProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != oldVal) {
                txtStock.validate();
            }
        });
        txtDescription.textProperty().addListener((o, oldVal, newVal) -> {
            if (newVal != oldVal) {
                txtDescription.validate();
            }
        });
    }

    private boolean validate() {
        RequiredFieldValidator required = new RequiredFieldValidator();
        DoubleValidator doubleValidator = new DoubleValidator();
        IntegerValidator intValidator = new IntegerValidator();
        required.setMessage("Input Required");
        doubleValidator.setMessage("Price must be double");
        intValidator.setMessage("Stock must be integer");
        txtProductName.getValidators().add(required);
        txtPrice.getValidators().addAll(required, doubleValidator);
        txtStock.getValidators().addAll(required, intValidator);
        txtDescription.getValidators().add(required);
        cbCategory.getValidators().add(required);
        cbStatus.getValidators().add(required);
        txtProductName.validate();
        txtPrice.validate();
        txtStock.validate();
        txtDescription.validate();
        cbCategory.validate();
        cbStatus.validate();
        return txtProductName.validate() && txtPrice.validate() && txtStock.validate() && txtDescription.validate() && cbCategory.validate() && cbStatus.validate();
    }

    private void save() {
        if (validate()) {
            Product extracted = extract_product_from_fields();
            if (editProduct == null && productDao.isUnique(extracted.getProductName())) {
                extracted = productDao.insert(extracted);
                if (extracted != null) {
                    Popup.getInstance().getStage().close();
                } else {
                    noti.setText("Some errors occurred.");
                    noti.setTextFill(Paint.valueOf("red"));
                }
            } else if (editProduct != null && (productDao.isUnique(extracted.getProductName()) || extracted.getProductName().equals(editProduct.getProductName()))) {
                extracted.setProductId(editProduct.getProductId());
                boolean result = productDao.update(extracted);
                if (result) {
                    Popup.getInstance().getStage().close();
                } else {
                    noti.setText("Some errors occurred.");
                    noti.setTextFill(Paint.valueOf("red"));
                }
            } else {
                noti.setText("This product already existed.");
                noti.setTextFill(Paint.valueOf("red"));
            }
        } else {
            addListener();
        }
    }

    public void initialize(Product editProduct) throws SQLException {
        this.editProduct = editProduct;

        ObservableList<Category> categories = categoryDao.selectAll();
        cbCategory.getItems().addAll(categories);
        for (Category c : categories) {
            if (this.editProduct != null && this.editProduct.getCategoryId().equals(c.getCategoryId())) {
                cbCategory.setValue(c);
                break;
            }
        }
        ObservableList<Status> status = statusDao.selectAll();
        cbStatus.getItems().addAll(status);
        for (Status s : status) {
            if (this.editProduct != null && this.editProduct.getStatusCode().equals(s.getStatusCode())) {
                cbStatus.setValue(s);
                break;
            }
        }

        if (this.editProduct == null) {
            lbEditor.setText("New Product Details");
        } else {
            lbEditor.setText("Edit Product Details");
            txtProductName.setText(editProduct.getProductName());
            txtPrice.setText(editProduct.getPrice().toString());
            txtStock.setText(editProduct.getStock().toString());
            txtDescription.setText(editProduct.getDescription());
            if (!editProduct.getImage().equals("")) {
                Image image = new Image(editProduct.getImage());
                ivProductImage.setImage(image);
                txtImage.setText(editProduct.getImage());
            }
        }
    }
}
