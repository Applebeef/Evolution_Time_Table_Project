package evolutionaryApp.utils.resultUtils;

import solution.Fifth;
import solution.TimeTableSolution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TimeTableResults {
    Map<Integer, Row> mapMap = new HashMap<>(); //First key stands for hours, second key stands for days.
    Map<String, Double> ruleScoreMap = new HashMap<>(); // First key stands for rule name, second key stands for fitness.
    ResultDisplay resultDisplay;

    public TimeTableResults(TimeTableSolution solution, ResultDisplay resultDisplay) {
        this.resultDisplay = resultDisplay;
        List<Fifth> filteredSolutionList = null;
        synchronized (solution){
            switch (resultDisplay) {
                case CLASS:
                    filteredSolutionList = solution.getFifthsList().stream().filter(fifth -> fifth.getSchoolClass().equals(resultDisplay.getId())).collect(Collectors.toList());
                    break;
                case TEACHER:
                    filteredSolutionList = solution.getFifthsList().stream().filter(fifth -> fifth.getTeacher().equals(resultDisplay.getId())).collect(Collectors.toList());
                    break;
            }
        }
        for (int i = 1; i <= solution.getTimeTable().getHours(); i++) {
            int finalI = i;
            if (!mapMap.containsKey(i)) {
                mapMap.put(i, new Row(i, resultDisplay, filteredSolutionList.stream().filter(fifth -> fifth.getHour().equals(finalI)).collect(Collectors.toList())));
            }
        }
    }

    public Row getDisplay(int hour) {
        return mapMap.getOrDefault(hour, null);
    }
}
