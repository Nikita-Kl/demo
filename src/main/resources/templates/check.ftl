<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Чек</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <div class="card">
        <div class="card-header">
            <h3>Чек покупки</h3>
        </div>
        <div class="card-body">
            <h5 class="card-title">Покупатель: ${userSale.user.username}</h5>
            <p class="card-text">Сообщение: ${userSale.message.title}</p>
            <p class="card-text">Описание: ${userSale.message.text}</p>
            <p class="card-text">Полное описание: ${userSale.message.full_text}</p>
            <p class="card-text">Тэги: ${userSale.message.tag}</p>
            <p class="card-text">Стоимость: ${userSale.cost}</p>
            <p class="card-text">Дата покупки: <span id="currentDate"></span></p>
        </div>
        <div class="card-footer">
            <a href="/" class="btn btn-primary">Вернуться на главную</a>
        </div>
    </div>
</div>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const currentDateElement = document.getElementById("currentDate");
        const currentDate = new Date();
        const formattedDate = currentDate.toLocaleString("ru-RU", {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
        currentDateElement.textContent = formattedDate;
    });
</script>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
