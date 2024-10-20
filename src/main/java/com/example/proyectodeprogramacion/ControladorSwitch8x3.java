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
    private Cables cables;

    private String corriente = "0";
    private boolean hayCorriente = false;
    private boolean pasoCorrienteSwitch = false;
    private Integer colPatSup1;

    // Variables para manejar el arrastre del switch
    private double mouseXOffset;
    private double mouseYOffset;

    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;
        cables = VariablesGlobales.cables;

        EliminarElementos.habilitarEliminacion(paneSwitch8x3);

        paneSwitch8x3.setOnMousePressed(this::handleMousePressed);
        paneSwitch8x3.setOnMouseDragged(this::handleMouseDragged);

        // Configuración de los botones para manejar el estado del switch
        bot1.setOnAction(event -> toggleSwitch(patSup1, patInf1));
        bot2.setOnAction(event -> toggleSwitch(patSup2, patInf2));
        bot3.setOnAction(event -> toggleSwitch(patSup3, patInf3));
        bot4.setOnAction(event -> toggleSwitch(patSup4, patInf4));
        bot5.setOnAction(event -> toggleSwitch(patSup5, patInf5));
        bot6.setOnAction(event -> toggleSwitch(patSup6, patInf6));
        bot7.setOnAction(event -> toggleSwitch(patSup7, patInf7));
        bot8.setOnAction(event -> toggleSwitch(patSup8, patInf8));

        // Temporizador para verificar continuamente el estado de la corriente
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                verificarCorrienteEnProtoboard();
                cables.actualizarCorrienteTodos();
            }
        };
        timer.start();
    }

    // Método para alternar el estado del switch
    private void toggleSwitch(Rectangle patSup, Rectangle patInf) {
        if (pasoCorrienteSwitch) {
            // Apaga la corriente
            patSup.setFill(Color.DODGERBLUE);
            patInf.setFill(Color.DODGERBLUE);
            pasoCorrienteSwitch = false;
        } else {
            // Verifica si la patita superior está recibiendo corriente
            verificarCorrienteEnPatita(patSup, patInf);
        }
    }

    // Verificar si hay corriente en la patita superior y pasarla a la patita inferior
    private void verificarCorrienteEnPatita(Rectangle patSup, Rectangle patInf) {
        GridPane[] gridPanes = {protoboard.getPistaSuperior(), protoboard.getPistaInferior()};

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (!(node instanceof Button)) continue;

                Button button = (Button) node;
                String carga = retornaUnValorDeID(button, 4);

                Bounds patSupBounds = patSup.localToScene(patSup.getBoundsInLocal());
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                if (patSupBounds.intersects(buttonBounds)) {
                    // Cambia el color según la carga detectada
                    if (carga.equals("positiva")) {
                        patSup.setFill(Color.GREEN);
                        patInf.setFill(Color.GREEN);
                        corriente = "positiva";
                        pasoCorrienteSwitch = true;
                    } else if (carga.equals("negativa")) {
                        patSup.setFill(Color.RED);
                        patInf.setFill(Color.RED);
                        corriente = "negativa";
                        pasoCorrienteSwitch = true;
                    } else {
                        patSup.setFill(Color.BLACK);
                        patInf.setFill(Color.BLACK);
                        corriente = "0";
                        pasoCorrienteSwitch = false;
                    }
                }
            }
        }
    }

    // Verifica constantemente el estado de las patitas para detectar si deben pasar corriente
    private void verificarCorrienteEnProtoboard() {
        GridPane[] gridPanes = {protoboard.getPistaSuperior(), protoboard.getPistaInferior()};

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (!(node instanceof Button)) continue;

                Button button = (Button) node;
                String carga = retornaUnValorDeID(button, 4);

                // Detectar colisiones entre patitas y botones de la protoboard
                verificarColisionesPatitas(patSup1, patInf1, button, carga);
                verificarColisionesPatitas(patSup2, patInf2, button, carga);
                verificarColisionesPatitas(patSup3, patInf3, button, carga);
                verificarColisionesPatitas(patSup4, patInf4, button, carga);
                verificarColisionesPatitas(patSup5, patInf5, button, carga);
                verificarColisionesPatitas(patSup6, patInf6, button, carga);
                verificarColisionesPatitas(patSup7, patInf7, button, carga);
                verificarColisionesPatitas(patSup8, patInf8, button, carga);
            }
        }
    }

    // Verifica si las patitas superiores e inferiores deben transferir corriente
    private void verificarColisionesPatitas(Rectangle patSup, Rectangle patInf, Button button, String carga) {
        Bounds patSupBounds = patSup.localToScene(patSup.getBoundsInLocal());
        Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

        if (patSupBounds.intersects(buttonBounds)) {
            procesarColision(patSup, patInf, carga);
        }
    }

    // Procesa la colisión entre patitas y botones de la protoboard
    private void procesarColision(Rectangle patSup, Rectangle patInf, String carga) {
        if (pasoCorrienteSwitch) {
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
        } else {
            patSup.setFill(Color.DODGERBLUE); // Desactiva la corriente
            patInf.setFill(Color.DODGERBLUE);
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
}



