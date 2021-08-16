package PlanerApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

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
        Plan plan = new Plan();
        plan.addNotification(dt);
        System.out.println(dt);
        System.out.println(LocalDateTime.now());

    }
}
