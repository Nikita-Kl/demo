<#import "parts/common.ftl" as c>
<@c.page>
    <style>
        .message-card {
            border: 2px solid #0056b3;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            padding: 20px;
            margin-bottom: 20px;
            background-color: #ffffff;
        }
        .message-img {
            width: 64px;
            height: 64px;
            border-radius: 50%;
            margin-right: 15px;
        }
        .message-title {
            font-size: 20px;
            font-weight: bold;
            color: #333;
        }
        .message-text {
            font-size: 16px;
            color: #555;
            line-height: 1.5;
        }
        .message-tag {
            display: inline-block;
            padding: 5px 10px;
            background-color: #0056b3;
            color: #fff;
            border-radius: 15px;
            font-size: 14px;
        }
        .message-actions {
            margin-top: 15px;
        }
        .message-actions a {
            text-decoration: none;
            color: #0056b3;
            margin-right: 10px;
        }
        .message-actions a:hover {
            color: #003580;
        }
        .form-inline input[type="text"] {
            width: auto;
            margin-right: 10px;
        }
        .alert {
            border-radius: 8px;
            padding: 15px;
            border: 1px solid #0056b3;
        }
        /* Adding padding and margin to separate messageEdit and messageList */
        #messageEditSection, #messageListSection {
            margin-top: 30px;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        .message-cost {
            font-size: 16px;
            font-weight: bold;
            color: #28a745;
            margin-top: 10px;
        }
    </style>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Поиск по названию">
                <button type="submit" class="btn btn-primary">Поиск</button>
            </form>
        </div>
    </div>

    <#if missingMessages?? && (missingMessages?size > 0)>
        <div class="alert alert-warning" role="alert">
            <ul class="list-group">
                <#list missingMessages as message>
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
                                    <a class="btn btn-outline-primary" href="/user-messages/${message.author.id}?message=${message.id}">
                                        Подробнее</a>
                                    От: <a href="/user-messages/${message.author.id}">${message.authorName}</a>
                                    <#if message.author.id == user.getId()>
                                        <a class="btn btn-outline-primary"
                                           href="/user-messages/${message.author.id}?edit=true&message=${message.id}">Редактировать</a>
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </li>
                </#list>
            </ul>
        </div>
    <#elseIf randomMessage??>
        <div class="alert alert-info" role="alert">
            <div class="list-group">
                <li class="list-group-item">
                    <div class="media message-card">
                        <#if randomMessage.filename??>
                            <img src="/img/${randomMessage.filename}" class="mr-3 message-img" alt="Message Image">
                        </#if>
                        <div class="media-body">
                            <h4 class="message-title">${randomMessage.title!''}</h4>
                            <p class="message-text">${randomMessage.text}</p>
                            <div class="message-cost">Стоимость: ${randomMessage.cost} тг.</div>
                            <span class="message-tag">${randomMessage.tag}</span>
                            <div class="message-actions">
                                <a class="btn btn-outline-primary" href="/user-messages/${randomMessage.author.id}?message=${randomMessage.id}">Подробнее</a>
                                От: <a href="/user-messages/${randomMessage.author.id}">${randomMessage.authorName}</a>
                            </div>
                        </div>
                    </div>
                </li>
            </div>
        </div>
    <#else>
        <div class="alert alert-info" role="alert">
            No messages found or selected user has no comparable messages.
        </div>
    </#if>

    <div id="messageEditSection">
        <#include "parts/messageEdit.ftl" />
    </div>
    <div id="messageListSection">
        <#include "parts/messageList.ftl" />
    </div>
</@c.page>
