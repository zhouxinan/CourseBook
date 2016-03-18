$.post("ChartServlet?action=getChartDataForCourseAnswerRate&courseID=" + $("#questionIDDiv").html(), function(data) {
    var a = {};
    for (var i = 1; i < 6; i++) {
        a[i] = 0;
    }
    for (var key in data) {
        a[key] = data[key];
    }
    $('#chartContainer').highcharts({
        //图表区选项
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        //标题选项
        title: {
            text: '用户评分分布'
        },
        //数据点提示框
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        //数据点选项
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                }
            }
        },
        //数据列选项
        series: [{
            type: 'pie',
            name: '评分总揽',
            data: [
                ['☆', Number(a[1])],
                ['☆☆', Number(a[2])],
                ['☆☆☆', Number(a[3])],
                ['☆☆☆☆', Number(a[4])],
                ['☆☆☆☆☆', Number(a[5])]
            ]
        }]
    });
}, "json");