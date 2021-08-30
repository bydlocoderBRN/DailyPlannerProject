package PlanerApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.naming.LinkRef;
import javax.naming.RefAddr;
import javax.naming.Reference;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TestDailyScene.fxml"));
        Scene sceneTest = new Scene(root);
        stage.setScene(sceneTest);
        stage.setTitle("DailyPlanner");
        stage.setWidth(960);
        stage.setHeight(540);
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
    public static void main(String[] args) throws Exception{
        Application.launch(args);
//        Callable<Integer> callable = new MyCallable();
//        Callable<Integer> c2 = new Callable2();
//        ExecutorService pol = Executors.newSingleThreadExecutor();
//        Future res;
//
//        res = pol.submit(callable);
//        res = pol.submit(c2);
//        System.out.println("res succesed");
//        System.out.println(res.get());
//        pol.shutdown();
    }
//    static class Callable2 implements Callable<Integer>{
//        @Override
//        public Integer call() throws Exception {
//            return 6;
//        }
//    }
//static class MyCallable implements Callable<Integer>{
//        int i=0;
//    @Override
//    public Integer call() throws Exception {
//        while(i<10){
//            i+=1;
//
//        }
//        Thread.sleep(5000);
//
//        return i;
//    }
//}

}

