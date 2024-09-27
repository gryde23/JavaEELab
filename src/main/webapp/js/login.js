
let urlForRedirect;
document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault(); // Предотвращаем стандартное поведение формы

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Формируем URL с параметрами
    const url = `LoginServlet?username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`;

    // Отправка данных на сервлет
    fetch(url, {
        method: 'GET'
    })
        .then(response => {
            urlForRedirect = response.url;
             return response.text()
        })
        .then(data => {
            if (data.includes("Success")) {
                // Сохраняем имя пользователя в localStorage
                localStorage.setItem('username', username);
                const currentDate = new Date().toLocaleString();
                localStorage.setItem('loginTime', currentDate);
                // Перенаправление на главную страницу
                window.location.href = "index.html";
            } else {
                window.location.href = urlForRedirect;
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
});
document.addEventListener("DOMContentLoaded", function() {
    console.log("Current URL: " + window.location.href);
    const urlParams = new URLSearchParams(window.location.search);
    const attempts = urlParams.get("attempts");

    if (attempts !== null && attempts !== '0') {
        document.getElementById('loginAttemptsMsg').innerText = "Incorrect login or password. Attempts left: " + attempts;
    }
});