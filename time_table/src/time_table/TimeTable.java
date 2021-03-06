package time_table;

import Generated.ETTCrossover;
import Generated.ETTMutations;
import Generated.ETTSelection;
import Generated.ETTTimeTable;
import evolution.engine.problem_solution.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import settings.*;
import solution.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TimeTable implements Problem {
    IntegerProperty days, hours;
    SchoolClasses schoolClasses;
    Subjects subjects;
    Teachers teachers;
    Rules rules;
    Mutations mutations;
    List<Crossovers> crossoversList;
    List<Selections> selectionsList;


    public TimeTable(ETTTimeTable gen, ETTMutations mutationsSettings, ETTCrossover crossoverSettings, ETTSelection selectionSettings) {
        days = new SimpleIntegerProperty(gen.getDays());
        hours = new SimpleIntegerProperty(gen.getHours());

        schoolClasses = new SchoolClasses(gen.getETTClasses());
        subjects = new Subjects(gen.getETTSubjects());
        teachers = new Teachers(gen.getETTTeachers());
        rules = new Rules(gen.getETTRules());

        mutations = new Mutations(mutationsSettings);

        selectionsList = Arrays.stream(Selections.values()).collect(Collectors.toList());
        for (Selections s : selectionsList) {
            if (s.getType().equals(selectionSettings.getType())) {
                s.initFromXml(selectionSettings);
                break;
            }
        }
        crossoversList = Arrays.stream(Crossovers.values()).collect(Collectors.toList());
        for (Crossovers c : crossoversList) {
            if (c.getName().equals(crossoverSettings.getName())) {
                c.initFromXML(crossoverSettings);
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
                "Selection - " + selectionsList + lineSeparator + lineSeparator +
                "Crossover - " + lineSeparator + crossoversList;
    }

    public Mutations getMutations() {
        return mutations;
    }

    public List<Crossovers> getCrossoverList() {
        return crossoversList;
    }

    public List<Selections> getSelectionsList() {
        return selectionsList;
    }

    public int getMaxListSize() {
        return getDays()
                * getHours()
                * getAmountofTeachers()
                * getAmountofSubjects()
                * getAmountofSchoolClasses();
    }
}
