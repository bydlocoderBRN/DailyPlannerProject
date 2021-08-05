package PlanerApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TestDailyScene.fxml"));
        Scene sceneTest = new Scene(root);
        stage.setScene(sceneTest);
        stage.setTitle("DailyPlanner");
        stage.setWidth(400);
        stage.setWidth(400);
        stage.show();
    }
    public static void main(String[] args){
        Application.launch(args);
    }
}
