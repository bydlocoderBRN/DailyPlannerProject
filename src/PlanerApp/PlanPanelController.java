package PlanerApp;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Rotate;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class PlanPanelController extends GridPane implements Initializable {
    @FXML
    TextField txtMinutesStart;
    @FXML
    TextField txtHoursStart;
    @FXML
    TextArea txtHead;
    @FXML
    TextField txtMinutesFinish;
    @FXML
    TextField txtHoursFinish;

    private int key;
    LocalDateTime startPlanTime;
    LocalDateTime finishPlanTime;
    String head;
    String body;
    PlanPanelController(){
        FXMLLoader loader =new FXMLLoader(getClass().getResource("PlanPanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch (IOException ex){System.out.println(ex);}

    }
    //Теперь панель плана не содержит самого плана, а лишь его ключ. При создании нового плана тот сначала добавляется в plans а потом на основе его ключа создается панель
    PlanPanelController(int planKey){

        FXMLLoader loader =new FXMLLoader(getClass().getResource("PlanPanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch (IOException ex){System.out.println(ex.getCause());}
        key=planKey;
        setStartTime(Plan.toPlan(key).getStartTime());
        setFinishPlanTime(Plan.toPlan(key).getFinishTime());
        setHead(Plan.toPlan(key).getHead());
        setBody(Plan.toPlan(key).getBody());
//        key = Plan.newPlan(head,body,start,finish);
//        setStartTime(Plan.toPlan(key).getStartTime());
//        setFinishPlanTime(Plan.toPlan(key).getFinishTime());
//        setHead(Plan.toPlan(key).getHead());
//        setBody(Plan.toPlan(key).getBody());
    }
    public int getKey(){return key;}
    public void setHead(String h){head=h;
        txtHead.setText(head);}
    public void setBody (String b){body=b;}
    public void setStartTime(LocalDateTime s){ startPlanTime=s;
        if(startPlanTime.getHour()<10){
            txtHoursStart.setText("0"+Integer.toString(startPlanTime.getHour()) + ":");
        }else {txtHoursStart.setText(Integer.toString(startPlanTime.getHour())+":");}
        if(startPlanTime.getMinute()<10){
            txtMinutesStart.setText("0"+Integer.toString(startPlanTime.getMinute()));
        }else {txtMinutesStart.setText(Integer.toString(startPlanTime.getMinute()));}}
    public void setFinishPlanTime(LocalDateTime f){finishPlanTime=f;
        if(finishPlanTime.getHour()<10){
            txtHoursFinish.setText("0"+Integer.toString(finishPlanTime.getHour())+":");
        }else {txtHoursFinish.setText(Integer.toString(finishPlanTime.getHour())+":");}
        if(finishPlanTime.getMinute()<10){
            txtMinutesFinish.setText("0"+Integer.toString(finishPlanTime.getMinute()));
        }else {txtMinutesFinish.setText(Integer.toString(finishPlanTime.getMinute()));}
}

    public void setSizeAndLocation(int height, int layoutX, int layoutY){
        this.setPrefHeight(height);
        txtHead.setPrefHeight(this.getPrefHeight()-50);
        if(height< txtMinutesStart.getMinHeight()+txtHoursFinish.getMinHeight()+txtHead.getMinHeight()){
            txtHead.setLayoutY(-25);
            txtHead.setPrefHeight(this.getPrefHeight());
            txtMinutesStart.setVisible(false);
            txtMinutesFinish.setVisible(false);
        }
        this.setLayoutX(layoutX);
        this.setLayoutY(layoutY);

//        txtHead.setLayoutX(350);
//        txtHead.setLayoutY(0);
//        Rotate rotateTxt = new Rotate();
//        rotateTxt.setAngle(0);
//        rotateTxt.setPivotX(0);
//        rotateTxt.setPivotY(this.getPrefHeight()-50);
//        txtHead.getTransforms().addAll(rotateTxt);
    }
    public void isTop(){
        this.txtHoursStart.setVisible(false);
        this.txtMinutesStart.setVisible(false);
    };
    public void isBottom(){
        this.txtHoursFinish.setVisible(false);
        this.txtMinutesFinish.setVisible(false);
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.getRowConstraints().get(0).setMaxHeight(txtHoursStart.getHeight());
        this.getRowConstraints().get(2).setMaxHeight(txtHoursFinish.getHeight());
        this.setOnMouseClicked(eventDoubleClick);
        txtMinutesStart.setOnMouseClicked(eventDoubleClick);
        txtHoursStart.setOnMouseClicked(eventDoubleClick);
        txtHoursFinish.setOnMouseClicked(eventDoubleClick);
        txtMinutesFinish.setOnMouseClicked(eventDoubleClick);
        txtHead.setOnMouseClicked(eventDoubleClick);


    }
    EventHandler<MouseEvent> eventDoubleClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        ControllerClass.lblGlobalKey.setText(Integer.toString(key));
                    }
                }
        }
    };
//        txtHoursStart.setText(Integer.toString(startPlanTime.getHour()));
//        txtMinutesStart.setText(Integer.toString(startPlanTime.getMinute()));
//        txtHoursFinish.setText(Integer.toString(finishPlanTime.getHour()));
//        txtMinutesFinish.setText(Integer.toString(finishPlanTime.getMinute()));
//        txtHead.setText(head);

    }

