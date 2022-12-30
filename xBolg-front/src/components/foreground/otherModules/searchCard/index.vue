<template>
    <!-- 卡片/搜索功能 -->
    <el-card class="search_card">
        <div class="search_img_box">
            <img :src="url">
        </div>
        <div class="search_input_box">
            <!-- 搜索框 -->
            <el-input v-model="input" class="w-50 m-2 search_input" size="large" placeholder="请输入关键字进行搜索"
                clearable="true" :prefix-icon="Search" @keyup.enter.native="searchContent('')" />
        </div>
        <div class="search_link_box">
            <p>
                <el-link type="info" :underline="false" @click="searchContent('JAVA')">JAVA</el-link>
                <el-link type="info" :underline="false" @click="searchContent('资源')">资源</el-link>
                <el-link type="info" :underline="false" @click="searchContent('VUE')">VUE</el-link>
                <el-link type="info" :underline="false" @click="searchContent('技术')">技术</el-link>
            </p>

        </div>
    </el-card>
</template>

<script>
    import {
        onBeforeMount,
        onMounted,
        reactive,
        toRefs
    } from "vue"
    import {
        Search
    } from '@element-plus/icons-vue'
    import {
        ElMessage
    } from 'element-plus'
    import {
        useRouter
    } from 'vue-router'


    export default {
        setup() {
            onMounted(() => {
                // 生成随机数，用户改变搜索框上方的背景图
                const nonce = Math.floor(Math.random() * 5+1);
                const nonce2 = (nonce < 6 ? '0' + nonce : nonce);
                state.url = require('../../../../assets/banner/banner'+ nonce2 +'.png')
            })
            const state = reactive({
                input: '',
                nonce: null,
                url:null
            });

            const router = new useRouter();

            const searchContent = (val) => {
                if (val == "") {
                    if (state.input !== "") {
                        router.push({
                            path: "/search",
                            query: {
                                keyword: state.input
                            }
                        })
                    } else {
                        ElMessage.error('请输入关键字进行搜索')
                    }
                } else {
                    router.push({
                        path: "/search",
                        query: {
                            keyword: val
                        }
                    })
                }


            }

            return {
                // ...toRefs 对响应式数据解构赋值
                ...toRefs(state),
                Search,
                searchContent
            };
        }
    };
</script>

<style scoped>
    .el-card {
        border-radius: 10px;
        margin-bottom: 20px;
    }

    :deep(.el-card__body) {
        padding: 0;
    }

    .search_img_box {
        width: 100%;
        overflow: hidden;
        margin-bottom: 20px;
    }

    .search_img_box img {
        width: 100%;
        height: 100%;
        -o-object-fit: cover;
        object-fit: cover;
    }

    .search_input_box {
        text-align: center;
        font-size: 12px;
        margin-bottom: 8px;
    }

    .search_input {
        width: 80%;
        border-radius: 50px;
    }

    .search_link_box {
        height: 18%;
        padding: 0px 15%;
        margin-bottom: 30px;
    }

    .search_link_box p {
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
    }

    :deep(.el-input--large .el-input__wrapper) {
        margin-top: 0px;
        border-radius: 50px;
    }

    .el-link {
        margin-right: 10px;
    }

    .el-link .el-icon--right.el-icon {
        vertical-align: text-bottom;
    }
</style>