package evolutionaryApp.servlets;

import com.google.gson.Gson;
import evolutionaryApp.utils.ServletUtils;
import time_table.TimeTable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/pages/tablePage/getTableJSON")
public class getTableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int index = Integer.parseInt(req.getParameter("index"));
        TimeTable timeTable = ServletUtils.getDescriptorManager(req.getServletContext()).getTimeTable(index);
        Gson gson = new Gson();
        String json = gson.toJson(timeTable);
        try (PrintWriter out = resp.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
