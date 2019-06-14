<template>
  <v-page>
    <v-header>
      <v-back></v-back>
      <h1>收益明细</h1>
    </v-header>
    <div class="main">
      <div class="historyPureValue">
        <div class="historyPureDetail">
          <ul :class="['common-ul', 'compress']">
            <li>
              <div v-picker="{model:period,data:periodList}" class="value select"></div>
            </li>
          </ul>
        </div>
        <div class="holdingProductInfo">
          <div class="holdingInfo">
            <div class="infoItem"><span>期间收益（元）</span></div>
            <div class="infoItem">{{periodProfit}}</div>
          </div>
        </div>
      </div>
    </div>
    <main>
      <v-scroller ref="scroller" :toLoadMore="true" @loadmore="getMore()">
        <div id="profitValue" >
        </div>
      </v-scroller>
    </main>
    <div class="bottom">
      <p>本页面数据仅支持查询2018年11月9日以后的收益明细数据，仅供参考</p>
    </div>
  </v-page>
</template>
<script>
  var echarts = require('echarts/lib/echarts');
  require('echarts/lib/chart/bar');
  export default {
    data() {
      return {
        periodList: [{text: '近一个月', value: '1'}, {text: '近三个月', value: '2'},
          {text: '近半年', value: '3'}, {text: '近一年', value: '4'}],
        period: '1',
        dataAxis: [],
        data: [],
        yMax: 100,
        dataShadow: [],
        periodProfit: '0.00',
        item: {},
        autoHeight: '',
        pageSize: 10,
        beginTime: '',
        endTime: '',
        dataIndex: [],
        loadMoreMark: 0,//日期间隔标志
        dataParams: [],//根据查询时间长短，将日期划分，保存到此数组中。
        begrow: null,
        endrow: null,
        option: {
          grid: {
            top: 1,
            left: 0,
            right: 0,
            bottom: 10
          },
          xAxis: {
            type: 'value',
            axisLabel: {show: false},
            min: "",
            max: "",
            axisTick: {
              show: false
            },
            axisLine: {
              show: false
            },
            splitLine: {
              show: false
            },
          },
          yAxis: {
            silent: true,
            z: 10,
            type: 'category',
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              inside: true,
              textStyle: {
                color: 'white'
              }
            },
            data: [],
          },
          series: [{ // For shadow
            barWidth: 40,
            type: 'bar',
            barGap: '-100%',
            barCategoryGap: '20%',
            data: [],
            animation: false,
            itemStyle: {
              color: 'white',
            }
          },
            {
              z: 10,
              type: 'bar',
              data: [],
              barWidth: 40,
              barMinHeight: 140,
              label: {
                show: true,
                position: "insideRight",
                textStyle: {
                  fontSize: 14,
                },
                formatter: function (value, index) {
                }
              },
              itemStyle: {
                normal: {
                  color: function (params) {

                  },
                }
              },
            }
          ]
        }
      }
    },

    methods: {
      formatDate(date) {
        let year = date.substring(0, 4);
        let month = date.substring(4, 6);
        let day = date.substring(6, 8);
        return year + "-" + month + "-" + day;
      },
      format(item) {
        if (item === 0 || item === "" || item === undefined) {
          return "0.00";
        }
        var array = item.split(".");
        var num = parseFloat(array[0]);
        var reg = /\d{1,3}(?=(\d{3})+$)/g;
        if (array[1] === undefined) {
          item = (num + '').replace(reg, '$&,') + ".00";
        } else {
          item = (num + '').replace(reg, '$&,') + "." + array[1];
        }
        return item;
      },
      query() {
        let vm = this;
        vm.dataAxis = [];
        vm.data = [];
        vm.dataShadow = [];
        vm.dataIndex = [];
        vm.autoHeight = 0;
        vm.loadMoreMark = 0;
        console.log(this.period + "启动");
        this.$request('YJ3073', {
          "begindate": vm.appendZero(vm.dataParams[0].beginTime),//时间开始
          "enddate": vm.appendZero(vm.dataParams[0].endTime),//时间结束
          "accno": vm.item.zjzh,//银行账号
          "jjgsdm": "01",
          "zqdm": vm.item.zqdm,//基金代码
          "pxflag": "",
          "page": "0",//首页，下也标识
          "begrow": "",//开始行数
          "endrow": ""//结束行数
        }, {
          onSuccess(data) {
            vm.periodProfit = vm.format(data.zje);
            if (data.zddrsy !== 0 || data.zddrsy !== "" || data.zddrsy !== undefined) {
              vm.yMax = Math.abs(data.zddrsy).toFixed(2);//当最大收益不为零的时候，就将柱状图的最大值设为最大收益值。
            }
            vm.option.xAxis.max = Math.abs(data.zddrsy).toFixed(2);
            vm.option.xAxis.min = Math.abs(data.zxdrsy).toFixed(2);
            console.log(vm.yMax)
            for (let i = 0; i < data.list.length; i++) {
              vm.dataAxis.push(vm.formatDate(data.list[i].date));
              if (data.list[i].drsy >= 0) {
                vm.dataIndex.push(0);
                vm.data.push(data.list[i].drsy.toFixed(2));
              }
              else {
                vm.dataIndex.push(-1);
                vm.data.push(Math.abs(data.list[i].drsy).toFixed(2));
              }
              vm.dataShadow.push(vm.yMax);
            }
            vm.option.yAxis.data = vm.dataAxis;
            vm.option.series[0].data = vm.dataShadow;
            vm.option.series[1].data = vm.data;
            vm.option.series[1].label.formatter = function (params) {
              if (vm.dataIndex[params.dataIndex] === 0) {
                return "+" + params.data;
              } else if (vm.dataIndex[params.dataIndex] === -1) {
                return "-" + params.data;
              }
            };
            vm.option.series[1].itemStyle.normal.color = function (params) {
              if (vm.dataIndex[params.dataIndex] === -1) {
                return '#25D49B';
              }
              if (vm.dataIndex[params.dataIndex] === 0) {
                return '#fe704c'
              }
            }
            //let myChart = echarts.init(document.getElementById("profitValue"));
            vm.myChart.setOption(vm.option);
            vm.autoHeight = vm.data.length * 50 + 20;
            vm.myChart.getDom().style.height = vm.autoHeight + "px";
            vm.myChart.getDom().childNodes[0].style.height = vm.autoHeight + "px";
            //myChart.getDom().childNodes[0].childNodes[0].setAttribute("height",vm.autoHeight);
            vm.myChart.getDom().childNodes[0].childNodes[0].style.height = vm.autoHeight + "px";
            vm.myChart.resize();
            vm.$refs.scroller.end({nomore: vm.pageSize > data.list.length});
            vm.begrow = data.list[0].rownum;
            vm.endrow = data.list[data.list.length - 1].rownum;
          },
          loading: 1
        })
      },
      getMore() {
        console.log("加载更多")
        let vm = this;
        this.$request('YJ3073', {
          "begindate": vm.appendZero(vm.beginTime),//时间开始
          "enddate": vm.appendZero(vm.endTime),//时间结束
          "accno": vm.item.zjzh,//银行账号
          "jjgsdm": "01",
          "zqdm": vm.item.zqdm,//基金代码
          "pxflag": "",
          "page": "2",//首页，下也标识
          "begrow": vm.begrow,//开始行数
          "endrow": vm.endrow//结束行数
        }, {
          onSuccess(data) {
            for (let i = data.list.length-1; i>=0; i--) {
              vm.dataAxis.unshift(vm.formatDate(data.list[i].date));
              if (data.list[i].drsy >= 0) {
                vm.dataIndex.unshift(0);
                vm.data.unshift(data.list[i].drsy.toFixed(2));
              }
              else {
                vm.dataIndex.unshift(-1);
                vm.data.unshift(Math.abs(data.list[i].drsy).toFixed(2));
              }
              vm.dataShadow.unshift(vm.yMax);
            }
            vm.option.yAxis.data = vm.dataAxis;
            vm.option.series[0].data = vm.dataShadow;
            vm.option.series[1].data = vm.data;
            vm.option.series[1].label.formatter = function (params) {
              if (vm.dataIndex[params.dataIndex] === 0) {
                return "+" + params.data;
              } else if (vm.dataIndex[params.dataIndex] === -1) {
                return "-" + params.data;
              }
            };
            vm.option.series[1].itemStyle.normal.color = function (params) {
              if (vm.dataIndex[params.dataIndex] === -1) {
                return '#25D49B';
              }
              if (vm.dataIndex[params.dataIndex] === 0) {
                return '#fe704c'
              }
            }
            // let myChart = echarts.init(document.getElementById("profitValue"));
            vm.myChart.setOption(vm.option);
            vm.autoHeight = vm.data.length * 50 + 20;
            vm.myChart.getDom().style.height = vm.autoHeight + 100 + "px";
            vm.myChart.getDom().childNodes[0].style.height = vm.autoHeight + "px";
            vm.myChart.getDom().childNodes[0].childNodes[0].setAttribute("height", vm.autoHeight);
            vm.myChart.getDom().childNodes[0].childNodes[0].style.height = vm.autoHeight + "px";
            vm.myChart.resize();
           // vm.$refs.scroller.end({nomore: vm.pageSize > data.list.length});
          if (vm.pageSize > data.list.length) {
              if (vm.loadMoreMark < vm.dataParams.length) {
                vm.loadMoreMark++;
                vm.beginTime = vm.dataParams[vm.loadMoreMark].beginTime;
                vm.endTime = vm.dataParams[vm.loadMoreMark].endTime;
                vm.begrow = data.list[0].rownum;
                vm.endrow = data.list[data.list.length - 1].rownum;
              } else {
                vm.$refs.scroller.end({nomore: vm.pageSize > data.list.length});
              }
            }
          },
          loading: 1
        })
      },
      appendZero(item) {

        let y = item.getFullYear();
        let m = item.getMonth();
        let d = item.getDate();
        if (m < 10) {
          m = "0" + m;
        }
        if (d < 10) {
          d = "0" + d;
        }
        console.log("appendZero " + item)
        return y.toString() + m.toString() + d.toString();//当前时间
      },
      generateDate() {
        console.log("分离日期启动")
        let vm = this;
        vm.dataParams = [];
        if (vm.period === '1') {//一个月时间段规则
          let currentDate = new Date();
          let endTime = new Date(currentDate);
          currentDate.setMonth(currentDate.getMonth() - 1);
          let beginTime = currentDate;
          vm.dataParams.push({"endTime": endTime, "beginTime": beginTime})
        }
        if (vm.period === '2') {//三个月时间段规则
          let currentDate = new Date();
          let endTime = new Date(currentDate);
          currentDate.setMonth(currentDate.getMonth() - 3);
          currentDate.setDate(currentDate.getDate() + 1);
          let beginTime = currentDate;
          vm.dataParams.push({"endTime": endTime, "beginTime": beginTime})
          console.log(vm.dataParams)
        }
        if (vm.period === '3') {//半年时间段规则
          let currentDate = new Date();
          let dataParams = [];
          dataParams[0] = new Date(currentDate);
          for (let i = 1; i < 3; i++) {
            currentDate.setMonth(currentDate.getMonth() - 3);
            currentDate.setDate(currentDate.getDate() + 1);
            dataParams[i] = new Date(currentDate)
          }
          for (let i = 0; i < dataParams.length - 1; i++) {
            vm.dataParams.push({"endTime": dataParams[i], "beginTime": dataParams[i + 1]})
          }
          console.log(vm.dataParams)
        }
        if (vm.period === '4') {//一年时间段规则
          let currentDate = new Date();
          let dataParams = [];
          dataParams[0] = new Date(currentDate);
          for (let i = 1; i < 5; i++) {
            currentDate.setMonth(currentDate.getMonth() - 3);
            currentDate.setDate(currentDate.getDate() + 1);
            dataParams[i] = new Date(currentDate)
          }
          console.log(dataParams);
          for (let i = 0; i < dataParams.length - 1; i++) {
            vm.dataParams.push({"endTime": dataParams[i], "beginTime": dataParams[i + 1]})
          }
          console.log(vm.dataParams)
        }

      },
    },
    created() {
      let currentDate = new Date();
      let endTime = new Date(currentDate);
      this.endTime = endTime;
      currentDate.setMonth(currentDate.getMonth() - 1);
      let beginTime = currentDate;
      this.beginTime = beginTime;
      this.dataParams.push({"endTime": endTime, "beginTime": beginTime})
      this.item = this.$params.item;
    },
    mounted() {
      this.query();
      this.myChart = echarts.init(document.getElementById("profitValue"));
    },
    watch: {
      period: function (val) {
        this.generateDate();
        this.query();
      },
      data() {
        this.$refs.scroller.update();
      }
    }
  }

</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .holdingInfo {
    text-align: center;
    background: #fff;
    padding: .5rem 0;

    .infoItem {
      margin-bottom: .5rem;
    }

    .infoItem:nth-of-type(1) {
    }

    .infoItem:nth-of-type(2) {
      font-size: 2rem;
      color: red;
    }

    .infoItem:last-child {
      margin-bottom: 1rem;

      label {
        margin-right: 1rem;
      }
    }
  }

  .historyPureDetail {
    span {
      padding-right: 1rem;
      color: red;
      float: right;
      font-size: .7rem;
    }

    li {
      height: .2rem;

      div {
        float: right
      }

      a {
        float: right;
      }
    }

    li:nth-of-type(1) {
      div {
        float: right;
      }
    }
  }

  #profitValue {
    width: 100%;
    height: 450px
  }

  .bottom {
    background-color: white;
    text-align: center;
    font-size: 0.8rem;
    padding: 0rem 2rem;
    color: #999;
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 11;
  }
</style>
