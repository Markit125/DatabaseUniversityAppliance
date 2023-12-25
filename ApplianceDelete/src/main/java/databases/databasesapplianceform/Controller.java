package databases.databasesapplianceform;

import databases.databasesapplianceform.sql.SQLProcessor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField fieldPassport;
    @FXML
    private Label resultLabel;
    private final long PASSPORT_MAX = 9_999_999_999L;
    private final long PASSPORT_MIN = 1_000_000_000L;

    private SQLProcessor sqlProcessor;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sqlProcessor = new SQLProcessor();
        resultLabel.setText("");
    }

    @FXML
    protected void onDeleteButtonClick() {

        resultLabel.setText("");

        if (!checkAllFields()) {
            resultLabel.setText("Wrong passport!");
            return;
        }

        int id;
        try {
            id = sqlProcessor.getStudentId(fieldPassport.getText());
        } catch (SQLException e) {
            resultLabel.setText("Such student doesn't exist!");
            return;
        }

        sqlProcessor.deleteStudentFromAppliance(id);
        sqlProcessor.deleteStudentFromResult(id);
        sqlProcessor.deleteStudentFromStudent(id);

        resultLabel.setText("Deleted successfully!");
    }

    private boolean checkAllFields() {

        boolean isFieldValid;
        FieldValidator validator = new FieldValidator();

        isFieldValid = validator.isNumberValid(fieldPassport.getText().replaceAll("\\s", ""),
                PASSPORT_MIN, PASSPORT_MAX);
        colorField(fieldPassport, isFieldValid);

        return isFieldValid;
    }

    private void colorField(TextField field, boolean correct) {
        field.setStyle(String.format("-fx-text-fill: %s;", correct ? "green" : "red"));
    }
}