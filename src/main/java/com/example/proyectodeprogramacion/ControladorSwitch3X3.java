package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
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

    private boolean pasoCorrienteSwitch = false, hayCorriente;
    private ControladorProtoboard protoboard;
    private Cables cables;
    private String corriente = "", tipoGridPane = ""; // Variable para guardar la corriente y el tipo de gridpane
    private int colCircle1;

    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;
        cables = VariablesGlobales.cables;

        //Borramos switch con clic derecho
        EliminarElementos.habilitarEliminacion(switchPane);

        // Configura los eventos cuando se presiona el Switch
        switchPane.setOnMousePressed(this::handleMousePressed);

        // Configura los eventos cuando se arrastra el Switch y si recibe corriente
        switchPane.setOnMouseDragged(this::handleMouseDragged);

        // Configura el evento al presionar el botón "Encender", al presionar se activa el paso de corriente
        encender.setOnAction(event -> {
            pasoCorrienteSwitch = !pasoCorrienteSwitch; // Cambia el paso de corriente
            
            pasoDeCorriente("pistaInferior"); //linea que ACTUALIZA la corriente en switch cuando se prende y se apaga
            
            //linea que ACTUALIZA la corriente en switch cuando se prende y se apaga
            cables.actualizarCorrienteTodos();
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                verificarEnGridPaneCorriente();
                cables.actualizarCorrienteTodos();
            }
        };
        timer.start();

    }

    // Funcion para maneja cuando se presione el switch
    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - switchPane.getLayoutX();
        offsetY = event.getSceneY() - switchPane.getLayoutY();
    }

    // Funcion para manejar cuando se arrastra el switch y verifica si recibe corriente
    private void handleMouseDragged(MouseEvent event) {
        switchPane.setLayoutX(event.getSceneX() - offsetX);
        switchPane.setLayoutY(event.getSceneY() - offsetY);
    }

    // Metodo que verifica si el cirle esta recibiendo corriente
    private void verificarEnGridPaneCorriente() {
        GridPane[] gridPanes = {protoboard.getPistaSuperior(), protoboard.getPistaInferior()};

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                Button button = (Button) node;
                String tipo = retornaUnValorDeID(button, 1);
                Integer nodeCol = GridPane.getColumnIndex(node);
                
                // Posicion de los circulos
                Bounds circle1Bounds = circle1.localToScene(circle1.getBoundsInLocal());
                Bounds circle2Bounds = circle2.localToScene(circle2.getBoundsInLocal());

                // Posicion del boton
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                String carga = retornaUnValorDeID(button, 4);
                if (circle1Bounds.intersects(buttonBounds)) {
                    colCircle1 = nodeCol;
                    if (carga.equals("positiva")) { // si la carga es positiva
                        circle1.setFill(Color.GREEN);
                        circle2.setFill(Color.GREEN);
                        corriente = "positiva";
                        hayCorriente = true;
                    } else if (carga.equals("negativa")) { // si la carga es negativa
                        circle1.setFill(Color.RED);
                        circle2.setFill(Color.RED);
                        corriente = "negativa";
                        hayCorriente = true;
                    } else if(carga.equals("0")){
                        circle1.setFill(Color.BLACK);
                        circle2.setFill(Color.BLACK);
                        corriente = "0";
                        hayCorriente = false;
                    }
                    corrienteVecina(nodeCol, nodeCol + 2, tipo);
                    //corrienteVecina(circle2);
                }else if (circle1.getFill().equals(Color.BLACK)) { // ----> Verificamos si el circle2 recibe corriente
                    if (circle2Bounds.intersects(buttonBounds)) {
                        if (carga.equals("positiva")) { // si la carga es positiva
                            circle1.setFill(Color.GREEN);
                            circle2.setFill(Color.GREEN);
                            corriente = "positiva";
                            hayCorriente = true;
                        } else if (carga.equals("negativa")) { // si la carga es negativa
                            circle1.setFill(Color.RED);
                            circle2.setFill(Color.RED);
                            corriente = "negativa";
                            hayCorriente = true;
                        } else if (carga.equals("0")) {
                            circle1.setFill(Color.BLACK);
                            circle2.setFill(Color.BLACK);
                            corriente = "0";
                            hayCorriente = false;
                        }
                        corrienteVecina(nodeCol, nodeCol - 2, tipo);
                        //corrienteVecina(circle1);
                    }
                }
            }
        }
    }

    // Manejamos la corriente del circleVecino (circle1 o circle2)
    private void corrienteVecina(int col1, int col2, String tipo) {
        GridPane gridPane = tipo.contains("pistaSuperior") ? protoboard.getPistaSuperior(): protoboard.getPistaInferior();
        for (Node node : gridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            if(nodeCol.equals(col1) || nodeCol.equals(col2)){
                if(corriente.equals("positiva")){
                    node.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                    circle3.setFill(Color.GREEN);
                    circle4.setFill(Color.GREEN);
                    pasoDeCorriente("pistaInferior");
                }else if(corriente.equals("negativa")){
                    node.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                    circle3.setFill(Color.RED);
                    circle4.setFill(Color.RED);
                    pasoDeCorriente("pistaInferior");
                }else{
                    node.setStyle("-fx-background-radius: 30;");
                    circle3.setFill(Color.BLACK);
                    circle4.setFill(Color.BLACK);
                    pasoDeCorriente("pistaInferior");
                }
                cambiarParteIdBoton((Button)node, 4, corriente);
            }
        }
    }

    // manejamos el traspaso de corriente al presionar el boton del switch
    private void pasoDeCorriente(String tipo) {
        GridPane gridPane = tipo.contains("pistaSuperior") ? protoboard.getPistaSuperior(): protoboard.getPistaInferior();
        for (Node node : gridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            if(nodeCol.equals(colCircle1) || nodeCol.equals(colCircle1 + 2)){
                if(pasoCorrienteSwitch && !circle3.getFill().equals(Color.BLACK)){
                    if(corriente.equals("positiva") && circle3.getFill().equals(Color.GREEN)){
                        node.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                        cambiarParteIdBoton((Button)node, 4, corriente);
                    }else if(corriente.equals("negativa") && circle3.getFill().equals(Color.RED)){
                        node.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                        cambiarParteIdBoton((Button)node, 4, corriente);
                    }
                    
                }else{
                    node.setStyle("-fx-background-radius: 30;");
                    cambiarParteIdBoton((Button)node, 4, "0");

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
}