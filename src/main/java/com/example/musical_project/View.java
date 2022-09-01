package com.example.musical_project;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class View {
    @FXML
    private AnchorPane personalPane, musicPane, albumPane;

    protected void changePane (int numPane){
        if(numPane == 1){
            personalPane.setVisible(true);
            musicPane.setVisible(false);
            albumPane.setVisible(false);
        }
        else if(numPane == 2){
            personalPane.setVisible(false);
            musicPane.setVisible(true);
            albumPane.setVisible(false);
        }
        else if(numPane == 3){
            personalPane.setVisible(false);
            musicPane.setVisible(false);
            albumPane.setVisible(true);
        }
    }
}
