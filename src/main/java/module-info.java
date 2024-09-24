module com.example.proyectodeprogramacion {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.proyectodeprogramacion to javafx.fxml;
    exports com.example.proyectodeprogramacion;
}