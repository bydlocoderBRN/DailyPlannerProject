package PlanerApp;

import com.sun.javafx.scene.paint.GradientUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;

public class TimeLineController extends AnchorPane {
    private int sliderValue;
@FXML
private Slider slide1;
   public TimeLineController(){
        this.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getX() != slide1.getLayoutX()){
                    slide1.setLayoutX(mouseEvent.getX()-(int)slide1.getWidth()/2);
                    sliderValue = (int)slide1.getValue();
                }
            }
        });
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TimeLinePanel.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        }catch (IOException e){System.out.println(e);}

    }
    public int getValue(){
        return sliderValue;
    }

}
