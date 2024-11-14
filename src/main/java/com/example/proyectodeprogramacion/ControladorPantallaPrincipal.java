package com.example.proyectodeprogramacion;

// Java version: 20
// IDE: IntelliJ IDEA 2022.1

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ControladorPantallaPrincipal {

    @FXML
    private Button mostrarLed, mostrarSwitch, agregarProtoboard, botonAgregaBateria, botonSwitch3X3, botonSwitch8x3, botonChip;
    @FXML
    private AnchorPane pantallaPrincipal;
    @FXML
    private Button agregaResistencia;

    @FXML
    public void initialize() {
        // Inicializar la variable estática
        VariablesGlobales.pantallaPrincipal = pantallaPrincipal;
        VariablesGlobales.aparecioBateria = false;

        botonAgregaBateria.setOnAction(event -> cargarInterfacezElementos("motor.fxml"));
        mostrarLed.setOnAction(event -> cargarInterfacezElementos("led.fxml"));
        agregarProtoboard.setOnAction(event -> cargarInterfacezElementos("protoboard.fxml"));
        botonSwitch3X3.setOnAction(event -> cargarInterfacezElementos("Switch3X3.fxml"));
        botonSwitch8x3.setOnAction(event -> cargarInterfacezElementos("Switch8x3.fxml"));
        agregaResistencia.setOnAction(event -> cargarInterfacezElementos("resistencia.fxml"));

        // Configurar el menú de opciones para el chip
        configurarMenuChip();
    }

    // Método que configura el menú de opciones para el botón "Chip"
    private void configurarMenuChip() {
        ContextMenu menuChip = new ContextMenu();

        // Crear opciones para el menú (AND, OR, NOT)
        MenuItem opcionAND = new MenuItem("AND");
        MenuItem opcionOR = new MenuItem("OR");
        MenuItem opcionNOT = new MenuItem("NOT");

        // Asignar acciones a cada opción del menú
        /*opcionAND.setOnAction(event -> cargarControladorChip("AND"));
        opcionOR.setOnAction(event -> cargarControladorChip("OR"));
        opcionNOT.setOnAction(event -> cargarControladorChip("NOT"));*/

        // Agregar las opciones al menú
        menuChip.getItems().addAll(opcionAND, opcionOR, opcionNOT);

        // Mostrar el menú al hacer clic derecho en el botón "Chip"
        botonChip.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                menuChip.show(botonChip, event.getScreenX(), event.getScreenY());
            }
        });
    }

    // Método que carga la interfaz y el controlador del chip seleccionado
    // Método que carga la interfaz del chip y pasa el nombre al controlador
   /* private void cargarControladorChip(String operacion) {
       try {
            // Cargar la interfaz del chip
            //cargarInterfacezElementos("chip.fxml");

            // Obtener el controlador y pasar la operación seleccionada
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("chip.fxml"));

            // Obtener el controlador y ejecutar la operación correspondiente
            ControladorChip controladorChip = loader.getController();
            controladorChip.cargarOperacion(operacion);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    // Método general para cargar interfaces de otros elementos
    private void cargarInterfacezElementos(String nombre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombre));
            Parent elemento = loader.load();
            elemento.setLayoutX(47);
            elemento.setLayoutY(100);
            pantallaPrincipal.getChildren().add(elemento);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
