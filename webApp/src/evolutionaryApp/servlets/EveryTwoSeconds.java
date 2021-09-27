package evolutionaryApp.servlets;

import com.google.gson.Gson;
import descriptor.Descriptor;
import evolution.engine.EvolutionEngine;
import evolution.util.Pair;
import evolution.util.Triplets;
import evolutionaryApp.utils.ServletUtils;
import evolutionaryApp.utils.SessionUtils;
import evolutionaryApp.utils.resultUtils.PullData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pages/tablePage/everyTwoSeconds")
public class EveryTwoSeconds extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Integer index = Integer.parseInt(req.getParameter("index"));
        Descriptor descriptor = ServletUtils.getDescriptorManager(req.getServletContext()).getDescriptor(index);
        EvolutionEngine engine = descriptor.getEngine(SessionUtils.getUsername(req));

        int currentGeneration;
        int bestSolutionGeneration;
        double bestSolutionFitness;
        boolean isAlive;
        boolean isPaused;
        PullData pullData = null;
        if (engine != null) {
            currentGeneration = engine.getCurrentGenerationProperty();
            synchronized (engine.getBestSolution()) {
                bestSolutionFitness = engine.getBestSolution().getV2().getFitness();
                bestSolutionGeneration = engine.getBestSolution().getV1();
            }
            isAlive = engine.isAlive();
            isPaused = engine.isEnginePaused();
            pullData = new PullData(currentGeneration, bestSolutionFitness, bestSolutionGeneration, isAlive, isPaused);
        }

        List<Triplets<String, Double, Integer>> allUsersStatusList = new ArrayList<>();
        descriptor.getEngineMap().forEach((user_name, map_engine) -> {
            allUsersStatusList.add(new Triplets<>(
                    user_name,
                    map_engine.getMaxFitness(),
                    map_engine.getCurrentGenerationProperty()));
        });

        Pair<PullData, List<Triplets<String, Double, Integer>>> pair = new Pair<>(pullData, allUsersStatusList);
        String json = new Gson().toJson(pair);
        try (PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
