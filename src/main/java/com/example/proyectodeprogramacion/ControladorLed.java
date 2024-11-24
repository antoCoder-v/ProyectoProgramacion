package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.MenuItem;

public class ControladorLed implements ControladorElemento {

    @FXML
    private Circle patita1, patita2, luzCirculo; // Vinculado con fx:id="patita1"

    @FXML
    private Rectangle luzRectangulo;

    @FXML
    private AnchorPane ledPane; // Contenedor del LED

    private double offsetX, offsetY;
    public boolean pasoCorrienteLed = false;
    private ControladorProtoboard protoboard;

    //private boolean ledConectado = false; // maneja la coneccion de led con protoboard
    //private boolean encimaDeProtoboard = false; // maneja si el led esta encima de la protoboard (requisito para
                                                // concectar)
    private boolean ledExploto = false;
    private String color = "rojo";
    //private ContextMenu colorMenu;

    // Metodos para modificar ledConectado (IMPORTANTE que las funciones esten en la
    // interfaz ControladorElemento)
    @Override
    public void setColor(String color) {
        this.color = color;
    }

    public void setProtoboard(ControladorProtoboard protoboard) {
        this.protoboard = protoboard;
    }

    // Inicialización del controlador
    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;
        // VariablesGlobales.elementoLed = this;

        // Manejamos eliminacion del elemento y la conceccion
        EliminarElementos.habilitarEliminacion(ledPane, this);
        // Inicializar el menú de colores

        // Inicialización de las patitas (puedes personalizar los estilos o
        // comportamientos)
        patita1.setFill(javafx.scene.paint.Color.DODGERBLUE);
        patita2.setFill(javafx.scene.paint.Color.DODGERBLUE);

        // Manejamos el moviemiento de led, solo se mueve si led esta conectado con
        // protoboard
        ledPane.setOnMousePressed(this::onMousePressed);
        ledPane.setOnMouseDragged(this::onMouseDragged);

        // Verifica si LED recibe corriente sin tener que moverlo
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Variable para habilitar opciones
                estaEncimaDeProtoboard();
                verificarCorrienteLED();
                
            }
        };
        timer.start();
        System.out.println(color);

    }

    // Método para configurar el menú de colores
    public void actualizarColor() {
        ContextMenu colorMenu = new ContextMenu();

        MenuItem morado = new MenuItem("Morado");
        morado.setOnAction(e -> cambiarColor("PURPLE"));

        MenuItem celeste = new MenuItem("Celeste");
        celeste.setOnAction(e -> cambiarColor("LIGHTBLUE"));

        MenuItem rosado = new MenuItem("Rosado");
        rosado.setOnAction(e -> cambiarColor("PINK"));

        MenuItem naranjo = new MenuItem("Naranjo");
        naranjo.setOnAction(e -> cambiarColor("ORANGE"));

        colorMenu.getItems().addAll(morado, celeste, rosado, naranjo);
        colorMenu.show(ledPane, ledPane.getLayoutX(), ledPane.getLayoutY());
        //return colorMenu;
    }


    // Método para cambiar el color del LED
    public void cambiarColor(String color) {
        luzCirculo.setFill(Color.web(color)); // Cambia el color del círculo
        luzRectangulo.setFill(Color.web(color));
    }

    // Método para capturar el punto inicial de arrastre (SOLO se mueve si led no
    // esta conectado)
    private void onMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - ledPane.getLayoutX();
        offsetY = event.getSceneY() - ledPane.getLayoutY();
    }

    // Método para mover el LED mientras se arrastra (SOLO se mueve si led no esta
    // conectado)
    private void onMouseDragged(MouseEvent event) {
        ledPane.setLayoutX(event.getSceneX() - offsetX);
        ledPane.setLayoutY(event.getSceneY() - offsetY);
    }

    // Metodo para verificar si led esta sobre los botones de protoboard
    public void estaEncimaDeProtoboard() {
        // Verificamos que haya una protoboard
        if (protoboard == null) {
            // System.out.println("Protoboard no está asignado.");
            return;
        }

        // variables para comprobacion
        boolean patita1Encima = false;
        boolean patita2Encima = false;

        // Verificar si las patitas estás en algún botón de la protoboard
        patita1Encima = verificarPatitasEnGridPane(protoboard.getBusSuperior(), patita1, "Patita 1")
                || verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita1, "Patita 1")
                || verificarPatitasEnGridPane(protoboard.getBusInferior(), patita1, "Patita 1")
                || verificarPatitasEnGridPane(protoboard.getPistaInferior(), patita1, "Patita 1");

        patita2Encima = verificarPatitasEnGridPane(protoboard.getBusSuperior(), patita2, "Patita 2")
                || verificarPatitasEnGridPane(protoboard.getPistaSuperior(), patita2, "Patita 2")
                || verificarPatitasEnGridPane(protoboard.getBusInferior(), patita2, "Patita 2")
                || verificarPatitasEnGridPane(protoboard.getPistaInferior(), patita2, "Patita 2");
    }

    // Método para verificar si el LED está correctamente colocado y recibiendo
    // corriente
    public void verificarCorrienteLED() {
        // Verficamos si recibe la corriente correcta las patitas
        if (patita1.getFill().equals(Color.RED) && patita2.getFill().equals(Color.GREEN)) {
            cambiarColor("yellow");
        } else if (patita2.getFill().equals(Color.RED) && patita1.getFill().equals(Color.GREEN) && !ledExploto) {
            cambiarColor("black");
            AudioClip explosionSound = new AudioClip(getClass().getResource("/Audio/explosion.wav").toExternalForm());
            explosionSound.play();
            //mostrarVentanaMensaje("EL LED SE HA QUEMADO", "ERROR DE EXPLOSION");
            ledExploto = true;
        } else if(!ledExploto){
            cambiarColor("red");
        }
    }

    // funcion que nos indica si las patitas del led coinciden con los botones de la
    // protoboard y si estan recibiendo corriente
    private Boolean verificarPatitasEnGridPane(GridPane gridPane, Circle patitas, String patita) {
        // Obtener la posición de las patitas del LED
        Bounds patitaBounds = patitas.localToScene(patitas.getBoundsInLocal());

        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                // variables para guardarpartes de la id
                String buttonId = button.getId();
                String carga = ""; // obtenemos carga del boton
                String[] parts = buttonId.split("-"); // se separa en partes la id
                carga = parts[4].trim(); // obtenemos solo carga del boton

                // Obtener los límites del botón
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                // Verificar si la patita está sobre algun botón
                if (patitaBounds.intersects(buttonBounds)) {
                    if (carga.equals("positiva")) { // si la carga es positiva
                        patitas.setFill(Color.GREEN);
                    } else if (carga.equals("negativa")) { // si la carga es negativa
                        patitas.setFill(Color.RED);
                    } else if (carga.equals("0")) {
                        patitas.setFill(Color.BLUE);
                    }
                    return true;
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