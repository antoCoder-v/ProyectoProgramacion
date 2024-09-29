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


public class Cables {
    private Button buttonStart,buttonEnd;
    private AnchorPane pane;
    private GridPane busSuperior,busInferior,pistaSuperior,pistaInferior;
    private String estilo;   //Manejamos la carga y la Id que tienen alguno de los botones
    private Line cableSeleccionado = null; // Cable actualmente seleccionado
   // private Line cable;

    public Cables() {
    }

    public Cables(GridPane busSuperior, GridPane pistaSuperior, GridPane busInferior, GridPane pistaInferior) {
        pane = VariablesGlobales.pantallaPrincipal;
        //EliminarElementos.habilitarEliminacion(cable);
        //recibimos los grindpane para el manejo correcto de las corrientes
        if (busSuperior == null || pistaSuperior == null || busInferior == null || pistaInferior == null) {
            System.out.println("Alguno de los GridPane recibidos es nulo.");
        }
        this.busSuperior = busSuperior;
        this.busInferior = busInferior;
        this.pistaSuperior = pistaSuperior;
        this.pistaInferior = pistaInferior;
    }

    // Método para establecer el primer botón
    public void setButtonStart(Button button) {
        this.buttonStart = button;
    }

    //| Método para establecer el segundo botón y dibujar el cable
    public void setButtonEndAndDrawCable(Button button) {
        this.buttonEnd = button;

        if (buttonStart != null && buttonEnd != null) {
            if(buttonStart == buttonEnd){
                mostrarVentanaMensaje("No puedes conectar un botón consigo mismo.");
            }else{
                drawCable();

                //indicar en la id si el boton esta conectado a un cable
                cambiarParteIdBoton(buttonStart, 5, "conectado"); 
                cambiarParteIdBoton(buttonEnd, 5, "conectado");

                // Reconocer la carga de los botones de inicio y fin
                reconoceCarga(buttonStart, buttonEnd);

                // Llamar al método manejoCorriente para propagar el color a otros botones en la fila/columna
                manejoCorriente(buttonStart);
                manejoCorriente(buttonEnd);

                actualizarGridPanes();

                // Reiniciar las variables temporales para otro uso
                buttonStart = null;
                buttonEnd = null;
                estilo = "";
            }
        }
    }

    public Button getButtonStart() {
        return buttonStart;
    }

    public void actualizarGridPanes() {
        busSuperior.layout();
        busInferior.layout();
        pistaSuperior.layout();
        pistaInferior.layout();
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

        // Agregar el cable al pane
        pane.getChildren().add(cable);
    }

    // Método para habilitar la edición del cable
    private void enableCableEdit(Line cable) {
        // Evento para seleccionar el cable al hacer clic izquierdo
        cable.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Resaltar el cable cuando esté seleccionado
                cable.setStroke(Color.BLUE);
                cableSeleccionado = cable;
            }
            // Evento para eliminar el cable al hacer clic derecho
            if (event.getButton() == MouseButton.SECONDARY) {
                pane.getChildren().remove(cable);
            }
        });

        // Evento para arrastrar el cable y moverlo
        cable.setOnMouseDragged(event -> {
            if (cableSeleccionado != null) {
                // Obtener las nuevas coordenadas y actualizar la posición del cable
                double newX = event.getX();
                double newY = event.getY();

                // Actualizar solo el punto final para simular el arrastre
                cableSeleccionado.setEndX(newX);
                cableSeleccionado.setEndY(newY);
            }
        });
    }

    // Método para mostrar una ventana de mensaje
    private void mostrarVentanaMensaje(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para manejar la corriente
    private void manejoCorriente(Button boton) {
        String buttonId = boton.getId();
        String tipo = "";
        int row = -1;
        int col = -1;
        String carga = "";

        if (buttonId.equals("botonCargaNegativa") || buttonId.equals("botonCargaPositiva")) {
            System.out.println("BOTON DE BATERIA");
            return;
        }

        // Separamos los datos que estan en el ID
        if (buttonId != null && buttonId.startsWith("Button -")) {
            String[] parts = buttonId.split("-");
            if (parts.length == 6) {
                tipo = parts[1].trim();
                row = Integer.parseInt(parts[2].trim());
                col = Integer.parseInt(parts[3].trim());
                carga = parts[4].trim();
                System.out.println("Tipo: " + tipo + ", Fila: " + row + ", Columna: " + col + ", Carga: " + carga); // BORRAR DESPUES
            } else {
                System.out.println("ID del botón no tiene el formato esperado.");
            }
        }
        if (tipo.equals("bateria")) {
            System.out.println("sale de la funcion");
            return;
        }
        // Propagar el color solo en la fila o columna correspondiente
        if (tipo.contains("busSuperior") || tipo.contains("busInferior")) {
            GridPane gridPane = tipo.contains("busSuperior") ? busSuperior : busInferior;
            for (Node node : gridPane.getChildren()) {
                Integer nodeRow = GridPane.getRowIndex(node);
                if (nodeRow != null && nodeRow.equals(row)) {
                    node.setStyle(estilo);
                    if (carga.equals("positiva")) {
                        cambiarParteIdBoton((Button) node, 4, "positiva");
                    } else if (carga.equals("negativa")) {
                        cambiarParteIdBoton((Button) node, 4, "negativa");
                    }
                }
            }
        } else{
            GridPane gridPane = tipo.contains("pistaSuperior") ? pistaSuperior : pistaInferior;
            for (Node node : gridPane.getChildren()) {
                Integer nodeCol = GridPane.getColumnIndex(node);
                if (nodeCol != null && nodeCol.equals(col)) {
                    node.setStyle(estilo);
                    if (carga.equals("positiva")) {
                        cambiarParteIdBoton((Button) node, 4, "positiva");
                    } else if (carga.equals("negativa")) {
                        cambiarParteIdBoton((Button) node, 4, "negativa");
                    }
                }
            }
        }
    }

    // Método para reconocer la carga de los botones y cambiar la Id respecto a su corriente
    private void reconoceCarga(Button boton1, Button boton2){
        String style1 = boton1.getStyle();
        String style2 = boton2.getStyle();
        if(style1.contains("green") && style2.contains("red") || style1.contains("red") && style2.contains("green")){
            mostrarVentanaMensaje("No se puede conectar distintas cargas de energias");
        }else if(style1.contains("green") || style2.contains("green")){
            if (style1.contains("green")) {
                System.out.println("Boton 1: " + boton1.getId()); // BORRAR DESPUES
                cambiarParteIdBoton(boton1, 4, "positiva");
            }
            if (style2.contains("green")) {
                System.out.println("Boton 2: " + boton2.getId()); // BORRAR DESPUES
                cambiarParteIdBoton(boton2, 4, "positiva");
            }
            estilo = "-fx-background-color: green; -fx-background-radius: 30;";
           
        }else if(style1.contains("red") || style2.contains("red")){
            if (style1.contains("red")) {
                System.out.println("Boton 1: " + boton1.getId()); // BORRAR DESPUES
                cambiarParteIdBoton(boton1, 4, "negativa");
            }
            if (style2.contains("red")) {
                System.out.println("Boton 2: " + boton2.getId()); // BORRAR DESPUES
                cambiarParteIdBoton(boton2, 4, "negativa");
            }
            estilo = "-fx-background-color: red; -fx-background-radius: 30;";
        }else{
            estilo = "-fx-background-radius: 30;";
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
}
