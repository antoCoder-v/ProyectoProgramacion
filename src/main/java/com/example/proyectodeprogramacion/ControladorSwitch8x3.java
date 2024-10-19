package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControladorSwitch8x3 {

    @FXML
    private Pane paneSwitch8x3;
    @FXML
    private RadioButton bot1, bot2, bot3, bot4, bot5, bot6, bot7, bot8;
    @FXML
    private Rectangle patSup1, patSup2, patSup3, patSup4, patSup5, patSup6, patSup7, patSup8;
    @FXML
    private Rectangle patInf1, patInf2, patInf3, patInf4, patInf5, patInf6, patInf7, patInf8;

    private ControladorProtoboard protoboard;
    private ManejoCables cables;

    private String corriente = "0";
    private boolean hayCorriente = false;
    private boolean pasoCorrienteSwitch = false;
    private Integer colPatSup1;

    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;
        cables = VariablesGlobales.cables;

        EliminarElementos.habilitarEliminacion(paneSwitch8x3);

        paneSwitch8x3.setOnMousePressed(this::handleMousePressed);
        paneSwitch8x3.setOnMouseDragged(this::handleMouseDragged);

        bot1.setOnAction(event -> {
            togglePatitas(patSup1, patInf1);
            verificarEnGridPaneCorriente();
        });
        bot2.setOnAction(event -> {
            togglePatitas(patSup2, patInf2);
            verificarEnGridPaneCorriente();
        });
        bot3.setOnAction(event -> {
            togglePatitas(patSup3, patInf3);
            verificarEnGridPaneCorriente();
        });
        bot4.setOnAction(event -> {
            togglePatitas(patSup4, patInf4);
            verificarEnGridPaneCorriente();
        });
        bot5.setOnAction(event -> {
            togglePatitas(patSup5, patInf5);
            verificarEnGridPaneCorriente();
        });
        bot6.setOnAction(event -> {
            togglePatitas(patSup6, patInf6);
            verificarEnGridPaneCorriente();
        });
        bot7.setOnAction(event -> {
            togglePatitas(patSup7, patInf7);
            verificarEnGridPaneCorriente();
        });
        bot8.setOnAction(event -> {
            togglePatitas(patSup8, patInf8);
            verificarEnGridPaneCorriente();
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

    private void togglePatitas(Rectangle patSup, Rectangle patInf) {
        if (patSup.getFill() == Color.DODGERBLUE && patInf.getFill() == Color.DODGERBLUE) {
            patSup.setFill(Color.LIMEGREEN);
            patInf.setFill(Color.LIMEGREEN);
            pasoCorrienteSwitch = true;
        } else {
            patSup.setFill(Color.DODGERBLUE);
            patInf.setFill(Color.DODGERBLUE);
            pasoCorrienteSwitch = false;
        }
    }

    private void verificarEnGridPaneCorriente() {
        GridPane[] gridPanes = {protoboard.getPistaSuperior(), protoboard.getPistaInferior()};

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                Button button = (Button) node;
                String tipo = retornaUnValorDeID(button, 1);
                Integer nodeCol = GridPane.getColumnIndex(node);

                Bounds patSup1Bounds = patSup1.localToScene(patSup1.getBoundsInLocal());
                Bounds patInf1Bounds = patInf1.localToScene(patInf1.getBoundsInLocal());

                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                String carga = retornaUnValorDeID(button, 4);

                if (patSup1Bounds.intersects(buttonBounds)) {
                    colPatSup1 = nodeCol;
                    if (carga.equals("positiva")) {
                        patSup1.setFill(Color.GREEN);
                        patInf1.setFill(Color.GREEN);
                        corriente = "positiva";
                        hayCorriente = true;
                        pasoCorrienteSwitch = true;
                    } else if (carga.equals("negativa")) {
                        patSup1.setFill(Color.RED);
                        patInf1.setFill(Color.RED);
                        corriente = "negativa";
                        hayCorriente = true;
                        pasoCorrienteSwitch = true;
                    } else if (carga.equals("0")) {
                        patSup1.setFill(Color.BLACK);
                        patInf1.setFill(Color.BLACK);
                        corriente = "0";
                        hayCorriente = false;
                        pasoCorrienteSwitch = false;
                    }
                    corrienteVecina(nodeCol, nodeCol + 2, tipo);
                } else if (patSup1.getFill().equals(Color.BLACK)) {
                    if (patInf1Bounds.intersects(buttonBounds)) {
                        if (carga.equals("positiva")) {
                            patSup1.setFill(Color.GREEN);
                            patInf1.setFill(Color.GREEN);
                            corriente = "positiva";
                            hayCorriente = true;
                            pasoCorrienteSwitch = true;
                        } else if (carga.equals("negativa")) {
                            patSup1.setFill(Color.RED);
                            patInf1.setFill(Color.RED);
                            corriente = "negativa";
                            hayCorriente = true;
                            pasoCorrienteSwitch = true;
                        } else if (carga.equals("0")) {
                            patSup1.setFill(Color.BLACK);
                            patInf1.setFill(Color.BLACK);
                            corriente = "0";
                            hayCorriente = false;
                            pasoCorrienteSwitch = false;
                        }
                        corrienteVecina(nodeCol, nodeCol - 2, tipo);
                    }
                }
            }
        }
    }

    private void corrienteVecina(int col1, int col2, String tipo) {
        GridPane gridPane = tipo.contains("pistaSuperior") ? protoboard.getPistaSuperior() : protoboard.getPistaInferior();
        for (Node node : gridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            if (nodeCol.equals(col1) || nodeCol.equals(col2)) {
                if (corriente.equals("positiva")) {
                    node.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                    patSup2.setFill(Color.GREEN);
                    patInf2.setFill(Color.GREEN);
                    pasoDeCorriente("pistaInferior");
                } else if (corriente.equals("negativa")) {
                    node.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                    patSup2.setFill(Color.RED);
                    patInf2.setFill(Color.RED);
                    pasoDeCorriente("pistaInferior");
                } else {
                    node.setStyle("-fx-background-radius: 30;");
                    patSup2.setFill(Color.BLACK);
                    patInf2.setFill(Color.BLACK);
                    pasoDeCorriente("pistaInferior");
                }
                cambiarParteIdBoton((Button) node, 4, corriente);
            }
        }
    }

    private void pasoDeCorriente(String tipo) {
        GridPane gridPane = tipo.contains("pistaSuperior") ? protoboard.getPistaSuperior() : protoboard.getPistaInferior();
        for (Node node : gridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            if (nodeCol.equals(colPatSup1) || nodeCol.equals(colPatSup1 + 2)) {
                if (pasoCorrienteSwitch && !patSup2.getFill().equals(Color.BLACK)) {
                    if (corriente.equals("positiva") && patSup2.getFill().equals(Color.GREEN)) {
                        node.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                        cambiarParteIdBoton((Button) node, 4, corriente);
                    } else if (corriente.equals("negativa") && patSup2.getFill().equals(Color.RED)) {
                        node.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                        cambiarParteIdBoton((Button) node, 4, corriente);
                    }
                } else {
                    node.setStyle("-fx-background-radius: 30;");
                    cambiarParteIdBoton((Button) node, 4, "0");
                }
            }
        }
    }

    public void cambiarParteIdBoton(Button button, int index, String nuevoValor) {
        try {
            String buttonId = button.getId();
            if (buttonId.equals("botonCargaNegativa") || buttonId.equals("botonCargaPositiva")) {
                return;
            }
            String[] parts = buttonId.split("-");
            parts[index] = nuevoValor;
            String newId = String.join("-", parts);
            button.setId(newId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String retornaUnValorDeID(Button button, int index) {
        String buttonId = button.getId();
        String[] parts = buttonId.split("-");
        return parts[index];
    }

    private void handleMousePressed(javafx.scene.input.MouseEvent mouseEvent) {
        paneSwitch8x3.toFront();
    }

    private void handleMouseDragged(javafx.scene.input.MouseEvent mouseEvent) {
        paneSwitch8x3.setLayoutX(mouseEvent.getSceneX() - paneSwitch8x3.getPrefWidth() / 2);
        paneSwitch8x3.setLayoutY(mouseEvent.getSceneY() - paneSwitch8x3.getPrefHeight() / 2);
    }
}


