<#include "/common/macro/common_macro.ftl">
<#macro head title,keywords,description>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>${title?default("Anatole")}</title>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="renderer" content="webkit">
    <meta name="theme-color" content="${options.anatole_style_google_color?default('#fff')}">
    <meta name="author" content="${user.userDisplayName?if_exists}"/>
    <meta name="keywords" content="${keywords?default("Anatole")}"/>
    <meta name="description" content="${description?default("Anatole")}"/>
    <@verification></@verification>
    <@favicon></@favicon>
    <link href="/anatole/source/css/font-awesome.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="/anatole/source/css/blog_basic.min.css?version=88107691fe">
    <link href="/anatole/source/css/style.min.css" type="text/css" rel="stylesheet"/>
    <link rel="alternate" type="application/rss+xml" title="atom 1.0" href="/feed.xml">
    <style>
            <#if options.anatole_style_post_title_lower?default("true") == "false">
            .post .post-title h3 {
                text-transform: none;
            }
            </#if>
            <#if options.anatole_style_blog_title_lower?default("true") == "false">
            .sidebar .logo-title .title h3 {
                text-transform: none;
            }
            </#if>
        ::-webkit-scrollbar {
            width: 6px;
            height: 6px;
            background-color: #eee;
        }

        ::-webkit-scrollbar-thumb {
            background-color: ${options.anatole_style_scrollbar?default("#3798e8")};
        }

        ::-webkit-scrollbar-track {
            background-color: #eee;
        }
        ${options.anatole_style_self?if_exists}

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
</head>
<body>
</#macro>
<#macro footer>
<script type="text/javascript" src="/anatole/source/js/jquery.min.js"></script>
<link rel="stylesheet" href="/anatole/source/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="/anatole/source/plugins/ztree/css/demo.css" type="text/css">
<script type="text/javascript" src="/anatole/source/plugins/ztree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="/anatole/source/plugins/layer/layer.js"></script>
<script type="text/javascript">
    var setting = {
        view: {
            showLine: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: onClick
        },

    };
    var zNodes = ${categoryList!};

    function onClick(event, treeId, treeNode, clickFlag) {
        getPostContent(1, null, treeNode.id);

        $("#expandImg").hide();
    }

    function searchKey() {
        var chReg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
        var enReg = new RegExp("[a-zA-Z]+", "g");
        var keyword = $("#content").val();
        if (keyword != '' && !chReg.test(keyword) && !enReg.test(keyword)) {
            layer.alert('输入正确的关键词！', {icon: 5});
        } else {
            getPostContent(1, keyword, null);
        }
    }

    function getPostContent(page, keyword, cateId) {
        $.ajax({
            type: 'GET',
            url: "/search/" + page,
            data: {
                'keyword': keyword,
                'cateId': cateId
            },
            async: false,
            success: function (resp) {
                $("#post-content").empty().html(resp);
            }
        });
    }

    var galleries = "${galleries}".replace("[", "").replace("]", "").split(",");

    var galleries_index = 0;

    function changeCover(type) {
        if (type == 'prev') {
            galleries_index = galleries_index - 1;
            $("#coverimg").attr("src", galleries[galleries_index]);
            if (galleries[galleries_index - 1] == null) {
                $("#prevPic").hide();
            }
            $("#nextPic").show();
        }
        if (type == 'next') {
            galleries_index = galleries_index + 1;
            $("#coverimg").attr("src", galleries[galleries_index]);
            if (galleries[galleries_index + 1] == null) {
                $("#nextPic").hide();
            }
            $("#prevPic").show();
        }
    }

    $(function () {
        $.fn.zTree.init($("#cate_tree_div"), setting, zNodes);
        $("#prevPic").hide();
        $("#nextPic").hide();

        if (galleries[galleries_index] == null) {
            $("#coverimg").attr("src", "/anatole/source/images/cover.jpg");
        } else {
            $("#coverimg").attr("src", galleries[galleries_index]);
            if (galleries[galleries_index + 1] != null) {
                $("#nextPic").show();
            }
        }

        $("#content").on("keydown",function(e){
            if(e.keyCode == 13){
               return false;
            }
        });

        $("#content").on("keyup",function(e){
            if(e.keyCode == 13){
               searchKey();
            }
        });

        $("#expandImg").click(function () {
            if ($("#expandImg").attr("isopen") == "false") {
                $("#sidebar").width(0);
                $("#sidebar").hide();
                $("#post-content").width("100%");
                $("#page-top").width("100%");
                $("#expandImg").attr("isopen", "true");
                $("#expandImg").attr("src", "/static/halo-frontend/images/rightarrow.png");
                $("#expandImg").css("margin-left", 0);
                $("#expandImg").css("cursor", "url('/static/halo-frontend/images/righttip.ico'),e-resize");

            } else {
                $("#sidebar").width("25%");
                $("#sidebar").show();
                $("#post-content").width("75%");
                $("#page-top").width("75%");
                $("#expandImg").attr("isopen", "false");
                $("#expandImg").attr("src", "/static/halo-frontend/images/leftarrow.png");
                $("#expandImg").css("margin-left", $("#sidebar").width() - $("#expandImg").width());
                $("#expandImg").css("cursor", "url('/static/halo-frontend/images/lefttip.ico'),w-resize");
            }
        });


        $.ajax({
            type: 'GET',
            url: "/search/1",
            async: false,
            success: function (data) {
                if (!${is_post?c}) {
                    $("#post-content").empty().html(data);
                }
            }
        });

    });

    var url = location.href;
    var urlstatus = false;
    $(".nav_l li a").each(function () {
        if ((url + '/').indexOf($(this).attr('href')) > -1 && $(this).attr('href') != '/') {
            $(this).addClass('current');
            urlstatus = true;
        } else {
            $(this).removeClass('current');
        }
    });
    if (!urlstatus) {
        $(".nav_l li a").eq(0).addClass('current');
    }

        <#if options.anatole_style_hitokoto?default("false")=="true">
        var xhr = new XMLHttpRequest();
        xhr.open('get', 'https://v1.hitokoto.cn');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                var data = JSON.parse(xhr.responseText);
                var yiyan = document.getElementById('yiyan');
                yiyan.innerText = data.hitokoto + "        -「" + data.from + "」";
            }
        };
        xhr.send();
        </#if>
</script>
    <@statistics></@statistics>
</body>
</html>
</#macro>
