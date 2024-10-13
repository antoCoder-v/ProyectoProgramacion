package com.example.proyectodeprogramacion;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ControladorSwitch8x3 {

    @FXML
    private AnchorPane paneSwitch8x3;
    @FXML
    private RadioButton bot1, bot2, bot3, bot4, bot5, bot6, bot7, bot8;
    @FXML
    private Rectangle patSup1, patInf1, patSup2, patInf2, patSup3, patInf3;
    @FXML
    private Rectangle patSup4, patInf4, patSup5, patInf5, patSup6, patInf6;
    @FXML
    private Rectangle patSup7, patInf7, patSup8, patInf8;


    // Variables para almacenar la posición del mouse
    private double offsetX, offsetY;

    @FXML
    public void initialize() {

        EliminarElementos.habilitarEliminacion(paneSwitch8x3);

        // Maneja los eventos del mouse para arrastrar el chip
        paneSwitch8x3.setOnMousePressed(this::handleMousePressed);
        paneSwitch8x3.setOnMouseDragged(this::handleMouseDragged);

        // Asigna las acciones a cada botón
        bot1.setOnAction(event -> togglePatitas(patSup1, patInf1));
        bot2.setOnAction(event -> togglePatitas(patSup2, patInf2));
        bot3.setOnAction(event -> togglePatitas(patSup3, patInf3));
        bot4.setOnAction(event -> togglePatitas(patSup4, patInf4));
        bot5.setOnAction(event -> togglePatitas(patSup5, patInf5));
        bot6.setOnAction(event -> togglePatitas(patSup6, patInf6));
        bot7.setOnAction(event -> togglePatitas(patSup7, patInf7));
        bot8.setOnAction(event -> togglePatitas(patSup8, patInf8));
    }

    // Método para manejar el evento cuando se presiona el mouse
    private void handleMousePressed(MouseEvent event) {
        offsetX = event.getSceneX() - paneSwitch8x3.getLayoutX();
        offsetY = event.getSceneY() - paneSwitch8x3.getLayoutY();
    }

    // Método para manejar el evento cuando se arrastra el mouse
    private void handleMouseDragged(MouseEvent event) {
        paneSwitch8x3.setLayoutX(event.getSceneX() - offsetX);
        paneSwitch8x3.setLayoutY(event.getSceneY() - offsetY);
    }

    // Método para cambiar el estado de las patitas (activar/desactivar)
    private void togglePatitas(Rectangle patSup, Rectangle patInf) {
        if (patSup.getFill() == Color.DODGERBLUE && patInf.getFill() == Color.DODGERBLUE) {
            // Si ambas patitas están desactivadas, las activamos
            patSup.setFill(Color.LIMEGREEN);
            patInf.setFill(Color.LIMEGREEN);
        } else {
            // Si están activadas, las desactivamos
            patSup.setFill(Color.DODGERBLUE);
            patInf.setFill(Color.DODGERBLUE);
        }
    }
}
