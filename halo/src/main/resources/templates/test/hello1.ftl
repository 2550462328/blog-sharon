<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>hello</title>
</head>
<body>
<article>hello freemarker!</article>
<section>
    <ul>
        <li>
        <#assign x="asdada" />
            ${x?index_of("e")}
        </li>
        <li>
            排序前
        <#assign y=[1,6,8,5,7] />
        <#list y as item >
        ${item}
            <#if item_has_next >,</#if>
        </#list>
            排序后
        <#list sort_int(y) as item >
        ${item}
            <#if item_has_next >,</#if>
        </#list>
        </li>
        <li>
            <@roleTag user="zhanghui" role="admin"; result1, result2 >
                <#if result1>
                    我的角色是<p style="color:red">admin</p><br>
                </#if>
                我拥有的权限是<p style="color: red;">
                <#list result2 as roleItem>
                    ${roleItem}
                </#list>
                </p><br>
            </@roleTag>
        </li>
        <li>
            <#list "a,b,c,d"?split(",") as item >
                ${item},
            </#list>
                <br>
            <#assign mylist = [1,2,3,4,5,6,7,8] />
            ${mylist?chunk(2)?size}
            <#list mylist?chunk(2)?last as item >
                ${item},
            </#list>
            <br>
            ${("1"+"2")?eval?is_string?string("yes","no")}
        </li>
        <li>
            <#function toAdd x y>
                <#return x + y />
            </#function>
            ${toAdd(1,2)}
        </li>
        <li>
            <#macro myMacro param1 param2="world" paramExt...>
                <p> ${param1}</p>
             <#nested param1,"我的netsted参数" />
            </#macro >
            <@myMacro param1="hello";loopvar1, loopvar2 >
                <p style="color: red">hello ${loopvar1}, ${loopvar2}</p>
            </@myMacro>
            <@myMacro param1="zhanghui";loopvar1>
                hello ${loopvar1}
            </@myMacro>
        </li>
    </ul>
</section>
</body>
</html>