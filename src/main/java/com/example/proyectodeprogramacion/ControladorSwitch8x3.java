package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControladorSwitch8x3 {

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
    private boolean pasoCorriente1 = false, pasoCorriente2 = false, pasoCorriente3 = false, pasoCorriente4 = false, pasoCorriente5 = false, pasoCorriente6 = false, pasoCorriente7 = false, pasoCorriente8 = false;

    // Variables para manejar el arrastre del switch
    private double mouseXOffset;
    private double mouseYOffset;

    @FXML
    public void initialize() {
        // Inicializamos las variables
        protoboard = VariablesGlobales.controladorProtoboard;
        cables = VariablesGlobales.cables;

        // Habilitamos la eliminación del switch
        EliminarElementos.habilitarEliminacion(paneSwitch8x3);

        // Configuramos los eventos para el arrastre del switch
        paneSwitch8x3.setOnMousePressed(this::handleMousePressed);
        paneSwitch8x3.setOnMouseDragged(this::handleMouseDragged);

        //Majeno de paso de corriente en las patitas
        bot1.setOnAction(event -> {
            pasoCorriente1 = !pasoCorriente1;
            String tipo = enQueGrindpane(patInf1);
            pasoCorriente(patInf1, pasoCorriente1, tipo);
        });

        bot2.setOnAction(event -> {
            pasoCorriente2 = !pasoCorriente2;
            String tipo = enQueGrindpane(patInf2);
            pasoCorriente(patInf2, pasoCorriente2, tipo);
        });

        bot3.setOnAction(event -> {
            pasoCorriente3 = !pasoCorriente3;
            String tipo = enQueGrindpane(patInf3);
            pasoCorriente(patInf3, pasoCorriente3, tipo);
        });

        bot4.setOnAction(event -> {
            pasoCorriente4 = !pasoCorriente4;
            String tipo = enQueGrindpane(patInf4);
            pasoCorriente(patInf4, pasoCorriente4, tipo);
        });

        bot5.setOnAction(event -> {
            pasoCorriente5 = !pasoCorriente5;
            String tipo = enQueGrindpane(patInf5);
            pasoCorriente(patInf5, pasoCorriente5, tipo);
        });

        bot6.setOnAction(event -> {
            pasoCorriente6 = !pasoCorriente6;
            String tipo = enQueGrindpane(patInf6);
            pasoCorriente(patInf6, pasoCorriente6, tipo);
        });

        bot7.setOnAction(event -> {
            pasoCorriente7 = !pasoCorriente7;
            String tipo = enQueGrindpane(patInf7);
            pasoCorriente(patInf7, pasoCorriente7, tipo);
        });

        bot8.setOnAction(event -> {
            pasoCorriente8 = !pasoCorriente8;
            String tipo = enQueGrindpane(patInf8);
            pasoCorriente(patInf8, pasoCorriente8, tipo);
        });

        // Temporizador para verificar continuamente el estado de la corriente
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                cables.actualizarCorrienteTodos();

                verificarCorrienteEnPatita(patSup1, patInf1);
                verificarCorrienteEnPatita(patSup2, patInf2);
                verificarCorrienteEnPatita(patSup3, patInf3);
                verificarCorrienteEnPatita(patSup4, patInf4);
                verificarCorrienteEnPatita(patSup5, patInf5);
                verificarCorrienteEnPatita(patSup6, patInf6);
                verificarCorrienteEnPatita(patSup7, patInf7);
                verificarCorrienteEnPatita(patSup8, patInf8);                
            }
        };
        timer.start();
    }

    // Verificar si hay corriente en la patita superior y pasarla a la patita inferior
    private void verificarCorrienteEnPatita(Rectangle patSup, Rectangle patInf) {
        //int columna = 0;
        GridPane[] gridPanes = {protoboard.getBusSuperior(), protoboard.getPistaSuperior(), protoboard.getPistaInferior()};

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                //if (!(node instanceof Button)) continue;

                Button button = (Button) node;
                String carga = retornaUnValorDeID(button, 4);
                Integer nodeCol = GridPane.getColumnIndex(node);

                Bounds patSupBounds = patSup.localToScene(patSup.getBoundsInLocal());
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                //Obtenemos carga de las patitas superiores
                if (patSupBounds.intersects(buttonBounds)) {
                    // Cambia el color según la carga detectada
                    if (carga.equals("positiva")) {
                        patSup.setFill(Color.GREEN);
                        patInf.setFill(Color.GREEN);
                    } else if (carga.equals("negativa")) {
                        patSup.setFill(Color.RED);
                        patInf.setFill(Color.RED);
                    } else {
                        patSup.setFill(Color.BLACK);
                        patInf.setFill(Color.BLACK);
                    }
                }
                
            }
        }
    }

    //Manejamos el paso de corriente en las patitas
    public void pasoCorriente(Rectangle patInf, boolean Corriente, String tipo) {
        int col=0, fila=0;
        GridPane gridPane = tipo.contains("pistaSuperior") ? protoboard.getPistaSuperior() : (tipo.contains("pistaInferior") ? protoboard.getPistaInferior() : protoboard.getBusInferior());
        
        for (Node node : gridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Button button = (Button) node;
            Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
            Bounds patInfBounds = patInf.localToScene(patInf.getBoundsInLocal());

            if(patInfBounds.intersects(buttonBounds)){
                col = nodeCol;
                fila = GridPane.getRowIndex(node);
            }

            if(tipo.contains("pista")){
                if(nodeCol.equals(col)){
                    if(Corriente){
                        if(patInf.getFill().equals(Color.GREEN)){
                            node.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                            cambiarParteIdBoton(button, 4, "positiva");
                        }else if(patInf.getFill().equals(Color.RED)){
                            node.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                            cambiarParteIdBoton(button, 4, "negativa");
                        }
                    }else if(!VariablesGlobales.corrienteBateria || !Corriente){
                        node.setStyle("-fx-background-radius: 30;");
                        cambiarParteIdBoton(button, 4, "0");
                    }
                }
            }else if(tipo.contains("bus")){
                if(GridPane.getRowIndex(node).equals(fila)){
                    if(Corriente){
                        if(patInf.getFill().equals(Color.GREEN)){
                            node.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                            cambiarParteIdBoton(button, 4, "positiva");
                        }else if(patInf.getFill().equals(Color.RED)){
                            node.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                            cambiarParteIdBoton(button, 4, "negativa");
                        }
                    }else if(!VariablesGlobales.corrienteBateria || !Corriente){
                        node.setStyle("-fx-background-radius: 30;");
                        cambiarParteIdBoton(button, 4, "0");
                    }
                }
            }
            
        }

    }

    // Métodos auxiliares
    public String retornaUnValorDeID(Button button, int parte) {
        String id = button.getId();
        String[] partes = id.split("-");
        return partes[parte];
    }

    // Manejo del arrastre del switch
    private void handleMousePressed(javafx.scene.input.MouseEvent event) {
        mouseXOffset = event.getSceneX() - paneSwitch8x3.getTranslateX();
        mouseYOffset = event.getSceneY() - paneSwitch8x3.getTranslateY();
    }

    private void handleMouseDragged(javafx.scene.input.MouseEvent event) {
        paneSwitch8x3.setTranslateX(event.getSceneX() - mouseXOffset);
        paneSwitch8x3.setTranslateY(event.getSceneY() - mouseYOffset);
    }

    //nos indica en que pista o bus se encuentra la patita inferior
    private String enQueGrindpane(Rectangle patInf){
        GridPane[] gridPanes = {protoboard.getPistaSuperior(), protoboard.getPistaInferior(), protoboard.getBusInferior()};
        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                Button button = (Button) node;
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                Bounds patInfBounds = patInf.localToScene(patInf.getBoundsInLocal());
                if(patInfBounds.intersects(buttonBounds)){
                    String tipo = retornaUnValorDeID(button, 1);
                    return tipo;
                }
            }
        }
        return ""; // Return an empty string if no intersection is found
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