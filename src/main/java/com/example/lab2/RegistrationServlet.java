package com.example.lab2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleReg(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleReg(req,resp);
    }

    protected void handleReg(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        System.out.println(login);
        System.out.println(password);
        if (checkUser(login)){
            resp.getWriter().println("User already exists");
            return;
        }
        String hashedPassword = hashPassword(password);
        System.out.println(hashedPassword);
        addUserToXML(login, hashedPassword);
        // Получаем текущую дату и время
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Передаем сообщение о успешной регистрации с текущей датой и временем через параметр запроса
        resp.sendRedirect("index.html?message=Registration successful at " + currentDateTime);
    }

    private boolean checkUser(String login){
        try {
            String filePath = "C:\\Users\\Денис\\IdeaProjects\\Lab2\\src\\main\\resources\\XML\\users.xml";
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("XML file does not exist.");
                return false; // Файл не существует, значит пользователь не найден
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList users = document.getElementsByTagName("user");
            if (users.getLength() == 0) {
                System.out.println("No users found in the XML file.");
                return false; // Пользователи не найдены
            }
            for (int i = 0; i < users.getLength(); i++) {
                String existingLogin = users.item(i).getChildNodes().item(0).getTextContent();
                if (existingLogin.equals(login)) {
                    return true; // Пользователь уже существует
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private void addUserToXML(String login, String hashedPassword){
        String filePath = "C:\\Users\\Денис\\IdeaProjects\\Lab2\\src\\main\\resources\\XML\\users.xml";
        File xmlFile = new File(filePath);

        try {
            // Шаг 1: Создаем экземпляр DocumentBuilderFactory
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Шаг 2: Загружаем существующий XML файл
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Шаг 3: Создаем новый элемент
            Element userElement = doc.createElement("user");
            Element usernameElement = doc.createElement("username");
            usernameElement.appendChild(doc.createTextNode(login));
            Element passwordElement = doc.createElement("password");
            passwordElement.appendChild(doc.createTextNode(hashedPassword));
            userElement.appendChild(usernameElement);
            userElement.appendChild(passwordElement);

            // Шаг 4: Добавляем новый элемент к корневому элементу
            doc.getDocumentElement().appendChild(userElement);

            // Шаг 5: Сохраняем изменения в XML файл
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            System.out.println("Запись успешно добавлена.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
