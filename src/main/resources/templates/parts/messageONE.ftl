<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Страница сообщений и отзывов</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .media img {
            max-width: 100px;
            margin-right: 15px;
        }

        .media-body h4, .media-body h5 {
            margin: 0;
        }

        .tag {
            display: inline-block;
            padding: 5px 10px;
            background-color: #007bff;
            color: #fff;
            border-radius: 5px;
            margin-top: 10px;
            font-size: 0.9em;
        }

        .cost {
            font-weight: bold;
            color: #28a745;
            margin-top: 10px;
        }

        .add-review, .reviews {
            margin-top: 30px;
        }

        .add-review h3, .reviews h3 {
            border-bottom: 2px solid #007bff;
            padding-bottom: 10px;
        }

        .btn-outline-primary {
            margin-top: 10px;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <ul class="list-group">
        <#if messageId??> <!-- проверка передачи id -->
            <#list messages as message>
                <#if message.id == messageId> <!-- проверка сходства -->
                    <li class="list-group-item">
                        <div class="media">
                            <#if message.filename??>
                                <img src="/img/${message.filename}" alt="${message.title!''}">
                            </#if>
                            <div class="media-body">
                                <#if message.title??>
                                    <h4>${message.title}</h4>
                                </#if>
                                <h5>${message.text}</h5>
                                <#if message.full_text??>
                                    <p>${message.full_text}</p>
                                </#if>
                                <span class="tag">${message.tag}</span>
                                <p class="cost">Стоимость: ${message.cost}</p> <!-- добавлено отображение стоимости -->
                            </div>
                        </div>
                        <form action="/user-sale/${userId}/add-sale" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <input type="hidden" name="userId" value="${user.id}">
                            <input type="hidden" name="messageId" value="${message.id}">
                            <input type="hidden" name="cost" value="${message.cost}">
                            <button type="submit" class="btn btn-outline-primary">Купить</button>
                        </form>
                    </li>
                    <#break>
                </#if>
            </#list>
        <#else> <!-- If no messageId is provided, display all messages -->
            <#list messages as message>
                <li class="list-group-item">
                    <div class="media">
                        <#if message.filename??>
                            <img src="/img/${message.filename}" alt="${message.title!''}">
                        </#if>
                        <div class="media-body">
                            <#if message.title??>
                                <h4>${message.title}</h4>
                            </#if>
                            <h5>${message.text}</h5>
                            <p>${message.tag}</p>
                            <p class="cost">Стоимость: ${message.cost}</p> <!-- добавлено отображение стоимости -->
                        </div>
                    </div>
                </li>
            </#list>
        </#if>
    </ul>

    <div class="reviews">
        <h3>Отзывы</h3>
        <ul class="list-group">
            <#list reviews as review>
                <li class="list-group-item">
                    <strong>${review.author_review.username}:</strong> ${review.review_text}
                </li>
            </#list>
        </ul>
    </div>

    <div class="add-review">
        <h3>Оставьте свой отзыв</h3>
        <form action="/user-sale/${userId}/add-review" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="hidden" name="messageId" value="${messageId}">
            <div class="form-group">
                <label for="reviewText">Текст отзыва</label>
                <textarea class="form-control" id="reviewText" name="reviewText" rows="3" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Опубликовать</button>
        </form>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
