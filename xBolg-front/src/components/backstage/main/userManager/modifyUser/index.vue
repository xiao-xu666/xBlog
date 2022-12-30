<template>
    <!-- 弹窗添加链接 -->
    <el-dialog v-model="modifyUserDialogFormVisible" title="编辑用户信息" width="400px" center
        @close="modifyUserCloseDialog(false,ruleFormRef)">
        <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="80px">
            <el-form-item prop="userName" label="用户名：">
                <el-input v-model="form.userName" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item prop="userPass" label="密&nbsp;&nbsp;&nbsp;&nbsp;码：">
                <el-input v-model="form.userPass" placeholder="请输入密码" />
            </el-form-item>
            <el-form-item prop="userEmail" label="邮&nbsp;&nbsp;&nbsp;&nbsp;箱：">
                <el-input v-model="form.userEmail" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item prop="userSignature" label="签&nbsp;&nbsp;&nbsp;&nbsp;名：">
                <el-input v-model="form.userSignature" placeholder="请输入个人签名" maxlength="20" show-word-limit />
            </el-form-item>
            <el-form-item prop="userType" label="类&nbsp;&nbsp;&nbsp;&nbsp;型：">
                <el-select v-model="form.userType" placeholder="请选择用户类型">
                    <el-option label="管理员" value="0" />
                    <el-option label="普通用户" value="1" />
                </el-select>
            </el-form-item>
            <el-form-item prop="state" label="状&nbsp;&nbsp;&nbsp;&nbsp;态：">
                <el-select v-model="form.state" placeholder="请选择用户状态">
                    <el-option label="正常" value="0" />
                    <el-option label="冻结" value="1" />
                </el-select>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="modifyUserCloseDialog(false,ruleFormRef)">取消</el-button>
                <el-button plain color="#2fa7b9" @click="submit" @keyup.enter="submit">提交</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
    import {
        reactive,
        ref,
        toRefs,
        nextTick,
        onUpdated,
        onMounted
    } from 'vue'

    import {
        ElMessage,
        FormInstance,
        ElNotification,
        ElMessageBox
    } from 'element-plus'

    import axios from "axios";

    import {
        Plus
    } from '@element-plus/icons-vue'


    export default {
        props: {
            modifyUserDialogFormVisible: Boolean,
            userInfo: Object
        },
        emits: ["onModifyUserCloseDialog"],
        setup(props, {
            emit
        }) {

            //数据更新后调用
            onUpdated(() => {
                state.form.userName = props.userInfo.userName;
                state.form.userPass = props.userInfo.userPass;
                state.form.userEmail = props.userInfo.userEmail;
                state.form.userSignature = props.userInfo.userSignature;
                state.form.userType = props.userInfo.userType.toString();
                state.form.state = props.userInfo.state.toString();
                state.userInfo = props.userInfo
            })

            // 获取页面元素
            const ruleFormRef = ref('');

            const state = reactive({
                // 弹窗状态
                modifyUserDialogFormVisible: props.modifyUserDialogFormVisible,
                // 当前编辑的用户信息
                userInfo: null,
                // 表单信息
                form: {
                    userName: "",
                    userPass: "",
                    userEmail: "",
                    userSignature: "",
                    userType: "",
                    state: ""
                },
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
                // 内容没有改变时不做验证
                if (value !== state.userInfo.userName) {
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
                } else {
                    callback()
                }

            }
            // 密码的非空验证
            const validatePass = (rule, value, callback) => {
                // 内容没有改变时不做验证
                if (value !== state.userInfo.userPass) {
                    if (!value) {
                        return callback(new Error('请填写此字段~'))
                    } else {
                        if (!passGrep.test(value)) {
                            callback(new Error('密码6~18位字母、数字、特殊字符组成'));
                        } else {
                            callback()
                        }
                    }
                } else {
                    callback()
                }
            }
            // 邮箱的非空验证
            const validateEmail = (rule, value, callback) => {
                // 内容没有改变时不做验证
                if (value !== state.userInfo.userEmail) {
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
                } else {
                    callback()
                }
            }


            // 约束规则 正则
            const rules = reactive({
                userName: [{
                    validator: validateName,
                    trigger: 'blur'
                }],
                userPass: [{
                    validator: validatePass,
                    trigger: 'blur'
                }],
                userEmail: [{
                    validator: validateEmail,
                    trigger: 'blur'
                }],
            });

            // 取消
            const modifyUserCloseDialog = (visible, FormInstance) => {
                // 关闭窗口
                emit("onModifyUserCloseDialog", visible);

                FormInstance.resetFields()
            };

            // 提交修改
            const submit = () => {
                // 1：校验表单信息
                ruleFormRef.value.validate((valid) => {
                    // 如果表单校验成功 提交数据到后台
                    if (valid) {
                        if (state.form.userName == state.userInfo.userName &&
                            state.form.userPass == state.userInfo.userPass &&
                            state.form.userEmail == state.userInfo.userEmail &&
                            state.form.userSignature == state.userInfo.userSignature &&
                            parseInt(state.form.userType) == state.userInfo.userType &&
                            parseInt(state.form.state) == state.userInfo.state) {
                                emit("onModifyUserCloseDialog", false);
                                ElNotification({
                                        title: '提示',
                                        message: "没有任何变化的操作~",
                                        type: 'success',
                                    })
                        } else {
                            // 修改用户信息
                            const params = {
                                userId: state.userInfo.userId,
                                userName: state.form.userName,
                                userPass: state.form.userPass,
                                userEmail: state.form.userEmail,
                                userSignature: state.form.userSignature,
                                userType: parseInt(state.form.userType),
                                state: parseInt(state.form.state)
                            }
                            axios.post("/user/updateUserInfo", params).then((res) => {
                                if (res.data.code == 0) {
                                    emit("onModifyUserCloseDialog", false, res.data);

                                    ElNotification({
                                        title: '提示',
                                        message: "用户信息修改成功~",
                                        type: 'success',
                                    })
                                }
                            })
                        }
                    } else {
                        ElMessage.error('根据要求填写内容进行修改~')
                    }
                })
            }
            // 链接添加
            // function modifyUser(ruleFormRef) {
            //     const submitModify = () => {
            //         // 1：校验链接信息
            //         ruleFormRef.value.validate((valid) => {
            //             // 如果表单校验成功 提交数据到后台
            //             if (valid) {
            //                 const imageUrl = state.imageUrl.replace(process.env.VUE_APP_URL,
            //                     "");
            //                 const params = {
            //                     urlName: state.form.urlName,
            //                     urlAddres: state.form.urlAddres,
            //                     urlReferral: state.form.urlReferral,
            //                     urlType: state.form.urlType,
            //                     urlLitimg: imageUrl,
            //                     webmasterEmail: state.form.webmasterEmail,
            //                 }
            //                 axios.post("/url/insert", params).then((res) => {
            //                     console.log(res)
            //                     if (res.data.code == 0) {
            //                         // 关闭弹窗
            //                         emit("onAddLinkCloseDialog", false, res.data);
            //                         // 重置表单项，将其值重置为初始值，并移除校验结果
            //                         ruleFormRef.value.resetFields()
            //                         // 清空已上传的文件列表
            //                         uploadEle.value.clearFiles();
            //                     }
            //                 })

            //             }
            //         })

            //     }
            //     return {
            //         submitModify
            //     }
            // }

            return {
                ...toRefs(state),
                rules,
                modifyUserCloseDialog,
                // ...modifyUser(ruleFormRef),
                ruleFormRef,
                submit
            }
        }


    }
</script>
<style scoped>
    .el-button--text {
        margin-right: 15px;
    }

    .dialog-footer button:first-child {
        margin-right: 10px;
    }

    .el-select {
        width: 100%;
    }

    :global(.el-dialog__title) {
        color: #2fa7b9 !important;
        font-size: 20px !important;
        font-weight: 700 !important;
    }

    :deep(.el-upload-dragger) {
        padding: 0px !important;
        width: 100%;
        height: 130px;
    }

    :global(.el-dialog__body) {
        padding: 25px calc(var(--el-dialog-padding-primary) + 5px) 0px !important;
    }
</style>