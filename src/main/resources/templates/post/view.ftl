<ul class="jieda" id="jieda">
    <#list pageData.records as comment>
        <li data-id="${comment.id}" class="jieda-daan">
            <a name="item-${comment.id}"></a>
            <div class="detail-about detail-about-reply">
                <a class="fly-avatar" href="">
                    <img src="${comment.authorAvatar}" alt=" ">
                </a>
                <div class="fly-detail-user">
                    <a href="" class="fly-link">
                        <cite>${comment.authorName}</cite>
                        <i class="iconfont icon-renzheng" title="认证信息：XXX"></i>
                        <i class="layui-badge fly-badge-vip">VIP${comment.authorVip}</i>
                    </a>
                    <#if post.userId == comment.userId>
                        <span>(楼主)</span>
                    </#if>
                </div>
                <div class="detail-hits">
                    <span>${comment.created?string('yyyy-MM-dd')}</span>
                </div>
            </div>
            <div class="detail-body jieda-body photos">
                ${comment.content}
            </div>
            <div class="jieda-reply">
                <span class="jieda-zan zanok" type="zan">
                    <i class="iconfont icon-zan"></i>
                    <em>${comment.voteUp}</em>
                </span>
                <span type="reply">
                    <i class="iconfont icon-svgmoban53"></i>回复
                </span>
                <div class="jieda-admin">
                    <#if comment.userId == profile.id><span type="del">删除</span></#if>
                </div>
            </div>
        </li>
    </#list>
</ul>
<div style="text-align: center">
    <@page pageData></@page>
</div>