package databases.databasesapplianceviewer.sql;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SQLProcessor {

    private DataSource dataSource;

    public SQLProcessor() {
        final String url =
                "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=7410";
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        this.dataSource = dataSource;
    }

    public List<String> getSubjects() throws SQLException {

        List<String> subjects = new ArrayList<>();

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM exam_subject ORDER BY exam_subject_id");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            subjects.add(rs.getString("exam_subject_name"));
        }

        return subjects;
    }

    public List<String> getDepartments() throws SQLException {

        List<String> departments = new ArrayList<>();

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM department ORDER BY department_id");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            departments.add(rs.getString("department_name"));
        }

        return departments;
    }

    public List<ApplianceRow> getAppliancesDepartment(int departmentId) throws SQLException {

        List<ApplianceRow> applianceRowList = new ArrayList<>();

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT student_id, exam_subject_1_id, exam_subject_2_id" +
                ", exam_subject_3_id FROM applience WHERE department_id=?");

        stmt.setInt(1, departmentId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            ApplianceRow applianceRow = new ApplianceRow();

            Student student = getStudentById(rs.getInt("student_id"));
            applianceRow.name = student.fullName;
            applianceRow.achievements = student.achievement;

            applianceRow.result1 = student.result1;
            applianceRow.result2 = student.result2;
            applianceRow.result3 = student.result3;

            applianceRowList.add(applianceRow);
        }

        applianceRowList.sort((lhs, rhs) -> {
            return -(lhs.result1 + lhs.result2 + lhs.result3 + lhs.achievements) +
                    (rhs.result1 + rhs.result2 + rhs.result3 + rhs.achievements);
        });

        return applianceRowList;
    }

    private Student getStudentById(int id) throws SQLException {

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT student_first_name, student_middle_name, " +
                "student_last_name, achievements FROM student WHERE student_id=?");

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        rs.next();

        Student student = new Student();
        student.fullName = rs.getString("student_first_name") + " " +
                rs.getString("student_middle_name") + " " +
                rs.getString("student_last_name");
        student.achievement = rs.getInt("achievements");


        stmt = conn.prepareStatement("SELECT result_score, exam_subject_id" +
                " FROM result WHERE student_id=?");

        stmt.setInt(1, id);
        rs = stmt.executeQuery();

        Map<Integer, Integer> subjects = new TreeMap<>();

        while (rs.next()) {
            subjects.put(rs.getInt("exam_subject_id"), rs.getInt("result_score"));
        }

        Iterator<Map.Entry<Integer, Integer>> iterator = subjects.entrySet().iterator();
        Map.Entry<Integer, Integer> entry = iterator.next();
        student.result1 = entry.getValue();

        entry = iterator.next();
        student.result2 = entry.getValue();

        entry = iterator.next();
        student.result3 = entry.getValue();

        return student;
    }

    public Department getDepartment(String departmentName, List<String> subjectList) throws SQLException {

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT subject_1_id, subject_2_id, " +
                "subject_3_id, budget_places FROM department WHERE department_name=?");

        stmt.setString(1, departmentName);
        ResultSet rs = stmt.executeQuery();

        rs.next();

        Department department = new Department();

        department.subjectName1 = subjectList.get(rs.getInt("subject_1_id") - 1);
        department.subjectName2 = subjectList.get(rs.getInt("subject_2_id") - 1);
        department.subjectName3 = subjectList.get(rs.getInt("subject_3_id") - 1);
        department.budgetPlaces = rs.getInt("budget_places");

        return department;
    }
}