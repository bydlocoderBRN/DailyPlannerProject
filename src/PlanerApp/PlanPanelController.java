package PlanerApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    public void setKey(int newKey){key = newKey;}
    public int getKey(){return key;}
    public void setHead(String h){head=h;
        txtHead.setText(head);}
    public void setBody (String b){body=b;}
    public void setStartTime(LocalDateTime s){startPlanTime=s;
        txtHoursStart.setText(Integer.toString(startPlanTime.getHour()));
        txtMinutesStart.setText(Integer.toString(startPlanTime.getMinute()));}
    public void setFinishPlanTime(LocalDateTime f){finishPlanTime=f;
        txtHoursFinish.setText(Integer.toString(finishPlanTime.getHour()));
        txtMinutesFinish.setText(Integer.toString(finishPlanTime.getMinute()));}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtHoursStart.setText(Integer.toString(startPlanTime.getHour()));
        txtMinutesStart.setText(Integer.toString(startPlanTime.getMinute()));
        txtHoursFinish.setText(Integer.toString(finishPlanTime.getHour()));
        txtMinutesFinish.setText(Integer.toString(finishPlanTime.getMinute()));
        txtHead.setText(head);

    }
}
