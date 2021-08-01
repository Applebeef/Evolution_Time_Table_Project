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
        double hardRulesWeight = (double) timeTable.getRules().getHardRulesWeight() / 100;
        double hardTotal = 0, softTotal = 0;
        int hardCount = 0, softCount = 0;
        for (Rule rule : timeTable.getRules().getRuleList()) {
            if (rule.getType() == Type.HARD) {
                hardCount++;
                hardTotal += rule.test(this);
            } else {
                softCount++;
                softTotal += rule.test(this);
            }
        }
        this.fitness = (hardTotal / (double) hardCount) * (hardRulesWeight) + (softTotal / (double) softCount) * (1 - hardRulesWeight);
        return this.fitness;
    }

    @Override
    public void mutate(Mutations mutations) {
        for (Mutation mutation : mutations.getMutationList()) {
            double probability = Math.random();
            if (probability <= mutation.getProbability()) {
                int mutatedNumber = Randomizer.getRandomNumber(1, mutation.getConfig().getMaxTupples());
                List<Fifth> toBeMutated = this.getFifthsList().stream().unordered().limit(mutatedNumber).collect(Collectors.toList());
                String component = mutation.getConfig().getComponent();
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
            TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
            //TODO check crossover type for different sorts.
            Collections.sort(this.getFifthsList());
            Collections.sort(timeTableSolution.getFifthsList());

            List<Integer> cuttingPointsList = new ArrayList<>();
            Integer cuttingPoint;
            for (int i = 0; i < crossover.getCuttingPoints(); i++) {
                do {
                    cuttingPoint = Randomizer.getRandomNumber(1, this.getFifthsList().size() - 1);
                } while (cuttingPointsList.contains(cuttingPoint));
                cuttingPointsList.add(cuttingPoint);
            }
            List<List<Fifth>> subLists1 = chopIntoParts(this.getFifthsList(), cuttingPointsList);
            List<List<Fifth>> subLists2 = chopIntoParts(timeTableSolution.getFifthsList(), cuttingPointsList);
            List<List<Fifth>> crossOverList1 = new ArrayList<>();
            List<List<Fifth>> crossOverList2 = new ArrayList<>();

            for (int i = 0; i < subLists1.size(); i++) {
                if (i % 2 == 0) {
                    crossOverList1.add(subLists1.get(i));
                    crossOverList2.add(subLists2.get(i));
                } else {
                    crossOverList1.add(subLists2.get(i));
                    crossOverList2.add(subLists1.get(i));
                }
            }

            TimeTableSolution solutionKid1 = new TimeTableSolution(this.getTimeTable(), crossOverList1);
            TimeTableSolution solutionKid2 = new TimeTableSolution(timeTableSolution.getTimeTable(), crossOverList2);
            res.add(solutionKid1);
            res.add(solutionKid2);

        }
        return res;
    }

    private List<List<Fifth>> chopIntoParts(List<Fifth> ls, List<Integer> cuttingPoints) {
        List<List<Fifth>> partsList = new ArrayList<>();
        Collections.sort(cuttingPoints);
        int min;
        int max = -1;
        for (Integer cuttingPoint : cuttingPoints) {
            min = max + 1;
            max = cuttingPoint;
            partsList.add(ls.subList(min, max));
        }
        if (max < ls.size() - 1) {
            min = max + 1;
            partsList.add(ls.subList(min, ls.size() - 1));
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
