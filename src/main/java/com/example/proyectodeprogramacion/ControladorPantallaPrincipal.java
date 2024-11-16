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
    private Button mostrarLed, mostrarSwitch, agregarProtoboard, botonAgregaBateria, botonSwitch3X3, botonSwitch8x3, botonChip, botonDisplay;
    @FXML
    private AnchorPane pantallaPrincipal;
    @FXML
    private Button agregaResistencia;

    @FXML
    public void initialize() {
        // Inicializar la variable estática
        VariablesGlobales.pantallaPrincipal = pantallaPrincipal;
        VariablesGlobales.aparecioBateria = false;

        botonAgregaBateria.setOnAction(event -> cargarInterfacezElementos("motor.fxml", "ninguno"));
        mostrarLed.setOnAction(event -> cargarInterfacezElementos("led.fxml", "ninguno"));
        agregarProtoboard.setOnAction(event -> cargarInterfacezElementos("protoboard.fxml", "ninguno"));
        botonSwitch3X3.setOnAction(event -> cargarInterfacezElementos("Switch3X3.fxml", "ninguno"));
        botonSwitch8x3.setOnAction(event -> cargarInterfacezElementos("Switch8x3.fxml", "ninguno"));
        agregaResistencia.setOnAction(event -> cargarInterfacezElementos("resistencia.fxml", "ninguno"));
        botonDisplay.setOnAction(event -> cargarInterfacezElementos("Display.fxml", "ninguno"));

        // Configurar el menú de opciones para el chip
        configurarMenuChip();
    }

    // Método que configura el menú de opciones para el botón "Chip"
    private void configurarMenuChip() {
        ChipAND chipAnd = new ChipAND();
        ContextMenu menuChip = new ContextMenu();

        // Crear opciones para el menú (AND, OR, NOT)
        MenuItem opcionAND = new MenuItem("AND");
        MenuItem opcionOR = new MenuItem("OR");
        MenuItem opcionNOT = new MenuItem("NOT");
        

        // Agregar las opciones al menú
        menuChip.getItems().addAll(opcionAND, opcionOR, opcionNOT);

        // Mostrar el menú al hacer clic derecho en el botón "Chip"
        botonChip.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                menuChip.show(botonChip, event.getScreenX(), event.getScreenY());
            }
        });

        //Manejamos cuando se selecciona chip AND
        opcionAND.setOnAction(event -> {
            cargarInterfacezElementos("chip.fxml", "AND");
        });

        opcionOR.setOnAction(event -> {
            cargarInterfacezElementos("chip.fxml", "OR");
        });

        opcionNOT.setOnAction(event -> {
            cargarInterfacezElementos("chip.fxml", "NOT");
        });
    }


    // Método general para cargar interfaces de otros elementos
    private void cargarInterfacezElementos(String nombre, String tipo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombre));

            //Asignamos el controlador manual, para que funcionen las subclases
            if (tipo.equals("AND")) {
                loader.setController(new ChipAND());
            }else if (tipo.equals("OR")) {
                loader.setController(new ChipOR());
            }else if (tipo.equals("NOT")) {
                loader.setController(new ChipNOT());
            }

            Parent elemento = loader.load();
            elemento.setLayoutX(47);
            elemento.setLayoutY(100);
            pantallaPrincipal.getChildren().add(elemento);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
