package solution;

import java.util.Objects;

public class Fifth implements Comparable<Fifth> {
    private Integer day, hour, schoolClass, teacher, subject;

    public Fifth(int day, int hour, int schoolClass, int teacher, int subject) {
        this.day = day;
        this.hour = hour;
        this.schoolClass = schoolClass;
        this.teacher = teacher;
        this.subject = subject;
    }


    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(Integer schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Integer getTeacher() {
        return teacher;
    }

    public void setTeacher(Integer teacher) {
        this.teacher = teacher;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    @Override
    public int compareTo(Fifth o) {
        int cmp;
        cmp = this.getDay().compareTo(o.getDay());
        if (cmp == 0) {
            cmp = this.getHour().compareTo(o.getHour());
            if (cmp == 0) {
                cmp = this.getSchoolClass().compareTo(o.getSchoolClass());
                if (cmp == 0) {
                    cmp = this.getTeacher().compareTo(o.getTeacher());
                }
            }
        }
        return cmp;
    }

    @Override
    public String toString() {
        return "<" +
                day + "," +
                hour + "," +
                schoolClass + "," +
                teacher + "," +
                subject + "," +
                ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fifth fifth = (Fifth) o;
        return Objects.equals(day, fifth.day) && Objects.equals(hour, fifth.hour) && Objects.equals(schoolClass, fifth.schoolClass) && Objects.equals(teacher, fifth.teacher) && Objects.equals(subject, fifth.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, schoolClass, teacher, subject);
    }

    public Fifth copy() {
        return new Fifth(day, hour, schoolClass, teacher, subject);
    }
}
