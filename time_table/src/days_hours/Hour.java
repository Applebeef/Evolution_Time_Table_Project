package days_hours;

import time_table.SchoolClass;
import time_table.Subject;
import time_table.Teacher;
import time_table.TimeTable;

import java.util.ArrayList;
import java.util.List;

public class Hour {
    // Each hour has a List of SchoolClassTeacherSubject * number of classes
    private List<SchoolClassTeacherSubject> schoolClassTeacherSubjectList;

    // Random trio Constructor:
    public Hour(TimeTable timeTable) {
        SchoolClassTeacherSubject scts;
        SchoolClass schoolClass;
        Teacher teacher;
        Subject subject;
        int random_number; // Random teacher or subject

        int amount_of_classes = timeTable.getAmountofSchoolClasses();
        schoolClassTeacherSubjectList = new ArrayList<>(amount_of_classes);

        for (int i = 0; i < amount_of_classes; i++) {
            schoolClass = timeTable.getSchoolClasses().getClassList().get(i);
            random_number = getRandomNumber(-1, timeTable.getAmountofTeachers() - 1);
            if (random_number == -1) {
                teacher = null;
                subject = null;
            } else {
                // Select random Teacher:
                teacher = timeTable.
                        getTeachers().
                        getTeacherList().
                        get(random_number);
                // Select random Subject:
                random_number = getRandomNumber(0, timeTable.getAmountofSubjects() - 1);
                subject = timeTable.
                                getSubjects().
                                getSubjectList().
                                get(random_number);
            }
            scts = new SchoolClassTeacherSubject(schoolClass, teacher, subject);
            schoolClassTeacherSubjectList.add(scts);
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public List<SchoolClassTeacherSubject> getSchoolClassTeacherSubjectList() {
        return schoolClassTeacherSubjectList;
    }
    //TODO:WRITE A NON RANDOM CONSTRUCTOR
}
