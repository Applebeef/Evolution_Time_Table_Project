package days_hours;

import time_table.SchoolClass;
import time_table.Subject;
import time_table.Teacher;

public class SchoolClassTeacherSubject {
    private SchoolClass schoolClass;
    private Teacher teacher;
    private Subject subject;

    public SchoolClassTeacherSubject(SchoolClass schoolClass, Teacher teacher, Subject subject) {
        this.schoolClass = schoolClass;
        this.teacher = teacher;
        this.subject = subject;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
