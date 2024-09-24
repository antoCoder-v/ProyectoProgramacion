package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

public class ControladorLed {

    @FXML
    private Circle patita1,patita2,luzCirculo;  // Vinculado con fx:id="patita1"
    @FXML
    private Rectangle luzRectangulo;
    @FXML
    private AnchorPane ledPane;    // Contenedor del LED y las patitas
    private double offsetX,offsetY;
    private boolean conectadoProtoboard = false;
    public boolean pasoCorrienteLed = false;
    private ControladorProtoboard protoboard;

    // Inicialización del controlador
    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;
        VariablesGlobales.elementoLed = this;

        EliminarElementos.habilitarEliminacion(ledPane);

        // Inicialización de las patitas (puedes personalizar los estilos o comportamientos)
        patita1.setFill(javafx.scene.paint.Color.DODGERBLUE);
        patita2.setFill(javafx.scene.paint.Color.DODGERBLUE);

        ledPane.setOnMousePressed(this::onMousePressed);
        ledPane.setOnMouseDragged(this::onMouseDragged);

        // Manejo de la interacción de la patita1
        patita1.setOnMouseClicked(event -> {
            verificarPosicion(); //nos dice en que botones de la protoboard esta el led
            if (conectadoProtoboard) {
                desconectarDeProtoboard("patita1");
            } else {
                conectarAProtoboard("patita1");
            }
        });

        // Manejo de la interacción de la patita2
        patita2.setOnMouseClicked(event -> {
            verificarPosicion(); //nos dice en que botones de la protoboard esta el led
            if (conectadoProtoboard) {
                desconectarDeProtoboard("patita2");
            } else {
                conectarAProtoboard("patita2");
            }
        });


    }

    public void cambiarColor(String color) {
        luzCirculo.setFill(Color.web(color)); // Cambia el color del círculo
        luzRectangulo.setFill(Color.web(color));
    }

    // Método para capturar el punto inicial de arrastre
    private void onMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - ledPane.getLayoutX();
        offsetY = event.getSceneY() - ledPane.getLayoutY();
    }

    // Método para mover el LED mientras se arrastra
    private void onMouseDragged(MouseEvent event) {
        ledPane.setLayoutX(event.getSceneX() - offsetX);
        ledPane.setLayoutY(event.getSceneY() - offsetY);
    }

    // Método para conectar una patita a la protoboard
    private void conectarAProtoboard(String patita) {
        conectadoProtoboard = true;
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Conexión exitosa");
        alert.setHeaderText(null);
        alert.setContentText("El LED ha sido conectado a la protoboard desde " + patita);
        alert.showAndWait();

        // Cambia el color para indicar conexión
        if (patita.equals("patita1")) {
            patita1.setFill(Color.GREEN);
        } else if (patita.equals("patita2")) {
            patita2.setFill(Color.GREEN);
        }
        verificarCorrienteLED(protoboard);
    }

    // Método para desconectar una patita de la protoboard
    private void desconectarDeProtoboard(String patita) {
        conectadoProtoboard = false;
        pasoCorrienteLed = false;
        VariablesGlobales.corrienteLed = pasoCorrienteLed;
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Desconexión");
        alert.setHeaderText(null);
        alert.setContentText("El LED ha sido desconectado de la protoboard desde " + patita);
        alert.showAndWait();

        // Cambia el color para indicar desconexión
        if (patita.equals("patita1")) {
            patita1.setFill(Color.DODGERBLUE);
        } else if (patita.equals("patita2")) {
            patita2.setFill(Color.DODGERBLUE);
        }
    }

    // Método para verificar sobre qué botones ha quedado el led
    public void verificarPosicion() {
        if (protoboard == null) {
            System.out.println("Protoboard no está asignado.");
            return;
        }

        verificarEnGridPane(protoboard.getBusSuperior());
        verificarEnGridPane(protoboard.getPistaSuperior());
        verificarEnGridPane(protoboard.getBusInferior());
        verificarEnGridPane(protoboard.getPistaInferior());
    }

    // Método auxiliar para iterar sobre los botones de un GridPane
    private void verificarEnGridPane(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                // Obtener los límites en escena de ambos elementos para una comparación precisa
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                Bounds patita1Bounds = patita1.localToScene(patita1.getBoundsInLocal());
                Bounds patita2Bounds = patita2.localToScene(patita2.getBoundsInLocal());

                // Verifica si los límites del switch intersectan con los límites del botón
                if (patita1Bounds.intersects(buttonBounds) && patita2Bounds.intersects(buttonBounds)) {
                    System.out.println("Ambas patitas estan en protoboard, sobre: " + button.getId());
                    System.out.println("----------------------------------");
                }else if (patita1Bounds.intersects(buttonBounds)) {
                    System.out.println("Patita 1 esta en protoboard, sobre: " + button.getId());
                    System.out.println("----------------------------------");
                }else if (patita2Bounds.intersects(buttonBounds)) {
                    System.out.println("Patita 2 esta en protoboard, sobre: " + button.getId());
                    System.out.println("----------------------------------");
                }
            }
        }
    }

    // Método para verificar si el LED está correctamente colocado y recibiendo corriente
    public void verificarCorrienteLED(ControladorProtoboard protoboard) {
        if (protoboard == null) {
            System.out.println("Protoboard no está asignado.");
            return;
        }

        // Obtener la posición de las patitas del LED
        Bounds patita1Bounds = patita1.localToScene(patita1.getBoundsInLocal());
        Bounds patita2Bounds = patita2.localToScene(patita2.getBoundsInLocal());

        // Inicializar las variables que controlan si hay corriente positiva y negativa
        boolean corrientePositiva = false;
        boolean corrienteNegativa = false;

        // Verificar la corriente en cada parte de la protoboard
        corrientePositiva = verificarCorrienteEnGridPane(protoboard.getBusSuperior(), patita2Bounds, "green") || corrientePositiva;
        corrienteNegativa = verificarCorrienteEnGridPane(protoboard.getBusSuperior(), patita1Bounds, "red") || corrienteNegativa;

        corrientePositiva = verificarCorrienteEnGridPane(protoboard.getPistaSuperior(), patita2Bounds, "green") || corrientePositiva;
        corrienteNegativa = verificarCorrienteEnGridPane(protoboard.getPistaSuperior(), patita1Bounds, "red") || corrienteNegativa;

        corrientePositiva = verificarCorrienteEnGridPane(protoboard.getBusInferior(), patita2Bounds, "green") || corrientePositiva;
        corrienteNegativa = verificarCorrienteEnGridPane(protoboard.getBusInferior(), patita1Bounds, "red") || corrienteNegativa;

        corrientePositiva = verificarCorrienteEnGridPane(protoboard.getPistaInferior(), patita2Bounds, "green") || corrientePositiva;
        corrienteNegativa = verificarCorrienteEnGridPane(protoboard.getPistaInferior(), patita1Bounds, "red") || corrienteNegativa;

        // Verificar si ambas patitas reciben corriente adecuada
        if (corrientePositiva && corrienteNegativa) {
            System.out.println("El LED está recibiendo corriente correctamente.");
            pasoCorrienteLed = true;
            VariablesGlobales.corrienteLed = pasoCorrienteLed;
        } else {
            mostrarError("El LED no está recibiendo corriente correctamente.");
        }
    }

    // Método auxiliar para verificar si un botón en un GridPane tiene la corriente deseada
    private boolean verificarCorrienteEnGridPane(GridPane gridPane, Bounds patitaBounds, String colorCorriente) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                // Obtener los límites del botón
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                // Verificar si la patita está sobre este botón
                if (patitaBounds.intersects(buttonBounds)) {
                    if (button.getStyle().contains(colorCorriente)) {
                        return true; // Se ha encontrado la corriente correcta
                    }
                }
            }
        }
        return false; // No se ha encontrado la corriente deseada en este GridPane
    }


    // Método para mostrar un mensaje de error
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}