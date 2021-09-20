package evolutionaryApp.servlets;

import com.google.gson.Gson;
import evolutionaryApp.utils.engineDataUtils.Crossovers.CrossoversJSON;
import evolutionaryApp.utils.engineDataUtils.Mutations.MutationsJSON;
import evolutionaryApp.utils.engineDataUtils.Selections.SelectionsJSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    }

}
