<#import "parts/common.ftl" as c>
    <style>
        #messageEditSection, #messageListSection {
            margin-top: 30px;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
    </style>


<@c.page>
    <h3>${userChannel.username}</h3>

    <#if isCurrentUser>
        <div id="messageEditSection">
            <#include "parts/messageEdit.ftl" />
        </div>
    </#if>

    <div id="messageListSection">
        <#include "parts/messageList.ftl" />
    </div>
</@c.page>