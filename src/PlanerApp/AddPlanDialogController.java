package PlanerApp;

import javafx.event.ActionEvent;
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
import java.time.LocalDate;
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
    CheckBox checkDateTomorrow;
    @FXML
    CheckBox checkTimeNow;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private Stage s = new Stage();
    AddPlanDialogController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPlanDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch (IOException ex){System.out.println(ex);}
    }
/*Текущая реализация добавления плана через интерфейс: после выбора в диалоговом окне всех параметров плана, по нажатию на кнопку происходит следующее:
-параметры плана записываются в соответствующие переменные
-вызывается метод в контроллере основной сцены, кторый создает новый экземпляр PlanPanel и добавляет план в listView
-Новый экземпяр PlanPanel создает новый план(что неправильно) и помещается на сцену
Надо реализовать систему фильтрации планов в зависимости от дня!!!!!
Итак будущая реализация:
-по нажатию кнопки добавить план в диалоговом окне палн просто будет добавлен в plans (общий список всех планов)
-в зависимости от выбранной даты в keysList(коллекция ключей для listview) будут помещаться ключи планов на кторые начинаются в эту дату
-оттуда планы с этими ключами будут помещены в listView
-оттуда же должны быть взяты планы для отрисовки planPanel ААААААААААААААААААААААААААААААААААААААААААА
*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        s.setScene(new Scene(this));
        dateStart.setValue(LocalDate.now());
        dateFinish.setValue(LocalDate.now());
        checkTimeNow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                checkedTimeNow();
            }
        });

        checkTimeAllDay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                checkedTimeAllDay();
            }
        });

        checkDateTomorrow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                checkedDateTomorrow();
            }
        });


        addPlan.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!checkTimeAllDay.isSelected() && !checkTimeNow.isSelected() && !checkDateTomorrow.isSelected()){
                    startTime= LocalDateTime.of(dateStart.getValue(), LocalTime.of(spinStartHours.getValue(),spinStartMinutes.getValue(), spinStartSeconds.getValue()));
                    finishTime = LocalDateTime.of(dateFinish.getValue(), LocalTime.of(spinFinishHours.getValue(),spinFinishMinutes.getValue(), spinFinishSeconds.getValue()));
                }
                if (checkTimeAllDay.isSelected()){
                    startTime = LocalDateTime.now();
                    finishTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
                }else {
                    if (checkTimeNow.isSelected() && !checkDateTomorrow.isSelected()) {
                        startTime = LocalDateTime.now();
                        finishTime = LocalDateTime.of(dateFinish.getValue(), LocalTime.of(spinFinishHours.getValue(), spinFinishMinutes.getValue(), spinFinishSeconds.getValue()));
                    }
                    if (checkDateTomorrow.isSelected() && !checkTimeNow.isSelected()) {
                        startTime= LocalDateTime.of(dateStart.getValue(), LocalTime.of(spinStartHours.getValue(),spinStartMinutes.getValue(), spinStartSeconds.getValue()));
                        finishTime = startTime.plusDays(1);
                    }
                    if(checkTimeNow.isSelected() && checkDateTomorrow.isSelected()){
                        startTime = LocalDateTime.now();
                        finishTime = startTime.plusDays(1);
                    }
                }
                String head = txtHead.getText();
                String body = txtContent.getText();
                Plan.newPlan(head,body,startTime,finishTime);
                ControllerClass.updateFilteredKeysList();
                s.close();
                System.out.println(Plan.plans);

            }
        });
            }


    public void open(){
        s.show();
    }

    private void checkedTimeNow(){
        if(checkTimeNow.isSelected()){
            dateStart.setDisable(true);
            spinStartHours.setDisable(true);
            spinStartMinutes.setDisable(true);
            spinStartSeconds.setDisable(true);
        }
        if(!checkTimeNow.isSelected()){
            dateStart.setDisable(false);
            spinStartHours.setDisable(false);
            spinStartMinutes.setDisable(false);
            spinStartSeconds.setDisable(false);
        }

    }
    private void checkedDateTomorrow(){
        if (checkDateTomorrow.isSelected()){
            dateFinish.setDisable(true);
            spinFinishHours.setDisable(true);
            spinFinishMinutes.setDisable(true);
            spinFinishSeconds.setDisable(true);
        }
        if (!checkDateTomorrow.isSelected()){
            dateFinish.setDisable(false);
            spinFinishHours.setDisable(false);
            spinFinishMinutes.setDisable(false);
            spinFinishSeconds.setDisable(false);
        }
    }
    private void checkedTimeAllDay(){
        if (checkTimeAllDay.isSelected()){
            checkTimeNow.setSelected(true);
            checkedTimeNow();
            dateFinish.setDisable(true);
            spinFinishHours.setDisable(true);
            spinFinishMinutes.setDisable(true);
            spinFinishSeconds.setDisable(true);

        }
        if (!checkTimeAllDay.isSelected()){
            checkTimeNow.setSelected(false);
            checkedTimeNow();
            dateFinish.setDisable(false);
            spinFinishHours.setDisable(false);
            spinFinishMinutes.setDisable(false);
            spinFinishSeconds.setDisable(false);

        }
    }
}
