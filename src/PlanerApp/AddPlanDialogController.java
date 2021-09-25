package PlanerApp;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddPlanDialogController extends Pane implements Initializable {
    @FXML
    Button addPlan;
    @FXML
    TextField txtHead;
    @FXML
    TextArea txtContent;
    @FXML
    Spinner<Integer> spinFinishHours;
    @FXML
    Spinner<Integer> spinFinishMinutes;
    @FXML
    Spinner<Integer> spinFinishSeconds;
    @FXML
    Spinner<Integer> spinStartHours;
    @FXML
    Spinner<Integer> spinStartMinutes;
    @FXML
    Spinner<Integer> spinStartSeconds;
    @FXML
    DatePicker dateStart;
    @FXML
    DatePicker dateFinish;
    @FXML
    CheckBox checkTimeAllDay;
    @FXML
    CheckBox checkDateToday;
    @FXML
    CheckBox checkDateTommorow;
    @FXML
    CheckBox checkDateNextdDay;
    @FXML
    CheckBox checkDateFinish;
    @FXML
    CheckBox checkTimeNow;

    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    @FXML
    private void timeAllDayChecked(){
        if(checkTimeNow.isSelected()){
        startTime = LocalDateTime.now();
        dateStart.setDisable(true);
        spinStartHours.setDisable(true);
        spinStartMinutes.setDisable(true);
        spinStartSeconds.setDisable(true);}
        else{
            dateStart.setDisable(false);
            spinStartHours.setDisable(false);
            spinStartMinutes.setDisable(false);
            spinStartSeconds.setDisable(false);}
        }
//        finishTime = LocalDateTime.of()


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPlan.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                startTime = LocalDateTime.of(dateStart.getValue(), LocalTime.of(spinStartHours.getValue(),spinStartMinutes.getValue(), spinStartSeconds.getValue()));
                finishTime = LocalDateTime.of(dateFinish.getValue(), LocalTime.of(spinFinishHours.getValue(),spinFinishMinutes.getValue(), spinFinishSeconds.getValue()));
                String head = txtHead.toString();
                String body = txtHead.toString();
                ControllerClass.hBoxAddPlan(startTime,finishTime,head,body);
                System.out.println(Plan.plans);
            }
        });
            }
    AddPlanDialogController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPlanDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch (IOException ex){System.out.println(ex);}
    }
}
