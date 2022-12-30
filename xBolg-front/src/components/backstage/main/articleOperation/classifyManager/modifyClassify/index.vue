<template>
    <!-- 弹窗添加链接 -->
    <el-dialog v-model="modifyClassifyDialogFormVisible" title="编辑用户信息" width="400px" center
        @close="modifyClassifyCloseDialog(false,ruleFormRef)">
        <el-form :model="form" label-width="100px">
            <el-form-item label="分类名称：">
                <el-input v-model="form.classifyName" placeholder="请输入分类名称" />
            </el-form-item>
            <el-form-item label="分类介绍：">
                <el-input v-model="form.classifyIntroduce" placeholder="请输入分类介绍" />
            </el-form-item>
            <el-form-item label="颜色1：" style="width: 100%;">
                <el-color-picker v-model="form.color1" style="width: 100%;" />
            </el-form-item>
            <el-form-item label="颜色2：" style="width: 100%;">
                <el-color-picker v-model="form.color2" style="width: 100%;" />
            </el-form-item>
            <el-form-item label="合成渐变：" style="width: 100%;">
                <div v-if="form.color1!=='' && form.color2!==''"
                    :style="'background:linear-gradient(to right,' + form.color1 + ',' + form.color2 + ')'"
                    style="width: 100%;height:25px;margin: 0 auto;">
                </div>
                <span v-if="form.color1==='' && form.color2===''">
                    未选择颜色
                </span>
                <div v-if="form.color1!=='' && form.color2===''" :style="'background:'+form.color1"
                    style="width: 100%;height:25px;margin: 0 auto;">
                </div>
                <div v-if="form.color1==='' && form.color2!==''" :style="'background:'+form.color2"
                    style="width: 100%;height:25px;margin: 0 auto;">
                </div>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="modifyClassifyCloseDialog(false,ruleFormRef)">取消</el-button>
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
            modifyClassifyDialogFormVisible: Boolean,
            classifyInfo: Object
        },
        emits: ["onModifyClassifyCloseDialog"],
        setup(props, {
            emit
        }) {

            //数据更新后调用
            onUpdated(() => {
                state.form.classifyName = props.classifyInfo.classifyName;
                state.form.color1 = props.classifyInfo.color1;
                state.form.color2 = props.classifyInfo.color2;
                state.form.classifyIntroduce = props.classifyInfo.classifyIntroduce;
                state.classifyInfo = props.classifyInfo
            })

            const state = reactive({
                // 弹窗状态
                modifyClassifyDialogFormVisible: props.modifyClassifyDialogFormVisible,
                // 当前编辑的用户信息
                classifyInfo: null,
                // 表单信息
                form: {
                    classifyName: "",
                    color1: "",
                    color2: "",
                    classifyIntroduce:""
                },
            })

            // 取消
            const modifyClassifyCloseDialog = (visible) => {
                // 关闭窗口
                emit("onModifyClassifyCloseDialog", visible);
            };

            // 提交修改
            const submit = () => {
                if (state.form.classifyName == state.classifyInfo.classifyName &&
                    state.form.color1 == state.classifyInfo.color1 &&
                    state.form.color2 == state.classifyInfo.color2 &&
                    state.form.classifyIntroduce == state.classifyInfo.classifyIntroduce) {
                    emit("onModifyClassifyCloseDialog", false);
                    ElNotification({
                        title: '提示',
                        message: "没有任何变化的操作~",
                        type: 'success',
                    })
                } else {
                    if (state.form.classifyName !== '' &&
                        state.form.color1 !== '' &&
                        state.form.color2 !== '' &&
                        state.form.classifyIntroduce !== '') {
                        // 修改分类信息
                        const params = {
                            classifyId: state.classifyInfo.classifyId,
                            classifyName: state.form.classifyName,
                            color1: state.form.color1,
                            color2: state.form.color2,
                            classifyIntroduce:state.form.classifyIntroduce
                        }
                        axios.post("/classify/update", params).then((res) => {
                            if (res.data.code == 0) {
                                emit("onModifyClassifyCloseDialog", false, res.data);

                                ElNotification({
                                    title: '提示',
                                    message: "分类信息修改成功~",
                                    type: 'success',
                                })
                            }
                        })
                    } else {
                        ElMessage({
                            showClose: true,
                            message: '请填写（选择）提供的所有选项，不能为空',
                            type: 'error',
                        })
                    }
                }
            }

            return {
                ...toRefs(state),
                modifyClassifyCloseDialog,
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

    :deep(.el-color-picker) {
        width: 100%;
    }

    :deep(.el-color-picker__trigge)r {
        width: 100%;
    }
</style>