package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public abstract class ControladorChip {

    @FXML
    private AnchorPane paneChip;

    @FXML
    private Rectangle pin1, pin2, pin3, pin4, pin5, pin6, pin7, pin8, pin9, pin10, pin11, pin12, pin13, pin14;

    @FXML
    private Button botonChip;   // Agregar referencia al botón "botonChip" en el FXML

    @FXML 
    private Text tipoChip;      //Se vera de manera visual que tipo de chip es

    private double offsetX, offsetY;
    private ControladorProtoboard protoboard;

    // Método para establecer el tipo de chip en la interfaz
    public void setTipoChip(String tipoChip) {
        this.tipoChip.setText(tipoChip);
    }

    @FXML
    public void initialize() {
        protoboard = VariablesGlobales.controladorProtoboard;

        //manejo de eventos de arrastre
        paneChip.setOnMousePressed(this::onMousePressed);
        paneChip.setOnMouseDragged(this::onMouseDragged);

        //Se ejecuta la subClase que corresponde
        ejecutarOperacion();
    }

    public void onMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - paneChip.getLayoutX();
        offsetY = event.getSceneY() - paneChip.getLayoutY();
    }

    public void onMouseDragged(MouseEvent event) {
        paneChip.setLayoutX(event.getSceneX() - offsetX);
        paneChip.setLayoutY(event.getSceneY() - offsetY);
        verificarConexionPins(); // Verificar las conexiones de los pines 7 y 14 después de mover el chip
    }

    public boolean verificarColorPin(Rectangle pin, Color colorEsperado) {
        GridPane[] gridPanes = { protoboard.getBusSuperior(), protoboard.getPistaSuperior(), protoboard.getPistaInferior() };
        Bounds pinBounds = pin.localToScene(pin.getBoundsInLocal());

        for (GridPane gridPane : gridPanes) {
            for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    Color colorBoton = obtenerColorBoton(button);
                    Bounds buttonBounds = button.localToScene(button.getBoundsInLocal());
                    if (pinBounds.intersects(buttonBounds) && colorBoton.equals(colorEsperado)) {
                        pin.setFill(colorEsperado);
                        return true;
                    }
                }
            }
        }
        pin.setFill(Color.BLACK);
        return false;
    }

    public Color obtenerColorBoton(Button button) {
        String buttonId = button.getId();
        String[] parts = buttonId.split("-");
        String carga = parts.length > 4 ? parts[4].trim() : "";

        switch (carga) {
            case "positiva":
                return Color.GREEN;
            case "negativa":
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }

    public void verificarConexionPins() {
        boolean pin7Correcto = verificarColorPin(pin7, Color.RED);
        boolean pin14Correcto = verificarColorPin(pin14, Color.GREEN);

        if (pin7Correcto && pin14Correcto) {
            System.out.println("Pin7 conectado a tierra y pin14 a corriente CORRECTAMENTE");
        } else {
            System.out.println("Pines 7 y 14 mal conectados");
        }
    }

    private void mostrarVentanaMensaje(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void cargarOperacion(String operacion) {
        // Lógica para manejar las operaciones AND, OR, NOT
        switch (operacion) {
            case "AND":
                System.out.println("Operación AND seleccionada");
                break;
            case "OR":
                System.out.println("Operación OR seleccionada");
                break;
            case "NOT":
                System.out.println("Operación NOT seleccionada");
                break;
            default:
                System.out.println("Operación no reconocida");
                break;
        }
    }

    // Método abstracto para ejecutar la operación lógica específica
    protected abstract void ejecutarOperacion();
}


