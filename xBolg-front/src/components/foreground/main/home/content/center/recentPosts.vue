<template>
    <div class="stick_articleBox" v-loading="loading1" element-loading-text="玩命加载中...">
        <div class="contentBox">
            <div class="tagBox">
                <p>
                    <!-- 鼠标点击跳转到分类 -->
                    <span>
                        <el-icon class="tagIcon">
                            <Place />
                        </el-icon>
                        置顶
                    </span>
                </p>
            </div>
            <a :href="'/articleDetails/'+stickArticleInfo.articleId">
                <span :title="stickArticleInfo.articleTitle">
                    <div class="content">
                        <h4 class="title">{{stickArticleInfo.articleTitle}}</h4>
                        <img v-if="stickArticleInfo.articleImgLitimg" class="img"
                            :src="stickArticleInfo.articleImgLitimg" alt="stickArticleInfo.articleTitle">
                    </div>
                </span>
                <p class="textRight">{{stickArticleInfo.articleClassifyName}} · {{stickArticleInfo.publishTime}}</p>
            </a>
        </div>
    </div>

    <div class="articleBox" v-for="item in articleInfo" v-loading="loading2" element-loading-text="玩命加载中...">
        <div class="tagBox">
            <p>
                <!-- 鼠标点击跳转到分类 -->
                <span>
                    <el-icon class="tagIcon">
                        <FolderOpened />
                    </el-icon>
                    {{item.articleClassifyName}}
                </span>
            </p>
        </div>
        <a :href="'/articleDetails/'+item.articleId">
            <h4 class="title"><span>{{item.articleTitle}}</span></h4>
            <div class="article">
                <img v-if="item.articleImgLitimg" class="focus" :src="item.articleImgLitimg" :alt="item.articleTitle">
                <!-- 内容样式根据有没有图片改变 -->
                <div class="textBox" :class="[item.articleImgLitimg ? '':'textBox2']">
                    <p style="margin-bottom: 25px;">{{item.articleDase}}</p>
                    <!--<p>xBlog · 11月前</p>-->
                    <p class="hidden-sm-and-up">{{item.userName}} · {{item.publishTime}}</p>
                    <p class="articleMessage hidden-xs-only"><span>
                            <el-icon>
                                <User />
                            </el-icon>{{item.userName}}
                        </span>
                        <span>
                            <el-icon>
                                <Timer />
                            </el-icon>{{item.publishTime}}
                        </span>
                        <span>
                            <el-icon>
                                <View />
                            </el-icon>{{item.click}}
                        </span></p>
                </div>
            </div>
        </a>
    </div>
</template>

<script>
    import {
        ref,
        toRefs,
        reactive,
        onMounted,
        watch
    } from 'vue'
    import axios from "axios";
    import {
        useRouter
    } from 'vue-router'
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

    /**
     * @desc 加载获取数据
     */
    function loadData(state) {
        state.loading1 = true;
        state.loading2 = true;
        // 查询系统设置中设置好的展示文章的文章id && 置顶文章id
        axios.get("/showAllSystemSetup").then(res => {
            // 展示文章的文章id
            state.allArticleArr = JSON.parse(res.data.data[0].allArticle);
            // 置顶文章id
            state.stickArticle = parseInt(res.data.data[0].stickArticle)
        })

        // 查询全部文章
        axios.get("/article/showAllArticleInfo").then(res => {
            // 筛选出系统设置总设置好的展示文章
            state.allArticleArr.forEach(val => {
                res.data.data.forEach(element => {
                    // 筛选出系统设置总设置好的展示文章
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
                })
            });
            state.loading2 = false;

            // 查询全部文章   置顶文章
            res.data.data.forEach(element => {
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

                // 筛选出置顶文章
                if (element.articleId == state.stickArticle) {
                    state.stickArticleInfo = element
                }
            })
            state.loading1 = false;
        })
    }
    export default {
        setup(props) {
            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });
            const router = useRouter();

            const state = reactive({
                // 置顶文章id
                stickArticle: null,
                // 置顶文章信息
                stickArticleInfo: {},
                // 需要展示文章的文章id
                allArticleArr: [],
                // 展示文章信息
                articleInfo: [],
                loading1: false,
                loading2: false
            })
            return {
                ...toRefs(state)
            }
        }
    }
</script>

<style scoped>
    /* 置顶文章样式 */
    .stick_articleBox {
        background: #fff;
        border-radius: 6px;
        padding: 20px;
        margin-bottom: 20px;
    }

    .stick_articleBox .contentBox {
        position: relative;
        font-size: 12px;
        color: #999;
        cursor: pointer;
    }

    .stick_articleBox .contentBox .content {
        position: relative;
        height: 120px;
        border-radius: 6px;
        padding: 0 25px;
        margin: 10px 0;
        overflow: hidden;
    }

    .stick_articleBox .contentBox .content .title {
        position: relative;
        z-index: 5;
        height: 120px;
        display: flex;
        align-items: center;
        line-height: 24px;
        color: #fff;
        font-size: 15px;
    }

    .stick_articleBox .contentBox .textRight {
        text-align: right;
        color: #999;
    }

    .stick_articleBox .contentBox .content:before {
        content: '';
        background: -webkit-gradient(linear, right top, left top, from(black), to(black));
        background: linear-gradient(to left, black, black);
        background-image: linear-gradient(-225deg, black 0%, black 78%);
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        opacity: 0.5;
        z-index: 1;
        border-radius: 6px;
        color: black;
    }

    .stick_articleBox .contentBox .content .img {
        width: 100%;
        height: 100%;
        border-radius: 6px;
        object-fit: cover;
        position: absolute;
        left: 0;
        top: 0;
    }

    /* 展示文章样式 */
    .articleBox {
        padding: 20px;
        background-color: white;
        border-radius: 8px;
        margin-bottom: 20px;
        border: 1px solid white;
    }

    .articleBox:hover {
        border: 1px solid #2fa7b9;
    }

    .tagBox span {
        background: #f6f6f6;
        padding: 3px 6px;
        border-radius: 20px;
        color: #2fa6b8;
        display: inline-flex;
        align-items: center;
        font-size: 13px;
        cursor: pointer;
    }

    .tagIcon {
        margin-right: 5px;
        vertical-align: middle;
        background: #2fa7b9;
        color: white;
        border-radius: 15px;
        padding: 5px;
        font-size: 20px;
    }

    .articleBox .title {
        font-size: 15px;
        line-height: 45px;
        color: #454545;
        overflow: hidden;
        -webkit-box-orient: vertical;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        text-align: justify;
    }

    .title span {
        cursor: pointer;
    }

    .title span:hover {
        color: #2fa7b9;
    }

    .articleMessage span {
        font-size: 13px;
        color: #999;
        line-height: 20px;
        margin-right: 15px;
        display: inline-flex;
        align-items: center;
    }

    .articleMessage .el-icon {
        font-size: 15px;
        margin-right: 8px;
    }

    .article {
        display: flex;
        justify-content: space-between;
        font-size: 12px;
        color: #999;
    }

    .article img {
        width: 140px;
        height: 90px;
        border-radius: 4px;
        object-fit: cover;
        overflow: hidden;
    }

    .textBox {
        width: 420px;
        height: 100px;
        line-height: 22px;
        font-size: 13px;
        color: #999;
        margin-left: 10px;
    }

    .textBox2 {
        width: auto;
        margin-left: 0px;
    }

    .textBox p {
        font-size: 12px;
        color: #999;
        margin-bottom: 15px;
        -webkit-box-orient: vertical;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        text-align: justify;
        margin-bottom: 25px;
        cursor: pointer;
        overflow: hidden;
    }

    @media screen and (max-width: 768px) {
        .textBox {
            width: 500px;
        }
    }
</style>