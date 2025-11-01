module com.example.programavimotechnologijosprif {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires java.naming;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires javafx.graphics;
    //requires javafx.graphics;

    opens com.example.programavimotechnologijosprif to javafx.fxml;
    exports com.example.programavimotechnologijosprif;
    exports com.example.programavimotechnologijosprif.fxControllers;
    opens com.example.programavimotechnologijosprif.fxControllers to javafx.fxml;
    opens com.example.programavimotechnologijosprif.model to org.hibernate.orm.core;
    exports com.example.programavimotechnologijosprif.model;

    //opens com.example.programavimotechnologijosprif.fxControllers to javafx.base;
}