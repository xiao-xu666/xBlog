<template>
    <div class="articleBox">
        <!-- 文章头部 -->
        <div class="articleHead">
            <p class="round"></p>
            <h2>{{articleInfo.articleTitle}}</h2>
            <p class="articleInfo">
                <!-- 作者 -->
                <span>
                    <el-icon>
                        <User />
                    </el-icon>{{articleInfo.userName}}
                </span>
                <!-- 时间 -->
                <span>
                    <el-icon>
                        <Timer />
                    </el-icon>{{articleInfo.publishTime}}
                </span>
                <!-- 分类 -->
                <span>
                    <el-icon>
                        <FolderOpened />
                    </el-icon>
                    {{articleInfo.articleClassifyName}}
                </span>
                <!-- 浏览量 -->
                <span>
                    <el-icon>
                        <View />
                    </el-icon>{{articleInfo.click}}
                </span>
            </p>
        </div>
        <!-- 文章内容 -->
        <div class="articleBody">
            <div v-highlight v-html="articleInfo.articleContent" class="article">

            </div>
        </div>
        <!-- 文章尾部 -->
        <div class="articleFloot">
            <p>
                版权声明：本站所提供的图文等内容部分源于网络，仅供学习参考，如有侵权，联系删除。转载本站内容请注明相关出处。
            </p>
            <p>
                本文链接：<a :href="wPath">{{wPath}}</a>
            </p>
        </div>


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
    export default {
        components: {},
        props: {
            articleInfo: Object
        },
        setup(props) {
            const state = reactive({
                articleInfo: props.articleInfo,
                // 获取当前页面地址，如http://localhost:8080/admin/index
                wPath: window.document.location.href
            })
            return {
                ...toRefs(state)
            }
        }
    }
</script>

<style scoped>
    .articleBox {
        height: auto;
        overflow: hidden;
        background-color: white;
        padding: 25px;
        line-height: 30px;
        color: #474749;
        border-radius: 10px;
        margin-bottom: 20px;
    }

    /** 3个点 */
    .round {
        width: 12px;
        height: 12px;
        background-color: #fc625d;
        border-radius: 16px;
        box-shadow: 20px 0 #fdbc40, 40px 0 #35cd4b;
        margin-bottom: 10px;
    }

    .articleHead {
        border-bottom: #dbdbdb 1px dashed;
        margin-bottom: 20px;
    }

    .articleHead h2 {
        line-height: 35px;
    }

    .articleInfo {
        line-height: 45px;
        color: #888;
        font-size: 13px;
    }

    .articleInfo span {
        margin-right: 20px;
        display: inline-flex;
        align-items: center;
    }

    .articleInfo .el-icon {
        margin-right: 8px;
    }


    /* 尾部样式 */
    .articleFloot {
        margin: 20px 0;
        border-left: 5px solid #2fa7b9;
        padding: 10px 15px;
        background: #f8f8f8;
        font-size: 14px;
        color: #888;
        line-height: 22px;
        box-sizing: border-box;
        overflow: hidden;
        align-items: center;
    }
</style>