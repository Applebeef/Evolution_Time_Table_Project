package evolutionaryApp.servlets;

import com.google.gson.Gson;
import descriptor.Descriptor;
import evolution.configuration.CrossoverIFC;
import evolution.configuration.MutationIFC;
import evolution.configuration.SelectionIFC;
import evolution.engine.EvolutionEngine;
import evolutionaryApp.utils.ServletUtils;
import evolutionaryApp.utils.SessionUtils;
import logicEngine.DescriptorManager.DescriptorManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/pages/tablePage/getEngineUpdates")
public class engineUpdates extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = SessionUtils.getUsername(request);
        Integer index = Integer.parseInt(request.getParameter("index"));
        DescriptorManager manager = ServletUtils.getDescriptorManager(request.getServletContext());
        Descriptor descriptor = manager.getDescriptor(index);
        EvolutionEngine engine = descriptor.getEngine(username);
        Gson gson = new Gson();
        String json = gson.toJson(engine);//FIXME stackoverflow
        System.out.println(json);
        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
