package databases.databasesstudentviewer;

import databases.databasesstudentviewer.sql.StudentRow;

import java.util.List;

public class TablePrint {
    private String STYLE =  " style=\"border: 1px solid grey;\" ";

    private String getTH(String argument) {
        return "<th" + STYLE + ">" + argument + "</th>\n";
    }

    private String getTD(String argument) {
        return "<th" + STYLE + ">" + argument + "</th>\n";
    }

    public String getTable(List<StudentRow> studentRows) {

        StringBuilder table = new StringBuilder();
        table.append("<html><table").append(STYLE).append(">\n<tr").append(STYLE).append(">\n")
                .append(getTH("№"))
                .append(getTH("Last name"))
                .append(getTH("First name"))
                .append(getTH("Middle name"))
                .append(getTH("Birth date"))
                .append(getTH("Passport"))
                .append(getTH("Department applied"))
                .append(getTH("Subject 1"))
                .append(getTH("Result 1"))
                .append(getTH("Subject 2"))
                .append(getTH("Result 2"))
                .append(getTH("Subject 3"))
                .append(getTH("Result 3"))
                .append(getTH("Achievements"))
                .append(getTH("Points"))
                .append(" </tr>\n");

        int i = 0;
        for (StudentRow row : studentRows) {
            table.append("<tr").append(STYLE).append(">\n")
                    .append(getTD(String.valueOf(++i)))
                    .append(getTD(row.lastName))
                    .append(getTD(row.firstName))
                    .append(getTD(row.middleName))
                    .append(getTD(row.birthDate))
                    .append(getTD(row.passport))
                    .append(getTD(row.departmentApplied))
                    .append(getTD(row.subjectName1))
                    .append(getTD(String.valueOf(row.result1)))
                    .append(getTD(row.subjectName2))
                    .append(getTD(String.valueOf(row.result2)))
                    .append(getTD(row.subjectName3))
                    .append(getTD(String.valueOf(row.result3)))
                    .append(getTD(String.valueOf(row.achievements)))
                    .append(getTD(String.valueOf(row.result1 + row.result2 + row.result3 + row.achievements)))
                    .append("</tr>\n");
        }
        table.append("</table></html>");

        return table.toString();
    }
}
