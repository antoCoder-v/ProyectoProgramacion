package com.example.proyectodeprogramacion;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class EliminarElementos {

    // Método estático para habilitar la eliminación con clic derecho en un nodo
    public static void habilitarEliminacion(Node elemento, ControladorElemento clase) {
        // Asignar un evento de clic derecho al nodo que se desea eliminar
        elemento.setOnMouseClicked(event -> handleRightClick(event, elemento, clase));
    }


    // Manejar el clic derecho para eliminar el nodo
    private static void handleRightClick(MouseEvent event, Node elemento, ControladorElemento clase) {
        if (event.getButton() == MouseButton.SECONDARY) { // Si es clic derecho
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Eliminar");
            MenuItem conectarItem = new MenuItem("Conectar");
            MenuItem desconectarItem = new MenuItem("Desconectar");
            MenuItem cambiarColorItem = new MenuItem("Cambiar Color");


            // Solo agregar la opción "Cambiar Color" si el elemento es un LED
            if (clase instanceof ControladorLed) {
                contextMenu.getItems().add(cambiarColorItem);
            }

            // Manejar el evento de eliminar
            deleteItem.setOnAction(e -> {
                Node parent = elemento.getParent(); // Obtener el contenedor padre
                if (parent instanceof AnchorPane) {
                    ((AnchorPane) parent).getChildren().remove(elemento); // Eliminar el elemento
                }
            });

            // Manejar el evento de cambiar color
            cambiarColorItem.setOnAction(e -> {
                if (clase instanceof ControladorLed) {
                    ((ControladorLed) clase).actualizarColor(); // Llama al método cambiarColor de la clase del LED
                }
            });

            // Agregar siempre el item de eliminar al menú
            contextMenu.getItems().add(deleteItem);

            // Mostrar el menú contextual
            contextMenu.show(elemento, event.getScreenX(), event.getScreenY());
        }
    }

}