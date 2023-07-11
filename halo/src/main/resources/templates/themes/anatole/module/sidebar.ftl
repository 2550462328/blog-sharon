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
            <img id="coverimg" style="width:80%;height:210px;margin:10px auto;"/>
            <img id="prevPic" onclick="changeCover('prev')" style="position:absolute;transform: scaleX(-1); left: 0;top:50%;width: 16px;height: 30px;cursor:url('/static/halo-frontend/images/click.ico'),w-resize;" src="/anatole/source/images/arrow.png">
            <img id="nextPic" onclick="changeCover('next')" style="position:absolute;right: 0;top:50%;width: 16px;height: 30px; cursor:url('/static/halo-frontend/images/click.ico'),e-resize;" src="/anatole/source/images/arrow.png">
        </div>
        <div class="description" style="margin-top:35px;font-size: 18px;">
            <a href="https://beian.miit.gov.cn/" target="_blank">皖ICP备19024261号-1</a>
        </div>
    </div>
</div>

