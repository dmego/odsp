/*
* @Author: Dmego
* @Date:   2018-12-29
* @Note:   fork from https://github.com/9499574/layui-transfer
*/
layui.define('table', function (exports) {
    "use strict";
    var $ = layui.$,
        table = layui.table,
        MOD_NAME = 'transfer',
        LEFT_TABLE = 'left-table-', //左表
        LEFT_DIV = 'left-div-', //左表div
        RIGHT_TABLE1 = 'right-table1-', //上右表
        RIGHT_DIV1 = 'right-div1-', //上右边表div
        RIGHT_TABLE2 = 'right-table2-', //下右表
        RIGHT_DIV2 = 'right-div2-', //下右表div
        LEFT_BTN1 = 'left-btn1-',
        RIGHT_BTN1 = 'right-btn1-',
        BTN_DIV1 = 'btn-div1-', //上右表选择按钮
        LEFT_BTN2 = 'left-btn2-',
        RIGHT_BTN2 = 'right-btn2-',
        BTN_DIV2 = 'btn-div2-', //下右表选择按钮
        FILTER = 'test',
        DISABLED = 'layui-btn-disabled',
        BTN = 'button',
        BTN_STLY = 'layui-btn-sm',
        MD2 = 'layui-col-md2',
        MD3 = 'layui-col-md3',
        MD5 = 'layui-col-md5',
        MD7 = 'layui-col-md7',
        MD9 = 'layui-col-md9',
        MD12 = 'layui-col-md12',
        CENTER = 'text-align: center;padding: 10px;',
        transfer = {
            index: layui.transfer ? (layui.transfer + 1000) : 0,
            idData: [], //ID池
            data: [], //原始数据
            options: [],
            update: function (data, filed) {
                if (filed == '' && data && data.length > 0) {
                    var d = [];
                    $.each(data, function (k, v) {
                        v.LAY_TABLE_INDEX && delete v.LAY_TABLE_INDEX
                        v.LAY_CHECKED && delete v.LAY_CHECKED
                        d.push(v)
                    })
                    return d
                } else if (filed && data && data.length > 0) {
                    d = '';
                    $.each(data, function (k, v) {
                        if (k === 0) {
                            d += v[filed]
                        } else {
                            d += ',' + v[filed]
                        }
                    })
                    return d
                } else {
                    return [];
                }
            },
            get: function (option, type, field = '') {
                var index = option.index
                var data = transfer.data;
                if (!index) {
                    return [];
                }

                if (type === 'all') {
                    var d = [],
                        d1 = [],
                        d2 = [],
                        d3 = [];
                    for (var i = data.length - 1; i >= 0; i--) {
                        if (data[i].id == LEFT_TABLE + index) {
                            d1 = data[i].data;
                        } else if (data[i].id == RIGHT_TABLE1 + index) {
                            d2 = data[i].data;
                        } else if (data[i].id == RIGHT_TABLE2 + index) {
                            d3 = data[i].data
                        }
                    }
                    d.push({
                        left: d1
                    })
                    d.push({
                        right1: d2
                    })
                    d.push({
                        right2: d3
                    })
                    return d
                } else if (type === 'left' || type === 'l') {
                    for (var i = data.length - 1; i >= 0; i--) {
                        if (data[i].id == LEFT_TABLE + index) {
                            return this.update(data[i].data, field);
                        }
                    }
                } else if (type === 'right1' || type === 'r1') {
                    for (var i = data.length - 1; i >= 0; i--) {
                        if (data[i].id == RIGHT_TABLE1 + index) {
                            return this.update(data[i].data, field);
                        }
                    }
                } else if (type === 'right2' || type === 'r2') {
                    for (var i = data.length - 1; i >= 0; i--) {
                        if (data[i].id == RIGHT_TABLE2 + index) {
                            return this.update(data[i].data, field);
                        }
                    }
                }
            }
        },
        thisRate = function () {
            var that = this
            return {
                index: that.index
            }
        },
        Class = function (options) {
            var that = this
            that.index = transfer.index ? transfer.index : ++transfer.index
            that.config = options
            transfer.options = options
            that.createHTMLDocument()
            that.render()
        };
    Class.prototype.createHTMLDocument = function () {
        var that = this,
            elem = that.config.elem,
            index = that.index;
        //创建页面元素
        var html = '<div class="layui-row">\n' +
            '            <div class="'+MD5+'" id="'+LEFT_DIV+index+'">\n' +
            '                <div class="layui-row" style="'+CENTER+'">\n' +
            '                   <div style="'+CENTER+'">\n' +
            '                       <table class="layui-hide" id="'+LEFT_TABLE+index+'" lay-filter="'+FILTER+'"></table>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '            </div>\n' +
            '            <div class="'+MD7+'">\n' +
            '                <div class="layui-row" style="'+CENTER+'">\n' +
            '                    <div class="'+MD3+'">\n' +
            '                        <div style="'+CENTER+'" id="'+BTN_DIV1+index+'">\n' +
            '                            <div id="'+LEFT_BTN1+index+'" style="margin-bottom: 2px;">\n' +
            '                                <button data-type="0" class="layui-btn '+DISABLED+' '+BTN_STLY+'"> <i class="layui-icon">&#xe602;</i></button>\n' +
            '                            </div>\n' +
            '                            <div id="'+RIGHT_BTN1+index+'">\n' +
            '                                <button data-type="2" class="layui-btn '+DISABLED+' '+BTN_STLY+'"> <i class="layui-icon">&#xe603;</i></button>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                    <div class="'+MD9+'" id="'+RIGHT_DIV1+index+'">\n' +
            '                        <div style="'+CENTER+'">\n' +
            '                            <table class="layui-hide" id="'+RIGHT_TABLE1+index+'" lay-filter="'+FILTER+'"></table>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                    <div class="'+MD3+'" id="'+BTN_DIV2+index+'">\n' +
            '                        <div style="'+CENTER+'">\n' +
            '                            <div id="'+LEFT_BTN2+index+'" style="margin-bottom: 2px;">\n' +
            '                                <button data-type="1" class="layui-btn '+DISABLED+' '+BTN_STLY+'"> <i class="layui-icon">&#xe602;</i></button>\n' +
            '                            </div>\n' +
            '                            <div id="'+RIGHT_BTN2+index+'">\n' +
            '                                <button data-type="3" class="layui-btn '+DISABLED+' '+BTN_STLY+'"> <i class="layui-icon">&#xe603;</i></button>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                    <div class="'+MD9+'" id="'+RIGHT_DIV2+index+'">\n' +
            '                        <div style="'+CENTER+'">\n' +
            '                            <table class="layui-hide" id="'+RIGHT_TABLE2+index+'" lay-filter="'+FILTER+'"></table>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </div>\n' +
            '            </div>\n' +
            '       </div>';
        $(elem).html(html)
    }
    //初始化表格
    Class.prototype.render = function () {
        var that = this,
            options = that.config;
        var d1_c = {
            elem: '#'+LEFT_TABLE+that.index,
            cols: [options.cols[0] ? options.cols[0] : []],
            data: (options.data[0] ? options.data[0] : []),
            id: LEFT_TABLE+that.index
        }
        var d2_c = {
            elem: '#'+RIGHT_TABLE1+that.index,
            cols: [options.cols[1] ? options.cols[1] : []],
            data: (options.data[1] ? options.data[1] : []),
            id: RIGHT_TABLE1+that.index
        }
        var d3_c = {
            elem: '#'+RIGHT_TABLE2+that.index,
            cols: [options.cols[2] ? options.cols[2] : []],
            data: (options.data[2] ? options.data[2] : []),
            id: RIGHT_TABLE2+that.index
        }
        if (options.tabConfig) {
            d1_c = $.extend(d1_c, options.tabConfig)
            d2_c = $.extend(d2_c, options.tabConfig)
            d3_c = $.extend(d3_c, options.tabConfig)
        }
        transfer.idData.push(that.index)
        transfer.data = [];
        transfer.data.push({
            id: LEFT_TABLE+that.index,
            data: (options.data[0] ? options.data[0] : [])
        })
        transfer.data.push({
            id: RIGHT_TABLE1+that.index,
            data: (options.data[1] ? options.data[1] : [])
        })
        transfer.data.push({
            id: RIGHT_TABLE2+that.index,
            data: (options.data[2] ? options.data[2] : [])
        })
        table.render(d1_c)
        table.render(d2_c)
        table.render(d3_c)
        that.move()
    };

    //左右移动按钮根据右表格居中
    Class.prototype.move = function () {
        var that = this;
        var h0 = $("#"+LEFT_DIV+that.index).height();
        var h1 = $("#"+RIGHT_DIV1+that.index).height();
        var h2 = $("#"+RIGHT_DIV2+that.index).height();
        $("#"+LEFT_DIV+that.index).css('padding-top', ((h1 + h2) / 2 - (h0 / 2)) + 'px');
        $("#"+BTN_DIV1+that.index).css('padding-top', (h1 / 2 - 44) + 'px');
        $("#"+BTN_DIV2+that.index).css('padding-top', (h2 / 2 - 44) + 'px')
    }
    //点击事件
    $(document).on('click', '.' + BTN_STLY, function () {
        if (!$(this).hasClass(DISABLED)) {
            var othis = $(this),
                type = othis.data('type');
            datas(type)
        }

    })

    //数据处理
    //data 选中数据
    /*type 类型
        0: 左边-->右上
        1: 左边-->右下
        2: 右上-->左边
        3: 右下-->左边
    */
    function datas(type) {
        var d = transfer.data;
        var dl = d[0].data; //左表数据
        var dr1 = d[1].data; //右上表数据
        var dr2 = d[2].data; //右下表数据
        var _d = [];
        if (dl.length > 0 && type == 0) {
            //左边-->右上
            var n_dl = [];
            dl.reverse()
            for (var i = 0; i < dl.length; i++) {
                if (dl[i].LAY_CHECKED === true) {
                    delete dl[i].LAY_CHECKED
                    delete dl[i].LAY_TABLE_INDEX
                    dr1.unshift(dl[i])
                } else {
                    delete dl[i].LAY_TABLE_INDEX
                    n_dl.push(dl[i])
                }
            }
            _d.push(n_dl, dr1, dr2)

        } else if (dl.length > 0 && type == 1) {
            //左边-->右下
            var n_dl = [];
            dl.reverse()
            for (var i = 0; i < dl.length; i++) {
                if (dl[i].LAY_CHECKED === true) {
                    delete dl[i].LAY_CHECKED
                    delete dl[i].LAY_TABLE_INDEX
                    dr2.unshift(dl[i])
                } else {
                    delete dl[i].LAY_TABLE_INDEX
                    n_dl.push(dl[i])
                }
            }
            _d.push(n_dl, dr1, dr2)

        } else if (dr1.length > 0 && type == 2) {
            //右上-->左边
            var n_dr1 = [];
            for (var i = 0; i < dr1.length; i++) {
                if (dr1[i].LAY_CHECKED && dr1[i].LAY_CHECKED === true) {
                    delete dr1[i].LAY_CHECKED
                    dr1[i].LAY_TABLE_INDEX && delete dr1[i].LAY_TABLE_INDEX
                    dl.push(dr1[i])
                } else {
                    delete dr1[i].LAY_TABLE_INDEX
                    n_dr1.push(dr1[i])
                }
            }
            _d.push(dl, n_dr1, dr2)

        }else if (dr2.length > 0 && type == 3) {
            //右下-->左边
            var n_dr2 = [];
            for (var i = 0; i < dr2.length; i++) {
                if (dr2[i].LAY_CHECKED && dr2[i].LAY_CHECKED === true) {
                    delete dr2[i].LAY_CHECKED
                    dr2[i].LAY_TABLE_INDEX && delete dr2[i].LAY_TABLE_INDEX
                    dl.push(dr2[i])
                } else {
                    delete dr2[i].LAY_TABLE_INDEX
                    n_dr2.push(dr2[i])
                }
            }
            _d.push(dl, dr1, n_dr2)
        }
        var options = transfer.options
        options.data = datasChecked(_d);
        transfer.render(options)
    }

    function datasChecked(data) {
        var d1 = [];
        var d2 = [];
        var d3 = [];
        if (data[0] && data[0].length) {
            $.each(data[0], function (k, v) {
                v.LAY_CHECKED === true && delete v.LAY_CHECKED
                d1.push(v)
            });
        }
        if (data[1] && data[1].length) {
            $.each(data[1], function (k, v) {
                v.LAY_CHECKED === true && delete v.LAY_CHECKED
                d2.push(v)
            });
        }
        if (data[2] && data[2].length) {
            $.each(data[2], function (k, v) {
                v.LAY_CHECKED === true && delete v.LAY_CHECKED
                d3.push(v)
            });
        }
        return [d1, d2, d3];
    }


    //选中状态
    table.on('checkbox('+FILTER+')', function (obj) {
        var idData = transfer.idData,
            lenght = idData.length;
        if (lenght > 0) {
            for (var i = 0; i <= lenght - 1; i++) {
                var checkStatus1 = table.checkStatus(LEFT_TABLE + idData[i]),
                    data_1 = checkStatus1.data,
                    checkStatus2 = table.checkStatus(RIGHT_TABLE1 + idData[i]),
                    data_2 = checkStatus2.data,
                    checkStatus3 = table.checkStatus(RIGHT_TABLE2 + idData[i]),
                    data_3 = checkStatus3.data;

                if (data_1.length > 0) {//如果选择左表格,右上右下都可以
                    $('#' + LEFT_BTN1 + idData[i]).children(BTN).removeClass(DISABLED);
                    $('#' + LEFT_BTN2 + idData[i]).children(BTN).removeClass(DISABLED);
                } else {
                    $('#' + LEFT_BTN1 + idData[i]).children(BTN).addClass(DISABLED);
                    $('#' + LEFT_BTN2 + idData[i]).children(BTN).addClass(DISABLED);
                }
                if (data_2.length > 0) {
                    $('#' + RIGHT_BTN1 + idData[i]).children(BTN).removeClass(DISABLED);
                } else {
                    $('#' + RIGHT_BTN1 + idData[i]).children(BTN).addClass(DISABLED);
                }
                if (data_3.length > 0) {
                    $('#' + RIGHT_BTN2 + idData[i]).children(BTN).removeClass(DISABLED);
                } else {
                    $('#' + RIGHT_BTN2 + idData[i]).children(BTN).addClass(DISABLED);
                }
            }
        }
    });

    transfer.render = function (options) {
        var inst = new Class(options)
        return thisRate.call(inst)
    }
    exports(MOD_NAME, transfer)
})