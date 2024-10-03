package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ControladorSwitch3X3 {

    @FXML
    private AnchorPane switchPane; // Contiene todo el contenido del switch, incluyendo el Rectangle y los Circles

    @FXML
    private Rectangle rectangle; // Rectángulo dentro del switchPane

    @FXML
    private Circle circleCenter, circle1, circle2, circle3, circle4; // (centro, derecha, derecha, izquierda, izquierda)

    @FXML
    private Button encender;

    private double offsetX, offsetY; // Variables para guardar la posición del mouse

    private boolean pasoCorrienteSwitch = false;
    private ControladorProtoboard protoboard;
    private int columna1, columna2; // Columnas de los botones a los que se les pasa corriente
    private String corriente = "0", tipoGridPane = "", gridPaneVecino; // Variable para guardar la corriente y el tipo
                                                                       // de gridpane

    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;

        // Configura los eventos cuando se presiona el Switch
        switchPane.setOnMousePressed(this::handleMousePressed);

        // Configura los eventos cuando se arrastra el Switch y si recibe corriente
        switchPane.setOnMouseDragged(this::handleMouseDragged);

        // Configura el evento al presionar el botón "Encender", al presionar se activa
        // el paso de corriente
        encender.setOnAction(event -> {
            pasoCorrienteSwitch = !pasoCorrienteSwitch; // Cambia el paso de corriente
            manejoDeCorriente();
        });
    }

    // Funcion para maneja cuando se presione el switch
    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - switchPane.getLayoutX();
        offsetY = event.getSceneY() - switchPane.getLayoutY();
    }

    // Funcion para manejar cuando se arrastra el switch y verifica si recibe
    // corriente
    private void handleMouseDragged(MouseEvent event) {
        switchPane.setLayoutX(event.getSceneX() - offsetX);
        switchPane.setLayoutY(event.getSceneY() - offsetY);

        // verifica que circle (circle1 o circle2) esta recibiendo corriente
        verificarPosicion(protoboard);
    }

    // Método para verificar si el switch esta recibiendo corriente en circle1 y
    // circle2
    public void verificarPosicion(ControladorProtoboard protoboard) {
        if (protoboard == null) {
            return;
        }

        // Lee las cargas de los botones de arriba (circle1 y circle2)
        verificarEnGridPaneCorriente(protoboard.getPistaSuperior(), "pistaSuperior");
        verificarEnGridPaneCorriente(protoboard.getPistaInferior(), "pistaInferior");

    }

    // Metodo que verifica si el cirle esta recibiendo corriente
    private void verificarEnGridPaneCorriente(GridPane gridPane, String tipo) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                // variables para guardarpartes de la id
                String buttonId = button.getId();
                String[] parts = buttonId.split("-"); // se separa en partes la id
                String carga = parts[4].trim(); // obtenemos solo carga del boton
                String columna = parts[3].trim(); // obtenemos solo columna del boton

                // Posicion del circulo
                Bounds circle1Bounds = circle1.localToScene(circle1.getBoundsInLocal());
                Bounds circle2Bounds = circle2.localToScene(circle2.getBoundsInLocal());
                Bounds circle3Bounds = circle3.localToScene(circle3.getBoundsInLocal());

                // Posicion del boton
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                // Verifica si circle1 recibe corriente
                if (circle1Bounds.intersects(buttonBounds)) {
                    if (carga.equals("positiva")) { // si la carga es positiva
                        circle1.setFill(Color.GREEN);
                        circle2.setFill(Color.GREEN);
                        corriente = "positiva";
                    } else if (carga.equals("negativa")) { // si la carga es negativa
                        circle1.setFill(Color.RED);
                        circle2.setFill(Color.RED);
                        corriente = "negativa";
                    } else if (carga.equals("0")) {
                        circle1.setFill(Color.BLACK);
                        circle2.setFill(Color.BLACK);
                        corriente = "0";
                    }
                    columna1 = Integer.parseInt(columna);
                    columna2 = Integer.parseInt(columna) + 2;
                    gridPaneVecino = tipo;
                } else if (circle1.getFill().equals(Color.BLACK)) { // ----> Verificamos si el circle2 recibe corriente
                    if (circle2Bounds.intersects(buttonBounds)) {
                        if (carga.equals("positiva")) { // si la carga es positiva
                            circle1.setFill(Color.GREEN);
                            circle2.setFill(Color.GREEN);
                            corriente = "positiva";
                        } else if (carga.equals("negativa")) { // si la carga es negativa
                            circle1.setFill(Color.RED);
                            circle2.setFill(Color.RED);
                            corriente = "negativa";
                        } else if (carga.equals("0")) {
                            circle1.setFill(Color.BLACK);
                            circle2.setFill(Color.BLACK);
                            corriente = "0";
                        }
                        columna1 = Integer.parseInt(columna);
                        columna2 = Integer.parseInt(columna) - 2;
                        gridPaneVecino = tipo;
                    }
                } else if (circle3Bounds.intersects(buttonBounds)) {
                    tipoGridPane = tipo; // Indicamos en que gridpane pasamos corriente
                }

            }
        }

        // manejamos la corriente del circleVecino
        corrienteVecina();
    }

    // Manejamos la corriente del circleVecino (circle1 o circle2)
    private void corrienteVecina() {
        if (gridPaneVecino == null) {
            return;
        }

        GridPane gridPane = gridPaneVecino.contains("pistaSuperior") ? protoboard.getPistaSuperior()
                : protoboard.getPistaInferior();
        for (Node node : gridPane.getChildren()) {
            Button button = (Button) node;
            Integer nodeCol = GridPane.getColumnIndex(node);
            if (nodeCol.equals(columna2)) {
                if (corriente.equals("positiva")) {
                    node.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                    cambiarParteIdBoton(button, 4, corriente);
                } else if (corriente.equals("negativa")) {
                    node.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                    cambiarParteIdBoton(button, 4, corriente);
                }
            }

        }
    }

    // manejamos el traspaso de corriente al lado derecho de swicth
    private void manejoDeCorriente() {
        GridPane gridPane = tipoGridPane.contains("pistaSuperior") ? protoboard.getPistaSuperior()
                : protoboard.getPistaInferior();
        for (Node node : gridPane.getChildren()) {
            Button button = (Button) node;
            Integer nodeCol = GridPane.getColumnIndex(node);
            if (nodeCol.equals(columna1) || nodeCol.equals(columna2)) {
                if (pasoCorrienteSwitch) {
                    if (corriente.equals("positiva")) {
                        node.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                    } else if (corriente.equals("negativa")) {
                        node.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                    }
                    cambiarParteIdBoton(button, 4, corriente);
                } else {
                    button.setStyle("-fx-background-radius: 30;");
                    cambiarParteIdBoton(button, 4, "0");
                }
            }

        }

    }

    // Funcion que cambia una parte del id segun el index que se le pase
    public void cambiarParteIdBoton(Button button, int index, String nuevoValor) {
        try {
            String buttonId = button.getId();
            if (buttonId.equals("botonCargaNegativa") || buttonId.equals("botonCargaPositiva")) {
                return; // sale de la funcion
            }
            // Dividir el ID del botón en partes
            String[] parts = buttonId.split("-");

            // Verificar que el índice es válido
            if (index < 0 || index >= parts.length) {
                throw new IllegalArgumentException("Índice fuera de rango");
            }

            // Modificar la parte deseada
            parts[index] = nuevoValor;

            // Unir las partes de nuevo en un solo ID y cambiarlo
            String nuevoId = String.join("-", parts);
            button.setId(nuevoId);
        } catch (Exception e) {
            System.err.println("Error en cambiarParteIdBoton: " + e.getMessage());
            e.printStackTrace();
            return; // Retornar el ID original en caso de error
        }
    }
}
