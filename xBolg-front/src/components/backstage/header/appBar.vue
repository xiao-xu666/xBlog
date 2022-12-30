<template>
    <div style="height: 70px;box-shadow: rgb(0 0 0 / 10%) 0px 0px 10px;background: white;">
        <div class="linkBox">
            <el-popover :width="300"
                popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding: 20px;">
                <template #reference>
                    <el-link :underline="false" @click="toPersonSetup">
                        <!-- 用户没设置头像的情况下使用系统默认的 -->
                        <img v-if="userInfo.userIcon ===null || userInfo.userIcon === ''" src="../../../assets/default_avatar.png"
                            style="width: 40px;border-radius: 50px;">
                        <img v-if="userInfo.userIcon !==null &&  userInfo.userIcon !== ''" :src="userInfo.userIcon"
                            style="width: 40px;border-radius: 50px;">
                        <span>{{userInfo.userName}}</span>
                    </el-link>
                </template>
                <template #default>
                    <div class="demo-rich-conent" style="display: flex; gap: 16px; flex-direction: column">
                        <div class="demo-rich-conent-custom">
                            <!-- 用户没设置头像的情况下使用系统默认的 -->
                            <img v-if="userInfo.userIcon === null || userInfo.userIcon === ''" src="../../../assets/default_avatar.png">
                            <img v-if="userInfo.userIcon !==null &&  userInfo.userIcon !== ''" :src="userInfo.userIcon">
                            <p class="demo-rich-content__name">
                                用户名：{{userInfo.userName}}
                            </p>
                            <p class="demo-rich-content__mention">
                                Email：{{userInfo.userEmail}}
                            </p>
                            <p class="demo-rich-content__mention">
                                个性签名：{{userInfo.userSignature}}
                            </p>
                        </div>
                        <p class="demo-rich-content__desc" style="margin: 0">
                            <div
                                style="float:left;width: 75px;padding:10px;border-right: 1px solid rgb(205, 205, 205);text-align: center;">
                                <p>文章</p>
                                <p style="font-size: 25px;font-weight:600;">
                                    {{articleCount}}
                                    <span style="font-size: 10px;font-weight:100;margin-left:5px;">篇</span>
                                </p>
                            </div>
                            <div
                                style="float:left;width: 80px;padding:10px;border-right: 1px solid rgb(205, 205, 205);text-align: center;">
                                <p>注册</p>
                                <p style="font-size: 25px;font-weight:600;">
                                    {{handlerDateDurationCurrent(userInfo.userRegdate)}}
                                    <span style="font-size: 10px;font-weight:100;margin-left:5px;">天</span>
                                </p>
                            </div>
                            <div style="float:left;width: 75px;padding:10px;text-align: center;">
                                <p>类型</p>
                                <p style="font-size: 15px;font-weight:600;line-height: 40px;">
                                    <span v-if="userInfo.userType==0">管理员</span>
                                    <span v-if="userInfo.userType==1">普通用户</span>
                                </p>
                            </div>
                        </p>
                    </div>
                </template>
            </el-popover>

            <el-link :underline="false" @click="returnHome">
                <el-icon>
                    <Promotion />
                </el-icon>
                <span>返回首页</span>
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

    export default {
        setup() {
            const route = useRoute();
            const state = reactive({
                // 当前登录的用户信息
                userInfo: '',
                // 当前登录用户编写文章总数量
                articleCount: "",
                // 服务器路径
                url: process.env.VUE_APP_URL,
            })
            onMounted(() => {
                // 获取用户信息
                const tokenStr = sessionStorage.getItem("userInfo");
                const userInfo = JSON.parse(tokenStr);
                state.userInfo = userInfo.data;

                // 查询当前登录用户编写文章总数量
                axios.get("/article/showArticleCountByUserId", {
                    params: {
                        userId: state.userInfo.userId
                    }
                }).then(res => {
                    state.articleCount = res.data.data < 10 ? "0" + res.data.data : res.data.data
                })
            });


            const handlerDateDurationCurrent = (time) => {
                let d1 = new Date(time)
                let d2 = new Date()

                let cha = Math.abs(d2.getTime() - d1.getTime())
                let days = parseInt(cha / (24 * 60 * 60 * 1000))
                let hours = parseInt(cha % (24 * 60 * 60 * 1000) / (60 * 60 * 1000))
                let mins = parseInt(cha % (60 * 60 * 1000) / (60 * 1000))
                return days
            }

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
            return {
                SwitchButton,
                ...toRefs(state),
                returnHome,
                exit,
                toPersonSetup,
                handlerDateDurationCurrent
            }
        }
    }
</script>

<style scoped>
    @media screen and (max-width: 768px) {
        .linkBox {
            margin: 0 auto !important;
            float: none !important;
        }
    }

    .linkBox {
        height: 100%;
        display: flex;
        align-items: center;
        text-align: center;
        float: right;
    }

    .linkBox .el-link {
        margin-right: 25px;
        color: #8c8c8c;
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
        border-radius: 50px;
        float: left;
    }

    .demo-rich-conent-custom p {
        margin: 0;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
</style>