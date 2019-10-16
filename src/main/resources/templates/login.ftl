<#import "parts/common.ftl" as c />
<#import "parts/login.ftl" as l />
<@c.page>
    <h4 class="mb-1">Login form</h4>
    <@l.login "/login" false />
</@c.page>