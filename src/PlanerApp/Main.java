package PlanerApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
       /* Plan testNote = new Plan();
        testNote.addNotification(LocalDateTime.of(2000,1,1,0,0,0));
        testNote.addNotification(LocalDateTime.of(2001,1,1,0,0,0));
        testNote.addNotification(LocalDateTime.of(1999,1,1,0,0,0));
        testNote.addNotification(LocalDateTime.of(1975,1,1,0,0,0));
        testNote.addNotification(LocalDateTime.of(2020,1,1,0,0,0));
        testNote.addNotification(LocalDateTime.of(2020,1,1,0,0,20));
        testNote.addNotification(LocalDateTime.of(2020,1,1,0,0,19));
        System.out.println(testNote.getAllNotifications());
        System.out.println(testNote.getFirst());
        System.out.println(testNote.getLast());
//        System.out.println(testNote.getFirst().equals(LocalDateTime.of(1975,1,1,8,0,0)));*/
//        LocalDateTime time;
//        time = LocalDateTime.of(2000,2,5,3,54,7,32);
//        System.out.println(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-nn")));
//        time = time.withNano(11);
//        System.out.println(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-nn")));
//        System.out.println(time.getNano());
    }
    public static void main(String[] args){
        Application.launch(args);
    }
}
