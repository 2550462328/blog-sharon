
<#list postlist as post>
    <div class="post animated fadeInDown">
        <div class="post-title">
            <h3>
                <a href="/archives/${post.postUrl}">${post.postTitle}</a>
            </h3>
        </div><br>
        <div class="post-content">
            <div class="p_part">
                <p>${post.postSummary?if_exists}...</p>
            </div>
            <div class="p_part">
                <p></p>
            </div>
        </div>
        <div class="post-footer">
            <div class="meta">
                <div class="info">
                    <i class="fa fa-sun-o"></i>
                    <span class="date">${post.postDate?string("yyyy-MM-dd")}</span>
                    <i class="fa fa-comment-o"></i>
                    <a href="/archives/${post.postUrl}#comment_widget">Comments</a>
                    <#if post.tags?size gt 0>
                        <i class="fa fa-tag"></i>
                        <#list post.tags as tag>
                            <a href="/tags/${tag.tagUrl}" class="tag">&nbsp;${tag.tagName}</a>
                        </#list>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</#list>
<#if posts.totalPages gt 1>
<div class="pagination">
    <ul class="clearfix">
        <#if posts.hasPrevious()>
            <#if posts.number == 1>
                <li class="pre pagbuttons">
                    <a class="btn" role="navigation" href="javascript:void(0)" onclick="getPostContent('1','${keyword!}','${cateId!}')">首页</a>
                </li>
            <#else >
                <li class="pre pagbuttons">
                    <a class="btn" role="navigation" href="javascript:void(0)" onclick="getPostContent('${posts.number}','${keyword!}','${cateId!}')">上一页</a>
                </li>
            </#if>
        </#if>
        <#if posts.hasNext()>
            <li class="next pagbuttons">
                <a class="btn" role="navigation"  onclick="getPostContent('${posts.number + 2}','${keyword!}','${cateId!}')">下一页</a>
            </li>
        </#if>
    </ul>
</div>
</#if>