package com.example.proyectodeprogramacion;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ChipAND extends ControladorChip{
    @FXML
    protected AnchorPane paneChip;

    @Override
    protected void ejecutarOperacion() {
        System.out.println("Ejecutando operación AND");

        //Logica de la operacion AND
        setTipoChip("AND");
        // Lógica de la operación AND en las cuatro compuertas
        verificarOperacionesAnd();
    }

    // Método para verificar las operaciones lógicas AND en las 4 compuertas
    private void verificarOperacionesAnd() {
        verificarCompuertaAnd(pin1, pin2, pin3); // Compuerta 1
        verificarCompuertaAnd(pin4, pin5, pin6); // Compuerta 2
        verificarCompuertaAnd(pin10, pin9, pin8); // Compuerta 3
        verificarCompuertaAnd(pin13, pin12, pin11); // Compuerta 4
    }

    // Método para verificar una compuerta AND individual
    private void verificarCompuertaAnd(Rectangle entrada1, Rectangle entrada2, Rectangle salida) {
        boolean entrada1Activa = verificarColorPin(entrada1, Color.GREEN);
        boolean entrada2Activa = verificarColorPin(entrada2, Color.GREEN);

        if (entrada1Activa && entrada2Activa) {
            salida.setFill(Color.GREEN); // Salida en verde si ambas entradas son verdes
            System.out.println("Salida AND activa en " + salida.getId() + ": ambas entradas son verdaderas");
            // Propagar corriente a los botones conectados a la salida
            propagarCorriente(salida, Color.GREEN);
        } else {
            salida.setFill(Color.BLACK); // Salida en negro si alguna entrada es falsa
            System.out.println("Salida AND inactiva en " + salida.getId() + ": alguna entrada es falsa");
        }
    }
}
