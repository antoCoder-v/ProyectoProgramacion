package com.example.proyectodeprogramacion;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ControladorPantallaPrincipal {

    @FXML
    private Button mostrarLed, mostrarSwitch, agregarProtoboard, botonAgregaBateria, verCircuito, botonSwitch3X3,mostrarChip;
    @FXML
    private AnchorPane pantallaPrincipal;

    @FXML
    public void initialize() {

        // Inicializar la variable estática
        VariablesGlobales.pantallaPrincipal = pantallaPrincipal;
        VariablesGlobales.aparecioBateria = false;

        botonAgregaBateria.setOnAction(event -> cargarInterfacezElementos("motor.fxml"));
        mostrarLed.setOnAction(event -> cargarInterfacezElementos("led.fxml"));
        mostrarSwitch.setOnAction(event -> cargarInterfacezElementos("switch.fxml"));
        agregarProtoboard.setOnAction(event -> cargarInterfacezElementos("protoboard.fxml"));
        botonSwitch3X3.setOnAction(event -> cargarInterfacezElementos("Switch3X3.fxml"));
        mostrarChip.setOnAction(event -> cargarInterfacezElementos("chip.fxml"));
        // verCircuito.setOnAction(actionEvent -> verConexion());

    }

    // Método que carga la interfaces de los elemtentos de los botones
    private void cargarInterfacezElementos(String nombre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombre));
            // Carga el archivo FXML
            Parent elemento = loader.load();
            elemento.setLayoutX(47);
            elemento.setLayoutY(53);
            pantallaPrincipal.getChildren().add(elemento);

        } catch (IOException e) {

        }
    }

    private void verConexion() {
        VariablesGlobales.controladorProtoboard.verificarCircuitoCerrado();
    }
}
