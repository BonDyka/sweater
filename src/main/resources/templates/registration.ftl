<#import "parts/common.ftl" as c />
<#import "parts/login.ftl" as l />

<@c.page>
    <h4 class="mb-1">Add new user</h4>
       ${message!}
    <@l.login "/registration" true/>
</@c.page>