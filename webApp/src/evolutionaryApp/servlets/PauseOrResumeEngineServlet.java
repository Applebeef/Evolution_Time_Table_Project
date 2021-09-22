package evolutionaryApp.servlets;

import descriptor.Descriptor;
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

@WebServlet("/pages/tablePage/pause")
public class PauseOrResumeEngineServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response){
        Integer index = Integer.parseInt(request.getParameter("index"));
        String userName = SessionUtils.getUsername(request);

        Descriptor descriptor = ServletUtils.getDescriptorManager(request.getServletContext()).getDescriptor(index);
        EvolutionEngine engine = descriptor.getEngine(userName);
        // Pause or Resume the engine:
        engine.pauseOrResumeEngine();
    }
}
