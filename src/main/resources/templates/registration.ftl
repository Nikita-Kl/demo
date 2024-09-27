<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <div class="mb-1">Регистрация нового пользователя. После создания аккаунта, активируйте его через свою почту</div>
    ${message?ifExists}
    <@l.login "/registration" true />
</@c.page>