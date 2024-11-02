package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class ControladorChip {

    @FXML
    private AnchorPane paneChip;
    @FXML
    private Rectangle pin1, pin2, pin3, pin4, pin5, pin6, pin7, pin8, pin9, pin10, pin11, pin12, pin13, pin14;
    private double offsetX, offsetY;
    private ControladorProtoboard protoboard;
    public ControladorChip(){}
    @FXML
    public void initialize(){
        protoboard = VariablesGlobales.controladorProtoboard;
        paneChip.setOnMousePressed(this::onMousePressed);
        paneChip.setOnMouseDragged(this::onMouseDragged);
    }
    private void onMousePressed(MouseEvent event) {
            offsetX = event.getSceneX() - paneChip.getLayoutX();
            offsetY = event.getSceneY() - paneChip.getLayoutY();

    }

    // Método para mover el LED mientras se arrastra (SOLO se mueve si led no esta
    // conectado)
    private void onMouseDragged(MouseEvent event) {
            paneChip.setLayoutX(event.getSceneX() - offsetX);
            paneChip.setLayoutY(event.getSceneY() - offsetY);

    }

}
