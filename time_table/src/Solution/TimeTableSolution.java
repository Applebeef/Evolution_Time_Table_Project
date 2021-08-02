package Solution;

import evolution.util.Randomizer;
import evolution.configuration.Crossover;
import evolution.configuration.Mutation;
import evolution.configuration.Mutations;
import evolution.engine.problem_solution.Solution;
import evolution.rules.Type;
import time_table.Rule;
import time_table.TimeTable;

import java.util.*;
import java.util.stream.Collectors;

public class TimeTableSolution implements Solution {
    private List<Fifth> fifthsList;
    private Double fitness;
    private TimeTable timeTable;

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
    }

    public TimeTableSolution(TimeTable timeTable, List<List<Fifth>> fifths) {
        this.timeTable = timeTable;
        this.fifthsList = new ArrayList<>();
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


    public List<Fifth> getFifthsList() {
        return fifthsList;
    }

    public Double getFitness() {
        return fitness;
    }

    public TimeTable getTimeTable() {
        return timeTable;
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
        //TODO make better toString.
        return "TimeTableSolution{" +
                "fitness=" + String.format("%.1f", fitness) +
                '}';
    }
}
