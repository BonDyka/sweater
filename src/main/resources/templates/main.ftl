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
    <a class="btn btn-primary" data-toggle="collapse" href="#addMessageForm" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        Add new message
    </a>
<div class="collapse" id="addMessageForm">
    <div class="form-group mt-3">
        <form method="post" enctype="multipart/form-data">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <div class="form-group">
                <input type="text" name="text" placeholder="Enter text..." class="form-control"/>
            </div>
            <div class="form-group">
                <input type="text" name="tag" placeholder="Enter tag..." class="form-control"/>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" class="custom-file-input" id="customFile"/>
                    <label class="custom-file-label" for="customFile">Choose file</label>
                </div>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary" id="">Add message</button>
            </div>
        </form>
    </div>
</div>
<div class="card-columns">
    <#list messages as message>
        <div class="card my-3">
            <div>
        <#if message.filename??>
            <img class="card-img-top" src="/img/${message.filename}"/>
        </#if>
            </div>
            <div class="m-2">
                <span>${message.text}</span>
                <i>${message.tag}</i>
            </div>
            <div class="card-footer text-muted">
                ${message.authorName}
            </div>
        </div>
    <#else>
    No messages
    </#list>
</div>
</@c.page>