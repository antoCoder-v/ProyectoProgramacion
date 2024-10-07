package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

public class ControladorChip {
    @FXML
    private AnchorPane paneChip; // Este es el contenedor del chip en la interfaz

    // Variables para almacenar la posición del mouse
    private double offsetX, offsetY;

    @FXML
    public void initialize() {
        // Habilitar la eliminación si es necesario
        EliminarElementos.habilitarEliminacion(paneChip);

        // Manejamos los eventos del mouse para arrastrar el chip
        paneChip.setOnMousePressed(this::handleMousePressed);
        paneChip.setOnMouseDragged(this::handleMouseDragged);
    }

    // Método para manejar el evento cuando se presiona el mouse
    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - paneChip.getLayoutX();
        offsetY = event.getSceneY() - paneChip.getLayoutY();
    }

    // Método para manejar el evento cuando se arrastra el mouse
    private void handleMouseDragged(MouseEvent event) {
        paneChip.setLayoutX(event.getSceneX() - offsetX);
        paneChip.setLayoutY(event.getSceneY() - offsetY);
    }

}

