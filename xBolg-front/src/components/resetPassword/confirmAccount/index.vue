<template>
    <div class="title">
        <span>找回步骤：</span>
        <span style="font-size: 15px;font-weight: 600;">1. 确认账号</span> &gt;&nbsp;&nbsp; <span>2.安全验证</span>
        &nbsp;&nbsp;&gt;&nbsp;&nbsp;
        <span>3.重置密码</span>&nbsp;&nbsp;&gt;&nbsp;&nbsp; <span>4. 成功</span>
    </div>
    <el-form ref="ruleFormRef" :model="ruleForm" status-icon :rules="rules" label-width="120px"
        style="padding-top:20px;" label-position="top" size="large" class="demo-ruleForm">
        <el-form-item prop="userNameOrEmail" label="请填写您需要找回的用户名或者电子邮箱：">
            <el-input :prefix-icon="User" v-model="ruleForm.userNameOrEmail" placeholder="请输入用户名或者电子邮箱" />
        </el-form-item>
        <!-- 验证码 -->
        <el-form-item prop="inputVerificationCode" style="width: 55%;float: left;">
            <el-input :prefix-icon="Right" v-model="ruleForm.inputVerificationCode" placeholder="请输入验证码">
                <template #append>
                    <VerificationCode ref="player" @getVerificationCode="getVerificationCode" title="看不清？点击换一张">
                    </VerificationCode>
                </template>
            </el-input>
        </el-form-item>

        <el-form-item prop="loginBtn" class="reset_password_btn">
            <el-button color="#2fa7b9" plain style="width:80%;margin-left: 20%;" @click="nextStep">下一步
            </el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    // 验证码组件
    import VerificationCode from './verificationCode/verificationCode.vue'
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
            VerificationCode
        },
        setup(props, {
            emit
        }) {
            // 子组件的方法
            const player = ref(null);
            // 获取页面元素
            const ruleFormRef = ref('');
            // 响应式内容
            const state = reactive({
                ruleForm: {
                    userNameOrEmail: '',
                    // 随机生成的验证码
                    verificationCode: '',
                    // 输入的验证码
                    inputVerificationCode: '',
                },
                showUserInfo: null,
                // 验证码
                code: ""
            });

            // 用户名或邮箱的非空、是否存在验证
            const validateName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    // 将输入的用户名或邮箱传给后台，判断是否存在该用户
                    let params = {
                        userNameOrEmail: value
                    };
                    axios.get("/resetPassword/showUserNameOrEmail", {
                        params
                    }).then((res) => {
                        if (res.data.code == 1) {
                            callback(new Error('当前输入的用户未注册本平台'));
                        } else {
                            state.showUserInfo = res.data.data;
                            callback()
                        }
                    })
                }
            };
            // 验证码的非空、输入正确验证
            const validateVerificationCode = (rule, value, callback) => {
                // 判断用户名or邮箱的正则是否通过，没有通过则先不验证验证码
                ruleFormRef.value.validateField('userNameOrEmail', err => {
                    if (err) {
                        if (!value) {
                            return callback(new Error('请填写此字段~'))
                        } else {
                            if (value.toLowerCase() == state.ruleForm.verificationCode.toLowerCase()) {
                                callback()
                            } else {
                                return callback(new Error('嘿，验证码输入有误哟~'))
                            }
                        }
                    }
                })
            };

            // 接收子组件（验证码）传的值
            const getVerificationCode = (codeList) => {
                state.ruleForm.verificationCode = codeList;
            };


            // 约束规则 正则
            const rules = reactive({
                userNameOrEmail: [{
                    validator: validateName,
                    trigger: 'blur'
                }],
                inputVerificationCode: [{
                    validator: validateVerificationCode,
                    trigger: 'blur'
                }]
            });

            // 下一步按钮
            function confirmAccount(ruleFormRef) {
                const nextStep = () => {
                    // 1：校验登录信息  validate
                    ruleFormRef.value.validate((valid) => {
                        // 如果表单校验成功
                        if (valid) {
                            // 生成六位数随机验证码发送给后台
                            for (var i = 0; i < 6; i++) {
                                state.code += Math.floor(Math.random() * 10);
                            }
                            // 发送验证码
                            // let params = {
                            //     registerEmail: decodeURI(state.showUserInfo.userEmail),
                            //     verificationCode: state.code
                            // };
                            // // 将验证码、用户输入的邮箱传给后台，发送给用户邮箱
                            // axios.get("/register/registerGetCode", {
                            //     params
                            // }).then((res) => {
                            // if (res.data.code == 0) {
                            const params = {
                                registerEmail: state.showUserInfo.userEmail,
                                content: "【xBlog博客】验证码："+state.code+"，此验证码用于重置密码，请勿将验证码告知他人，妥善保管。"
                            }
                            axios.get("/sendEmail", {
                                params
                            }).then((res) => {
                                if (res.data.code == 0) {
                                    // 将用户输入的用户名或邮箱查询到的用户信息传给父组件
                                    let info = {
                                        userInfo: state.showUserInfo,
                                        code: state.code
                                    }
                                    const loading = ElLoading.service({
                                        lock: true,
                                        text: 'Loading',
                                        background: 'rgba(0, 0, 0, 0.7)',
                                    })
                                    setTimeout(() => {
                                        loading.close()
                                        emit('setUserInfo', info);
                                    }, 1500)
                                    // 调用子组件方法（下一个验证码）refreshCode
                                    unref(player).refreshCode();
                                    // 重置表单项，将其值重置为初始值，并移除校验结果
                                    ruleFormRef.value.resetFields()
                                } else {
                                    ElMessage({
                                        message: '糟糕，系统出错啦！请再输入一次注册邮箱',
                                        type: 'warning',
                                    })
                                    window.location.href = "/resetPassword"
                                }
                            })

                        }
                    })
                }
                return {
                    nextStep
                }
            }

            return {
                ...toRefs(state),
                getVerificationCode,
                ...confirmAccount(ruleFormRef),
                player,
                rules,
                ruleFormRef,
                User,
                Lock,
                Right,
            }
        }
    }
</script>

<style scoped>
    .title {
        width: 100%-20px;
        padding: 10px 10px;
        background: linear-gradient(to right, #535b9a, #30bcd7);
        color: white;
        font-size: 14px;
        border-radius: 3px;
    }
</style>