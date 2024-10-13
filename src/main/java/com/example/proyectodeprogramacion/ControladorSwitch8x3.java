package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

public class ControladorSwitch8x3 {
    @FXML
    private AnchorPane paneSwitch8x3;

    // Variables para almacenar la posición del mouse
    private double offsetX, offsetY;

    @FXML
    public void initialize() {
        // Habilitar la eliminación si es necesario
        EliminarElementos.habilitarEliminacion(paneSwitch8x3);

        // Manejamos los eventos del mouse para arrastrar el chip
        paneSwitch8x3.setOnMousePressed(this::handleMousePressed);
        paneSwitch8x3.setOnMouseDragged(this::handleMouseDragged);
    }

    // Método para manejar el evento cuando se presiona el mouse
    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - paneSwitch8x3.getLayoutX();
        offsetY = event.getSceneY() - paneSwitch8x3.getLayoutY();
    }

    // Método para manejar el evento cuando se arrastra el mouse
    private void handleMouseDragged(MouseEvent event) {
        paneSwitch8x3.setLayoutX(event.getSceneX() - offsetX);
        paneSwitch8x3.setLayoutY(event.getSceneY() - offsetY);
    }

}

