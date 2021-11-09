package PlanerApp;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.io.File;

import java.lang.management.PlatformManagedObject;
import java.time.LocalDateTime;
import java.util.Optional;



public class Main extends Application {
/*Реализация всплывающего окна-будильника. Из фонового асинхронного потока для остлеживания оповещений меняется статичная перменная-флаг
* isAlertNow. Animationtimer все время отслеживает значение этой переменной, и как только оно становится true, вызывается метод,
* который запускает звук и показывает диалоговое окно. Поскольку в animationtimer нельзя вызвать showAndWait
* (его можно вызывать только в обработчике событий), то animationtimer запускает метод showandwait через runLater
* (runlater запускает fx поток через некоторое неопределеное время), что позволяет сначала отключить таймер,
*  а потом вызвать диалоговое окно */
//    private static Alert alertAlarm = new Alert(Alert.AlertType.CONFIRMATION);
    private static File alarmSound = new File("D://Coding//Java//DailyPlanner//src//PlanerApp//Alarm.wav");
    private static Sound alarm = new Sound(alarmSound);
    private static boolean isAlertNow=false;
    private static boolean isUpdateListsNow=false;
    public static String alertHeader;
    public static int globalKey;
    public static void alert(){
        isAlertNow = true;
    }
    public static void updateLists(){
        isUpdateListsNow=true;
    }
    public static void resetUpdateLists(){
        isUpdateListsNow=false;
    }
    static AnimationTimer timerAlarm = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if(isUpdateListsNow){
                Platform.runLater(()->{
                    ControllerClass.updateFilteredKeysList();
                    ControllerClass.updateFilteredNoteList();
                });
            }
            if(isAlertNow){

                Platform.runLater(() -> {
                        showAlarmDialog(alertHeader, new Alert(Alert.AlertType.CONFIRMATION));
                    });
                timerAlarm.stop();
            }
        }
    };

    public static void showAlarmDialog(String s, Alert alertAlarm){

        alertAlarm.setHeaderText(s);
        alertAlarm.setContentText("Alarm now");
        alertAlarm.setTitle("Alarm!");
        alarm.stop();
        alarm.play();
        isAlertNow=false;
        timerAlarm.start();
        Optional<ButtonType> result = alertAlarm.showAndWait();


        if (result.get() == ButtonType.OK){
            alarm.stop();

            alertAlarm.showAndWait();
        }else{
            alarm.stop();

            alertAlarm.close();

        }
    }
    private static Pane root;
//    public static Pane getRoot(){
//        return root;
//    };
    @Override
    public void start(Stage stage) throws Exception {
       TimeLineController timeLine1 = new TimeLineController();
       timeLine1.setPrefHeight(800);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainUI.fxml"));
        root = (Pane) loader.load();
        root.getChildren().add(timeLine1);
        Scene sceneTest = new Scene(root);
        stage.setScene(sceneTest);
        stage.setTitle("DailyPlanner");
        stage.setResizable(false);
        stage.show();
        timerAlarm.start();
        Plan.load();
        Plan.launchPoolNotes();
        Plan.deleteOldData();
        Plan.trayNote();

        ControllerClass.updateFilteredKeysList();
        ControllerClass.updateFilteredNoteList();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Plan.save();
                Plan.closePool();
                Platform.exit();
                System.exit(0);
            }
        });
//        int key = Plan.newPlan("Bday", "Grannys birthday", LocalDateTime.of(2021, 1, 1, 0, 0, 0), LocalDateTime.of(2031, 1, 1, 0, 0, 0));
//        System.out.println(Plan.plans.keySet());
    }

    public static void main(String[] args) throws Exception{
        Application.launch(args);


}

}

