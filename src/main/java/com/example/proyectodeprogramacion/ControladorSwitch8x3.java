package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControladorSwitch8x3 implements ControladorElemento{

    @FXML
    private Pane paneSwitch8x3;
    @FXML
    private Button bot1, bot2, bot3, bot4, bot5, bot6, bot7, bot8;
    @FXML
    private Rectangle patSup1, patSup2, patSup3, patSup4, patSup5, patSup6, patSup7, patSup8;
    @FXML
    private Rectangle patInf1, patInf2, patInf3, patInf4, patInf5, patInf6, patInf7, patInf8;

    private ControladorProtoboard protoboard;
    private Cables cables;
    private boolean pasoCorriente1 = false, pasoCorriente2 = false, pasoCorriente3 = false, pasoCorriente4 = false,
            pasoCorriente5 = false, pasoCorriente6 = false, pasoCorriente7 = false, pasoCorriente8 = false;

    // Variables para manejar el arrastre del switch
    protected double offsetX, offsetY;
    private String color = "";


    @FXML
    public void initialize() {
        // Inicializamos las variables
        protoboard = VariablesGlobales.controladorProtoboard;
        cables = VariablesGlobales.cables;

        //habilitamos la eliminación del switch
        EliminarElementos.habilitarEliminacion(paneSwitch8x3, this);

        // Configuramos los eventos para el arrastre del switch
        paneSwitch8x3.setOnMousePressed(this::onMousePressed);
        paneSwitch8x3.setOnMouseDragged(this::onMouseDragged);

        // Majeno de paso de corriente en las patitas
        bot1.setOnAction(event -> {
            pasoCorriente1 = !pasoCorriente1;
            pasoCorriente(patSup1, patInf1, pasoCorriente1);
        });

        bot2.setOnAction(event -> {
            pasoCorriente2 = !pasoCorriente2;
            pasoCorriente(patSup2, patInf2, pasoCorriente2);
        });

        bot3.setOnAction(event -> {
            pasoCorriente3 = !pasoCorriente3;
            pasoCorriente(patSup3, patInf3, pasoCorriente3);
        });

        bot4.setOnAction(event -> {
            pasoCorriente4 = !pasoCorriente4;
            pasoCorriente(patSup4, patInf4, pasoCorriente4);
        });

        bot5.setOnAction(event -> {
            pasoCorriente5 = !pasoCorriente5;
            pasoCorriente(patSup5, patInf5, pasoCorriente5);
        });

        bot6.setOnAction(event -> {
            pasoCorriente6 = !pasoCorriente6;
            pasoCorriente(patSup6, patInf6, pasoCorriente6);
        });

        bot7.setOnAction(event -> {
            pasoCorriente7 = !pasoCorriente7;
            pasoCorriente(patSup7, patInf7, pasoCorriente7);
        });

        bot8.setOnAction(event -> {
            pasoCorriente8 = !pasoCorriente8;
            pasoCorriente(patSup8, patInf8, pasoCorriente8);
        });

        // Temporizador para verificar continuamente el estado de la corriente
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                cables.actualizarCorrienteTodos();

                //Vetificamos si switch recibe algun tipo de corriente 
                verificarCorrienteEnPatitas();
            }
        };
        timer.start();
    }
    
    @Override
    public void setColor(String color) {
        this.color = color;
    }

    //Verificar corriente de patitas
    private void verificarCorrienteEnPatitas(){
        //Reunimos las patitas que verifican la corriente
        Rectangle[] patitas = {patSup1, patSup2, patSup3, patSup4, patSup5, patSup6, patSup7, patSup8};

        GridPane[] gridPanes = {protoboard.getBusSuperior(), protoboard.getPistaSuperior(),protoboard.getPistaInferior() };

        //Verifcamos en que patita se encuentra la corriente
        
        for (Rectangle patita : patitas) {

            for (GridPane gridPane : gridPanes) {
                for (Node node : gridPane.getChildren()) {
                    Bounds patitaBounds = patita.localToScene(patita.getBoundsInLocal());
                        Button button = (Button) node;
                        Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                        if (patitaBounds.intersects(buttonBounds)) {
                            if (button.getId().contains("positiva")) {
                                patita.setFill(Color.GREEN);
                            } else if (button.getId().contains("negativa")) {
                                patita.setFill(Color.RED);
                            } else {
                                patita.setFill(Color.BLACK);
                            }
                        }
                    
                }
            }
        }
    }


    private void pasoCorriente(Rectangle patSup, Rectangle patInf, boolean puede){
        Bounds patitaBounds = patInf.localToScene(patInf.getBoundsInLocal());

        GridPane[] gridPanes = {protoboard.getBusSuperior(), protoboard.getPistaSuperior(),protoboard.getPistaInferior() };

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                Button button = (Button) node;
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                Integer fila = GridPane.getRowIndex(node);
                Integer columna = GridPane.getColumnIndex(node);
                if (patitaBounds.intersects(buttonBounds)){
                    daElPaso(gridPane, patSup, fila, columna, puede);
                    return;
                }
            }
        }
    }

    private void daElPaso(GridPane gridPane, Rectangle patSup, Integer fila, Integer columna, boolean puede){
        for (Node node : gridPane.getChildren()) {
            int nodeCol = GridPane.getColumnIndex(node);
            Integer nodRow = GridPane.getRowIndex(node);
            Button button = (Button) node;
            if(gridPane.equals(protoboard.getPistaInferior()) || gridPane.equals(protoboard.getPistaSuperior())){
                if(nodeCol == columna){
                    if(puede){
                        if(patSup.getFill().equals(Color.GREEN)){
                            button.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                            cambiarParteIdBoton(button, 4, "positiva");
                        }else if(patSup.getFill().equals(Color.RED)){
                            button.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                            cambiarParteIdBoton(button, 4, "negativa");
                        }
                    }else{
                        button.setStyle("-fx-background-radius: 30;");
                        cambiarParteIdBoton(button, 4, "0");
                    }
                }
            }else{
                if(nodRow == fila){
                    if(puede){
                        if(patSup.getFill().equals(Color.GREEN)){
                            button.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                            cambiarParteIdBoton(button, 4, "positiva");
                        }else if(patSup.getFill().equals(Color.RED)){
                            button.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                            cambiarParteIdBoton(button, 4, "negativa");
                        }
                    }else{
                        button.setStyle("-fx-background-radius: 30;");
                        cambiarParteIdBoton(button, 4, "0");
                    }
                }
            }
        }
    }

    // Manejo del arrastre del switch
    public void onMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - paneSwitch8x3.getLayoutX();
        offsetY = event.getSceneY() - paneSwitch8x3.getLayoutY();
    }

    public void onMouseDragged(MouseEvent event) {
        paneSwitch8x3.setLayoutX(event.getSceneX() - offsetX);
        paneSwitch8x3.setLayoutY(event.getSceneY() - offsetY);
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