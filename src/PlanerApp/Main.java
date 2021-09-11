package PlanerApp;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


import java.io.File;

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
    public static boolean isAlertNow=false;
    public static String alertHeader;
    static AnimationTimer timerAlarm = new AnimationTimer() {
        @Override
        public void handle(long l) {

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
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TestDailyScene.fxml"));
        Scene sceneTest = new Scene(root);
        stage.setScene(sceneTest);
        stage.setTitle("DailyPlanner");
        stage.setWidth(960);
        stage.setHeight(540);
        stage.show();
        Plan.trayNote();
        timerAlarm.start();
    }
    public static void main(String[] args) throws Exception{

        Application.launch(args);


}
}

