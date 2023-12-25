module databases.databasesapplianceviewer.databasesapplianceviewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.naming;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.postgresql.jdbc;
    requires java.sql;

    opens databases.databasesapplianceviewer to javafx.fxml;
    exports databases.databasesapplianceviewer;
}