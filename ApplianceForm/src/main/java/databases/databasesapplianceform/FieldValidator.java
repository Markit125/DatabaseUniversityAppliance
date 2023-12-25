package databases.databasesapplianceform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldValidator {

    public boolean isDateValid(String inputDate) {
        inputDate = inputDate.replaceAll("\\s", "");
        try {
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            formatter.setLenient(false);
            Date date = formatter.parse(inputDate);
            if (date.after(new Date(System.currentTimeMillis()))) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public boolean isNameValid(String name) {

        name = name.replaceAll("\\s", "");

        if (name == null) {
            return false;
        }

        if (name.length() == 0) {
            return false;
        }

        if (name.charAt(0) != (name.toUpperCase().charAt(0))) {
            return false;
        }

        return true;
    }

    public boolean isNumberValid(String number, long from, long to) {
        long num;
        try {
            num = Long.parseLong(number.replaceAll("\\s", ""));
        } catch (NumberFormatException e) {
            return false;
        }
        return from <= num && num <= to;
    }
}
