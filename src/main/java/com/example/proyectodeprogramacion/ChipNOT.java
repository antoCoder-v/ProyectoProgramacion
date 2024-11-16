package com.example.proyectodeprogramacion;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class ChipNOT extends ControladorChip{
    @FXML
    protected AnchorPane paneChip;

    @Override
    protected void ejecutarOperacion() {

        //Logica de la operacion NOT
        setTipoChip("NOT");
    }
}