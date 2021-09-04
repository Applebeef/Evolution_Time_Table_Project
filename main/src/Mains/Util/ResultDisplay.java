package Mains.Util;

import solution.Fifth;
import time_table.SchoolClass;
import time_table.Subject;
import time_table.Teacher;
import time_table.TimeTable;

public enum ResultDisplay {
    TEACHER {
        @Override
        String getDisplay(Fifth fifth) {
            SchoolClass schoolClass = null;
            Subject subject = null;
            for (SchoolClass schoolClass1 : timeTable.getSchoolClasses().getClassList()) {
                if (schoolClass1.getId() == fifth.getSchoolClass()) {
                    schoolClass = schoolClass1;
                    break;
                }
            }
            for (Subject subject1 : timeTable.getSubjects().getSubjectList()) {
                if (subject1.getId() == fifth.getSubject()) {
                    subject = subject1;
                    break;
                }
            }
            return "Class: " + fifth.getSchoolClass() + "-" + schoolClass.getName() + System.lineSeparator() +
                    "Subject: " + fifth.getSubject() + "-" + subject.getName();
        }
    },
    CLASS {
        @Override
        String getDisplay(Fifth fifth) {
            Teacher teacher = null;
            Subject subject = null;
            for (Teacher teacher1 : timeTable.getTeachers().getTeacherList()) {
                if (teacher1.getId() == fifth.getTeacher()) {
                    teacher = teacher1;
                    break;
                }
            }
            for (Subject subject1 : timeTable.getSubjects().getSubjectList()) {
                if (subject1.getId() == fifth.getSubject()) {
                    subject = subject1;
                    break;
                }
            }
            return "Teacher: " + fifth.getTeacher() + "-" + teacher.getName() + System.lineSeparator()
                    + "Subject: " + fifth.getSubject() + "-" + subject.getName();
        }
    };

    int id = 1;
    TimeTable timeTable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    abstract String getDisplay(Fifth fifth);
}
