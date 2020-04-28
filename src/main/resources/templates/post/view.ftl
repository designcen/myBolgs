<#include "/inc/layout.ftl"/>

<@layout "首页">
<div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md8 content detail">
            <div class="fly-panel detail-box">
                <h1>${post.title}</h1>
                <div class="fly-detail-info">
                    <span class="layui-badge layui-bg-green fly-detail-column">${post.categoryName}</span>

                    <#if post.level gt 0>
                        <span class="layui-badge layui-bg-black">置顶</span>
                    </#if>
                    <#if post.recommend>
                        <span class="layui-badge layui-bg-red">精帖</span>
                    </#if>

                    <@shiro.user>
                        <div class="fly-admin-box" data-id="${post.id}">
                            <#if post.userId == profile.id>
                                <span class="layui-btn layui-btn-xs jie-admin" type="del">删除</span>
                            </#if>
                            <@shiro.hasRole name="admin">
                                <#if post.userId != profile.id>
                                    <span class="layui-btn layui-btn-xs jie-admin" type="set" field="delete" rank="1">删除</span>
                                </#if>
                                <#if post.level == 0>
                                    <span class="layui-btn layui-btn-xs jie-admin" type="set" field="stick" rank="1">置顶</span>
                                </#if>
                                <#if post.level gt 0>
                                    <span class="layui-btn layui-btn-xs jie-admin" type="set" field="stick" rank="0" style="background-color:#ccc;">取消置顶</span>
                                </#if>
                                <#if !post.recommend>
                                    <span class="layui-btn layui-btn-xs jie-admin" type="set" field="status" rank="1">加精</span>
                                </#if>
                                <#if post.recommend>
                                    <span class="layui-btn layui-btn-xs jie-admin" type="set" field="status" rank="0" style="background-color:#ccc;">取消加精</span>
                                </#if>
                            </@shiro.hasRole>
                        </div>
                    </@shiro.user>

                    <span class="fly-list-nums">
                        <a href="#comment"><i class="iconfont" title="回答">&#xe60c;</i> ${post.commentCount}</a>
                        <i class="iconfont" title="人气">&#xe60b;</i> ${post.viewCount}
                    </span>
                </div>
                <div class="detail-about">
                    <a class="fly-avatar" href="${base}/user/${post.authorId}">
                        <img src="${post.authorAvatar}" alt="${post.authorName}">
                    </a>
                    <div class="fly-detail-user">
                        <a href="${base}/user/${post.authorId}" class="fly-link">
                            <cite>${post.authorName}</cite>
                            <i class="layui-badge fly-badge-vip">VIP${post.authorVip}</i>
                        </a>
                        <span>${post.created?string('yyyy-MM-dd')}</span>
                    </div>
                    <div class="detail-hits" id="LAY_jieAdmin" data-id="${post.id}">
                        <span style="padding-right: 10px; color: #FF7200">悬赏：60飞吻</span>
                        <#if post.userId == profile.id>
                            <span class="layui-btn layui-btn-xs jie-admin" type="edit">
                                <a href="${base}/post/edit?id=${post.id}">编辑此贴</a>
                            </span>
                        </#if>
                    </div>
                </div>
                <div class="detail-body photos">
                    ${post.content}
                </div>
            </div>

            <div class="fly-panel detail-box" id="flyReply">
                <fieldset class="layui-elem-field layui-field-title" style="text-align: center;">
                    <legend>回帖</legend>
                </fieldset>
                <ul class="jieda" id="jieda">
                    <#list pageData.records as comment>
                        <li data-id="${comment.id}" class="jieda-daan">
                            <a name="item-${comment.id}"></a>
                            <div class="detail-about detail-about-reply">
                                <a class="fly-avatar" href="${base}/user/${comment.authorId}">
                                    <img src="${comment.authorAvatar}" alt="${comment.authorName}">
                                </a>
                                <div class="fly-detail-user">
                                    <a href="${base}/user/${comment.authorId}" class="fly-link">
                                        <input type="hidden" value="${comment.authorId}">
                                        <cite>${comment.authorName}</cite>
                                        <i class="iconfont icon-renzheng" title="认证信息：XXX"></i>
                                        <i class="layui-badge fly-badge-vip">VIP${comment.authorVip}</i>
                                    </a>

                                <#if post.userId == comment.userId>
                                    <span>(楼主)</span>
                                </#if>
                                </div>

                                <div class="detail-hits">
                                    <span>${comment.created?string('yyyy-MM-dd HH:mm:ss')}</span>
                                </div>
                                <#if comment.status == 1>
                                    <i class="iconfont icon-caina" title="最佳答案"></i>
                                </#if>
                            </div>
                            <div class="detail-body jieda-body photos">
                                ${comment.content!}
                            </div>
                            <input type="hidden" name="parentId" value="${comment.parentId!}">
                            <div class="jieda-reply">
                          <span class="jieda-zan zanok" type="zan">
                            <i class="iconfont icon-zan"></i>
                            <em>${comment.voteUp}</em>
                          </span>
                          <span type="reply">
                            <i class="iconfont icon-svgmoban53"></i> 回复
                          </span>
                                <div class="jieda-admin">
                                <#if comment.userId == profile.id >
                                    <#if comment.status == 0>
                                        <span type="edit">编辑</span>
                                    </#if>
                                    <span type="del">删除</span>
                                </#if>
                                    <#if post.userId == profile.id && comment.status == 0>
                                        <span class="jieda-accept" type="accept">采纳</span>
                                    </#if>
                                    <@shiro.hasRole name="admin" && comment.userId != profile.id>
                                        <span type="del">删除</span>
                                    </@shiro.hasRole>
                                </div>
                            </div>
                        </li>
                    </#list>
                </ul>

                <div style="text-align: center">
                    <@page pageData></@page>
                </div>

                <div class="layui-form layui-form-pane">
                    <form action="${base}/comment/reply/" method="post">
                        <div class="layui-form-item layui-form-text">
                            <a name="comment"></a>
                            <div class="layui-input-block">
                                <textarea id="L_content" name="content" required lay-verify="required" placeholder="请输入内容"
                                          class="layui-textarea fly-editor" style="height: 150px;"></textarea>
                            <input type="hidden" name="parentId" value="">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <input type="hidden" name="pid" value="${post.id}">
                            <button class="layui-btn" lay-filter="*" lay-submit>提交回复</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!--侧边栏的签到等-->
        <#include "/inc/right.ftl" />
    </div>
</div>

<script type="text/javascript">
    $(function () {
        layui.use(['fly', 'face'], function () {
            var $ = layui.$, fly = layui.fly;
            //如果你是采用模版自带的编辑器，你需要开启以下语句来解析。
            $('.detail-body').each(function () {
                var othis = $(this), html = othis.html();
                othis.html(fly.content(html));
            });
        });
    });
</script>
</@layout>