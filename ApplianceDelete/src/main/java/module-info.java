module databases.databasescyrsach {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.naming;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.persistence;

    opens databases.databasesapplianceform to javafx.fxml;
    exports databases.databasesapplianceform;
    exports databases.databasesapplianceform.sql;
    opens databases.databasesapplianceform.sql to javafx.fxml;
}