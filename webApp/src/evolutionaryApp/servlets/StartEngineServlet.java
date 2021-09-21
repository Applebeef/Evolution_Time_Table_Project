package evolutionaryApp.servlets;

import com.google.gson.Gson;
import evolution.configuration.CrossoverIFC;
import evolution.configuration.MutationIFC;
import evolution.configuration.SelectionIFC;
import evolution.engine.EvolutionEngine;
import evolutionaryApp.utils.ServletUtils;
import evolutionaryApp.utils.SessionUtils;
import evolutionaryApp.utils.engineDataUtils.Crossovers.CrossoversJSON;
import evolutionaryApp.utils.engineDataUtils.EndingConditionsJSON;
import evolutionaryApp.utils.engineDataUtils.Mutations.MutationsJSON;
import evolutionaryApp.utils.engineDataUtils.Selections.SelectionsJSON;
import logicEngine.DescriptorManager.DescriptorManager;
import settings.*;
import time_table.TimeTable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pages/tablePage/startEngine")
public class StartEngineServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Integer index = Integer.parseInt(request.getParameter("index"));
        Integer initialPopulation = Integer.parseInt(request.getParameter("popSize"));
        Integer frequency = Integer.parseInt(request.getParameter("frequency"));


        String str = request.getParameter("selections");
        SelectionsJSON selections = gson.fromJson(str, SelectionsJSON.class);
        str = request.getParameter("crossovers");
        CrossoversJSON crossovers = gson.fromJson(str, CrossoversJSON.class);
        str = request.getParameter("mutations");
        MutationsJSON mutations = gson.fromJson(str, MutationsJSON.class);
        str = request.getParameter("endingConditions");
        EndingConditionsJSON endingConditionsJSON = gson.fromJson(str, EndingConditionsJSON.class);

        List<SelectionIFC> selectionsList = getSelectionsListFromJson(selections);
        List<CrossoverIFC> crossoverWrapperList = getCrossoverListFromJson(crossovers);
        List<MutationIFC> mutationWrapperList = getMutationsListFromJson(mutations);
        DescriptorManager descriptorManager = ServletUtils.getDescriptorManager(request.getServletContext());
        TimeTable timeTable = descriptorManager.getTimeTable(index);

        // Initiate the engine:
        EvolutionEngine engine = new EvolutionEngine(
                selectionsList,
                crossoverWrapperList,
                mutationWrapperList,
                initialPopulation);
        engine.initThreadParameters(
                frequency,
                endingConditionsJSON.getGenerations(),
                endingConditionsJSON.getFitness(),
                endingConditionsJSON.getTime()
        );
        engine.initSolutionPopulation(timeTable);
        engine.setName("Engine " + index);
        engine.start();

        // Save thread at TimeTable:
        descriptorManager.getDescriptor(index).addEvolutionEngine(SessionUtils.getUsername(request), engine);
    }

    private List<SelectionIFC> getSelectionsListFromJson(SelectionsJSON slc) {
        List<SelectionIFC> selectionsList = new ArrayList<>(3);
        selectionsList.add(
                new SelectionWrapper(
                        Selections.TRUNCATION,
                        slc.getTruncation().getTopPercent(),
                        slc.getTruncation().getElitism(),
                        null,
                        slc.getTruncation().getIsActive()));
        selectionsList.add(
                new SelectionWrapper(
                        Selections.ROULETTE_WHEEL,
                        null,
                        slc.getRouletteWheel().getElitism(),
                        null,
                        slc.getRouletteWheel().getIsActive()));
        selectionsList.add(
                new SelectionWrapper(
                        Selections.TOURNAMENT,
                        null,
                        slc.getTournament().getElitism(),
                        slc.getTournament().getPte(),
                        slc.getTournament().getIsActive()));
        return selectionsList;
    }

    private List<CrossoverIFC> getCrossoverListFromJson(CrossoversJSON crs) {
        List<CrossoverIFC> crossoverWrapperList = new ArrayList<>(2);
        Boolean b = crs.getDayTimeOriented().getIsActive();
        crossoverWrapperList.add(
                new CrossoverWrapper(
                        Crossovers.DAY_TIME_ORIENTED,
                        null,
                        crs.getDayTimeOriented().getCuttingPoints(),
                        crs.getDayTimeOriented().getIsActive()));
        b = crs.getAspectOriented().getIsActive();
        crossoverWrapperList.add(
                new CrossoverWrapper(
                        Crossovers.ASPECT_ORIENTED,
                        crs.getAspectOriented().getAspect(),
                        crs.getAspectOriented().getCuttingPoints(),
                        crs.getAspectOriented().getIsActive()));
        return crossoverWrapperList;
    }

    private List<MutationIFC> getMutationsListFromJson(MutationsJSON mts) {
        List<MutationIFC> mutationsWrapperList = new ArrayList<>(2);
        mutationsWrapperList.add(
                new MutationWrapper(
                        Mutation.Flipping,
                        mts.getFlipping().getProbability(),
                        mts.getFlipping().getTupples(),
                        mts.getFlipping().getComponent()));
        mutationsWrapperList.add(
                new MutationWrapper(
                        Mutation.Sizer,
                        mts.getSizer().getProbability(),
                        mts.getSizer().getTupples(),
                        null));
        return mutationsWrapperList;
    }

}
