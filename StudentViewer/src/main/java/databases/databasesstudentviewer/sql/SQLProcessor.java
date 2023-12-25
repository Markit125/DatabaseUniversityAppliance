package databases.databasesstudentviewer.sql;

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

    public List<StudentRow> getAllStudentInfo(List<String> subjectList) throws SQLException {

        List<StudentRow> studentRowList = new ArrayList<>();

        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM student");

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            StudentRow studentRow = new StudentRow();

            studentRow.id = rs.getInt("student_id");
            studentRow.firstName = rs.getString("student_first_name");
            studentRow.middleName = rs.getString("student_middle_name");
            studentRow.lastName = rs.getString("student_last_name");
            studentRow.birthDate = rs.getString("birth_date");
            studentRow.passport = rs.getString("passport");
            studentRow.achievements = rs.getInt("achievements");


            stmt = conn.prepareStatement("SELECT department_name FROM department WHERE department_id IN " +
                    "(SELECT department_id FROM applience WHERE student_id = ?)");

            stmt.setInt(1, studentRow.id);
            ResultSet rsDepartment = stmt.executeQuery();

            rsDepartment.next();
            studentRow.departmentApplied = rsDepartment.getString("department_name");


            stmt = conn.prepareStatement("SELECT * FROM result WHERE student_id = ? ORDER BY exam_subject_id");

            stmt.setInt(1, studentRow.id);
            ResultSet rsResult = stmt.executeQuery();

            rsResult.next();
            studentRow.result1 = rsResult.getInt("result_score");
            studentRow.subjectName1 = subjectList.get(rsResult.getInt("exam_subject_id") - 1);
            rsResult.next();
            studentRow.result2 = rsResult.getInt("result_score");
            studentRow.subjectName2 = subjectList.get(rsResult.getInt("exam_subject_id") - 1);
            rsResult.next();
            studentRow.result3 = rsResult.getInt("result_score");
            studentRow.subjectName3 = subjectList.get(rsResult.getInt("exam_subject_id") - 1);


            studentRowList.add(studentRow);
        }

        return studentRowList;
    }
}