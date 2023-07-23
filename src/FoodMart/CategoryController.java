/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.validation.RequiredFieldValidator;
import dao.AdminSession;
import dao.Category;
import dao.CategoryDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;

/**
 *
 * @author Admin
 */
public class CategoryController {

    private CategoryDAO categoryDao = new CategoryDAO();

    private Category editCategory = null;

    private ChangeListener<String> inputListener;

    @FXML
    private JFXTreeView<Category> treeView;

    @FXML
    private Label lbEditor;

    @FXML
    private Label noti;

    @FXML
    private JFXTextField txtCategoryName;

    @FXML
    private JFXButton admin;

    @FXML
    void refresh(ActionEvent event) {
        buildTreeView();
    }

    @FXML
    private JFXComboBox<Category> cbParentCategory;

    @FXML
    void gotoOrder(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoOrderIndex();
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
    void gotoProduct(ActionEvent event) throws IOException {
        Navigator.getInstance().gotoProductIndex();
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
    void clear(ActionEvent event) {
        clearFields();
    }

    @FXML
    void createNew(ActionEvent event) {
        lbEditor.setText("New Category Details");
        noti.setText("");
        editCategory = null;
        clearFields();
        buildComboboxItems();
    }

    @FXML
    void edit(ActionEvent event) {
        if (treeView.getSelectionModel().getSelectedItem() == null) {
            selectWarning();
        } else {
            Category selectedCategory = treeView.getSelectionModel().getSelectedItem().getValue();
            lbEditor.setText("Edit Category Details");
            noti.setText("");
            editCategory = selectedCategory;
            buildComboboxItems();
            txtCategoryName.setText(selectedCategory.getCategoryName());
        }
    }

    @FXML
    void save(ActionEvent event) {
        txtCategoryName.textProperty().removeListener(inputListener);
        if (validate()) {
            Category extracted = extract_category_from_fields();
            if (editCategory == null && categoryDao.isUnique(extracted.getCategoryName())) {
                extracted = categoryDao.insert(extracted);
                if (extracted != null) {
                    noti.setText("New category added successfully.");
                    noti.setTextFill(Paint.valueOf("green"));
                    buildTreeView();
                    cbParentCategory.getItems().clear();
                } else {
                    noti.setText("Some errors occurred.");
                    noti.setTextFill(Paint.valueOf("red"));
                }
                clearFields();
            } else if (editCategory != null && (categoryDao.isUnique(extracted.getCategoryName()) || extracted.getCategoryName().equals(editCategory.getCategoryName()))) {
                extracted.setCategoryId(editCategory.getCategoryId());
                boolean result = categoryDao.update(extracted);
                if (result) {
                    noti.setText("Category edited successfully.");
                    noti.setTextFill(Paint.valueOf("green"));
                    buildTreeView();
                    cbParentCategory.getItems().clear();
                } else {
                    noti.setText("Some errors occurred.");
                    noti.setTextFill(Paint.valueOf("red"));
                }
                editCategory = null;
                clearFields();
                lbEditor.setText("New Category Details");
            } else {
                noti.setText("This category already existed.");
                noti.setTextFill(Paint.valueOf("red"));
            }
        } else {
            txtCategoryName.textProperty().addListener(inputListener);
        }
    }

    @FXML
    void delete(ActionEvent event) {
        if (treeView.getSelectionModel().getSelectedItem() == null) {
            selectWarning();
        } else {
            TreeItem selectedItem = treeView.getSelectionModel().getSelectedItem();
            Category selectedCategory = (Category) selectedItem.getValue();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("DELETE");
            alert.setHeaderText("Are you sure you want to delete this category?");
            Optional<ButtonType> confirmationResponse = alert.showAndWait();
            if (confirmationResponse.get() == ButtonType.OK) {
                boolean result = categoryDao.delete(selectedCategory);
                if (result) {
                    selectedItem.getParent().getChildren().remove(selectedItem);
                    cbParentCategory.getItems().clear();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Some error happened.");
                    errorAlert.showAndWait();
                }
            }
            lbEditor.setText("New Category Details");
            editCategory = null;
        }
    }

    private void buildComboboxItems() {
        cbParentCategory.getItems().clear();
        Category none = new Category();
        none.setCategoryName("None");
        cbParentCategory.getItems().add(none);
        cbParentCategory.getItems().addAll(categoryDao.selectAll());
        if (editCategory != null) {
            for (int i = 0; i < cbParentCategory.getItems().size(); i++) {
                if (editCategory.getCategoryId().equals(cbParentCategory.getItems().get(i).getCategoryId())) {
                    cbParentCategory.getItems().remove(cbParentCategory.getItems().get(i));
                } else if (editCategory.getParentId().equals(cbParentCategory.getItems().get(i).getCategoryId())) {
                    cbParentCategory.setValue(cbParentCategory.getItems().get(i));
                }
            }
        }
    }

    private boolean validate() {
        RequiredFieldValidator required = new RequiredFieldValidator();
        required.setMessage("Input Required");
        txtCategoryName.getValidators().add(required);
        return txtCategoryName.validate();
    }

    private Category extract_category_from_fields() {
        Category c = new Category();
        c.setCategoryName(txtCategoryName.getText());
        if (cbParentCategory.getValue() != null && !cbParentCategory.getValue().getCategoryName().equals("none")) {
            c.setParentId(cbParentCategory.getValue().getCategoryId());
        }
        return c;
    }

    private void selectWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setHeaderText("Please select a category to perform this operation!");
        alert.showAndWait();
    }

    private void clearFields() {
        txtCategoryName.setText("");
        for (int i = 0; i < cbParentCategory.getItems().size(); i++) {
            if (cbParentCategory.getItems().get(i).getCategoryName().equals("None")) {
                cbParentCategory.setValue(cbParentCategory.getItems().get(i));
                break;
            }
        }
    }

    private ObservableList<Category> dataList = FXCollections.observableArrayList();

    private TreeItem<Category> getCategoryTree(Category category) throws SQLException {
        TreeItem<Category> treeCategory = new TreeItem(category);
        ObservableList<Category> subCategories = categoryDao.selectSub(category.getCategoryId());
        for (Category c : subCategories) {
            TreeItem<Category> tc = new TreeItem(c);
            if (categoryDao.selectSub(c.getCategoryId()).size() > 0) {
                tc = getCategoryTree(c);
            }
            treeCategory.getChildren().add(tc);
        }
        treeCategory.setExpanded(true);
        return treeCategory;
    }

    private void buildTreeView() {
        TreeItem<Category> root = new TreeItem();
        ObservableList<Category> categories = categoryDao.selectMain();
        categories.forEach((c) -> {
            try {
                root.getChildren().add(getCategoryTree(c));
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        });
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        root.setExpanded(true);
    }

    public void initialize() throws SQLException {
        admin.setText(AdminSession.getInstance().getEmail());
        buildTreeView();
        inputListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> o, String oldVal, String newVal) {
                if (newVal == null ? oldVal != null : !newVal.equals(oldVal)) {
                    txtCategoryName.validate();
                }
            }
        };
    }

}
