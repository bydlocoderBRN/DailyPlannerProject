package PlanerApp;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Optional;

/*Привязка планов к графическим элементам. При создании плана через newPlan(), возвращается ключ, через который осуществляется доступ
 к плану в HashMap. Надо привязать этот ключ к графическому элементу, и когда фокус или действие будет на этом элементе, в глобальной переменной
  key значение ключа меняется на то, которое заложено в элементе. Через key будет осуществлен доступ ко всем планам в HashMap*/
public class ControllerClass {
    @FXML
    private Button btnTest;
    @FXML
    private TextField txtStartTime;
    @FXML
    private TextField txtFinishTime;
    @FXML
    private TextField txtNotificationTime;
    @FXML
    public static Label lblNote;
    @FXML
    private TextField txtYear;
    @FXML
    private TextField txtMonth;
    @FXML
    private TextField txtDay;
    @FXML
    private TextField txtHours;
    @FXML
    private TextField txtMinutes;
    @FXML
    private TextField txtSeconds;
    @FXML
    private void btnClick(){
        int year = Integer.parseInt(txtYear.getText());
        int month = Integer.parseInt(txtMonth.getText());
        int day = Integer.parseInt(txtDay.getText());
        int hours = Integer.parseInt(txtHours.getText());
        int minutes = Integer.parseInt(txtMinutes.getText());
        int seconds = Integer.parseInt(txtSeconds.getText());
        LocalDateTime dt = LocalDateTime.of(year,month,day,hours,minutes,seconds);


        int key1 = Plan.newPlan("Doctor");
        int key2 = Plan.newPlan("Granny");
        int key3 = Plan.newPlan("B.Day");
        Plan.toPlan(key1).addAlarm(dt);
        Plan.toPlan(key2).addNotification(dt.withSecond(dt.getSecond()+3));
        Plan.toPlan(key3).addNotification(dt.withSecond(dt.getSecond()+6));
        Plan.toPlan(key2).addAlarm(dt.withSecond(dt.getSecond()+9));

        System.out.println(dt);
        System.out.println(LocalDateTime.now());
        System.out.println(Plan.getAllNotifications());

    }





}
