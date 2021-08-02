package Solution;

import Generated.ETTDescriptor;
import evolution.util.Randomizer;
import evolution.configuration.Crossover;
import evolution.configuration.Mutation;
import evolution.configuration.Mutations;
import evolution.engine.problem_solution.Solution;
import evolution.rules.Type;
import time_table.Rule;
import time_table.SchoolClass;
import time_table.Teacher;
import time_table.TimeTable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TimeTableSolution implements Solution {
    private List<Fifth> fifthsList;
    private Double fitness;
    private TimeTable timeTable;
    private PresentationOptions presentationOption;


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
        TEACHER_ORIENTED(2, "Display solution teacher oriented.") {
            @Override
            public String getDisplayString(TimeTableSolution timeTableSolution) {
                String res = "";
                // Show TimeTableSolution for each teacher:
                for (Teacher teacher : timeTableSolution.timeTable.getTeachers().getTeacherList()) {
                    res += "Id: " + teacher.getId() +
                            "Name: " + teacher.getName() + ":";
                    res += timeTableSolution.getDisplay(teacher);
                }
                return res;
            }
        },
        SCHOOLCLASS_ORIENTED(3, "Display solution class oriented.") {
            @Override
            public String getDisplayString(TimeTableSolution timeTableSolution) {
                return null;
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
                List<Fifth> toBeMutated = this.getFifthsList().
                        stream().
                        unordered().
                        limit(mutatedNumber).
                        collect(Collectors.toList());
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

            /*System.out.println("s1 size: " + subLists1.size());
            System.out.println("s2 size: " + subLists2.size());*/

            List<List<Fifth>> crossoverList1 = new ArrayList<>();
            List<List<Fifth>> crossoverList2 = new ArrayList<>();

            for (int i = 0; i < subLists1.size(); i++) {
              /*  System.out.println("s1["+i+"] size: " + subLists1.get(i).size());
                System.out.println("s2["+i+"] size: " + subLists2.get(i).size());*/
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
    public void setPresentationOption(int presentationOption) {
        if (presentationOption < 0 || presentationOption >= PresentationOptions.values().length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.presentationOption = PresentationOptions.values()[presentationOption];
    }

    public List<Fifth> getFifthsList() {
        return fifthsList;
    }

    public Double getFitness() {
        return fitness;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    private String getDisplay(Teacher teacher) {
        /* Line of action: 1. Create a 2D array of fifths
                           2. Add to 2D array relevant fifths
                           3. Compute corresponding table
         */
        // 2D fifths array:
        List<List<Fifth>> fifthsArray = new ArrayList<>(this.timeTable.getHours());
        // Create arrays:
        for (int i = 0; i < this.timeTable.getHours(); i++) {
            fifthsArray.set(i, new ArrayList<Fifth>());
        }
        // Iterate through all fifths and add relevant fifths to 2D array:
        for (Fifth fifth : this.fifthsList) {
            if (fifth.getTeacher() == teacher.getId()) {
                // Set the fifth in the 2D array:
                fifthsArray.
                        get(fifth.getHour()).
                        set(fifth.getDay(), fifth);
            }
        }
        // Create and return relevant table:
        return getDisplayHelper(fifthsArray);
    }

    private String getDisplay(SchoolClass schoolClass) {
        String lineSeparator = System.getProperty("line.separator");
        String res = "";
        return res;
    }

    private String getDisplayHelper(List<List<Fifth>> fifths2DList) {
        String lineSeparator = System.getProperty("line.separator");
        String res = "  ";
        int i, j;
        // Add days:
        for (i = 0; i < this.timeTable.getDays(); i++) {
            res += " |  " + (i + 1) + " ";
        }
        res += lineSeparator;
        i = 0;
        // Insert fifths by hours:
        for (List<Fifth> fls : fifths2DList) {
            res += " " + (i + 1);
            for (j = 0; j < this.timeTable.getDays(); j++) {
                res += " | <";
                switch (this.presentationOption) {
                    case TEACHER_ORIENTED:
                        if (fls.get(j).getSchoolClass() == -1 || fls.get(j).getSubject() == -1) {
                            res += " , >";
                        } else {
                            res += fls.get(j).getSchoolClass() + ",";
                            res += fls.get(j).getSubject() + ">";
                        }
                        break;
                    case SCHOOLCLASS_ORIENTED:
                        if (fls.get(j).getTeacher() == -1 || fls.get(j).getSubject() == -1) {
                            res += " , >";
                        } else {
                            res += fls.get(j).getTeacher() + ",";
                            res += fls.get(j).getSubject() + ">";
                        }
                        break;
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
