layui.define(['layer'], function (exports) {
    var $ = layui.jquery;
    var layer = layui.layer;

    var admin = {
        // 设置侧栏折叠
        flexible: function (expand) {
            var isExapnd = $('.layui-layout-admin').hasClass('admin-nav-mini');
            if (isExapnd == !expand) {
                return;
            }
            if (expand) {
                $('.layui-layout-admin').removeClass('admin-nav-mini');
            } else {
                $('.layui-layout-admin').addClass('admin-nav-mini');
            }
            $('.admin-nav-hover>.layui-nav-child').css('top', 'auto');
            $('.admin-nav-hover').removeClass('admin-nav-hover');
        },
        // 设置导航栏选中
        activeNav: function (url) {
            if (!url) {
                url = window.location.pathname;
            }
            $('.layui-layout-admin .layui-side .layui-nav .layui-nav-item .layui-nav-child dd').removeClass('layui-this');
            $('.layui-layout-admin .layui-side .layui-nav .layui-nav-item').removeClass('layui-this');
            if (url && url != '') {
                $('.layui-layout-admin .layui-side .layui-nav .layui-nav-item').removeClass('layui-nav-itemed');
                var $a = $('.layui-layout-admin .layui-side .layui-nav a[lay-href="' + url + '"]');
                $a.parent('li').addClass('layui-this');  // 一级菜单
                $a.parent('dd').addClass('layui-this');  // 二级菜单
                $a.parent('dd').parent('.layui-nav-child').parent('.layui-nav-item').addClass('layui-nav-itemed');  // 二级菜单
                $a.parent('dd').parent('.layui-nav-child').parent('dd').addClass('layui-nav-itemed');  // 三级菜单
                $a.parent('dd').parent('.layui-nav-child').parent('dd').parent('.layui-nav-child').parent('.layui-nav-item').addClass('layui-nav-itemed');  // 三级菜单
            }
        },
        // 右侧弹出
        popupRight: function (param) {
            var eCallBack = param.end;
            param.title = param.title ? param.title : false;
            param.anim = 2;
            param.isOutAnim = false;
            param.closeBtn = false;
            param.offset = 'r';
            param.shadeClose = true;
            param.area = '336px';
            param.skin = 'layui-layer-adminRight';
            param.end = function () {
                layer.closeAll('tips');
                eCallBack ? eCallBack : '';
            };
            return admin.open(param);
        },
        // 封装layer.open
        open: function (param) {
            param.area = param.area ? param.area : '380px';
            param.offset = param.offset ? param.offset : '30px';
            param.resize = param.resize ? param.resize : false;
            param.shade = param.shade ? param.shade : .1;
            return layer.open(param);
        },

        // 封装ajax请求，返回数据类型为json
        req: function (url, data, success, method) {
            admin.ajax({
                url: url,
                data: data,
                type: method,
                dataType: 'json',
                success: success
            });
        },
        // 封装ajax请求
        ajax: function (param) {
            var successCallback = param.success;
            param.success = function (result, status, xhr) {
                // 判断登录过期和没有权限
                var jsonRs;
                if ('json' == param.dataType.toLowerCase()) {
                    jsonRs = result;
                } else if ('html' == param.dataType.toLowerCase() || 'text' == param.dataType.toLowerCase()) {
                    jsonRs = admin.parseJSON(result);
                }
                if (jsonRs) {
                    if (jsonRs.code == 401) {
                        layer.msg(jsonRs.msg, {icon: 2, time: 1500}, function () {
                            location.replace('./login');
                        }, 1000);
                        return;
                    } else if ('html' == param.dataType.toLowerCase() && jsonRs.code == 403) {
                        layer.msg(jsonRs.msg, {icon: 2});
                    }
                }
                successCallback(result, status, xhr);
            };
            param.error = function (xhr) {
                param.success({code: xhr.status, msg: xhr.statusText});
            };
            $.ajax(param);
        },
        // 缓存临时数据
        putTempData: function (key, value) {
            if (value) {
                layui.sessionData('tempData', {key: key, value: value});
            } else {
                layui.sessionData('tempData', {key: key, remove: true});
            }
        },
        // 获取缓存临时数据
        getTempData: function (key) {
            return layui.sessionData('tempData')[key];
        },
        // 判断是否为json
        parseJSON: function (str) {
            if (typeof str == 'string') {
                try {
                    var obj = JSON.parse(str);
                    if (typeof obj == 'object' && obj) {
                        return obj;
                    }
                } catch (e) {
                }
            }
        },
        // 滑动选项卡
        rollPage: function (d) {
            var $tabTitle = $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title');
            var left = $tabTitle.scrollLeft();
            if ('left' === d) {
                $tabTitle.scrollLeft(left - 120);
            } else if ('auto' === d) {
                var autoLeft = 0;
                $tabTitle.children("li").each(function () {
                    if ($(this).hasClass('layui-this')) {
                        return false;
                    } else {
                        autoLeft += $(this).outerWidth();
                    }
                });
                $tabTitle.scrollLeft(autoLeft - 47);
            } else {
                $tabTitle.scrollLeft(left + 120);
            }
        },
        // 刷新主体部分
        refresh: function () {
            var $iframe = $('.layui-layout-admin .layui-body .layui-tab-content .layui-tab-item.layui-show .admin-iframe');
            if (!$iframe) {
                $iframe = $('.layui-layout-admin .layui-body>.admin-iframe');
            }
            if ($iframe) {
                $iframe[0].contentWindow.location.reload(true);
            }
        },
        // 关闭选项卡操作菜单
        closeTabOperNav: function () {
            $('.layui-icon-down .layui-nav .layui-nav-child').removeClass('layui-show');
        },
        // 设置主题
        changeTheme: function (theme) {
            top.removeTheme();
            !theme || top.layui.link(theme);
            var ifs = top.window.frames;
            for (var i = 0; i < ifs.length; i++) {
                var tif = ifs[i];
                try {
                    tif.removeTheme();
                } catch (e) {
                    // console.log(e);
                }
                if (theme && tif.layui) {
                    tif.layui.link(theme);
                }
            }
        }
    };

    // ewAdmin提供的事件
    admin.events = {
        // 折叠侧导航
        flexible: function (e) {
            var expand = $('.layui-layout-admin').hasClass('admin-nav-mini');
            admin.flexible(expand);
        },
        //后退
        back: function () {
            history.back();
        },
        // 设置主题
        theme: function () {
            admin.popupRight({
                type: 2,
                content: 'tpl/theme'
            });
        },
        // 全屏
        fullScreen: function (e) {
            var ac = 'layui-icon-screen-full', ic = 'layui-icon-screen-restore';
            var ti = $(this).find('i');

            var isFullscreen = document.fullscreenElement || document.msFullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || false;
            if (isFullscreen) {
                var efs = document.exitFullscreen || document.webkitExitFullscreen || document.mozCancelFullScreen || document.msExitFullscreen;
                if (efs) {
                    efs.call(document);
                } else if (window.ActiveXObject) {
                    var ws = new ActiveXObject('WScript.Shell');
                    ws && ws.SendKeys('{F11}');
                }
                ti.addClass(ac).removeClass(ic);
            } else {
                var el = document.documentElement;
                var rfs = el.requestFullscreen || el.webkitRequestFullscreen || el.mozRequestFullScreen || el.msRequestFullscreen;
                if (rfs) {
                    rfs.call(el);
                } else if (window.ActiveXObject) {
                    var ws = new ActiveXObject('WScript.Shell');
                    ws && ws.SendKeys('{F11}');
                }
                ti.addClass(ic).removeClass(ac);
            }
        },
        // 刷新主体部分
        refresh: function () {
            admin.refresh();
        },
        // 左滑动tab
        leftPage: function () {
            admin.rollPage("left");
        },
        // 右滑动tab
        rightPage: function () {
            admin.rollPage();
        },
        // 关闭当前选项卡
        closeThisTabs: function () {
            admin.closeTabOperNav();
            var $title = $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title');
            if ($title.find('li').first().hasClass('layui-this')) {
                layer.msg('主页不能关闭', {icon: 2});
                return;
            }
            $title.find('li.layui-this').find(".layui-tab-close").trigger("click");
        },
        // 关闭其他选项卡
        closeOtherTabs: function () {
            $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title li:gt(0):not(.layui-this)').find('.layui-tab-close').trigger('click');
            admin.closeTabOperNav();
        },
        // 关闭所有选项卡
        closeAllTabs: function () {
            $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title li:gt(0)').find('.layui-tab-close').trigger('click');
            $('.layui-layout-admin .layui-body .layui-tab .layui-tab-title li:eq(0)').trigger('click');
            admin.closeTabOperNav();
        },
        // iframe关闭自己所在的弹窗
        closeDialog: function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        }
    };

    // 所有ew-event
    $('body').on('click', '*[ew-event]', function () {
        var event = $(this).attr('ew-event');
        var te = admin.events[event];
        te && te.call(this, $(this));
    });

    // 移动设备遮罩层点击事件
    $('.site-mobile-shade').click(function () {
        admin.flexible(true);
    });

    // 侧导航折叠状态下鼠标经过显示提示
    var isHover = false;
    $('body').on('mouseenter', '.layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item>a', function () {
        if (document.body.clientWidth > 750) {
            var $that = $(this);
            $('.admin-nav-hover>.layui-nav-child').css('top', 'auto');
            $('.admin-nav-hover').removeClass('admin-nav-hover');
            $that.parent().addClass('admin-nav-hover');
            var $nav = $('.admin-nav-hover>.layui-nav-child');
            if ($nav.length > 0) {
                var isOver = ($that.offset().top + $nav.outerHeight()) > window.innerHeight;  // 是否溢出屏幕
                if (isOver) {
                    $nav.css('top', $that.offset().top - $nav.outerHeight() + $that.outerHeight());
                } else {
                    $nav.css('top', $that.offset().top);
                }
                isHover = true;
            } else {
                var tipText = $that.find('cite').text();
                var bgColor = $('.layui-layout-admin .layui-side').css('background-color');
                bgColor = (bgColor == 'rgb(255, 255, 255)' ? '#009688' : bgColor);
                layer.tips(tipText, $that, {tips: [2, bgColor], time: -1});
            }
        }
    }).on('mouseleave', '.layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item>a', function () {
        layer.closeAll('tips');
    });

    // 鼠标离开侧导航关闭折叠浮窗
    $('body').on('mouseleave', '.layui-layout-admin.admin-nav-mini .layui-side', function () {
        isHover = false;
        setTimeout(function () {
            if (!isHover) {
                $('.admin-nav-hover>.layui-nav-child').css('top', 'auto');
                $('.admin-nav-hover').removeClass('admin-nav-hover');
            }
        }, 500);
    });

    $('body').on('mouseenter', '.layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item.admin-nav-hover .layui-nav-child', function () {
        isHover = true;
    });

    // 侧导航折叠状态下点击展开
    $('body').on('click', '.layui-layout-admin.admin-nav-mini .layui-side .layui-nav .layui-nav-item>a', function () {
        if (document.body.clientWidth > 750) {
            layer.closeAll('tips');
            $('li.layui-nav-itemed').removeClass('layui-nav-itemed');
            $(this).parent().addClass('layui-nav-itemed');
            admin.flexible(true);
        }
    });

    // 所有lay-tips处理
    $('body').on('mouseenter', '*[lay-tips]', function () {
        var tipText = $(this).attr('lay-tips');
        var dt = $(this).attr('lay-direction');
        var bgColor = $(this).attr('lay-bg');
        layer.tips(tipText, this, {tips: [dt || 3, bgColor || '#333333'], time: -1});
    }).on('mouseleave', '*[lay-tips]', function () {
        layer.closeAll('tips');
    });

    exports('admin', admin);
});
