package databases.databasesapplianceform.sql;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLProcessor {

    private DataSource dataSource;

    public SQLProcessor() {
        final String url =
                "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=7410";
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        this.dataSource = dataSource;
    }


    public void printTable() throws SQLException {

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM student");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            System.out.printf("id:%d\tname:%s\tmiddle name:%s\tlast name:%s\tbirth date:%s\tachievements:%d\tpassport:%s\n",
                    rs.getLong("student_id"), rs.getString("student_first_name"),
                    rs.getString("student_middle_name"), rs.getString("student_last_name"),
                    rs.getDate("birth_date"), rs.getInt("achievements"),
                    rs.getString("passport"));
        }
    }

    public void printAppliances(List<String> departments, List<String> examSubjects) throws SQLException {

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM applience");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            System.out.printf("id:%d\tstudent_id:%d\tdepartment:%s\t" +
                            "exam_subject_1_id:%s\texam_subject_2_id:%s\texam_subject_3_id:%s\n",
                    rs.getLong("applience_id"), rs.getLong("student_id"),
                    departments.get((int) rs.getLong("department_id") - 1),
                    examSubjects.get((int) rs.getLong("exam_subject_1_id") - 1),
                    examSubjects.get((int) rs.getLong("exam_subject_2_id") - 1),
                    examSubjects.get((int) rs.getLong("exam_subject_3_id") - 1));
        }
    }

    public void insertStudent(StudentForm studentForm) throws SQLException {

        Connection conn = dataSource.getConnection();

        PreparedStatement insertStmt =
                conn.prepareStatement("INSERT INTO Student(student_first_name, student_middle_name, " +
                        "student_last_name, birth_date, achievements, passport, student_id) VALUES (?,?,?,?,?,?,?)");

        studentForm.studentId = getNextId("student", "student_id");


        insertStmt.setString(1, studentForm.firstName);
        insertStmt.setString(2, studentForm.middleName);
        insertStmt.setString(3, studentForm.lastName);
        insertStmt.setDate(4, new java.sql.Date(studentForm.birthDate.getTime()));
        insertStmt.setInt(5, studentForm.achievements);
        insertStmt.setString(6, studentForm.passport);
        insertStmt.setInt(7, studentForm.studentId);

        int insertedRows = insertStmt.executeUpdate();
        System.out.printf("inserted %s student\n", insertedRows);
    }

    public void insertResults(List<ExamResult> examResults) throws SQLException {

        for (ExamResult examResult : examResults) {

            Connection conn = dataSource.getConnection();

            PreparedStatement insertStmt =
                    conn.prepareStatement("INSERT INTO result(result_id, result_score, " +
                            "student_id, exam_subject_id) VALUES (?,?,?,?)");

            examResult.id = getNextId("result", "result_id");

            insertStmt.setInt(1, examResult.id);
            insertStmt.setInt(2, examResult.score);
            insertStmt.setInt(3, examResult.studentId);
            insertStmt.setInt(4, examResult.examSubjectId);

            int insertedRows = insertStmt.executeUpdate();
            System.out.printf("inserted %s result\n", insertedRows);

        }
    }

    public void insertAppliance(Appliance appliance) throws SQLException {

        Connection conn = dataSource.getConnection();

        PreparedStatement insertStmt =
                conn.prepareStatement("INSERT INTO applience(applience_id, student_id, department_id, " +
                        "exam_subject_1_id, exam_subject_2_id, exam_subject_3_id) VALUES (?,?,?,?,?,?)");

        appliance.id = getNextId("applience", "applience_id");

        insertStmt.setInt(1, appliance.id);
        insertStmt.setInt(2, appliance.studentId);
        insertStmt.setInt(3, appliance.departmentId);
        insertStmt.setInt(4, appliance.examSubjectId1);
        insertStmt.setInt(5, appliance.examSubjectId2);
        insertStmt.setInt(6, appliance.examSubjectId3);

        int insertedRows = insertStmt.executeUpdate();
        System.out.printf("inserted %s result\n", insertedRows);
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


    private int getNextId(String tableName, String columnName) throws SQLException {

        Connection conn = dataSource.getConnection();

        PreparedStatement stmt = conn.prepareStatement("SELECT max(" + columnName + ") FROM " + tableName);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        if (rs.getString(1) == null) {
            return 0;
        }
        return Integer.parseInt(rs.getString(1)) + 1;
    }

    public int[] getSubjectIds(String departmentName) throws SQLException {

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM department WHERE department_name=?");

        stmt.setString(1, departmentName);
        ResultSet rs = stmt.executeQuery();

        int[] ids = new int[3];

        rs.next();
        ids[0] = rs.getInt("subject_1_id");
        ids[1] = rs.getInt("subject_2_id");
        ids[2] = rs.getInt("subject_3_id");

        return ids;
    }
}
