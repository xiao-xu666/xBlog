<template>
    <!-- 弹窗审核链接 -->
    <el-dialog v-model="auditLinkDialogFormVisible" title="审核链接" width="400px" center
        @close="auditLinkCloseDialog(false)">
        <div>
            <p style="margin-bottom: 10px;line-height: 25px;">
                链接名称：<span style="color:#2fa7b9;">{{auditLinkInfo.urlName}}</span>
            </p>
            <p style="margin-bottom: 10px;line-height: 25px;">
                链接地址：<a style="color:#2fa7b9;" :underline="false" :href="auditLinkInfo.urlAddres"
                    target="_blank">{{auditLinkInfo.urlAddres}}（点击前往）</a>
            </p>
            <p style="margin-bottom: 10px;line-height: 25px;">
                链接介绍：<span style="color:#2fa7b9;">{{auditLinkInfo.urlReferral}}</span>
            </p>
            <p style="margin-bottom: 10px;line-height: 25px;">
                站长邮箱：
                <!-- <span style="color:#2fa7b9;">{{auditLinkInfo.webmasterEmail}}</span> -->
                <a style="color:#2fa7b9;"
                    :href="'http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&amp;email='+auditLinkInfo.webmasterEmail"
                    target="_blank">{{auditLinkInfo.webmasterEmail}}（点击联系站长）</a>
            </p>
            <p style="margin-bottom: 10px;line-height: 25px;">
                申请类型：
                <span v-if="auditLinkInfo.urlType ==1" style="color:#2fa7b9;">推荐网站</span>
                <span v-if="auditLinkInfo.urlType ==2" style="color:#2fa7b9;">友情链接</span>
            </p>
            <p style="margin-bottom: 10px;line-height: 25px;">
                审核结果：
                <el-select v-model="value" class="m-2" placeholder="请选择审核结果" size="large" style="width: 75%;">
                    <el-option label="待审核" :value="1" />
                    <el-option label="通过" :value="2" />
                    <el-option label="拒绝" :value="3" />
                </el-select>
            </p>
            <p style="text-align: center;color: #9f9f9f;line-height: 25px;">
                审核结果系统将会自动下发至站长邮箱
            </p>
        </div>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="auditLinkCloseDialog(false)">取消</el-button>
                <el-button plain color="#2fa7b9" @click="submitConfirm">确定</el-button>
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
        watch
    } from 'vue'

    import {
        ElMessage,
        ElLoading,
        ElNotification
    } from 'element-plus'

    import axios from "axios";

    import {
        Plus
    } from '@element-plus/icons-vue'


    export default {
        props: ['auditLinkDialogFormVisible', 'auditLinkInfo'],
        emits: ["onAuditLinkCloseDialog"],
        setup(props, {
            emit
        }) {

            const state = reactive({
                // 弹窗状态
                auditLinkDialogFormVisible: props.auditLinkDialogFormVisible,
                // 当前链接的信息
                auditLinkInfo: props.auditLinkInfo,
                // 审核结果
                value: null,
            })

            watch(() => props.auditLinkInfo, (val, preVal) => {
                state.auditLinkInfo = val
            })

            // 取消
            const auditLinkCloseDialog = (visible) => {
                // 关闭窗口
                emit("onAuditLinkCloseDialog", visible);
                // 清除选择
                state.value = null;
            };

            // 确定审核
            const submitConfirm = () => {
                let loading = ElLoading.service({
                    lock: true,
                    text: 'Loading',
                    background: 'rgba(0, 0, 0, 0.7)',
                })
                // 判断审核内容是否由改变
                if (state.value == null || state.value == state.auditLinkInfo.urlPass) {
                    setTimeout(() => {
                        loading.close()
                        ElNotification({
                            title: '提示',
                            message: '未发生改变的操作',
                            type: 'success',
                        })
                        // 清除选择
                        state.value = null;
                        // 关闭弹窗
                        emit("onAuditLinkCloseDialog", false);
                    }, 500)
                } else {
                    // 判断审核结果为：待审核|通过|拒绝，发送不同的审核结果信息
                    if (state.value == 1) { // 待审核
                        // 将审核结果传给数据库进行修改
                        const params = {
                            urlId: state.auditLinkInfo.urlId,
                            urlPass: state.value,
                        }
                        axios.get("/url/updateUrlPass", {
                            params
                        }).then((res) => {
                            if (res.data.code == 0) {
                                setTimeout(() => {
                                    loading.close()
                                    ElNotification({
                                        title: '提示',
                                        message: '已审核成功',
                                        type: 'success',
                                    })
                                    // 清除选择
                                    state.value = null;
                                    // 关闭弹窗
                                    emit("onAuditLinkCloseDialog", false, res.data);
                                }, 1000)
                            }
                        })

                    } else if (state.value == 2) { // 通过
                        // 将审核结果传给数据库进行修改
                        const params = {
                            urlId: state.auditLinkInfo.urlId,
                            urlPass: state.value,
                        }
                        axios.get("/url/updateUrlPass", {
                            params
                        }).then((res) => {
                            if (res.data.code == 0) {
                                // 发送审核结果给链接站长
                                const params = {
                                    registerEmail: state.auditLinkInfo.webmasterEmail,
                                    content: "您好，" + state.auditLinkInfo.urlName + "(" + state
                                        .auditLinkInfo.urlAddres +
                                        ")站长，您在xBlog博客中申请添加链接已通过，合作共赢！"
                                }
                                axios.get("/sendEmail", {
                                    params
                                }).then((res) => {
                                    if (res.data.code == 0) {
                                        setTimeout(() => {
                                            loading.close()
                                            ElNotification({
                                                title: '提示',
                                                message: '已审核成功',
                                                type: 'success',
                                            })
                                            // 清除选择
                                            state.value = null;
                                            // 关闭弹窗
                                            emit("onAuditLinkCloseDialog", false, res.data);
                                        }, 1000)
                                    }
                                })
                            }
                        })

                    } else if (state.value == 3) { // 拒绝
                        // 将审核结果传给数据库进行修改
                        const params = {
                            urlId: state.auditLinkInfo.urlId,
                            urlPass: state.value,
                        }
                        axios.get("/url/updateUrlPass", {
                            params
                        }).then((res) => {
                            if (res.data.code == 0) {
                                // 发送审核结果给链接站长
                                const params = {
                                    registerEmail: state.auditLinkInfo.webmasterEmail,
                                    content: "您好，" + state.auditLinkInfo.urlName + "(" + state
                                        .auditLinkInfo.urlAddres +
                                        ")站长，您在xBlog博客中申请添加链接失败。申请内容有误或站点内容不合适与本站建立连接，期待您更好的展现站点，再与我们合作，谢谢！"
                                }
                                axios.get("/sendEmail", {
                                    params
                                }).then((res) => {
                                    if (res.data.code == 0) {
                                        setTimeout(() => {
                                            loading.close()
                                            ElNotification({
                                                title: '提示',
                                                message: '已审核成功',
                                                type: 'success',
                                            })
                                            // 清除选择
                                            state.value = null;
                                            // 关闭弹窗
                                            emit("onAuditLinkCloseDialog", false, res.data);
                                        }, 1000)
                                    }
                                })
                            }
                        })

                    }
                }

            }


            return {
                ...toRefs(state),
                auditLinkCloseDialog,
                submitConfirm
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
    }

    :global(.el-dialog__body) {
        padding: 25px calc(var(--el-dialog-padding-primary) + 5px) 0px !important;
    }
</style>