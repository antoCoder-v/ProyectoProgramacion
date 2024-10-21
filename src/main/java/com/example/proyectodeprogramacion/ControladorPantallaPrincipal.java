package com.example.proyectodeprogramacion;

// Java version: 20
// IDE: IntelliJ IDEA 2022.1

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ControladorPantallaPrincipal {

    @FXML
    private Button mostrarLed, mostrarSwitch, agregarProtoboard, botonAgregaBateria, botonSwitch3X3,botonSwitch8x3;
    @FXML
    private AnchorPane pantallaPrincipal;
    @FXML
    private Button agregaResistencia;

    @FXML
    public void initialize() {

        // Inicializar la variable estática
        VariablesGlobales.pantallaPrincipal = pantallaPrincipal;
        VariablesGlobales.aparecioBateria = false;

        botonAgregaBateria.setOnAction(event -> cargarInterfacezElementos("motor.fxml"));
        mostrarLed.setOnAction(event -> cargarInterfacezElementos("led.fxml"));
        agregarProtoboard.setOnAction(event -> cargarInterfacezElementos("protoboard.fxml"));
        botonSwitch3X3.setOnAction(event -> cargarInterfacezElementos("Switch3X3.fxml"));
        botonSwitch8x3.setOnAction(event -> cargarInterfacezElementos("Switch8x3.fxml"));
        agregaResistencia.setOnAction(event -> cargarInterfacezElementos("resistencia.fxml"));

    }

    // Método que carga la interfaces de los elemtentos de los botones
    private void cargarInterfacezElementos(String nombre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombre));
            // Carga el archivo FXML
            Parent elemento = loader.load();
            elemento.setLayoutX(47);
            elemento.setLayoutY(100);
            pantallaPrincipal.getChildren().add(elemento);

        } catch (IOException e) {

        }
    }

}
