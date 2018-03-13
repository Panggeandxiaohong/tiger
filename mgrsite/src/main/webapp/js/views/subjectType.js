$(function(){
    var subjectTypeDatagrid,subjectTypeCombobox,subjectTypeForm,subjectTypeDialog,subjectTypeFormId;
    subjectTypeDatagrid = $("#subject_type_datagrid");
    subjectTypeCombobox = $("#subject_type_combobox");
    subjectTypeForm = $("#subject_type_form");
    subjectTypeFormId = $("#subject_type_form [name='id']");
    subjectTypeDialog = $("#subject_type_dialog");
    var cmdObj={
        //新增
        add: function () {
            subjectTypeForm.form("reset");
            subjectTypeDialog.dialog("open");
            subjectTypeDialog.dialog("setTitle", "新增");
        },
        //编辑
        edit: function () {
            var rowData = subjectTypeDatagrid.datagrid("getSelected");
            if (rowData) {
                subjectTypeDialog.dialog("open");
                subjectTypeDialog.dialog("setTitle", "编辑");
                subjectTypeForm.form("clear");
                subjectTypeForm.form("load", {
                    id: rowData.id,
                    typeName: rowData.typeName,
                    typeCode: rowData.typeCode
                });
            } else {
                $.messager.alert("温馨提示", "请选择需要编辑的题目类型", "info");
            }
        },
        //删除
        delete: function () {
            var rowData = subjectTypeDatagrid.datagrid("getChecked");
            if (rowData) {
                $.messager.confirm("温馨提示", "您确定要删除此题目类型？", function (r) {
                    if (r) {
                        var ids = new Array();
                        for (var i = 0; i < rowData.length; i++) {
                            ids[i] = rowData[i].id;
                        }
                        var params = {
                            ids: ids
                        };
                        $.post("/subjectType/delete.do", params, function (data) {
                            if (data.success) {
                                $.messager.alert("温馨提示", data.msg, "info", function () {
                                    //刷新数据表格
                                    subjectTypeDatagrid.datagrid("reload");
                                });
                            } else {
                                subjectTypeDatagrid.datagrid("reload");
                                $.messager.alert("温馨提示", data.msg, "info");
                            }
                        }, "json")
                    }
                })
            } else {
                $.messager.alert("温馨提示", "请选择需要删除的题目类型!", "info");
            }
        },
        //关闭窗口
        cancel: function () {
            subjectTypeDialog.dialog("close");
        },
        //保存
        save: function () {
            var url;
            if (subjectTypeFormId.val()) {
                url = "/subjectType/update.do";
            } else {
                url = "/subjectType/save.do";
            }
            subjectTypeForm.form("submit", {
                url: url,
                contentType: false,
                processData: false,
                success: function (data) {
                    data = $.parseJSON(data);
                    if (data.success) {
                        $.messager.alert("温馨提示", data.msg, "info", function () {
                            //关闭对话框
                            subjectTypeDialog.dialog("close");
                            //刷新数据表格
                            subjectTypeDatagrid.datagrid("load");
                        });
                    } else {
                        $.messager.alert("温馨提示", data.msg, "info");
                        //关闭对话框
                        subjectTypeDialog.dialog("close");
                        //刷新数据表格
                        subjectTypeDatagrid.datagrid("load");
                    }
                }
            })
        },
        //超级题目类型格式化
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
    subjectTypeDatagrid.datagrid({
        url: "/subjectType/type_list.do",
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
        toolbar: "#subject_type_datagrid_tb",

        columns: [
            [
                {field: 'id', align: 'center', title: '', checkbox: true},
                {field: 'typeName', align: 'center', title: '类型名称', width: 1},
                {field: 'typeCode', align: 'center', title: '类型编码', width: 1}
            ]
        ]
    });
    subjectTypeCombobox.combobox({
        valueField: 'value',
        textField: 'value',
        panelHeight: 'auto',
        data: [{
            value: 'text',
        }, {
            value: 'image'
        }, {
            value: 'voice'
        }, {
            value: 'video'
        }]
    });
    //新增
    subjectTypeDialog.dialog({
        width: 300,
        height: 350,
        buttons: "#subject_type_dialog_bt",
        closed: true
    });
})