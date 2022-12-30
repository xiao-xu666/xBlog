<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <h3>
                    <el-icon style="margin-right: 10px;">
                        <Link />
                    </el-icon>链接管理
                </h3>
                <!-- 小于768px -->
                <div class="hidden-sm-and-down">
                    <el-row :gutter="10">
                        <el-col :span="10">
                            <el-input :prefix-icon="Search" v-model="searchValue" @keyup.enter.native="search"
                                placeholder="关键字搜索（回车）" />
                        </el-col>
                        <el-col :span="6">
                            <el-select v-model="auditValue" placeholder="审核筛选">
                                <el-option label="全部" value="0" />
                                <el-option label="待审核" value="1" />
                                <el-option label="已通过" value="2" />
                                <el-option label="已拒绝" value="3" />
                            </el-select>
                        </el-col>
                        <el-col :span="5">
                            <el-button plain style="width: 100%;" color="#2fa7b9" @click="addLink">添加链接</el-button>
                        </el-col>
                        <el-col :span="3" style="display: inline-flex;justify-content: center;align-items: center;">
                            <el-icon style="font-size: 20px;color: #b3b6bc;" @click="refresh">
                                <Refresh />
                            </el-icon>
                        </el-col>
                    </el-row>
                </div>

                <!-- 大于等于768px  -->
                <div class="visible">
                    <p style="display: inline-flex;justify-content: center;align-items: center;">
                        <el-dropdown style="line-height:0px;" :hide-on-click="false" trigger="click">
                            <span class="el-dropdown-link" style="color:#2fa7b9;font-size: 16px;margin-right: 15px;"
                                title="菜单">
                                <el-icon>
                                    <Grid />
                                </el-icon>
                            </span>
                            <template #dropdown>
                                <el-dropdown-menu class="visible">
                                    <el-dropdown-item>
                                        <el-select v-model="auditValue" placeholder="审核筛选">
                                            <el-option label="全部" value="0" />
                                            <el-option label="待审核" value="1" />
                                            <el-option label="已通过" value="2" />
                                            <el-option label="已拒绝" value="3" />
                                        </el-select>
                                    </el-dropdown-item>
                                    <el-dropdown-item>
                                        <el-button plain style="width: 100%;" color="#2fa7b9" @click="openSearch">搜索内容
                                        </el-button>
                                    </el-dropdown-item>
                                    <el-dropdown-item>
                                        <el-button plain style="width: 100%;" color="#2fa7b9" @click="addLink">添加链接
                                        </el-button>
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                        <el-icon @click="refresh">
                            <Refresh />
                        </el-icon>
                    </p>
                </div>


            </div>
        </template>
        <div>
            <el-table v-loading="loading" element-loading-text="玩命加载中..." :data="tableData"
                style="width: 100%;text-align: center;" :cell-style="{textAlign:'center'}"
                :header-cell-style="{fontSize: '15px',background:'#2fa7b9','color':'white',textAlign:'center' }">
                <!-- <el-table-column prop="urlId" label="编号" width="100" /> -->
                <el-table-column label="序号" width="100" type="index" :index="Nindex" />
                <!-- <el-table-column prop="urlName" label="网站名称" /> -->
                <el-table-column label="名称">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.urlName" placement="top" effect="light">
                            <span class="highlight">{{scope.row.urlName}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="urlAddres" label="网站地址" /> -->
                <el-table-column label="地址">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.urlAddres" placement="top" effect="light">
                            <span>{{scope.row.urlAddres}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="urlType" label="网站类型" /> -->
                <el-table-column label="类型">
                    <template #default="scope">
                        <div v-if="scope.row.urlType == 1">推荐网站</div>
                        <div v-if="scope.row.urlType == 2">友情链接</div>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="urlReferral" label="网站介绍" /> -->
                <!-- <el-table-column prop="urlType" label="网站缩略图" /> -->
                <el-table-column label="缩略图">
                    <template #default="scope">
                        <el-image style="width:70px" :src="scope.row.urlLitimg" fit="cover"
                            preview-teleported="true" :preview-src-list="[scope.row.urlLitimg]"
                            :title="scope.row.urlName"></el-image>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="webmasterEmail" label="站长邮箱" /> -->
                <el-table-column label="站长邮箱">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.webmasterEmail" placement="top" effect="light">
                            <span>{{scope.row.webmasterEmail}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="urlPass" label="审核" /> -->
                <el-table-column label="审核状态">
                    <template #default="scope">
                        <div v-if="scope.row.urlPass == 1" style="color: #E6A23C;">待审核</div>
                        <div v-if="scope.row.urlPass == 2" style="color: #67C23A;">已通过</div>
                        <div v-if="scope.row.urlPass == 3" style="color: #F56C6C;">已拒绝</div>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button size="small" @click="auditLink(scope.$index, scope.row)"
                            style="margin: 0 0 10px 10px;">审核</el-button>
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" :icon="Delete"
                            icon-color="#626AEF" :title="'确定删除“'+scope.row.urlName+'”链接吗？'"
                            @confirm="handleDelete(scope.$index, scope.row)">
                            <template #reference>
                                <el-button size="small" type="danger" style="margin-bottom: 10px;">删除</el-button>
                            </template>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
            <!--
			    total 总行数
			    page-size	每页显示条目个数
			    current-change 改变页码时触发
			    currentPage:当前页码
			 -->
            <el-pagination class="showPaging" background layout="prev, pager, next" :total="total" :page-size="pageSize"
                :disabled="disabled" @current-change="changePage" v-model:currentPage="currentPage"
                @size-change="handleSizeChange" :pager-count="3" :hide-on-single-page="true" />
            <el-pagination class="hidePaging" background layout="prev, pager, next, jumper" :total="total"
                :page-size="pageSize" :disabled="disabled" @current-change="changePage"
                v-model:currentPage="currentPage" @size-change="handleSizeChange" :pager-count="3"
                :hide-on-single-page="true" />
        </div>
    </el-card>
    <!-- 添加链接 -->
    <AddLink :addLinkDialogFormVisible="dialogFormVisible.addLinkDialogFormVisible"
        @onAddLinkCloseDialog="addLinkCloseDialogVisible"></AddLink>
    <!-- 审核链接 -->
    <AuditLink :auditLinkInfo="auditLinkInfo" :auditLinkDialogFormVisible="dialogFormVisible.auditLinkDialogFormVisible"
        @onAuditLinkCloseDialog="auditLinkCloseDialogVisible"></AuditLink>
</template>

<script>
    // 添加链接弹窗
    import AddLink from './addLink/index.vue';
    // 审核链接弹窗
    import AuditLink from './auditLink/index.vue'
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
        var params = {
            'currentPage': state.currentPage,
            'pageSize': state.pageSize
        }
        axios.get("/url/showUrlPage", {
            params
        }).then(res => {
            //JSON.parse 将从后台得到的数据转换为标准JSON格式
            //前台展示的是需要数组，JSON.parse转换后的数据，element-plus可以解析
            state.tableData = res.data.data.records;
            state.total = res.data.data.total;
            state.pageSize = res.data.data.size;
            state.currentPage = res.data.data.current;
            state.loading = false
        })
    }

    /**
     * 搜索
     */
    function searchContent(state) {
        state.loading = true;
        const params = {
            'currentPage': state.currentPage,
            'pageSize': state.pageSize,
            'searchContent': state.searchValue,
            'auditContent': state.auditValue == 0 ? 0 : state.auditValue
        }
        axios.get("/url/showUrlPage", {
            params
        }).then(res => {
            state.tableData = res.data.data.records;
            state.total = res.data.data.total;
            state.pageSize = res.data.data.size;
            state.currentPage = res.data.data.current;

            state.loading = false;
        })
    }
    /**
     * 审核筛选
     */
    function auditScreen(state) {
        state.loading = true;
        const params = {
            'currentPage': state.currentPage,
            'pageSize': state.pageSize,
            'auditContent': state.auditValue == 0 ? "" : state.auditValue
        }
        axios.get("/url/showUrlPage", {
            params
        }).then(res => {
            state.tableData = res.data.data.records;
            state.total = res.data.data.total;
            state.pageSize = res.data.data.size;
            state.currentPage = res.data.data.current;

            state.loading = false;
        })
    }
    export default {
        components: {
            AddLink,
            AuditLink
        },
        setup(props) {
            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });
            // 图片 根url
            const url = process.env.VUE_APP_URL;
            const state = reactive({
                // 搜索表单内容
                searchValue: "",
                // 筛选下拉框内容
                auditValue: null,
                // 表格全部信息
                tableData: [],
                dialogFormVisible: {
                    // 添加链接弹窗状态
                    addLinkDialogFormVisible: false,
                    // 审核链接弹窗状态
                    auditLinkDialogFormVisible: false,
                    a: 1
                },
                total: 0, //总条数
                pageSize: 6, //每页显示行数
                currentPage: 1, //当前页码
                // 当前审核链接的信息
                auditLinkInfo: {},
                loading: false
            })

            // 添加链接
            const addLink = () => {
                state.dialogFormVisible.addLinkDialogFormVisible = true;
            }

            // 审核链接
            const auditLink = (index, row) => {
                state.dialogFormVisible.auditLinkDialogFormVisible = true;
                state.auditLinkInfo = row;
            }

            // 添加链接关闭窗口
            const addLinkCloseDialogVisible = (visible, data) => {
                state.dialogFormVisible.addLinkDialogFormVisible = visible;
                if (data != undefined) {
                    //重新加载数据
                    if (state.searchValue !== "") {
                        // 搜索框不为空时调用搜索内容
                        searchContent(state)
                    } else if (state.auditValue !== 0 && state.auditValue != null) {
                        // 审核筛选
                        auditScreen(state)
                    } else {
                        // 加载全部数据
                        loadData(state);
                    }
                }
            }

            // 审核链接关闭窗口
            const auditLinkCloseDialogVisible = (visible, data) => {
                state.dialogFormVisible.auditLinkDialogFormVisible = visible;
                if (data != undefined) {
                    //重新加载数据
                    if (state.searchValue !== "") {
                        // 搜索框不为空时调用搜索内容
                        searchContent(state)
                    } else if (state.auditValue !== 0 && state.auditValue != null) {
                        // 审核筛选
                        auditScreen(state)
                    } else {
                        // 加载全部数据
                        loadData(state);
                    }
                }
            }

            // 切换页面的执行事件，  val 当前页码
            const changePage = (val) => {
                state.currentPage = val;
                //重新加载数据
                if (state.searchValue !== "") {
                    // 搜索框不为空时调用搜索内容
                    searchContent(state)
                } else if (state.auditValue !== 0 && state.auditValue != null) {
                    // 审核筛选
                    auditScreen(state)
                } else {
                    // 加载全部数据
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
                    state.auditValue = ""
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
                state.auditValue = ""
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

            // 监听下拉框内容的改变
            watch(() => state.auditValue, (val, preVal) => {
                if (val) {
                    state.searchValue = "";
                    auditScreen(state)
                }
            })

            // 增加删除事件
            function useDelete(state) {
                const handleDelete = (index, row) => {
                    //确定删除
                    axios.delete("/url/delete", {
                        params: {
                            urlId: row.urlId
                        }
                    }).then(res => {
                        if (res.data.code == "0") {
                            //通知提示框
                            ElNotification({
                                title: '成功',
                                message: '已成功删除“' + row.urlName + '”链接',
                                type: 'success',

                            });
                            //重新加载数据
                            if (state.searchValue !== "") {
                                // 搜索框不为空时调用搜索内容
                                searchContent(state)
                            } else if (state.auditValue !== 0 && state.auditValue != null) {
                                // 审核筛选
                                auditScreen(state)
                            } else {
                                // 加载全部数据
                                loadData(state);
                            }
                        } else {
                            ElNotification({
                                title: '失败',
                                message: '链接删除失败',
                                type: 'error'
                            })
                        }
                    })
                };
                return {
                    handleDelete,
                }

            };
            return {
                // 搜索、删除图标
                Search,
                Delete,
                ...toRefs(state),
                // 搜索事件
                search,
                openSearch,
                addLink,
                auditLink,
                addLinkCloseDialogVisible,
                auditLinkCloseDialogVisible,
                changePage,
                ...useDelete(state),
                Nindex,
                url,
                refresh
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