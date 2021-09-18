package evolutionaryApp.servlets;

import com.google.gson.Gson;
import evolutionaryApp.constants.Constants;
import evolutionaryApp.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class checkUsername extends HttpServlet {
    private final String CHAT_ROOM_URL = "pages/mainPage/mainPage.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        if (SessionUtils.getUsername(req) != null) {
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(CHAT_ROOM_URL);

            try (PrintWriter out = resp.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
        }
    }
}
