package Mains.Util;

import solution.Fifth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {
    Map<Integer, Fifth> fifthMap = new HashMap<>();
    int hour;
    ResultDisplay resultDisplay;

    public Row(int hour, ResultDisplay resultDisplay, List<Fifth> fifthList) {
        this.hour = hour;
        this.resultDisplay = resultDisplay;

        for (Fifth fifth : fifthList) {
            if (!fifthMap.containsKey(fifth.getDay())) {
                fifthMap.put(fifth.getDay(), fifth);
            }
        }

    }

    public String getDisplay(int day) {
        String res = "";
        if (fifthMap.containsKey(day)) {
            res = resultDisplay.getDisplay(fifthMap.get(day));
        }
        return res;
    }

    public int getHour() {
        return hour;
    }
}
