package PlanerApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
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
        Plan mainPlan = new Plan();
        int key1 = mainPlan.newPlan("Doctor");
        int key2 = mainPlan.newPlan("Granny");
        int key3 = mainPlan.newPlan("B.Day");

        mainPlan.plans.get(key1).addAlarm(dt);
        mainPlan.plans.get(key2).addNotification(dt.withSecond(dt.getSecond()+3));
        mainPlan.plans.get(key3).addNotification(dt.withSecond(dt.getSecond()+6));
        mainPlan.plans.get(key2).addAlarm(dt.withSecond(dt.getSecond()+9));

        System.out.println(dt);
        System.out.println(LocalDateTime.now());
        System.out.println(mainPlan.getAllNotifications());

    }
}
