<template>
    <div class="loginBox">
        <a class="login_logo"></a>
        <div class="midgroup">
            <div class="loginwrap loginwarrp">
                <div class="logingroup login_form">
                    <h2 style="text-align: center; color: #2fa7b9;">注 册</h2>
                    <el-form ref="ruleFormRef" :model="ruleForm" status-icon :rules="rules" label-width="120px"
                        style="padding-top:20px;" label-position="top" size="large" class="demo-ruleForm">
                        <el-form-item prop="registerName">
                            <el-input :prefix-icon="User" v-model="ruleForm.registerName" placeholder="请输入用户名" />
                        </el-form-item>
                        <el-form-item prop="registerEmail">
                            <el-input :prefix-icon="Message" v-model="ruleForm.registerEmail" placeholder="请输入电子邮箱" />
                        </el-form-item>
                        <el-form-item prop="registerPass">
                            <el-input :prefix-icon="Lock" v-model="ruleForm.registerPass" show-password
                                autocomplete="off" placeholder="请输入密码" />
                        </el-form-item>
                        <el-form-item prop="inputVerificationCode">
                            <el-input :prefix-icon="Right" v-model="ruleForm.inputVerificationCode" placeholder="邮箱验证码">
                                <template #append>
                                    <el-button plain @click="getCode(formData)" :disabled="!show" color="#FAFCFF"
                                         >{{codeText}}</el-button>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="loginBtn" class="loginBtn">
                            <el-button color="#2fa7b9" plain style="width:100%;" @click="submitAddRegisterInfo">立即注册
                            </el-button>
                        </el-form-item>
                        <p style="font-size: 12px; text-align: center;color:#999;">
                            已有账号，<a href="/login" style="color:#2fa7b9;">立即登录</a>
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
    import {
        reactive,
        ref,
        toRefs,
        watch
    } from 'vue'

    import {
        User,
        Lock,
        Right,
        Message
    } from '@element-plus/icons-vue'

    import {
        ElMessage,
        ElLoading,
        ElNotification
    } from 'element-plus'

    import axios from "axios";

    // 获取验证码60秒倒计时
    const TIME_COUNT = 60;
    // 3分钟=180秒，三分钟后清除验证码
    const TIME_CLEAR_COUNT = 180;
    export default {
        setup() {
            const ruleFormRef = ref('')
            const state = reactive({
                ruleForm: {
                    registerName: '',
                    registerEmail: '',
                    registerPass: '',
                    // 随机生成的验证码
                    verificationCode: '',
                    // 输入的验证码
                    inputVerificationCode: ''
                },
                //获取当前访问的协议/:IP/端口
                loginUrl: location.protocol + '//' + location.host + '/' + 'login.html',
                // 获取验证码 点击后为禁止状态
                show: true,
                // 获取验证码的文字 点击后改变
                codeText: '获取验证码',
                // 当前秒数
                count: '',
                // 时间  计时器
                timer: null,
                // 存放生成的六位随机数   验证码
                code: '',
                // 三分钟后清除验证码,计时器
                clearCount: "",
                timer2: null,
                // 点击发送验证码，再进行验证验证码输入框
                affirmGet: false
            })

            // 用户名正则表达式判断
            var nameGrep = /^[\u4e00-\u9fa5a-zA-Z0-9_]{2,8}$/;
            // 邮箱正则表达式判断
            var emailGrep =
                /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
            // 密码正则表达式判断
            var passGrep = /^[a-zA-Z0-9_.-=*&^%$#@!+]{6,18}$/;

            // 用户名的非空、正则验证
            const validateName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {

                    if (!nameGrep.test(value)) {
                        callback(new Error('用户名由2~8位中英文、数字、下划线组成'));

                    } else {
                        let params = {
                            registerName: value
                        };
                        axios.get("/register/showUserName", {
                            params
                        }).then((res) => {
                            if (res.data.code == 1) {
                                callback(new Error('此用户名已被注册，请更换一个'));
                            } else {
                                callback()
                            }
                        })
                    }
                }
            }
            // 密码的非空验证
            const validatePass = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    if (!passGrep.test(value)) {
                        callback(new Error('密码6~18位字母、数字、特殊字符组成'));
                    } else {
                        callback()
                    }
                }
            }
            // 邮箱的非空验证
            const validateEmail = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    if (!emailGrep.test(value)) {
                        callback(new Error('请按照正确的邮箱格式输入'));
                    } else {
                        let params = {
                            registerEmail: value
                        };
                        axios.get("/register/showUserEmail", {
                            params
                        }).then((res) => {
                            if (res.data.code == 1) {
                                callback(new Error('此邮箱已被注册，请更换一个'));
                            } else {
                                callback()
                            }
                        })
                    }
                }
            }

            // 验证码的非空、输入正确验证
            const validateVerificationCode = (rule, value, callback) => {
                // 判断邮箱的正则是否通过，没有通过则先不验证此字段
                ruleFormRef.value.validateField('registerEmail', err => {
                    if (err) {
                        if (state.affirmGet) {
                            if (!value) {
                                return callback(new Error('请填写此字段~'))
                            } else {
                                // 输入的验证码正确
                                if (value != state.code) {
                                    return callback(new Error('验证码有误，请重新输入'))
                                } else {
                                    callback()
                                }
                            }
                        } else {
                            if (!value) {
                                return callback(new Error('获取验证码，填写此字段~'))
                            }
                        }
                    }
                })
            }
            // 接收子组件（验证码）传的值
            const getVerificationCode = (codeList) => {
                state.ruleForm.verificationCode = codeList;
            }

            const rules = reactive({
                registerName: [{
                    validator: validateName,
                    trigger: 'blur'
                }],
                registerPass: [{
                    validator: validatePass,
                    trigger: 'blur'
                }],
                registerEmail: [{
                    validator: validateEmail,
                    trigger: 'blur'
                }],
                inputVerificationCode: [{
                    validator: validateVerificationCode,
                    trigger: 'blur'
                }],
            })

            // 确认好获取验证码的邮箱，防止获取后修改，修改则本次验证码立即失效
            let affirmEmail = "";
            // 获取验证码 
            const getCode = (formData) => {
                // 点击发送验证码，再进行验证验证码输入框
                state.affirmGet = true
                // 判断邮箱的正则是否通过
                ruleFormRef.value.validateField('registerEmail', err => {
                    if (err) {
                        // 获取验证码，将当前表单中的邮箱号保存
                        affirmEmail = state.ruleForm.registerEmail
                        // 生成六位数随机验证码发送给后台
                        for (var i = 0; i < 6; i++) {
                            state.code += Math.floor(Math.random() * 10);
                        }
                        // let params = {
                        //     registerEmail: decodeURI(state.ruleForm.registerEmail),
                        //     verificationCode: state.code
                        // };
                        // // 将验证码、用户输入的邮箱传给后台，发送给用户邮箱
                        // axios.get("/register/registerGetCode", {
                        //     params
                        // }).then((res) => {
                        //     if (res.data.code == 0) {
                        // 将验证码、用户输入的邮箱传给后台，发送给用户邮箱
                        const params = {
                            registerEmail: state.ruleForm.registerEmail, // 用户当前邮箱
                            content: "【xBlog博客】验证码：" + state.code +
                                "，请勿将验证码告知他人，有效期3分钟，请妥善保管。"
                        }
                        axios.get("/sendEmail", {
                            params
                        }).then((res) => {
                            if (res.data.code == 0) {
                                // 点击后倒计时60秒
                                if (!state.timer) {
                                    state.count = TIME_COUNT;
                                    state.show = false;
                                    state.timer = setInterval(() => {
                                        if (state.count > 0 && state.count <= TIME_COUNT) {

                                            if (state.ruleForm.registerEmail ==
                                                affirmEmail) {
                                                state.count--;
                                                state.codeText = state.count + 's';
                                            } else {
                                                state.show = true;
                                                window.clearInterval(state.timer);
                                                state.timer = null;
                                                state.codeText = "获取验证码";
                                                affirmEmail = "";
                                                state.code = "";
                                            }
                                        } else {
                                            state.show = true;
                                            window.clearInterval(state.timer);
                                            state.timer = null;
                                            state.codeText = "重新获取";
                                            affirmEmail = ""
                                        }
                                    }, 1000)
                                }
                            } else {
                                ElMessage.error('验证码获取失败.')
                            }
                        })


                    } else {
                        ElMessage.error('按要求填写电子邮箱后再获取验证码.')
                        return false;
                    }
                });

            }

            // 立即注册按钮
            function register(ruleFormRef) {
                const submitAddRegisterInfo = () => {
                    // 1：校验注册信息  validate
                    ruleFormRef.value.validate((valid) => {
                        // 如果表单校验成功 提交数据到后台
                        if (valid) {
                            const params = {
                                userName: state.ruleForm.registerName,
                                userPass: state.ruleForm.registerPass,
                                userEmail: state.ruleForm.registerEmail,
                            }
                            axios.post("/register/insertRegisterInfo", params).then((res) => {
                                // 加载动画
                                const loading = ElLoading.service({
                                    lock: true,
                                    text: 'Loading',
                                    background: 'rgba(0, 0, 0, 0.7)',
                                })
                                setTimeout(() => {
                                    loading.close()
                                }, 1000)

                                setTimeout(() => {
                                    ElNotification({
                                        title: '注册成功',
                                        message: '登录即可访问xBlog博客后台.',
                                        type: 'success',
                                    })
                                    // 重置表单项，将其值重置为初始值，并移除校验结果
                                    ruleFormRef.value.resetFields()
                                    state.show = true;
                                    window.clearInterval(state.timer);
                                    state.timer = null;
                                    state.codeText = "获取验证码";
                                    affirmEmail = "";
                                    state.code = "";
                                }, 1200)
                                setTimeout(() => {
                                    window.location.href = "/login"
                                }, 1800)

                            })
                        }
                    })
                }
                return {
                    submitAddRegisterInfo
                }
            }
            return {
                ...toRefs(state),
                getVerificationCode,
                getCode,
                rules,
                ruleFormRef,
                ...register(ruleFormRef),
                // 图标
                User,
                Lock,
                Right,
                Message
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
        padding: 0px 10px;
        background: #f6f6f6;
    }

    .loginBtn :deep(.el-form-item__error) {
        text-align: center;
        width: 100%;
    }
</style>