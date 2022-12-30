<template>
    <div v-if="isRouterAlive">
        <el-dialog v-model="previewArticleDialogFormVisible" title="预览文章" width="70%" draggable
            @close="previewArticleCloseDialog(false)">
            <!-- 文章内容 -->
            <div v-highlight v-html="articleMain" class="article">

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
                state.articleMain = val
            })
            const state = reactive({
                previewArticleDialogFormVisible: props.previewArticleDialogFormVisible,
                articleMain: props.valueHtml
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
    
</style>