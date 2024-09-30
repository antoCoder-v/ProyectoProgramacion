package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
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

        verificarCorrienteLED(protoboard);

        if(corrientePositiva && corrienteNegativa){
            cambiarColor("yellow");
        }else{
            cambiarColor("red");
        }
            
    }

    // Método para verificar si el LED está correctamente colocado y recibiendo corriente
    public void verificarCorrienteLED(ControladorProtoboard protoboard) {
        if (protoboard == null) {
            System.out.println("Protoboard no está asignado.");
            return;
        }

        // Verificar si patita esta en algun boton de la protoboard
        verificarPatitasEnGridPane(protoboard.getBusSuperior(), patita1, "Patita 1");
        verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita1, "Patita 1");
        verificarPatitasEnGridPane(protoboard.getBusInferior(), patita1, "Patita 1");
        verificarPatitasEnGridPane(protoboard.getPistaInferior(), patita1, "Patita 1");

        verificarPatitasEnGridPane(protoboard.getBusSuperior(), patita2, "Patita 2");
        verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita2, "Patita 2");
        verificarPatitasEnGridPane(protoboard.getBusInferior(), patita2, "Patita 2");
        verificarPatitasEnGridPane(protoboard.getPistaInferior(), patita2, "Patita 2");

    }

    // funcion que nos indica si las patitas del led coinciden con los botones de la protoboard y si estan recibiendo corriente
    private void verificarPatitasEnGridPane(GridPane gridPane, Circle patitas, String patita) {
        // Obtener la posición de las patitas del LED
        Bounds patitaBounds = patitas.localToScene(patita1.getBoundsInLocal());

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

                    }else if(carga.equals("negativa")){ //si la carga es negativa
                        patitas.setFill(Color.RED);
                        corrienteNegativa = true;
                    }else if(carga.equals("0")){
                        corrienteNegativa = false;
                        corrientePositiva = false;
                        patitas.setFill(Color.BLUE);
                    }
                }
            }
        }
    }


}