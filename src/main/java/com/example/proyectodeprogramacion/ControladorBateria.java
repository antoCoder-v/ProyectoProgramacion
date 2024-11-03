package com.example.proyectodeprogramacion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ControladorBateria {

    @FXML
    private Button botonCargaPositiva;
    @FXML
    private Button botonCargaNegativa;
    @FXML
    private AnchorPane paneBateria;
    @FXML
    private Button corrriente;

    // variables para almacenar la posición del mouse
    private double offsetX, offsetY;
    private boolean pasoCorriente = true;
    private Cables cableManager;

    @FXML
    public void initialize() {
        VariablesGlobales.aparecioBateria = true;
        // EliminarElementos.habilitarEliminacion(paneBateria);
        cableManager = VariablesGlobales.cables;

        // Manejamos los movimientos del mouse en el paneBateria
        paneBateria.setOnMousePressed(this::handleMousePressed);
        paneBateria.setOnMouseDragged(this::handleMouseDragged);

        corrriente.setOnAction(event -> {
            pasoCorriente = !pasoCorriente; // Cambia el paso de corriente
            // Identificamos el apagar y encerde corriente
            if (!pasoCorriente) {
                VariablesGlobales.corrienteBateria = false;
                cableManager.LogicaBateria();

            } else {
                VariablesGlobales.corrienteBateria = true;
                cableManager.LogicaBateria();
            }
            // actualizar cargas
            cableManager.actualizarCorrienteTodos();
        });

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
