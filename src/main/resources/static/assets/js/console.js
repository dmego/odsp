var option1 = {
    animationDurationUpdate: 1500,
    animationEasingUpdate: 'quinticInOut',
    title: {
        text: '平台概览',
        subtext: "共计 30 种模型",
        left: "left"
    },
    legend: {
        orient: 'vertical',
        x: 'left',
        data: ['不确定型决策法','风险决策法','数据包络分析','动态规划','决策规划','图与网络分析']
    },

    series: [{
        type: 'graph',
        tooltip: {},
        ribbonType: true,
        layout: 'circular',

        circular: {
            rotateLabel: true
        },
        symbolSize: 30,
        roam: true,
        focusNodeAdjacency: true,

        label: {
            normal: {
                position: 'center',
                formatter: '{b}',
                normal: {
                    textStyle: {
                        fontFamily: '宋体'
                    }
                }
            }
        },

        edgeSymbol: ['circle'],
        edgeSymbolSize: [4, 10],
        edgeLabel: {
            normal: {
                textStyle: {
                    fontSize: 13,
                    fontFamily: '宋体'
                }
            }
        },

        itemStyle: {
            normal: {
                label: {
                    rotate: true,
                    show: true,
                    textStyle: {
                        color: '#333'
                    }
                },

                color: ["#393f51", "#393f51", "#393f51", "#393f51", "#393f51", "#393f51", "#393f51", "#85d6f7", "#85d6f7", "#85d6f7", "#85d6f7", "#85d6f7", "#85d6f7", "#85d6f7", "#85d6f7", "#85d6f7", "#85d6f7"] /* 内的颜色#393f51，外的颜色#85d6f7 */
            },
            emphasis: {
                label: {
                    show: true
                    // textStyle: null      // 默认使用全局文本样式，详见TEXTSTYLE
                }
            }
        },

        data: [
            {
                name: '数据包络分析',
                symbolSize: 20,
                itemStyle: {
                    normal: {
                        color: '#32e3ec'
                    }
                }
            },
            {
                name: '悲观决策法',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#f56e12'
                    }
                }
            }, {
                name: '乐观决策法',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            }, {
                name: '0-1背包',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: '多重背包',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: '最小机会损失准则',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: '最大流问题',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            }, {
                name: '最小费用最大流',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: 'BBC模型',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: 'CCR模型',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },

            {
                name: '不确定型决策法',
                symbolSize: 25,
                itemStyle: {
                    normal: {
                        color: '#f5c312'
                    }
                }
            },{
                name: 'NCN模型',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#39c4bf'
                    }
                }
            },{
                name: '最短路径',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: 'NDSC模型',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#6ac439'
                    }
                }
            },
            {
                name: '完全背包',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },{
                name: '0-1规划',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: '风险决策法',
                symbolSize: 20,
                itemStyle: {
                    normal: {
                        color: '#c1f488'
                    }
                }
            },
            {
                name: 'DRS模型',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: '等概率准则',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            }, {
                name: '折中主义准则',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            }, {
                name: '资源分配',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            }, {
                name: '生产库存',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },{
                name: 'IRS模型',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },  {
                name: 'EMV决策法',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: 'EOL决策法',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },{
                name: '最小生成树',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },{
                name: 'GRS模型',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },
            {
                name: '整数规划',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },{
                name: 'SBM模型',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#2a1469'
                    }
                }
            },
            {
                name: 'EVPI决策法',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            }, {
                name: '线性规划',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },{
                name: 'EUV决策法',
                symbolSize: 10,
                itemStyle: {
                    normal: {
                        color: '#4abce8'
                    }
                }
            },{
                name: '动态规划',
                symbolSize: 25,
                itemStyle: {
                    normal: {
                        color: '#6ac439'
                    }
                }
            },
            {
                name: '决策规划',
                symbolSize: 30,
                itemStyle: {
                    normal: {
                        color: '#6df2c5'
                    }
                }
            },
            {
                name: '图与网络分析',
                symbolSize: 20,
                itemStyle: {
                    normal: {
                        color: '#f56e12'
                    }
                }
            }
        ],

        links: [{
            source: '数据包络分析',
            target: 'BBC模型',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#32e3ec'
                }
            }
        }, {
            source: '数据包络分析',
            target: 'CCR模型',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#32e3ec'
                }
            }
        }, {
            source: '数据包络分析',
            target: 'DRS模型',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#32e3ec'
                }
            }
        }, {
            source: '数据包络分析',
            target: 'IRS模型',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#32e3ec'
                }
            }
        }, {
            source: '数据包络分析',
            target: 'GRS模型',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#32e3ec'
                }
            }
        }, {
            source: '数据包络分析',
            target: 'SBM模型',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#32e3ec'
                }
            }
        }, {
            source: '数据包络分析',
            target: 'NCN模型',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#32e3ec'
                }
            }
        }, {
            source: '数据包络分析',
            target: 'NDSC模型',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#32e3ec'
                }
            }
        }, {
            source: '不确定型决策法',
            target: '悲观决策法',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#f5c312'
                }
            }
        }, {
            source: '不确定型决策法',
            target: '乐观决策法',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#f5c312'
                }
            }
        }, {
            source: '不确定型决策法',
            target: '最小机会损失准则',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#f5c312'
                }
            }
        }, {
            source: '不确定型决策法',
            target: '等概率准则',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#f5c312'
                }
            }
        }, {
            source: '不确定型决策法',
            target: '折中主义准则',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#f5c312'
                }
            }
        }, {
            source: '风险决策法',
            target: 'EMV决策法',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#c1f488'
                }
            }
        }, {
            source: '风险决策法',
            target: 'EOL决策法',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#c1f488'
                }
            }
        }, {
            source: '风险决策法',
            target: 'EVPI决策法',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#c1f488'
                }
            }
        }, {
            source: '风险决策法',
            target: 'EUV决策法',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#c1f488'
                }
            }
        }, {
            source: '动态规划',
            target: '0-1背包',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#6ac439'
                }
            }
        }, {
            source: '动态规划',
            target: '多重背包',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#6ac439'
                }
            }
        }, {
            source: '动态规划',
            target: '完全背包',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#6ac439'
                }
            }
        }, {
            source: '动态规划',
            target: '资源分配',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#6ac439'
                }
            }
        }, {
            source: '动态规划',
            target: '生产库存',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#6ac439'
                }
            }
        }, {
            source: '决策规划',
            target: '线性规划',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#6df2c5'
                }
            }
        }, {
            source: '决策规划',
            target: '整数规划',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#6df2c5'
                }
            }
        }, {
            source: '决策规划',
            target: '0-1规划',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#6df2c5'
                }
            }
        }, {
            source: '图与网络分析',
            target: '最短路径',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#f56e12'
                }
            }
        }, {
            source: '图与网络分析',
            target: '最小生成树',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#f56e12'
                }
            }
        }, {
            source: '图与网络分析',
            target: '最大流问题',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#f56e12'
                }
            }
        }, {
            source: '图与网络分析',
            target: '最小费用最大流',
            name: '',
            tooltip: {
                trigger: 'item',
                formatter: function (params, ticket, callback) {
                    return params.data.name;
                }
            },
            symbolSize: [5, 10],
            label: {
                normal: {
                    formatter: function (params, ticket, callback) {
                        params.name = params.data.name;
                        return params.name;
                    },
                    show: true
                }
            },
            lineStyle: {
                normal: {
                    width: 1.0,
                    curveness: 0.2,
                    color: '#f56e12'
                }
            }
        }
        ]
    }]
};

var option2 = {
    title: {
        text: '平台优势'
    },
    backgroundColor:'#fff',
    radar: {
        axisLine:{
            lineStyle:{
                color:'#f7f7f9'
            }
        },
        splitLine:{
            lineStyle:{
                color:['#f7f7f9','#f7f7f9','#f7f7f9']
            }
        },
        splitArea: {
            areaStyle: {
                color: '#f7f7f9'
            }
        },
        shape: 'circle',
        name: {
            textStyle: {
                color: '#333',
                fontSize:'14'
            }
        },
        indicator: [
            { name: '模型覆盖领域广'},
            { name: '可按需设置参数'},
            { name: '当前页切换模型直接计算'},
            { name: '主要参数说明'},
            { name: '分析结果一键保存'},
        ]
    },
    series: [{

        type: 'radar',
        data : [
            {
                value : [30,30, 30, 30, 20],
            }
        ],

        areaStyle: {
            normal: {
                opacity: .8
            }
        }

    }]
};


var option3 = {
    title: {
        text: '平台优势',
        subtext: '5 大优势',
        left: 'center'
    },
    graphic: {
        elements: [{
            type: 'image',
            style: {
                image: imageUrl,
                width: 30,
                height: 30
            },
            left: 'center',
            top: 'center'
        }]
    },
    legend: {
        type:'scroll',
        bottom: 10,
        data:['模型覆盖领域广','可按需设置参数','当前页切换模型直接计算','主要参数说明','分析结果一键保存']
    },
    left:'5%',
    right: '5%',
    series:[{
        type: 'pie',
        radius: [30, '55%'],
        center: ['50%', '50%'],
        roseType: 'radius',
        color: ['#f2c955', '#00a69d','#f56e12','#46d185', '#ec5845'],

        data: [{
            value: 20,
            name: '模型覆盖领域广'
        }, {
            value: 25,
            name: '可按需设置参数'
        }, {
            value: 20,
            name: '当前页切换模型直接计算'
        }, {
            value: 20,
            name: '主要参数说明'
        },{
            value: 15,
            name: '分析结果一键保存'
        }],

        label: {
            formatter: function(param) {
                return param.name;
            }
        },
        labelLine: {
            normal: {
                smooth: true,
                lineStyle: {
                    width: 2
                }
            }
        },
        itemStyle: {
            normal: {
                shadowBlur: 30,
                shadowColor: 'rgba(0, 0, 0, 0.4)'
            }
        },

        animationType: 'scale',
        animationEasing: 'elasticOut',
        animationDelay: function(idx) {
            return Math.random() * 200;
        }
    }]
};