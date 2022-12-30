<template>
    <el-row :gutter="20">
        <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
            <!-- 左侧间隙 -->
        </el-col>
        <el-col :xs="24" :sm="22" :md="22" :lg="18" :xl="16">
            <el-row :gutter="20">
                <el-col :xs="24" :sm="24" :md="18" :lg="18" :xl="18">
                    <el-row :gutter="20">
                        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
                            <!-- 走马灯/轮播图 -->
                            <Carousel></Carousel>
                        </el-col>
                    </el-row>
                    <el-row :gutter="20">
                        <el-col class="hidden-xs-only" :sm="7" :md="7" :lg="8" :xl="8">
                            <!-- Featured精选 -->
                            <Featured></Featured>
                            <!-- advertising 广告  传递参数：系统设置中设置好的广告图片、链接 -->
                            <Advertising v-if="advertising1" :advertisingImg="advertising1"
                                :advertisingLink="advertisingLink1">
                            </Advertising>
                            <!-- Technology 技术文章 -->
                            <Technology></Technology>
                        </el-col>
                        <el-col :xs="24" :sm="17" :md="17" :lg="16" :xl="16">
                            <!-- 最新文章 -->
                            <RecentPosts></RecentPosts>
                        </el-col>
                    </el-row>

                </el-col>
                <el-col class="hidden-sm-and-down" :md="6" :lg="6" :xl="6">
                    <!-- 搜索功能 -->
                    <SearchCard></SearchCard>
                    <!-- advertising 广告  传递参数：系统设置中设置好的广告图片、链接 -->
                    <Advertising v-if="advertising2" :advertisingImg="advertising2" :advertisingLink="advertisingLink2">
                    </Advertising>
                    <!-- 文章分类 -->
                    <ArticleCategories></ArticleCategories>
                    <!-- Resource 资源文章 -->
                    <Resource></Resource>
                    <!-- advertising 广告  传递参数：系统设置中设置好的广告图片、链接 -->
                    <Advertising v-if="advertising3" :advertisingImg="advertising3" :advertisingLink="advertisingLink3">
                    </Advertising>
                    <!-- 站点信息 -->
                    <OtherModules></OtherModules>
                </el-col>
            </el-row>
        </el-col>
        <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
            <!-- 右侧间隙 -->
        </el-col>
    </el-row>
</template>
<script>
    import axios from "axios";
    import {
        onMounted,
        reactive,
        toRefs
    } from "vue";


    // 引入走马灯/轮播图
    import Carousel from './content/banner/Carousel.vue';
    // 引入搜索功能
    import SearchCard from '../../otherModules/searchCard/index.vue';
    // 引入Featured精选
    import Featured from '../../otherModules/featured/index.vue';
    // 引入advertising 广告
    import Advertising from '../../otherModules/advertising/index.vue';
    // 引入technology 技术
    import Technology from '../../otherModules/technology/index.vue'
    // 引入RecentPosts展示文章
    import RecentPosts from './content/center/recentPosts.vue'
    // 引入ArticleCategories文章分类
    import ArticleCategories from '../../otherModules/articleCategories/index.vue';
    // 引入Featured资源
    import Resource from '../../otherModules/resource/index.vue';
    // 引入OtherModules站点信息
    import OtherModules from '../../otherModules/websiteInfo/index.vue'

    function loadData(state) {
        // 查询系统设置中设置好的精选文章的文章id
        axios.get("/showAllSystemSetup").then(res => {
            // 图片 根url
            const url = process.env.VUE_APP_URL;
            // 缩略图 判断是点击上传的还是，网络图片    本地图片需要加上服务器路径
            if (res.data.data[0].advertising1 != "" && !res.data.data[0].advertising1.includes('http')) {
                res.data.data[0].advertising1 = url + res.data.data[0].advertising1
            }
            if (res.data.data[0].advertising2 != "" && !res.data.data[0].advertising2.includes('http')) {
                res.data.data[0].advertising2 = url + res.data.data[0].advertising2
            }
            if (res.data.data[0].advertising3 != "" && !res.data.data[0].advertising3.includes('http')) {
                res.data.data[0].advertising3 = url + res.data.data[0].advertising3
            }

            // 广告内容
            state.advertising1 = res.data.data[0].advertising1;
            state.advertisingLink1 = res.data.data[0].advertisingLink1;
            state.advertising2 = res.data.data[0].advertising2;
            state.advertisingLink2 = res.data.data[0].advertisingLink2;
            state.advertising3 = res.data.data[0].advertising3;
            state.advertisingLink3 = res.data.data[0].advertisingLink3;
        })
    }

    export default {
        components: {
            Carousel,
            SearchCard,
            Featured,
            Advertising,
            RecentPosts,
            Technology,
            Resource,
            OtherModules,
            ArticleCategories
        },
        setup(props) {
            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });

            const state = reactive({
                advertising1: '',
                advertisingLink1: '',
                advertising2: '',
                advertisingLink2: '',
                advertising3: '',
                advertisingLink3: '',
            })

            return {
                ...toRefs(state)
            }
        }
    }
</script>

<style scoped>
    .el-col {
        overflow: hidden;
    }

    .el-col>div {
        border-radius: 10px;
    }

    .el-row {
        margin-bottom: 20px;
    }
</style>