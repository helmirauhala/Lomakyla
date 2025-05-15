module org.example.lomakyla {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.lomakyla to javafx.fxml;
    exports org.example.lomakyla;
}