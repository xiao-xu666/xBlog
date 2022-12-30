<template v-if="isRouterAlive">
    <!-- 弹窗添加链接 -->
    <el-dialog v-model="addLinkDialogFormVisible" title="添加链接" width="400px" center @close="addLinkCloseDialog(false)">
        <el-form ref="ruleFormRef" :model="form" :rules="rules">
            <el-form-item prop="urlName" label="网站名称：">
                <el-input v-model="form.urlName" placeholder="请输入网站名称" />
            </el-form-item>
            <el-form-item prop="urlAddres" label="网站地址：">
                <el-input v-model="form.urlAddres" placeholder="请输入网站地址" />
            </el-form-item>
            <el-form-item prop="urlReferral" label="网站介绍：">
                <el-input v-model="form.urlReferral" placeholder="请输入网站介绍" maxlength="50" show-word-limit />
            </el-form-item>
            <el-form-item prop="urlType" label="网站类型：">
                <el-select v-model="form.urlType" placeholder="请选择网站类型">
                    <el-option label="学习网站" value="1" />
                    <el-option label="友情链接" value="2" />
                </el-select>
            </el-form-item>
            <el-form-item prop="urlLitimg" label="网站首页：">
                <el-upload ref="uploadEle" style="width:100%;height: 150px;" class="upload-demo" drag :action="uploadURL"
                    :on-success="handleAvatarSuccess" :before-upload="beforeUploadFunction"
                     :on-preview="handlePictureCardPreview">
                    <img w-full v-if="imageUrl" :src="imageUrl" class="avatar"
                        style="height: 100%;object-fit: cover;" />
                    <el-icon class="el-icon--upload" v-if="!imageUrl">
                        <upload-filled />
                    </el-icon>
                    <div class="el-upload__text" v-if="!imageUrl">
                        图片拖拽此处或者<em>点击上传</em>
                    </div>
                    <el-dialog v-model="dialogVisible">
                        <img w-full :src="imageUrl" alt="Preview Image" />
                    </el-dialog>
                </el-upload>
            </el-form-item>
            <el-form-item prop="webmasterEmail" label="站长邮箱：">
                <el-input v-model="form.webmasterEmail" placeholder="请输入站长邮箱" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addLinkCloseDialog(false)">取消</el-button>
                <el-button plain color="#2fa7b9" @click="submitAdd">添加</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
    import {
        reactive,
        ref,
        toRefs,
        nextTick
    } from 'vue'

    import {
        ElMessage,
    } from 'element-plus'

    import axios from "axios";

    import {
        Plus
    } from '@element-plus/icons-vue'


    export default {
        props: {
            addLinkDialogFormVisible: Boolean
        },
        emits: ["onAddLinkCloseDialog"],
        setup(props, {
            emit
        }) {
            // 获取文件上传元素
            const uploadEle = ref(null)

            // 获取页面元素
            const ruleFormRef = ref('');

            const state = reactive({
                // 弹窗状态
                addLinkDialogFormVisible: props.addLinkDialogFormVisible,
                form: {
                    urlName: "",
                    urlAddres: "",
                    urlReferral: "",
                    urlType: "",
                    urlLitimg: "",
                    webmasterEmail: ""
                },
                // 图片上传到服务器的路径
                uploadURL: process.env.VUE_APP_URL + "/upload",
                // uploadURL: "http://localhost/fileUpload",
                imageUrl: ''
            })

            // 网站名称非空验证
            const validateUrlName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    callback()
                }
            };

            // 网址正则
            var urlGrep = new RegExp('^((https|http|ftp|rtsp|mms)?://)' +
                    "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" + '(([0-9]{1,3}\\.){3}[0-9]{1,3}' + '|' +
                    "([0-9a-z_!~*'()-]+\\.)*" + '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\.' + '[a-z]{2,6})' +
                    '(:[0-9]{1,5})?' + '((/?)|' + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$");
                // 网站地址非空验证
                const validateUrlAddres = (rule, value, callback) => {
                    if (!value) {
                        return callback(new Error('请填写此字段~'))
                    } else {
                        if (!urlGrep.test(value)) {
                            callback(new Error('请按照URL标准格式填写，必须包含http/https'));
                        } else {
                            callback()
                        }
                    }
                };

            // 网站简介非空验证
            const validateUrlReferral = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    callback()
                }
            };

            // 网站类型非空验证
            const validateUrlType = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    callback()
                }
            };

            // 图片是否上传
            const validateUrlLitimg = (rule, value, callback) => {
                if (state.imageUrl == "") {
                    return callback(new Error('网站缩略图未上传成功，上传后再添加哟~'))
                } else {
                    callback()
                }
            };

            // 邮箱正则表达式判断
            var emailGrep = /^\w+@[\da-z\.-]+\.([a-z]{2,6}|[\u2E80-\u9FFF]{2,3})$/;
            // 站长邮箱验证
            const validateWebmasterEmail = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    if (!emailGrep.test(value)) {
                        callback(new Error('请按照正确的邮箱格式输入'));
                    } else {
                        callback()
                    }
                }
            }

            // 约束规则 正则
            const rules = reactive({
                urlName: [{
                    validator: validateUrlName,
                    trigger: 'blur'
                }],
                urlAddres: [{
                    validator: validateUrlAddres,
                    trigger: 'blur'
                }],
                urlReferral: [{
                    validator: validateUrlReferral,
                    trigger: 'blur'
                }],
                urlType: [{
                    validator: validateUrlType,
                    trigger: 'blur'
                }],
                urlLitimg: [{
                    validator: validateUrlLitimg,
                    trigger: 'blur'
                }],
                webmasterEmail: [{
                    validator: validateWebmasterEmail,
                    trigger: 'blur'
                }],
            });

            // 图片上传格式、大小要求
            const beforeUploadFunction = (rawFile) => {
                if (rawFile.type !== 'image/jpeg' &&
                    rawFile.type !== 'image/jpg' &&
                    rawFile.type !== 'image/png' &&
                    rawFile.type !== 'image/gif') {
                    ElMessage.error('仅支持图片格式.png | .jpg | .jpeg | .gif ')
                    return false
                } else if (rawFile.size / 1024 / 1024 > 5) {
                    ElMessage.error('仅支持大小不超过5MB的图片!')
                    return false
                }
                return true
            }

            // 图片上传成功后执行的函数
            const handleAvatarSuccess = (response) => {
                state.imageUrl =  response.data;
                uploadEle.value.clearFiles();
            }

            // 局部组件刷新
            const isRouterAlive = ref(true);
            // 删除组件
            isRouterAlive.value = false;
            nextTick(() => {
                // 重新渲染组件
                isRouterAlive.value = true;
            });
            // 取消
            const addLinkCloseDialog = (visible) => {
                // 关闭窗口
                emit("onAddLinkCloseDialog", visible);
                // 清空已上传的文件列表
                uploadEle.value.clearFiles();
                // 重置表单项，将其值重置为初始值，并移除校验结果
                ruleFormRef.value.resetFields()
                // 清空图片
                state.imageUrl = "";
                // location.reload();
            };

            // 链接添加
            function addLink(ruleFormRef) {
                const submitAdd = () => {
                    // 1：校验链接信息
                    ruleFormRef.value.validate((valid) => {
                        // 如果表单校验成功 提交数据到后台
                        if (valid) {
                            const imageUrl = state.imageUrl.replace(process.env.VUE_APP_URL, "");
                            const params = {
                                urlName: state.form.urlName,
                                urlAddres: state.form.urlAddres,
                                urlReferral: state.form.urlReferral,
                                urlType: state.form.urlType,
                                urlLitimg: imageUrl,
                                webmasterEmail: state.form.webmasterEmail,
                            }
                            axios.post("/url/insert", params).then((res) => {
                                if (res.data.code == 0) {
                                    // 关闭弹窗
                                    emit("onAddLinkCloseDialog", false, res.data);
                                    // 重置表单项，将其值重置为初始值，并移除校验结果
                                    ruleFormRef.value.resetFields()
                                    // 清空已上传的文件列表
                                    uploadEle.value.clearFiles();
                                }
                            })

                        }
                    })

                }
                return {
                    submitAdd
                }
            }

            const dialogVisible = ref(false)

            const handlePictureCardPreview = (uploadFile) => {
                uploadEle.value = uploadFile.url
                dialogVisible.value = true
            }

            return {
                ...toRefs(state),
                rules,
                uploadEle,
                beforeUploadFunction,
                addLinkCloseDialog,
                handleAvatarSuccess,
                ...addLink(ruleFormRef),
                dialogVisible,
                handlePictureCardPreview,
                ruleFormRef,
                isRouterAlive
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