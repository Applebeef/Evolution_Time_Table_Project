package evolutionaryApp.servlets;

import com.google.gson.Gson;
import descriptor.Descriptor;
import evolution.engine.EvolutionEngine;
import evolution.util.Triplets;
import evolutionaryApp.utils.ServletUtils;
import logicEngine.DescriptorManager.DescriptorManager;
import time_table.TimeTable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@WebServlet("/pages/mainPage/tableListUpdate")
public class TableListUpdate extends HttpServlet {
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            DescriptorManager descriptorManager = ServletUtils.getDescriptorManager(req.getServletContext());
            List<Triplets<TimeTable, Integer, Double>> list = new ArrayList<>();
            synchronized (descriptorManager.getDescriptorList()) {
                descriptorManager.getDescriptorList().forEach(descriptor -> {
                    OptionalDouble bestFitness = descriptor.getEngineMap().values().stream().mapToDouble(EvolutionEngine::getBestSolutionFitness).max();
                    Triplets<TimeTable, Integer, Double> triplet = new Triplets<>(descriptor.getTimeTable(), descriptor.getEngineMap().size(), bestFitness.orElse(0));
                    list.add(triplet);
                });
            }
            String res = gson.toJson(list);
            out.println(res);
            out.flush();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
