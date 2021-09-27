package evolutionaryApp.servlets;

import com.google.gson.Gson;
import descriptor.Descriptor;
import evolution.engine.EvolutionEngine;
import evolution.engine.problem_solution.Solution;
import evolution.util.Pair;
import evolutionaryApp.utils.ServletUtils;
import evolutionaryApp.utils.SessionUtils;
import evolutionaryApp.utils.engineDataUtils.Selections.SelectionsJSON;
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
import java.sql.Time;
import java.util.Collections;
import java.util.Comparator;

@WebServlet("/pages/tablePage/result")
public class ResultsServlet extends HttpServlet {
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
        String user = SessionUtils.getUsername(request);
        String str = request.getParameter("teacher_or_class");
        Integer id = Integer.parseInt(request.getParameter("Id"));


        DescriptorManager dManager = ServletUtils.getDescriptorManager(request.getServletContext());
        Descriptor descriptor = dManager.getDescriptor(index);
        EvolutionEngine engine = descriptor.getEngine(user);
        TimeTableResults results = null;
        TimeTableSolution timeTableSolution;
        Integer bestSolutionGeneration;
        if (engine != null) {
            synchronized (engine.getBestSolution()) {
                bestSolutionGeneration = engine.getBestSolution().getV1();
                timeTableSolution = (TimeTableSolution) engine.getBestSolution().getV2();
            }
            ResultDisplay resultDisplay;

            switch (str) {
                case "Teacher":
                    resultDisplay = ResultDisplay.TEACHER;
                    resultDisplay.setId(id);
                    results = new TimeTableResults(timeTableSolution, resultDisplay, id, bestSolutionGeneration);
                    break;
                case "Class":
                    resultDisplay = ResultDisplay.CLASS;
                    resultDisplay.setId(id);
                    results = new TimeTableResults(timeTableSolution, resultDisplay, id, bestSolutionGeneration);
                    break;
                default:
                    results = null;
                    break;
            }
            if (results != null) {
                timeTableSolution.getRuleGradeMap().forEach(results::addRuleToMap);
            }
        }
        String json = gson.toJson(results);
        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
