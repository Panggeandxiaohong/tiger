<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>微信考试系统</title>
    <%@ include file="common.jsp" %>
    <script type="text/javascript" src="/js/views/subjectType.js"></script>
    <script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
</head>
<body>
<div id="subject_type_datagrid"></div>
<%--新增/更新对话框--%>
<div id="subject_type_dialog">
    <form id="subject_type_form"  enctype="multipart/form-data" method="post">
        <input type="hidden" name="id"/>
        <table align="center" style="margin-top: 15px">
            <tr>
                <td>类型名称</td>
                <td><input type="text" id="type_name" name="typeName" class="easyui-validatebox"/></td>
            </tr>
            <tr>
                <td>类型编码</td>
                <td><input type="text" id="type_code" name="typeCode" class="easyui-validatebox"/></td>
            </tr>
        </table>
    </form>
</div>
<div id="subject_type_dialog_bt">
    <a class="easyui-linkbutton" iconCls="icon-save" data-cmd="save">保存</a>
    <a class="easyui-linkbutton" iconCls="icon-cancel" data-cmd="cancel">取消</a>
</div>
<div id="subject_type_datagrid_tb">
    <div>
        <a id="subject_type_datagrid_save" class="easyui-linkbutton" iconCls="icon-add" plain="true" data-cmd="add">新增</a>
        <a id="subject_type_datagrid_edit" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-cmd="edit">编辑</a>
        <a id="subject_type_datagrid_delete" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-cmd="delete">删除</a>
    </div>
</div>
</body>
</html>
