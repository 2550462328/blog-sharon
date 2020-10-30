<link rel="stylesheet" href="/static/halo-backend/plugins/bootstrap/css/bootstrap.min.css">
<div id="sidebar" class="sidebar animated fadeInRight">
    <div class="search-div">
        <form method="get" class="form-horizontal" id="search-form">
            <div class="form-group" style="width:80%;margin-left: 10%">
                <div class="input-group  input-group-lg ">
                    <input type="text" class="form-control" id="content" name="content" value=""
                           placeholder="<@spring.message code='front.pages.index.search.key' />">
                    <span class="input-group-btn"><button type="button" class="btn btn-primary" onclick="searchKey()">Search</button>
                    </span>
                </div>
            </div>
            <div class="form-group" style="margin-top: 10px;width:80%;margin-left: 10%">
                <ul id="cate_tree_div" class="ztree"></ul>
            </div>
        </form>
    </div>
    <div class="logo-title">
        <div class="title">
            <img id="coverimg" style="width:80%;height:230px;margin:0 auto;"/>
            <img id="prevPic" onclick="changeCover('prev')" style="position:absolute;transform: scaleX(-1); left: 0;top:50%;width: 16px;height: 30px;cursor:url('/static/halo-frontend/images/lefttip.ico'),w-resize;" src="/anatole/source/images/arrow.png">
            <img id="nextPic" onclick="changeCover('next')" style="position:absolute;right: 0;top:50%;width: 16px;height: 30px; cursor:url('/static/halo-frontend/images/righttip.ico'),e-resize;" src="/anatole/source/images/arrow.png">
        </div>
        <div class="description" style="margin-top:10px;">
            <#if options.anatole_style_hitokoto?default("false")=="true">
                <p id="yiyan">获取中...</p>
            <#else >
                <i style="float:left; margin-left: 10%;">${user.userDesc?default("A other Halo theme")}</i><br>
                <i style="float:right;margin-right:10%;">--By Myself</i>
            </#if>
        </div>
    </div>
</div>

