package com.example.proyectodeprogramacion;

// Java version: 20
// IDE: IntelliJ IDEA 2022.1

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ControladorPantallaPrincipal {

    @FXML
    private Button mostrarLed, mostrarSwitch, agregarProtoboard, botonAgregaBateria, botonSwitch3X3, botonSwitch8x3, botonChip, botonDisplay;
    @FXML
    private AnchorPane pantallaPrincipal;
    @FXML
    private Button agregaResistencia;

    private VBox contenedorProtoboards; // Contenedor para las protoboards dinámicas

    private int contadorProtoboards = 0; // Contador global de IDs para las protoboards
    private List<ControladorProtoboard> listaProtoboards = new ArrayList<>(); // Lista de protoboards

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
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombre));
            Parent elemento = loader.load();

            // Configurar controlador si es una protoboard
            if (nombre.equals("protoboard.fxml")) {
                ControladorProtoboard controlador = loader.getController();

                // Asignar un ID único a la protoboard y registrarla en la lista
                controlador.setId(contadorProtoboards++);
                listaProtoboards.add(controlador);

                // Agregar elementos dinámicos a la protoboard
                agregarElementosDinamicos(controlador);

                System.out.println("Nueva protoboard creada con ID: " + controlador.getId());
                System.out.println("Cantidad actual de protoboards: " + listaProtoboards.size());
            } else if (nombre.equals("chip.fxml")) {
                // Si se está cargando un chip, establecer el tipo en el controlador
                ControladorChip controladorChip = loader.getController();

                // Configurar el tipo de chip
                if (tipo.equals("AND")) {
                    ((ChipAND) controladorChip).setTipoChip("AND");
                } else if (tipo.equals("OR")) {
                    ((ChipOR) controladorChip).setTipoChip("OR");
                } else if (tipo.equals("NOT")) {
                    ((ChipNOT) controladorChip).setTipoChip("NOT");
                }

                // Agregar el chip al contenedor principal
                pantallaPrincipal.getChildren().add(elemento);
            } else {
                // Para otros elementos que no son protoboards ni chips
                pantallaPrincipal.getChildren().add(elemento);
            }

            // Configurar posición inicial del elemento (opcional)
            elemento.setLayoutX(47);
            elemento.setLayoutY(100);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void agregarElementosDinamicos(ControladorProtoboard controlador) {
        try {
            // **Cargar y asociar un LED**
            FXMLLoader ledLoader = new FXMLLoader(getClass().getResource("led.fxml"));
            Parent led = ledLoader.load();
            ControladorLed controladorLed = ledLoader.getController();
            controladorLed.setProtoboard(controlador);
            controlador.getMainPane().getChildren().add(led);

            // Registrar el LED en la protoboard
            controlador.agregarElementoDinamico(controladorLed);

            // **Cargar y asociar una Resistencia**
            FXMLLoader resistenciaLoader = new FXMLLoader(getClass().getResource("resistencia.fxml"));
            Parent resistencia = resistenciaLoader.load();
            ControladorResistencia controladorResistencia = resistenciaLoader.getController();
            controladorResistencia.setProtoboard(controlador);
            controlador.getMainPane().getChildren().add(resistencia);

            // Registrar la resistencia en la protoboard
            controlador.agregarElementoDinamico(controladorResistencia);

            // **Cargar y asociar un Switch 3x3**
            FXMLLoader switch3x3Loader = new FXMLLoader(getClass().getResource("Switch3X3.fxml"));
            Parent switch3x3 = switch3x3Loader.load();
            ControladorSwitch3X3 controladorSwitch3X3 = switch3x3Loader.getController();
            controladorSwitch3X3.setProtoboard(controlador);
            controlador.getMainPane().getChildren().add(switch3x3);

            // Registrar el Switch 3x3 en la protoboard
            controlador.agregarElementoDinamico(controladorSwitch3X3);

            // **Cargar y asociar un Switch 8x3**
            FXMLLoader switch8x3Loader = new FXMLLoader(getClass().getResource("Switch8x3.fxml"));
            Parent switch8x3 = switch8x3Loader.load();
            ControladorSwitch8x3 controladorSwitch8x3 = switch8x3Loader.getController();
            controladorSwitch8x3.setProtoboard(controlador);
            controlador.getMainPane().getChildren().add(switch8x3);

            // Registrar el Switch 8x3 en la protoboard
            controlador.agregarElementoDinamico(controladorSwitch8x3);

            // **Cargar y asociar un Display**
            FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("Display.fxml"));
            Parent display = displayLoader.load();
            ControladorDisplay controladorDisplay = displayLoader.getController();
            controladorDisplay.setProtoboard(controlador);
            controlador.getMainPane().getChildren().add(display);

            // Registrar el Display en la protoboard
            controlador.agregarElementoDinamico(controladorDisplay);

            // **Cargar y asociar un Chip (AND, OR, NOT)**
            FXMLLoader chipLoader = new FXMLLoader(getClass().getResource("chip.fxml"));
            Parent chip = chipLoader.load();
            ControladorChip controladorChip = chipLoader.getController();
            controladorChip.setProtoboard(controlador);

            // Dependiendo del tipo de chip, puedes establecer su tipo
            if (tipo.equals("AND")) {
                ((ChipAND) controladorChip).setTipoChip("AND");
            } else if (tipo.equals("OR")) {
                ((ChipOR) controladorChip).setTipoChip("OR");
            } else if (tipo.equals("NOT")) {
                ((ChipNOT) controladorChip).setTipoChip("NOT");
            }

            controlador.getMainPane().getChildren().add(chip);

            // Registrar el Chip en la protoboard
            controlador.agregarElementoDinamico(controladorChip);

            // **Cargar y asociar una Batería (Motor)**
            FXMLLoader bateriaLoader = new FXMLLoader(getClass().getResource("motor.fxml"));
            Parent bateria = bateriaLoader.load();
            ControladorBateria controladorBateria = bateriaLoader.getController();
            controladorBateria.setProtoboard(controlador);
            controlador.getMainPane().getChildren().add(bateria);

            // Registrar la Batería en la protoboard
            controlador.agregarElementoDinamico(controladorBateria);

            // **Agregar más elementos si es necesario**
            // Aquí puedes cargar y asociar otros elementos adicionales
            // ...

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<ControladorProtoboard> getListaProtoboards() {
        return listaProtoboards;
    }
}