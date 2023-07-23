/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FoodMart;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Admin
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        Navigator.getInstance().setStage(primaryStage);
        Navigator.getInstance().getStage().getIcons().add(new Image("img/icon.jpg"));
        Navigator.getInstance().getStage().setTitle("L'splace");
        Navigator.getInstance().gotoLogin();
        Navigator.getInstance().getStage().show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
