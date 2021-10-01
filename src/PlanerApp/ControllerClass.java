package PlanerApp;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.net.URL;
import java.time.LocalDateTime;

import java.util.ResourceBundle;

/*Привязка планов к графическим элементам. При создании плана через newPlan(), возвращается ключ, через который осуществляется доступ
 к плану в HashMap. Надо привязать этот ключ к графическому элементу, и когда фокус или действие будет на этом элементе, в глобальной переменной
  key значение ключа меняется на то, которое заложено в элементе. Через key будет осуществлен доступ ко всем планам в HashMap*/
public class ControllerClass implements Initializable {
    public static int globalKey;
    AddPlanDialogController planDialog;
    @FXML
    private Button btnAddPlan;
    @FXML
    private HBox h1;
    @FXML
    public ListView<Integer> listPlans;
    @FXML
    private void btnClick(){
        planDialog = new AddPlanDialogController();
//        Stage s = new Stage();
//        s.setScene(new Scene(planDialog));
//        s.show();
        planDialog.open();
    }
    private static HBox hBoxPlans;
    private static ObservableList<Integer> keysList;
    public static void hBoxAddPlan(LocalDateTime start, LocalDateTime finish, String head, String body){
        PlanPanelController p1 = new PlanPanelController(head, body,start,finish);
        hBoxPlans.getChildren().add(p1);
        System.out.println((Integer) p1.getKey() + "p1.getkey");
        keysList.add((Integer) p1.getKey());
        System.out.println(keysList);
        System.out.println(hBoxPlans);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hBoxPlans = h1;
        keysList = FXCollections.observableArrayList(Plan.plans.keySet());
        System.out.println(keysList);
        listPlans.setItems(keysList);

        listPlans.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {
            @Override
            public ListCell<Integer> call(ListView<Integer> integerListView) {
                System.out.println("cellFactory");
                return new CellFactoryPlan();

            }
        });

        listPlans.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                globalKey = t1;
            }
        });
    }
     class CellFactoryPlan extends ListCell<Integer> {

        @Override
        protected void updateItem(Integer integer, boolean b) {
            super.updateItem(integer, b);
            System.out.println(integer + " " + b) ;
            if (integer == null || b) {
                System.out.println("CellIsEmpty");
            }else {
                setText(Plan.toPlan(integer).getHead());

            }
        };


    }
}
