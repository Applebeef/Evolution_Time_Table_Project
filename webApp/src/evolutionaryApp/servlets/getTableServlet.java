package evolutionaryApp.servlets;

import com.google.gson.Gson;
import descriptor.Descriptor;
import evolution.configuration.*;
import evolution.engine.EvolutionEngine;
import evolutionaryApp.utils.ServletUtils;
import evolutionaryApp.utils.SessionUtils;
import evolutionaryApp.utils.engineDataUtils.Crossovers.AspectOriented;
import evolutionaryApp.utils.engineDataUtils.Crossovers.CrossoversJSON;
import evolutionaryApp.utils.engineDataUtils.Crossovers.DayTimeOriented;
import evolutionaryApp.utils.engineDataUtils.EndingConditionsJSON;
import evolutionaryApp.utils.engineDataUtils.EngineData;
import evolutionaryApp.utils.engineDataUtils.Mutations.Flipping;
import evolutionaryApp.utils.engineDataUtils.Mutations.MutationsJSON;
import evolutionaryApp.utils.engineDataUtils.Mutations.Sizer;
import evolutionaryApp.utils.engineDataUtils.Selections.RouletteWheel;
import evolutionaryApp.utils.engineDataUtils.Selections.SelectionsJSON;
import evolutionaryApp.utils.engineDataUtils.Selections.Tournament;
import evolutionaryApp.utils.engineDataUtils.Selections.Truncation;
import settings.CrossoverWrapper;
import settings.MutationWrapper;
import settings.SelectionWrapper;
import time_table.TimeTable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/pages/tablePage/getTableJSON")
public class getTableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = Integer.parseInt(req.getParameter("index"));
        Descriptor descriptor = ServletUtils.getDescriptorManager(req.getServletContext()).getDescriptor(index);
        TimeTable timeTable = descriptor.getTimeTable();
        CrossoversJSON crossoversJSON = null;
        SelectionsJSON selectionsJSON = null;
        MutationsJSON mutationsJSON = null;
        EndingConditionsJSON endingConditionsJSON = null;
        Integer popSize = null;
        EvolutionEngine evolutionEngine = descriptor.getEngine(SessionUtils.getUsername(req));
        if (evolutionEngine != null) {
            selectionsJSON = createSelectionJSON(evolutionEngine.getSelectionIFCList());
            crossoversJSON = createCrossoverJSON(evolutionEngine.getCrossoverIFCList());
            mutationsJSON = createMutationsJSON(evolutionEngine.getMutationIFCList());
            endingConditionsJSON = createEndingConditionsJSON(evolutionEngine.getEndingConditions());
            popSize = evolutionEngine.getInitialSolutionPopulation().getSize();
        }
        EngineData engineData = new EngineData(timeTable, crossoversJSON, selectionsJSON, mutationsJSON, endingConditionsJSON, popSize);
        Gson gson = new Gson();
        String json = gson.toJson(engineData);
        try (PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    private EndingConditionsJSON createEndingConditionsJSON(EndingConditions endingConditions) {
        EndingConditionsJSON endingConditionsJSON = new EndingConditionsJSON();
        int fitness = 0;
        int generations = 100;
        long time = 0;
        for (EndingCondition endingCondition : EndingCondition.values()) {
            switch (endingCondition) {
                case TIME:
                    time = endingConditions.getEndingConditionWrapper(endingCondition).getMax().longValue();
                    break;
                case FITNESS:
                    fitness = endingConditions.getEndingConditionWrapper(endingCondition).getMax().intValue();
                    break;
                case GENERATIONS:
                    generations = endingConditions.getEndingConditionWrapper(endingCondition).getMax().intValue();
                    break;
            }
        }
        endingConditionsJSON.setFitness(fitness);
        endingConditionsJSON.setGenerations(generations);
        endingConditionsJSON.setTime(time);
        return endingConditionsJSON;
    }

    private MutationsJSON createMutationsJSON(List<MutationIFC> mutationIFCList) {
        MutationsJSON mutationsJSON = new MutationsJSON();
        Flipping flipping = new Flipping();
        Sizer sizer = new Sizer();
        for (MutationIFC mutationIFC : mutationIFCList) {
            if (mutationIFC instanceof MutationWrapper) {
                MutationWrapper wrapper = (MutationWrapper) mutationIFC;
                switch (wrapper.getName()) {
                    case "Flipping":
                        flipping.setComponent(wrapper.getComponent());
                        flipping.setProbability(wrapper.getProbability());
                        flipping.setTupples(wrapper.getTupples());
                        break;
                    case "Sizer":
                        sizer.setProbability(wrapper.getProbability());
                        sizer.setTupples(wrapper.getTupples());
                        break;
                }
            } else
                return null;
        }
        mutationsJSON.setFlipping(flipping);
        mutationsJSON.setSizer(sizer);
        return mutationsJSON;
    }

    private CrossoversJSON createCrossoverJSON(List<CrossoverIFC> crossoverIFCList) {
        CrossoversJSON crossoversJSON = new CrossoversJSON();
        AspectOriented aspectOriented = new AspectOriented();
        DayTimeOriented dayTimeOriented = new DayTimeOriented();
        for (CrossoverIFC crossoverIFC : crossoverIFCList) {
            if (crossoverIFC instanceof CrossoverWrapper) {
                CrossoverWrapper wrapper = (CrossoverWrapper) crossoverIFC;
                switch (wrapper.getName()) {
                    case "DayTimeOriented":
                        dayTimeOriented.setCuttingPoints(wrapper.getCuttingPoints());
                        dayTimeOriented.setIsActive(wrapper.isActive());
                        break;
                    case "AspectOriented":
                        aspectOriented.setAspect(wrapper.getOrientation());
                        aspectOriented.setCuttingPoints(wrapper.getCuttingPoints());
                        aspectOriented.setIsActive(wrapper.isActive());
                        break;
                }
            } else
                return null;
        }
        crossoversJSON.setAspectOriented(aspectOriented);
        crossoversJSON.setDayTimeOriented(dayTimeOriented);
        return crossoversJSON;
    }

    private SelectionsJSON createSelectionJSON(List<SelectionIFC> selectionIFCList) {
        SelectionsJSON selectionsJSON = new SelectionsJSON();
        Truncation truncation = new Truncation();
        RouletteWheel rouletteWheel = new RouletteWheel();
        Tournament tournament = new Tournament();
        for (SelectionIFC selectionIFC : selectionIFCList) {
            if (selectionIFC instanceof SelectionWrapper) {
                SelectionWrapper wrapper = (SelectionWrapper) selectionIFC;
                switch (wrapper.getType()) {
                    case "Truncation":
                        truncation.setIsActive(wrapper.isActive());
                        truncation.setElitism(wrapper.getElitism());
                        truncation.setTopPercent(wrapper.getTopPercent());
                        break;
                    case "RouletteWheel":
                        rouletteWheel.setIsActive(wrapper.isActive());
                        rouletteWheel.setElitism(wrapper.getElitism());
                        break;
                    case "Tournament":
                        tournament.setElitism(wrapper.getElitism());
                        tournament.setIsActive(wrapper.isActive());
                        tournament.setPte(wrapper.getPte());
                        break;
                }
            } else {
                return null;
            }
        }
        selectionsJSON.setTruncation(truncation);
        selectionsJSON.setRouletteWheel(rouletteWheel);
        selectionsJSON.setTournament(tournament);
        return selectionsJSON;
    }
}
