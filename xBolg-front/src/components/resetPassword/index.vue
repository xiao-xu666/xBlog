<template>
    <div class="reset_password_box">
        <a class="reset_password_logo"></a>
        <div class="midGroup">
            <div class="reset_password_group">
                <div v-if="componentsInfo.isShow">
                    <h2>通过电子邮箱找回密码</h2>
                    <!-- 1、确认账号 -->
                    <ConfirmAccount v-if="componentsInfo.confirmAccountIsShow" @setUserInfo="getUserInfo">
                    </ConfirmAccount>
                    <!-- 2、安全验证 -->
                    <SafetyVerification v-if="componentsInfo.safetyVerificationIsShow" @setPass="getPass"
                        :setUserEmail="userInfo.userEmail" :setCode="code">
                    </SafetyVerification>
                    <!-- 3、重置密码 -->
                    <ResetPassword v-if="componentsInfo.resetPasswordIsShow" @updateSucceed="succeed"
                        :setUserName="userInfo.userName" :setUserEmail="userInfo.userEmail"></ResetPassword>

                </div>
                <!-- 4、成功 -->
                <div v-if="!componentsInfo.isShow" style="padding: 100px 0px;">
                    <p style="font-size: 30px;font-weight:600;color:#2fa7b9;text-align: center;line-height: 60px;">
                        密码重置成功</p>
                    <p style="text-align: center;color: #999999;font-size: 14px;">{{count}}秒后返回登录界面</p>
                </div>

                <p style="font-size: 12px; color:#999;">
                    还没有账号，<span @click="toPath('/register')" style="color:#2fa7b9;cursor:pointer;">立即注册</span>
                    <p class="rests_path">
                        <span @click="toPath('/login')">返回登录</span> | <span @click="toPath('/')">前往首页</span>
                    </p>
                </p>
                <footer style="text-align:center;">
                    <p>相信有一天, 理想主义终将所向披靡.</p>
                </footer>
            </div>
        </div>
        <div class="footer_copyright">
            <p>
                · Designed by xBlog. All Rights Reserved. Copyright © 2022 xBlog ·
            </p>
        </div>
    </div>
</template>
<script>
    // 1、确认账号
    import ConfirmAccount from './confirmAccount/index.vue'
    // 2、安全验证
    import SafetyVerification from './safetyVerification/index.vue'
    // 3、重置密码
    import ResetPassword from './resetPassword/index.vue'
    import {
        reactive,
        ref,
        toRefs,
        unref
    } from 'vue'

    import {
        User,
        Lock,
        Right
    } from '@element-plus/icons-vue'

    import {
        ElLoading,
        ElMessage,
        ElNotification,
        ElMessageBox
    } from 'element-plus'

    import axios from "axios";

    export default {
        components: {
            ConfirmAccount,
            SafetyVerification,
            ResetPassword
        },
        setup() {
            // 前往某个界面
            const toPath = (path) => {
                window.location.href = path
            }
            const state = reactive({
                componentsInfo: {
                    isShow: true,
                    confirmAccountIsShow: true,
                    safetyVerificationIsShow: false,
                    resetPasswordIsShow: false,
                },
                userInfo: {},
                code: "",
                count: '',
                timer: null,
            })

            // 第一步：确认信息，子组件传的值（邮箱）
            const getUserInfo = (info) => {
                state.userInfo = info.userInfo;
                state.code = info.code
                state.componentsInfo.confirmAccountIsShow = false;
                state.componentsInfo.safetyVerificationIsShow = true;
            };

            // 第二步：将生成的验证码传给第二个子组件

            // 第三步：用户验证输入正确之后
            const getPass = (info) => {
                state.componentsInfo.safetyVerificationIsShow = false;
                state.componentsInfo.resetPasswordIsShow = true;
            };

            let TIME_COUNT = 3
            // 第四步：用户修改密码成功
            const succeed = (info) => {
                state.componentsInfo.resetPasswordIsShow = false;
                state.componentsInfo.isShow = false;
                // 3秒后跳转到登录界面
                if (!state.timer) {
                    state.count = TIME_COUNT
                    state.timer = setInterval(() => {
                        if (state.count > 1 && state.count <= TIME_COUNT) { //限制倒计时区间
                            state.count--
                        } else {
                            clearInterval(state.timer) //删除定时器
                            state.timer = null
                            // 跳转
                            window.location.href = "/login"
                        }
                    }, 1000)
                }
            };

            return {
                ...toRefs(state),
                getUserInfo,
                getPass,
                succeed,
                toPath
            }
        }
    }
</script>

<style scoped>
    .reset_password_box {
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        width: 100%;
        height: 100%;
        background: #fff url(../../assets/login/login-bg.png) no-repeat;
        background-size: cover;
    }

    .reset_password_logo {
        display: block;
        position: absolute;
        left: 8vw;
        top: 20px;
        width: 160px;
        height: 50px;
        background: url(../../assets/logo.svg) no-repeat center;
        background-size: 160px;
    }

    .reset_password_box .midGroup {
        width: 700px;
        margin: 20vh auto;
        position: relative;
    }

    .reset_password_box h2 {
        border-bottom: 1px dashed #ddd;
        padding: 10px 0;
        font-size: 25px;
        margin-bottom: 20px;
        text-align: center;
        color: #2fa7b9;
        padding-bottom: 10px;
    }

    .reset_password_box .midGroup .reset_password_group {
        padding: 20px 40px;
        background: white;
        overflow: hidden;
        box-shadow: 0 1px 10px 0 rgb(7 17 27 / 10%);
        padding-top: 20px;
        border-radius: 10px;
    }

    .reset_password_box .midGroup .reset_password_group footer {
        margin-top: 10px;
        padding-top: 10px;
        border-top: 1px dotted #ddd;
        color: #999;
        font-size: 12px;
        overflow: hidden;
    }

    .footer_copyright {
        width: 100%;
        text-align: center;
        line-height: 24px;
        color: #999;
        font-size: 12px;
        position: fixed;
        left: 0;
        bottom: 0;
        overflow: hidden;
        padding: 10px;
        border-top: 1px solid #eee;
        background: white;
    }


    :global(.el-input-group__append) {
        padding: 0px;
        background: #f6f6f6;
    }

    .reset_password_btn :deep(.el-form-item__error) {
        text-align: center;
        width: 100%;
    }

    .rests_path {
        float: right;
        color: #999;
    }

    .rests_path span {
        cursor: pointer;
    }

    .rests_path span:hover {
        color: #2fa7b9;
    }
</style>