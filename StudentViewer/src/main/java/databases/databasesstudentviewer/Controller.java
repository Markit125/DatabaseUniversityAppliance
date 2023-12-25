package databases.databasesstudentviewer;

import databases.databasesstudentviewer.sql.StudentRow;
import databases.databasesstudentviewer.sql.SQLProcessor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Label departmentLabel;
    @FXML
    private MenuButton sortByButton;
    @FXML
    private WebView webView;
    @FXML
    private CheckBox reversedCheckbox;
    @FXML
    private MenuButton departmentButton;
    @FXML
    private TextField sumLeftBound;
    @FXML
    private TextField sumRightBound;

    private SQLProcessor sqlProcessor;
    private List<String> subjectList;
    private List<String> departmentList;
    List<StudentRow> studentRows;

    private Comparator<StudentRow> comparator;
    private String html;
    private final String DOWNLOADS_FOLDER = "downloads/";


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        html = "";
        studentRows = new ArrayList<>();
        subjectList = new ArrayList<>();
        departmentList = new ArrayList<>();
        sqlProcessor = new SQLProcessor();
        comparator = StudentRow.comparatorByLastName();

        try {
            subjectList = sqlProcessor.getSubjects();
            departmentList = sqlProcessor.getDepartments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        initializeSortByButton();
        initializeFilterDepartmentButton();

        printTable();
    }

    private void initializeFilterDepartmentButton() {
        for (String name : departmentList) {
            MenuItem item = new MenuItem("✔ " + name);
            departmentButton.getItems().add(item);
            item.setOnAction((val) -> {
                if (item.getText().startsWith("✔")) {
                    item.setText(item.getText().substring(2));
                } else {
                    item.setText("✔ " + item.getText());
                }
                printTable();
            });
        }
    }

    private void initializeSortByButton() {

        MenuItem item = new MenuItem("First Name");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorByFirstName();
            sortByButton.setText("Sorted by First Name");
            printTable();
        });
        sortByButton.getItems().add(item);

        item = new MenuItem("Middle Name");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorByMiddleName();
            sortByButton.setText("Sorted by Middle Name");
            printTable();
        });
        sortByButton.getItems().add(item);

        item = new MenuItem("Last Name");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorByLastName();
            sortByButton.setText("Sorted by Last Name");
            printTable();
        });
        sortByButton.getItems().add(item);

        item = new MenuItem("Department");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorByDepartment();
            sortByButton.setText("Sorted by Department");
            printTable();
        });
        sortByButton.getItems().add(item);

        item = new MenuItem("Point Sum");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorBySum();
            sortByButton.setText("Sorted by Point Sum");
            printTable();
        });
        sortByButton.getItems().add(item);

        item = new MenuItem("Birth Date");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorByBirthDate();
            sortByButton.setText("Sorted by Birth Date");
            printTable();
        });
        sortByButton.getItems().add(item);

        item = new MenuItem("Result 1");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorByResult1();
            sortByButton.setText("Sorted by Result 1");
            printTable();
        });
        sortByButton.getItems().add(item);

        item = new MenuItem("Result 2");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorByResult2();
            sortByButton.setText("Sorted by Result 2");
            printTable();
        });
        sortByButton.getItems().add(item);

        item = new MenuItem("Result 3");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorByResult3();
            sortByButton.setText("Sorted by Result 3");
            printTable();
        });
        sortByButton.getItems().add(item);

        item = new MenuItem("Achievements");
        item.setOnAction((val) -> {
            comparator = StudentRow.comparatorByAchievements();
            sortByButton.setText("Sorted by Achievements");
            printTable();
        });
        sortByButton.getItems().add(item);

    }

    @FXML
    private void createPDFReport() throws IOException {

        File dir = new File(DOWNLOADS_FOLDER);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(DOWNLOADS_FOLDER + "report.html");

        Writer writer = new FileWriter(file);
        writer.write(html);
        writer.close();
    }

    @FXML
    private void printTable() {

        if (studentRows.isEmpty()) {
            try {
                studentRows = sqlProcessor.getAllStudentInfo(subjectList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (reversedCheckbox.isSelected()) {
            studentRows.sort(comparator.reversed());
        } else {
            studentRows.sort(comparator);
        }

        List<StudentRow> filteredStudentRows = filterRows(studentRows);


        TablePrint tablePrint = new TablePrint();
        html = tablePrint.getTable(filteredStudentRows);
        webView.getEngine().loadContent(html, "text/html");
    }

    private List<StudentRow> filterRows(List<StudentRow> studentRows) {

        List<StudentRow> filtered = new ArrayList<>();

        ObservableList<MenuItem> departmentFilteredList = departmentButton.getItems();
        List<String> departmentsNeed = new ArrayList<>();

        for (MenuItem item : departmentFilteredList) {
            if (item.getText().startsWith("✔ ")) {
                departmentsNeed.add(item.getText().substring(2));
            }
        }


        int leftBound = 0;
        int rightBound = 310;

        try {
            leftBound = Integer.parseInt(sumLeftBound.getText());
        } catch (NumberFormatException ignored) {}

        try {
            rightBound = Integer.parseInt(sumRightBound.getText());
        } catch (NumberFormatException ignored) {}


        for (StudentRow row : studentRows) {

            if (departmentsNeed.contains(row.departmentApplied)
                    && (leftBound <= row.result1 + row.result2 + row.result3 + row.achievements)
                    && (row.result1 + row.result2 + row.result3 + row.achievements <= rightBound)
            ) {
                filtered.add(row);
            }
        }

        return filtered;
    }
}