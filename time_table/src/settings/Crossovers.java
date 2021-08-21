package settings;

import Generated.ETTCrossover;
import Generated.ETTMutation;
import evolution.configuration.CrossoverIFC;
import evolution.engine.problem_solution.Solution;
import evolution.util.Randomizer;
import javafx.beans.property.*;
import solution.Fifth;
import solution.TimeTableSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum Crossovers implements CrossoverIFC {
    DAY_TIME_ORIENTED("DayTimeOriented") {
        @Override
        List<Solution> cross(Solution s_1, Solution s_2) {
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
                int maxListSize = s1.getMaxListSize();
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
    ASPECT_ORIENTED("AspectOriented") {
        @Override
        List<Solution> cross(Solution s_1, Solution s_2) {
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

        List<Solution> teacherOriented(TimeTableSolution s1, TimeTableSolution s2) {
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

            for (int t = 0; t < s1.getTimeTable().getAmountofTeachers(); t++) {
                for (int i = 0; i < this.getCuttingPoints(); i++) {
                    // Randomize cutting points. Amount according to CuttingPoints:
                    do {
                        cuttingPoint = Randomizer.getRandomNumber(1, maxListSize - 1);
                    } while (cuttingPointsList.contains(cuttingPoint));
                    cuttingPointsList.add(cuttingPoint);
                }

                Collections.sort(cuttingPointsList);


                int finalT = t;
                list1Parts = s1.splitToParts(
                        s1.getFifthsList()
                                .stream()
                                .filter(fifth-> fifth.getTeacher().equals(finalT))
                                .collect(Collectors.toList()), cuttingPointsList);
                list2Parts = s2.splitToParts(
                        s2.getFifthsList()
                                .stream()
                                .filter(fifth-> fifth.getTeacher().equals(finalT))
                                .collect(Collectors.toList()), cuttingPointsList);




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

        List<Solution> classOriented(TimeTableSolution s1, TimeTableSolution s2) {
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
            int maxListSize = s1.getMaxTeacherListSize();

            List<List<Fifth>> list1Parts;
            List<List<Fifth>> list2Parts;

            List<List<Fifth>> crossoverList1 = new ArrayList<>();
            List<List<Fifth>> crossoverList2 = new ArrayList<>();

            for (int t = 0; t < s1.getTimeTable().getAmountofTeachers(); t++) {
                for (int i = 0; i < this.getCuttingPoints(); i++) {
                    // Randomize cutting points. Amount according to CuttingPoints:
                    do {
                        cuttingPoint = Randomizer.getRandomNumber(1, maxListSize - 1);
                    } while (cuttingPointsList.contains(cuttingPoint));
                    cuttingPointsList.add(cuttingPoint);
                }

                Collections.sort(cuttingPointsList);


                int finalT = t;
                list1Parts = s1.splitToParts(
                        s1.getFifthsList()
                                .stream()
                                .filter(fifth-> fifth.getTeacher().equals(finalT))
                                .collect(Collectors.toList()), cuttingPointsList);
                list2Parts = s2.splitToParts(
                        s2.getFifthsList()
                                .stream()
                                .filter(fifth-> fifth.getTeacher().equals(finalT))
                                .collect(Collectors.toList()), cuttingPointsList);




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

    Crossovers(String name) {
        this.name = name;
        cuttingPoints = new SimpleIntegerProperty(0);
        active = new SimpleBooleanProperty(false);
        orientation = new SimpleStringProperty("");
    }

    @Override
    public int getCuttingPoints() {
        return 0;
    }

    @Override
    public void initFromXML(ETTCrossover ettCrossover) {
        this.name = ettCrossover.getName();
        this.cuttingPoints.setValue(ettCrossover.getCuttingPoints());
        if (name.equals("AspectOriented")) {
            Pattern pattern = Pattern.compile("^Orientation=(CLASS|TEACHER)$");
            Matcher m = pattern.matcher(ettCrossover.getConfiguration());

            if (m.find())
                this.orientation.setValue(m.group(1));
        } else {
            this.orientation.setValue(null);
        }
    }

    abstract List<Solution> cross(Solution s_1, Solution s_2);
}
