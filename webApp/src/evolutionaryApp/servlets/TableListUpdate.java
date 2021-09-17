package evolutionaryApp.servlets;

import com.google.gson.Gson;
import evolutionaryApp.utils.ServletUtils;
import logicEngine.TimeTableManager.TimeTableManager;
import time_table.TimeTable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/tableListUpdate")
public class TableListUpdate extends HttpServlet {
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            TimeTableManager tableManager = ServletUtils.getTimeTableManager(getServletContext());
            List<TimeTable> list = tableManager.getTimeTables();
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
