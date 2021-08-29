package Mains.Util;

import solution.Fifth;
import solution.TimeTableSolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TimeTableSolutionDisplayer {

    Map<Integer, Map<Integer, Fifth>> mapMap = new HashMap<>(); //First key stands for hours, second key stands for days.
    ResultDisplay resultDisplay;

    public TimeTableSolutionDisplayer(TimeTableSolution solution, ResultDisplay resultDisplay) {
        this.resultDisplay = resultDisplay;
        List<Fifth> filteredSolutionList = null;
        switch (resultDisplay) {
            case RAW:
                filteredSolutionList = solution.getFifthsList();
                break;
            case CLASS:
                filteredSolutionList = solution.getFifthsList().stream().filter(fifth -> fifth.getSchoolClass().equals(resultDisplay.getId())).collect(Collectors.toList());
                break;
            case TEACHER:
                filteredSolutionList = solution.getFifthsList().stream().filter(fifth -> fifth.getTeacher().equals(resultDisplay.getId())).collect(Collectors.toList());
                break;
        }
        for (Fifth fifth : filteredSolutionList) {
            if (!mapMap.containsKey(fifth.getHour())) {
                mapMap.put(fifth.getHour(), new HashMap<>());
            }
            if (!mapMap.get(fifth.getHour()).containsKey(fifth.getDay())) {
                mapMap.get(fifth.getHour()).put(fifth.getDay(), fifth);
            }
        }
    }

    public String getDisplay(int hour, int day) {
        String res = "";
        if (mapMap.containsKey(hour)) {
            if (mapMap.get(hour).containsKey(day)) {
                res = resultDisplay.getDisplay(mapMap.get(hour).get(day));
            }
        }
        return res;
    }
}
