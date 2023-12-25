package databases.databasesapplianceform.sql;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLProcessor {

    private DataSource dataSource;

    public SQLProcessor() {
        final String url =
                "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=7410";
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        this.dataSource = dataSource;
    }


    public int getStudentId(String passport) throws SQLException {

        Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT student_id FROM student WHERE passport=?");

        stmt.setString(1, passport);
        ResultSet rs = stmt.executeQuery();

        rs.next();

        return rs.getInt("student_id");
    }

    public void deleteStudentFromAppliance(int id) {

        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM applience WHERE student_id=?");

            stmt.setInt(1, id);
            stmt.executeQuery();
        } catch (SQLException ignored) {}
    }

    public void deleteStudentFromResult(int id) {

        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM result WHERE student_id=?");

            stmt.setInt(1, id);
            stmt.executeQuery();
        } catch (SQLException ignored) {}
    }

    public void deleteStudentFromStudent(int id) {

        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM student WHERE student_id=?");

            stmt.setInt(1, id);
            stmt.executeQuery();
        } catch (SQLException ignored) {}
    }
}
