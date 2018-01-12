$(function () {
    $("#pp").portal({
        fit: true
    });

    var p1 = $('<div></div>').appendTo('body');
    p1.panel({
        title: '当前时间',
        height: 300,
        closable: true,
        collapsible: true,
        content: '<iframe src ="/js/plugin/clock/index.html" style="width:100%;height:99%" frameborder=0></iframe>'
    });

    var p4 = $('<div></div>').appendTo('body');
    p4.panel({
        title: '日程安排',
        height: 330,
        closable: true,
        collapsible: true,
        content: "<div id='cc' class='easyui-calendar' fit='true'></div>"
    });


    $('#pp').portal('add', {
        panel: p1,
        columnIndex: 0
    });
    $('#pp').portal('add', {
        panel: p4,
        columnIndex: 2
    });
});