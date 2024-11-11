package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;

public class ControladorChip {

    @FXML
    private AnchorPane paneChip;
    @FXML
    private Rectangle pin1, pin2, pin3, pin4, pin5, pin6, pin7, pin8, pin9, pin10, pin11, pin12, pin13, pin14;
    @FXML
    private Button botonChip; // Agregar referencia al botón "botonChip" en el FXML
    private double offsetX, offsetY;
    private ControladorProtoboard protoboard;
    public ControladorChip() {}

    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;
        paneChip.setOnMousePressed(this::onMousePressed);
        paneChip.setOnMouseDragged(this::onMouseDragged);


    }

    private void onMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - paneChip.getLayoutX();
        offsetY = event.getSceneY() - paneChip.getLayoutY();
    }

    private void onMouseDragged(MouseEvent event) {
        paneChip.setLayoutX(event.getSceneX() - offsetX);
        paneChip.setLayoutY(event.getSceneY() - offsetY);
        verificarConexionPins(); // Verificar las conexiones de los pines 7 y 14 después de mover el chip
    }

    private boolean verificarColorPin(Rectangle pin, Color colorEsperado) {
        GridPane[] gridPanes = { protoboard.getBusSuperior(), protoboard.getPistaSuperior(), protoboard.getPistaInferior() };
        Bounds pinBounds = pin.localToScene(pin.getBoundsInLocal());

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    Color colorBoton = obtenerColorBoton(button);
                    Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                    if (pinBounds.intersects(buttonBounds) && colorBoton.equals(colorEsperado)) {
                        pin.setFill(colorEsperado);
                        return true;
                    }
                }
            }
        }
        pin.setFill(Color.BLACK);
        return false;
    }

    private Color obtenerColorBoton(Button button) {
        String buttonId = button.getId();
        String[] parts = buttonId.split("-");
        String carga = parts.length > 4 ? parts[4].trim() : "";

        switch (carga) {
            case "positiva":
                return Color.GREEN;
            case "negativa":
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }

    private void verificarConexionPins() {
        boolean pin7Correcto = verificarColorPin(pin7, Color.RED);
        boolean pin14Correcto = verificarColorPin(pin14, Color.GREEN);
        boolean pin1Correcto = verificarColorPin(pin1, Color.GREEN);
        boolean pin2Correcto = verificarColorPin(pin2, Color.GREEN);
        boolean pin4Correcto = verificarColorPin(pin4, Color.GREEN);
        boolean pin5Correcto = verificarColorPin(pin5, Color.GREEN);
        boolean pin9Correcto = verificarColorPin(pin9, Color.GREEN);
        boolean pin10Correcto = verificarColorPin(pin10, Color.GREEN);
        boolean pin12Correcto = verificarColorPin(pin12, Color.GREEN);
        boolean pin13Correcto = verificarColorPin(pin13, Color.GREEN);

        if (pin7Correcto && pin14Correcto) {
            System.out.println("Pin7 conectado a tierra y pin14 a corriente CORRECTAMENTE");
        } else {
            System.out.println("Pines 7 y 14 mal conectados");
        }
        if(pin1Correcto && pin2Correcto){
            System.out.println("Compuerta 1 activa.\nSalida pin3: 1");
        }
        else{
            System.out.println("Compuerta 1 activa.\nSalida pin3: 0");
        }
        if(pin4Correcto && pin5Correcto){
            System.out.println("Compuerta 2 activa.\nSalida pin3: 1");
        }
        else{
            System.out.println("Compuerta 2 activa.\nSalida pin3: 0");
        }
        if(pin9Correcto && pin10Correcto){
            System.out.println("Compuerta 3 activa.\nSalida pin3: 1");
        }
        else{
            System.out.println("Compuerta 3 activa.\nSalida pin3: 0");
        }
        if(pin2Correcto && pin13Correcto){
            System.out.println("Compuerta 4 activa.\nSalida pin3: 1");
        }
        else{
            System.out.println("Compuerta 4 activa.\nSalida pin3: 0");
        }
    }

    private void mostrarVentanaMensaje(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


