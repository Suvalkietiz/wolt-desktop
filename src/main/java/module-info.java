module com.example.programavimotechnologijosprif {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;

    opens com.example.programavimotechnologijosprif to javafx.fxml;
    exports com.example.programavimotechnologijosprif;
    exports com.example.programavimotechnologijosprif.fxControllers to javafx.fxml;
    opens com.example.programavimotechnologijosprif.fxControllers to javafx.fxml;
}