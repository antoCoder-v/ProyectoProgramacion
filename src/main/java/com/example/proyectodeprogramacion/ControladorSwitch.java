package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.geometry.Bounds;

public class ControladorSwitch {

    @FXML
    private AnchorPane switchPane; // Contiene todo el contenido del switch, incluyendo el Rectangle y los Circles

    @FXML
    private Pane mainPane; // Asegúrate de que el fx:id de tu Pane en el archivo FXML coincida

    @FXML
    private Rectangle rectangle; // Rectángulo dentro del switchPane

    @FXML
    private Circle circleCenter,circle1,circle2,circle3,circle4; // Círculo dentro del switchPane (derecha, derecha, izquierda, izquierda)

    @FXML
    private Button encender;

    private double offsetX,offsetY;
    private ControladorProtoboard protoboard;
    private boolean pasoCorrienteSwitch = false;
    private String corriente = "0";
    private String tipoGridPane = "";
    private Button botonDerecho;


    public ControladorSwitch() {
    }

    @FXML
    public void initialize() {
        EliminarElementos.habilitarEliminacion(switchPane);
        protoboard = VariablesGlobales.controladorProtoboard;
        //VariablesGlobales.controladorSwitch = this;

        // Configura los eventos de arrastre para el switchPane
        switchPane.setOnMousePressed(this::handleMousePressed);

        //sl momento de dejar el switch verifica si recibe corriente
        switchPane.setOnMouseDragged(this::handleMouseDragged);

        // Configura el evento al presionar el botón "Encender", al presionar se activa el paso de corriente
        encender.setOnAction(event -> {
            manejamosPasoCorriente();
        });
    }

    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - switchPane.getLayoutX();
        offsetY = event.getSceneY() - switchPane.getLayoutY();
    }

    private void handleMouseDragged(MouseEvent event) {
        switchPane.setLayoutX(event.getSceneX() - offsetX);
        switchPane.setLayoutY(event.getSceneY() - offsetY);

         //verifica que el lado izquierdo del switch este conectado a los botones y recibiendo corriente
         verificarPosicion(protoboard);
    }

    // Método para verificar si el switch esta recibiendo corriente en algunos de sus botones izquierdos
    public void verificarPosicion(ControladorProtoboard protoboard) {
        pasoCorrienteSwitch = false; 

        if (protoboard == null) {
            System.out.println("Protoboard no está asignado.");
            return;
        }

        //El lado izquierdo recibe la corriente
        //circle3
        verificarEnGridPane(protoboard.getBusSuperior(), circle3, "busSuperior");
        verificarEnGridPane(protoboard.getPistaSuperior(), circle3, "pistaSuperior");
        verificarEnGridPane(protoboard.getBusInferior(), circle3, "busInferior");
        verificarEnGridPane(protoboard.getPistaInferior(), circle3, "pistaInferior");

        //circle4
        verificarEnGridPane(protoboard.getBusSuperior(), circle4, "busSuperior");
        verificarEnGridPane(protoboard.getPistaSuperior(), circle4, "pistaSuperior");
        verificarEnGridPane(protoboard.getBusInferior(), circle4, "busInferior");
        verificarEnGridPane(protoboard.getPistaInferior(), circle4, "pistaInferior");

    }

    // Metodo que verifica si el circulo esta en protoboard y si recibe corriente
    private void verificarEnGridPane(GridPane gridPane, Circle circle, String tipo) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                //variables para guardarpartes de la id
                String buttonId = button.getId();
                String carga = "";                          //obtenemos carga del boton
                String[] parts = buttonId.split("-"); //se separa en partes la id
                carga = parts[4].trim();                    //obtenemos solo carga del boton

                // Obtener los límites en escena de ambos elementos para una comparación precisa
                Bounds circleBounds = circle.localToScene(circle.getBoundsInLocal());
                Bounds circle1Bounds = circle1.localToScene(circle1.getBoundsInLocal());
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                // Verifica que algun circulo intersecte con los límites del botón
                if (circleBounds.intersects(buttonBounds)) {
                    //mostrarVentanaMensaje("Circulo conectado", "Conexiòn");
                    if(carga.equals("positiva")){ //si la carga es positiva
                        circle.setFill(Color.GREEN);
                        corriente = "positiva";
                        tipoGridPane = tipo;
                    }else if(carga.equals("negativa")){ //si la carga es negativa
                        circle.setFill(Color.RED);
                        corriente = "negativa";
                        tipoGridPane = tipo;
                    }else if(carga.equals("0")){
                        circle.setFill(Color.BLACK);
                        corriente = "0";
                    }
                }

                //obtenemos algun boton de la parte derecha del switch
                if (circle1Bounds.intersects(buttonBounds)) {
                    botonDerecho = button;
                }
            }
        }
    }

    //manejamos el traspaso de corriente al lado derecho de swicth
    private void manejamosPasoCorriente(){

        int col = Integer.parseInt(retornaUnValorDeID(botonDerecho, 3));

        //pasamos la corriente al otro lado del switch, solo el switch funciona en las pistas
        GridPane gridPane = tipoGridPane.contains("pistaSuperior") ? protoboard.getPistaSuperior() : protoboard.getPistaInferior();
        for (Node node : gridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Button button = (Button) node;

            if (nodeCol != null && nodeCol.equals(col)) {
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

        //Funcion que cambia una parte del id segun el index que se le pase
        public void cambiarParteIdBoton(Button button, int index, String nuevoValor) {
            try {
                String buttonId = button.getId();
                if(buttonId.equals("botonCargaNegativa") || buttonId.equals("botonCargaPositiva")){
                    return; //sale de la funcion
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
    
        private String retornaUnValorDeID(Button button, int index){
            if(button.getId().equals("botonCargaNegativa")){
                return "negativa";
            }else if(button.getId().equals("botonCargaPositiva")){
                return "positiva";
            }
            String buttonId = button.getId();
            String[] parts = buttonId.split("-");
            return parts[index];
        }
    
}

  
