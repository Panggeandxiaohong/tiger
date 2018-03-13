$(function(){
    var adminDatagrid,adminCombobox,adminForm,adminDialog,adminFormId;
    adminDatagrid = $("#admin_datagrid");
    adminCombobox = $("#admin_combobox");
    adminForm = $("#admin_form");
    adminFormId = $("#admin_form [name='id']");
    adminDialog = $("#admin_dialog");
    var cmdObj={
        //新增
        add: function () {
            adminForm.form("reset");
            adminDialog.dialog("open");
            adminDialog.dialog("setTitle", "新增");
        },
        //编辑
        edit: function () {
            var rowData = adminDatagrid.datagrid("getSelected");
            if (rowData) {
                adminDialog.dialog("open");
                adminDialog.dialog("setTitle", "编辑");
                adminForm.form("clear");
                adminForm.form("load", {
                    id: rowData.id,
                    name: rowData.name,
                    username: rowData.username,
                    password: rowData.password,
                    isSuperAdmin: rowData.isSuperAdmin,
                });
            } else {
                $.messager.alert("温馨提示", "请选择需要编辑的管理员", "info");
            }
        },
        //删除
        delete: function () {
            var rowData = adminDatagrid.datagrid("getChecked");
            if (rowData) {
                $.messager.confirm("温馨提示", "您确定要删除此管理员？", function (r) {
                    if (r) {
                        var ids = new Array();
                        for (var i = 0; i < rowData.length; i++) {
                            ids[i] = rowData[i].id;
                        }
                        var params = {
                            ids: ids
                        };
                        $.post("/admin/delete.do", params, function (data) {
                            if (data.success) {
                                $.messager.alert("温馨提示", data.msg, "info", function () {
                                    //刷新数据表格
                                    adminDatagrid.datagrid("reload");
                                });
                            } else {
                                adminDatagrid.datagrid("reload");
                                $.messager.alert("温馨提示", data.msg, "info");
                            }
                        }, "json")
                    }
                })
            } else {
                $.messager.alert("温馨提示", "请选择需要删除的管理员!", "info");
            }
        },
        //关闭窗口
        cancel: function () {
            adminDialog.dialog("close");
        },
        //保存
        save: function () {
            var url;
            if (adminFormId.val()) {
                url = "/admin/update.do";
            } else {
                url = "/admin/save.do";
            }
            adminForm.form("submit", {
                url: url,
                contentType: false,
                processData: false,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.success) {
                        $.messager.alert("温馨提示", data.msg, "info", function () {
                            //关闭对话框
                            adminDialog.dialog("close");
                            //刷新数据表格
                            adminDatagrid.datagrid("load");
                        });
                    } else {
                        $.messager.alert("温馨提示", data.msg, "info");
                        //关闭对话框
                        adminDialog.dialog("close");
                        //刷新数据表格
                        adminDatagrid.datagrid("load");
                    }
                }
            })
        },
        //超级管理员格式化
        superAdminFormatr: function (value, row, index) {
            if (value) {
                return "是";
            } else {
                return "否";
            }
        }
    };
    $("a[data-cmd]").on("click",function(){
        var cmd = $(this).data("cmd");
        if(cmd){
            cmdObj[cmd]();
        }
    });

    //表格
    adminDatagrid.datagrid({
        url: "/admin/admin_list.do",
        fit: true,
        fitColumns: true,
        rownumbers: true,
        pagination: true,
        selectOnCheck: true,
        striped: true,
        //pageNumber: 1,
        // pageSize: 5,
        checkOnSelect: true,
        pageList: [1, 5, 10],
        singleSelect: false,
        toolbar: "#admin_datagrid_tb",

        columns: [
            [
                {field: 'id', align: 'center', title: '', checkbox: true},
                {field: 'name', align: 'center', title: '姓名', width: 1},
                {field: 'username', align: 'center', title: '用户名', width: 1},
                {field: 'isSuperAdmin', align: 'center', title: '超级管理员', width: 1, formatter: cmdObj.superAdminFormatr}
            ]
        ]
    });
    adminCombobox.combobox({
        valueField: 'value',
        textField: 'label',
        panelHeight: 'auto',
        required: true,
        value:0,
        data: [{
            label: '是',
            value: '1'
        },{
            label: '否',
            value: '0'
        }]
    });
    //新增
    adminDialog.dialog({
        width: 300,
        height: 350,
        buttons: "#admin_dialog_bt",
        closed: true
    });
})