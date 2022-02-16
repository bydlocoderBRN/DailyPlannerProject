package PlanerApp;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
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

//        FXMLLoader loader = new FXMLLoader(getClass().getResource(pathFxml.toUri().toURL()));


    }

    public void setSize(int height){
        mainPane.setMaxHeight(height);
        mainPane.setMinHeight(height);
        mainPane.setPrefHeight(height);
        paneSlider.setPrefHeight(height);
        paneTxt.setPrefHeight(height);
        sliderTime.setPrefHeight(height);
        double i = 0;
        int time =1;
        while (i<height){
            i+=height/24.0;
            Line l = new Line(paneSlider.getPrefWidth()/2, i, paneSlider.getPrefWidth(),i);
            l.setStrokeWidth(3);
            l.setStroke(Paint.valueOf("#20B2AA"));
            paneSlider.getChildren().add(l);
            Text t = new Text(String.valueOf(time));
            t.setLayoutX(paneSlider.getPrefWidth()/2);
            t.setLayoutY(i-5);
            t.setFont(Font.font("Comic Sans MS",15));
            t.setFill( Paint.valueOf("#20B2AA"));
            paneSlider.getChildren().add(t);
            time+=1;

        }
    }
    public void clearMiniPanels(){
        for(int i =0; i<paneSlider.getChildren().size();i++){
            if(paneSlider.getChildren().get(i).getClass().equals(Rectangle.class)){
                paneSlider.getChildren().remove(i);
                i-=1;
            }
        }
    }
    public void addMiniPanel(double startY, double height){
//        double centerX = paneSlider.getPrefWidth()/4;
//        double centerY = (finishY-startY)/2+startY;
//        double radiusX =  paneSlider.getPrefWidth()/4;
//        double radiusY = (finishY-startY)/2+startY;
//        Ellipse e = new Ellipse(centerX,centerY,radiusX,radiusY);
//        e.setFill(Paint.valueOf("#9370DB"));
//        e.
//        paneSlider.getChildren().add(e);

        Rectangle r = new Rectangle(0,startY,paneSlider.getPrefWidth()/4,height);
        r.setFill(Paint.valueOf("#DC143C"));
        r.setOpacity(0.3);
        r.setArcHeight(10);
        r.setArcWidth(10);
        paneSlider.getChildren().add(r);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        paneSlider.setMaxWidth(100);
        paneSlider.setPrefWidth(100);
        paneTxt.setMinWidth(100);
        paneSlider.setMinWidth(100);
        lblTime.setLayoutX(20);
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
