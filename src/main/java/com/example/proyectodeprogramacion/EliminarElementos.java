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

    // Funcion que nos ayuda para ocupar diferentes clases con un solo llamado en
    // este caso (led, bateria, swicth, etc)
    public boolean manejarElemento(ControladorElemento elemento) {
        if (elemento.isConectado()) {
            return false;
        } else {
            elemento.setConectado(true);
            return true;
        }
    }

    // Manejar el clic derecho para eliminar el nodo
    private static void handleRightClick(MouseEvent event, Node elemento, ControladorElemento clase) {

        if (event.getButton() == MouseButton.SECONDARY) { // Si es clic derecho
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Eliminar");
            MenuItem conectarItem = new MenuItem("Conectar");
            MenuItem desconectarItem = new MenuItem("Desconectar");

            // Alternar las opciones del menú según el estado del nodo
            if (clase.isConectado()) { // --->Verificamos si el elemento esta conectado
                contextMenu.getItems().add(desconectarItem);
            } else if (clase.EncimaDeProtoboard()) { // --->solo se conecta si el elemento esta encima de la protoboard
                contextMenu.getItems().add(conectarItem);
            }

            // Manejar el evento de eliminar
            deleteItem.setOnAction(e -> {
                Node parent = elemento.getParent(); // Obtener el contenedor padre
                if (parent instanceof AnchorPane) {
                    ((AnchorPane) parent).getChildren().remove(elemento); // Eliminar el elemento
                }
            });

            // Manejar el evento de conectar
            conectarItem.setOnAction(e -> {
                // elemento.getProperties().put("conectado", true);
                System.out.println("Elemento conectado");
                clase.setConectado(true);
            });

            // Manejar el evento de desconectar
            desconectarItem.setOnAction(e -> {
                // elemento.getProperties().put("conectado", false);
                System.out.println("Elemento desconectado");
                clase.setConectado(false);
            });

            contextMenu.getItems().add(deleteItem);
            contextMenu.show(elemento, event.getScreenX(), event.getScreenY());
        }
    }
}