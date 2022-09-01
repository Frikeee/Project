package com.example.musical_project;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animation {
    private TranslateTransition animObj;

    public Animation(Node node){
        animObj = new TranslateTransition(Duration.millis(70), node);
        animObj.setFromX(0f);
        animObj.setByX(10f);
        animObj.setCycleCount(4);
        animObj.setAutoReverse(true);
    }

    public void startAnimation(){
        animObj.playFromStart();
    }


}
