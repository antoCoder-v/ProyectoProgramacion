package com.example.proyectodeprogramacion;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;

public class ControladorProtoboard {

    @FXML
    private GridPane busSuperior, pistaSuperior, busInferior, pistaInferior;

    @FXML
    private Pane mainPane;

    @FXML
    private AnchorPane PantallaProtoboard;

    // Clases externas
    private Cables cableManager;

    private double offsetX, offsetY; // variables para almacenar la posición del mouse

    public GridPane getBusSuperior() {
        return busSuperior;
    }

    public GridPane getPistaSuperior() {
        return pistaSuperior;
    }

    public GridPane getBusInferior() {
        return busInferior;
    }

    public GridPane getPistaInferior() {
        return pistaInferior;
    }

    @FXML
    public void initialize() {
        VariablesGlobales.controladorProtoboard = this;

        // Habilitar eliminación con clic derecho para la protoboard
        // EliminarElementos.habilitarEliminacion(PantallaProtoboard);

        // Agregar botones a busSuperior
        agregarBotonesGridPane(busSuperior, "busSuperior");

        // Agregar botones a pistaSuperior
        agregarBotonesGridPane(pistaSuperior, "pistaSuperior");

        // Agregar botones a busInferior
        agregarBotonesGridPane(busInferior, "busInferior");

        // Agregar botones a pistaInferior
        agregarBotonesGridPane(pistaInferior, "pistaInferior");

        // Manejamos los movimientos del mouse en el paneBateria
        PantallaProtoboard.setOnMousePressed(this::handleMousePressed);
        PantallaProtoboard.setOnMouseDragged(this::handleMouseDragged);

        cableManager = new Cables(busSuperior, pistaSuperior, busInferior, pistaInferior);
        VariablesGlobales.cables = cableManager;

        // Verifica cambios en tiempo real
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                cableManager.actualizarCorrienteTodos();
            }
        };
        timer.start();
    }

    // Método que recorre un GridPane y añade botones en cada celda
    private void agregarBotonesGridPane(GridPane gridPane, String tipo) {
        int rows = gridPane.getRowConstraints().size();
        int columns = gridPane.getColumnConstraints().size();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Button button = new Button();
                // Tamaño del botón
                button.setMinSize(15, 15);
                button.setMaxSize(15, 15);
                button.setStyle("-fx-background-radius: 30;");

                // Asignar un ID basado en la posición button + tipo + row + col + carga +
                // cableConectado
                button.setId("Button -" + tipo + "-" + row + "-" + col + "-0-desconectado"); // carga "0 y no" por
                                                                                             // defecto

                // manejamos el clic a la protoboard
                button.setOnAction(event -> {
                    onButtonClicked(button, tipo);
                });

                // Agregar botón a la celda
                gridPane.add(button, col, row);
            }
        }
    }

    // Método para manejar cuando se hace clic en un botón de la protoboard
    public void onButtonClicked(Button button, String tipo) {
        int row = GridPane.getRowIndex(button);
        // Verificamos que la bateria se conecte correctamente
        if (VariablesGlobales.botonPresionadoBateria != null && tipo.equals("busSuperior")) {
            if (VariablesGlobales.botonPresionadoBateria.getStyle().contains("green") && row == 0
                    || VariablesGlobales.botonPresionadoBateria.getStyle().contains("red") && row == 1) {
                AudioClip explosionSound = new AudioClip(
                        getClass().getResource("/Audio/explosion.wav").toExternalForm());
                explosionSound.play();
                mostrarVentanaMensaje("LA BATERIA SE HA SOBRECALENTADO HASTA EXPLOTAR", "ERROR DE EXPLOSION");
                // Reiniciar el botón presionado de la batería
                VariablesGlobales.botonPresionadoBateria = null;
                VariablesGlobales.aparecioBateria = false;
                return; // Detener más acciones
            }
        }

        // Si no hay un botón de inicio configurado, configúralo
        if (VariablesGlobales.aparecioBateria) {
            cableManager.setButtonStart(VariablesGlobales.botonPresionadoBateria);
            cableManager.setButtonEndAndDrawCable(button);
            VariablesGlobales.aparecioBateria = false;
            VariablesGlobales.botonPresionadoBateria = null;
        } else if (cableManager.getButtonStart() == null) {
            cableManager.setButtonStart(button);
        } else {
            // Si ya hay un botón de inicio, configúralo como final y dibuja el cable
            cableManager.setButtonEndAndDrawCable(button);

        }
    }

    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - PantallaProtoboard.getLayoutX();
        offsetY = event.getSceneY() - PantallaProtoboard.getLayoutY();
    }

    private void handleMouseDragged(MouseEvent event) {
        PantallaProtoboard.setLayoutX(event.getSceneX() - offsetX);
        PantallaProtoboard.setLayoutY(event.getSceneY() - offsetY);
    }

    // Método para mostrar una ventana de mensaje
    private void mostrarVentanaMensaje(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}