package time_table;

import Generated.ETTTimeTable;
import evolution.engine.problem_solution.*;

public class TimeTable implements Problem {
    int days, hours;
    SchoolClasses schoolClasses;
    Subjects subjects;
    Teachers teachers;
    Rules rules;

    public TimeTable(ETTTimeTable gen) {
        days = gen.getDays();
        hours = gen.getHours();

        schoolClasses = new SchoolClasses(gen.getETTClasses());
        subjects = new Subjects(gen.getETTSubjects());
        teachers = new Teachers(gen.getETTTeachers());
        rules = new Rules(gen.getETTRules());
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public SchoolClasses getSchoolClasses() {
        return schoolClasses;
    }

    public Subjects getSubjects() {
        return subjects;
    }

    public Teachers getTeachers() {
        return teachers;
    }

    public Rules getRules() {
        return rules;
    }

    public int getAmountofSchoolClasses() {
        return this.schoolClasses.getClassList().size();
    }

    public int getAmountofTeachers() {
        return this.teachers.getTeacherList().size();
    }

    public int getAmountofSubjects() {
        return this.subjects.getSubjectList().size();
    }

    @Override
    public Solution solve() {
        return new TimeTableSolution(this);
    }

    @Override
    public String toString() {
        String lineSeparator = System.getProperty("line.separator");
        return "Total days: " + days + lineSeparator +
                "Hours per day: " + hours + lineSeparator +
                "The classes are: " + lineSeparator + schoolClasses + lineSeparator +
                "The subjects are: " + lineSeparator + subjects + lineSeparator +
                "The teachers are: " + lineSeparator + teachers + lineSeparator +
                "The rules are: " + rules;
    }
}
