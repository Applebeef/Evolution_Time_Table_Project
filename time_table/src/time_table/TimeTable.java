package time_table;

import Generated.ETTTimeTable;
import evolution.engine.EvolutionEngine;
import evolution.engine.problem_solution.*;
import evolution.util.Pair;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import settings.CrossoverWrapper;
import settings.Mutations;
import settings.SelectionWrapper;
import solution.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTable implements Problem {
    IntegerProperty days, hours;
    SchoolClasses schoolClasses;
    Subjects subjects;
    Teachers teachers;
    Rules rules;
    String uploader;
    /*Mutations mutations;
    List<CrossoverWrapper> crossoversList;
    List<SelectionWrapper> selectionsList;*/
    Map<String, EvolutionEngine> engineMap;


    public TimeTable(ETTTimeTable gen) {
        days = new SimpleIntegerProperty(gen.getDays());
        hours = new SimpleIntegerProperty(gen.getHours());

        schoolClasses = new SchoolClasses(gen.getETTClasses());
        subjects = new Subjects(gen.getETTSubjects());
        teachers = new Teachers(gen.getETTTeachers());
        rules = new Rules(gen.getETTRules());
        engineMap = new HashMap<>();
//        mutations = new Mutations(gen.get); OLD - left for future reference on how to build lists
//
//        selectionsList = Arrays.stream(Selections.values()).collect(Collectors.toList());
//        for (Selections s : selectionsList) {
//            if (s.getType().equals(selectionSettings.getType())) {
//                s.initFromXml(selectionSettings);
//                break;
//            }
//        }
//        crossoversList = Arrays.stream(Crossovers.values()).collect(Collectors.toList());
//        for (Crossovers c : crossoversList) {
//            if (c.getName().equals(crossoverSettings.getName())) {
//                c.initFromXML(crossoverSettings);
//                break;
//            }
//        }
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
        return this.schoolClasses.getSchoolClassList().size();
    }

    public int getAmountofTeachers() {
        return this.teachers.getTeacherList().size();
    }

    public int getAmountofSubjects() {
        return this.subjects.getSubjectList().size();
    }

    public Map<String, EvolutionEngine> getEngineMap() {
        return engineMap;
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
                "The rules are: " + rules + lineSeparator;
    }

    public int getMaxListSize() {
        return getDays()
                * getHours()
                * getAmountofTeachers()
                * getAmountofSubjects()
                * getAmountofSchoolClasses();
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getUploader() {
        return uploader;
    }
}
