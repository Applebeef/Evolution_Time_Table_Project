package evolutionaryApp.servlets;

import com.google.gson.Gson;
import evolutionaryApp.constants.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class checkUsername extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String jsonResponse;
        if (req.getSession(false) == null) {
            jsonResponse = gson.toJson(null);
        } else {
            jsonResponse = gson.toJson(req.getSession(false).getAttribute(Constants.USERNAME));
        }
        System.out.println(jsonResponse);
        try (PrintWriter out = resp.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }
}
