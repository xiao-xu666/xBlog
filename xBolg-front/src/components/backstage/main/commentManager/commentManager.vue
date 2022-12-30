<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <h3>
                    <el-icon style="margin-right: 10px;">
                        <ChatDotSquare />
                    </el-icon>
                    评论管理
                </h3>
                <!-- 小于768px -->
                <div class="hidden-sm-and-down">
                    <el-row :gutter="10">
                        <el-col :span="18">
                            <el-input :prefix-icon="Search" v-model="searchValue" @keyup.enter.native="search"
                                placeholder="关键字搜索（回车）" />
                        </el-col>
                        <el-col :span="6" style="display: inline-flex;justify-content: center;align-items: center;">
                            <el-icon style="font-size: 20px;color: #b3b6bc;" @click="refresh">
                                <Refresh />
                            </el-icon>
                        </el-col>
                    </el-row>
                </div>

                <!-- 大于等于768px  -->
                <div class="visible">
                    <p style="display: inline-flex;justify-content: center;align-items: center;">
                        <el-icon style="margin-right: 20px;" @click="openSearch">
                            <Search />
                        </el-icon>
                        <el-icon @click="refresh">
                            <Refresh />
                        </el-icon>
                    </p>
                </div>
            </div>
        </template>
        <div>
            <el-table element-loading-text="玩命加载中..." v-loading="loading" :data="tableData"
                style="width: 100%;text-align: center;" :cell-style="{textAlign:'center'}"
                :row-class-name="rowClassName"
                :header-cell-style="{fontSize: '15px',background:'#2fa7b9','color':'white',textAlign:'center' }">
                <el-table-column label="序号" width="100" type="index" :index="Nindex" />
                <el-table-column prop="commentId" label="评论ID" width="100" />
                <el-table-column label="上级评论ID" width="120">
                    <template #default="scope">
                        <div v-if="scope.row.parentId == 0">一级评论</div>
                        <div v-else>{{scope.row.parentId}}</div>
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="评论者" width="150" />
                <el-table-column label="内容">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.content" placement="top" effect="light">
                            <span>{{scope.row.content}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="评论文章">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.articleTitle" placement="top" effect="light">
                            <a :href="'/articleDetails/'+scope.row.articleId"
                                style="display: inline-flex;justify-content: center;align-items: center;">
                                <el-icon style="margin-right: 5px;">
                                    <Link />
                                </el-icon>
                                {{scope.row.articleTitle}}
                            </a>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column prop="commentDate" label="评论时间" />
                <el-table-column label="操作" width="80">
                    <template #default="scope">
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" :icon="Delete"
                            icon-color="#626AEF" :title="'确定删除此评论吗？'" @confirm="deleteComment(scope.$index, scope.row)">
                            <template #reference>
                                <el-button size="small" type="danger" style="margin-bottom: 10px;">删除</el-button>
                            </template>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
            <!-- 分页 -->
            <el-pagination class="showPaging" background layout="prev, pager, next" :total="total" :page-size="pageSize"
                :disabled="disabled" @current-change="changePage" v-model:currentPage="currentPage"
                @size-change="handleSizeChange" :pager-count="3" :hide-on-single-page="true" />
            <el-pagination class="hidePaging" background layout="prev, pager, next, jumper" :total="total"
                :page-size="pageSize" :disabled="disabled" @current-change="changePage"
                v-model:currentPage="currentPage" @size-change="handleSizeChange" :pager-count="3"
                :hide-on-single-page="true" />
        </div>
    </el-card>
</template>

<script>
    import {
        ref,
        toRefs,
        reactive,
        onMounted,
        onUpdated,
        watch,
        nextTick, //组件刷新
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
     * @desc 加载获取数据
     */
    function loadData(state) {
        state.loading = true
        // 获取当前登录的用户信息
        const tokenStr = sessionStorage.getItem("userInfo");
        const userInfo = JSON.parse(tokenStr);
        state.userInfo = userInfo.data
        var params = {
            'currentPage': state.currentPage,
            'pageSize': state.pageSize,
            'userType': state.userInfo.userType,
            'userId': state.userInfo.userId
        }
        axios.get("/comment/page", {
            params
        }).then(res => {
            state.total = res.data.data.total;
            state.pageSize = res.data.data.size;
            state.currentPage = res.data.data.current;

            state.tableData = res.data.data.records

            state.loading = false
        })
    }

    /**
     * @desc 加载搜索数据
     */
    function searchContent(state) {
        state.loading = true
        // 获取当前登录的用户信息
        const tokenStr = sessionStorage.getItem("userInfo");
        const userInfo = JSON.parse(tokenStr);
        state.userInfo = userInfo.data
        var params = {
            'currentPage': state.currentPage,
            'pageSize': state.pageSize,
            'userType': state.userInfo.userType,
            'userId': state.userInfo.userId,
            'searchContent': state.searchValue
        }
        axios.get("/comment/page", {
            params
        }).then(res => {
            state.total = res.data.data.total;
            state.pageSize = res.data.data.size;
            state.currentPage = res.data.data.current;

            state.tableData = res.data.data.records

            state.loading = false
        })
    }
    export default {
        setup(props) {
            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });
            const state = reactive({
                // 当前登录的用户信息
                userInfo: null,
                // 搜索表单内容
                searchValue: "",
                // 表格全部信息
                tableData: [],
                total: 0, //总条数
                pageSize: 8, //每页显示行数
                currentPage: 1, //当前页码
                loading: false, // 数据加载
            })

            // 切换页面的执行事件，  val 当前页码
            const changePage = (val) => {
                state.currentPage = val;
                // 搜索框不为空时调用搜索内容
                if (state.searchValue !== "") {
                    searchContent(state)
                } else {
                    loadData(state);
                }
            };

            // 分页序号不乱
            const Nindex = (index) => {
                // 当前页数 - 1 * 每页数据条数 + 1
                const page = state.currentPage // 当前页码
                const pagesize = state.pageSize // 每页条数
                return index + 1 + (page - 1) * pagesize
            }

            // openSearch 打开搜索框 分辨率低时
            const openSearch = () => {
                ElMessageBox.prompt('请输入网站关键字进行搜索', '搜索', {
                    confirmButtonText: '搜索',
                    cancelButtonText: '取消',
                    inputErrorMessage: 'Invalid Email',
                }).then(({
                    value
                }) => {
                    state.auditValue = null
                    state.searchValue = value
                    searchContent(state)
                    if (value !== null) {
                        ElMessage({
                            type: 'success',
                            message: `关键字“${value}”搜索内容如下`,
                        })
                    } else {
                        ElMessage({
                            type: 'success',
                            message: `已为您清除搜索`,
                        })
                    }
                })
            }

            // 搜索
            const search = () => {
                state.auditValue = null
                searchContent(state)
                if (state.searchValue !== "") {
                    ElMessage({
                        type: 'success',
                        message: `关键字“${state.searchValue}”搜索内容如下`,
                    })
                } else {
                    ElMessage({
                        type: 'success',
                        message: `已为您清除搜索`,
                    })
                }
            }

            // 刷新按钮
            const refresh = () => {
                // 搜索表单内容
                state.searchValue = ""
                // 筛选下拉框内容
                state.auditValue = null
                // 更新数据
                loadData(state);

                ElMessage({
                    message: '刷新成功',
                    type: 'success',
                })
            }
            // 删除评论
            const deleteComment = (index, row) => {
                axios.delete("/comment/delete", {
                    params: {
                        commentId: row.commentId
                    }
                }).then(res => { // 删除成功
                    if (res.data.code == 0) {
                        //通知提示框
                        ElNotification({
                            title: '提示',
                            message: '已成功删除评论',
                            type: 'success',

                        });
                        //重新加载数据
                        if (state.searchValue !== "") {
                            // 搜索框不为空时调用搜索内容
                            searchContent(state)
                        }else {
                            // 加载全部数据
                            loadData(state);
                        }
                    } else {
                        ElMessage.error('评论删除失败')
                    }

                })
            }

            return {
                // 搜索、删除图标
                Search,
                Delete,
                ...toRefs(state),
                // 搜索事件
                search,
                openSearch,
                changePage,
                Nindex,
                refresh,
                deleteComment
            }
        }
    }
</script>

<style scoped>
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

    @media screen and (min-width: 992px) {
        .visible {
            display: none !important;
        }
    }

    @media screen and (max-width: 992px) {
        .visible {
            display: block !important;
        }
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