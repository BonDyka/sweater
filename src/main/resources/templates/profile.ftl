<#import "parts/common.ftl" as c />

<@c.page>
    <h5 class="mb-1">${username}</h5>
    ${message!}
    <form method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Password: </label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Email: </label>
            <div class="col-sm-6">
                <input type="email" name="email" class="form-control" placeholder="some@some.com" value="${email!''}"/>
            </div>
        </div>
        <button class="btn btn-primary" type="submit">Save</button>
    </form>
</@c.page>