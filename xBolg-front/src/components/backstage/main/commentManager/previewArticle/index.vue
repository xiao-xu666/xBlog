<template>
    <div v-if="isRouterAlive">
        <el-dialog v-model="previewArticleDialogFormVisible" title="预览文章" draggable
            @close="previewArticleCloseDialog(false)">
            <!-- 文章简介 -->
            <div class="articleDase">
                <h3>文章摘要：</h3>
                <p>
                    {{articleInfo.articleDase}}
                </p>
            </div>
            <!-- 文章内容 -->
            <div class="articleDase">
                <h3>文章内容：</h3>
            </div>
            <div v-highlight v-html="articleInfo.articleContent" class="article">

            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="previewArticleCloseDialog(false)">退出预览</el-button>
                </span>
            </template>
        </el-dialog>
    </div>

</template>

<script>
    import {
        reactive,
        ref,
        toRefs,
        nextTick,
        watch
    } from 'vue'
    export default {
        props: [
            'previewArticleDialogFormVisible',
            'valueHtml'
        ],
        emits: ["onPreviewArticleCloseDialog"],
        setup(props, {
            emit
        }) {
            watch(() => props.valueHtml, (val, preVal) => {
                state.articleInfo = val
            })
            const state = reactive({
                previewArticleDialogFormVisible: props.previewArticleDialogFormVisible,
                articleInfo: props.valueHtml
            })
            // 局部组件刷新
            const isRouterAlive = ref(true);
            const previewArticleCloseDialog = (visible) => {
                // 关闭窗口
                emit("onPreviewArticleCloseDialog", visible);

                // 删除组件
                isRouterAlive.value = false;
                nextTick(() => {
                    // 重新渲染组件
                    isRouterAlive.value = true;
                });
            };
            return {
                ...toRefs(state),
                previewArticleCloseDialog,
                isRouterAlive
            }
        }
    }
</script>

<style scoped>
    /* 文章摘要 */
    .articleDase {
        margin: 20px 0;
        border-left: 5px solid #2fa7b9;
        padding: 10px 15px;
        background: #f8f8f8;
        font-size: 14px;
        color: #888;
        line-height: 30px;
        box-sizing: border-box;
        overflow: hidden;
        align-items: center;
    }


    /* 弹窗大小 */
    @media screen and (min-width: 1100px) {
        :deep(.el-dialog) {
            --el-dialog-width: 60%;
        }
    }
    @media screen and (max-width: 1100px) {
        :deep(.el-dialog) {
            --el-dialog-width: 90%;
        }
    }
</style>