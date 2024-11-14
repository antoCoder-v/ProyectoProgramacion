package com.example.proyectodeprogramacion;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class ChipAND extends ControladorChip{
    @FXML
    protected AnchorPane paneChip;
    @Override
    protected void ejecutarOperacion() {
        System.out.println("Ejecutando operación AND");
        // Lógica específica para la operación AND
    }
}
