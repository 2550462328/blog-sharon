<#include "module/macro.ftl">
<@head title="${options.blog_title?default('Anatole')}" keywords="${options.seo_keywords?default('Anatole')}" description="${options.seo_desc?default('Anatole')}"></@head>
<#include "module/sidebar.ftl">
<div class="main">
    <#include "module/page-top.ftl">   
    <div class="autopagerize_page_element">
        <div class="content" id="post-content">
        </div>
    </div>
</div>
<@footer></@footer>
