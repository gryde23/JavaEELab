package com.example.lab2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.PrintWriter;

@WebServlet("/SecTaskServlet")
public class SecTaskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        if (req.getSession().getAttribute("amountOfRequest") == null) {
            req.getSession().setAttribute("amountOfRequest", 1);
        } else {
            req.getSession().setAttribute(
                    "amountOfRequest", ((Integer) req.getSession().getAttribute("amountOfRequest")) + 1
            );
        }

        resp.setContentType("text/html");
        String sessionId = req.getParameter("id_session");
        String serverTime = req.getParameter("server_time");
        String amountOfRequest = req.getParameter("request_amount");

        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Second Task</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Second Task</h1>");
        if (sessionId != null) {
            out.println("<h2>Session ID: " + req.getSession().getId() + "</h2>");
        }
        if (serverTime != null) {
            out.println("<h2>Server Time: " + formattedDateTime + "</h2>");
        }
        if (amountOfRequest != null) {
            out.println("<h2>Request Amount: " + req.getSession().getAttribute("amountOfRequest") + "</h2>");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
