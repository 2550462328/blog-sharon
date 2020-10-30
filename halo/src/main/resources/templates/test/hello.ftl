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
        <li>${userName}115</li>
        小数显示格式
        <li>
            <#setting number_format="number"/>
            <#assign x = 42 />
            ${x}
            ${x?string}
            ${x?string.number}
            ${x?string.percent}
            ${x?string.currency}
            ${x?string.computer}
        </li>
        保留小数
        <li>
            <#assign y=1.254/>
            ${y?string("0")}
            ${y?string("0.#")}
            ${y?string("0.##")}
            ${y?string("0.###")}
            ${y?string("0.####")}

            ${1?string("0.00")}
            ${1?string("00.00")}
            ${12.1?string("000.00")}
            ${12.456 ? string("000.00")}

            ${1.2?string("0")}
            ${1.8?string("0")}
            <!--奇数会进一位 输出2-->
            ${1.5?string("0")}
            <!--偶数不会进一位 也输出2-->
            ${2.5?string("0")}
            <!-- 科学进制 -->
            ${12345?string("0.##E0")}
        </li>
        不同地区数据格式
        <li>
            <#setting locale="en_US">
            US :       ${12345678?string(",##0.00")}
            <#setting locale="hu">
            Hungarian: ${12345678?string(",##0.00")}
        </li>
        数字取整
        <li>
            <#assign list =[0,-1,1,0.5,1.5,2.5,-0.5,-1.5,-2.5,0.25,-0.25,1.75,-1.75] />
            <#list list as item>
                ${item}?round = ${item?round}<br>
                ${item}?ceiling = ${item?ceiling}<br>
                ${item}?floor = ${item?floor}<br>
            </#list>
        </li>
        数字格式化
        <li>
        <#assign i = 2.582 />
        <#assign j =4 />
        #{i;M2};
        #{j;M2};
        #{i;m1};
        #{j;m1};
        #{i;m1M2};
        #{j;m1M2};
        </li>
    </ul>
</section>
</body>
</html>