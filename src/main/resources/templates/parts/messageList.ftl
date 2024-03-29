<#include "security.ftl" />
<#import "pager.ftl" as p />
<#if url??>
    <@p.pager url  page />
</#if>
<div class="card-columns" id="message-list">
    <#list page.content as message>
        <div class="card my-3" data-id="${message.id}">
            <div>
                <#if message.filename??>
                    <img class="card-img-top" src="/img/${message.filename}" />
                </#if>
            </div>
            <div class="m-2">
                <span>${message.text}</span><br />
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted container">
                <div class="row">
                    <a class="col align-self-center" href="/user-messages/${message.author.id}">${message.authorName}</a>
                    <a class="col align-self-center" href="/messages/${message.id}/like">
                        <#if message.meLiked>
                            <i class="fas fa-heart"></i>
                        <#else>
                            <i class="far fa-heart"></i>
                        </#if>
                        ${message.likes}
                    </a>
                    <#if message.author.id == currentUserId>
                        <a href="/user-messages/${message.author.id}?message=${message.id}" class="btn btn-primary">
                            Edit
                        </a>
                    </#if>
                </div>
            </div>
        </div>
    <#else>
        No messages
    </#list>
</div>

<#if url??>
    <@p.pager url  page />
</#if>