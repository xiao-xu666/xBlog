<template>
  <router-view></router-view>
</template>

<script>
  import * as echarts from 'echarts'
  import effects01 from './utils/js/effects01.js'
  import effects02 from './utils/js/effects02.js'
  import {
    provide,
    onMounted
  } from 'vue'
    import axios from "axios";
  export default {
    name: 'App',
    setup() {
      // 数据表
      provide('ec', echarts) // provide

      onMounted(() => {
        // 查询系统设置
        axios.get("/showAllSystemSetup").then(res => {
          // 点击网站页面显示24字核心价值观上升的特效
          if (res.data.effects01) {
            let body = document.getElementsByTagName("body")[0];
            body.classList.add('effects01')
          }
          // 雪花飘落特效
          if (res.data.effects02) {
            const effects02 = document.getElementById("effects02");
            effects02.style.display = 'block'
          }
        })
      })
    },
    components: {}
  }
</script>

<style>

</style>