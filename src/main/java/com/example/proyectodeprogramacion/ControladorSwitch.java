package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.geometry.Bounds;

import java.util.ArrayList;
import java.util.List;

public class ControladorSwitch {

    @FXML
    private AnchorPane switchPane; // Contiene todo el contenido del switch, incluyendo el Rectangle y los Circles

    @FXML
    private Pane mainPane; // Asegúrate de que el fx:id de tu Pane en el archivo FXML coincida

    @FXML
    private Rectangle rectangle; // Rectángulo dentro del switchPane

    @FXML
    private Circle circleCenter,circle1,circle2,circle3,circle4; // Círculo dentro del switchPane

    @FXML
    private Button encender;

    private double offsetX,offsetY;
    private ControladorProtoboard protoboard;
    private boolean pasoCorrienteSwitch = false;


    public ControladorSwitch() {
    }

    @FXML
    public void initialize() {
        EliminarElementos.habilitarEliminacion(switchPane);
        protoboard = VariablesGlobales.controladorProtoboard;
        //VariablesGlobales.controladorSwitch = this;
        // Configura los eventos de arrastre para el switchPane
        switchPane.setOnMousePressed(this::handleMousePressed);
        switchPane.setOnMouseDragged(this::handleMouseDragged);

        // Configura los eventos de arrastre para cada círculo (opcional si se necesita mover individualmente)
        circle1.setOnMousePressed(this::handleMousePressed);
        circle1.setOnMouseDragged(this::handleMouseDragged);

        circle2.setOnMousePressed(this::handleMousePressed);
        circle2.setOnMouseDragged(this::handleMouseDragged);

        circle3.setOnMousePressed(this::handleMousePressed);
        circle3.setOnMouseDragged(this::handleMouseDragged);

        circle4.setOnMousePressed(this::handleMousePressed);
        circle4.setOnMouseDragged(this::handleMouseDragged);

        circleCenter.setOnMousePressed(this::handleMousePressed);
        circleCenter.setOnMouseDragged(this::handleMouseDragged);

        // Configura el evento al presionar el botón "Encender"
        encender.setOnAction(event -> verificarPosicion(protoboard));
    }

    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - switchPane.getLayoutX();
        offsetY = event.getSceneY() - switchPane.getLayoutY();
    }

    private void handleMouseDragged(MouseEvent event) {
        switchPane.setLayoutX(event.getSceneX() - offsetX);
        switchPane.setLayoutY(event.getSceneY() - offsetY);
    }

    // Método para verificar si el switch está correctamente colocado sobre 4 botones
    public void verificarPosicion(ControladorProtoboard protoboard) {
        pasoCorrienteSwitch = false;
        VariablesGlobales.corrienteSwitch = pasoCorrienteSwitch;
        System.out.println("Valor de pasoCorrienteSwitch: " + pasoCorrienteSwitch); // Verificar cambio
        if (protoboard == null) {
            System.out.println("Protoboard no está asignado.");
            return;
        }

        double switchX = switchPane.getLayoutX();
        double switchY = switchPane.getLayoutY();

        // Lista para almacenar los botones debajo del switch
        List<Button> botonesDebajoSwitch = new ArrayList<>();

        verificarEnGridPane(protoboard.getBusSuperior(), switchX, switchY, botonesDebajoSwitch);
        verificarEnGridPane(protoboard.getPistaSuperior(), switchX, switchY, botonesDebajoSwitch);
        verificarEnGridPane(protoboard.getBusInferior(), switchX, switchY, botonesDebajoSwitch);
        verificarEnGridPane(protoboard.getPistaInferior(), switchX, switchY, botonesDebajoSwitch);

        if (botonesDebajoSwitch.size() == 4) {
            mostrarVentanaMensaje("El SWITCH fue conectado correctamente","Conexiòn");
            //System.out.println("Switch correctamente colocado sobre 4 botones.");
            verificarCorriente(botonesDebajoSwitch); // Verificar corriente automáticamente
            //System.out.println("Valor de pasoCorrienteSwitch en verificarPosicion: " + pasoCorrienteSwitch); // Verificar cambio
        } else {
            mostrarError("El switch debe estar sobre 4 botones.");
        }
    }

    // Método auxiliar para iterar sobre los botones de un GridPane
    private void verificarEnGridPane(GridPane gridPane, double switchX, double switchY, List<Button> botonesDebajoSwitch) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;

                // Obtener los límites en escena de ambos elementos para una comparación precisa
                Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                Bounds switchBounds = switchPane.localToScene(switchPane.getBoundsInLocal());

                // Verifica si los límites del switch intersectan con los límites del botón
                if (switchBounds.intersects(buttonBounds)) {
                    System.out.println("Switch sobre: " + button.getId());
                    botonesDebajoSwitch.add(button); // Agregar botón a la lista
                }
            }
        }
        System.out.println("----------------------------------");
    }

    // Método para verificar si los botones reciben corriente
    private void verificarCorriente(List<Button> botonesDebajoSwitch) {
        boolean tieneCorrientePositiva = false;
        boolean tieneCorrienteNegativa = false;

        for (Button boton : botonesDebajoSwitch) {
            if (boton.getStyle().contains("green")) {
                tieneCorrientePositiva = true;
            } else if (boton.getStyle().contains("red")) {
                tieneCorrienteNegativa = true;
            }
        }

        if (tieneCorrientePositiva && tieneCorrienteNegativa) {
            System.out.println("El switch está recibiendo corriente correctamente.");
            pasoCorrienteSwitch = true;
            VariablesGlobales.corrienteSwitch = pasoCorrienteSwitch;
        } else {
            mostrarError("El switch no está recibiendo corriente correctamente.");
        }
    }
    // Método para mostrar un mensaje de error
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    private void mostrarVentanaMensaje(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}