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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

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
    private ScrollPane scroll;
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
    private static ScrollPane scrollGlobal;
    @FXML
    private ImageView imgNoteDrag;
    @FXML
    private Spinner<Integer> spinHoursDrag;
    @FXML
    private Spinner<Integer> spinMinutesDrag;
    @FXML
    private Button btnAddNoteDrag;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane paneDragNoteAddition;
    @FXML
    private CheckBox checkAlarmDrag;
    @FXML
    private Button btnDragClose;
    private static AnchorPane mainPaneGlobal;

    /////////Authorization////////
    @FXML
    private Button btnAuthorizationShow;
    @FXML
    private Pane paneAuthorization;
    @FXML
    private TextField txtAuthorizationLogin;
    @FXML
    private TextField txtAuthorizationPassword;
    @FXML
    private Button btnAuthorizationSignUp;
    @FXML
    private Button btnAuthorizationClose;
    @FXML
    private Button btnAuthorizationCreatePassword;
    @FXML
    private Button btnAuthorizationLogIn;
    //Generate Password
    @FXML
    private Pane paneAuthGeneratePass;
    @FXML
    private TextField txtAuthGeneratePassP;
    @FXML
    private TextField txtAuthGeneratePassV;
    @FXML
    private TextField txtAuthGeneratePassT;
    @FXML
    private Label lblAuthGeneratePassS;
    @FXML
    private Label lblAuthGeneratePassA;
    @FXML
    private Label lblAuthGeneratePassL;
    @FXML
    private Label lblAuthGeneratePassPassword;
    @FXML
    private CheckBox chkAuthGeneratePassLAT;
    @FXML
    private CheckBox chkAuthGeneratePasslat;
    @FXML
    private CheckBox chkAuthGeneratePassRUS;
    @FXML
    private CheckBox chkAuthGeneratePassrus;
    @FXML
    private CheckBox chkAuthGeneratePassSymb;
    @FXML
    private CheckBox chkAuthGeneratePassNumb;
    @FXML
    private CheckBox chkAuthGeneratePassCustom;
    @FXML
    private TextArea txtAuthGeneratePassCustomAlphabet;
    @FXML
    private Button btnAuthGeneratePass;
    @FXML
    private Button btnAuthGeneratePassClose;

/////Authorization\\\\\\



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
        disableScene(true);
    }

    public static void disableScene(boolean disable){
        mainPaneGlobal.setDisable(disable);
    }
    public static void updateFilteredKeysList(){
        ObservableList<Integer> oldKeys = FXCollections.observableArrayList(keysList);
        keysList.clear();
        keysList.addAll(Plan.plansDayFilter(dateCurr));
        System.out.println("KeysList: " + keysList);
        System.out.println("OldKeys: " + oldKeys);
        createPlanPanels(keysList, oldKeys);
    }
public static void updateFilteredNoteList(){
    noteList.clear();
    noteList.addAll(Plan.filterNotes(globalKey));

}
   private static  int planSpacing = 5;
private static void calculatePaneSizeAndLocation(PlanPanelController p, int parentHeight){
    LocalDateTime start =p.startPlanTime;
    LocalDateTime finish=p.finishPlanTime;
        int height=50;
        int layoutY=0;
        int layoutX = panePlans.getChildren().size()*(int)(p.getPrefWidth()+ planSpacing);
        double minuteInPixel = 1440.0/parentHeight;
        if(start.toLocalDate().equals(dateCurr) && finish.toLocalDate().equals(dateCurr)){
            long differentStartFinish = (finish.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC))/60;
            height = (int)(differentStartFinish / minuteInPixel);
            long differentZeroStart = (start.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.of(start.toLocalDate(), LocalTime.of(0,0,0)).toEpochSecond(ZoneOffset.UTC))/60;
            layoutY = (int)(differentZeroStart/minuteInPixel);

        }else if(start.toLocalDate().isBefore(dateCurr) && finish.toLocalDate().equals(dateCurr)){
            layoutY = 0;
            long differentZeroFinish = (finish.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.of(dateCurr, LocalTime.of(0,0,0)).toEpochSecond(ZoneOffset.UTC))/60;
            height = (int) (differentZeroFinish/minuteInPixel);

            p.isTop();
        }else if(start.toLocalDate().equals(dateCurr) && finish.toLocalDate().isAfter(dateCurr)){
            long differentZeroStart = (start.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.of(start.toLocalDate(), LocalTime.of(0,0,0)).toEpochSecond(ZoneOffset.UTC))/60;
            layoutY = (int)(differentZeroStart/minuteInPixel);
            long diffStartEnd= (LocalDateTime.of(dateCurr, LocalTime.of(23,59,59)).toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC))/60;
            height = (int)(diffStartEnd/minuteInPixel);

            p.isBottom();
        }else if(start.toLocalDate().isBefore(dateCurr) && finish.toLocalDate().isAfter(dateCurr)){
            layoutY=0;
            height = parentHeight;
            p.isBottom();
            p.isTop();

        }
        if(height<p.getMinHeight()){
            height = (int)p.getMinHeight();
        }
        if(layoutY + height>parentHeight){
            layoutY = parentHeight-height;
        }
        p.setSizeAndLocation(height,layoutX,layoutY);

}
    private static GridPane gridPanePlanPanels = new GridPane();

    private static void createPlanPanels(ObservableList<Integer> newKeys, ObservableList<Integer> oldKeys) {
        scrollGlobal.setContent(null);
        if (newKeys.isEmpty()) {
            gridPanePlanPanels.getChildren().clear();
            gridPanePlanPanels.getColumnConstraints().clear();
            Main.timeLine1.clearMiniPanels();
        } else {gridPanePlanPanels.getChildren().clear();
            gridPanePlanPanels.getColumnConstraints().clear();

//                panePlans.getChildren().clear();
//                panePlans.setMinWidth(newKeys.size()*105);
//                panePlans.setPrefWidth(newKeys.size()*105);
//                panePlans.setPrefHeight(scrollGlobal.getPrefHeight()-15);
//                panePlans.setLayoutY(0);
//                panePlans.setLayoutX(0);
                Main.timeLine1.clearMiniPanels();
                for (int key: newKeys){
                        PlanPanelController p1 = new PlanPanelController(key);
                        calculatePaneSizeAndLocation(p1, (int) gridPanePlanPanels.getPrefHeight());
                        Pane panePlanPanels = new Pane();
                        panePlanPanels.setLayoutX(0);
                        panePlanPanels.setLayoutY(0);
                        panePlanPanels.setPrefWidth(gridPanePlanPanels.getPrefWidth());
                        panePlanPanels.setPrefHeight(gridPanePlanPanels.getPrefHeight());
                        panePlanPanels.getChildren().add(p1);

                        if(!addToExistColumn(p1,gridPanePlanPanels)){gridPanePlanPanels.getColumnConstraints().add(new ColumnConstraints(100));
                            GridPane.setColumnIndex(panePlanPanels,gridPanePlanPanels.getColumnCount()-1);
                            gridPanePlanPanels.getChildren().add(panePlanPanels);}

                        Main.timeLine1.addMiniPanel(p1.getLayoutY(), p1.getPrefHeight());
                }
//            for (int i =0;i<panePlans.getChildren().size();i++){
//                if(!newKeys.contains(((PlanPanelController)panePlans.getChildren().get(i)).getKey())){
//                    panePlans.getChildren().remove(i);
//                    if(panePlans.getChildren().size()>i) {
//                        for (int i1 = i; i1 < panePlans.getChildren().size(); i1++) {
//                            panePlans.getChildren().get(i1).setLayoutX(panePlans.getChildren().get(i1).getLayoutX() - 105);
//                        }
//                    }
//                }
//            }
//            if(!panePlans.getChildren().isEmpty()) {
//                ArrayList<Integer> currentKeys = new ArrayList<Integer>();
//                for (int i = 0; i < panePlans.getChildren().size(); i++) {
//                    currentKeys.add(((PlanPanelController) panePlans.getChildren().get(i)).getKey());
//                }
//                for (int key : newKeys) {
//                    if (!currentKeys.contains(key)) {
//                        PlanPanelController p1 = new PlanPanelController(key);
//                        calculatePaneSizeAndLocation(p1, (int) scroll.getHeight());
//                        panePlans.getChildren().add(p1);
//                    }
//                }
//            }else {
//                for (int key : newKeys) {
//                        PlanPanelController p1 = new PlanPanelController(key);
//                        calculatePaneSizeAndLocation(p1, (int) scroll.getHeight());
//                        panePlans.getChildren().add(p1);
//                }
//
//            }
        }
        scrollGlobal.setContent(gridPanePlanPanels);
    }

    private static boolean addToExistColumn(PlanPanelController planPanel, GridPane grid){
        int numberOfTrying=0;
        for(int i=0;i<grid.getColumnCount();i++){
            for(Node node:grid.getChildren()){
                if(GridPane.getColumnIndex(node) == i){
                    numberOfTrying = 0;
                    for(int k=0; k<((Pane)node).getChildren().size();k++){
                        if((planPanel.getLayoutY()+ planPanel.getPrefHeight()<((Pane)node).getChildren().get(k).getLayoutY()) || (planPanel.getLayoutY()>((PlanPanelController)((Pane)node).getChildren().get(k)).getPrefHeight()+((Pane)node).getChildren().get(k).getLayoutY())){
                            numberOfTrying+=1;
                        }
                    }
                    if(numberOfTrying == ((Pane)node).getChildren().size()){
                        ((Pane)node).getChildren().add(planPanel);
                        return true;
                    }

                }
            }
        }
        return false;
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

        mainPaneGlobal = mainPane;
        scrollGlobal = scroll;
        gridPanePlanPanels.setLayoutX(0);
        gridPanePlanPanels.setLayoutY(0);
        gridPanePlanPanels.setPrefHeight(scrollGlobal.getPrefHeight()-15);
        gridPanePlanPanels.setHgap(10);
        imgNoteDrag.toFront();

        Path imgNote = Path.of(Main.PATH + "\\src\\NotificationIcon.png");
        try {
            imgIconDrag = new Image(imgNote.toUri().toURL().toString());
        }catch (MalformedURLException e){System.out.println(e);}
        imgNoteDrag.setImage(imgIconDrag);
        panePlans = new Pane();
        startDragPaneX = (int)imgNoteDrag.getLayoutX();
        startDragPaneY = (int)imgNoteDrag.getLayoutY();
        spinHoursDrag.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));
        spinMinutesDrag.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
        spinH.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23));
        spinM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        spinS.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        spinHoursDrag.setOnScroll(setScrollEvent(spinHoursDrag));
        spinMinutesDrag.setOnScroll(setScrollEvent(spinMinutesDrag));
        spinH.setOnScroll(setScrollEvent(spinH));
        spinM.setOnScroll(setScrollEvent(spinM));
        spinS.setOnScroll(setScrollEvent(spinS));
        lblGlobalKey = new Label("0");
        panePlanInfo.setVisible(false);
        dateMain.setValue(LocalDate.now());
        dateCurr = dateMain.getValue();
        dateNote.setValue(dateCurr);
        keysList = FXCollections.observableArrayList(Plan.plansDayFilter(dateCurr));
        updateFilteredKeysList();
        listPlans.setItems(keysList);
//        Line l = new Line(1000,400,1600,800);
//        l.setStrokeWidth(5);
//        mainPane.getChildren().add(l);
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
        imgNoteDrag.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getSceneX()>100 && mouseEvent.getSceneX()<mainPane.getWidth() && mouseEvent.getSceneY()>0 && mouseEvent.getSceneY()<mainPane.getHeight()) {
                    imgNoteDrag.setLayoutX(mouseEvent.getSceneX());
                    imgNoteDrag.setLayoutY(mouseEvent.getSceneY());
                }
            }
        });

        imgNoteDrag.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dragDone = false;
                int panelX=0;
                for(Node node:gridPanePlanPanels.getChildren()){
                    Pane pane =(Pane)node;
                    System.out.println(GridPane.getColumnIndex(node));
                    panelX = (int)(GridPane.getColumnIndex(node)*(pane.getWidth()+gridPanePlanPanels.getHgap()));
                    for(Node paneNode:pane.getChildren()){

                        PlanPanelController panel = (PlanPanelController)paneNode;
                        System.out.println(panel.getKey()+" panelXY: " +panelX + " " +  panel.getLayoutY()  + " panelHeight: "+panel.getPrefHeight() + " panel width: " + panel.getPrefWidth() + " ImgNoteDragXY: " + imgNoteDrag.getLayoutX() + " " + imgNoteDrag.getLayoutY() +" scrollX: " + scroll.getLayoutX());
                        if(imgNoteDrag.getLayoutX()>=panelX+scroll.getLayoutX() && imgNoteDrag.getLayoutX()<=panelX+panel.getPrefWidth()+scroll.getLayoutX() && imgNoteDrag.getLayoutY()>=panel.getLayoutY()+scroll.getLayoutY() && imgNoteDrag.getLayoutY()<=panel.getLayoutY()+panel.getPrefHeight()+scroll.getLayoutY()){
                            planPanelDrag = panel;
                            dragDone = true;
                            System.out.println("dragDone");
                            break;
                        }else dragDone = false;
                    }
                    if(dragDone){break;}

                }
                if(dragDone){
                    paneDragNoteAddition.setLayoutX(mouseEvent.getSceneX());
                    paneDragNoteAddition.setLayoutY(mouseEvent.getSceneY());
                    showInputDrag(true);
                    imgNoteDrag.setLayoutX(startDragPaneX);
                    imgNoteDrag.setLayoutY(startDragPaneY);

                }
                else{
                    imgNoteDrag.setLayoutX(startDragPaneX);
                    imgNoteDrag.setLayoutY(startDragPaneY);
                }

//                for(int i=0; i<gridPanePlanPanels.getChildren().size();i++){
//                    PlanPanelController panel = (PlanPanelController) gridPanePlanPanels.getChildren().get(i);
//                    if(imgNoteDrag.getLayoutX()>=panel.getLayoutX()+scroll.getLayoutX() && imgNoteDrag.getLayoutX()<=panel.getLayoutX()+panel.getWidth()+scroll.getLayoutX() && imgNoteDrag.getLayoutY()>=panel.getLayoutY()+scroll.getLayoutY() && imgNoteDrag.getLayoutY()<=panel.getLayoutY()+panel.getHeight()+scroll.getLayoutY()){
//                        planPanelDrag = panel;
//                        dragDone = true;
//                        break;
//                    }else dragDone = false;
//               }
//                if(dragDone){
//                    paneDragNoteAddition.setLayoutX(mouseEvent.getSceneX());
//                    paneDragNoteAddition.setLayoutY(mouseEvent.getSceneY());
//                    showInputDrag(true);
//                    imgNoteDrag.setLayoutX(startDragPaneX);
//                    imgNoteDrag.setLayoutY(startDragPaneY);
//                }
//                else{
//                    imgNoteDrag.setLayoutX(startDragPaneX);
//                    imgNoteDrag.setLayoutY(startDragPaneY);
//                }

           }

        });

        btnAddNoteDrag.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LocalDateTime time = LocalDateTime.of(dateCurr,LocalTime.of(spinHoursDrag.getValue(),spinMinutesDrag.getValue()));
                if(time.isAfter(LocalDateTime.now()) && time.isBefore(planPanelDrag.finishPlanTime) && time.isAfter(planPanelDrag.startPlanTime)) {
                    if(!checkAlarmDrag.isSelected()) {
                        Plan.toPlan(planPanelDrag.getKey()).addNotification(time);
                    }else {Plan.toPlan(planPanelDrag.getKey()).addAlarm(time);}
                }
                updateFilteredNoteList();
                showInputDrag(false);
                dragDone = false;
            }
        });
        btnDragClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showInputDrag(false);
            }
        });
        ///Authorization
        paneAuthorization.setVisible(false);
        paneAuthorization.setDisable(true);
        paneAuthGeneratePass.setDisable(true);
        paneAuthGeneratePass.setVisible(false);
        btnAuthorizationShow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                paneAuthorization.setVisible(!false);
                paneAuthorization.setDisable(!true);
            }
        });
        btnAuthorizationCreatePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                paneAuthGeneratePass.setDisable(!true);
                paneAuthGeneratePass.setVisible(!false);
            }
        });
        btnAuthorizationClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                paneAuthorization.setVisible(false);
                paneAuthorization.setDisable(true);
            }
        });
        btnAuthGeneratePassClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                paneAuthGeneratePass.setDisable(true);
                paneAuthGeneratePass.setVisible(false);
            }
        });
        chkAuthGeneratePassLAT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(chkAuthGeneratePassLAT.isSelected()){
                    Authorization.plusAbcUpper();
                }else {Authorization.minusAbcUpper();}
            }
        });
        chkAuthGeneratePasslat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(chkAuthGeneratePasslat.isSelected()){
                    Authorization.plusAbc();
                }else {Authorization.minusAbc();}
            }
        });
        chkAuthGeneratePassRUS.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(chkAuthGeneratePassRUS.isSelected()){
                    Authorization.plusRusUpper();
                }else {Authorization.minusRusUpper();}
            }
        });
        chkAuthGeneratePassrus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(chkAuthGeneratePassrus.isSelected()){
                    Authorization.plusRus();
                }else {Authorization.minusRus();}
            }
        });
        chkAuthGeneratePassNumb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(chkAuthGeneratePassNumb.isSelected()){
                    Authorization.plusNumbers();
                }else{Authorization.minusNumbers();}
            }
        });
        chkAuthGeneratePassSymb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(chkAuthGeneratePassSymb.isSelected()){
                    Authorization.plusSymbols();
                }else {Authorization.minusSymbols();}
            }
        });
        chkAuthGeneratePassCustom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(chkAuthGeneratePassCustom.isSelected()){

                    chkAuthGeneratePassRUS.setSelected(false);
                    chkAuthGeneratePassrus.setSelected(false);
                    chkAuthGeneratePassLAT.setSelected(false);
                    chkAuthGeneratePasslat.setSelected(false);
                    chkAuthGeneratePassSymb.setSelected(false);
                    chkAuthGeneratePassNumb.setSelected(false);
                }
            }
        });
        btnAuthGeneratePass.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(chkAuthGeneratePassCustom.isSelected()){
                    Authorization.customAlphabet(txtAuthGeneratePassCustomAlphabet.getText());
                }
                String[]s = Authorization.generatePassword(Double.parseDouble(txtAuthGeneratePassP.getText()),Double.parseDouble(txtAuthGeneratePassV.getText()), Double.parseDouble(txtAuthGeneratePassT.getText()));
                lblAuthGeneratePassPassword.setText(s[0]);
                lblAuthGeneratePassS.setText(s[1]);
                lblAuthGeneratePassA.setText(s[2]);
                lblAuthGeneratePassL.setText(s[3]);
                chkAuthGeneratePassRUS.setSelected(false);
                chkAuthGeneratePassrus.setSelected(false);
                chkAuthGeneratePassLAT.setSelected(false);
                chkAuthGeneratePasslat.setSelected(false);
                chkAuthGeneratePassSymb.setSelected(false);
                chkAuthGeneratePassNumb.setSelected(false);
                chkAuthGeneratePassCustom.setSelected(false);
            }
        });
    }

    private void showInputDrag(boolean a){

        if(a) {
            paneDragNoteAddition.setDisable(false);
            paneDragNoteAddition.setVisible(true);
        }else{
            paneDragNoteAddition.setDisable(true);
            paneDragNoteAddition.setVisible(false);
            planPanelDrag=null;
            checkAlarmDrag.setSelected(false);
            spinHoursDrag.getValueFactory().setValue(0);
            spinMinutesDrag.getValueFactory().setValue(0);
        }
    }

private EventHandler<ScrollEvent> setScrollEvent(Spinner spinner) {
    EventHandler<ScrollEvent> spinScrollEvent = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            if (scrollEvent.getDeltaY() > 0) {
                spinner.increment();
            } else {
                spinner.decrement();
            }
        }
    };
    return  spinScrollEvent;
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
    private static int listViewColorCount =0;
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
