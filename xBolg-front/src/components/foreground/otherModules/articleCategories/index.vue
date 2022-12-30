<template>
    <div class="articleCategoriesBox" v-loading="loading" element-loading-text="玩命加载中...">
        <div class="articleCategories_title">
            <h3 class="title"><b>文章分类</b></h3>
        </div>
        <div class="articleCategories_content">
            <ul>
                <li v-for="item in classifyInfo">
                    <el-icon>
                        <ArrowRight />
                    </el-icon>
                    <a :title="item.classifyName" :href="'/classify/'+item.classifyId">{{item.classifyName}}</a>
                </li>
            </ul>
        </div>
    </div>
</template>
<script>
    import {
        onMounted,
        reactive,
        toRefs
    } from "vue";

    import axios from "axios"

    function loadData(state) {
        state.loading = true;

        axios.get("/showAllClassifyInfo").then(res => {
            state.classifyInfo = res.data.data
            state.loading = false;
        })
    }

    export default {
        setup(props) {
            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });
            const state = reactive({
                classifyInfo:[],
                loading: false,
            })
            return {
                ...toRefs(state)
            }
        }
    }
</script>

<style scoped>
    .articleCategoriesBox {
        background: white;
        height: auto;
        border-radius: 10px;
        margin-bottom: 20px;
        padding: 20px;
    }

    .articleCategories_title {
        margin-bottom: 15px;
        margin-top: -10px;
        display: flex;
        justify-content: space-between;
        border-bottom: 1px solid rgb(238, 238, 238);
        overflow: hidden;
    }

    .articleCategories_title .title {
        line-height: 40px;
        color: #474749;
    }

    .articleCategories_title .title b {
        display: inline-block;
        border-bottom: 4px solid #2fa7b9;
    }

    .articleCategories_content ul {
        overflow: hidden;
        zoom: 1;
    }

    .articleCategories_content li {
        height: 36px;
        line-height: 36px;
        border-bottom: 1px dotted #e4e8eb;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        width: 50%;
        float: left;
        display: inline-flex;
        align-items: center;
    }

    .articleCategories_content li .el-icon {
        margin-right: 8px;
        font-size: 12px;
        color: grey;
    }
</style>