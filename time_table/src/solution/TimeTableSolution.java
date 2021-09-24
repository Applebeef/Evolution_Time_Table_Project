package solution;

import evolution.util.Randomizer;
import evolution.engine.problem_solution.Solution;
import evolution.rules.Type;

import settings.Crossovers;
import time_table.*;
import evolution.util.Pair;

import java.util.*;


public class TimeTableSolution implements Solution {
    private List<Fifth> fifthsList;
    private Double fitness;
    private TimeTable timeTable;
    private PresentationOptions presentationOption;
    private Map<RuleWrapper, Double> ruleGradeMap;
    private Comparator<Fifth> dayTimeComparator;
    private Comparator<Fifth> teacherAspectComparator;
    private Comparator<Fifth> classAspectComparator;
    private static final String lineSeparator = System.getProperty("line.separator");

    // Inner enum PresentationOptions:
    public enum PresentationOptions {
        //                * Fifths list
        //                * Teacher oriented timetable
        //                * Class oriented timetable
        FIFTHS_LIST(1, "Display solution as a fifths list.") {
            @Override
            public String getDisplayString(TimeTableSolution timeTableSolution) {
                String res = "";
                int i;
                for (i = 0; i < timeTableSolution.fifthsList.size() - 1; i++) {
                    res += timeTableSolution.fifthsList.get(i);
                    res += ", ";
                }
                res += timeTableSolution.fifthsList.get(i);
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
                for (SchoolClass schoolClass : timeTableSolution.timeTable.getSchoolClasses().getSchoolClassList()) {
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

    public TimeTableSolution(TimeTableSolution other) {
        this.fifthsList = new ArrayList<>(other.fifthsList.size());
        for (Fifth fifth : other.fifthsList) {
            this.fifthsList.add(fifth.copy());
        }
        this.fitness = other.fitness;
        this.timeTable = other.timeTable;
        this.presentationOption = other.presentationOption;
        this.dayTimeComparator = other.dayTimeComparator;
        this.teacherAspectComparator = other.teacherAspectComparator;
        this.classAspectComparator = other.classAspectComparator;
        this.ruleGradeMap = other.ruleGradeMap;
    }

    public TimeTableSolution(TimeTable timeTable) {
        this.timeTable = timeTable;
        fifthsList = new ArrayList<>();
        int day, hour, schoolClass, teacher, subject;
        int amountOfClasses = timeTable.getSchoolClasses().getSchoolClassList().size();
        for (int i = 0; i < amountOfClasses; i++) {
            for (Study study : timeTable.getSchoolClasses().getSchoolClassList().get(i).getRequirements().getStudyList()) {
                for (int j = 0; j < study.getHours(); j++) {
                    schoolClass = timeTable.getSchoolClasses().getSchoolClassList().get(i).getId();
                    subject = study.getSubjectId();
                    day = Randomizer.getRandomNumber(1, timeTable.getDays());
                    hour = Randomizer.getRandomNumber(1, timeTable.getHours());
                    teacher = Randomizer.getRandomNumber(1, timeTable.getAmountofTeachers());
                    fifthsList.add(new Fifth(day, hour, schoolClass, teacher, subject));
                }
            }
        }
        // Default presentation option is FIFTHS_LIST:
        this.presentationOption = PresentationOptions.FIFTHS_LIST;
        setComparators();
        this.ruleGradeMap = new HashMap<>();
        this.calculateFitness();
    }

    public TimeTableSolution(TimeTable timeTable, List<List<Fifth>> fifths) {
        this.timeTable = timeTable;
        this.fifthsList = new ArrayList<>();
        // Default presentation option is FIFTHS_LIST:
        this.presentationOption = PresentationOptions.FIFTHS_LIST;
        fifths.forEach(list -> fifthsList.addAll(list));
        this.ruleGradeMap = new HashMap<>();
        this.calculateFitness();
        setComparators();
    }

    private void setComparators() {
        dayTimeComparator = Comparator.comparing(Fifth::getDay)
                .thenComparing(Fifth::getHour)
                .thenComparing(Fifth::getSchoolClass)
                .thenComparing(Fifth::getTeacher)
                .thenComparing(Fifth::getSubject);
        teacherAspectComparator = Comparator.comparing(Fifth::getTeacher)
                .thenComparing(Fifth::getDay)
                .thenComparing(Fifth::getHour)
                .thenComparing(Fifth::getSchoolClass)
                .thenComparing(Fifth::getSubject);
        classAspectComparator = Comparator.comparing(Fifth::getSchoolClass)
                .thenComparing(Fifth::getDay)
                .thenComparing(Fifth::getHour)
                .thenComparing(Fifth::getTeacher)
                .thenComparing(Fifth::getSubject);
    }

    @Override
    public double calculateFitness() {
        // Get weight of hard\soft rules:
        double hardRulesWeight = (double) timeTable.getRules().getHardRulesWeight() / 100;
        double softRulesWeight = 1 - hardRulesWeight;
        double hardTotalScore = 0, softTotalScore = 0;
        int hardCount = 0, softCount = 0;
        Double grade;
        // Iterate through Rules list and calculate data:
        for (RuleWrapper rule : timeTable.getRules().getRuleList()) {
            grade = rule.test(this);
            if (rule.getType() == Type.HARD) {
                hardCount++;
                hardTotalScore += grade;
            } else {
                softCount++;
                softTotalScore += grade;
            }
            ruleGradeMap.put(rule, grade);
        }
        // Calculate total fitness according to percentage given by user:
        this.fitness = (hardTotalScore / (double) hardCount) * hardRulesWeight +
                (softTotalScore / (double) softCount) * softRulesWeight;
        return this.fitness;
    }

    public Fifth generateRandomFifth() {
        int day, hour, schoolClass, teacher, subject;
        schoolClass = Randomizer.getRandomNumber(1, timeTable.getAmountofSchoolClasses());
        subject = Randomizer.getRandomNumber(1, timeTable.getAmountofSubjects());
        day = Randomizer.getRandomNumber(1, timeTable.getDays());
        hour = Randomizer.getRandomNumber(1, timeTable.getHours());
        teacher = Randomizer.getRandomNumber(1, timeTable.getAmountofTeachers());
        return new Fifth(day, hour, schoolClass, teacher, subject);
    }


    public int getMaxTeacherListSize() {
        return timeTable.getDays()
                * timeTable.getHours()
                * timeTable.getAmountofSubjects()
                * timeTable.getAmountofSchoolClasses();
    }

    public int getMaxSchoolClassListSize() {
        return timeTable.getDays()
                * timeTable.getHours()
                * timeTable.getAmountofSubjects()
                * timeTable.getAmountofTeachers();
    }

    public List<List<Fifth>> splitToParts(List<Fifth> fifthsList, List<Integer> cuttingPointsList) {
        List<List<Fifth>> res = new ArrayList<>(cuttingPointsList.size() + 1);
        for (int i = 0; i < cuttingPointsList.size() + 1; i++) {
            res.add(new ArrayList<>());
        }
        for (Fifth fifth : fifthsList) {
            /* Returns the index of this fifth assuming we had a "full" array
             (accounting for every class,teacher,subject,hour and day): */
            int position = getPosition(fifth);
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

    private int getPosition(Fifth fifth) {
        //returns the index of this fifth assuming we had a "full" array (accounting for every class,teacher,subject,hour and day).
        return (fifth.getSubject() - 1) +
                (fifth.getTeacher() - 1) * timeTable.getAmountofSubjects() +
                (fifth.getSchoolClass() - 1) * timeTable.getAmountofSubjects() * timeTable.getAmountofTeachers() +
                (fifth.getHour() - 1) * timeTable.getAmountofSubjects() * timeTable.getAmountofTeachers() * timeTable.getAmountofSchoolClasses() +
                (fifth.getDay() - 1) * timeTable.getAmountofSubjects() * timeTable.getAmountofTeachers() * timeTable.getAmountofSchoolClasses() * timeTable.getHours();
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

    @Override
    public Solution copy() {
        return new TimeTableSolution(this);
    }


    public TimeTable getTimeTable() {
        return timeTable;
    }

    private String getDisplay(Identifiable schoolclass_or_teacher) {
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
        boolean multiple_arguments_flag;
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
        return this.getFitness().compareTo(o.getFitness());
    }

    @Override
    public String toString() {
        return this.presentationOption.getDisplayString(this);
    }

    public Map<String, Double> getRuleFitnessMap() {
        Map<String, Double> map = new HashMap<>();
        ruleGradeMap.forEach((ruleWrapper, fitness) -> map.put(ruleWrapper.getRuleName(), fitness));
        return map;
    }

    public Map<RuleWrapper, Double> getRuleGradeMap() {
        return ruleGradeMap;
    }
}
