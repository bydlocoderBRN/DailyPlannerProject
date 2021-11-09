package PlanerApp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/*Привязка планов к графическим элементам. При создании плана через newPlan(), возвращается ключ, через который осуществляется доступ
 к плану в HashMap. Надо привязать этот ключ к графическому элементу, и когда фокус или действие будет на этом элементе, в глобальной переменной
  key значение ключа меняется на то, которое заложено в элементе. Через key будет осуществлен доступ ко всем планам в HashMap*/
public class ControllerClass implements Initializable {
    public static int globalKey;
    private static ObservableList<Integer> keysList;
    private static ObservableList<String> noteList;
    AddPlanDialogController planDialog;
    private static LocalDate dateCurr;
    public static Label lblGlobalKey;
    private static String noteGlobalKey;
    private LocalDateTime noteLocalDateTime;
    @FXML
    private Button btnAddPlan;
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
    @FXML
    private Button btnDeletePlan;
    @FXML
    private Button btnDeleteNote;
    private static Pane panePlans;
    private static ScrollPane scroll;
    @FXML
    private ScrollBar scrollBarPlans;
    @FXML
    private Pane paneNoteDrag;
    @FXML
    private ImageView imgNoteDrag;
    @FXML
    private TextField txtHoursNoteDrag;
    @FXML
    private TextField txtMinutesNoteDrag;
    @FXML
    private Button btnAddNoteDrag;




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
        Main.resetUpdateLists();
    }
public static void updateFilteredNoteList(){
    noteList.clear();
    noteList.addAll(Plan.filterNotes(globalKey));
    Main.resetUpdateLists();
    Main.timerAlarm.start();

}
   private static  int planSpacing = 5;
private static void calculatePaneSizeAndLocation(PlanPanelController p, int parentHeight){
    LocalDateTime start =p.startPlanTime;
    LocalDateTime finish=p.finishPlanTime;
        int height;
        int layoutY;
        int layoutX = panePlans.getChildren().size()*(int)(p.getPrefWidth()+ planSpacing);
        double minuteInPixel = 1440.0/parentHeight;
        if(start.toLocalDate().equals(dateCurr) && finish.toLocalDate().equals(dateCurr)){
            long differentStartFinish = (finish.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC))/60;
            height = (int)(differentStartFinish / minuteInPixel);
            long differentZeroStart = (start.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.of(start.toLocalDate(), LocalTime.of(0,0,0)).toEpochSecond(ZoneOffset.UTC))/60;
            layoutY = (int)(differentZeroStart/minuteInPixel);
            p.setSizeAndLocation(height,layoutX,layoutY);
        }else if(start.toLocalDate().isBefore(dateCurr) && finish.toLocalDate().equals(dateCurr)){
            layoutY = 0;
            long differentZeroFinish = (finish.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.of(dateCurr, LocalTime.of(0,0,0)).toEpochSecond(ZoneOffset.UTC))/60;
            height = (int) (differentZeroFinish/minuteInPixel);
            p.setSizeAndLocation(height,layoutX,layoutY);
            p.isTop();
        }else if(start.toLocalDate().equals(dateCurr) && finish.toLocalDate().isAfter(dateCurr)){
            long differentZeroStart = (start.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.of(start.toLocalDate(), LocalTime.of(0,0,0)).toEpochSecond(ZoneOffset.UTC))/60;
            layoutY = (int)(differentZeroStart/minuteInPixel);
            long diffStartEnd= (LocalDateTime.of(dateCurr, LocalTime.of(23,59,59)).toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC))/60;
            height = (int)(diffStartEnd/minuteInPixel);
            p.setSizeAndLocation(height,layoutX,layoutY);
            p.isBottom();
        }else if(start.toLocalDate().isBefore(dateCurr) && finish.toLocalDate().isAfter(dateCurr)){
            layoutY=0;
            height = parentHeight;
            p.isBottom();
            p.isTop();
            p.setSizeAndLocation(height,layoutX,layoutY);
        }
}
    private static ArrayList<Integer> finalKeys = new ArrayList<Integer>();
    private static void createPlanPanels(ObservableList<Integer> newKeys, ObservableList<Integer> oldKeys) {
        scroll.setContent(null);
        if (newKeys.isEmpty()) {
            panePlans.getChildren().clear();
        } else {
                panePlans.setMinWidth(newKeys.size()*105);
                panePlans.setPrefWidth(newKeys.size()*105);
                panePlans.setPrefHeight(780);
            for (int i =0;i<panePlans.getChildren().size();i++){
                if(!newKeys.contains(((PlanPanelController)panePlans.getChildren().get(i)).getKey())){
                    panePlans.getChildren().remove(i);
                    if(panePlans.getChildren().size()>i) {
                        for (int i1 = i; i1 < panePlans.getChildren().size(); i1++) {
                            panePlans.getChildren().get(i1).setLayoutX(panePlans.getChildren().get(i1).getLayoutX() - 105);
                        }
                    }
                }
            }
            if(!panePlans.getChildren().isEmpty()) {
                ArrayList<Integer> currentKeys = new ArrayList<Integer>();
                for (int i = 0; i < panePlans.getChildren().size(); i++) {
                    currentKeys.add(((PlanPanelController) panePlans.getChildren().get(i)).getKey());
                }
                for (int key : newKeys) {
                    if (!currentKeys.contains(key)) {
                        PlanPanelController p1 = new PlanPanelController(key);
                        calculatePaneSizeAndLocation(p1, (int) scroll.getHeight());
                        panePlans.getChildren().add(p1);
                    }
                }
            }else {
                for (int key : newKeys) {
                        PlanPanelController p1 = new PlanPanelController(key);
                        calculatePaneSizeAndLocation(p1, (int) scroll.getHeight());
                        panePlans.getChildren().add(p1);
                }

            }
        }
        scroll.setContent(panePlans);
    }
//                if (newKeys.size() > oldKeys.size()) {
//                    finalKeys.addAll(newKeys);
//                    finalKeys.removeAll(oldKeys);
//                    if(finalKeys.equals(newKeys)){
//                        panePlans.getChildren().clear();
//                    }
//                    for (int key : finalKeys) {
//                        PlanPanelController p1 = new PlanPanelController(key);
//                        calculatePaneSizeAndLocation(p1, (int)scroll.getHeight());
//                        panePlans.getChildren().add(p1);
//
//                    }
//
//                } else if (newKeys.size()<oldKeys.size()){
//
//                    }
////                    panePlans.getChildren().clear();
////                    for (int key : newKeys) {
////                        PlanPanelController p1 = new PlanPanelController(key);
////                        calculatePaneSizeAndLocation(p1, (int) scroll.getHeight());
////                        panePlans.getChildren().add(p1);
//
//                }else if(newKeys.size()==oldKeys.size()){
//
//                }



boolean isNoteDrag = false;
    private Image imgIconDrag;
    private PlanPanelController planPanelDrag;
    boolean dragDone = false;
    int startDragPaneX=0;
    int startDragPaneY=0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Path imgNote = Path.of("C:\\PlanerApp\\src\\NotificationIcon.png");
        try {
            imgIconDrag = new Image(imgNote.toUri().toURL().toString());
        }catch (MalformedURLException e){System.out.println(e);}
        imgNoteDrag.setImage(imgIconDrag);
        panePlans = new Pane();
        scroll = scroll1;
        startDragPaneX = (int)paneNoteDrag.getLayoutX();
        startDragPaneY = (int)paneNoteDrag.getLayoutY();
        spinH.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));
        spinM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        spinS.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        lblGlobalKey = new Label("0");
        panePlanInfo.setVisible(false);
        dateMain.setValue(LocalDate.now());
        dateCurr = dateMain.getValue();
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

        listNote.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1 != null){
                    noteGlobalKey = t1;
                }
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

          }
      });

        btnDeletePlan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Plan.deletePlan(globalKey);
                updateFilteredKeysList();
                updateFilteredNoteList();
            }
        });
        btnDeleteNote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Plan.deleteNote(noteGlobalKey);
                updateFilteredNoteList();
            }
        });
        paneNoteDrag.toFront();

        paneNoteDrag.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                isNoteDrag = true;

            }
        });
        paneNoteDrag.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                isNoteDrag = false;
                for(int i=0; i<panePlans.getChildren().size();i++){
                    PlanPanelController panel = (PlanPanelController) panePlans.getChildren().get(i);
                    if(paneNoteDrag.getLayoutX()>=panel.getLayoutX()+scroll1.getLayoutX() && paneNoteDrag.getLayoutX()<=panel.getLayoutX()+panel.getWidth()+scroll1.getLayoutX()){
                        planPanelDrag = panel;
                        dragDone = true;
                        break;


                    }

               }
                if(dragDone){
                    showInputDrag(true);
                }
                else{
                    paneNoteDrag.setLayoutX(startDragPaneX);
                    paneNoteDrag.setLayoutY(startDragPaneY);
                }

           }

        });
        paneNoteDrag.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(isNoteDrag){
                    paneNoteDrag.setLayoutX(mouseEvent.getSceneX());
                    paneNoteDrag.setLayoutY(mouseEvent.getSceneY());
                }
            }
        });
        paneNoteDrag.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                paneNoteDrag.setLayoutX(mouseEvent.getSceneX());
                paneNoteDrag.setLayoutY(mouseEvent.getSceneY());
            }
        });
        btnAddNoteDrag.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Plan.toPlan(planPanelDrag.getKey()).addNotification(LocalDateTime.of(dateCurr,LocalTime.of(Integer.parseInt(txtHoursNoteDrag.getText()),Integer.parseInt(txtMinutesNoteDrag.getText()))));
                paneNoteDrag.setLayoutX(startDragPaneX);
                paneNoteDrag.setLayoutY(startDragPaneY);
                showInputDrag(false);
                dragDone = false;
            }
        });
    }

    private void showInputDrag(boolean a){
        if(a) {
            txtHoursNoteDrag.setText("");
            txtMinutesNoteDrag.setText("");
            imgNoteDrag.setVisible(false);
            txtHoursNoteDrag.setVisible(true);
            txtMinutesNoteDrag.setVisible(true);
            btnAddNoteDrag.setVisible(true);
        }else{
            imgNoteDrag.setVisible(true);
            txtHoursNoteDrag.setVisible(false);
            txtMinutesNoteDrag.setVisible(false);
            btnAddNoteDrag.setVisible(false);
        }
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
                    setText("N: "+Plan.getNotification(s).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
                }else if(Plan.getNotificationType(s)==2){
                    setText("A: "+Plan.getNotification(s).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString());
                }else setText("");

            }else setText("");
        }
    }

}
