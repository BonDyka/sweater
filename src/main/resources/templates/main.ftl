<#import "parts/common.ftl" as c />

<@c.page>

<div class="form-row">
    <div class="form-group">
        <form method="get" action="/main" class="form-inline">
            <input class="form-control" type="text" name="filter" value="${filter!}" placeholder="Search by tag..."/>
            <button type="submit" class="btn btn-primary ml-2">Search</button>
        </form>
    </div>
</div>
    <#include "parts/messageEdit.ftl" />
    <#include "parts/messageList.ftl" />
</@c.page>