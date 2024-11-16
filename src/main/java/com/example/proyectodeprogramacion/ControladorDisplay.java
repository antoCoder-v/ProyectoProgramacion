package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;

public class ControladorDisplay {
    //SOLO RECIBE CARGA NEGATIVA

    @FXML
    private AnchorPane anchorDeDisplay;

    @FXML
    private Circle a, b, c, d, e, f, g; //Ciculos que nos ayudan a reconocder la carga negativa

    @FXML
    private Rectangle deA, deB, deC, deD, deE, deF, deG;    //Se encienden segun el circle

    private double offsetX, offsetY;
    private ControladorProtoboard protoboard;

    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;

        //Controlamos movimiento del display
        anchorDeDisplay.setOnMousePressed(this::onMousePressed);
        anchorDeDisplay.setOnMouseDragged(this::onMouseDragged);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                reconoceCarga(a, deA);
                reconoceCarga(b, deB);
                reconoceCarga(c, deC);
                reconoceCarga(d, deD);
                reconoceCarga(e, deE);
                reconoceCarga(f, deF);
                reconoceCarga(g, deG);                
            }
        };
        timer.start();
    }

    private void onMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - anchorDeDisplay.getLayoutX();
        offsetY = event.getSceneY() - anchorDeDisplay.getLayoutY();
    }

    private void onMouseDragged(MouseEvent event) {
        anchorDeDisplay.setLayoutX(event.getSceneX() - offsetX);
        anchorDeDisplay.setLayoutY(event.getSceneY() - offsetY);
    }

    //verifica e ilumina si recibe carga negativa
    private void reconoceCarga(Circle recibe, Rectangle luz) {
        if(protoboard == null){
            return;
        }

        Bounds recibeBounds = recibe.localToScene(recibe.getBoundsInLocal());
        GridPane[] gridPanes = {protoboard.getPistaSuperior(), protoboard.getPistaInferior() };
        
        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                Button button = (Button) node;

                // variables para guardarpartes de la id
                String buttonId = button.getId();
                String carga = ""; // obtenemos carga del boton
                String[] parts = buttonId.split("-"); // se separa en partes la id
                carga = parts[4].trim(); // obtenemos solo carga del boton

                // Obtener los límites del botón
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                //verificamos si el display recibe carga positiva
                if(recibeBounds.intersects(buttonBounds) && carga.equals("positiva")){
                    luz.setFill(Color.WHITE);
                    recibe.setFill(Color.GREEN);
                }else if(recibeBounds.intersects(buttonBounds) && carga.equals("negativa") || 
                    recibeBounds.intersects(buttonBounds) && carga.equals("0")){
                    luz.setFill(Color.GRAY);
                    recibe.setFill(Color.GRAY);
                }
                
            }
        }

    }
    
}
