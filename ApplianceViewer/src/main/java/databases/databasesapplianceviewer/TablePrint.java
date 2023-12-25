package databases.databasesapplianceviewer;

import databases.databasesapplianceviewer.sql.ApplianceRow;
import databases.databasesapplianceviewer.sql.Department;

import java.util.List;

public class TablePrint {

    private String STYLE_BUDGET =  " style=\"border: 1px solid grey; background-color:rgba(0,255,0,0.3); \" ";
    private String STYLE =  " style=\"border: 1px solid grey;\" ";

    private String getTH(String argument) {
        return "<th" + STYLE + ">" + argument + "</th>\n";
    }

    private String getTD(String argument) {
        return "<th" + STYLE + ">" + argument + "</th>\n";
    }

    public String getTable(List<ApplianceRow> applianceRows, Department department) {

        StringBuilder table = new StringBuilder();
        table.append("<html><table").append(STYLE).append(">\n<tr").append(STYLE).append(">\n")
                .append(getTH("№"))
                .append(getTH("Full name"))
                .append(getTH(department.subjectName1))
                .append(getTH(department.subjectName2))
                .append(getTH(department.subjectName3))
                .append(getTH("Achievements"))
                .append(getTH("Sum"))
                .append(" </tr>\n");

        int i = 0;
        String style = STYLE_BUDGET;
        for (ApplianceRow row : applianceRows) {

            if (i == department.budgetPlaces) {
                style = STYLE;
            }

            table.append("<tr").append(style).append(">\n")
                    .append(getTD(String.valueOf(++i)))
                    .append(getTD(row.name))
                    .append(getTD(String.valueOf(row.result1)))
                    .append(getTD(String.valueOf(row.result2)))
                    .append(getTD(String.valueOf(row.result3)))
                    .append(getTD(String.valueOf(row.achievements)))
                    .append(getTD(String.valueOf(row.result1 + row.result2 + row.result3 + row.achievements)))
                    .append("</tr>\n");
        }
        table.append("</table></html>");

        return table.toString();
    }
}
