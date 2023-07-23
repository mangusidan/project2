/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

//import dao.Admin;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dao.Product;
import java.sql.SQLException;


/**
 *
 * @author Admin
 */
public class Popup {
    public static final String PRODUCT_EDIT_FXML = "ProductEdit.fxml";
    public static final String PRODUCT_DETAIL_FXML = "Detail.fxml";
    
    private FXMLLoader loader;
    private Stage stage = null;
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    public Stage getStage(){
        return this.stage;
    }
    
    private static Popup popup = null;
    
    private Popup(){
    }
    
    public static Popup getInstance(){
        if(popup==null){
            popup = new Popup();
        }
        return popup;
    }
    
    private void goTo(String fxml) throws IOException{
        this.loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        this.stage.setScene(scene);        
    }
    
    public void gotoProductEdit(Product editProduct) throws IOException, SQLException{
        this.goTo(PRODUCT_EDIT_FXML);
        ProductEditController ctrl = loader.getController();
        ctrl.initialize(editProduct);
    }
    
    public void gotoDetail(Product product) throws IOException, SQLException{
        this.goTo(PRODUCT_DETAIL_FXML);
        DetailController ctrl = loader.getController();
        ctrl.initialize(product);
    }
}
