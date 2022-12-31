<template>
    <div class="featured" v-loading="loading" element-loading-text="玩命加载中...">
        <div class="featured_title">
            <h3 class="title"><b>精选</b></h3>
        </div>
        <div class="featured_layout">
            <div v-for="(item,index) in articleInfo">
                <div class="contentBox" v-if="index===0">
                    <!-- <span @click="articleDetails(item)" :title="item.articleTitle">
                        <img v-if="item.articleImgLitimg" class="bannerImg" :src="item.articleImgLitimg"
                            :alt="item.articleTitle" @click="articleDetails(item)">
                        <h4 class="title" @click="articleDetails(item)">{{item.articleTitle}}</h4>
                        <p>{{item.articleClassifyName}} · {{item.publishTime}}</p>
                    </span> -->
                    <a :href="'/articleDetails/'+item.articleId" :title="item.articleTitle">
                        <img v-if="item.articleImgLitimg" class="bannerImg" :src="item.articleImgLitimg"
                            :alt="item.articleTitle" @click="articleDetails(item)">
                        <h4 class="title">{{item.articleTitle}}</h4>
                        <p>{{item.articleClassifyName}} · {{item.publishTime}}</p>
                    </a>
                </div>


                <div class="contentBox item" v-if="index>0">
                    <a :href="'/articleDetails/'+item.articleId" :title="item.articleTitle">
                        <div v-if="item.articleImgLitimg" class="banner">
                            <img :src="item.articleImgLitimg" :alt="item.articleTitle">
                        </div>
                        <!-- 判断是否有缩略图，没有则样式不同 -->
                        <div :class="[item.articleImgLitimg ? 'articleContent':'articleContent2']">
                            <h4 class="title">{{item.articleTitle}}</h4>
                            <p>{{item.articleClassifyName}} · {{item.publishTime}}</p>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import axios from "axios";
    import {
        val
    } from "dom7";
    import {
        forEach
    } from "lodash";
    import {
        useRouter
    } from 'vue-router'
    import {
        onMounted,
        reactive,
        toRefs
    } from "vue";
    /**
     * @desc 格式化日期字符串
     * @param { Nubmer} - 时间戳
     * @returns { String } 格式化后的日期字符串
     */
    function formatDate(timestamp) {
        // 补全为13位
        let arrTimestamp = (timestamp + '').split('');
        for (let start = 0; start < 13; start++) {
            if (!arrTimestamp[start]) {
                arrTimestamp[start] = '0';
            }
        }
        timestamp = arrTimestamp.join('') * 1;
        let minute = 1000 * 60;
        let hour = minute * 60;
        let day = hour * 24;
        let month = day * 30;
        let now = new Date().getTime();
        let diffValue = now - timestamp;
        // 如果本地时间反而小于变量时间
        if (diffValue < 0) {
            return '不久前';
        }
        // 计算差异时间的量级
        let monthC = diffValue / month;
        let weekC = diffValue / (7 * day);
        let dayC = diffValue / day;
        let hourC = diffValue / hour;
        let minC = diffValue / minute;
        // 数值补0方法
        let zero = function (value) {
            if (value < 10) {
                return '0' + value;
            }
            return value;
        };
        // 使用
        if (monthC > 4) {
            // 超过1年，直接显示年月日
            return (function () {
                let date = new Date(timestamp);
                return date.getFullYear() + '年' + zero(date.getMonth() + 1) + '月' + zero(date.getDate()) + '日';
            })();
        } else if (monthC >= 1) {
            return parseInt(monthC) + '月前';
        } else if (weekC >= 1) {
            return parseInt(weekC) + '周前';
        } else if (dayC >= 1) {
            return parseInt(dayC) + '天前';
        } else if (hourC >= 1) {
            return parseInt(hourC) + '小时前';
        } else if (minC >= 1) {
            return parseInt(minC) + '分钟前';
        }
        return '刚刚';
    }

    function loadData(state) {
        state.loading = true
        // 查询系统设置中设置好的精选文章的文章id
        axios.get("/showAllSystemSetup").then(res => {
            state.featuredData = JSON.parse(res.data.data[0].featuredArticle);
        })

        // 查询全部文章
        axios.get("/article/showAllArticleInfo").then(res => {
            state.featuredData.forEach(val => {
                res.data.data.forEach(element => {// 筛选出系统设置中设置好的精选文章
                    if (element.articleId === val) {
                        // 时间戳格式化
                        element.publishTime = formatDate(element.publishTime);
                        // 图片 根url
                        const url = process.env.VUE_APP_URL;
                        // 缩略图 判断是点击上传的还是，网络图片
                        if (element.articleImgLitimg != "" && !element.articleImgLitimg
                            .includes('http') && !element.articleImgLitimg
                            .includes('https')) {
                            element.articleImgLitimg = url + element.articleImgLitimg
                        }
                        // 添加
                        state.articleInfo.push(element)
                    }
                });
                state.loading = false
            });
        })
    }
    export default {
        setup() {
            const state = reactive({
                // 精选文章的文章id
                featuredData: [],
                // 筛选出来的文章内容
                articleInfo: [],
                loading:false
            });
            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });
            return {
                //...toRefs 对响应式数据 解构赋值   
                ...toRefs(state)
            };
        }
    }
</script>

<style scoped>
    .featured {
        background: rgb(255, 255, 255);
        border-radius: 6px;
        padding: 20px;
        margin-bottom: 20px;
    }

    .featured_title {
        margin-bottom: 15px;
        margin-top: -10px;
        display: flex;
        justify-content: space-between;
        border-bottom: 1px solid rgb(238, 238, 238);
        overflow: hidden;
    }

    .featured_title .title {
        line-height: 40px;
        color: #474749;
    }

    .featured_title .title b {
        display: inline-block;
        border-bottom: 4px solid #2fa7b9;
    }

    .featured_layout .contentBox {
        margin-bottom: 15px;
        position: relative;
        overflow: hidden;
        font-size: 12px;
        color: #999;
        line-height: 18px;
    }

    .featured_layout .contentBox h4:hover {
        color: #2fa7b9;
    }

    .featured_layout .contentBox span {
        cursor: pointer;
    }

    .featured_layout .bannerImg {
        display: block;
        width: 100%;
        object-fit: cover;
        margin-bottom: 10px;
        border-radius: 6px;
    }

    .featured_layout .title {
        font-size: 15px;
        line-height: 24px;
        margin-bottom: 10px;
        color: #454545;
    }

    .featured_layout p {
        font-size: 12px;
        color: #999;
    }

    .featured_layout .contentBox.item {
        padding-top: 15px;
        border-top: 1px solid #eee;
    }

    .featured_layout .contentBox.item .banner {
        width: 30%;
        height: 60px;
        overflow: hidden;
        float: left;
        border-radius: 4px;
        margin-right: 15px;
        cursor: pointer;
    }

    .featured_layout .contentBox.item .banner img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .featured_layout .contentBox.item .title {
        font-size: 12px;
        line-height: 18px;
        overflow: hidden;
        -webkit-box-orient: vertical;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        text-align: justify;
        cursor: pointer;
        line-height: 20px;
    }

    .articleContent {
        width: 55%;
        float: left;
        word-break: break-all;
        text-align: justify;
    }

    .articleContent2 {
        width: 100%;
        float: left;
        word-break: break-all;
        text-align: justify;
    }
</style>