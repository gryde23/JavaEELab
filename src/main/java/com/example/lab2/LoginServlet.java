package com.example.lab2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final int MAX_ATTEMPTS = 3;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleLogin(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleLogin(req, resp);
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();
        Integer attempts = (Integer) session.getAttribute("loginAttempts");
        if (attempts == null) {
            attempts = 0;
        }

        if (attempts >= MAX_ATTEMPTS) {
            resp.sendRedirect("index.html?message=You are blocked due to too many failed login attempts.");
            return;
        }

        if (validateUser(username, password)) {
            // Сбросить счетчик при успешном входе
            session.removeAttribute("loginAttempts");
            resp.getWriter().println("Success");
        } else {
            // Увеличиваем счетчик неудачных попыток
            attempts++;
            System.out.println("Attempt " + attempts);
            session.setAttribute("loginAttempts", attempts);
            resp.sendRedirect("login.html?attempts=" + attempts);
        }
    }


    private boolean validateUser(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        try {
            String filePath = "C:\\Users\\Денис\\IdeaProjects\\Lab2\\src\\main\\resources\\XML\\users.xml";
            FileInputStream inputStream = new FileInputStream(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);


            NodeList users = document.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                String xmlUsername = users.item(i).getChildNodes().item(0).getTextContent();
                String xmlPassword = users.item(i).getChildNodes().item(1).getTextContent();
                if (xmlUsername.equals(username) && BCrypt.checkpw(password, xmlPassword)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
