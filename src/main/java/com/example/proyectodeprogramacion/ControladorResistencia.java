package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class ControladorResistencia implements ControladorElemento {
    @FXML
    Rectangle pata2, pata1;

    @FXML
    AnchorPane paneResistencia;

    private ControladorProtoboard protoboard;
    private String corriente = "0";
    // Atributo que almacena el tipo de GridPane (pistaSuperior, pistaInferior,
    // etc.)
    private String tipoGridPane = "";

    private boolean resistenciaConectada = false, encimaDeProtoboard = false; // --->Variables importantes para manejar la conceccion con protoboa
    
    private boolean resistenciaMoviendose = false;

    private double offsetX, offsetY;

    // Metodos para modificar ledConectado (IMPORTANTE que las funciones esten en la
    // interfaz ControladorElemento)

    @Override
    public void setColor(String color) {
        //this.color = color;
    }
   
    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;

        // manejamos la eliminacion y la coneccion
        EliminarElementos.habilitarEliminacion(paneResistencia, this);

        paneResistencia.setOnMousePressed(this::handleMousePressed);
        paneResistencia.setOnMouseDragged(this::handleMouseDragged);

        // verifica cambios en protoboard y se ven en resistencia
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                encimaDeProtoboard = verificarPosicionResistencia(protoboard);

                //manejamosPasoCorrienteResistencia();
                 // else if (encimaDeProtoboard && !resistenciaConectada) {
                manejamosPasoCorrienteResistencia();

                
            }
        };
        timer.start();
    }

    // Metodos para manejar el movimiento de resistencia 
    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - paneResistencia.getLayoutX();
        offsetY = event.getSceneY() - paneResistencia.getLayoutY();
        resistenciaMoviendose = true;
        //manejamosPasoCorrienteResistencia();
    }

    private void handleMouseDragged(MouseEvent event) {
        paneResistencia.setLayoutX(event.getSceneX() - offsetX);
        paneResistencia.setLayoutY(event.getSceneY() - offsetY);
        // aneResistencia.toFront();
        resistenciaMoviendose = false;
    }

    // Método que verifica si alguna de las patitas está recibiendo corriente
    public boolean verificarPosicionResistencia(ControladorProtoboard protoboard) {
        if (protoboard == null) {
            // System.out.println("Protoboard no está asignado.");
            return false;
        }

        boolean corrienteDetectada = false;

        // Verificamos si la pata1 está recibiendo corriente
        corrienteDetectada = verificarEnGridPane(protoboard.getBusSuperior(), pata1, pata2, "busSuperior")
                || verificarEnGridPane(protoboard.getPistaSuperior(), pata1, pata2, "pistaSuperior")
                || verificarEnGridPane(protoboard.getBusInferior(), pata1, pata2, "busInferior")
                || verificarEnGridPane(protoboard.getPistaInferior(), pata1, pata2, "pistaInferior");

        // Si se detecta corriente, se propaga a la columna donde está la otra patita
        if (corrienteDetectada) {
            // manejamosPasoCorrienteResistencia();
            return true;
        }
        return false;
    }

    // Método para verificar si una de las patitas está recibiendo corriente en la
    // protoboard
    private boolean verificarEnGridPane(GridPane gridPane, Rectangle pata, Rectangle pataRecibe, String tipo) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                // Obtener los límites en escena de ambos elementos para una comparación precisa
                Bounds pataBounds = pata.localToScene(pata.getBoundsInLocal());
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                // Verifica si la patita intersecta con los límites del botón
                if (pataBounds.intersects(buttonBounds)) {
                    // Obtenemos la carga del botón
                    String buttonId = button.getId();
                    String[] parts = buttonId.split("-");
                    String carga = parts[4].trim();

                    // Si el botón tiene carga, marcamos que la resistencia está recibiendo
                    // corriente
                    if (carga.equals("positiva") || carga.equals("negativa")) {
                        pata.setFill(carga.equals("positiva") ? Color.GREEN : Color.RED);
                        pataRecibe.setFill(carga.equals("positiva") ? Color.GREEN : Color.RED);
                        corriente = carga;
                        tipoGridPane = tipo;
                        return true; // Se encontró corriente
                    } else {
                        pata.setFill(Color.DODGERBLUE);
                        pataRecibe.setFill(Color.DODGERBLUE);
                        corriente = carga;
                        tipoGridPane = tipo;
                        return true; // Se encontró corriente
                    }
                }
            }
        }
        return false; // No se encontró corriente
    }

    // Método que maneja el traspaso de corriente a la columna donde está la otra
    // patita de la resistencia
    private void manejamosPasoCorrienteResistencia() {
        // Determinamos cuál es la patita que NO está recibiendo corriente

        // Obtenemos la columna de la pata 2
        int col = obtenerColumnaDePata(pata2);

        // Propagamos la corriente en la columna de la otra patita
        GridPane gridPane = tipoGridPane.contains("pistaSuperior") ? protoboard.getPistaSuperior()
                : protoboard.getPistaInferior();
        for (Node node : gridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            if (nodeCol != null && nodeCol.equals(col) && node instanceof Button) {
                Button button = (Button) node;
                if(!resistenciaMoviendose){
                    if (pata2.getFill().equals(Color.GREEN)) {
                        button.setStyle("-fx-background-color: green; -fx-background-radius: 30;");
                        cambiarParteIdBoton(button, 4, corriente);
                    } else if (pata2.getFill().equals(Color.RED)) {
                        button.setStyle("-fx-background-color: red; -fx-background-radius: 30;");
                        cambiarParteIdBoton(button, 4, corriente);
                    } else{
                        button.setStyle("fx-background-radius: 30;");
                        cambiarParteIdBoton(button, 4, corriente);
                    }
                }else {
                    button.setStyle("fx-background-radius: 30;");
                    cambiarParteIdBoton(button, 4, corriente);
                }
                
            }
        }
    }

    // Método auxiliar para obtener la columna en la que está una patita
    private int obtenerColumnaDePata(Rectangle pata) {
        GridPane gridPane = tipoGridPane.contains("pistaSuperior") ? protoboard.getPistaSuperior()
                : protoboard.getPistaInferior();
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                // Obtener los límites en escena para comparar
                Bounds pataBounds = pata.localToScene(pata.getBoundsInLocal());
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());

                // Verificamos si la patita está en la misma posición que el botón
                if (pataBounds.intersects(buttonBounds)) {
                    return GridPane.getColumnIndex(button); // Retorna la columna del botón
                }
            }
        }
        return -1; // Si no se encontró una columna
    }

    // Método auxiliar que cambia una parte específica del ID de un botón
    private void cambiarParteIdBoton(Button button, int parteIndex, String nuevaParte) {
        // Obtenemos el ID actual del botón
        String buttonId = button.getId();

        if (buttonId != null && !buttonId.isEmpty()) {
            // Dividimos el ID del botón en partes usando el guion como separador
            String[] partes = buttonId.split("-");

            // Nos aseguramos de que el índice proporcionado sea válido
            if (parteIndex >= 0 && parteIndex < partes.length) {
                // Cambiamos la parte del ID en el índice proporcionado por la nueva parte
                partes[parteIndex] = nuevaParte;

                // Unimos las partes de nuevo en un solo string
                String nuevoId = String.join("-", partes);

                // Asignamos el nuevo ID al botón
                button.setId(nuevoId);
            }
        }
    }

}
