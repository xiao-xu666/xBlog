<template>
    <div class="loginBox">
        <a class="login_logo"></a>
        <div class="midgroup">
            <div class="loginwrap loginwarrp">
                <div class="logingroup login_form">
                    <h2 style="text-align: center; color: #2fa7b9;">登 录</h2>
                    <el-form ref="ruleFormRef" :model="ruleForm" status-icon :rules="rules" label-width="120px"
                        style="padding-top:20px;" label-position="top" size="large" class="demo-ruleForm">
                        <!-- @submit.native.prevent-->
                        <el-form-item prop="loginName">
                            <el-input :prefix-icon="User" v-model="ruleForm.loginName" placeholder="请输入用户名或者电子邮箱" />
                        </el-form-item>
                        <el-form-item prop="loginPass">
                            <el-input :prefix-icon="Lock" v-model="ruleForm.loginPass" show-password autocomplete="off"
                                placeholder="请输入密码" />
                        </el-form-item>
                        <!-- 验证码 -->
                        <el-form-item prop="inputVerificationCode">
                            <el-input :prefix-icon="Right" v-model="ruleForm.inputVerificationCode"
                                placeholder="请输入验证码">
                                <template #append>
                                    <VerificationCode ref="player" @getVerificationCode="getVerificationCode"
                                        title="看不清？点击换一张">
                                    </VerificationCode>
                                </template>
                            </el-input>
                        </el-form-item>

                        <el-form-item prop="loginBtn" class="loginBtn">
                            <el-button color="#2fa7b9" plain style="width:100%;" @click="submitLoginInfo">立即登录
                            </el-button>
                        </el-form-item>
                        <p class="toPath">
                            还没有账号，<span style="color:#2fa7b9;" @click="toPath('/register')">立即注册</span>
                            <span style="float:right;color:#999;" @click="toPath('/resetPassword')">忘记密码？</span>
                        </p>
                    </el-form>

                    <footer style="text-align:center;">
                        <p>相信有一天, 理想主义终将所向披靡.</p>
                    </footer>
                </div>
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
    // 验证码组件
    import VerificationCode from './verification-code/verificationCode.vue'
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

    import {
        useRoute,
        useRouter
    } from 'vue-router';

    import axios from "axios";

    export default {
        components: {
            VerificationCode
        },
        setup() {
            // 前往某个界面
            const toPath = (path) => {
                window.location.href = path
            }
            const route = useRoute();
            const router = useRouter();

            // 子组件的方法
            const player = ref(null);
            // 获取页面元素
            const ruleFormRef = ref('');
            // 响应式内容
            const state = reactive({
                ruleForm: {
                    loginName: '',
                    loginPass: '',
                    // 随机生成的验证码
                    verificationCode: '',
                    // 输入的验证码
                    inputVerificationCode: ''
                },
                //获取当前访问的协议/:IP/端口
                registerUrl: location.protocol + '//' + location.host + '/' + 'register.html'
            });

            // 用户名的非空验证
            const validateName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    callback()
                }
            };
            // 密码的非空验证
            const validatePass = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    callback()
                }
            };
            // 验证码的非空、输入正确验证
            const validateVerificationCode = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    if (value.toLowerCase() == state.ruleForm.verificationCode.toLowerCase()) {
                        callback()
                    } else {
                        return callback(new Error('嘿，验证码输入有误哟~'))
                    }
                }
            };

            // 接收子组件（验证码）传的值
            const getVerificationCode = (codeList) => {
                state.ruleForm.verificationCode = codeList;
            };


            // 约束规则 正则
            const rules = reactive({
                loginName: [{
                    validator: validateName,
                    trigger: 'blur'
                }],
                loginPass: [{
                    validator: validatePass,
                    trigger: 'blur'
                }],
                inputVerificationCode: [{
                    validator: validateVerificationCode,
                    trigger: 'blur'
                }]
            });

            // 立即登录按钮
            function login(ruleFormRef) {
                const submitLoginInfo = () => {
                    // 1：校验登录信息  validate
                    ruleFormRef.value.validate((valid) => {
                        // 如果表单校验成功 提交数据到后台
                        if (valid) {
                            // 判断用户是否已经登录
                            const userInfo = sessionStorage.getItem("userInfo");
                            if (!userInfo) {
                                // 登录验证
                                continueLogin();
                            } else { // 用户已经登录账号
                                ElMessageBox.confirm(
                                    '糟糕！系统检测到您已登录，请选择继续登录or前往后台',
                                    '登录提示', {
                                        confirmButtonText: '继续登录',
                                        cancelButtonText: '前往后台',
                                        type: 'warning',
                                        center: true,
                                    }
                                ).then(() => {
                                    // 清除上一次登录信息
                                    window.sessionStorage.removeItem("userInfo");
                                    // 继续登录
                                    continueLogin();
                                }).catch(() => {
                                    //前往后台
                                    window.location.href = "/backstageHome"
                                })
                            }

                            function continueLogin() {
                                // 验证用户名、密码是否正确,数据库中是否存在
                                let params = {
                                    loginName: state.ruleForm.loginName,
                                    loginPass: state.ruleForm.loginPass,
                                };
                                axios.get("/login/showUserInfo", {
                                    params
                                }).then((res) => {
                                    if (res.data.code == "0") {
                                        // 将登录用户的信息存起来
                                        window.sessionStorage.setItem("userInfo", JSON
                                            .stringify(res
                                                .data));
                                        // 调用子组件方法（下一个验证码）refreshCode
                                        unref(player).refreshCode();
                                        // 重置表单项，将其值重置为初始值，并移除校验结果
                                        ruleFormRef.value.resetFields()


                                        // 加载动画
                                        const loading = ElLoading.service({
                                            lock: true,
                                            text: 'Loading',
                                            background: 'rgba(0, 0, 0, 0.7)',
                                        })
                                        setTimeout(() => {
                                            loading.close()
                                            // 判断是不是从别的页面跳转过来的
                                            if (route.query.redirect) {
                                                router.push({
                                                    path: decodeURIComponent(route
                                                        .query
                                                        .redirect) //跳转到原页面
                                                })
                                            } else {
                                                router.push({
                                                    path: '/backstageHome',
                                                }); //正常登录流程进入的页面
                                            }
                                        }, 1000)


                                    } else {
                                        ElMessage.error(res.data.msg)
                                    }
                                })
                            }
                        }
                    })
                }
                return {
                    submitLoginInfo
                }
            }

            return {
                ...toRefs(state),
                getVerificationCode,
                ...login(ruleFormRef),
                toPath,
                route,
                player,
                rules,
                ruleFormRef,
                User,
                Lock,
                Right
            }
        }
    }
</script>

<style scoped>
    .loginBox {
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        width: 100%;
        height: 100%;
        background: #fff url(../../assets/login/login-bg.png) no-repeat;
        background-size: cover;
    }

    .login_logo {
        display: block;
        position: absolute;
        left: 8vw;
        top: 20px;
        width: 160px;
        height: 50px;
        background: url(../../assets/logo.svg) no-repeat center;
        background-size: 160px;
    }

    .loginBox .midgroup {
        width: 420px;
        margin: 20vh auto;
        position: relative;
    }

    .loginBox .midgroup .logingroup {
        padding: 20px 40px;
        background: white;
        overflow: hidden;
        box-shadow: 0 1px 10px 0 rgb(7 17 27 / 10%);
        padding-top: 20px;
        border-radius: 10px;
    }

    .loginBox .midgroup .logingroup footer {
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

    .loginBtn :deep(.el-form-item__error) {
        text-align: center;
        width: 100%;
    }

    .toPath {
        font-size: 12px;
        color: #999;
    }

    .toPath span {
        cursor: pointer;
    }

    .toPath span:hover {
        color: #2fa7b9;
    }
</style>