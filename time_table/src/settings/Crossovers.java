package settings;

import evolution.configuration.CrossoverIFC;
import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;
import javafx.beans.property.*;
import solution.Fifth;
import solution.TimeTableSolution;
import time_table.SchoolClass;
import time_table.Teacher;
import time_table.TimeTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum Crossovers implements CrossoverIFC {
    DAY_TIME_ORIENTED("DayTimeOriented", "") {
        @Override
        void parseString(String configuration) {
            setOrientation("");
        }

        @Override
        public List<? extends Solution> cross(Solution s_1, Solution s_2) {
            List<Solution> res = new ArrayList<>();
            if (s_1 instanceof TimeTableSolution && s_2 instanceof TimeTableSolution) {
                TimeTableSolution s1 = (TimeTableSolution) s_1;
                TimeTableSolution s2 = (TimeTableSolution) s_2;

                // Create comparator:
                Comparator<Fifth> comparator = Comparator.comparing(Fifth::getDay)
                        .thenComparing(Fifth::getHour)
                        .thenComparing(Fifth::getSchoolClass)
                        .thenComparing(Fifth::getTeacher)
                        .thenComparing(Fifth::getSubject);
                // Sort my fifths:
                s1.getFifthsList().sort(comparator);
                // Sort other's fifths:
                s2.getFifthsList().sort(comparator);

                List<Integer> cuttingPointsList = new ArrayList<>();
                Integer cuttingPoint;
                int maxListSize = s1.getTimeTable().getMaxListSize();
                for (int i = 0; i < this.getCuttingPoints(); i++) {
                    // Randomize cutting points. Amount according to CuttingPoints:
                    do {
                        cuttingPoint = Randomizer.getRandomNumber(1, maxListSize - 1);
                    } while (cuttingPointsList.contains(cuttingPoint));
                    cuttingPointsList.add(cuttingPoint);
                }

                Collections.sort(cuttingPointsList);

                List<List<Fifth>> list1Parts;
                List<List<Fifth>> list2Parts;
                list1Parts = s1.splitToParts(s1.getFifthsList(), cuttingPointsList);
                list2Parts = s2.splitToParts(s2.getFifthsList(), cuttingPointsList);


                List<List<Fifth>> crossoverList1 = new ArrayList<>();
                List<List<Fifth>> crossoverList2 = new ArrayList<>();

                for (int i = 0; i < list1Parts.size() && i < list2Parts.size(); i++) {
                    if (i % 2 == 0) {
                        crossoverList1.add(list1Parts.get(i));
                        crossoverList2.add(list2Parts.get(i));
                    } else {
                        crossoverList1.add(list2Parts.get(i));
                        crossoverList2.add(list1Parts.get(i));
                    }
                }

                TimeTableSolution solutionOffspring1 = new TimeTableSolution(s1.getTimeTable(), crossoverList1);
                TimeTableSolution solutionOffspring2 = new TimeTableSolution(s2.getTimeTable(), crossoverList2);
                res.add(solutionOffspring1);
                res.add(solutionOffspring2);

            }
            return res;
        }
    },
    ASPECT_ORIENTED("AspectOriented", "TEACHER") {
        @Override
        void parseString(String configuration) {
            if (getName().equals("AspectOriented")) {
                Pattern pattern = Pattern.compile("^Orientation=(CLASS|TEACHER)$");
                Matcher m = pattern.matcher(configuration);
                if (m.find())
                    setOrientation(m.group(1));
            }
        }

        @Override
        public List<? extends Solution> cross(Solution s_1, Solution s_2) {
            if (s_1 instanceof TimeTableSolution && s_2 instanceof TimeTableSolution) {
                TimeTableSolution s1 = (TimeTableSolution) s_1;
                TimeTableSolution s2 = (TimeTableSolution) s_2;

                switch (this.orientation.getValue()) {
                    case "TEACHER":
                        return teacherOriented(s1, s2);
                    case "CLASS":
                        return classOriented(s1, s2);
                    default:
                        return null;
                }
            }
            return null;
        }

        List<? extends Solution> teacherOriented(TimeTableSolution s1, TimeTableSolution s2) {
            List<Solution> res = new ArrayList<>();

            // Create comparator:
            Comparator<Fifth> comparator = Comparator.comparing(Fifth::getTeacher)
                    .thenComparing(Fifth::getDay)
                    .thenComparing(Fifth::getHour)
                    .thenComparing(Fifth::getSchoolClass)
                    .thenComparing(Fifth::getSubject);
            // Sort my fifths:
            s1.getFifthsList().sort(comparator);
            // Sort other's fifths:
            s2.getFifthsList().sort(comparator);

            List<Integer> cuttingPointsList = new ArrayList<>();
            Integer cuttingPoint;
            int maxListSize = s1.getMaxTeacherListSize();

            List<List<Fifth>> list1Parts;
            List<List<Fifth>> list2Parts;

            List<List<Fifth>> crossoverList1 = new ArrayList<>();
            List<List<Fifth>> crossoverList2 = new ArrayList<>();

            for (Teacher teacher : s1.getTimeTable().getTeachers().getTeacherList()) {
                for (int i = 0; i < this.getCuttingPoints(); i++) {
                    // Randomize cutting points. Amount according to CuttingPoints:
                    do {
                        cuttingPoint = Randomizer.getRandomNumber(1, maxListSize - 1);
                    } while (cuttingPointsList.contains(cuttingPoint));
                    cuttingPointsList.add(cuttingPoint);
                }

                Collections.sort(cuttingPointsList);

                int finalT = teacher.getId();
                list1Parts = splitToParts(s1.getFifthsList().stream()
                                .filter(fifth -> fifth.getTeacher()
                                        .equals(finalT)).collect(Collectors.toList()),
                        cuttingPointsList, s1.getTimeTable());
                list2Parts = splitToParts(s2.getFifthsList().stream()
                                .filter(fifth -> fifth.getTeacher()
                                        .equals(finalT)).collect(Collectors.toList()),
                        cuttingPointsList, s2.getTimeTable());


                for (int i = 0; i < list1Parts.size() && i < list2Parts.size(); i++) {
                    if (i % 2 == 0) {
                        crossoverList1.add(list1Parts.get(i));
                        crossoverList2.add(list2Parts.get(i));
                    } else {
                        crossoverList1.add(list2Parts.get(i));
                        crossoverList2.add(list1Parts.get(i));
                    }
                }
            }

            TimeTableSolution solutionOffspring1 = new TimeTableSolution(s1.getTimeTable(), crossoverList1);
            TimeTableSolution solutionOffspring2 = new TimeTableSolution(s2.getTimeTable(), crossoverList2);
            res.add(solutionOffspring1);
            res.add(solutionOffspring2);

            return res;
        }

        List<? extends Solution> classOriented(TimeTableSolution s1, TimeTableSolution s2) {
            List<Solution> res = new ArrayList<>();

            // Create comparator:
            Comparator<Fifth> comparator = Comparator.comparing(Fifth::getSchoolClass)
                    .thenComparing(Fifth::getDay)
                    .thenComparing(Fifth::getHour)
                    .thenComparing(Fifth::getTeacher)
                    .thenComparing(Fifth::getSubject);
            // Sort my fifths:
            s1.getFifthsList().sort(comparator);
            // Sort other's fifths:
            s2.getFifthsList().sort(comparator);

            List<Integer> cuttingPointsList = new ArrayList<>();
            Integer cuttingPoint;
            int maxListSize = s1.getMaxSchoolClassListSize();

            List<List<Fifth>> list1Parts;
            List<List<Fifth>> list2Parts;

            List<List<Fifth>> crossoverList1 = new ArrayList<>();
            List<List<Fifth>> crossoverList2 = new ArrayList<>();

            for (SchoolClass schoolClass : s1.getTimeTable().getSchoolClasses().getClassList()) {
                for (int i = 0; i < this.getCuttingPoints(); i++) {
                    // Randomize cutting points. Amount according to CuttingPoints:
                    do {
                        cuttingPoint = Randomizer.getRandomNumber(1, maxListSize - 1);
                    } while (cuttingPointsList.contains(cuttingPoint));
                    cuttingPointsList.add(cuttingPoint);
                }

                Collections.sort(cuttingPointsList);

                int finalC = schoolClass.getId();
                list1Parts = splitToParts(s1.getFifthsList().stream()
                                .filter(fifth -> fifth.getSchoolClass()
                                        .equals(finalC)).collect(Collectors.toList()),
                        cuttingPointsList, s1.getTimeTable());
                list2Parts = splitToParts(s2.getFifthsList().stream()
                                .filter(fifth -> fifth.getSchoolClass()
                                        .equals(finalC)).collect(Collectors.toList()),
                        cuttingPointsList, s2.getTimeTable());


                for (int i = 0; i < list1Parts.size() && i < list2Parts.size(); i++) {
                    if (i % 2 == 0) {
                        crossoverList1.add(list1Parts.get(i));
                        crossoverList2.add(list2Parts.get(i));
                    } else {
                        crossoverList1.add(list2Parts.get(i));
                        crossoverList2.add(list1Parts.get(i));
                    }
                }
            }

            TimeTableSolution solutionOffspring1 = new TimeTableSolution(s1.getTimeTable(), crossoverList1);
            TimeTableSolution solutionOffspring2 = new TimeTableSolution(s2.getTimeTable(), crossoverList2);
            res.add(solutionOffspring1);
            res.add(solutionOffspring2);
            return res;
        }

    };

    protected StringProperty orientation;
    protected String name;
    protected BooleanProperty active;
    protected IntegerProperty cuttingPoints;

    Crossovers(String name, String orientation) {
        this.name = name;
        cuttingPoints = new SimpleIntegerProperty(1);
        active = new SimpleBooleanProperty(false);
        this.orientation = new SimpleStringProperty(orientation);

        active.addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(true)) {
                for (Crossovers crossovers : Crossovers.values()) {
                    if (!crossovers.equals(this)) {
                        crossovers.setActive(false);
                    }
                }
            }
        });
    }

    @Override
    public int getCuttingPoints() {
        return cuttingPoints.get();
    }

//    @Override
//    public void initFromXML(ETTCrossover ettCrossover) { TODO fix
//        setName(ettCrossover.getName());
//        setActive(true);
//        setCuttingPoints(ettCrossover.getCuttingPoints());
//        parseString(ettCrossover.getConfiguration());
//    }

    abstract void parseString(String configuration);

    public String getOrientation() {
        return orientation.get();
    }

    public StringProperty orientationProperty() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation.set(orientation);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public IntegerProperty cuttingPointsProperty() {
        return cuttingPoints;
    }

    public void setCuttingPoints(int cuttingPoints) {
        this.cuttingPoints.set(cuttingPoints);
    }

    public List<List<Fifth>> splitToParts(List<Fifth> fifthsList, List<Integer> cuttingPointsList, TimeTable timeTable) {
        List<List<Fifth>> res = new ArrayList<>(cuttingPointsList.size() + 1);
        for (int i = 0; i < cuttingPointsList.size() + 1; i++) {
            res.add(new ArrayList<>());
        }
        for (Fifth fifth : fifthsList) {
            /* Returns the index of this fifth assuming we had a "full" array
             (accounting for every class,teacher,subject,hour and day): */
            int position = getPosition(fifth, timeTable);
            // Add the current fifth to the correct List according to position:
            if (position < cuttingPointsList.get(0)) {
                res.get(0).add(fifth);
            } else if (position >= cuttingPointsList.get(cuttingPointsList.size() - 1)) {
                res.get(res.size() - 1).add(fifth);
            } else {
                for (int i = 0; i < cuttingPointsList.size() - 1; i++) {
                    if (position >= cuttingPointsList.get(i) && position < cuttingPointsList.get(i + 1)) {
                        res.get(i + 1).add(fifth);
                        break;
                    }
                }
            }
        }
        return res;
    }

    private int getPosition(Fifth fifth, TimeTable timeTable) {
        //returns the index of this fifth assuming we had a "full" array (accounting for every class,teacher,subject,hour and day).
        return (fifth.getSubject() - 1) +
                (fifth.getTeacher() - 1) * timeTable.getAmountofSubjects() +
                (fifth.getSchoolClass() - 1) * timeTable.getAmountofSubjects() * timeTable.getAmountofTeachers() +
                (fifth.getHour() - 1) * timeTable.getAmountofSubjects() * timeTable.getAmountofTeachers() * timeTable.getAmountofSchoolClasses() +
                (fifth.getDay() - 1) * timeTable.getAmountofSubjects() * timeTable.getAmountofTeachers() * timeTable.getAmountofSchoolClasses() * timeTable.getHours();
    }
}
