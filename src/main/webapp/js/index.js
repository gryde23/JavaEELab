// Получаем имя пользователя из localStorage
const username = localStorage.getItem('username');
const loginTime = localStorage.getItem('loginTime');
const navigation = document.getElementById('navigation');

if (username) {
    // Если пользователь авторизован, изменяем содержимое навигации
    navigation.innerHTML = `
            <h3>You are logged in as: ${username}</h3>
            <label style="color: lightgray; font-size: 1em;">${loginTime}</label>
            <button id="signOutButton">Sign Out</button>
        `;

    // Добавляем обработчик события для кнопки "Sign Out"
    document.getElementById('signOutButton').addEventListener('click', function() {
        // Удаляем имя пользователя из localStorage
        localStorage.removeItem('username');
        localStorage.removeItem('loginTime');
        // Перезагружаем страницу, чтобы вернуть исходное состояние
        location.reload();
    });
} else {
    // Если пользователь не авторизован, показываем кнопки входа и регистрации
    navigation.innerHTML = `
            <button id="logButton" onclick="location.href='login.html'">Login</button>
            <button id="regButton" onclick="location.href='registration.html'">Registration</button>
        `;
}

// Получаем параметры URL
const urlParams = new URLSearchParams(window.location.search);
const message = urlParams.get('message');

// Если сообщение существует, отображаем его
if (message) {
    document.getElementById('message').innerText = message;
}