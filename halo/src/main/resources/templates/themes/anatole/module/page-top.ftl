<div id="page-top" class="page-top animated fadeInDown">
    <div class="nav_l">
        <@commonTag method="menus">
            <#list menus?sort_by('menuSort') as menu>
                <li style="line-height: 24px">
                    <a  href="${menu.menuUrl}" target="${menu.menuTarget?if_exists}">${menu.menuName} </a>
                </li>
            </#list>
        </@commonTag>
    </div>
    <div class="information">
        <div class="avatar">
            <img src="${options.anatole_style_right_icon?default("/anatole/source/images/logo.png")}" />
        </div>
    </div>
</div>