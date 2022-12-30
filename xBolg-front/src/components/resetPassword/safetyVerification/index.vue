<template>
    <div class="title">
        <span>找回步骤：</span>
        <span>1. 确认账号</span> &gt;&nbsp;&nbsp;
        <span style="font-size: 15px;font-weight: 600;">2.安全验证</span>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
        <span>3.重置密码</span>&nbsp;&nbsp;&gt;&nbsp;&nbsp;
        <span>4. 成功</span>
    </div>

    <el-form ref="ruleFormRef" :model="ruleForm" status-icon :rules="rules" label-width="120px"
        style="padding-top:20px;" label-position="top" size="large" class="demo-ruleForm">
        <p style="color: #606266;margin-bottom: 12px;font-size: 14px;">
            您的帐号可能存在安全风险，为了确保为您本人操作，请先进行安全验证。
            <p>我们已向您的邮箱{{userEmail}}发送了一条验证码。</p>
        </p>
        <el-form-item prop="verificationCode" style="width: 55%;float: left;">
            <el-input :prefix-icon="Key" v-model="ruleForm.verificationCode" placeholder="请输入六位数验证码" />
        </el-form-item>
        <el-form-item prop="nextStepBtn" class="reset_password_btn">
            <el-button color="#2fa7b9" plain style="width:80%;margin-left: 20%;" @click="nextStep">下一步</el-button>
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
            // 父组件传过来的邮箱、验证码
            setUserEmail: String,
            setCode: String
        },
        setup(props, {
            emit
        }) {
            // 获取页面元素
            const ruleFormRef = ref('');
            // 响应式内容
            const state = reactive({
                ruleForm: {
                    verificationCode: '',
                },
                // 用户邮箱
                userEmail: props.setUserEmail,
                // 系统生成验证码
                code: props.setCode
            });
            // 将用户邮箱部分数字加上星号
            state.userEmail = state.userEmail.slice(0, 1) + '***' + state.userEmail.slice(5, (state.userEmail.split('')
                .length+5));

            // 用户名或邮箱的非空、是否存在验证
            const validateVerificationCode = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    callback()
                }
            };

            // 约束规则 正则
            const rules = reactive({
                verificationCode: [{
                    validator: validateVerificationCode,
                    trigger: 'blur'
                }]
            });

            // 下一步按钮 
            function safetyVerification(ruleFormRef) {
                const nextStep = () => {
                    // 1：校验登录信息  validate
                    ruleFormRef.value.validate((valid) => {
                        // 表单校验成功
                        if (valid) {
                            if (state.ruleForm.verificationCode == state.code) {
                                let pass = 1;
                                emit('setPass', pass);
                                ElNotification({
                                    title: '提示',
                                    message: '验证通过，按要求填写输入框重置密码',
                                    type: 'success',
                                })
                                // 重置表单项，将其值重置为初始值，并移除校验结果
                                ruleFormRef.value.resetFields()
                            } else {
                                ElMessage.error('您当前输入的验证码有误')
                                state.ruleForm.verificationCode = ""
                            }
                        }
                    })
                }
                return {
                    nextStep
                }
            }

            return {
                ...toRefs(state),
                ...safetyVerification(ruleFormRef),
                rules,
                ruleFormRef,
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