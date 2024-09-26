// Получаем имя пользователя из localStorage
const username = localStorage.getItem('username');
const navigation = document.getElementById('navigation');

if (username) {
    // Если пользователь авторизован, изменяем содержимое навигации
    navigation.innerHTML = `
            <h3>You are logged in as: ${username}</h3>
            <button id="signOutButton">Sign Out</button>
        `;

    // Добавляем обработчик события для кнопки "Sign Out"
    document.getElementById('signOutButton').addEventListener('click', function() {
        // Удаляем имя пользователя из localStorage
        localStorage.removeItem('username');
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