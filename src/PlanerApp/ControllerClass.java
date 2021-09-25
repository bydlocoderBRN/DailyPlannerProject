package PlanerApp;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Scene;

import javafx.scene.control.Button;

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
    private void btnClick(){
        planDialog = new AddPlanDialogController();
        Stage s = new Stage();
        s.setScene(new Scene(planDialog));
        s.showAndWait();
    }






//    public static void addPlanMainUi(LocalDateTime start, LocalDateTime finish, String head, String body){
//        PlanPanelController p1 = new PlanPanelController();
//        p1.setHead(head);
//        p1.setStartTime(start);
//        p1.setFinishPlanTime(finish);
//        p1.setBody("body");
//        p1.setKey(Plan.newPlan(head,body,start,finish));
////        root.getChildren().add(p1);
//        ControllerClass.hBoxAddPlan(p1);
//    }

    public static void hBoxAddPlan(LocalDateTime start, LocalDateTime finish, String head, String body){
        PlanPanelController p1 = new PlanPanelController();
        p1.setHead(head);
        p1.setStartTime(start);
        p1.setFinishPlanTime(finish);
        p1.setBody("body");
        p1.setKey(Plan.newPlan(head,body,start,finish));
        hBoxPlans.getChildren().add(p1);

    }
private static HBox hBoxPlans;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hBoxPlans = h1;
    }
}
