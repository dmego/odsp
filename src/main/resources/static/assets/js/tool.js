//自定义滚动条样式
$(".scroll").slimScroll({
    width: 'auto', //可滚动区域宽度
    height: '100%', //可滚动区域高度
    wheelStep: 5, //滚轮滚动量
});

//点击收缩参数设置div
$('#fl-btn').click(function () {
    var isExapnd = $('#fl-btn-l').hasClass('fl-btn-mini');
    if (isExapnd) {
        //如果已经折叠，则点击是恢复
        $('#fl-btn-l').removeClass('fl-btn-mini');
        $('#fl-btn-r').addClass('layui-col-md7 page-main');

        $('#fl-btn-icon').removeClass('layui-icon-left');
        $('#fl-btn-icon').addClass('layui-icon-right');
    } else {
        //否则折叠
        $('#fl-btn-l').addClass('fl-btn-mini');
        $('#fl-btn-r').addClass('fl-btn-block');
        $('#fl-btn-r').removeClass('layui-col-md7 page-main');

        $('#fl-btn-icon').removeClass('layui-icon-right');
        $('#fl-btn-icon').addClass('layui-icon-left');
    }

    if(isNotEmpty(chartsNames)){
        for (var i = 0; i < chartsNames.length; i++) {
            chartsNames[i].resize();
        }
    }
});


function returnMax(chartsData,action,state) {

}


