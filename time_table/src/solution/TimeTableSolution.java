package solution;

import evolution.util.Randomizer;
import evolution.configuration.Crossover;
import evolution.configuration.Mutation;
import evolution.configuration.Mutations;
import evolution.engine.problem_solution.Solution;
import evolution.rules.Type;
import time_table.*;
import util.Pair;

import java.util.*;

public class TimeTableSolution implements Solution {
    private List<Fifth> fifthsList;
    private Double fitness;
    private Integer generation;
    private TimeTable timeTable;
    private PresentationOptions presentationOption;
    private static String lineSeparator = System.getProperty("line.separator");


    // Inner enum PresentationOptions:
    public enum PresentationOptions {
        //                * Fifths list
        //                * Teacher oriented timetable
        //                * Class oriented timetable
        FIFTHS_LIST(1, "Display solution as a fifths list.") {
            @Override
            public String getDisplayString(TimeTableSolution timeTableSolution) {
                String res = "{";
                int i;
                for (i = 0; i < timeTableSolution.fifthsList.size() - 1; i++) {
                    res += timeTableSolution.fifthsList.get(i);
                    res += ",";
                }
                res += timeTableSolution.fifthsList.get(i);
                res += '}';
                return res;
            }
        },
        TEACHER_ORIENTED(2, "Display solution - teacher oriented.") {
            @Override
            public String getDisplayString(TimeTableSolution timeTableSolution) {
                String res = "";
                // Show TimeTableSolution for each teacher:
                for (Teacher teacher : timeTableSolution.timeTable.getTeachers().getTeacherList()) {
                    res += "Id: " + teacher.getId() + ", " +
                            "Name: " + teacher.getName() + ":";
                    res += lineSeparator;
                    res += timeTableSolution.getDisplay(teacher);
                }
                return res;
            }
        },
        SCHOOLCLASS_ORIENTED(3, "Display solution - class oriented.") {
            @Override
            public String getDisplayString(TimeTableSolution timeTableSolution) {
                String res = "";
                // Show TimeTableSolution for each teacher:
                for (SchoolClass schoolClass : timeTableSolution.timeTable.getSchoolClasses().getClassList()) {
                    res += "Id: " + schoolClass.getId() + ", " +
                            "Name: " + schoolClass.getName() + ":";
                    res += lineSeparator;
                    res += timeTableSolution.getDisplay(schoolClass);
                }
                return res;
            }
        };

        int number;
        String action;

        PresentationOptions(int number, String action) {
            this.number = number;
            this.action = action;
        }

        abstract public String getDisplayString(TimeTableSolution timeTableSolution);

        @Override
        public String toString() {
            return number + " - " + action;
        }
    }

    public TimeTableSolution(TimeTable timeTable) {
        this.timeTable = timeTable;
        fifthsList = new ArrayList<>();
        int day, hour, schoolClass, teacher, subject;
        for (day = 1; day <= timeTable.getDays(); day++) {
            for (hour = 1; hour <= timeTable.getHours(); hour++) {
                for (schoolClass = 1; schoolClass <= timeTable.getAmountofSchoolClasses(); schoolClass++) {
                    teacher = Randomizer.getRandomNumber(0, timeTable.getAmountofTeachers());
                    if (teacher == 0) {
                        subject = 0;
                    } else {
                        subject = Randomizer.getRandomNumber(1, timeTable.getAmountofSubjects());
                    }
                    fifthsList.add(new Fifth(day, hour, schoolClass, teacher, subject));
                }
            }
        }
        // Default presentation option is FIFTS_LIST:
        this.presentationOption = PresentationOptions.FIFTHS_LIST;
    }

    public TimeTableSolution(TimeTable timeTable, List<List<Fifth>> fifths) {
        this.timeTable = timeTable;
        this.fifthsList = new ArrayList<>();
        // Default presentation option is FIFTS_LIST:
        this.presentationOption = PresentationOptions.FIFTHS_LIST;
        fifths.forEach(list -> fifthsList.addAll(list));
        this.calculateFitness();
    }

    @Override
    public double calculateFitness() {
        // Get weight of hard\soft rules:
        double hardRulesWeight = (double) timeTable.getRules().getHardRulesWeight() / 100;
        double sofRulesWeight = 1 - hardRulesWeight;
        double hardTotalScore = 0, softTotalScore = 0;
        int hardCount = 0, softCount = 0;
        // Iterate through Rules list and calculate data:
        for (Rule rule : timeTable.getRules().getRuleList()) {
            if (rule.getType() == Type.HARD) {
                hardCount++;
                hardTotalScore += rule.test(this);
            } else {
                softCount++;
                softTotalScore += rule.test(this);
            }
        }
        // Calculate total fitness according to percentage given by user:
        this.fitness = (hardTotalScore / (double) hardCount) * hardRulesWeight +
                (softTotalScore / (double) softCount) * sofRulesWeight;
        return this.fitness;
    }

    @Override
    public void mutate(Mutations mutations) {
        double probability;
        int mutatedNumber;
        // Iterate through mutations:
        for (Mutation mutation : mutations.getMutationList()) {
            // Randomize a real number from 0 to 1:
            probability = Math.random();
            // If the random number "hits" the mutation probability then the mutation will happend:
            if (probability <= mutation.getProbability()) {
                // mutatedNumber = maximum amount of tupples with the mutation:
                mutatedNumber = Randomizer.getRandomNumber(1, mutation.getConfig().getMaxTupples());
                List<Fifth> toBeMutated = new ArrayList<>();
                for (int i = 0; i < mutatedNumber; i++) {
                    toBeMutated.add(this.getFifthsList().get(Randomizer.getRandomNumber(0, this.getFifthsList().size() - 1)));
                }
                String component = mutation.getConfig().getComponent();
                // Mutate the toBeMutated's according to the component in the mutation:
                switch (component) {
                    case "D":
                        toBeMutated.forEach(fifth -> fifth.setDay(Randomizer.getRandomNumber(1, timeTable.getDays())));
                        break;
                    case "H":
                        toBeMutated.forEach(fifth -> fifth.setHour(Randomizer.getRandomNumber(1, timeTable.getHours())));
                        break;
                    case "C":
                        toBeMutated.forEach(fifth -> fifth.setSchoolClass(Randomizer.getRandomNumber(1, timeTable.getAmountofSchoolClasses())));
                        break;
                    case "T":
                        toBeMutated.forEach(fifth -> fifth.setTeacher(Randomizer.getRandomNumber(0, timeTable.getAmountofTeachers())));
                        break;
                    case "S":
                        toBeMutated.forEach(fifth -> fifth.setSubject(Randomizer.getRandomNumber(0, timeTable.getAmountofSubjects())));
                        break;
                }
            }
        }
        calculateFitness();
    }


    @Override
    public List<Solution> crossover(Solution solution, Crossover crossover) {
        List<Solution> res = new ArrayList<>();
        if (solution instanceof TimeTableSolution) {
            //TODO check crossover type for different sorts.
            TimeTableSolution other_timeTable_solution = (TimeTableSolution) solution;
            // Sort my fifths:
            Collections.sort(this.getFifthsList());
            // Sort other's fifths:
            Collections.sort(other_timeTable_solution.getFifthsList());

            List<Integer> cuttingPointsList = new ArrayList<>();
            Integer cuttingPoint;
            for (int i = 0; i < crossover.getCuttingPoints(); i++) {
                // Randomize cutting points. Amount according to CuttingPoints:
                do {
                    cuttingPoint = Randomizer.getRandomNumber(1, this.getFifthsList().size() - 1);
                } while (cuttingPointsList.contains(cuttingPoint));
                cuttingPointsList.add(cuttingPoint);
            }

            Collections.sort(cuttingPointsList);


            List<List<Fifth>> subLists1 = chopIntoParts(this.getFifthsList(), cuttingPointsList);
            List<List<Fifth>> subLists2 = chopIntoParts(other_timeTable_solution.getFifthsList(), cuttingPointsList);


            List<List<Fifth>> crossoverList1 = new ArrayList<>();
            List<List<Fifth>> crossoverList2 = new ArrayList<>();

            for (int i = 0; i < subLists1.size(); i++) {
                if (i % 2 == 0) {
                    crossoverList1.add(subLists1.get(i));
                    crossoverList2.add(subLists2.get(i));
                } else {
                    crossoverList1.add(subLists2.get(i));
                    crossoverList2.add(subLists1.get(i));
                }
            }

            TimeTableSolution solutionOffspring1 = new TimeTableSolution(this.getTimeTable(), crossoverList1);
            TimeTableSolution solutionOffspring2 = new TimeTableSolution(other_timeTable_solution.getTimeTable(), crossoverList2);
            res.add(solutionOffspring1);
            res.add(solutionOffspring2);

        }
        return res;
    }

    // Return a list of sublists according to cutting points:
    private List<List<Fifth>> chopIntoParts(List<Fifth> fifthList, List<Integer> cuttingPointsList) {
        List<List<Fifth>> partsList = new ArrayList<>();
        int min;
        int max = 0;
        for (Integer cuttingPoint : cuttingPointsList) {
            min = max;
            max = cuttingPoint;
            partsList.add(fifthList.subList(min, max));
        }
        if (max < fifthList.size()) {
            min = max;
            partsList.add(fifthList.subList(min, fifthList.size()));
        }
        return partsList;
    }

    @Override
    public void setPresentationOption(int requested_presentation_option) {
        this.presentationOption = PresentationOptions.values()[requested_presentation_option];
    }

    public List<Fifth> getFifthsList() {
        return fifthsList;
    }

    @Override
    public Double getFitness() {
        return fitness;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    private String getDisplay(SchoolClassOrTeacher schoolclass_or_teacher) {
        /* Line of action: 1. Create a 2D array of fifths
                           2. Add to 2D array relevant fifths
                           3. Compute corresponding table
         */
        // 2D fifths array (Pair<Fifth,Boolean> indicates multiple values in a specific day/hour:
        List<List<Pair<Fifth, Boolean>>> fifthsArray = new ArrayList<>(this.timeTable.getHours());
        // Initializing fifth:
        final Fifth null_fifth = new Fifth(-1, -1, -1, -1, -1);
        // Create arrays:
        for (int i = 0; i < this.timeTable.getHours(); i++) {
            fifthsArray.add(i, new ArrayList<>());
            for (int j = 0; j < this.timeTable.getDays(); j++) {
                fifthsArray.get(i).add(new Pair<>(null_fifth, false));
            }
        }
        int i =0;
        Boolean multiple_arguments_flag;
        // Iterate through all fifths and add relevant fifths to 2D array:
        for (Fifth fifth : this.fifthsList) {
            multiple_arguments_flag = false;
            if ((schoolclass_or_teacher.getClass().equals(SchoolClass.class) &&
                    fifth.getSchoolClass() == schoolclass_or_teacher.getId())
                    ||
                    (schoolclass_or_teacher.getClass().equals(Teacher.class) &&
                            fifth.getTeacher() == schoolclass_or_teacher.getId())
            ) {
                if (!fifthsArray.
                        get(fifth.getHour() - 1).
                        get(fifth.getDay() - 1).
                        getV1().
                        equals(null_fifth)) {
                    multiple_arguments_flag = true;
                }
                // Set the fifth in the 2D array:
                fifthsArray.
                        get(fifth.getHour() - 1).
                        add(fifth.getDay() - 1, new Pair<>(fifth, multiple_arguments_flag));
            }
        }
        // Create and return relevant table:
        return getDisplayHelper(fifthsArray);
    }

    private String getDisplayHelper(List<List<Pair<Fifth, Boolean>>> fifths2DList) {
        String res = "  ";
        int i, j;
        // Add days:
        for (i = 0; i < this.timeTable.getDays(); i++) {
            res += " |   " + (i + 1) + "   ";
        }
        res += lineSeparator;
        i = 0;
        // Insert fifths by hours:
        for (List<Pair<Fifth, Boolean>> fls : fifths2DList) {
            // Hour no.:
            res += " " + (i + 1);
            // Fifths (presented as tuples according to presentationOption):
            for (j = 0; j < this.timeTable.getDays(); j++) {
                res += " | <";
                switch (this.presentationOption) {
                    case TEACHER_ORIENTED:
                        if (fls.get(j).getV1().getSchoolClass() == -1 || fls.get(j).getV1().getSubject() == -1) {
                            res += " , >";
                        } else {
                            res += fls.get(j).getV1().getSchoolClass() + ",";
                            res += fls.get(j).getV1().getSubject() + ">";
                        }
                        break;
                    case SCHOOLCLASS_ORIENTED:
                        if (fls.get(j).getV1().getTeacher() == -1 || fls.get(j).getV1().getSubject() == -1) {
                            res += " , >";
                        } else {
                            res += fls.get(j).getV1().getTeacher() + ",";
                            res += fls.get(j).getV1().getSubject() + ">";
                        }
                        break;
                }
                // Add note for multiple values:
                if (fls.get(j).getV2()) {
                    res += "*";
                } else {
                    res += " ";
                }
            }
            res += lineSeparator;
            i++;
        }
        return res;
    }


    @Override
    public int compareTo(Solution o) {
        if (o instanceof TimeTableSolution) {
            return this.getFitness().compareTo(((TimeTableSolution) o).getFitness());
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return this.presentationOption.getDisplayString(this);
    }
}