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
            MenuItem cambiarColorItem = new MenuItem("Cambiar Color");

            // Alternar las opciones del menú según el estado del nodo
            if (clase.isConectado()) { // Verificamos si el elemento está conectado
                contextMenu.getItems().add(desconectarItem);
            } else if (clase.EncimaDeProtoboard()) { // Solo se conecta si el elemento está encima de la protoboard
                contextMenu.getItems().add(conectarItem);
            }

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

            // Manejar el evento de conectar
            conectarItem.setOnAction(e -> {
                System.out.println("Elemento conectado");
                clase.setConectado(true);
            });

            // Manejar el evento de cambiar color
            cambiarColorItem.setOnAction(e -> {
                if (clase instanceof ControladorLed) {
                    ((ControladorLed) clase).cambiarColor(); // Llama al método cambiarColor de la clase del LED
                }
            });

            // Manejar el evento de desconectar
            desconectarItem.setOnAction(e -> {
                System.out.println("Elemento desconectado");
                clase.setConectado(false);
            });

            // Agregar siempre el item de eliminar al menú
            contextMenu.getItems().add(deleteItem);

            // Mostrar el menú contextual
            contextMenu.show(elemento, event.getScreenX(), event.getScreenY());
        }
    }

}