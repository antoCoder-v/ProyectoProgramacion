package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
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
    private Cables cableManager;

    boolean corrientePositiva = false;
    boolean corrienteNegativa = false;

    // Inicialización del controlador
    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;
        cableManager = VariablesGlobales.cables;
        VariablesGlobales.elementoLed = this;

        //Elimina Led con clic derecho
        EliminarElementos.habilitarEliminacion(ledPane);

        // Inicialización de las patitas (puedes personalizar los estilos o comportamientos)
        patita1.setFill(javafx.scene.paint.Color.DODGERBLUE);
        patita2.setFill(javafx.scene.paint.Color.DODGERBLUE);

        ledPane.setOnMousePressed(this::onMousePressed);

        //verifica si led recibe corriente positiva y negativa, al moverlo
        ledPane.setOnMouseDragged(this::onMouseDragged);
        ledPane.setOnMouseReleased(this::onMouseReleased);

        //Verifica cambios en tiempo real
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                cableManager.actualizarCorrienteTodos();

                //Reconoce si led recibe carga
                verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita1);
                verificarPatitasEnGridPane(protoboard.getPistaInferior(), patita2);

                verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita1);
                verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita2);

                //Verifica si se prende el led
                if(corrientePositiva && corrienteNegativa){
                    cambiarColor("yellow");
                }else if(!corrientePositiva || !corrienteNegativa){
                    cambiarColor("red");
                }
            }
        };
        timer.start();


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
        
    }

    /* 
    // Método para verificar si el LED está correctamente colocado y recibiendo corriente
    public void verificarCorrienteLED(ControladorProtoboard protoboard) {
        if (protoboard == null) {
            System.out.println("Protoboard no está asignado.");
            return;
        }
        boolean patita1ConectadaAPositiva = false;
        boolean patita2ConectadaAPositiva = false;

        // Verificar si patita está en algún botón de la protoboard
        patita1ConectadaAPositiva = verificarPatitasEnGridPane(protoboard.getBusSuperior(), patita1, "Patita 1")
                || verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita1, "Patita 1")
                || verificarPatitasEnGridPane(protoboard.getBusInferior(), patita1, "Patita 1")
                || verificarPatitasEnGridPane(protoboard.getPistaInferior(), patita1, "Patita 1");

        patita2ConectadaAPositiva = verificarPatitasEnGridPane(protoboard.getBusSuperior(), patita2, "Patita 2")
                || verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita2, "Patita 2")
                || verificarPatitasEnGridPane(protoboard.getBusInferior(), patita2, "Patita 2")
                || verificarPatitasEnGridPane(protoboard.getPistaInferior(), patita2, "Patita 2");

        // Procedimientos basados en el estado de las patitas
        if (patita1ConectadaAPositiva || patita2ConectadaAPositiva == false) {
            AudioClip explosionSound = new AudioClip(getClass().getResource("/Audio/explosion.wav").toExternalForm());
            explosionSound.play();
            mostrarVentanaMensaje("EL LED SE HA SOBRECALENTADO HASTA EXPLOTAR", "ERROR DE EXPLOSION");
            System.out.println("El LED ha explotado porque una patita está conectada a corriente positiva.");
        }
    }*/

    // funcion que nos indica si las patitas del led coinciden con los botones de la protoboard y si estan recibiendo corriente
    private Boolean verificarPatitasEnGridPane(GridPane gridPane, Circle patitas) {
        // Obtener la posición de las patitas del LED
        Bounds patitaBounds = patitas.localToScene(patitas.getBoundsInLocal());
        //String cargaRetornada = "neutra"; BORRAR? 

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
                if (patitaBounds.intersects(buttonBounds) ) { 
                    if(carga.equals("positiva")){ //si la carga es positiva
                        patitas.setFill(Color.GREEN);
                        corrientePositiva = true;
                        return true;
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