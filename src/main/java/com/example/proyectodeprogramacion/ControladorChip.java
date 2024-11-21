package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ControladorChip {

    @FXML private AnchorPane paneChip;
    @FXML protected Rectangle pin1, pin2, pin3, pin4, pin5, pin6, pin7, pin8, pin9, pin10, pin11, pin12, pin13, pin14;
    @FXML protected Button botonChip;
    @FXML private Text tipoChip;

    protected double offsetX, offsetY;
    protected ControladorProtoboard protoboard;
    private final Map<Rectangle, Set<Button>> botonesPorSalida = new HashMap<>();

    public void setTipoChip(String tipoChip) {
        this.tipoChip.setText(tipoChip);
    }

    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;
        paneChip.setOnMousePressed(this::onMousePressed);
        paneChip.setOnMouseDragged(this::onMouseDragged);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (pinesCorrectamenteConectados()) {
                    ejecutarOperacion(); // Propaga corriente normalmente
                } else {
                    // Limpia todas las salidas si los pines están desconectados
                    for (Rectangle salida : botonesPorSalida.keySet()) {
                        limpiarColoresBotones(salida);
                    }
                }
            }
        };
        timer.start();


    }

    public void onMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - paneChip.getLayoutX();
        offsetY = event.getSceneY() - paneChip.getLayoutY();
    }

    public void onMouseDragged(MouseEvent event) {
        paneChip.setLayoutX(event.getSceneX() - offsetX);
        paneChip.setLayoutY(event.getSceneY() - offsetY);
        //verificarConexionPins();
        ejecutarOperacion();
    }
    private void limpiarColoresBotones(Rectangle salida) {
        Set<Button> botones = botonesPorSalida.get(salida);
        if (botones != null) {
            for (Button button : botones) {
                cambiarColorBoton(button, Color.BLACK); // Restablece al estilo inicial (sin color)
            }
            botones.clear(); // Limpia el registro para esta salida
        }
    }




    public boolean verificarColorPin(Rectangle pin, Color colorEsperado) {
        Button button = obtenerBotonCorrespondiente(pin);
        if (button != null && obtenerColorBoton(button).equals(colorEsperado)) {
            pin.setFill(colorEsperado);
            return true;
        }
        pin.setFill(Color.BLACK);
        return false;
    }
    public Color obtenerColorBoton(Button button) {
        String[] parts = button.getId().split("-");
        String carga = parts.length > 4 ? parts[4].trim() : "";
        return switch (carga) {
            case "positiva" -> Color.GREEN;
            case "negativa" -> Color.RED;
            default -> Color.BLACK;
        };
    }

    private boolean pinesCorrectamenteConectados() {
        boolean pin7Correcto = verificarColorPin(pin7, Color.RED);  // Pin 7 debe estar conectado a tierra (rojo)
        boolean pin14Correcto = verificarColorPin(pin14, Color.GREEN);  // Pin 14 debe estar conectado a corriente (verde)

        if (pin7Correcto && pin14Correcto) {
            System.out.println("Pines 7 y 14 correctamente conectados");
            return true;
        } else {
            System.out.println("Error: Pines 7 y 14 mal conectados. No se puede propagar corriente.");
            return false;
        }
    }


    protected void propagarCorriente(Rectangle salida, Color color) {
        if (!pinesCorrectamenteConectados()) {
            System.out.println("Corriente no propagada debido a conexión incorrecta en pines 7 y 14.");
            limpiarColoresBotones(salida); // Limpia solo los botones asociados a esta salida
            return;
        }

        // Limpia los colores anteriores de esta salida
        limpiarColoresBotones(salida);

        GridPane pista = obtenerPistaCorrespondiente(salida);
        if (pista != null) {
            Bounds salidaBounds = salida.localToScene(salida.getBoundsInLocal());
            for (Node node : pista.getChildren()) {
                if (node instanceof Button button) {
                    Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                    if (Math.abs(buttonBounds.getMinX() - salidaBounds.getMinX()) < 5) {
                        cambiarColorBoton(button, color);
                        botonesPorSalida
                                .computeIfAbsent(salida, k -> new HashSet<>()) // Crea un nuevo conjunto si no existe
                                .add(button); // Registra el botón coloreado para esta salida
                    }
                }
            }
        }
    }



    private void cambiarColorBoton(Button button, Color color) {
        if (color.equals(Color.BLACK)) {
            // Restablece al estilo sin color
            button.setStyle("-fx-background-radius: 30;");
        } else {
            // Aplica el color correspondiente
            String colorHex = color.equals(Color.RED) ? "red" : color.equals(Color.GREEN) ? "green" : "black";
            button.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 30;");
        }
    }


    private GridPane obtenerPistaCorrespondiente(Rectangle salida) {
        Bounds salidaBounds = salida.localToScene(salida.getBoundsInLocal());
        if (coincideConPista(protoboard.getPistaSuperior(), salidaBounds)) {
            return protoboard.getPistaSuperior();
        } else if (coincideConPista(protoboard.getPistaInferior(), salidaBounds)) {
            return protoboard.getPistaInferior();
        }
        return null;
    }

    private boolean coincideConPista(GridPane pista, Bounds salidaBounds) {
        return pista.getChildren().stream()
                .filter(node -> node instanceof Button)
                .anyMatch(node -> salidaBounds.intersects(node.localToScene(node.getBoundsInLocal())));
    }

    private Button obtenerBotonCorrespondiente(Rectangle pin) {
        GridPane[] gridPanes = { protoboard.getBusSuperior(), protoboard.getPistaSuperior(), protoboard.getPistaInferior() };
        Bounds pinBounds = pin.localToScene(pin.getBoundsInLocal());
        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button button && pinBounds.intersects(button.localToScene(button.getBoundsInLocal()))) {
                    return button;
                }
            }
        }
        return null;
    }

    protected abstract void ejecutarOperacion();
}


