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