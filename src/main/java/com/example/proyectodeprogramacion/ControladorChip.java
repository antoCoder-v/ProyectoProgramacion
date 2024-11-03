package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class ControladorChip {

    @FXML
    private AnchorPane paneChip;
    @FXML
    private Rectangle pin1, pin2, pin3, pin4, pin5, pin6, pin7, pin8, pin9, pin10, pin11, pin12, pin13, pin14;
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

    // Método para verificar si el pin está sobre un botón con el color esperado
    private boolean verificarColorPin(Rectangle pin, Color colorEsperado) {
        GridPane[] gridPanes = { protoboard.getBusSuperior(), protoboard.getPistaSuperior(), protoboard.getPistaInferior() };

        // Obtener los límites globales del pin
        Bounds pinBounds = pin.localToScene(pin.getBoundsInLocal());

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;

                    // Obtener el color de fondo del botón
                    Color colorBoton = obtenerColorBoton(button);

                    // Obtener los límites globales del botón
                    Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                    // Verificar si el pin está sobre el botón y el color coincide
                    if (pinBounds.intersects(buttonBounds) && colorBoton.equals(colorEsperado)) {
                        pin.setFill(colorEsperado);  // Cambiar color del pin al color esperado
                        return true;
                    }
                }
            }
        }
        pin.setFill(Color.BLACK);  // Si no está sobre el color esperado, cambiar a negro (sin conexión)
        return false;
    }

    // Método para obtener el color del botón según su id
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

    // Método para verificar las conexiones de los pines 7 y 14
    private void verificarConexionPins() {
        boolean pin7Correcto = verificarColorPin(pin7, Color.RED);   // Negativo (rojo) para pin7
        boolean pin14Correcto = verificarColorPin(pin14, Color.GREEN);  // Positivo (verde) para pin14

        if (pin7Correcto && pin14Correcto) {
            System.out.println("Pin7 conectado a tierra y pin14 a corriente CORRECTAMENTE");
        } else {
            System.out.println("Pines 7 y 14 mal conectados");
        }
    }

    // Método para mostrar una ventana de mensaje
    private void mostrarVentanaMensaje(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

