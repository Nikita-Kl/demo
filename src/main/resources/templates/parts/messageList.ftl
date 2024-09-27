<#include "security.ftl">
<style>
    .message-card {
        border: 1px solid #ddd;
        border-radius: 4px;
        padding: 10px;
        margin-bottom: 10px;
        display: flex; /* Добавляем flex display для выравнивания элементов в строку */
        align-items: center; /* Выравнивание элементов по центру по вертикали */
    }
    .message-img {
        width: 64px; /* Ширина картинки */
        height: 64px; /* Высота картинки */
        border-radius: 0; /* Убираем закругление, делаем картинку квадратной */
        margin-right: 15px; /* Отступ справа */
    }
    .message-title {
        font-size: 18px;
        font-weight: bold;
    }
    .message-text {
        font-size: 16px;
        color: #666;
    }
    .message-tag {
        display: inline-block;
        padding: 5px;
        background-color: #007bff;
        color: #fff;
        border-radius: 10px;
        margin-top: 5px;
    }
    .message-actions {
        margin-top: 10px;
    }
    .message-cost {
        font-size: 16px;
        font-weight: bold;
        color: #28a745;
        margin-top: 10px;
    }
</style>


<div class="container">
    <ul class="list-group">
        <#list messages as message>
            <li class="list-group-item">
                <div class="media message-card">
                    <#if message.filename??>
                        <img src="/img/${message.filename}" class="mr-3 message-img" alt="Message Image">
                    </#if>
                    <div class="media-body">
                        <h4 class="message-title">${message.title!''}</h4>
                        <p class="message-text">${message.text}</p>
                        <span class="message-tag">${message.tag}</span>
                        <div class="message-cost">Стоимость: ${message.cost} тг.</div>
                        <div class="message-actions">
                            <a class="btn btn-outline-primary"
                               href="/user-salee/${message.author.id}?message=${message.id}">Подробнее</a>
                            From: <a href="/user-messages/${message.author.id}">${message.authorName}</a>
                            <#if message.author.id == user.getId()>
                                <a class="btn btn-outline-primary"
                                   href="/user-messages/${message.author.id}?message=${message.id}">Редактировать</a>
                            </#if>
                        </div>
                    </div>
                </div>
            </li>
        <#else>
            <div class="alert alert-warning">У вас ещё не создано ни одного предложения.</div>
        </#list>
    </ul>
</div>
