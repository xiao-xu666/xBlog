<template>
    <div class="addClassify">
        <h3>
            <el-icon style="margin-right: 10px;">
                <Plus />
            </el-icon>
            添加分类
        </h3>
        <el-form status-icon :model="classify" class="demo-form-inline">
            <el-row :gutter="20">
                <el-col :xs="24" :sm="24" :md="12" :lg="5" :xl="5">
                    <el-form-item prop="classifyName" label="分类名称：" style="width: 100%;">
                        <el-input v-model="classify.classifyName" placeholder="请输入分类名称" />
                    </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :md="12" :lg="5" :xl="5">
                    <el-form-item prop="classifyIntroduce" label="分类介绍：" style="width: 100%;">
                        <el-input v-model="classify.classifyIntroduce" placeholder="请输入分类介绍" />
                    </el-form-item>
                </el-col>
                <el-col :xs="12" :sm="12" :md="8" :lg="3" :xl="3">
                    <el-form-item prop="color1" label="颜色1：" style="width: 100%;">
                        <el-color-picker v-model="classify.color1" />
                    </el-form-item>
                </el-col>
                <el-col :xs="12" :sm="12" :md="8" :lg="3" :xl="3">
                    <el-form-item prop="color2" label="颜色2：" style="width: 100%;">
                        <el-color-picker v-model="classify.color2" />
                    </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :md="8" :lg="4" :xl="3">
                    <el-form-item label="合成渐变：" style="width: 100%;">
                        <div v-if="classify.color1!=='' && classify.color2!==''"
                            :style="'background:linear-gradient(to right,' + classify.color1 + ',' + classify.color2 + ')'"
                            style="width: 100%;height:25px;margin: 0 auto;">
                        </div>
                        <span v-if="classify.color1==='' && classify.color2===''">
                            未选择颜色
                        </span>
                        <div v-if="classify.color1!=='' && classify.color2===''" :style="'background:'+classify.color1"
                            style="width: 100%;height:25px;margin: 0 auto;">
                        </div>
                        <div v-if="classify.color1==='' && classify.color2!==''" :style="'background:'+classify.color2"
                            style="width: 100%;height:25px;margin: 0 auto;">
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12" :md="24" :lg="4" :xl="5" style="text-align: center;">
                    <el-button style="width: 150px; text-align: center;" plain color="#2fa7b9" @click="submitAdd">添加
                    </el-button>
                </el-col>
            </el-row>
        </el-form>
    </div>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <h3>
                    <el-icon style="margin-right: 10px;">
                        <FolderOpened />
                    </el-icon>分类管理
                </h3>

                <p style="display: inline-flex;justify-content: center;align-items: center;font-size: 20px;">
                    <el-icon @click="refresh">
                        <Refresh />
                    </el-icon>
                </p>
            </div>
        </template>
        <div>
            <el-table v-loading="loading" element-loading-text="玩命加载中..." :data="tableData"
                style="width: 100%;text-align: center;" :cell-style="{textAlign:'center'}"
                :header-cell-style="{fontSize: '15px',background:'#2fa7b9','color':'white',textAlign:'center' }">
                <el-table-column label="序号" width="100" type="index" />
                <el-table-column prop="classifyName" label="分类名称" />
                <el-table-column prop="classifyIntroduce" label="文章介绍" />
                <el-table-column prop="articleNumber" label="文章数量" />
                <el-table-column label="颜色1">
                    <template #default="scope">
                        <span>{{scope.row.color1}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="颜色2">
                    <template #default="scope">
                        <span>{{scope.row.color2}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="合成渐变">
                    <template #default="scope">
                        <div :style="'background:linear-gradient(to right,' + scope.row.color1 + ',' + scope.row.color2 + ')'"
                            style="width: 60px;height:25px;margin: 0 auto;">
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button size="small" @click="modifyClassify(scope.$index, scope.row)"
                            style="margin: 0 0 10px 10px;">编辑</el-button>
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" :icon="Delete"
                            icon-color="#626AEF" :title="'确定删除“'+scope.row.classifyName+'”分类吗？'"
                            @confirm="submitDelete(scope.$index, scope.row)">
                            <template #reference>
                                <el-button size="small" type="danger" style="margin-bottom: 10px;">删除</el-button>
                            </template>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </el-card>

    <ModifyClassify :modifyClassifyDialogFormVisible="modifyClassifyDialogFormVisible" :classifyInfo="classifyInfo"
        @onModifyClassifyCloseDialog="modifyClassifyCloseDialogVisible"></ModifyClassify>
</template>

<script>
    // 引入编辑分类的弹窗
    import ModifyClassify from './modifyClassify/index.vue'

    import {
        ref,
        toRefs,
        reactive,
        onMounted,
        onUpdated,
        watch
    } from 'vue'
    import {
        Search,
        Delete
    } from '@element-plus/icons-vue'
    import axios from "axios";
    import {
        ElMessage,
        ElNotification,
        ElMessageBox
    } from 'element-plus'

    /**
     * 加载全部数据
     */
    function loadData(state) {
        state.loading = true

        axios.get("/showAllClassifyInfo").then(res => {
            if(res.data.code == 0){
                state.tableData = res.data.data;
                state.loading = false
            }else{
                ElMessage.error(res.data.msg)
            }
            
        })
    }
    export default {
        components: {
            ModifyClassify
        },
        setup(props) {

            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });

            const state = reactive({
                classify: {
                    classifyName: "",
                    color1: "",
                    color2: "",
                    classifyIntroduce:""
                },
                // 表格全部信息
                tableData: [],
                loading: false,
                // 编辑窗口状态
                modifyClassifyDialogFormVisible: false,
                // 当前编辑的信息
                classifyInfo: null
            })

            // 刷新按钮
            const refresh = () => {
                // 更新数据
                loadData(state);
                ElMessage({
                    message: '刷新成功',
                    type: 'success',
                })
            }

            // 打开编辑弹窗
            const modifyClassify = (index, row) => {
                state.modifyClassifyDialogFormVisible = true;
                state.classifyInfo = row;
            }

            // 关闭编辑窗口
            const modifyClassifyCloseDialogVisible = (visible, data) => {
                state.modifyClassifyDialogFormVisible = visible;
                if (data != undefined) {
                    //重新加载数据
                    loadData(state);
                }
            }

            // 增加删除事件
            const submitDelete = (index, row) => {
                //确定删除
                axios.delete("/classify/delete", {
                    params: {
                        classifyId: row.classifyId
                    }
                }).then(res => {
                    if (res.data.code == "0") {
                        //通知提示框
                        ElNotification({
                            title: '提示',
                            message: '已成功删除“' + row.classifyName + '”分类',
                            type: 'success',
                        });
                        //重新加载数据
                        loadData(state);
                    } else {
                        ElMessage.error(res.data.msg)
                    }
                })
            };

            // 添加分类
            const submitAdd = () => {
                if (state.classify.classifyName !== '' &&
                    state.classify.color1 !== '' &&
                    state.classify.color2 !== '' &&
                    state.classify.classifyIntroduce !== '') {
                    const params = {
                        classifyName: state.classify.classifyName,
                        color1: state.classify.color1,
                        color2: state.classify.color2,
                        classifyIntroduce: state.classify.classifyIntroduce
                    }
                    axios.post("/classify/insert", params).then(res => {
                        if (res.data.code == 0) {
                            ElNotification({
                                title: '提示',
                                message: '已成功添加“' + state.classify.classifyName + '”分类',
                                type: 'success',
                            });
                            // 清空提交内容
                            state.classify = {
                                classifyName: "",
                                color1: "",
                                color2: "",
                                classifyIntroduce:""
                            }
                            // 更新数据
                            loadData(state);
                        }else{
                            ElMessage.error(res.data.msg)
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
            return {
                // 搜索、删除图标
                Search,
                Delete,
                ...toRefs(state),
                submitAdd,
                submitDelete,
                refresh,
                modifyClassify,
                modifyClassifyCloseDialogVisible
            }
        }
    }
</script>

<style scoped>
    /* 添加分类管理样式 */
    .addClassify {
        width: 100%;
        padding: 20px;
        box-sizing: border-box;
        background: white;
        margin-bottom: 20px;
    }

    .addClassify h3 {
        color: #2fa7b9;
        margin-bottom: 20px;
        display: inline-flex;
        justify-content: center;
        align-items: center;
    }

    :deep(.el-color-picker) {
        width: 100%;
    }

    :deep(.el-color-picker__trigge)r {
        width: 100%;
    }

    /* 分类管理卡片样式 */
    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .card-header h3 {
        display: inline-flex;
        justify-content: center;
        align-items: center;
    }

    :deep(.el-card__header) {
        border-bottom: 1px solid rgb(238 238 238);
        /* background: linear-gradient(to right, #535b9a, #30bcd7); */
        color: #2fa7b9;
    }

    .text {
        font-size: 14px;
    }

    .item {
        margin-bottom: 18px;
    }

    .el-card {
        border-radius: 0px;
        border: none;
    }

    .el-card.is-always-shadow {
        box-shadow: 0px 0px 0px rgb(0 0 0 / 12%);
    }

    .demo-image__error .image-slot {
        font-size: 30px;
    }

    .demo-image__error .image-slot .el-icon {
        font-size: 30px;
    }

    .demo-image__error .el-image {
        width: 100%;
        height: 200px;
    }

    .example-showcase .el-dropdown+.el-dropdown {
        margin-left: 15px;
    }

    .example-showcase .el-dropdown-link {
        cursor: pointer;
        color: var(--el-color-primary);
        display: flex;
        align-items: center;
    }


    /* 分页样式 */
    :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
        background-color: #2fa7b9;
    }

    .el-pagination {
        margin-top: 20px;
        justify-content: center;
    }

    :deep(.el-table .cell) {
        -webkit-box-orient: vertical;
        display: -webkit-box;
        -webkit-line-clamp: 2;
    }

    @media screen and (max-width: 500px) {
        .hidePaging {
            display: none;
        }
    }

    @media screen and (min-width: 500px) {
        .showPaging {
            display: none;
        }
    }
</style>