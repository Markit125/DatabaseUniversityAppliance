package databases.databasesapplianceviewer;

import databases.databasesapplianceviewer.sql.ApplianceRow;
import databases.databasesapplianceviewer.sql.Department;
import databases.databasesapplianceviewer.sql.SQLProcessor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Label departmentLabel;
    @FXML
    private MenuButton departmentButton;
    @FXML
    private WebView webView;

    private SQLProcessor sqlProcessor;
    private List<String> subjectList;
    private List<String> departmentList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        departmentLabel.setText("Department name");
        subjectList = new ArrayList<>();
        departmentList = new ArrayList<>();
        sqlProcessor = new SQLProcessor();

        try {
            subjectList = sqlProcessor.getSubjects();
            departmentList = sqlProcessor.getDepartments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (String department : departmentList) {
            MenuItem menuItem = new MenuItem(department);
            menuItem.setOnAction((val) -> {
                departmentLabel.setText(menuItem.getText());
                printTable(menuItem.getText());
            });
            departmentButton.getItems().add(menuItem);
        }
    }

    private void printTable(String departmentName) {

        List<ApplianceRow> applianceRows = new ArrayList<>();
        Department department;

        try {
            applianceRows = sqlProcessor.getAppliancesDepartment(departmentList.indexOf(departmentName) + 1);
            department = sqlProcessor.getDepartment(departmentName, subjectList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TablePrint tablePrint = new TablePrint();

        webView.getEngine().loadContent(tablePrint.getTable(applianceRows, department), "text/html");
    }
}