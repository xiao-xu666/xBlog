<template>
    <div class="systemSetup">
        <el-row :gutter="20">
            <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
                <div class="left_box">
                    <h3 class="title">
                        <el-icon style="margin-right: 10px;">
                            <Setting />
                        </el-icon>
                        首页展示设置
                    </h3>
                    <div class="content">
                        <h4>首页置顶文章（单选）</h4>
                        <p>提示：1、不选择则不显示置顶文章。</p>
                        <el-select-v2 v-model="stickArticleValue" :options="articleInfo" placeholder="请选择首页置顶文章"
                            style="width: 100%" clearable="true" value-key="label" size="large">
                            <template #default="{ item }">
                                <span style="margin-right: 8px">{{ item.label }}</span>
                                <span style="color: var(--el-text-color-secondary); font-size: 13px">
                                    {{ item.value }}
                                </span>
                            </template>
                        </el-select-v2>
                    </div>
                    <div class="content">
                        <h4>首页文章展示（多选）</h4>
                        <p>提示：1、最多选择10条数据。</p>
                        <el-select-v2 v-model="allArticleValue" :options="articleInfo" placeholder="请选择首页文章展示【10篇】"
                            style="width: 100%" multiple value-key="label" :multiple-limit="10" clearable="true"
                            size="large">
                            <template #default="{ item }">
                                <span style="margin-right: 8px">{{ item.label }}</span>
                                <span style="color: var(--el-text-color-secondary); font-size: 13px">
                                    {{ item.value }}
                                </span>
                            </template>
                        </el-select-v2>
                    </div>
                    <div class="content">
                        <h4>左侧精选文章（多选）</h4>
                        <p>提示：1、最多选择8条数据。2、选中的第一条文章为封面文章</p>
                        <el-select-v2 v-model="featuredArticleValue" :options="articleInfo"
                            placeholder="请选择首页左侧精选文章【8篇】" style="width: 100%" multiple value-key="label"
                            :multiple-limit="8" clearable="true" size="large">
                            <template #default="{ item }">
                                <span style="margin-right: 8px">{{ item.label }}</span>
                                <span style="color: var(--el-text-color-secondary); font-size: 13px">
                                    {{ item.value }}
                                </span>
                            </template>
                        </el-select-v2>
                    </div>
                    <div class="content">
                        <h4>左侧技术专区文章（多选）</h4>
                        <p>提示：1、最多选择10条数据。</p>
                        <el-select-v2 v-model="technologyArticleValue" :options="technologyArticleInfo"
                            placeholder="请选择首页左侧技术专区文章【10篇】" style="width: 100%" multiple value-key="label"
                            :multiple-limit="10" clearable="true" size="large">
                            <template #default="{ item }">
                                <span style="margin-right: 8px">{{ item.label }}</span>
                                <span style="color: var(--el-text-color-secondary); font-size: 13px">
                                    {{ item.value }}
                                </span>
                            </template>
                        </el-select-v2>
                    </div>
                    <div class="content">
                        <h4>右侧资源专区文章（多选）</h4>
                        <p>提示：1、最多选择10条数据。2、选中的第一条文章为封面文章</p>
                        <el-select-v2 v-model="resourceArticleValue" :options="resourceArticleInfo"
                            placeholder="请选择首页右侧资源专区文章【10篇】" style="width: 100%" multiple value-key="label"
                            :multiple-limit="10" clearable="true" size="large">
                            <template #default="{ item }">
                                <span style="margin-right: 8px">{{ item.label }}</span>
                                <span style="color: var(--el-text-color-secondary); font-size: 13px">
                                    {{ item.value }}
                                </span>
                            </template>
                        </el-select-v2>
                    </div>
                    <div class="content">
                        <h4>左侧广告1</h4>
                        <h5>广告图</h5>
                        <p>提示：点击上传的图片地址由系统生成，修改会导致图片失效</p>
                        <el-input size="large" v-model="advertising.advertisingImg1" placeholder="请点击上传图片或手动输入图片地址">
                            <template #append>
                                <el-upload ref="uploadEle" class="upload-demo" :action="uploadURL"
                                    :on-success="handleAvatarSuccess1" :before-upload="beforeUploadFunction"
                                    :show-file-list="false">
                                    <template #trigger>
                                        <el-button style="width: 100px;">上传图片</el-button>
                                    </template>
                                </el-upload>
                            </template>
                        </el-input>
                        <h5>广告链接</h5>
                        <p>提示：链接必须包含http或者https</p>
                        <el-input size="large" v-model="advertising.advertisingLink1" placeholder="请输入广告链接" />
                    </div>
                    <div class="content">
                        <h4>右侧广告1</h4>
                        <h5>广告图</h5>
                        <p>提示：点击上传的图片地址由系统生成，修改会导致图片失效</p>
                        <el-input size="large" v-model="advertising.advertisingImg2" placeholder="请点击上传图片或手动输入图片地址">
                            <template #append>
                                <el-upload ref="uploadEle" class="upload-demo" :action="uploadURL"
                                    :on-success="handleAvatarSuccess2" :before-upload="beforeUploadFunction"
                                    :show-file-list="false">
                                    <template #trigger>
                                        <el-button style="width: 100px;">上传图片</el-button>
                                    </template>
                                </el-upload>
                            </template>
                        </el-input>
                        <h5>广告链接</h5>
                        <p>提示：链接必须包含http或者https</p>
                        <el-input size="large" v-model="advertising.advertisingLink2" placeholder="请输入广告链接" />
                    </div>
                    <div class="content">
                        <h4>右侧广告2</h4>
                        <h5>广告图</h5>
                        <p>提示：点击上传的图片地址由系统生成，修改会导致图片失效</p>
                        <el-input size="large" v-model="advertising.advertisingImg3" placeholder="请点击上传图片或手动输入图片地址">
                            <template #append>
                                <el-upload ref="uploadEle" class="upload-demo" :action="uploadURL"
                                    :on-success="handleAvatarSuccess3" :before-upload="beforeUploadFunction"
                                    :show-file-list="false">
                                    <template #trigger>
                                        <el-button style="width: 100px;">上传图片</el-button>
                                    </template>
                                </el-upload>
                            </template>
                        </el-input>
                        <h5>广告链接</h5>
                        <p>提示：链接必须包含http或者https</p>
                        <el-input size="large" v-model="advertising.advertisingLink3" placeholder="请输入广告链接" />
                    </div>
                    <h3 class="title">
                        <el-icon style="margin-right: 10px;">
                            <Setting />
                        </el-icon>
                        网站特效设置
                    </h3>
                    <div class="content">
                        <h4>特效1：点击网站页面显示24字核心价值观上升</h4>
                        <el-select v-model="effects.effects01" placeholder="请选择特效1状态" size="large" style="width: 100%;">
                            <el-option label="关闭" value="false" />
                            <el-option label="开启" value="true" />
                        </el-select>
                    </div>
                    <div class="content">
                        <h4>特效2：彩片飘落效果</h4>
                        <el-select v-model="effects.effects02" placeholder="请选择特效2状态" size="large" style="width: 100%;">
                            <el-option label="关闭" value="false" />
                            <el-option label="开启" value="true" />
                        </el-select>
                    </div>
                    <div class="content" style="text-align: center;">
                        <el-button plain color="#2fa7b9" @click="leftSaveSettings">保存设置</el-button>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>

</template>
<script>
    import {
        ref,
        toRefs,
        reactive,
        onMounted,
        watch
    } from 'vue'
    import axios from "axios";
    import {
        ElMessage,
        ElNotification,
        ElMessageBox
    } from 'element-plus'
    /**
     * @desc 加载获取数据
     */
    function loadData(state) {
        // 查询全部文章
        axios.get("/article/showAllArticleInfo").then(res => {
            // 全部文章信息
            res.data.data.forEach(element => {
                state.articleInfo.unshift({
                    label: element.articleId,
                    value: element.articleTitle
                })
            })
            // 技术文章信息
            res.data.data.forEach(element => {
                if (element.articleClassifyId == 1) {
                    state.technologyArticleInfo.unshift({
                        label: element.articleId,
                        value: element.articleTitle
                    })
                }
            })
            // 资源文章信息
            res.data.data.forEach(element => {
                if (element.articleClassifyId == 2) {
                    state.resourceArticleInfo.unshift({
                        label: element.articleId,
                        value: element.articleTitle
                    })
                }
            })
        })

        // 查询系统设置
        axios.get("/showAllSystemSetup").then(res => {
            state.stickArticleValue = res.data.data[0].stickArticle;
            state.allArticleValue = JSON.parse(res.data.data[0].allArticle);
            state.featuredArticleValue = JSON.parse(res.data.data[0].featuredArticle);
            state.technologyArticleValue = JSON.parse(res.data.data[0].technologyArticle);
            state.resourceArticleValue = JSON.parse(res.data.data[0].resourceArticle);
            state.advertising.advertisingImg1 = res.data.data[0].advertising1;
            state.advertising.advertisingLink1 = res.data.data[0].advertisingLink1;
            state.advertising.advertisingImg2 = res.data.data[0].advertising2;
            state.advertising.advertisingLink2 = res.data.data[0].advertisingLink2;
            state.advertising.advertisingImg3 = res.data.data[0].advertising3;
            state.advertising.advertisingLink3 = res.data.data[0].advertisingLink3;

            // 网站特效设置
            state.effects.effects01=res.data.data[0].effects01.toString();
            state.effects.effects02=res.data.data[0].effects02.toString();
        })
    }
    export default ({
        setup() {
            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });
            const state = reactive({
                // 文章信息
                articleInfo: [],
                // 置顶文章
                stickArticleValue: '',
                // 十篇文章展示
                allArticleValue: [],
                // 左侧精选文章
                featuredArticleValue: [],
                // 技术专区的文章信息
                technologyArticleInfo: [],
                // 左侧技术专区文章
                technologyArticleValue: [],
                // 资源专区的文章信息
                resourceArticleInfo: [],
                // 右侧资源专区文章
                resourceArticleValue: [],
                // 广告
                advertising: {
                    advertisingImg1: '',
                    advertisingLink1: '',
                    advertisingImg2: '',
                    advertisingLink1: '',
                    advertisingImg3: '',
                    advertisingLink1: '',
                },
                // 网站特效
                effects: {
                    effects01: null, // 特效1
                    effects02: null, // 特效2
                },
                // 广告上传到服务器的路径
                uploadURL: process.env.VUE_APP_URL + "/upload",
            })

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
            const handleAvatarSuccess1 = (response) => {
                state.advertising.advertisingImg1 = response.data;
            }
            const handleAvatarSuccess2 = (response) => {
                state.advertising.advertisingImg2 = response.data;
            }
            const handleAvatarSuccess3 = (response) => {
                state.advertising.advertisingImg3 = response.data;
            }

            // 保存首页展示设置
            const leftSaveSettings = () => {
                const params = {
                    stickArticle: state.stickArticleValue, // '置顶',
                    allArticle: '[' + state.allArticleValue + ']', // '文章展示',
                    featuredArticle: '[' + state.featuredArticleValue + ']', // '左侧精选文章',
                    technologyArticle: '[' + state.technologyArticleValue + ']', // '左侧技术专区文章',
                    resourceArticle: '[' + state.resourceArticleValue + ']', // '右侧资源专区文章',
                    advertising1: state.advertising.advertisingImg1, // '左侧广告图1',
                    advertisingLink1: state.advertising.advertisingLink1, // '左侧广告链接1',
                    advertising2: state.advertising.advertisingImg2, // '右侧广告图1',
                    advertisingLink2: state.advertising.advertisingLink2, // '左侧广告链接1',
                    advertising3: state.advertising.advertisingImg3, // '右侧广告图2',
                    advertisingLink3: state.advertising.advertisingLink3, // '左侧广告链接1'

                    effects01: state.effects.effects01,// 特效1
                    effects02: state.effects.effects02,// 特效2
                }
                axios.post("/systemSetup/updateSystemSetup",params).then((res) => {
                    if (res.data.code == 0) {
                        loadData(state);
                        ElMessage({
                            message: '保存成功',
                            type: 'success',
                        })
                        // 刷新界面
                        location.reload()
                    }
                })
            }

            return {
                ...toRefs(state),
                beforeUploadFunction,
                handleAvatarSuccess1,
                handleAvatarSuccess2,
                handleAvatarSuccess3,
                leftSaveSettings
            }
        }
    })
</script>

<style>
    .systemSetup .el-col {
        margin-bottom: 20px;
    }

    .systemSetup .title {
        color: #2fa7b9;
        margin-bottom: 10px;
        padding: 10px 20px;
        display: inline-flex;
        justify-content: center;
        align-items: center;
    }


    .left_box {
        width: 100%;
        height: auto;
        background: white;
        padding: 20px;
        /* 添加此属性 padding间距不扩大div */
        box-sizing: border-box;
    }

    .right_box {
        width: 100%;
        height: auto;
        background: white;
        padding: 20px;
        /* 添加此属性 padding间距不扩大div */
        box-sizing: border-box;
    }

    .systemSetup .content {
        width: 100%;
        padding: 10px 20px;
        /* 添加此属性 padding间距不扩大div */
        box-sizing: border-box;
    }

    .systemSetup .content h4 {
        line-height: 45px;
        color: #8f8f8f;
    }

    .systemSetup .content h5 {
        line-height: 35px;
        color: #8f8f8f;
        font-size: 15px;
        font-weight: lighter;
    }

    .systemSetup .content p {
        line-height: 25px;
        color: #8f8f8f;
        font-size: 12px;
    }
</style>