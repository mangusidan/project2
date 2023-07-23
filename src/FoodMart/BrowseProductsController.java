/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXTextField;
import dao.Category;
import dao.CategoryDAO;
import dao.CustomerSession;
import dao.OrderDetail;
import dao.Product;
import dao.ProductDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.RangeSlider;

/**
 *
 * @author Admin
 */
public class BrowseProductsController {

    private ProductDAO productDao = new ProductDAO();
    private final CategoryDAO categoryDao = new CategoryDAO();
    private final ObservableList<Product> allProducts = CustomerSession.getInstance().getProducts();
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    private Category category;
    private static Stage popup = new Stage();

    static {
        Popup.getInstance().setStage(popup);
    }

    @FXML
    private JFXButton btnCart;

    @FXML
    private JFXDrawer previewCart;

    @FXML
    private JFXComboBox<Category> cbCategories;

    @FXML
    private VBox sliderPrice;

    @FXML
    private JFXComboBox<String> sortBy;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private JFXTextField fromVal;

    @FXML
    private JFXTextField toVal;

    private RangeSlider priceRange;

    @FXML
    private JFXTextField txtSearch;

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
    void gotoMyAccount(ActionEvent event) throws IOException, SQLException {
        Navigator.getInstance().gotoMyAccount();
    }

    @FXML
    void gotoCart(ActionEvent event) {
        if (previewCart.isClosed()) {
            loadCart();
            previewCart.open();
            previewCart.setVisible(true);
            previewCart.toFront();
        } else {
            previewCart.close();
            previewCart.setVisible(false);
            previewCart.toBack();
        }
    }

    @FXML
    void search(ActionEvent event) throws IOException, SQLException {
        if (!txtSearch.getText().equals("")) {
            products.clear();
            filteredProducts.clear();
            allProducts.stream().filter(p -> {
                return (p.getProductName().toLowerCase().contains(txtSearch.getText().toLowerCase()));
            }).forEach(p -> {
                products.add(p);
            });
            buildPriceRange();
            showProducts(products);
        }
    }

    @FXML
    void enter(KeyEvent event) throws IOException, SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            if (!txtSearch.getText().equals("")) {
                products.clear();
                filteredProducts.clear();
                allProducts.stream().filter(p -> {
                    return (p.getProductName().toLowerCase().contains(txtSearch.getText().toLowerCase()));
                }).forEach(p -> {
                    products.add(p);
                });
                buildPriceRange();
                showProducts(products);
            }
        }
    }

    @FXML
    void chooseCategory(ActionEvent event) throws SQLException {
        products.clear();
        filteredProducts.clear();
        this.category = cbCategories.getValue();
        allProducts.stream().filter(p -> {
            return (p.getCategoryId().equals(category.getCategoryId()));
        }).forEach(p -> {
            products.add(p);
        });

        ObservableList<Category> subcategories = getSubcategories(category);
        subcategories.forEach(c -> {
            allProducts.stream().filter(p -> {
                return (p.getCategoryId().equals(c.getCategoryId()));
            }).forEach(p -> {
                products.add(p);
            });
        });
        buildPriceRange();
        sortBy.setValue("A-Z");
        showProducts(products);
    }

    @FXML
    void changeOrder(ActionEvent event) {
        if (filteredProducts.size() != 0) {
            showProducts(filteredProducts);
        } else {
            showProducts(products);
        }
    }

    @FXML
    void applyFilter(ActionEvent event) {
        filteredProducts.clear();
        products.stream().filter(p -> {
            return (p.getPrice() <= priceRange.getHighValue() && p.getPrice() >= priceRange.getLowValue());
        }).forEach(p -> {
            filteredProducts.add(p);
        });
        showProducts(filteredProducts);
    }

    private ObservableList<Product> sort(ObservableList<Product> products) {
        ObservableList<Product> sortedProducts = FXCollections.observableArrayList();
        String sortVal = sortBy.getValue();
        switch (sortVal) {
            case "A-Z":
                products.stream().sorted(Comparator.comparing(Product::getProductName)).forEach(p -> {
                    sortedProducts.add(p);
                });
                break;
            case "Sort by newest":
                products.stream().sorted(Comparator.comparing(Product::getProductId, (p1, p2) -> {
                    return p2.compareTo(p1);
                })).forEach(p -> {
                    sortedProducts.add(p);
                });
                break;
            case "Sort by price: low to high":
                products.stream().sorted(Comparator.comparing(Product::getPrice)).forEach(p -> {
                    sortedProducts.add(p);
                });
                break;
            case "Sort by price: high to low":
                products.stream().sorted(Comparator.comparing(Product::getPrice, (p1, p2) -> {
                    return p2.compareTo(p1);
                })).forEach(p -> {
                    sortedProducts.add(p);
                });
                break;
            default:
                break;
        }
        return sortedProducts;
    }

    EventHandler clickProduct = (EventHandler) (Event e) -> {
        if (previewCart.isOpened()) {
            previewCart.close();
            previewCart.setVisible(false);
            previewCart.toBack();
        }
        try {
            Node clickedProduct = (Node) e.getSource();
            Integer productID = (Integer) clickedProduct.getUserData();
            Product product = productDao.select(productID);
            Popup.getInstance().gotoDetail(product);
            Popup.getInstance().getStage().show();
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void showProducts(ObservableList<Product> products) {
        ObservableList<Product> sortedProducts = sort(products);
        scrollPane.setContent(null);
        int col = 0, row = 0;
        GridPane gpProductsList = new GridPane();
        gpProductsList.getStyleClass().addAll("background-white", "gpProductsList");
        scrollPane.setContent(gpProductsList);
        scrollPane.getStyleClass().add("background-white");

        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            gpProductsList.getColumnConstraints().add(i, column);
        }

        for (Product p : sortedProducts) {
            VBox vbox = new VBox();
            VBox imageWrapper = new VBox();
            Label category = new Label(p.getCategoryName());
            category.getStyleClass().addAll("hover-hand", "categoryName");
            category.setUserData(p.getCategoryId());
            category.setOnMouseClicked(event -> {
                try {
                    Category c = categoryDao.select(p.getCategoryId());
                    Navigator.getInstance().gotoBrowse(c);
                } catch (IOException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            ImageView iv = new ImageView(p.getImage());
            iv.setFitWidth(250);
            iv.setFitHeight(250);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setCache(true);
            iv.getStyleClass().addAll("hover-shadow", "hover-hand");
            iv.setUserData(p.getProductId());
            iv.setOnMouseClicked(clickProduct);
            imageWrapper.getChildren().add(iv);
            imageWrapper.setPrefHeight(250);
            imageWrapper.setAlignment(Pos.CENTER_LEFT);
            VBox detail = new VBox();
            Label name = new Label(p.getProductName());
            name.getStyleClass().addAll("productName", "hover-hand");
            name.setMaxWidth(250);
            name.setUserData(p.getProductId());
            name.setOnMouseClicked(clickProduct);
            DecimalFormat df = new DecimalFormat("#,###");
            Label price = new Label(df.format(p.getPriceProperty().get()) + " VND");
            price.getStyleClass().add("price");
            JFXButton addToCart = new JFXButton("Add to Cart");
            if (p.getStock() == 0) {
                addToCart.setDisable(true);
            }
            HBox action = new HBox();
            FontAwesomeIconView check = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
            check.setSize("25");
            check.setFill(Paint.valueOf("white"));
            FadeTransition fade = new FadeTransition(Duration.seconds(1), check);
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setOnFinished(e -> {
                check.setFill(Paint.valueOf("white"));
            });
            for (OrderDetail item : CustomerSession.getInstance().getCart()) {
                if (item.getProductName().equals(p.getProductName())) {
                    FontAwesomeIconView c = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                    c.setSize("15");
                    c.setFill(Paint.valueOf("white"));
                    addToCart.setGraphic(c);
                }
            }
            addToCart.getStyleClass().addAll("btn-primary", "hover-shadow", "hover-hand", "txt-white", "txt-bold");
            addToCart.setOnAction((event) -> {
                if (previewCart.isOpened()) {
                    previewCart.close();
                    previewCart.setVisible(false);
                    previewCart.toBack();
                }
                boolean existed = false;
                for (OrderDetail item : CustomerSession.getInstance().getCart()) {
                    if (item.getProductName().equals(p.getProductName())) {
                        item.setQuantity(item.getQuantity() + 1);
                        existed = true;
                        break;
                    }
                }
                if (!existed) {
                    OrderDetail newItem = new OrderDetail();
                    newItem.setProductId(p.getProductId());
                    newItem.setProductName(p.getProductName());
                    newItem.setQuantity(1);
                    newItem.setUnitPrice(p.getPrice());
                    CustomerSession.getInstance().getCart().add(newItem);
                    FontAwesomeIconView c = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
                    c.setSize("15");
                    c.setFill(Paint.valueOf("white"));
                    addToCart.setGraphic(c);
                }
                check.setFill(Paint.valueOf("green"));
                fade.play();
                int sum = CustomerSession.getInstance().getCart().stream().mapToInt((item) -> {
                    return item.getQuantity();
                }).sum();
                btnCart.setText("Cart (" + sum + ")");
            });
            action.getChildren().addAll(addToCart, check);
            action.setSpacing(20);
            detail.getChildren().addAll(name, price, action);
            detail.setSpacing(5);
            vbox.getChildren().addAll(category, imageWrapper, detail);
            vbox.getStyleClass().add("product-box");
            GridPane.setConstraints(vbox, col, row);
            gpProductsList.getChildren().add(vbox);
            col++;
            if (col == 3) {
                row++;
                col = 0;
            }
        }
    }

    private void buildPriceRange() {
        if (sliderPrice.getChildren().contains(priceRange)) {
            sliderPrice.getChildren().remove(priceRange);
        }
        if (products.stream().min(Comparator.comparing(Product::getPrice)).isPresent() && products.stream().max(Comparator.comparing(Product::getPrice)).isPresent()) {
            Double minPrice = products.stream().min(Comparator.comparing(Product::getPrice)).get().getPrice();
            Double maxPrice = products.stream().max(Comparator.comparing(Product::getPrice)).get().getPrice();
            priceRange = new RangeSlider(minPrice, maxPrice, minPrice, maxPrice);
            sliderPrice.getChildren().add(priceRange);
            fromVal.setText(minPrice + " VND");
            toVal.setText(maxPrice + " VND");
            DecimalFormat df = new DecimalFormat("#,###");
            priceRange.lowValueProperty().addListener((obs, olV, newV) -> {
                fromVal.setText(df.format((Double) newV) + " VND");
            });
            priceRange.highValueProperty().addListener((obs, olV, newV) -> {
                toVal.setText(df.format((Double) newV) + " VND");
            });
        };
    }

    private ObservableList<Category> getSubcategories(Category category) throws SQLException {
        ObservableList<Category> list = FXCollections.observableArrayList();
        ObservableList<Category> subCategories = categoryDao.selectSub(category.getCategoryId());
        list.addAll(subCategories);
        for (Category c : subCategories) {
            if (categoryDao.selectSub(c.getCategoryId()).size() > 0) {
                list.addAll(getSubcategories(c));
            }
        }
        return list;
    }

    private void loadCart() {
        if (CustomerSession.getInstance().getCart().isEmpty()) {
            previewCart.setSidePane(new Label("No product in cart."));
        } else {
            VBox vbox = new VBox();
            ScrollPane scrollpane = new ScrollPane();
            GridPane gpCart = new GridPane();
            int row = 0;
            DecimalFormat df = new DecimalFormat("#,###");
            for (OrderDetail item : CustomerSession.getInstance().getCart()) {
                Product product = productDao.select(item.getProductId());
                ImageView col_1 = new ImageView(product.getImage());
                col_1.setFitWidth(100);
                col_1.setFitHeight(100);
                col_1.setPreserveRatio(true);
                col_1.setSmooth(true);
                col_1.setCache(true);
                GridPane.setConstraints(col_1, 0, row);

                VBox col_2 = new VBox();
                Label name = new Label(product.getProductName());
                name.getStyleClass().add("txt-grey");
                Label number = new Label(item.getQuantity() + " x " + df.format(item.getUnitPrice()) + " VND");
                col_2.getChildren().addAll(name, number);
                col_2.setAlignment(Pos.CENTER_LEFT);
                GridPane.setConstraints(col_2, 1, row);

                JFXButton col_3 = new JFXButton("");
                FontAwesomeIconView x = new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE);
                x.setFill(Paint.valueOf("#6ac2ee"));
                x.setSize("20");
                col_3.setGraphic(x);
                col_3.setOnAction(e -> {
                    int rowIndex = GridPane.getRowIndex(col_3);
                    gpCart.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == rowIndex);
                    CustomerSession.getInstance().getCart().remove(item);
                });
                GridPane.setConstraints(col_3, 2, row);

                gpCart.getChildren().addAll(col_1, col_2, col_3);
                row++;
            }
            gpCart.getStyleClass().add("background-white");
            scrollpane.setContent(gpCart);
            scrollpane.setMinHeight(300);
            scrollpane.setFitToWidth(true);
            scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            JFXButton viewCart = new JFXButton("View Cart");
            viewCart.getStyleClass().addAll("btn-primary", "txt-bold");
            viewCart.setOnAction(e -> {
                try {
                    Navigator.getInstance().gotoCart();
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            JFXButton checkout = new JFXButton("Checkout");
            checkout.getStyleClass().addAll("btn-primary", "txt-bold");
            checkout.setOnAction(e -> {
                try {
                    if (!CustomerSession.getInstance().getCart().isEmpty()) {
                        Navigator.getInstance().gotoCheckout();
                    }
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            HBox btn = new HBox(viewCart, checkout);
            btn.setSpacing(10);
            btn.setAlignment(Pos.CENTER_RIGHT);
            vbox.getChildren().addAll(scrollpane, btn);
            vbox.setSpacing(10);
            vbox.getChildren().forEach(node -> {
                node.getStyleClass().add("txt-medium");
            });
            vbox.getStyleClass().add("p-10");
            previewCart.setSidePane(vbox);
        }
        previewCart.getStyleClass().add("shadow");
    }

    public void initialize(Category category) throws SQLException {
        this.category = category;
        allProducts.stream().filter(p -> {
            return (p.getCategoryId().equals(category.getCategoryId()));
        }).forEach(p -> {
            products.add(p);
        });

        ObservableList<Category> subcategories = getSubcategories(category);
        subcategories.forEach(c -> {
            allProducts.stream().filter(p -> {
                return (p.getCategoryId().equals(c.getCategoryId()));
            }).forEach(p -> {
                products.add(p);
            });
        });

        initialize();
        for (Category c : cbCategories.getItems()) {
            if (c.getCategoryId().equals(category.getCategoryId())) {
                cbCategories.setValue(c);
                break;
            }
        }
    }

    public void initialize(String search) throws SQLException {
        allProducts.stream().filter(p -> {
            return (p.getProductName().toLowerCase().contains(search.toLowerCase()));
        }).forEach(p -> {
            products.add(p);
        });
        initialize();
    }

    private void initialize() {
        cbCategories.getItems().addAll(categoryDao.selectAll());
        buildPriceRange();
        sortBy.getItems().addAll("A-Z", "Sort by newest", "Sort by price: low to high", "Sort by price: high to low");
        sortBy.setValue("A-Z");
        showProducts(products);
        int sum = CustomerSession.getInstance().getCart().stream().mapToInt((item) -> {
            return item.getQuantity();
        }).sum();
        btnCart.setText("Cart (" + sum + ")");
    }
}
