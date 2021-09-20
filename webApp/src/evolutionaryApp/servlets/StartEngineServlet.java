package evolutionaryApp.servlets;

import com.google.gson.Gson;
import evolution.configuration.CrossoverIFC;
import evolution.configuration.MutationIFC;
import evolution.configuration.SelectionIFC;
import evolution.engine.EvolutionEngine;
import evolutionaryApp.utils.engineDataUtils.Crossovers.CrossoversJSON;
import evolutionaryApp.utils.engineDataUtils.Mutations.MutationsJSON;
import evolutionaryApp.utils.engineDataUtils.Selections.SelectionsJSON;
import settings.*;

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
        String str = request.getParameter("selections");
        Integer initialPopulation = Integer.parseInt(request.getParameter("popSize"));
        Gson gson = new Gson();
        SelectionsJSON selections = gson.fromJson(str, SelectionsJSON.class);
        str = request.getParameter("crossovers");
        CrossoversJSON crossovers = gson.fromJson(str, CrossoversJSON.class);
        str = request.getParameter("mutations");
        MutationsJSON mutations = gson.fromJson(str, MutationsJSON.class);

        List<SelectionIFC> selectionsList = getSelectionsListFromJson(selections);
        List<CrossoverIFC> crossoverWrapperList = getCrossoverListFromJson(crossovers);
        List<MutationIFC> mutationWrapperList = getMutationsListFromJson(mutations);

        //TODO: Initiate the engine (need to add ending conditions)

        EvolutionEngine engine = new EvolutionEngine(initialPopulation, selectionsList, crossoverWrapperList, mutationWrapperList);
        //engine.initSolutionPopulation();

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
                        null,
                        1.0,
                        slc.getTournament().getIsActive()));
        return selectionsList;
    }

    private List<CrossoverIFC> getCrossoverListFromJson(CrossoversJSON crs) {
        List<CrossoverIFC> crossoverWrapperList = new ArrayList<>(2);
        crossoverWrapperList.add(
                new CrossoverWrapper(
                        Crossovers.DAY_TIME_ORIENTED,
                        null,
                        crs.getDayTimeOriented().getCuttingPoints()));
        crossoverWrapperList.add(
                new CrossoverWrapper(
                        Crossovers.ASPECT_ORIENTED,
                        crs.getAspectOriented().getAspect(),
                        crs.getAspectOriented().getCuttingPoints()));
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
