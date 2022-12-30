<template>
    <div id="backstage">
        <!-- 背景图片 -->
        <div id="ebg">

        </div>
        <div class="common-layout">
            <el-container>
                <!-- 左侧菜单栏 -->
                <el-aside width="200px" class="hidden-xs-only">
                    <AsideMenu></AsideMenu>
                </el-aside>
                <el-container>
                    <!-- 顶部导航栏 -->
                    <el-header>
                        <HeaderAppBar class="hidden-xs-only"></HeaderAppBar>
                        <HeaderAppBarTwo class="hidden-sm-and-up"></HeaderAppBarTwo>
                    </el-header>
                    <!-- 主要内容 -->
                    <el-main>
                        <router-view></router-view>
                    </el-main>
                </el-container>
            </el-container>
        </div>
    </div>
</template>
<script>
    // 引入顶部导航栏自定义组件  大于768px时的布局
    import HeaderAppBar from '../../components/backstage/header/appBar.vue';
    // 引入顶部导航栏自定义组件  小于768px时的布局
    import HeaderAppBarTwo from '../../components/backstage/header/appBar2.vue';
    // 引入左侧菜单栏自定义组件
    import AsideMenu from '../../components/backstage/aside/Menu.vue';

    import {
        ElNotification,
    } from 'element-plus'


    export default {
        components: {
            AsideMenu,
            HeaderAppBar,
            HeaderAppBarTwo
        },
        setup() {
            // 进入后台提示
            // ElNotification({
            //     title: '欢迎回来',
            //     message: '您已进入xBlog博客后台!',
            //     type: 'success',
            //     duration: 2000,
            // })
            return {
                // ElNotification
            }
        }
    }
</script>

<style scoped>
    /* 顶部导航栏 */
    .el-header {
        --el-header-padding: 0px;
        --el-header-height: auto;
        height: 80px;
        background: #F3F3F3;
    }

    #ebg {
        width: 100%;
        height: 100%;
        position: fixed;
        top: 0;
        left: 0;
        padding: 10px;
        background-image: url(../../assets/backstage-bg.jpg);
        background-size: cover;
        background-position: center center;
        background-repeat: no-repeat;
    }

    .el-main {
        background: #F3F3F3;
    }

    .el-container {
        height: 100%;
    }

    .common-layout {
        width: 99vw;
        height: 98vh;
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        margin: auto;
        background-color: white;
        border-radius: 15px;
        overflow: hidden;
    }
</style>