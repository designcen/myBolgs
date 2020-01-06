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