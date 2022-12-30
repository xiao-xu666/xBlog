<template>
    <div class="main_centent" v-loading="loading" element-loading-text="玩命加载中...">
        <div>
            <div class="centent_title">
                <h3>友情链接</h3>
            </div>
            <div class="centent_box friendshipLink">
                <el-row :gutter="20">
                    <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6" v-for="item in friendshipLinkInfo">
                        <a :href="item.urlAddres" target="_blank">
                            <el-card class="card_centent">
                                <div class="card_img">
                                    <img :src="item.urlLitimg" />
                                </div>

                                <div class="link_text">
                                    <p>{{item.urlName}}</p>
                                    <span>{{item.urlReferral}}</span>
                                </div>
                            </el-card>
                        </a>
                    </el-col>
                </el-row>
            </div>
        </div>
        <div>
            <div class="centent_title">
                <h3>推荐链接</h3>
            </div>
            <div class="centent_box recommendLink">
                <el-row :gutter="20">
                    <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6" v-for="item in recommendLinkInfo">
                        <a :href="item.urlAddres" target="_blank">
                            <el-card class="card_centent">
                                <div class="card_img">
                                    <img :src="item.urlLitimg" />
                                </div>

                                <div class="link_text">
                                    <p>{{item.urlName}}</p>
                                    <span>{{item.urlReferral}}</span>
                                </div>
                            </el-card>
                        </a>
                    </el-col>
                </el-row>
            </div>
        </div>
        <div>
            <div class="centent_title">
                <h3>申请说明</h3>
            </div>
            <div class="centent_box">
                <h4>推荐链接申请：</h4>
                <p>1. 不强制要求互链；</p>
                <p>2. 对Google Pagerank和百度权重等SEO指标有要求；</p>
                <p>3. 网站内容健康积极向上，全技术、全资源类网站。</p>
                <p><strong>注</strong>：申请结果系统将会自动下发至站长邮箱，注意查收。</p>

                <h4>友情链接申请：</h4>
                <p>1. 申请前请将本站添加为友链，符合要求的本站会在12小时内跟上友链；</p>
                <p>2. 对Google Pagerank和百度权重等SEO指标无要求；</p>
                <p>3. 不接受内容含有淘宝客、色情、纯商业以及违反中华人民共和国法律的网站；</p>
                <p>4. 优先考虑收录原创个人博客，日记，技术类和生活类博客；</p>
                <p>5. 如若发现互换友链后未经通知私自撤换友链者，本站将拉黑并永不交换；</p>
                <p>6. 不定时互访，对于长时间无法访问的站点，会撤下友链，恢复后重新申请。</p>
                <p><strong>注</strong>：申请结果系统将会自动下发至站长邮箱，注意查收。</p>
            </div>
        </div>

        <div>
            <div class="centent_title">
                <h3>申请链接</h3>
            </div>
            <div class="centent_box">
                <el-form ref="ruleFormRef" :model="form" :rules="rules">
                    <el-row :gutter="20">
                        <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
                            <el-row :gutter="20">
                                <el-col :span="24">
                                    <el-form-item prop="urlName" label="网站名称：">
                                        <el-input v-model="form.urlName" placeholder="请输入网站名称" />
                                    </el-form-item>
                                </el-col>

                                <el-col :span="24">
                                    <el-form-item prop="urlAddres" label="网站地址：">
                                        <el-input v-model="form.urlAddres" placeholder="请输入网站地址" />
                                    </el-form-item>
                                </el-col>
                                <el-col :span="24">
                                    <el-form-item prop="urlReferral" label="网站介绍：">
                                        <el-input v-model="form.urlReferral" placeholder="请输入网站介绍" maxlength="50"
                                            show-word-limit />
                                    </el-form-item>
                                </el-col>
                                <el-col :span="24">
                                    <el-form-item prop="urlType" label="网站类型：">
                                        <el-select v-model="form.urlType" placeholder="请选择网站类型" style="width: 100%;">
                                            <el-option label="推荐链接" value="1" />
                                            <el-option label="友情链接" value="2" />
                                        </el-select>
                                    </el-form-item>
                                </el-col>

                            </el-row>
                        </el-col>
                        <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
                            <el-row>
                                <el-col :span="24">
                                    <el-form-item prop="urlLitimg" label="网站首页：">
                                        <el-upload ref="uploadEle" style="width:100%;height: 130px;" class="upload-demo"
                                            drag :action="uploadURL" :on-success="handleAvatarSuccess"
                                            :before-upload="beforeUploadFunction" :on-progress="handleProgress"
                                            :on-preview="handlePictureCardPreview" :on-remove="handleRemove">
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
                                </el-col>
                                <el-col :span="24">
                                    <el-form-item prop="webmasterEmail" label="站长邮箱：">
                                        <el-input v-model="form.webmasterEmail" placeholder="请输入站长邮箱" />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                        </el-col>

                        <el-col :span="24">
                            <el-form-item>
                                <el-button style="margin: 0 auto;" plain color="#2fa7b9" @click="submitAdd">立即添加
                                </el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>

                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
    import {
        reactive,
        ref,
        toRefs,
        onMounted
    } from 'vue'

    import axios from "axios";

    import {
        ElNotification
    } from 'element-plus'

    function loadData(state) {
        state.loading = true
        axios.get("/url/showAllUrlInfo", {
            params: {
                'urlType': 1
            }
        }).then(res => {
            state.recommendLinkInfo = res.data.data;
        })
        axios.get("/url/showAllUrlInfo", {
            params: {
                'urlType': 2
            }
        }).then(res => {
            state.friendshipLinkInfo = res.data.data;

            state.loading = false
        })
    }


    export default {
        setup(props) {
            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });
            // 获取文件上传元素
            const uploadEle = ref(null)
            // 获取页面元素
            const ruleFormRef = ref('');
            const state = reactive({
                // 推荐链接所有信息
                recommendLinkInfo: [],
                // 友情链接所有信息
                friendshipLinkInfo: [],
                // 图片根路径
                url: process.env.VUE_APP_URL,
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
                imageUrl: '',
                loading: false
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
            var urlGrep = new RegExp('^((https|http|)?://)' +
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
                if (!state.urlLitimg || !state.imageUrl) {
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

            // 图片格式的文件
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
                state.imageUrl = response.data;
                uploadEle.value.clearFiles();
                state.urlLitimg = state.imageUrl
            }


            // 链接添加
            function addLink(ruleFormRef) {
                const submitAdd = () => {
                    // 1：校验链接信息
                    ruleFormRef.value.validate((valid) => {
                        // 如果表单校验成功 提交数据到后台
                        if (valid) {
                            const imageUrl = state.imageUrl.replace(process.env.VUE_APP_URL+'/download/', "");
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
                                    // 重置表单项，将其值重置为初始值，并移除校验结果
                                    ruleFormRef.value.resetFields()
                                    // 清空已上传的文件列表
                                    uploadEle.value.clearFiles();
                                    state.imageUrl = ''
                                    ElNotification({
                                        title: '提示',
                                        message: '链接申请成功，等待审核，审核结果将会下发至您邮箱，请注意查收！',
                                        type: 'success',
                                    })
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

            const handleRemove = (uploadFile, uploadFiles) => {
            }

            const handlePictureCardPreview = (uploadFile) => {
                uploadEle.value = uploadFile.url
                dialogVisible.value = true
            }

            return {
                ...toRefs(state),
                rules,
                beforeUploadFunction,
                handleAvatarSuccess,
                ...addLink(ruleFormRef),
                handleRemove,
                dialogVisible,
                handlePictureCardPreview,
                ruleFormRef,
                uploadEle
            }
        }

    }
</script>

<style scoped>
    .friendshipLink .el-col,
    .recommendLink .el-col {
        margin-bottom: 20px;
    }

    .main_centent {
        width: 100%-50px;
        height: auto;
        overflow: hidden;
        background-color: white;
        padding: 25px;
        line-height: 30px;
        color: #474749;
        border-radius: 10px;
    }

    .centent_box {
        width: 100%;
    }

    .centent_box>h4 {
        margin-left: 10px;
    }

    .centent_box>p {
        text-indent: 2em;
        line-height: 35px;
    }

    .centent_title>h3 {
        font-size: 20px;
        font-weight: 700;
        padding-left: 10px;
        margin: 15px 0;
        border-left: 4px solid #2FA7B9;
    }

    .el-card {
        --el-card-padding: 0px;
    }

    .el-card:hover {
        background: rgb(240 240 240);
    }

    .card_centent {
        padding: 0px;
    }

    .card_img {
        width: 100%;
        height: 100px;
        overflow: hidden;
    }

    .card_img img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }


    .link_text {
        padding: 5px 15px;
        height: 75px;
    }

    .link_text p {
        font-size: 14px;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-box-orient: vertical;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        line-height: 25px;
    }

    .link_text span {
        font-size: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-box-orient: vertical;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-height: 20px;
        color: #989898;
    }

    :deep(.el-upload-dragger) {
        padding: 3px;
    }

    :deep(.el-upload-dragger) {
        width: 100%;
        height: 130px;
    }
</style>