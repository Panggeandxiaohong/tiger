<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>微信考试系统</title>
    <%@ include file="common.jsp" %>
    <script type="text/javascript" src="/js/views/admin.js"></script>
    <script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
</head>
<body>
<div id="admin_datagrid"></div>
<%--新增/更新对话框--%>
<div id="admin_dialog">
    <form id="admin_form"  enctype="multipart/form-data" method="post">
        <input type="hidden" name="id"/>
        <table align="center" style="margin-top: 15px">
            <tr>
                <td>姓名</td>
                <td><input type="text" id="name" name="name" class="easyui-validatebox"/></td>
            </tr>
            <tr>
                <td>用户名</td>
                <td><input type="text" id="username" name="username" class="easyui-validatebox"/></td>
            </tr>
            <tr>
                <td>密码</td>
                <td><input type="text" id="password" name="password" class="easyui-validatebox"/></td>
            </tr>
            <tr>
                <td>超级管理员</td>
                <td><input id="admin_combobox" name="isSuperAdmin" class="easyui-combobox"/></td>
            </tr>
        </table>
    </form>
</div>
<div id="admin_dialog_bt">
    <a class="easyui-linkbutton" iconCls="icon-save" data-cmd="save">保存</a>
    <a class="easyui-linkbutton" iconCls="icon-cancel" data-cmd="cancel">取消</a>
</div>
<div id="admin_datagrid_tb">
    <div>
        <a id="admin_datagrid_save" class="easyui-linkbutton" iconCls="icon-add" plain="true" data-cmd="add">新增</a>
        <a id="admin_datagrid_edit" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-cmd="edit">编辑</a>
        <a id="admin_datagrid_delete" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-cmd="delete">删除</a>
    </div>
</div>
</body>
</html>
