package evolution.configuration;

import java.util.*;

public class EndingConditions {
    Map<EndingCondition, EndingConditionWrapper> endingConditions;

    public EndingConditions(int generationsMax, double fitnessMax, long timeMax) {
        endingConditions = new HashMap<>();
        endingConditions.put(EndingCondition.GENERATIONS, new EndingConditionWrapper(EndingCondition.GENERATIONS, generationsMax));
        endingConditions.put(EndingCondition.FITNESS, new EndingConditionWrapper(EndingCondition.FITNESS, fitnessMax));
        endingConditions.put(EndingCondition.TIME, new EndingConditionWrapper(EndingCondition.TIME, timeMax));
    }

    public boolean test(int generationCurrent, double fitnessCurrent, long timeCurrent) {
        return endingConditions.get(EndingCondition.FITNESS).test(fitnessCurrent) ||
                endingConditions.get(EndingCondition.GENERATIONS).test(generationCurrent) ||
                endingConditions.get(EndingCondition.TIME).test(timeCurrent);
    }

    public EndingConditionWrapper getEndingConditionWrapper(EndingCondition ec){
        return endingConditions.get(ec);
    }

    public void setEndingConditionsMax(EndingCondition ec, Number max){
        this.endingConditions.get(ec).setMax(max);
    }
}
