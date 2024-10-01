package com.example.proyectodeprogramacion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ControladorBateria {

    @FXML
    private Button botonCargaNegativa,botonCargaPositiva;
    @FXML
    private AnchorPane paneBateria;

    //variables para almacenar la posición del mouse
    private double offsetX,offsetY;

    @FXML
    public void initialize() {
        EliminarElementos.habilitarEliminacion(paneBateria);
        //Manejamos los movimientos del mouse en el paneBateria
        paneBateria.setOnMousePressed(this::handleMousePressed);
        paneBateria.setOnMouseDragged(this::handleMouseDragged);

    }

    @FXML
    public void botonCargaNegativa(ActionEvent event) {
        VariablesGlobales.aparecioBateria = true;
        VariablesGlobales.botonPresionadoBateria = botonCargaNegativa;

        botonCargaNegativa.setStyle("-fx-background-color: red;");
    }

    @FXML
    public void botonCargaPositiva(ActionEvent event) {
        VariablesGlobales.aparecioBateria = true;
        VariablesGlobales.botonPresionadoBateria = botonCargaPositiva;

        botonCargaPositiva.setStyle("-fx-background-color: green;");
    }

    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - paneBateria.getLayoutX();
        offsetY = event.getSceneY() - paneBateria.getLayoutY();
    }

    private void handleMouseDragged(MouseEvent event) {
        paneBateria.setLayoutX(event.getSceneX() - offsetX);
        paneBateria.setLayoutY(event.getSceneY() - offsetY);
    }

}
