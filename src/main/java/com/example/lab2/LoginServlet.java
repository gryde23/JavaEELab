package com.example.lab2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if(validateUser(username, password)){
            resp.getWriter().println("Success");
        } else {
            resp.getWriter().println("Fail");
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
