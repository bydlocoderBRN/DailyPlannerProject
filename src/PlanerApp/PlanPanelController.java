package PlanerApp;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

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
    @FXML
    GridPane grid;
    private int key;
    LocalDateTime startPlanTime;
    LocalDateTime finishPlanTime;
    String head;
    String body;
    int clickCounter = 0;
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
        }catch (IOException ex){System.out.println(ex);}
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
        txtHoursStart.setText(Integer.toString(startPlanTime.getHour()));
        txtMinutesStart.setText(Integer.toString(startPlanTime.getMinute()));}
    public void setFinishPlanTime(LocalDateTime f){finishPlanTime=f;
        txtHoursFinish.setText(Integer.toString(finishPlanTime.getHour()));
        txtMinutesFinish.setText(Integer.toString(finishPlanTime.getMinute()));}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        if(mouseEvent.getClickCount() == 2){
                            ControllerClass.lblGlobalKey.setText(Integer.toString(key));
                        }
                    }

                }
            });
//        txtHoursStart.setText(Integer.toString(startPlanTime.getHour()));
//        txtMinutesStart.setText(Integer.toString(startPlanTime.getMinute()));
//        txtHoursFinish.setText(Integer.toString(finishPlanTime.getHour()));
//        txtMinutesFinish.setText(Integer.toString(finishPlanTime.getMinute()));
//        txtHead.setText(head);

    }
}
