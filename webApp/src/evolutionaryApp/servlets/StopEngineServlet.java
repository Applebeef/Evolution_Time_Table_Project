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


@WebServlet("/pages/tablePage/stopEngine")
public class StopEngineServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        Integer index = Integer.parseInt(request.getParameter("index"));
        String user = SessionUtils.getUsername(request);
        DescriptorManager descriptorManager = ServletUtils.getDescriptorManager(request.getServletContext());
        Descriptor descriptor = descriptorManager.getDescriptor(index);
        EvolutionEngine engine = descriptor.getEngine(user);

        if (engine != null && engine.isAlive()) {
            engine.interrupt();
            try {
                engine.join();
            } catch (Exception e) {

            }
        }
    }
}

