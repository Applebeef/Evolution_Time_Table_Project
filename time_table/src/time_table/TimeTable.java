package time_table;

import Generated.ETTCrossover;
import Generated.ETTMutations;
import Generated.ETTSelection;
import Generated.ETTTimeTable;
import evolution.configuration.CrossoverIFC;
import evolution.configuration.MutationsIFC;
import evolution.configuration.SelectionIFC;
import evolution.engine.problem_solution.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import settings.*;
import solution.*;

public class TimeTable implements Problem {
    IntegerProperty days, hours;
    SchoolClasses schoolClasses;
    Subjects subjects;
    Teachers teachers;
    Rules rules;
    Mutations mutations;
    Crossovers crossover;
    Selections selection;


    public TimeTable(ETTTimeTable gen, ETTMutations mutationsSettings, ETTCrossover crossoverSettings, ETTSelection selectionSettings) {
        days = new SimpleIntegerProperty(gen.getDays());
        hours = new SimpleIntegerProperty(gen.getHours());

        schoolClasses = new SchoolClasses(gen.getETTClasses());
        subjects = new Subjects(gen.getETTSubjects());
        teachers = new Teachers(gen.getETTTeachers());
        rules = new Rules(gen.getETTRules());

        mutations = new Mutations(mutationsSettings);

        for (Selections s : Selections.values()) {
            if (s.getType().equals(selectionSettings.getType())) {
                s.initFromXml(selectionSettings);
                selection = s;
                break;
            }
        }
        for (Crossovers c : Crossovers.values()){
            if (c.getName().equals(crossoverSettings.getName())){
                c.initFromXML(crossoverSettings);
                crossover=c;
                break;
            }
        }
    }

    public int getDays() {
        return days.get();
    }

    public IntegerProperty daysProperty() {
        return days;
    }

    public int getHours() {
        return hours.get();
    }

    public IntegerProperty hoursProperty() {
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
                "The rules are: " + rules + lineSeparator +
                "Mutations - " + lineSeparator + mutations + lineSeparator +
                "Selection - " + selection + lineSeparator + lineSeparator +
                "Crossover - " + lineSeparator + crossover;
    }

    public Mutations getMutations() {
        return mutations;
    }

    public Crossovers getCrossover() {
        return crossover;
    }

    public Selections getSelection() {
        return selection;
    }
}
