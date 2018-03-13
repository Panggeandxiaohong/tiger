<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>微信考试系统</title>
    <%@ include file="common.jsp"%>
    <script type="text/javascript" src="/js/views/index.js"></script>
</head>
<body class="easyui-layout">
<div data-options="region:'north'" style="height:80px;background: url('/image/back.png') no-repeat;background-size:cover;">

</div>
<div data-options="region:'west',split:true" style="width:150px;">
    <div class="easyui-accordion" fit="true">
        <div title="菜单">
            <div id="menu"></div>
        </div>
        <div iconCls="icon-help" title="帮助"></div>
    </div>
</div>
<div data-options="region:'center'" style="padding:1px;">
    <div id="tabs">
        <div title="首页" data-options="closable:true">
            <iframe src ="/welcome.jsp" style="width:100%;height:99%" frameborder=0></iframe>
        </div>
    </div>
</div>
<div data-options="region:'south'" style="height:25px;background: url('/image/back1.png') no-repeat;background-size:cover;">
    <center>版权所有,侵权收费</center>
</div>
</body>
</html>
