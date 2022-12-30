<template>
    <div class="websiteInfo">
        <div class="titleWrapper" data-v-d40d816c="">
            <h3 class="title">站点信息</h3>
        </div>
        <ul>
            <li><b>建站时间：</b>2022年11月10日</li>
            <li><b>站点主题：</b>自主设计</li>
            <li><b>采用技术：</b>Java、Vue3等前后端开发技术</li>
            <li><b>文章统计：</b>{{articleCount}} 篇文章</li>
            <li><b>本站走过：</b>{{distance}}</li>
        </ul>
    </div>
</template>

<script setup>
    import {
        ref,
        toRefs,
        reactive,
        onMounted,
        watch,
        onUpdated
    } from 'vue'

    import axios from "axios";

    const articleCount = ref(0)
    // 查询通过审核的文章总数量
    axios.get("/article/showArticleCount").then(res => {
        articleCount.value = res.data.data < 10 ? "0" + res.data.data : res.data.data
    })

    const distance = ref('00 天 00 时 00 分 00 秒');
    setInterval(() => {
        var day = Math.floor(new Date().getTime() / 1000) - (new Date('2022/11/10 00:00:00').getTime() / 1000),
            day2 = Math.floor(day / (24 * 3600)),
            day3 = day2 * 24 * 3600,
            day4 = day - day3,
            day5 = Math.floor(day4 / 3600),
            day6 = day4 - day5 * 3600,
            day7 = Math.floor(day6 / 60),
            day8 = day6 - day7 * 60;
        distance.value = `${day2} 天 ${day5} 时 ${day7} 分 ${day8} 秒`
    }, 1000)
</script>

<style scoped>
    .websiteInfo {
        padding: 20px;
        border-radius: 10px;
        background-color: white;
        margin-bottom: 20px;
        overflow: hidden;
    }

    .titleWrapper {
        margin-bottom: 15px;
        margin-top: -10px;
        display: flex;
        justify-content: space-between;
        border-bottom: 1px solid rgb(238, 238, 238);
        overflow: hidden;
    }

    .websiteInfo .title {
        display: inline-block;
        border-bottom: 4px solid #2fa7b9;
        line-height: 40px;
        color: rgb(69, 69, 69);
    }

    .websiteInfo li {
        margin-bottom: 8px;
        font-size: 15px;
    }

    .websiteInfo_img {
        width: 80%;
        margin: auto;
    }

    .websiteInfo_img img {
        width: 100%;
    }
</style>