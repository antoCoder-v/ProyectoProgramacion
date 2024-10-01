package com.example.proyectodeprogramacion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectodeprogramacion/PantallaPrincipal.fxml"));
            Parent root = loader.load();

            // Configurar la escena y el escenario
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Simulación de Protoboard");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
