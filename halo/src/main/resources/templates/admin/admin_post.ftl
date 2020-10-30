<#compress >
    <#include "module/_macro.ftl">
    <@head>${options.blog_title!} | <@spring.message code='admin.posts.title' /></@head>
<script src="/templates/themes/anatole/source/js/jquery.js"></script>
<link rel="stylesheet" href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="external nofollow"/>
<script type="text/javascript" src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>

<div class="content-wrapper">
    <style type="text/css" rel="stylesheet">
        .draft, .publish, .trash {
            list-style: none;
            float: left;
            margin: 0;
            padding-bottom: 20px;
        }

        .search {
            list-style: none;
            float: left;
            margin-left: 20px;
            padding-bottom: 20px;
        }

        .search_button {
            float: left;
            margin-left: 50px;
            margin-bottom: 30px;
        }

        .dropdown-submenu {
            position: relative;
        }

        .dropdown-submenu > .dropdown-menu {
            top: 0;
            left: 100%;
            margin-top: -6px;
            margin-left: -1px;
            -webkit-border-radius: 0 6px 6px 6px;
            -moz-border-radius: 0 6px 6px;
            border-radius: 0 6px 6px 6px;
        }

        .dropdown-submenu:hover > .dropdown-menu {
            display: block;
        }

        .dropdown-submenu > a:after {
            display: block;
            content: "";
            float: right;
            width: 0;
            height: 0;
            border-color: transparent;
            border-style: solid;
            border-width: 5px 0 5px 5px;
            border-left-color: #ccc;
            margin-top: 5px;
            margin-right: -10px;
        }

        .dropdown-submenu:hover > a:after {
            border-left-color: #fff;
        }

        .dropdown-submenu.pull-left {
            float: none;
        }

        .dropdown-submenu.pull-left > .dropdown-menu {
            left: -100%;
            margin-left: 10px;
            -webkit-border-radius: 6px 0 6px 6px;
            -moz-border-radius: 6px 0 6px 6px;
            border-radius: 6px 0 6px 6px;
        }
    </style>
    <section class="content-header" id="animated-header">
        <h1 style="display: inline-block;"><@spring.message code='admin.posts.title' /></h1>
        <a data-pjax="false" class="btn-header" id="btnNewPost" href="/admin/posts/write">
            <@spring.message code='admin.posts.btn.new-post' />
        </a>
        <ol class="breadcrumb">
            <li>
                <a data-pjax="true" href="/admin">
                    <i class="fa fa-dashboard"></i> <@spring.message code='admin.index.bread.index' />
                </a>
            </li>
            <li><a data-pjax="true" href="javascript:void(0)"><@spring.message code='admin.posts.title' /></a></li>
            <li class="active"><@spring.message code='admin.posts.bread.all-posts' /></li>
        </ol>
    </section>
    <section class="content container-fluid" id="animated-content">
        <div class="row">
            <div class="col-xs-12">
                <ul style="list-style: none;padding-left: 0">
                    <li class="publish">
                        <a data-pjax="false" href="/admin/posts"
                           <#if status==0>style="color: #000" </#if>><@spring.message code='common.status.published' />
                            <span class="count">(${publishCount})</span></a>&nbsp;|&nbsp;
                    </li>
                    <li class="draft">
                        <a data-pjax="false" href="/admin/posts?status=1"
                           <#if status==1>style="color: #000" </#if>><@spring.message code='common.status.draft' /><span
                                class="count">(${draftCount})</span></a>&nbsp;|&nbsp;
                    </li>
                    <li class="trash">
                        <a data-pjax="false" href="/admin/posts?status=2"
                           <#if status==2>style="color: #000" </#if>><@spring.message code='common.status.recycle-bin' />
                            <span class="count">(${trashCount})</span></a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <ul style="list-style: none;padding-left: 0">
                    <li class="search">
                        <span class="search_span"><@spring.message code="admin.posts.search.name" /></span>&nbsp;&nbsp;
                        <input id="search_name" type="text" value="${keyword!}"/>
                    </li>
                    <li class="search">
                        <span class="search_span"><@spring.message code="admin.posts.search.category" /></span>
                    </li>
                    <li class="search">
                        <div id="catePid" class="dropdown">
                            <a id="dLabel" role="button" data-toggle="dropdown"
                               data-target="#" href="javascript:;" rel="external nofollow">
                                <span id="select-title">
                                    <#if select_name??>
                                        ${select_name}
                                    <#else>
                                        <@spring.message code='admin.post.search.tips' />
                                    </#if>
                                </span><span class="caret"></span></a>
                            <ul class="dropdown-menu multi-level" role="menu" aria-labelledby="dropdownMenu">
                                <#if categoryList?? && categoryList?size gt 0>
                                    <#list categoryList as cate>
                                        <li class='${cate.hasChild?string("dropdown-submenu","")}'><a
                                                href="javascript:;" rel="external nofollow"
                                                onmouseover='extendPackage(this)'
                                                onclick='chooseCate(this)'
                                                has-child='${cate.hasChild?string("true","false")}'
                                                data-index="${cate.cateId}"
                                                data-title="${cate.cateName}">${cate.cateName}</a>
                                        </li>
                                    </#list>
                                </#if>
                            </ul>
                        </div>
                    </li>
                    <li class="search_button">
                        <button class="btn btn-primary"
                                id="clear_button"><@spring.message code="admin.posts.clear.button-value" /></button>
                        <button class="btn btn-success"
                                id="search_button"><@spring.message code="admin.posts.search.button-value" /></button>
                    </li>
                </ul>

                <input type="hidden" id="search_category" value="${search_category!}"/>
                <input type="hidden" id="page" value="${posts.number}"/>
                <input type="hidden" id="size" value="${posts.size}"/>
                <input type="hidden" id="status" value="${status}"/>
                <input type="hidden" id="trashCount" value="${trashCount}"/>
                <input type="hidden" id="draftCount" value="${draftCount}"/>
                <input type="hidden" id="publishCount" value="${publishCount}"/>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <div class="box box-primary">
                    <div class="box-body table-responsive no-padding">
                        <table class="table table-hover">
                            <tbody>
                            <tr>
                                <th><@spring.message code='common.th.title' /></th>
                                <th><@spring.message code='common.th.categories' /></th>
                                <th><@spring.message code='common.th.tags' /></th>
                                <th><@spring.message code='common.th.comments' /></th>
                                <th><@spring.message code='common.th.views' /></th>
                                <th><@spring.message code='common.th.date' /></th>
                                <th><@spring.message code='common.th.control' /></th>
                            </tr>
                                <#if posts.content?size gt 0>
                                    <#list posts.content as post>
                                    <tr>
                                        <td>
                                            <#if post.postStatus==0>
                                                <a target="_blank"
                                                   href="/archives/${post.postUrl}">${post.postTitle}</a>
                                            <#else>
                                            ${post.postTitle}
                                            </#if>
                                        </td>
                                        <td>
                                            <#if post.categories?size gt 0>
                                                <#list post.categories as cate>
                                                    <label>${cate.cateName}</label>
                                                </#list>
                                            <#else >
                                                <label>无分类</label>
                                            </#if>
                                        </td>
                                        <td>
                                            <#if post.tags?size gt 0>
                                                <#list post.tags as tag>
                                                    <label>${tag.tagName}</label>
                                                </#list>
                                            <#else >
                                                <label>无标签</label>
                                            </#if>
                                        </td>
                                        <td>
                                            <span class="label"
                                                  style="background-color: #d6cdcd;">${post.getComments()?size}</span>
                                        </td>
                                        <td>
                                            <span class="label"
                                                  style="background-color: #d6cdcd;">${post.postViews}</span>
                                        </td>
                                        <td>${post.postDate!?string("yyyy-MM-dd HH:mm")}</td>
                                        <td>
                                            <#switch post.postStatus>
                                                <#case 0>
                                                    <a href="/admin/posts/edit?postId=${post.postId?c}"
                                                       class="btn btn-info btn-xs "><@spring.message code='common.btn.edit' /></a>
                                                    <button class="btn btn-danger btn-xs "
                                                            onclick="modelShow('/admin/posts/throw?postId=${post.postId?c}&status=0','<@spring.message code="common.text.tips.to-recycle-bin" />')"><@spring.message code='common.btn.recycling' /></button>
                                                    <#break >
                                                <#case 1>
                                                    <a href="/admin/posts/edit?postId=${post.postId?c}"
                                                       class="btn btn-info btn-xs "><@spring.message code="common.btn.edit" /></a>
                                                    <button class="btn btn-primary btn-xs "
                                                            onclick="modelShow('/admin/posts/revert?postId=${post.postId?c}&status=1','<@spring.message code="common.text.tips.to-release-post" />')"><@spring.message code='common.btn.release' /></button>
                                                    <button class="btn btn-danger btn-xs "
                                                            onclick="modelShow('/admin/posts/throw?postId=${post.postId?c}&status=1','<@spring.message code="common.text.tips.to-recycle-bin" />')"><@spring.message code='common.btn.recycling' /></button>
                                                    <#break >
                                                <#case 2>
                                                    <a href="/admin/posts/revert?postId=${post.postId?c}&status=2"
                                                       class="btn btn-primary btn-xs "><@spring.message code='common.btn.reduction' /></a>
                                                    <button class="btn btn-danger btn-xs "
                                                            onclick="modelShow('/admin/posts/remove?postId=${post.postId?c}&postType=${post.postType}','<@spring.message code="common.text.tips.to-delete" />')"><@spring.message code='common.btn.delete' /></button>
                                                    <#break >
                                            </#switch>
                                        </td>
                                    </tr>
                                    </#list>
                                <#else>
                                <tr>
                                    <th colspan="7"
                                        style="text-align: center"><@spring.message code='common.text.no-data' /></th>
                                </tr>
                                </#if>
                            </tbody>
                        </table>
                    </div>
                    <div class="box-footer clearfix">
                        <div class="no-margin pull-left">
                            <@spring.message code='admin.pageinfo.text.no' />${posts.number+1}
                            /${posts.totalPages}<@spring.message code='admin.pageinfo.text.page' />
                        </div>
                        <div class="btn-group pull-right btn-group-sm" role="group">
                            <a data-pjax="true" class="btn btn-default <#if !posts.hasPrevious()>disabled</#if>"
                               href="javascript:void(0);" onclick="searchIndex(0)">
                                <@spring.message code='admin.pageinfo.btn.first' />
                            </a>
                            <a data-pjax="true" class="btn btn-default <#if !posts.hasPrevious()>disabled</#if>"
                               href="javascript:void(0);" onclick="searchIndex(${posts.number-1})">
                                <@spring.message code='admin.pageinfo.btn.pre' />
                            </a>
                            <a data-pjax="true" class="btn btn-default <#if !posts.hasNext()>disabled</#if>"
                               href="javascript:void(0);" onclick="searchIndex(${posts.number+1})">
                                <@spring.message code='admin.pageinfo.btn.next' />
                            </a>
                            <a data-pjax="true" class="btn btn-default <#if !posts.hasNext()>disabled</#if>"
                               href="javascript:void(0);" onclick="searchIndex(${posts.totalPages-1})">
                                <@spring.message code='admin.pageinfo.btn.last' />
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- 删除确认弹出层 -->
    <div class="modal fade" id="removePostModal">
        <div class="modal-dialog">
            <div class="modal-content message_align">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">×</span></button>
                    <h4 class="modal-title"><@spring.message code='common.text.tips' /></h4>
                </div>
                <div class="modal-body">
                    <p id="message"></p>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="url"/>
                    <button type="button" class="btn btn-default"
                            data-dismiss="modal"><@spring.message code='common.btn.cancel' /></button>
                    <a onclick="removeIt()" class="btn btn-danger"
                       data-dismiss="modal"><@spring.message code='common.btn.define' /></a>
                </div>
            </div>
        </div>
    </div>
</div>
    <@footer></@footer>
</#compress>
<script>
    function modelShow(url, message) {
        $('#url').val(url);
        $('#message').html(message);
        $('#removePostModal').modal();
    }

    function removeIt() {
        var url = $.trim($("#url").val());
        window.location.href = url;
    }

    $("#clear_button").on("click", function () {
        $("#search_name").val('');
        $("#search_category").val('');
        $("#select-title").text('请输入文件类型')
    })

    $("#search_button").on("click", function () {
        searchIndex(0);
    });
    
    function searchIndex(pageNum) {
        var search_name = $("#search_name").val();
        var search_category = $("#search_category").val();
        var page = $("#page").val();
        var size = $("#size").val();
        var status = $("#status").val();
        var select_name = $("#select-title").text().trim();
        var publishCount = $("#publishCount").val();
        var draftCount = $("#draftCount").val();
        var trashCount = $("#trashCount").val();

        if(pageNum != null){
            page = pageNum;
        }

        window.location.href("/admin/posts/search?keyword=" + search_name
            + "&search_category=" + search_category
            + "&page=" + page
            + "&size=" + size
            + "&status=" + status
            + "&select_name=" + select_name
            + "&publishCount=" + publishCount
            + "&draftCount=" + draftCount
            + "&trashCount=" + trashCount
        );

    }
    function extendPackage(which) {
        var hasChild = $(which).attr("has-child");

        if (hasChild == "false") {
            return;
        }

        var cateId = $(which).attr("data-index");
        var dataDiv;
        $.ajax({
            type: 'GET',
            url: "/admin/category/getSub",
            async: false,
            data: {
                'catepId': cateId
            },
            success: function (data) {
                if (data.code == 1) {
                    var cateList = data.result;
                    if (cateList.length > 0) {
                        dataDiv = "<ul class='dropdown-menu'>"
                        $.each(cateList, function (i, cate) {
                            if (cate.hasChild) {
                                dataDiv += "<li class='dropdown-submenu'>";
                            } else {
                                dataDiv += "<li>";
                            }
                            dataDiv += "<a href='javascript:;' onclick='chooseCate(this)' onmouseover='extendPackage(this)' rel='external nofollow' data-title='" + cate.cateName + "' data-index='" + cate.cateId + "' has-child='" + cate.hasChild + "'>" + cate.cateName + "</a>"
                                    + "</li>";
                        })
                        dataDiv += "</ul>";
                    }
                }
            }
        });
        $(which).after(dataDiv);
    }

    function chooseCate(which) {
        var cateId = $(which).attr("data-index");
        var cateName = $(which).attr("data-title");
        $("#select-title").text(cateName);
        $("#search_category").val(cateId);
    }
</script>