<template>
  <div class="EchartsManager">
    <div style="margin: 20px">
      <el-button size="large" type="primary" @click="getCarTypeChart">汽车种类饼状图</el-button>
      <el-button size="large" type="success" @click="getCarNameChart">汽车品牌饼状图</el-button>
      <el-button size="large" type="warning" @click="dailyFunding">公司七日盈亏表</el-button>
    </div>
    <div id="echartsManager" class="EchartsManager" style="height: 900px; margin: 20px">

    </div>
  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts';
import request from "@/request/request";
import {CarStatus} from "@/type/dataDefinition";
import {ElMessage} from "element-plus";

const init = () => {
  dailyFunding();
}
setTimeout(init,  500);

const getCarTypeChart = () => {
  request.get("/car/getCarTypeChart").then((res: any) => {
    if (res.code === 200) {
      type EChartsOption = echarts.EChartsOption;

      const chartDom = document.getElementById('echartsManager')!;
      const myChart = echarts.init(chartDom);
      let option: EChartsOption;
      option = {
        title: {
          text: '汽车种类',
          subtext: '饼状图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 500,
          top: 100,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: res.data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      option && myChart.setOption(option);
    } else {
      ElMessage.error(res.msg);
    }
  })
}

const getCarNameChart = () => {
  request.get("/car/getCarNameChart").then((res: any) => {
    if (res.code === 200) {
      type EChartsOption = echarts.EChartsOption;

      const chartDom = document.getElementById('echartsManager')!;
      const myChart = echarts.init(chartDom);
      let option: EChartsOption;
      option = {
        title: {
          text: '汽车品牌',
          subtext: '饼状图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 500,
          top: 100,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: res.data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      option && myChart.setOption(option);
    } else {
      ElMessage.error(res.msg);
    }
  })
}

const dailyFunding = () => {
  type EChartsOption = echarts.EChartsOption;

  const chartDom = document.getElementById('echartsManager')!;
  const myChart = echarts.init(chartDom);
  let option: EChartsOption;

  request.get("/companyFunding/getDailyFundingChart").then((res: any) => {
    if (res.code === 200) {
      let xAxis = res.data.xAxis;
      for (let key in xAxis) {
        xAxis[key] = new Date(xAxis[key]).toLocaleDateString();
      }

      option = {
        title: {
          text: '公司七日盈亏表',
          subtext: '折现图',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'top',
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: xAxis
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '收入',
            type: 'line',
            stack: 'Total',
            data: res.data.funding
          }
        ]
      };

      option && myChart.setOption(option);
    }
  })
}
</script>

<style>

</style>