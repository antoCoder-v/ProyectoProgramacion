package com.example.proyectodeprogramacion;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class EliminarElementos {

    // Método estático para habilitar la eliminación con clic derecho en un nodo
    public static void habilitarEliminacion(Node elemento) {
        // Asignar un evento de clic derecho al nodo que se desea eliminar
        elemento.setOnMouseClicked(event -> handleRightClick(event, elemento));
    }

    // Manejar el clic derecho para eliminar el nodo
    private static void handleRightClick(MouseEvent event, Node elemento) {
        if (event.getButton() == MouseButton.SECONDARY) { // Si es clic derecho
            Node parent = elemento.getParent(); // Obtener el contenedor padre
            if (parent instanceof AnchorPane) {
                ((AnchorPane) parent).getChildren().remove(elemento); // Eliminar el elemento
            }
        }
    }
}