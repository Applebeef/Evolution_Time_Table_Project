package evolutionaryApp.servlets;

import com.google.gson.Gson;
import descriptor.Descriptor;
import evolution.engine.EvolutionEngine;
import evolution.engine.problem_solution.Solution;
import evolution.util.Pair;
import evolutionaryApp.utils.ServletUtils;
import evolutionaryApp.utils.SessionUtils;
import logicEngine.DescriptorManager.DescriptorManager;
import solution.TimeTableSolution;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;

@WebServlet("/pages/tablePage/result")
public class ResultsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Integer index = Integer.parseInt(request.getParameter("index"));
        String user = SessionUtils.getUsername(request);

        DescriptorManager dManager = ServletUtils.getDescriptorManager(request.getServletContext());
        Descriptor descriptor = dManager.getDescriptor(index);
        EvolutionEngine engine = descriptor.getEngine(user);
        Pair<Integer, Solution> pair = engine.getBestSolution();

        String json = gson.toJson(pair);
        System.out.println(json);
        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        } catch (IOException e) {}
    }
}
