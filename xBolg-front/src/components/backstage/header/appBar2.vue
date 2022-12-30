<template>
    <div style="height: 70px;box-shadow: rgb(0 0 0 / 10%) 0px 0px 10px;background: white;">
        <div class="linkBox">
            <el-link :underline="false" style="text-align: left;margin-left: 0px;">
                <!-- <img class="" src="../../../assets/logo.svg" style="width: 120px;"> -->
                <img src="../../../assets/logo.png"  style="width: 40px;">
            </el-link>

            <el-link :underline="false" @click="drawer = true">
                <el-icon>
                    <Operation />
                </el-icon>
                <span>菜单</span>
                <!-- 抽屉 -->
                <el-drawer v-model="drawer" direction="right" :show-close="true" :size="220">
                    <template #title>
                        <img src="../../../assets/logo.svg" style="width: 130px">
                    </template>
                    <template #default>
                        <!-- 菜单 -->
                        <AsideMenu class="hidden-sm-and-up" @onDrawerVisible="drawerVisible"></AsideMenu>
                    </template>
                </el-drawer>
            </el-link>

            <el-link :underline="false" @click="returnHome" >
                <el-icon>
                    <Promotion />
                </el-icon>
                <span>前台</span>
            </el-link>

            <el-popconfirm confirm-button-text="确认" cancel-button-text="取消" :icon="SwitchButton" icon-color="#30bcd7"
                title="确认退出登录嘛?" @confirm="exit">
                <template #reference>
                    <el-link :underline="false">
                        <el-icon>
                            <SwitchButton />
                        </el-icon>
                        <span>退出</span>
                    </el-link>
                </template>
            </el-popconfirm>
        </div>
    </div>
</template>

<script>
    import {
        ref,
        reactive,
        toRefs,
        watch,
        onMounted
    } from 'vue'
    import {
        useRoute
    } from "vue-router"
    import axios from "axios"
    import {
        SwitchButton
    } from '@element-plus/icons-vue'

    // 引入左侧菜单栏自定义组件
    import AsideMenu from '../../../components/backstage/aside/Menu.vue';

    export default {
        components: {
            AsideMenu
        },
        setup() {
            const route = useRoute();
            const state = reactive({
                // 当前登录的用户信息
                userInfo: '',
                drawer: false
            })
            onMounted(() => {
                // 获取用户信息
                const tokenStr = sessionStorage.getItem("userInfo");
                const userInfo = JSON.parse(tokenStr);
                state.userInfo = userInfo.data;
            });

            // 点击头像前往用户设置
            const toPersonSetup = () => {
                window.location.href = "/personSetup"
            }

            // 返回首页
            const returnHome = () => {
                window.location.href = "/"
            }
            // 退出登录
            const exit = () => {
                // 清除用户登录信息
                window.sessionStorage.removeItem("userInfo");
                // 返回首页
                window.location.href = "/";
            }
            // 抽屉菜单可见状态
            const drawerVisible = (visible) => {
                state.drawer = visible
            }
            return {
                SwitchButton,
                ...toRefs(state),
                returnHome,
                exit,
                toPersonSetup,
                drawerVisible
            }
        }
    }
</script>

<style scoped>
    :deep(.el-drawer) {
        height: 100% !important;
        width: 220px;
    }

    :deep(.el-menu) {
        height: auto;
    }

    .linkBox {
        height: 100%;
        padding: 0 20px;
        display: flex;
    }

    .linkBox .el-link {
        margin-left: 20px;
    }

    .linkBox .el-link:hover {
        color: #30bcd7;
    }

    .linkBox .el-link span {
        margin-left: 8px;
    }

    /* 鼠标滑过头像的样式 */
    .demo-rich-conent-custom img {
        width: 55px;
        margin: 0px 15px 0px 0px;
        float: left;
    }

    .demo-rich-conent-custom p {
        margin: 0;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
</style>