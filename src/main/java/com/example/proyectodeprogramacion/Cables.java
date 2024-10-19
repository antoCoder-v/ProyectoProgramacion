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
import java.util.ArrayList;
import java.util.List;

public class Cables {
    private Button buttonStart, buttonEnd;
    private AnchorPane pane;
    private GridPane busSuperior, busInferior, pistaSuperior, pistaInferior;
    private String estilo, carga; // Manejamos la carga que tienen alguno de los botones
    private List<CableInfo> cablesConectados; // Lista para almacenar los cables conectados

    // Clase interna para almacenar la información de los cables
    public class CableInfo {
        Button startButton;
        Button endButton;
        Line cable;

        public CableInfo(Button startButton, Button endButton, Line cable) {
            this.startButton = startButton;
            this.endButton = endButton;
            this.cable = cable;
        }
    }

    public List<CableInfo> getCablesConectados() {
        return cablesConectados;
    }

    // Constructor de la clase
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

    // Metodo para actualizar los cambios en los Gridpanes
    public void actualizarGridpanes() {
        busInferior.layout();
        busSuperior.layout();
        pistaInferior.layout();
        pistaSuperior.layout();
    }

    // | Método para establecer el segundo botón y dibujar el cable
    public void setButtonEndAndDrawCable(Button button) {
        this.buttonEnd = button;

        if (buttonStart != null && buttonEnd != null) {
            if (buttonStart == buttonEnd) {
                mostrarVentanaMensaje("No puedes conectar un botón consigo mismo.");
                buttonStart = null;
                buttonEnd = null;
            } else {
                // Verifica si ya existe un cable entre los dos botones
                CableInfo cableExistente = encontrarCable(buttonStart, buttonEnd);
                if (cableExistente != null) {
                    // Si existe un cable, lo eliminamos y actualizamos cargas
                    eliminarCable(cableExistente, buttonStart, buttonEnd);

                } else {

                    // Reconocer la carga de los botones y verifica que no se conecte a pista o bus
                    // quemados
                    if (!reconoceCarga(buttonStart, buttonEnd)) {
                        // Reiniciar las variables temporales para otro uso
                        buttonStart = null;
                        buttonEnd = null;
                        estilo = "";
                        carga = "";
                        return;
                    }

                    // Si no existe un cable, dibujamos uno nuevo
                    drawCable();

                    // Indicamos cables conectados
                    cambiarParteIdBoton(buttonStart, 5, "conectado");
                    cambiarParteIdBoton(buttonEnd, 5, "conectado");

                    // Manejar la corriente transmitida
                    manejoCorriente(buttonStart);
                    manejoCorriente(buttonEnd);

                    // actualizar la corriente
                    actualizarCorrienteTodos(); // Error aqui
                    actualizarGridpanes();

                    // Reiniciar las variables temporales para otro uso
                    buttonStart = null;
                    buttonEnd = null;
                    estilo = "";
                    carga = "";

                }
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
        Line cable = new Line(startPaneCoords.getX(), startPaneCoords.getY(), endPaneCoords.getX(),
                endPaneCoords.getY());
        cable.setStrokeWidth(3);
        cable.setStroke(Color.BLACK);

        // Agregar el cable al pane
        pane.getChildren().add(cable);

        // Agregar el cable y los botones a la lista de cables conectados
        cablesConectados.add(new CableInfo(buttonStart, buttonEnd, cable));
    }

    // Método para eliminar un cable
    private void eliminarCable(CableInfo cableInfo, Button startButton, Button endButton) {
        pane.getChildren().remove(cableInfo.cable);
        cablesConectados.remove(cableInfo);

        // Cambiamos la Id de los botones a desconectar
        cambiarParteIdBoton(endButton, 5, "desconectado");
        cambiarParteIdBoton(startButton, 5, "desconectado");

        // Cortamos la corriente transmitida
        carga = "0";
        estilo = "-fx-background-radius: 30;";
        manejoCorriente(endButton);

        // Reiniciar las variables temporales para otro uso
        buttonStart = null;
        buttonEnd = null;
        estilo = "";
        carga = "";

        // actualizar la corriente
        actualizarCorrienteTodos();
        actualizarGridpanes();
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
        // verificamos si es un boton de bateria
        if (boton.getId().equals("botonCargaNegativa") || boton.getId().equals("botonCargaPositiva")) {
            return; // Sale de la función
        }

        // String buttonId = boton.getId();
        String tipo = retornaUnValorDeID(boton, 1);
        int row = Integer.parseInt(retornaUnValorDeID(boton, 2));
        int col = Integer.parseInt(retornaUnValorDeID(boton, 3));

        // Propagar el color solo en la fila o columna correspondiente
        if (tipo.contains("busSuperior") || tipo.contains("busInferior")) {
            GridPane gridPane = tipo.contains("busSuperior") ? busSuperior : busInferior;
            for (Node node : gridPane.getChildren()) {
                Integer nodeRow = GridPane.getRowIndex(node);
                if (nodeRow != null && nodeRow.equals(row)) {
                    node.setStyle(estilo);
                    cambiarParteIdBoton((Button) node, 4, carga);
                }
            }
        } else {
            GridPane gridPane = tipo.contains("pistaSuperior") ? pistaSuperior : pistaInferior;
            for (Node node : gridPane.getChildren()) {
                Integer nodeCol = GridPane.getColumnIndex(node);
                String cargaCol = retornaUnValorDeID((Button) node, 4); // Obtenemos carga de los botones
                if (nodeCol != null && nodeCol.equals(col)) {
                    node.setStyle(estilo);
                    cambiarParteIdBoton((Button) node, 4, carga);
                }
            }
        }
    }

    // Método para reconocer la carga (color) de los botones
    private boolean reconoceCarga(Button boton1, Button boton2) {
        String carga1 = retornaUnValorDeID(boton1, 4);
        String carga2 = retornaUnValorDeID(boton2, 4);

        if (carga1.contains("positiva") && carga2.contains("negativa")|| carga1.contains("negativa") && carga2.contains("positiva")) {

            // Logica de corto circuito
            carga = "quemada";
            estilo = "-fx-background-color: black; -fx-background-radius: 30;";

            // quemamos la fila
            manejoCorriente(boton2);

            // Borra cable conectado anterioriormente
            borrarCables(boton2);

            mostrarVentanaMensaje("Se ha quemado una linea");
            return false;
        } else if (carga1.contains("quemada") || carga2.contains("quemada")) {
            mostrarVentanaMensaje("No se puede conectar a pista quemada");
            return false;
        } else if (carga1.contains("positiva") || carga2.contains("positiva")) {
            estilo = "-fx-background-color: green; -fx-background-radius: 30;";
            carga = "positiva";
            return true;
        } else if (carga1.contains("negativa") || carga2.contains("negativa")) {
            estilo = "-fx-background-color: red; -fx-background-radius: 30;";
            carga = "negativa";
            return true;
        } else {
            estilo = "-fx-background-radius: 30;";
            carga = "0";
            return true;
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

    // Método para mostrar una ventana de mensaje
    private void mostrarVentanaMensaje(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para encontrar el otro extremo de un cable conectado a un botón
    private Button encontrarOtroExtremo(Button button) {
        for (CableInfo cableInfo : cablesConectados) {
            if (cableInfo.startButton == button) {
                return cableInfo.endButton;
            } else if (cableInfo.endButton == button) {
                return cableInfo.startButton;
            }
        }
        return null; // Retorna null si no se encuentra ningún cable conectado
    }

    // funcion para actualizar el paso de corriente
    public void actualizarCorrienteTodos() {
        // Recorrer todos los GridPane
        GridPane[] gridPanes = { busSuperior, busInferior, pistaSuperior, pistaInferior };

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                Button button = (Button) node;
                String conexion = retornaUnValorDeID(button, 5);

                // identificamos cable conectado
                if (conexion.equals("conectado")) {

                    // reconocemos carga
                    carga = retornaUnValorDeID(button, 4);
                    // cambiarParteIdBoton(button, 4, carga);
                    if (carga.contains("positiva")) {
                        estilo = "-fx-background-color: green; -fx-background-radius: 30;";
                    } else if (carga.contains("negativa")) {
                        estilo = "-fx-background-color: red; -fx-background-radius: 30;";
                    } else {
                        estilo = "-fx-background-radius: 30;";
                    }

                    // buscamo su otro extremo
                    Button otroExtremo = encontrarOtroExtremo(button);
                    if (otroExtremo != null) {
                        manejoCorriente(otroExtremo);
                    }

                    // inicializar datos
                    carga = "0";
                    estilo = "";
                }

            }
        }
    }

    //Borramos cable a la fila quemada para no producir errores
    public void borrarCables(Button endButton) {
        String tipo = retornaUnValorDeID(endButton, 1);
        int row = Integer.parseInt(retornaUnValorDeID(endButton, 2));
        int col = Integer.parseInt(retornaUnValorDeID(endButton, 3));

        if (tipo.contains("busSuperior") || tipo.contains("busInferior")) {
            GridPane gridPane = tipo.contains("busSuperior") ? busSuperior : busInferior;
            for (Node node : gridPane.getChildren()) {
                Button boton = (Button) node;
                Integer nodeRow = GridPane.getRowIndex(node);
                String conectadoRow = retornaUnValorDeID(boton, 5); // Obtenemos si un boton esta conectado
                if (nodeRow != null && nodeRow.equals(row)) {
                    if (conectadoRow.equals("conectado")) {
                        // buscamos el otro extremo del boton
                        Button otroExtremo = encontrarOtroExtremo(boton);

                        // encontramos su cable en CableInfo
                        CableInfo info = encontrarCable(otroExtremo, boton);

                        // Borramo cable de la lista y pantalla
                        pane.getChildren().remove(info.cable);
                        cablesConectados.remove(info);

                    }

                }
            }
        } else {
            GridPane gridPane = tipo.contains("pistaSuperior") ? pistaSuperior : pistaInferior;
            for (Node node : gridPane.getChildren()) {
                Button boton = (Button) node;
                Integer nodeCol = GridPane.getColumnIndex(node);
                String conectadoCol = retornaUnValorDeID((Button) node, 5); // Obtenemos si un boton esta conectado
                if (nodeCol != null && nodeCol.equals(col)) {
                    if (conectadoCol.equals("conectado")) {
                        // buscamos el otro extremo del boton
                        Button otroExtremo = encontrarOtroExtremo(boton);

                        // encontramos su cable en CableInfo
                        CableInfo info = encontrarCable(otroExtremo, boton);

                        // Borramo cable de la lista y pantalla
                        pane.getChildren().remove(info.cable);
                        cablesConectados.remove(info);
                    }

                }
            }
        }
    }

    public void LogicaBateria(){
        //buscamos donde pasa corriente la bateria
        //recorre cablesConectado
        for (CableInfo cableInfo : cablesConectados) {
            if(cableInfo.startButton.getId().equals("botonCargaNegativa") || cableInfo.startButton.getId().equals("botonCargaPositiva")){
                //buscamos el otro extremo
                Button otroExtremo = encontrarOtroExtremo(cableInfo.startButton);
                
                if(!VariablesGlobales.corrienteBateria){
                    //Apagamos la corriente
                    estilo = "-fx-background-radius: 30;";
                    carga = "0";
                    cambiarParteIdBoton(otroExtremo, 4, carga);

                    //Actualizamos corriente
                    manejoCorriente(otroExtremo);
                }else{
                    //Encendemos la corriente
                    if(cableInfo.startButton.getId().equals("botonCargaNegativa")){
                        estilo = "-fx-background-color: red; -fx-background-radius: 30;";
                        carga = "negativa";
                    }else{
                        estilo = "-fx-background-color: green; -fx-background-radius: 30;";
                        carga = "positiva";
                    }
                    cambiarParteIdBoton(otroExtremo, 4, carga);

                    //Actualizamos corriente
                    manejoCorriente(otroExtremo);
                }
            }
        }
    }
}
