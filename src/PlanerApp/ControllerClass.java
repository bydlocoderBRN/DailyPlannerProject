package PlanerApp;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ResourceBundle;

/*Привязка планов к графическим элементам. При создании плана через newPlan(), возвращается ключ, через который осуществляется доступ
 к плану в HashMap. Надо привязать этот ключ к графическому элементу, и когда фокус или действие будет на этом элементе, в глобальной переменной
  key значение ключа меняется на то, которое заложено в элементе. Через key будет осуществлен доступ ко всем планам в HashMap*/
public class ControllerClass implements Initializable {
    public static int globalKey;
    private static HBox hBoxPlans;
    private static ObservableList<Integer> keysList;
    AddPlanDialogController planDialog;
    private static LocalDate dateCurr;
    @FXML
    private Button btnAddPlan;
    @FXML
    private HBox h1;
    @FXML
    public ListView<Integer> listPlans;
    @FXML
    public DatePicker dateMain;


//    public static void hBoxAddPlan(LocalDateTime start, LocalDateTime finish, String head, String body){
////        PlanPanelController p1 = new PlanPanelController(head, body,start,finish);
////        hBoxPlans.getChildren().add(p1);
////        keysList.add((Integer) p1.getKey());
//        Plan.newPlan(head,body,start,finish);
//        updateFilteredKeysList();
//    }



/*Реализация системы хранения планов и навигации по ним.
-> Элемент PlanPanel теперь не содержит самого плана и не создает новый план. В конструктор просто передается ключ нужного плана
-> При любом изменении plans (а сейчас plans меняется только после добавления нового плана через диалоговое окно) вызывается функция updateFilteredKeysList(далее update), которая помещает в keysList только планы, которые есть в выбранный пользователем день,
а затем вызывает функцию отрисовки панелей планов в приложении.
->Также update вызыается когда интерфейс загружается и при изменении пользователем дня, который он просматривает.
->Отрисовка происходит путем очищения hBox для планов и отрисовки всех элементов keysList.
->update работает следующим образом: в oldKeys сохраняется текущее состояние keysList, keysList очищается и в него помещаются все ключи всех планов ввыбранный денб(Plan.plansDayFilter)*/
    @FXML
    private void btnClick(){
        planDialog = new AddPlanDialogController();
        planDialog.open();
    }
    public static void updateFilteredKeysList(){
        ObservableList<Integer> oldKeys = FXCollections.observableArrayList(keysList);;
        keysList.clear();
        keysList.addAll(Plan.plansDayFilter(dateCurr));
        System.out.println("KeysList: " + keysList);
        System.out.println("OldKeys" + keysList);
            createPlanPanels(keysList, oldKeys);
    }

    private static void createPlanPanels(ObservableList<Integer> newKeys, ObservableList<Integer> oldKeys){
        if(!newKeys.equals(oldKeys)) {
            if (!newKeys.isEmpty()) {
                hBoxPlans.getChildren().clear();
                for (int i : newKeys) {
                    PlanPanelController p1 = new PlanPanelController(i);
                    hBoxPlans.getChildren().add(p1);
                }
            }
            if (newKeys.isEmpty()) {
                hBoxPlans.getChildren().clear();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateMain.setValue(LocalDate.now());
        dateCurr = dateMain.getValue();
        hBoxPlans = h1;
        keysList = FXCollections.observableArrayList(Plan.plansDayFilter(dateCurr));
        updateFilteredKeysList();
        listPlans.setItems(keysList);
        listPlans.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {
            @Override
            public ListCell<Integer> call(ListView<Integer> integerListView) {

                return new CellFactoryPlan();

            }

        });

//        listPlans.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
//            @Override
//            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
//                globalKey = t1;
//            }
//        });
        dateMain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                dateCurr = dateMain.getValue();
                updateFilteredKeysList();
            }
        });
    }


     class CellFactoryPlan extends ListCell<Integer> {
        @Override
        protected void updateItem(Integer integer, boolean b) {
            super.updateItem(integer, b);

            if (integer == null || b) {

            }else {
                setText(Plan.toPlan(integer).getHead());

            }
        };


    }


}
