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

@WebServlet("/pages/mainPage/enterTable")
public class redirectToTable extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        int index = Integer.parseInt(req.getParameter("index"));
        resp.sendRedirect("../tablePage/tablePage.html?index=" + index);
    }
}
