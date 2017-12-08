/**
 * Created by twl on 2017/12/8.
 */
var ACCOUNT = {};


$(function () {
    ACCOUNT.init();
});

ACCOUNT.init = function () {
    var datas={};

    $.ajax({
        type: 'post',
        url: "/echarts/getDate",
        data: datas,
        dataType: 'json',
//            async: true,
        success: function (data) {
            RenderMap(data);
        }
    });

};

function RenderMap(data) {

    console.log(data)
    // 基于准备好的dom，初始化echarts实例
    var myCharts = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
        title: {
            text: 'ECharts 柱状图示例'
        },
        tooltip: {},
        legend: {
            data:['销量']
        },
        xAxis: {
            data:data.keys
        },
        yAxis: {},
        series: [{
            name: '销量',
            type: 'bar',
            data: data.values
        }]
    };
    // 使用刚指定的配置项和数据显示图表。
    myCharts.setOption(option);
    // 处理点击事件并且跳转到相应的百度搜索页面
    myCharts.on('click', function (data) {
        alert("触发点击事件！");
    });



    // 饼图。
    echarts.init(document.getElementById('pie')).setOption({

        backgroundColor: '#c5f1c8',//设置背景色
//            textStyle: {//设置文本样式
//                color: 'rgba(255, 255, 255, 0.3)'
//            },

        title: {
            text: 'ECharts 饼图示例'
        },
        itemStyle:{
            /*normal:{//正常展示下的样式
             lineStyle: {//设置视觉引导线样式
             color: '#f1231e'
             },
             //                  color: '#f1231e',//设置扇形的颜色
             // 阴影的大小
             shadowBlur:200,
             // 阴影水平方向上的偏移
             shadowOffsetx:0,
             // 阴影垂直方向上的偏移
             shadowOffsetY:0,
             // 阴影颜色
             shadowColor:'rgba(0,0,0,0.5)'
             }*/
            emphasis: {//鼠标 hover 时候的高亮样式
                textStyle: {//单独设置文本样式
                    color: 'rgba(255, 255, 255, 0.3)'
                },
                shadowBlur: 200,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
        },
        series: {
            name: '访问来源',
            type: 'pie',
//                radius: '55%',//放大比例
//                roseType: 'angle', //南丁格尔图
            data: data.pies
        }
    });

    var testdata = [];

    for (var i = 0; i <= 360; i++) {
        var t = i / 180 * Math.PI;
        var r = Math.sin(2 * t) * Math.cos(2 * t);
        testdata.push([r, i]);
    }
    echarts.init(document.getElementById('line')).setOption({

        title: {
            text: '极坐标双数值轴'
        },
        legend: {
            data: ['line']
        },
        polar: {
            center: ['50%', '54%']
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross'
            }
        },
        angleAxis: {
            type: 'value',
            startAngle: 0
        },
        radiusAxis: {
            min: 0
        },
        series: [{
            coordinateSystem: 'polar',
            name: 'line',
            type: 'line',
            showSymbol: false,
            data: testdata
        }],
        animationDuration: 2000
    });

}

