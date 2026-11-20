module com.example.javafx_veterinariasegundointento {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.javafx_veterinariasegundointento.model to com.google.gson;
    opens com.example.javafx_veterinariasegundointento to javafx.fxml;
    exports com.example.javafx_veterinariasegundointento;
}