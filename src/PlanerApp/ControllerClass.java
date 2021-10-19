package PlanerApp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/*Привязка планов к графическим элементам. При создании плана через newPlan(), возвращается ключ, через который осуществляется доступ
 к плану в HashMap. Надо привязать этот ключ к графическому элементу, и когда фокус или действие будет на этом элементе, в глобальной переменной
  key значение ключа меняется на то, которое заложено в элементе. Через key будет осуществлен доступ ко всем планам в HashMap*/
public class ControllerClass implements Initializable {
    public static int globalKey;
    private static HBox hBoxPlans;
    private static ObservableList<Integer> keysList;
    private static ObservableList<String> noteList;
    AddPlanDialogController planDialog;
    private static LocalDate dateCurr;
    public static Label lblGlobalKey;
    private LocalDateTime noteLocalDateTime;
    @FXML
    private Button btnAddPlan;
    @FXML
    private HBox h1;
    @FXML
    public ListView<Integer> listPlans;
    @FXML
    public DatePicker dateMain;
    @FXML
    private ScrollPane scroll1;
    @FXML
    private AnchorPane panePlanInfo;
    @FXML
    private Label lblStartInfo;
    @FXML
    private Label lblFinishInfo;
    @FXML
    private TextField txtHeadInfo;
    @FXML
    private TextArea txtBodyInfo;
    @FXML
    private Button btnAddNotification;
    @FXML
    private Button btnHide;
    @FXML
    private Pane paneAddNote;
    @FXML
    private Button btnAdd;
    @FXML
    private DatePicker dateNote;
    @FXML
    private ToggleButton toggleAlarm;
    @FXML
    private Spinner<Integer> spinH;
    @FXML
    private Spinner<Integer> spinM;
    @FXML
    private Spinner<Integer> spinS;
    @FXML
    private ListView<String> listNote;
    //КАСАТЕЛЬНО ОГРАНИЧЕНИЯ ОБЛАСТИ СПИНЕРОВ: ВСЕ ХУЙНЯ, ДАВАЙ ПО НОВОЙ




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
        ObservableList<Integer> oldKeys = FXCollections.observableArrayList(keysList);
        keysList.clear();
        keysList.addAll(Plan.plansDayFilter(dateCurr));
        System.out.println("KeysList: " + keysList);
        System.out.println("OldKeys: " + oldKeys);
//        listPlans.setItems(keysList);
        createPlanPanels(keysList, oldKeys);
    }
public static void updateFilteredNoteList(){
    noteList.clear();
    noteList.addAll(Plan.filterNotes(globalKey));
}
    private static void createPlanPanels(ObservableList<Integer> newKeys, ObservableList<Integer> oldKeys){
        if(!newKeys.equals(oldKeys)) {
            if (!newKeys.isEmpty()) {
                hBoxPlans.getChildren().clear();
                for (int i : newKeys) {
                    PlanPanelController p1 = new PlanPanelController(i);
                    hBoxPlans.getChildren().add(p1);
                    hBoxPlans.setSpacing(5);
                }
            }
            if (newKeys.isEmpty()) {
                hBoxPlans.getChildren().clear();
            }
        }
    }

//    private LocalTime[] calculateNoteTimeBounds(int key, LocalDate datePicked){
//        //Plan.toPlan(key).getStartTime().toLocalDate()
//        //Plan.toPlan(key).getFinishTime().toLocalDate()
//        //timeArray[0] - min time
//        //timeArray[1] - max time
//        LocalTime[] timeArray = new LocalTime[2];
//        if(Plan.toPlan(key).getStartTime().toLocalDate().equals(Plan.toPlan(key).getFinishTime().toLocalDate()) && datePicked.equals(Plan.toPlan(key).getStartTime().toLocalDate())){
//            timeArray[0]=Plan.toPlan(key).getStartTime().toLocalTime();
//            timeArray[1]=Plan.toPlan(key).getFinishTime().toLocalTime();
//        } else if (datePicked.equals(Plan.toPlan(key).getStartTime().toLocalDate())){
//            timeArray[0]=Plan.toPlan(key).getStartTime().toLocalTime();
//            timeArray[1]=LocalTime.of(23,59,59);
//        }else if(datePicked.equals(Plan.toPlan(key).getFinishTime().toLocalDate())){
//            timeArray[0] = LocalTime.of(0,0,0);
//            timeArray[1] = Plan.toPlan(key).getFinishTime().toLocalTime();
//        }else{
//            timeArray[0] = LocalTime.of(0,0,0);
//            timeArray[1] = LocalTime.of(23,59,59);
//        }
//        return timeArray;
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spinH.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));
        spinM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        spinS.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        lblGlobalKey = new Label("0");
        panePlanInfo.setVisible(false);
        h1.setPrefWidth(960);
        scroll1.setPrefViewportWidth(960);
        scroll1.setPrefViewportHeight(758);
        scroll1.fitToHeightProperty();
        scroll1.fitToWidthProperty();
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
        noteList = FXCollections.observableArrayList("");
        updateFilteredNoteList();
        listNote.setItems(noteList);
        listNote.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> stringListView) {
                return new CellFactoryNote();
            }
        });
        listPlans.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                if(t1!=null){
                    lblGlobalKey.setText(Integer.toString(t1));
                    System.out.println(t1);}
                if (t1==null)
                    System.out.println(t1);
            }
        });


        dateMain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                dateCurr = dateMain.getValue();
                panePlanInfo.setVisible(false);
                updateFilteredKeysList();
            }
        });

        lblGlobalKey.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                globalKey = Integer.parseInt(t1);
                panePlanInfo.setVisible(true);
                lblStartInfo.setText(Plan.toPlan(globalKey).getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
                lblFinishInfo.setText(Plan.toPlan(globalKey).getFinishTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
                txtHeadInfo.setText(Plan.toPlan(globalKey).getHead());
                txtBodyInfo.setText(Plan.toPlan(globalKey).getBody());
                txtBodyInfo.setEditable(false);
                txtHeadInfo.setEditable(false);
                updateFilteredNoteList();
            }
        });
        btnHide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                panePlanInfo.setVisible(false);
            }
        });
      btnAdd.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
              noteLocalDateTime = LocalDateTime.of(dateNote.getValue(),LocalTime.of(spinH.getValue(),spinM.getValue(),spinS.getValue()));
              if((noteLocalDateTime.isAfter(Plan.toPlan(globalKey).getStartTime()) && noteLocalDateTime.isBefore(Plan.toPlan(globalKey).getFinishTime()))||noteLocalDateTime.equals(Plan.toPlan(globalKey).getFinishTime())){
                  if(toggleAlarm.isSelected()){
                      Plan.toPlan(globalKey).addAlarm(noteLocalDateTime);}
                  if(!toggleAlarm.isSelected()){
                      Plan.toPlan(globalKey).addNotification(noteLocalDateTime);
                  }
              }
              updateFilteredNoteList();
              System.out.println(Plan.getAllNotifications());
              System.out.println(Plan.isEmpty());
          }
      });


    }


     class CellFactoryPlan extends ListCell<Integer> {
        @Override
        protected void updateItem(Integer integer, boolean b) {
            super.updateItem(integer, b);

            if (integer != null) {
                setText(Plan.toPlan(integer).getHead());

            }else {
                setText("");
            }
        };


    }
    class CellFactoryNote extends ListCell<String>{
        @Override
        protected void updateItem(String s, boolean b) {
            super.updateItem(s, b);
            if(s!=null){
                if(Plan.getNotificationType(s)==1){
                    setText("N: "+Plan.getNotification(s).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());}
                if(Plan.getNotificationType(s)==2){
                    setText("A: "+Plan.getNotification(s).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
                }

            }else setText("");
        }
    }

}
