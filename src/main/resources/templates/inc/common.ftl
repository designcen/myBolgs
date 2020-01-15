
<#-- 导航栏-->
<#macro centerBanner level>
<ul class="layui-nav layui-nav-tree layui-inline" lay-filter="user">
    <li class="layui-nav-item">
        <a href="${base}/user/<@shiro.principal property="id" />">
            <i class="layui-icon">&#xe609;</i>
            我的主页
        </a>
    </li>
    <li class="layui-nav-item <#if level==1>layui-this</#if>">
        <a href="${base}/center">
            <i class="layui-icon">&#xe612;</i>
            用户中心
        </a>
    </li>
    <li class="layui-nav-item <#if level==2>layui-this</#if>">
        <a href="${base}/center/setting">
            <i class="layui-icon">&#xe620;</i>
            基本设置
        </a>
    </li>
    <li class="layui-nav-item <#if level==3>layui-this</#if>">
        <a href="${base}/center/message">
            <i class="layui-icon">&#xe611;</i>
            我的消息
        </a>
    </li>
</ul>

<div class="site-tree-mobile layui-hide">
    <i class="layui-icon">&#xe602;</i>
</div>
<div class="site-mobile-shade"></div>

<div class="site-tree-mobile layui-hide">
    <i class="layui-icon">&#xe602;</i>
</div>
<div class="site-mobile-shade"></div>
</#macro>

<#-- 分页模板-->
<#macro page pageData>
<div id="laypage-main"></div>
<script type="application/javascript">
    $(function () {

        layui.use(['laypage', 'layer'], function(){
            var laypage = layui.laypage
                    ,layer = layui.layer;
            //总页数大于页码总数
            laypage.render({
                elem: 'laypage-main'
                ,count: ${pageData.total} //数据总数
                ,curr: ${pageData.current}
                ,limit: ${pageData.size}
                ,jump: function(obj, first){
                    console.log(obj)

                    //首次不执行
                    if(!first){
                        var url = window.location.href;
                        location.href = "?pn=" + obj.curr;
                    }
                }
            });
        });
    });

</script>
</#macro>

<#-- 文章列表 -->
<#macro listing post>
   <li>
       <a href="${base}/user/${post.authorId}" class="fly-avatar">
           <img src="${post.authorAvatar}" alt="${post.authorName}">
       </a>
       <h2>
           <a class="layui-badge">${post.categoryName}</a>
           <a href="${base}/post/${post.id}">${post.title}</a>
       </h2>
       <div class="fly-list-info">
           <a href="${base}/user/${post.authorId}" link>
               <cite>${post.authorName}</cite>
               <i class="layui-badge fly-badge-vip">VIP${post.authorVip}</i>
           </a>
           <span>${post.created?string('yyyy-MM-dd')}</span>
           <span class="fly-list-nums">
                <i class="iconfont icon-pinglun1" title="回答"></i> ${post.commentCount}
              </span>
       </div>
       <div class="fly-list-badge">
         <#if post.level gt 0><span class="layui-badge layui-bg-black">置顶</span></#if>
         <#if post.recommend><span class="layui-badge layui-bg-red">精帖</span></#if>
       </div>
   </li>
</#macro>