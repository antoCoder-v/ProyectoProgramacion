package com.example.proyectodeprogramacion;

import java.util.List;

import com.example.proyectodeprogramacion.Cables.CableInfo;

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
    private Cables cables;
    private int columna1, columna2; // Columnas de los botones a los que se les pasa corriente
    private String corriente = "0", tipoGridPane = "", gridPaneVecino; // Variable para guardar la corriente y el tipo de gridpane

    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;
        cables = VariablesGlobales.cables;

        // Configura los eventos cuando se presiona el Switch
        switchPane.setOnMousePressed(this::handleMousePressed);

        // Configura los eventos cuando se arrastra el Switch y si recibe corriente
        switchPane.setOnMouseDragged(this::handleMouseDragged);

        // Configura el evento al presionar el botón "Encender", al presionar se activa
        // el paso de corriente
        encender.setOnAction(event -> {
            pasoCorrienteSwitch = !pasoCorrienteSwitch; // Cambia el paso de corriente
            manejoDeCorriente();
            //actualizarCorrienteTodos();
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

        GridPane gridPane = gridPaneVecino.contains("pistaSuperior") ? protoboard.getPistaSuperior(): protoboard.getPistaInferior();
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
        GridPane gridPane = tipoGridPane.contains("pistaSuperior") ? protoboard.getPistaSuperior(): protoboard.getPistaInferior();
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

    //Funcion para actualizar la corriente de todos los botones
    public void actualizarCorrienteTodos() {
        // Recorrer todos los GridPane
        GridPane[] gridPanes = {protoboard.getBusInferior(), protoboard.getBusSuperior(), protoboard.getPistaInferior(), protoboard.getPistaSuperior()};
        
        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                Button button = (Button) node;
                String conexion = retornaUnValorDeID(button, 5);
                String estilo = "";

                //identificamos cable conectado
                if (conexion.equals("conectado")) {
                    //reconocemos carga
                    String carga = retornaUnValorDeID(button, 4);
                    System.out.println("carga"+ carga);
                    //cambiarParteIdBoton(button, 4, carga);
                    if(carga.contains("positiva")){
                        estilo = "-fx-background-color: green; -fx-background-radius: 30;";
                    }else if(carga.contains("negativa")){
                        estilo = "-fx-background-color: red; -fx-background-radius: 30;";
                    }else{
                        estilo = "-fx-background-radius: 30;";
                    }
                    

                    //buscamo su otro extremo
                    Button otroExtremo = encontrarOtroExtremo(button);
                    manejoCorriente(otroExtremo, carga, estilo);

                    carga = "0";
                    estilo = "";
                }
                
            }
        }
    }

    private void manejoCorriente(Button boton, String carga, String estilo) {
        //verificamos si es un boton de bateria
        if (boton.getId().equals("botonCargaNegativa") || boton.getId().equals("botonCargaPositiva")) {
            return; // Sale de la función
        }

        // String buttonId = boton.getId();
        String tipo = retornaUnValorDeID(boton, 1);
        int row = Integer.parseInt(retornaUnValorDeID(boton, 2));
        int col = Integer.parseInt(retornaUnValorDeID(boton, 3));

        // Propagar el color solo en la fila o columna correspondiente
        if (tipo.contains("busSuperior") || tipo.contains("busInferior")) {
            GridPane gridPane = tipo.contains("busSuperior") ? protoboard.getBusInferior() : protoboard.getBusSuperior();
            for (Node node : gridPane.getChildren()) {
                Integer nodeRow = GridPane.getRowIndex(node);
                if (nodeRow != null && nodeRow.equals(row)) {
                    node.setStyle(estilo);
                    cambiarParteIdBoton((Button) node, 4, carga);
                }
            }
        } else {
            GridPane gridPane = tipo.contains("pistaSuperior") ? protoboard.getBusInferior() : protoboard.getBusSuperior();
            for (Node node : gridPane.getChildren()) {
                Integer nodeCol = GridPane.getColumnIndex(node);
                if (nodeCol != null && nodeCol.equals(col)) {
                    node.setStyle(estilo);
                    cambiarParteIdBoton((Button) node, 4, carga);
                }
            }
        }
    }

    // Método para retornar un valor de un ID
    private String retornaUnValorDeID(Button button, int index) {
        if (button.getId().equals("botonCargaNegativa")) {
            return "negativa";
        } else if (button.getId().equals("botonCargaPositiva")) {
            return "positiva";
        }
        String buttonId = button.getId();
        String[] parts = buttonId.split("-");
        return parts[index];
    }

    // Método para encontrar el otro extremo de un cable conectado a un botón
    private Button encontrarOtroExtremo(Button button) {
        List<Cables.CableInfo> cablesConectados = cables.getCablesConectados();
        for (CableInfo cableInfo : cablesConectados) {
            if (cableInfo.startButton == button) {
                return cableInfo.endButton;
            } else if (cableInfo.endButton == button) {
                return cableInfo.startButton;
            }
        }
        return null; // Retorna null si no se encuentra ningún cable conectado
    }
}
