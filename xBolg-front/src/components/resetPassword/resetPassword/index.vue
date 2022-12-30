<template>
    <div class="title">
        <span>找回步骤：</span>
        <span>1. 确认账号</span> &gt;&nbsp;&nbsp;
        <span>2.安全验证</span>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
        <span style="font-size: 15px;font-weight: 600;">3.重置密码</span>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
        <span>4. 成功</span>
    </div>

    <el-form ref="ruleFormRef" :model="ruleForm" status-icon :rules="rules" label-width="100px"
        style="padding-top:20px;" size="large" class="demo-ruleForm">
        <p style="color: #606266;margin-bottom: 12px;font-size: 14px;">
            您正在找回的帐号是：{{setUserName}}
        </p>
        <el-form-item prop="password" label="新密码：">
            <el-input :prefix-icon="Lock" v-model="ruleForm.password" placeholder="请输入新密码" show-password
                autocomplete="off" />
        </el-form-item>
        <el-form-item prop="confirmPassword" label="确认新密码：">
            <el-input :prefix-icon="Key" v-model="ruleForm.confirmPassword" placeholder="请确认新密码" show-password
                autocomplete="off" />
        </el-form-item>
        <el-form-item prop="loginBtn" class="reset_password_btn">
            <el-button color="#2fa7b9" plain @click="nextStep" style="width: 100%;">重置密码</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    import {
        reactive,
        ref,
        toRefs,
        unref
    } from 'vue'

    import {
        Lock,
        Key
    } from '@element-plus/icons-vue'

    import {
        ElLoading,
        ElMessage,
        ElNotification,
        ElMessageBox
    } from 'element-plus'

    import axios from "axios";

    export default {
        components: {},
        props: {
            setUserName: String,
            setUserEmail: String
        },
        setup(props, {
            emit
        }) {
            // 获取页面元素
            const ruleFormRef = ref('');
            // 响应式内容
            const state = reactive({
                ruleForm: {
                    password: '',
                    confirmPassword: ''
                },
                userEmail: props.setUserEmail
            });

            // 密码正则表达式判断
            var passGrep = /^[a-zA-Z0-9_.-=*&^%$#@!+]{6,18}$/;

            // 新密码
            const validatePassword = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    if (value) {
                        if (!passGrep.test(value)) {
                            callback(new Error('密码6~18位字母、数字、特殊字符组成'));
                        } else {
                            if (!ruleFormRef.value) return
                            ruleFormRef.value.validateField('confirmPassword', () => null)
                        }
                    }
                    callback()
                }
            };

            // 确认密码
            const validateConfirmPassword = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请填写此字段~'))
                } else if (value !== state.ruleForm.password) {
                    callback(new Error("您输入的密码与确认密码不一致"))
                } else {
                    callback()
                }
            };

            // 约束规则 正则
            const rules = reactive({
                password: [{
                    validator: validatePassword,
                    trigger: 'blur'
                }],
                confirmPassword: [{
                    validator: validateConfirmPassword,
                    trigger: 'blur'
                }]
            });

            // 下一步按钮 
            function resetPassword(ruleFormRef) {
                const nextStep = () => {
                    // 1：校验登录信息
                    ruleFormRef.value.validate((valid) => {
                        // 表单校验成功
                        if (valid) {
                            // 将用户输入的密码通过get请求到服务器，进行修改
                            let params = {
                                userPass: state.ruleForm.confirmPassword,
                                userEmail: state.userEmail
                            };
                            axios.get("/resetPassword/updateUserPass", {
                                params
                            }).then((res) => {
                                // 修改成功
                                if (res.data.code == 0) {
                                    emit('updateSucceed', res.data.code);
                                    // 重置表单项，将其值重置为初始值，并移除校验结果
                                    ruleFormRef.value.resetFields()
                                } else {
                                    ElMessage({
                                        message: '糟糕，系统出错啦！耐心的再来一遍吧',
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
                ...resetPassword(ruleFormRef),
                rules,
                ruleFormRef,
                Lock,
                Key
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