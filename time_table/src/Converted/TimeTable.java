package Converted;

import Generated.ETTTimeTable;
import evolution.engine.problem_solution.*;


public class TimeTable implements Problem {
    int days,hours;
    SchoolClasses schoolClasses;
    Subjects subjects;
    Teachers teachers;
    Rules rules;

    public TimeTable(ETTTimeTable gen){
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

    @Override
    public Solution Solve() {
        return null;//TODO add new class TimeTableSolution?
    }
}
