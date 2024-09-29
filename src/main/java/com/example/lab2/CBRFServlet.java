package com.example.lab2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebServlet("/cbrf")
public class CBRFServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "http://cbr.ru/scripts/";

        // Чтение данных из тела запроса
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        // Преобразование JSON в Map
        Gson gson = new Gson();
        Map<String, String> data = gson.fromJson(sb.toString(), new TypeToken<Map<String, String>>(){}.getType());

        // Извлечение параметров
        String category = data.get("category");
        String subCategory = data.get("subCategory");
        String startDate = data.get("startDate");
        String endDate = data.get("endDate");

        startDate = convertDateFormat(startDate);
        endDate = convertDateFormat(endDate);

        if(category.equals("metals")) {
            url += "xml_metall.asp?date_req1=" + startDate + "&date_req2=" + endDate;
        } else {
            url += "XML_dynamic.asp?date_req1=" + startDate + "&date_req2=" + endDate
                    + "&VAL_NM_RQ=R01235";
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Проверка кода ответа
            if (httpResponse.statusCode() == 200) { // 200 OK
                // Установка типа контента и отправка ответа обратно клиенту
                response.setContentType("application/xml"); // Установите нужный тип контента
                PrintWriter out = response.getWriter();
                out.print(httpResponse.body());
                out.flush();
            } else {
                response.sendError(httpResponse.statusCode(), "Ошибка при получении данных с API: " + httpResponse.body());
            }
        } catch (IOException | InterruptedException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка сервера: " + e.getMessage());
        }
    }
    public static String convertDateFormat(String dateStr) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
