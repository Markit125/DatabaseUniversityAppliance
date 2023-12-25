package databases.databasesstudentviewer.sql;

import java.util.Comparator;

public class StudentRow {

    public int id;
    public String firstName;
    public String middleName;
    public String lastName;
    public String birthDate;
    public String passport;
    public String departmentApplied;

    public String subjectName1;
    public String subjectName2;
    public String subjectName3;
    public int result1;
    public int result2;
    public int result3;
    public int achievements;

    public static Comparator<StudentRow> comparatorByFirstName() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o1.firstName.compareTo(o2.firstName);
            }
        };
    }

    public static Comparator<StudentRow> comparatorByMiddleName() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o1.middleName.compareTo(o2.middleName);
            }
        };
    }

    public static Comparator<StudentRow> comparatorByLastName() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o1.lastName.compareTo(o2.lastName);
            }
        };
    }


    public static Comparator<StudentRow> comparatorByDepartment() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o1.departmentApplied.compareTo(o2.departmentApplied);
            }
        };
    }

    public static Comparator<StudentRow> comparatorBySum() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o2.result1 + o2.result2 + o2.result3 + o2.achievements
                        -(o1.result1 + o1.result2 + o1.result3 + o1.achievements);
            }
        };
    }

    public static Comparator<StudentRow> comparatorByBirthDate() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o1.birthDate.compareTo(o2.birthDate);
            }
        };
    }

    public static Comparator<StudentRow> comparatorByResult1() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o2.result1 - o1.result1;
            }
        };
    }

    public static Comparator<StudentRow> comparatorByResult2() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o2.result2 - o1.result2;
            }
        };
    }

    public static Comparator<StudentRow> comparatorByResult3() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o2.result3 - o1.result3;
            }
        };
    }

    public static Comparator<StudentRow> comparatorByAchievements() {
        return new Comparator<>() {
            @Override
            public int compare(StudentRow o1, StudentRow o2) {
                return o2.achievements - o1.achievements;
            }
        };
    }
}
