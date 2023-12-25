package databases.databasesapplianceform;

import databases.databasesapplianceform.sql.Appliance;
import databases.databasesapplianceform.sql.StudentForm;
import databases.databasesapplianceform.sql.ExamResult;
import databases.databasesapplianceform.sql.SQLProcessor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class Controller implements Initializable {

    @FXML
    private TextField fieldFirstName;
    @FXML
    private TextField fieldMiddleName;
    @FXML
    private TextField fieldLastName;
    @FXML
    private TextField fieldBirthDate;
    @FXML
    private TextField fieldAchievements;
    @FXML
    private TextField fieldPassport;
    @FXML
    private TextField fieldResult1;
    @FXML
    private TextField fieldResult2;
    @FXML
    private TextField fieldResult3;
    @FXML
    private Label examLabel1;
    @FXML
    private Label examLabel2;
    @FXML
    private Label examLabel3;
    @FXML
    private MenuButton departmentButton;
    @FXML
    private Label errorLabel;

    private final int ZERO = 0;
    private final int ACHIEVEMENT_MAX = 10;
    private final int RESULT_MAX = 100;
    private final long PASSPORT_MAX = 9_999_999_999L;
    private final long PASSPORT_MIN = 1_000_000_000L;

    private SQLProcessor sqlProcessor;
    private List<String> subjectList;
    private List<String> departmentList;

    private String departmentButtonStyle;
    private final String DOWNLOADS_FOLDER = "downloads/";
    private final String FONT = "src/main/resources/databases/databasesapplianceform/Roboto-Black.ttf";
    private final String PRINT = "src/main/resources/databases/databasesapplianceform/print.png";


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        subjectList = new ArrayList<>();
        departmentList = new ArrayList<>();
        sqlProcessor = new SQLProcessor();
        departmentButtonStyle = departmentButton.getStyle();

        try {
            subjectList = sqlProcessor.getSubjects();
            departmentList = sqlProcessor.getDepartments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (String department : departmentList) {
            MenuItem menuItem = new MenuItem(department);
            menuItem.setOnAction((val) -> {
                departmentButton.setText(menuItem.getText());
                setExamSubjects(menuItem.getText());
            });
            departmentButton.getItems().add(menuItem);
        }
    }

    private void setExamSubjects(String departmentName) {

        try {
            int[] subjectIds = sqlProcessor.getSubjectIds(departmentName);
            examLabel1.setText(subjectList.get(subjectIds[0] - 1));
            examLabel2.setText(subjectList.get(subjectIds[1] - 1));
            examLabel3.setText(subjectList.get(subjectIds[2] - 1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onSubmitButtonClick() {

        // fillFields();

        errorLabel.setText("");

        if (!checkAllFields()) {
            errorLabel.setText("Resolve wrong fields!");
            return;
        }


        StudentForm studentForm = makeStudentForm();
        try {
            sqlProcessor.insertStudent(studentForm);
            sqlProcessor.insertResults(makeExamResult(studentForm));
            sqlProcessor.insertAppliance(makeAppliance(studentForm));

        } catch (SQLException e) {
            errorLabel.setText("Such student already exists!");
            System.out.println(e);
            return;
        }

        try {
            sqlProcessor.printTable();
        } catch (SQLException e) {
            System.out.println(e);
        }

        errorLabel.setText("Submitted successfully!");

        try {
            createNotice(studentForm);
        } catch (URISyntaxException | IOException e) {
            System.out.println(e);
        }
    }

    private void createNotice(StudentForm studentForm) throws IOException, URISyntaxException {

        File dir = new File(DOWNLOADS_FOLDER);
        if (!dir.exists()) {
            dir.mkdir();
        }

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.setFont(PDType0Font.load(document, new FileInputStream(FONT), false), 14);

        contentStream.beginText();

        contentStream.newLineAtOffset(285, 550);
        String notice = "СПРАВКА";
        contentStream.showText(notice);

        contentStream.newLineAtOffset(-150, -50);
        notice = "Настоящим подтверждаю, что " + studentForm.lastName + " " +
                studentForm.firstName + " " + studentForm.middleName;
        contentStream.showText(notice);

        contentStream.newLineAtOffset(-70, -20);
        notice = "подал(а) заявление о намерении участвовать в конкурсе на поступление в";
        contentStream.showText(notice);

        contentStream.newLineAtOffset(160, -20);
        notice = studentForm.department + " университета ВУЗ.";
        contentStream.showText(notice);

        contentStream.newLineAtOffset(220, -370);
        notice = "_________________";
        contentStream.showText(notice);

        contentStream.endText();



        Path path = Paths.get(getClass().getResource("print.png").toURI());
        PDImageXObject image = PDImageXObject.createFromFile(path.toAbsolutePath().toString(), document);


        image.setWidth(100);
        image.setHeight(100);
        contentStream.drawImage(image, 450, 70);

//        path = Paths.get(getClass().getResource("print.png").toURI());
//        Path pathSignature = Paths.get(getClass().getResource("signature.png").toURI());
//        PDImageXObject imageSignature = PDImageXObject.createFromFile(pathSignature.toAbsolutePath().toString(), document);
//
//        imageSignature.setWidth(40);
//        imageSignature.setHeight(40);
////        imageSignature.se
//
//        contentStream.drawImage(imageSignature, 100, 100);


        contentStream.close();




        document.save(DOWNLOADS_FOLDER + "notice.pdf");
        document.close();
    }

    private void fillFields() {

        fieldFirstName.setText("William");
        fieldMiddleName.setText("Grace");
        fieldLastName.setText("Reed");

        fieldBirthDate.setText("30.12.2003");
        fieldAchievements.setText("8");
        fieldPassport.setText("1762523941");

        fieldResult1.setText("90");
        fieldResult2.setText("66");
        fieldResult3.setText("93");
    }

    private StudentForm makeStudentForm() {

        StudentForm studentForm = new StudentForm();

        studentForm.firstName = fieldFirstName.getText().replaceAll("\\s", "");
        studentForm.middleName = fieldMiddleName.getText().replaceAll("\\s", "");
        studentForm.lastName = fieldLastName.getText().replaceAll("\\s", "");

        try {
            studentForm.birthDate = new SimpleDateFormat("dd.MM.yyyy").parse(fieldBirthDate.getText());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        studentForm.achievements = Integer.parseInt(fieldAchievements.getText().replaceAll("\\s", ""));
        studentForm.passport = fieldPassport.getText().replaceAll("\\s", "");

        studentForm.examSubject1 = examLabel1.getText();
        studentForm.examSubject2 = examLabel2.getText();
        studentForm.examSubject3 = examLabel3.getText();

        studentForm.result1 = Integer.parseInt(fieldResult1.getText().replaceAll("\\s", ""));
        studentForm.result2 = Integer.parseInt(fieldResult2.getText().replaceAll("\\s", ""));
        studentForm.result3 = Integer.parseInt(fieldResult3.getText().replaceAll("\\s", ""));

        studentForm.department = departmentButton.getText();

        return studentForm;
    }

    private List<ExamResult> makeExamResult(StudentForm studentForm) {

        List<ExamResult> examResults = new ArrayList<>();

        ExamResult examResult = new ExamResult();
        examResult.studentId = studentForm.studentId;
        examResult.score = studentForm.result1;
        examResult.examSubjectId = subjectList.indexOf(studentForm.examSubject1) + 1;
        examResults.add(examResult);

        examResult = new ExamResult();
        examResult.studentId = studentForm.studentId;
        examResult.score = studentForm.result2;
        examResult.examSubjectId = subjectList.indexOf(studentForm.examSubject2) + 1;
        examResults.add(examResult);

        examResult = new ExamResult();
        examResult.studentId = studentForm.studentId;
        examResult.score = studentForm.result3;
        examResult.examSubjectId = subjectList.indexOf(studentForm.examSubject3) + 1;
        examResults.add(examResult);

        return examResults;
    }

    private Appliance makeAppliance(StudentForm studentForm) {

        Appliance appliance = new Appliance();

        appliance.studentId = studentForm.studentId;
        appliance.departmentId = departmentList.indexOf(studentForm.department) + 1;
        appliance.examSubjectId1 = subjectList.indexOf(studentForm.examSubject1) + 1;
        appliance.examSubjectId2 = subjectList.indexOf(studentForm.examSubject2) + 1;
        appliance.examSubjectId3 = subjectList.indexOf(studentForm.examSubject3) + 1;

        return appliance;
    }

    private boolean checkAllFields() {

        boolean valid = true;
        boolean isFieldValid;
        FieldValidator validator = new FieldValidator();

        for (TextField field : Arrays.asList(fieldFirstName, fieldMiddleName, fieldLastName)) {
            isFieldValid = validator.isNameValid(field.getText());
            colorField(field, isFieldValid);
            valid = isFieldValid && valid;
        }

        isFieldValid = validator.isDateValid(fieldBirthDate.getText());
        colorField(fieldBirthDate, isFieldValid);
        valid = isFieldValid && valid;


        isFieldValid = validator.isNumberValid(fieldAchievements.getText(), ZERO, ACHIEVEMENT_MAX);
        colorField(fieldAchievements, isFieldValid);
        valid = isFieldValid && valid;


        isFieldValid = validator.isNumberValid(fieldPassport.getText().replaceAll("\\s", ""),
                PASSPORT_MIN, PASSPORT_MAX);
        colorField(fieldPassport, isFieldValid);
        valid = isFieldValid && valid;


        for (TextField field : Arrays.asList(fieldResult1, fieldResult2, fieldResult3)) {
            isFieldValid = validator.isNumberValid(field.getText(), ZERO, RESULT_MAX);
            colorField(field, isFieldValid);
            valid = isFieldValid && valid;
        }

        isFieldValid = !departmentButton.getText().equals("Choose");
        departmentButton.setStyle(String.format("-fx-background-color: %s;", isFieldValid ? "green" : "red"));
        valid = isFieldValid && valid;

        return valid;
    }

    private void colorField(TextField field, boolean correct) {
        field.setStyle(String.format("-fx-text-fill: %s;", correct ? "green" : "red"));
    }

    @FXML
    private void onDepartmentButtonClicked() {
        departmentButton.setStyle(departmentButtonStyle);
    }
}