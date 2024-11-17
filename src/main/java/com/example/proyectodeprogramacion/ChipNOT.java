package com.example.proyectodeprogramacion;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ChipNOT extends ControladorChip{
    @FXML
    protected AnchorPane paneChip;

    @Override
    protected void ejecutarOperacion() {

        //Logica de la operacion NOT
        setTipoChip("NOT");
        verificarOperacionesNOT();
    }
    private void verificarOperacionesNOT() {
        verificarCompuertaNOT(pin1, pin2); // Compuerta 1
        verificarCompuertaNOT(pin3, pin4); // Compuerta 2
        verificarCompuertaNOT(pin5, pin6); // Compuerta 3
        verificarCompuertaNOT(pin9, pin8); // Compuerta 4
        verificarCompuertaNOT(pin11, pin10); //Compuesta 5
        verificarCompuertaNOT(pin13, pin12); //Compuerta 6
    }

    // Método para verificar una compuerta AND individual
    private void verificarCompuertaNOT(Rectangle entrada1, Rectangle salida) {
        boolean entrada1Activa = verificarColorPin(entrada1, Color.GREEN);

        if (!entrada1Activa)  {
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