package evolutionaryApp.servlets;

import com.google.gson.Gson;
import descriptor.Descriptor;
import evolution.engine.EvolutionEngine;
import evolution.engine.problem_solution.Solution;
import evolution.util.Pair;
import evolution.util.Triplets;
import evolutionaryApp.utils.ServletUtils;
import evolutionaryApp.utils.SessionUtils;
import evolutionaryApp.utils.resultUtils.ResultDisplay;
import evolutionaryApp.utils.resultUtils.TimeTableResults;
import logicEngine.DescriptorManager.DescriptorManager;
import solution.TimeTableSolution;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pages/tablePage/all_users_results")
public class GetAllResultsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Gson gson = new Gson();
        Integer index = Integer.parseInt(request.getParameter("index"));

        DescriptorManager dManager = ServletUtils.getDescriptorManager(request.getServletContext());
        Descriptor descriptor = dManager.getDescriptor(index);

        List<Triplets<String, Double, Integer>> allUsersStatusList = new ArrayList<>();
        descriptor.getEngineMap().forEach((user_name, engine) -> {
            allUsersStatusList.add(new Triplets<>(
                    user_name,
                    engine.getMaxFitness(),
                    engine.getCurrentGenerationProperty()));
        });

        String json = gson.toJson(allUsersStatusList);
        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
