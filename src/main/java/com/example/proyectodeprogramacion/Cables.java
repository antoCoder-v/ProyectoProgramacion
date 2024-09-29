package com.example.proyectodeprogramacion;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;

public class Cables {
    private Button buttonStart, buttonEnd;
    private AnchorPane pane;
    private GridPane busSuperior, busInferior, pistaSuperior, pistaInferior;
    private String estilo;   // Manejamos la carga que tienen alguno de los botones
    private List<CableInfo> cablesConectados; // Lista para almacenar los cables conectados

    // Clase interna para almacenar la información de los cables
    private class CableInfo {
        Button startButton;
        Button endButton;
        Line cable;

        CableInfo(Button startButton, Button endButton, Line cable) {
            this.startButton = startButton;
            this.endButton = endButton;
            this.cable = cable;
        }
    }

    public Cables(GridPane busSuperior, GridPane pistaSuperior, GridPane busInferior, GridPane pistaInferior) {
        cablesConectados = new ArrayList<>();
        this.busSuperior = busSuperior;
        this.pistaSuperior = pistaSuperior;
        this.busInferior = busInferior;
        this.pistaInferior = pistaInferior;
        this.pane = VariablesGlobales.pantallaPrincipal; // Suponiendo que el pane se obtiene de las variables globales
    }

    // Método para establecer el primer botón
    public void setButtonStart(Button button) {
        this.buttonStart = button;
    }

    // Método para obtener el primer botón (soluciona el error)
    public Button getButtonStart() {
        return buttonStart;
    }

    public void setButtonEndAndDrawCable(Button button) {
        this.buttonEnd = button;

        if (buttonStart != null && buttonEnd != null) {
            if (buttonStart == buttonEnd) {
                mostrarVentanaMensaje("No puedes conectar un botón consigo mismo.");
            } else {
                // Verifica si ya existe un cable entre los dos botones
                CableInfo cableExistente = encontrarCable(buttonStart, buttonEnd);
                if (cableExistente != null) {
                    // Si existe un cable, lo eliminamos
                    eliminarCable(cableExistente);
                } else {
                    // Si no existe un cable, dibujamos uno nuevo
                    drawCable();

                    // Reconocer la carga de los botones de inicio y fin
                    reconoceCarga(buttonStart, buttonEnd);

                    // Llamar al método manejoCorriente para propagar el color a otros botones en la fila/columna
                    manejoCorriente(buttonStart);
                    manejoCorriente(buttonEnd);
                }

                // Reiniciar las variables temporales para otro uso
                buttonStart = null;
                buttonEnd = null;
                estilo = "";
            }
        }
    }

    // Método para dibujar el cable
    private void drawCable() {
        // Obtener las posiciones globales de los botones
        Point2D startPoint = buttonStart.localToScene(buttonStart.getWidth() / 2, buttonStart.getHeight() / 2);
        Point2D endPoint = buttonEnd.localToScene(buttonEnd.getWidth() / 2, buttonEnd.getHeight() / 2);

        // Convertir las coordenadas de escena a coordenadas de pane
        Point2D startPaneCoords = pane.sceneToLocal(startPoint);
        Point2D endPaneCoords = pane.sceneToLocal(endPoint);

        // Crear la línea que representa el cable
        Line cable = new Line(startPaneCoords.getX(), startPaneCoords.getY(), endPaneCoords.getX(), endPaneCoords.getY());
        cable.setStrokeWidth(3);
        cable.setStroke(Color.BLACK);

        // Agregar el cable al pane
        pane.getChildren().add(cable);

        // Agregar el cable y los botones a la lista de cables conectados
        cablesConectados.add(new CableInfo(buttonStart, buttonEnd, cable));
    }

    // Método para eliminar un cable
    private void eliminarCable(CableInfo cableInfo) {
        pane.getChildren().remove(cableInfo.cable);
        cablesConectados.remove(cableInfo);
    }

    // Método para encontrar un cable entre dos botones
    private CableInfo encontrarCable(Button startButton, Button endButton) {
        for (CableInfo cableInfo : cablesConectados) {
            if ((cableInfo.startButton == startButton && cableInfo.endButton == endButton) ||
                    (cableInfo.startButton == endButton && cableInfo.endButton == startButton)) {
                return cableInfo;
            }
        }
        return null;
    }

    // Método para manejar la corriente
    private void manejoCorriente(Button boton) {
        String buttonId = boton.getId();
        String tipo = "";
        int row = -1;
        int col = -1;

        // Sepamos los datos que están en el ID
        if (buttonId != null && buttonId.startsWith("Button -")) {
            String[] parts = buttonId.split("-");
            if (parts.length == 4) {
                tipo = parts[1].trim();
                row = Integer.parseInt(parts[2].trim());
                col = Integer.parseInt(parts[3].trim());
            } else {
                System.out.println("ID del botón no tiene el formato esperado.");
            }
        }

        // Propagar el color solo en la fila o columna correspondiente
        if (tipo.contains("busSuperior") || tipo.contains("busInferior")) {
            GridPane gridPane = tipo.contains("busSuperior") ? busSuperior : busInferior;
            for (Node node : gridPane.getChildren()) {
                Integer nodeRow = GridPane.getRowIndex(node);
                if (nodeRow != null && nodeRow.equals(row)) {
                    node.setStyle(estilo);
                }
            }
        } else if (tipo.contains("pistaSuperior") || tipo.contains("pistaInferior")) {
            GridPane gridPane = tipo.contains("pistaSuperior") ? pistaSuperior : pistaInferior;
            for (Node node : gridPane.getChildren()) {
                Integer nodeCol = GridPane.getColumnIndex(node);
                if (nodeCol != null && nodeCol.equals(col)) {
                    node.setStyle(estilo);
                }
            }
        }
    }

    // Método para reconocer la carga (color) de los botones
    private void reconoceCarga(Button boton1, Button boton2) {
        String style1 = boton1.getStyle();
        String style2 = boton2.getStyle();
        if (style1.contains("green") && style2.contains("red") || style1.contains("red") && style2.contains("green")) {
            mostrarVentanaMensaje("No se puede conectar distintas cargas de energías");
        } else if (style1.contains("green") || style2.contains("green")) {
            estilo = "-fx-background-color: green; -fx-background-radius: 30;";
        } else if (style1.contains("red") || style2.contains("red")) {
            estilo = "-fx-background-color: red; -fx-background-radius: 30;";
        } else {
            estilo = "-fx-background-radius: 30;";
        }
    }

    // Método para mostrar una ventana de mensaje
    private void mostrarVentanaMensaje(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
