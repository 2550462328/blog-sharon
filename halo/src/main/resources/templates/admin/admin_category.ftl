<#compress >
<#include "module/_macro.ftl">
    <@head>${options.blog_title!} | <@spring.message code='admin.categories.title' /></@head>
<script src="/templates/themes/anatole/source/js/jquery.js"></script>
<link rel="stylesheet" href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="external nofollow"/>
<script type="text/javascript" src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
<style>
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
        content: " ";
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
<div class="content-wrapper">
    <section class="content-header" id="animated-header">
        <h1>
            <@spring.message code='admin.categories.title' />
            <small></small>
        </h1>
        <ol class="breadcrumb">
            <li>
                <a data-pjax="true" href="/admin">
                    <i class="fa fa-dashboard"></i> <@spring.message code='admin.index.bread.index' /></a>
            </li>
            <li><a data-pjax="true"
                   href="javascript:void(0)"><@spring.message code='admin.categories.bread.posts' /></a></li>
            <li class="active"><@spring.message code='admin.categories.title' /></li>
        </ol>
    </section>
    <section class="content container-fluid" id="animated-content">
        <div class="row">
            <div class="col-md-5">
                <div class="box box-primary">
                    <#if updateCategory??>
                        <div class="box-header with-border">
                            <h3 class="box-title"><@spring.message code='admin.categories.text.edit-category' /> <#if updateCategory??>
                                [${updateCategory.cateName}]</#if></h3>
                        </div>
                        <form action="/admin/category/save" method="post" role="form" id="cateAddForm">
                            <input type="hidden" name="cateId" value="${updateCategory.cateId}">
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="cateName"><@spring.message code='admin.categories.form.cate-name' /></label>
                                    <input type="text" class="form-control" id="cateName" name="cateName"
                                           value="${updateCategory.cateName}">
                                    <small><@spring.message code='admin.categories.form.cate-name-tips' /></small>
                                </div>
                                <div class="form-group">
                                    <label for="catePid"><@spring.message code='admin.categories.form.cate-sup-url' /></label>
                                    <input type="hidden" name="catePid" id="category_id" value="${updateCategory.catePid}"/>
                                    <div id="catePid" class="dropdown">
                                        <a id="dLabel" role="button" data-toggle="dropdown" class="btn btn-white"
                                           data-target="#" href="javascript:;" rel="external nofollow"><span
                                                id="select-title">${pCateName}</span>
                                            <span
                                                    class="caret"></span></a>
                                        <ul class="dropdown-menu multi-level" role="menu"
                                            aria-labelledby="dropdownMenu">
                                            <#if categoryList?? && categoryList?size gt 0>
                                                <#list categoryList as cate>
                                                    <li class='${cate.hasChild?string("dropdown-submenu","")}'><a
                                                            href="javascript:;" rel="external nofollow"
                                                            onmouseover='extendPackage(this)'
                                                            onclick='chooseCate(this)'
                                                            has-child='${cate.hasChild?string("true","false")}'
                                                            data-index="${cate.cateId?c}"
                                                            data-url="${cate.cateUrl}"
                                                            data-title="${cate.cateName}">${cate.cateName}</a>
                                                    </li>
                                                </#list>
                                            </#if>
                                        </ul>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="cateUrl"><@spring.message code='admin.categories.form.cate-url' /></label>
                                    <input type="text" class="form-control" id="cateUrl" name="cateUrl"
                                           value="${updateCategory.cateUrl}">
                                    <small><@spring.message code='admin.categories.form.cate-url-tips' /></small>
                                </div>
                                <div class="form-group">
                                    <label for="cateIcon"><@spring.message code='admin.categories.form.cate-icon' /></label>
                                    <div class="input-group">
                                        <input type="text" class="form-control" id="cateIcon" name="cateIcon" value="${updateCategory.cateIcon}">
                                        <span class="input-group-btn">
                                                <button class="btn btn-default " type="button" onclick="halo.layerModal('/admin/attachments/select?id=cateIcon','<@spring.message code="common.js.all-attachment" />')"><@spring.message code='common.btn.choose' /></button>
                                            </span>
                                    </div>
                                    <small><@spring.message code='admin.categories.form.cate-icon-tips' /></small>
                                </div>
                                <div class="form-group">
                                    <label for="cateDesc"
                                           class="control-label"><@spring.message code='admin.categories.form.cate-desc' /></label>
                                    <textarea class="form-control" rows="3" id="cateDesc" name="cateDesc"
                                              style="resize: none">${updateCategory.cateDesc}</textarea>
                                    <small><@spring.message code='admin.categories.form.cate-desc-tips' /></small>
                                </div>
                            </div>
                            <div class="box-footer">
                                <button type="submit"
                                        class="btn btn-primary btn-sm "><@spring.message code='common.btn.define-edit' /></button>
                                <a data-pjax="true" href="/admin/category"
                                   class="btn btn-info btn-sm "><@spring.message code='common.btn.back-to-add' /></a>
                            </div>
                        </form>
                    <#else >
                        <div class="box-header with-border">
                            <h3 class="box-title"><@spring.message code='admin.categories.text.add-category' /></h3>
                        </div>
                        <form action="/admin/category/save" method="post" role="form" id="cateAddForm"
                              onsubmit="return checkCate()">
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="cateName"><@spring.message code='admin.categories.form.cate-name' /></label>
                                    <input type="text" class="form-control" id="cateName" name="cateName"
                                           placeholder="">
                                    <small><@spring.message code='admin.categories.form.cate-name-tips' /></small>
                                </div>

                                <div class="form-group">
                                    <label for="catePid"><@spring.message code='admin.categories.form.cate-sup-url' /></label>
                                    <input type="hidden" name="catePid" id="category_id" value=""/>
                                    <div id="catePid" class="dropdown">
                                        <a id="dLabel" role="button" data-toggle="dropdown" class="btn btn-white"
                                           data-target="#" href="javascript:;" rel="external nofollow"><span
                                                id="select-title"><@spring.message code='admin.categories.form.cate-sup-url-tip' /></span>
                                            <span
                                                    class="caret"></span></a>
                                        <ul class="dropdown-menu multi-level" role="menu"
                                            aria-labelledby="dropdownMenu">
                                            <#if categoryList?? && categoryList?size gt 0>
                                                <#list categoryList as cate>
                                                    <li class='${cate.hasChild?string("dropdown-submenu","")}'><a
                                                            href="javascript:;" rel="external nofollow"
                                                            onmouseover='extendPackage(this)'
                                                            onclick='chooseCate(this)'
                                                            has-child='${cate.hasChild?string("true","false")}'
                                                            data-index="${cate.cateId}"
                                                            data-url="${cate.cateUrl}"
                                                            data-title="${cate.cateName}">${cate.cateName}</a>
                                                    </li>
                                                </#list>
                                            </#if>
                                        </ul>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="cateUrl"><@spring.message code='admin.categories.form.cate-url' /></label>
                                    <input type="text" class="form-control" id="cateUrl" name="cateUrl" placeholder="">
                                    <small><@spring.message code='admin.categories.form.cate-url-tips' /></small>
                                </div>

                                <div class="form-group">
                                    <label for="cateIcon"><@spring.message code='admin.categories.form.cate-icon' /></label>
                                    <div class="input-group">
                                        <input type="text" class="form-control" id="cateIcon" name="cateIcon">
                                        <span class="input-group-btn">
                                                <button class="btn btn-default " type="button" onclick="halo.layerModal('/admin/attachments/select?id=cateIcon','<@spring.message code="common.js.all-attachment" />')"><@spring.message code='common.btn.choose' /></button>
                                            </span>
                                    </div>
                                    <small><@spring.message code='admin.categories.form.cate-icon-tips' /></small>
                                </div>

                                <div class="form-group">
                                    <label for="cateDesc"
                                           class="control-label"><@spring.message code='admin.categories.form.cate-desc' /></label>
                                    <textarea class="form-control" rows="3" id="cateDesc" name="cateDesc"
                                              style="resize: none"></textarea>
                                    <small><@spring.message code='admin.categories.form.cate-desc-tips' /></small>
                                </div>
                            </div>
                            <div class="box-footer">
                                <button type="submit"
                                        class="btn btn-primary btn-sm "><@spring.message code='common.btn.define-add' /></button>
                            </div>
                        </form>
                    </#if>
                </div>
            </div>
            <div class="col-md-7">
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title"><@spring.message code='admin.categories.text.all-categories' /></h3>
                    </div>
                    <div class="box-body table-responsive no-padding">
                        <table class="table table-hover">
                            <tbody>
                            <tr>
                                <th><@spring.message code='common.th.name' /></th>
                                <th><@spring.message code='common.th.url' /></th>
                                <th><@spring.message code='common.th.desc' /></th>
                                <th><@spring.message code='common.th.posts-count' /></th>
                                <th><@spring.message code='common.th.control' /></th>
                            </tr>
                                <@commonTag method="categories">
                                    <#if categories?? && categories?size gt 0>
                                        <#list categories as cate>
                                        <tr>
                                            <td>${cate.cateName}</td>
                                            <td>${cate.cateUrl}</td>
                                            <td>${(cate.cateDesc)!}</td>
                                            <td>
                                                <span class="label"
                                                      style="background-color: #d6cdcd;">${cate.posts?size}</span>
                                            </td>
                                            <td>
                                                <#if cate.cateId == 1 || (updateCategory?? && updateCategory.cateId?c==cate.cateId?c)>
                                                    <a href="javascript:void(0)" class="btn btn-primary btn-xs "
                                                       disabled><@spring.message code='common.btn.modify' /></a>
                                                    <button class="btn btn-danger btn-xs" disabled><@spring.message code='common.btn.delete' /></button>
                                                <#else >
                                                    <a data-pjax="true"
                                                       href="/admin/category/edit?cateId=${cate.cateId?c}"
                                                       class="btn btn-primary btn-xs "><@spring.message code='common.btn.modify' /></a>
                                                    <button class="btn btn-danger btn-xs "
                                                            onclick="modelShow('/admin/category/remove?cateId=${cate.cateId?c}')"><@spring.message code='common.btn.delete' /></button>
                                                </#if>

                                            </td>
                                        </tr>
                                        </#list>
                                    </#if>
                                </@commonTag>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- 删除确认弹出层 -->
    <div class="modal fade" id="removeCateModal">
        <div class="modal-dialog">
            <div class="modal-content message_align">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">×</span></button>
                    <h4 class="modal-title"><@spring.message code='common.text.tips' /></h4>
                </div>
                <div class="modal-body">
                    <p><@spring.message code='common.text.tips.to-delete' /></p>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="url"/>
                    <button type="button" class="btn btn-default "
                            data-dismiss="modal"><@spring.message code='common.btn.cancel' /></button>
                    <a onclick="removeIt()" class="btn btn-danger "
                       data-dismiss="modal"><@spring.message code='common.btn.define' /></a>
                </div>
            </div>
        </div>
    </div>
</div>
    <@footer></@footer>
</#compress>
<script>
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
                            dataDiv += "<a href='javascript:;' onclick='chooseCate(this)' onmouseover='extendPackage(this)' rel='external nofollow' data-url='"+ cate.cateUrl +"'data-title='" + cate.cateName + "' data-index='" + cate.cateId + "' has-child='" + cate.hasChild + "'>" + cate.cateName + "</a>"
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
        var cateUrl = $(which).attr("data-url");

        $("#select-title").text(cateName);
        $("#category_id").val(parseInt(cateId));
        $("#cateUrl").val(cateUrl + "/");
    }

    function modelShow(url) {
        $('#url').val(url);
        $('#removeCateModal').modal();
    }

    function removeIt() {
        var url = $.trim($("#url").val());
        window.location.href = url;
    }

    function checkCate() {
        var name = $('#cateName').val();
        var url = $('#cateUrl').val();
        var desc = $('#cateDesc').val();
        var result = true;
        if (name == "" || url == "" || desc == "") {
            halo.showMsg("<@spring.message code='common.js.info-no-complete' />", 'info', 2000);
            result = false;
        }
        $.ajax({
            type: 'GET',
            url: '/admin/category/checkUrl',
            async: false,
            data: {
                'cateUrl': url
            },
            success: function (data) {
                if (data.code == 0) {
                    halo.showMsg(data.msg, 'error', 2000);
                    result = false;
                }
            }
        });
        return result;
    }
</script>