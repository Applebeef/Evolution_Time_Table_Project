package evolutionaryApp.utils.engineDataUtils;

import evolutionaryApp.utils.engineDataUtils.Crossovers.CrossoversJSON;
import evolutionaryApp.utils.engineDataUtils.Mutations.MutationsJSON;
import evolutionaryApp.utils.engineDataUtils.Selections.SelectionsJSON;
import time_table.TimeTable;

public class EngineData {
    CrossoversJSON crossoversJSON;
    SelectionsJSON selectionsJSON;
    MutationsJSON mutationsJSON;
    EndingConditionsJSON endingConditionsJSON;
    TimeTable timeTable;
    Integer popSize;
    Integer frequency;
    Boolean isAlive;

    public EngineData(TimeTable timeTable, CrossoversJSON crossoversJSON, SelectionsJSON selectionsJSON,
                      MutationsJSON mutationsJSON, EndingConditionsJSON endingConditionsJSON, Integer popSize, Integer frequency, Boolean isAlive) {
        this.crossoversJSON = crossoversJSON;
        this.selectionsJSON = selectionsJSON;
        this.mutationsJSON = mutationsJSON;
        this.endingConditionsJSON = endingConditionsJSON;
        this.timeTable = timeTable;
        this.popSize = popSize;
        this.frequency = frequency;
        this.isAlive = isAlive;
    }
}
