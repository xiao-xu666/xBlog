<template>
    <div id="foreground">
        <div class="common-layout">
            <el-container>
                <!-- 页眉 -->
                <el-header>
                    <MenuTop></MenuTop>
                </el-header>
                <!-- 主要的 -->
                <el-main>
                    <router-view></router-view>
                </el-main>
                <!-- 页尾\页脚 -->
                <el-footer class="front_footer">
                    <FooterCopyright></FooterCopyright>
                </el-footer>
            </el-container>
        </div>

        <!-- Backtop 回到顶部 -->
        <BackTop></BackTop>

        <!-- 打开菜单栏固钉 -->
        <MenuRight></MenuRight>
    </div>
</template>
<script>
    // 引入顶部菜单栏自定义组件
    import MenuTop from './header/Menu.vue';
    // 引入页尾\页脚版权信息自定义组件
    import FooterCopyright from './footer/Copyright.vue';
    // 引入回到顶部固钉自定义组件
    import BackTop from './affix/BackTop.vue';
    // 引入打开菜单固钉自定义组件
    import MenuRight from './affix/Menu.vue';

    import {
        useRoute
    } from "vue-router"
    export default {
        components: {
            MenuTop,
            FooterCopyright,
            BackTop,
            MenuRight
        },
        setup() {
            const route = useRoute()
        }
    }
</script>

<style scoped>
    /* 首页底部版权样式 */
    .front_footer {
        border-top: 1px solid #ebebeb;
        background: #fff;
    }
</style>