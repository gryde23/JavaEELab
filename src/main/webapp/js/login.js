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
        .then(response => response.text())
        .then(data => {
            if (data.trim() === "Success") {
                // Сохраняем имя пользователя в localStorage
                localStorage.setItem('username', username);
                // Перенаправление на главную страницу
                window.location.href = "index.html";
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
});