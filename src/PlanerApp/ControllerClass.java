package PlanerApp;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import java.net.URL;
import java.time.LocalDateTime;

import java.util.ResourceBundle;

/*Привязка планов к графическим элементам. При создании плана через newPlan(), возвращается ключ, через который осуществляется доступ
 к плану в HashMap. Надо привязать этот ключ к графическому элементу, и когда фокус или действие будет на этом элементе, в глобальной переменной
  key значение ключа меняется на то, которое заложено в элементе. Через key будет осуществлен доступ ко всем планам в HashMap*/
public class ControllerClass implements Initializable {
    AddPlanDialogController planDialog;
    @FXML
    private Button btnAddPlan;
    @FXML
    private HBox h1;
    @FXML
    public static ListView listPlans;
    @FXML
    private void btnClick(){
        planDialog = new AddPlanDialogController();
//        Stage s = new Stage();
//        s.setScene(new Scene(planDialog));
//        s.show();
        planDialog.open();
    }

    public static void hBoxAddPlan(LocalDateTime start, LocalDateTime finish, String head, String body){
        PlanPanelController p1 = new PlanPanelController(head, body,start,finish);
        hBoxPlans.getChildren().add(p1);
        System.out.println(hBoxPlans);
    }
private static HBox hBoxPlans;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hBoxPlans = h1;
        listPlans = new ListView();
    }
}
