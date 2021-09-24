package evolutionaryApp.servlets;

import com.google.gson.Gson;
import evolution.engine.EvolutionEngine;
import evolutionaryApp.utils.ServletUtils;
import evolutionaryApp.utils.SessionUtils;
import evolutionaryApp.utils.resultUtils.pullData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/pages/tablePage/genAndFitness")
public class genAndFitness extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Integer index = Integer.parseInt(req.getParameter("index"));
        EvolutionEngine engine = ServletUtils.getDescriptorManager(req.getServletContext()).getDescriptor(index).getEngine(SessionUtils.getUsername(req));
        int currentGeneration;
        double bestSolutionFitness;
        boolean isAlive;
        pullData pullData = null;
        if (engine != null) {
            currentGeneration = engine.getCurrentGenerationProperty();
            bestSolutionFitness = engine.getBestSolutionFitness();
            isAlive = engine.isAlive();
            pullData = new pullData(currentGeneration, bestSolutionFitness, isAlive);
        }
        String json = new Gson().toJson(pullData);
        System.out.println(json);
        try (PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
