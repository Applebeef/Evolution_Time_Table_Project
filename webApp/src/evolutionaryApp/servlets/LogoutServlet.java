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
import logicEngine.users.UserManager;
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

@WebServlet("/pages/tablePage/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession != null) {
            System.out.println("Clearing session for " + usernameFromSession);
            userManager.removeUser(usernameFromSession);
            SessionUtils.clearSession(request);

            /*
            when sending redirect, you need to think weather its relative or not.
            (you can read about it here: https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/http/HttpServletResponse.html#sendRedirect(java.lang.String))

            the best way (IMO) is to fetch the context path dynamically and build the redirection from it and on
             */

            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }
}