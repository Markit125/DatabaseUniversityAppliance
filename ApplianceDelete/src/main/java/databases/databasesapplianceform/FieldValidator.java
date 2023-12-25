package databases.databasesapplianceform;

public class FieldValidator {

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
