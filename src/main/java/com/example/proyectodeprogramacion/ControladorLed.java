package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.animation.AnimationTimer;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

public class ControladorLed {

    @FXML
    private Circle patita1,patita2,luzCirculo;  // Vinculado con fx:id="patita1"

    @FXML
    private Rectangle luzRectangulo;

    @FXML
    private AnchorPane ledPane;    // Contenedor del LED

    private double offsetX,offsetY;
    public boolean pasoCorrienteLed = false;
    private ControladorProtoboard protoboard;

    boolean corrientePositiva = false;
    boolean corrienteNegativa = false;
    int corrienteNeutra = 0;

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

        //verifica si led recibe corriente positiva y negativa, al moverlo
        ledPane.setOnMouseDragged(this::onMouseDragged);
        ledPane.setOnMouseReleased(this::onMouseReleased);
    }

    // Método para cambiar el color del LED
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
    private void onMouseDragged(MouseEvent event){
        ledPane.setLayoutX(event.getSceneX() - offsetX);
        ledPane.setLayoutY(event.getSceneY() - offsetY);
    }

    // Método para verificar cuando el LED ha sido colocado
    private void onMouseReleased(MouseEvent event) {
        verificarCorrienteLED(protoboard);
    }

    // Método para verificar si el LED está correctamente colocado y recibiendo corriente
    public void verificarCorrienteLED(ControladorProtoboard protoboard) {
        if (protoboard == null) {
            System.out.println("Protoboard no está asignado.");
            return;
        }
        boolean patita1Conectada = false;
        boolean patita2Conectada = false;

        // Verificar si patita está en algún botón de la protoboard
        patita1Conectada = verificarPatitasEnGridPane(protoboard.getBusSuperior(), patita1, "Patita 1")
                || verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita1, "Patita 1")
                || verificarPatitasEnGridPane(protoboard.getBusInferior(), patita1, "Patita 1")
                || verificarPatitasEnGridPane(protoboard.getPistaInferior(), patita1, "Patita 1");

        patita2Conectada = verificarPatitasEnGridPane(protoboard.getBusSuperior(), patita2, "Patita 2")
                || verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita2, "Patita 2")
                || verificarPatitasEnGridPane(protoboard.getBusInferior(), patita2, "Patita 2")
                || verificarPatitasEnGridPane(protoboard.getPistaInferior(), patita2, "Patita 2");

        // Procedimientos basados en el estado de las patitas
        if (patita1Conectada && corrienteNeutra!=0 || !patita2Conectada && corrienteNeutra!=0) {
            AudioClip explosionSound = new AudioClip(getClass().getResource("/Audio/explosion.wav").toExternalForm());
            explosionSound.play();
            mostrarVentanaMensaje("EL LED SE HA SOBRECALENTADO HASTA EXPLOTAR", "ERROR DE EXPLOSION");
            cambiarColor("red");
        } else if (!patita1Conectada && corrienteNeutra!=0 && patita2Conectada) {
            cambiarColor("yellow");
        } else if (corrienteNeutra ==0) {
            cambiarColor("red");
        }
    }

    // funcion que nos indica si las patitas del led coinciden con los botones de la protoboard y si estan recibiendo corriente
    private Boolean verificarPatitasEnGridPane(GridPane gridPane, Circle patitas, String patita) {
        // Obtener la posición de las patitas del LED
        Bounds patitaBounds = patitas.localToScene(patitas.getBoundsInLocal());

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                //variables para guardarpartes de la id
                String buttonId = button.getId();
                String carga = "";                          //obtenemos carga del boton
                String[] parts = buttonId.split("-"); //se separa en partes la id
                carga = parts[4].trim();                    //obtenemos solo carga del boton

                // Obtener los límites del botón
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                // Verificar si la patita está sobre algun botón
                if (patitaBounds.intersects(buttonBounds)) {
                    if(carga.equals("positiva")){ //si la carga es positiva
                        patitas.setFill(Color.GREEN);
                        corrientePositiva = true;
                        corrienteNeutra=1;
                        return true;
                    }else if(carga.equals("negativa")){ //si la carga es negativa
                        patitas.setFill(Color.RED);
                        corrienteNegativa = true;
                        corrienteNeutra=1;
                    }else if(carga.equals("0")){
                        corrienteNegativa = false;
                        corrientePositiva = false;
                        corrienteNeutra = 0;
                        patitas.setFill(Color.BLUE);
                    }
                }
            }
        }
        return false;
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