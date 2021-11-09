package PlanerApp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TimeLineController extends HBox implements Initializable {
    int hours;
    int minutes;
@FXML
    Pane paneSlider;
@FXML
    Pane paneTxt;
@FXML
    Slider sliderTime;
@FXML
    Label lblTime;
@FXML
    HBox mainPane;
    public TimeLineController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TimeLinePanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch (IOException ex){System.out.println(ex);}
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainPane.setMaxHeight(800);
        paneSlider.setMaxHeight(800);
        paneSlider.setMaxWidth(100);
        paneSlider.setPrefWidth(100);
        sliderTime.setMaxHeight(800);
        sliderTime.setPrefHeight(800);
        paneTxt.setMaxHeight(800);
        paneSlider.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sliderTime.setLayoutX(mouseEvent.getX()-sliderTime.getWidth()/2);
            }
        });
        sliderTime.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(sliderTime.getLayoutX()>0 && sliderTime.getLayoutX()<= (paneSlider.getMaxWidth()-sliderTime.getWidth())){
                    sliderTime.setLayoutX(mouseEvent.getX()+sliderTime.getLayoutX()-sliderTime.getWidth()/2);
                }
            }
        });

        sliderTime.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) { lblTime.setText(String.valueOf(newVal));
            newVal = 1440-newVal.intValue();
                if(newVal.intValue()/60>=1){
                    hours = newVal.intValue()/60;
                    minutes = newVal.intValue() - hours*60;
                }else {
                    hours = 0;
                    minutes = newVal.intValue();
                }
                lblTime.setText(Integer.toString(hours) + ":" + Integer.toString(minutes));
            }
        });
        sliderTime.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                lblTime.setLayoutY(mouseEvent.getY());
            }
        });
        sliderTime.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                lblTime.setVisible(true);
            }
        });
        sliderTime.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                lblTime.setVisible(false);
            }
        });
    }
}
