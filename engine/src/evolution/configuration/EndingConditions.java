package evolution.configuration;

import java.util.Arrays;
import java.util.List;

public class EndingConditions {
    List<EndingCondition> endingConditions;

    public EndingConditions(int generationsMax, double fitnessMax, long timeMax) {
        endingConditions = Arrays.asList(EndingCondition.values());
        EndingCondition.FITNESS.setMax(fitnessMax);
        EndingCondition.GENERATIONS.setMax(generationsMax);
        EndingCondition.TIME.setMax(timeMax);

    }

    public boolean test(int generationCurrent, double fitnessCurrent, long timeCurrent) {
        return EndingCondition.FITNESS.test(fitnessCurrent) || EndingCondition.GENERATIONS.test(generationCurrent) || EndingCondition.TIME.test(timeCurrent);
    }

}
