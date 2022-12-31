<template>
    <div class="backstage_home">
        <div class="top_bg">
            <h1>脚踏实地，笃志前行</h1>
            <p>xBlog博客后台管理系统·欢迎您，{{userInfo.userName}}</p>
        </div>

        <p style="margin-bottom:15px;color: #144b9f;">
            <div style="width: 12px;height:12px;background-color:#f9a332;border-radius: 50%;float: left;margin-top: 5px;
            margin-right: 8px;">
            </div>本站数据统计
        </p>
        <el-row :gutter="40" class="data_row">
            <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
                <div style="background: linear-gradient(to right, #6D80FE, #23D2FD);">
                    <div class="data_left">
                        <el-icon>
                            <Avatar />
                        </el-icon>
                    </div>
                    <div class="data_right">
                        <h1>{{userCount}}<span>人</span></h1>
                        <p>注册用户</p>
                    </div>
                </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
                <div style="background: linear-gradient(to right, #FF988B, #FF6B88);">
                    <div class="data_left">
                        <el-icon>
                            <Reading />
                        </el-icon>
                    </div>
                    <div class="data_right">
                        <h1>{{articleCount}}<span>篇</span></h1>
                        <p>本站文章</p>
                    </div>
                </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
                <div style="background: linear-gradient(to right, #09B0E8, #29F49A);">
                    <div class="data_left">
                        <el-icon>
                            <ChatDotSquare />
                        </el-icon>
                    </div>
                    <div class="data_right">
                        <h1>{{commentCount}}<span>条</span></h1>
                        <p>本站评论</p>
                    </div>
                </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
                <div style="background: linear-gradient(to right, #717CFE, #FC83EC);">
                    <div class="data_left">
                        <el-icon>
                            <Clock />
                        </el-icon>
                    </div>
                    <div class="data_right">
                        <h1>{{siteDays}}<span>天</span></h1>
                        <p>本站上线天数</p>
                    </div>
                </div>
            </el-col>
        </el-row>

        <p style="margin-bottom:15px;color: #144b9f;">
            <div style="width: 12px;height:12px;background-color:#f9a332;border-radius: 50%;float: left;margin-top: 5px;
            margin-right: 8px;"></div>本站文章的数据统计图
        </p>
        <el-row :gutter="35">
            <el-col :xs="24" :sm="24" :md="24" :lg="10" :xl="10">
                <div class="echarts" id="chart">

                </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="24" :lg="14" :xl="14">
                <div class="latest_article">
                    <h3 style="color: #404040;">
                        最新文章
                        <a href="/addArticle">
                            <el-button
                                style="color:white;background: linear-gradient(to right, #6D80FE, #23D2FD);float: right;">
                                新建文章
                            </el-button>
                        </a>

                    </h3>
                    <el-divider border-style="dashed" />
                    <p class="article_list" v-for="item in articleInfo">
                        <span>{{item.publishTime}}</span>
                        <span>{{item.userName}}</span>
                        <span>发布了</span>
                        <span>
                            <a
                                :href="'/articleDetails/' + item.articleId">{{item.articleTitle}}</a>
                        </span>
                    </p>
                    <a href="/article" class="more">
                        查看更多
                        <el-icon>
                            <DArrowRight />
                        </el-icon>
                    </a>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
    import {
        Iphone,
        Location,
        OfficeBuilding,
        Tickets,
        User,
    } from '@element-plus/icons-vue'

    import {
        ref,
        reactive,
        toRefs,
        onMounted,
        getCurrentInstance,
    } from 'vue'

    import axios from "axios"
import { env } from 'echarts'
    export default {
        setup(props) {
            const {
                proxy
            } = getCurrentInstance()
            onMounted(() => {
                // 获取登录的用户信息
                const tokenStr = sessionStorage.getItem("userInfo");
                const userInfo = JSON.parse(tokenStr);
                state.userInfo = userInfo.data;

                // 查询注册用户总数
                axios.get("/user/showUserCount").then(res => {
                    var value = res.data.data < 10 ? "0" + res.data.data : res.data.data;
                    if (value > 0) {
                        var num = 0;
                        var t = setInterval(function () {
                            num++;
                            state.userCount = num < 10 ? "0" + num : num;
                            if (num == value) {
                                clearInterval(t);
                            }
                        }, 30);
                    } else {
                        state.userCount = value;
                    }
                })

                // 查询通过审核的文章总数量
                axios.get("/article/showArticleCount").then(res => {
                    var value = res.data.data < 10 ? "0" + res.data.data : res.data.data;
                    if (value > 0) {
                        var num = 0;
                        var t = setInterval(function () {
                            num++;
                            state.articleCount = num < 10 ? "0" + num : num;
                            if (num == value) {
                                clearInterval(t);
                            }
                        }, 10);
                    } else {
                        state.articleCount = value;
                    }
                })

                // 查询评论总数
                axios.get("/comment/count").then(res => {
                    var value = res.data.data < 10 ? "0" + res.data.data : res.data.data;
                    if (value > 0) {
                        var num = 0;
                        var t = setInterval(function () {
                            num++;
                            state.commentCount = num < 10 ? "0" + num : num;
                            if (num == value) {
                                clearInterval(t);
                            }
                        }, 10);
                    } else {
                        state.commentCount = value;
                    }
                })

                // 本站上线天数
                calculateDays()

                // 饼图 
                pie()

                // 获取文章全部信息
                getArticleInfo()
            });

            // 查询文章信息
            const getArticleInfo = () => {
                axios.get("/article/showAllArticleInfo").then(res => {
                    res.data.data.forEach(element => {
                        // 将时间戳转为时间格式
                        var date = function (time) {
                            var date = new Date(+time + 8 * 3600 * 1000);
                            return date.toJSON().substr(0, 19).replace('T', ' ').replace(/-/g,
                                '.');
                        }
                        var time1 = date(new Date(element.publishTime));
                        element.publishTime = time1;

                        // 添加  if判断 仅展示6条数据
                        if (state.articleInfo.length < 6) {
                            state.articleInfo.push(element)
                        }
                    })
                })
            }

            const calculateDays = () => {
                var day = Math.floor(new Date().getTime() / 1000) - (new Date('2022-11-09 00:00:00')
                        .getTime() /
                        1000),
                    day2 = Math.floor(day / (24 * 3600));


                var value = day2 < 10 ? "0" + day2 : day2;
                if (value > 0) {
                    var num = 0;
                    var t = setInterval(function () {
                        num++;
                        state.siteDays = num < 10 ? "0" + num : num;
                        if (num == value) {
                            clearInterval(t);
                        }
                    }, 30);
                } else {
                    state.siteDays = value;
                }
            }

            const state = reactive({
                // 当前登录的用户信息
                userInfo: [],
                // 注册用户总数
                userCount: "",
                // 通过审核的文章总数量
                articleCount: "",
                // 评论总数
                commentCount: "",
                // 本站上线天数
                siteDays: "",
                // 饼图
                myChart: "",
                // 文章信息
                articleInfo: []
            })


            // 饼图 
            const pie = () => {
                state.myChart = proxy.$echarts.init(document.getElementById("chart"), 'walden');
                var option = {
                    title: {
                        text: '本站文章分类',
                        subtext: '文章数量',
                        left: 'left'
                    },
                    tooltip: {
                        trigger: 'item'
                    },
                    legend: {
                        top: '5%',
                        top: 'bottom'
                    },
                    series: [{
                        name: 'Access From',
                        type: 'pie',
                        radius: ['40%', '70%'],
                        avoidLabelOverlap: false,
                        itemStyle: {
                            borderRadius: 10,
                            borderColor: '#fff',
                            borderWidth: 2
                        },
                        label: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: '40',
                                fontWeight: 'bold'
                            }
                        },
                        labelLine: {
                            show: false
                        },
                        data: []
                    }]
                }

                // 查询所有文章分类
                axios.get("/showAllClassifyInfo").then(res => {
                    res.data.data.forEach(element => {
                        option.series[0].data.push({
                            value: element.articleNumber,
                            name: element.classifyName,
                            itemStyle: {
                                color: {
                                    type: 'linear', // 线性渐变
                                    x: 0,
                                    y: 0,
                                    x2: 0,
                                    y2: 1,
                                    colorStops: [{
                                        offset: 0,
                                        color: element
                                            .color1 // 0%处的颜色
                                    }, {
                                        offset: 1,
                                        color: element
                                            .color2 // 100%处的颜色
                                    }]
                                }
                            },
                        })
                    });

                    // 指定图表的配置项和数据
                    state.myChart.setOption(option);
                })


            }

            // 根据浏览器的大小来改变图表大小
            window.onresize = () => {
                state.myChart.resize();
            }


            return {
                Iphone,
                Location,
                OfficeBuilding,
                Tickets,
                User,
                ...toRefs(state),
            }
        }
    }
</script>

<style>
    .backstage_home {
        width: 100%;
    }

    .top_bg {
        width: 100%;
        height: 200px;
        background-image: url(../../../../assets/banner/banner07.png);
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        margin: 0 auto;
        color: white;
        line-height: 60px;
        text-align: center;
        margin-bottom: 20px;
    }

    .top_bg h1 {
        font-size: 60px;
        text-shadow: 3px 3px 0px #515151;
        padding-top: 50px;
    }

    .top_bg p {
        font-weight: lighter;
        font-size: 18px;
    }

    @media screen and (max-width: 850px) {
        .top_bg h1 {
            font-size: 50px;
        }
    }

    @media screen and (max-width: 550px) {
        .top_bg h1 {
            font-size: 40px;
        }
    }

    @media screen and (max-width: 450px) {
        .top_bg {
            line-height: 50px;
        }

        .top_bg h1 {
            font-size: 30px;
        }

        .top_bg p {
            font-size: 10px;
        }
    }



    /* .abstract>h4{
        color: #416aff;
        margin-bottom: 20px;
    }

    .round {
        width: 12px;
        height: 12px;
        background-color: #fc625d;
        border-radius: 16px;
        box-shadow: 20px 0 #fdbc40, 40px 0 #35cd4b;
        margin-bottom: 10px;
    }*/

    .data_row .el-col {
        height: 100px;
        margin-bottom: 20px;
        overflow: hidden;
    }

    .data_row .el-col>div {
        width: 100%;
        height: 100%;
        border-radius: 10px;
        color: white;
    }

    .data_left {
        float: left;
        width: 40%;
        height: 100%;
        display: inline-flex;
        justify-content: center;
        align-items: center;
        text-align: center;
    }

    .data_left .el-icon {
        font-size: 60px;
    }

    .data_right {
        width: 60%;
        float: right;
        margin-top: 10px;
    }

    .data_right h1 {
        font-size: 40px;
    }

    .data_right h1 span {
        font-size: 15px;
        margin-left: 10px;
    }

    .data_right p {
        font-size: 14px;
        font-weight: 600;
        margin-left: 3px;
    }

    .echarts {
        width: 100%;
        height: 300px;
        float: left;
        background-color: white;
        border-radius: 10px;
        box-sizing: border-box;
        padding: 20px;
        margin-bottom: 20px;
    }

    .latest_article {
        width: 100%;
        height: 300px;
        float: left;
        background-color: white;
        border-radius: 10px;
        box-sizing: border-box;
        padding: 20px;
        margin-bottom: 20px;
    }

    .el-divider--horizontal {
        margin: 20px 0;
    }

    .article_list {
        color: rgb(153 153 153);
        line-height: 30px;
        -webkit-box-orient: vertical;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        text-align: justify;
        overflow: hidden;
    }

    .article_list span {
        margin-right: 10px;
    }

    .article_list a {
        color: #ff958b;
    }

    .article_list a:hover {
        color: rgb(255, 107, 136);
    }

    .more {
        display: inline-flex;
        justify-content: center;
        align-items: center;
        float: right;
        color: #bababa;
        font-size: 13px;
        cursor: pointer;
    }
</style>