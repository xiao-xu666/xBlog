<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <h3>
                    <el-icon style="margin-right: 10px;">
                        <MagicStick />
                    </el-icon>用户管理
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
            <el-table v-loading="loading" element-loading-text="玩命加载中..." :data="tableData"
                style="width: 100%;text-align: center;" :cell-style="{textAlign:'center'}"
                :header-cell-style="{fontSize: '15px',background:'#2fa7b9','color':'white',textAlign:'center' }">
                <el-table-column label="序号" width="100" type="index" :index="Nindex" />
                <el-table-column label="用户名">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.userName" placement="top" effect="light">
                            <span class="highlight">{{scope.row.userName}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="邮箱">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.userEmail" placement="top" effect="light">
                            <span>{{scope.row.userEmail}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="类型">
                    <template #default="scope">
                        <div v-if="scope.row.userType == 0">管理员</div>
                        <div v-if="scope.row.userType == 1">普通用户</div>
                    </template>
                </el-table-column>
                <el-table-column label="头像">
                    <template #default="scope">
                        <!-- 用户没有设置头像，则为系统默认头像 -->
                        <el-image v-if="scope.row.userIcon" style="width:50px;border-radius: 50px;"
                            :src="scope.row.userIcon" fit="cover" preview-teleported="true"
                            :preview-src-list="[scope.row.userIcon]" :title="scope.row.urlName"></el-image>
                        <el-image v-else style="width:50px" :src="require('../../../../assets/default_avatar.png')"
                            fit="cover" preview-teleported="true"
                            :preview-src-list="[require('../../../../assets/default_avatar.png')]"
                            :title="scope.row.urlName"></el-image>
                    </template>
                </el-table-column>
                <el-table-column label="签名">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.userSignature" placement="top" effect="light">
                            <span v-if="scope.row.userSignature">{{scope.row.userSignature}}</span>
                            <span v-else>无</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="注册时间">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.userRegdate" placement="top" effect="light">
                            <span>{{scope.row.userRegdate}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="状态">
                    <template #default="scope">
                        <span v-if="scope.row.state == 0">正常</span>
                        <span v-if="scope.row.state == 1">冻结</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button size="small" @click="modifyUser(scope.$index, scope.row)"
                            style="margin: 0 0 10px 10px;">编辑</el-button>
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" :icon="Delete"
                            icon-color="#626AEF" :title="'确定删除用户名为“'+scope.row.userName+'”的用户吗？'"
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
    <!-- <AddLink :addLinkDialogFormVisible="dialogFormVisible.addLinkDialogFormVisible"
        @onAddLinkCloseDialog="addLinkCloseDialogVisible"></AddLink> -->
    <!-- 修改用户 -->
    <ModifyUser :modifyUserDialogFormVisible="modifyUserDialogFormVisible" :userInfo="userInfo" @onModifyUserCloseDialog="modifyUserCloseDialogVisible"></ModifyUser>
</template>

<script>
    // 修改用户弹窗
    import ModifyUser from './modifyUser/index.vue';
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

    function loadData(state) {
        state.loading = true
        const params = {
            'currentPage': state.currentPage,
            'pageSize': state.pageSize
        }
        axios.get("/user/showUserPage", {
            params
        }).then(res => {
            // 先清空数据
            state.tableData = [];
            res.data.data.records.forEach(element => {
                // 图片 根url
                const url = process.env.VUE_APP_URL;
                // 缩略图 判断是点击上传的还是，网络图片
                if (element.userIcon != null) {
                    if (!element.userIcon.includes('http')) {
                        element.userIcon = url + element.userIcon
                    }
                }
                // 添加
                state.tableData.push(element)
            })
            //JSON.parse 将从后台得到的数据转换为标准JSON格式
            //前台展示的是需要数组，JSON.parse转换后的数据，element-plus可以解析
            state.total = res.data.data.total;
            state.pageSize = res.data.data.size;
            state.currentPage = res.data.data.current;
            state.loading = false
        })
    }
    /**
     * @desc 加载搜索数据
     */
    function searchContent(state) {
        state.loading = true
        const params = {
            'currentPage': state.currentPage,
            'pageSize': state.pageSize,
            'searchContent': state.searchValue
        }
        axios.get("/user/showUserPage", {
            params
        }).then(res => {
            // 先清空数据
            state.tableData = [];
            res.data.data.records.forEach(element => {
                // 图片 根url
                const url = process.env.VUE_APP_URL;
                // 缩略图 判断是点击上传的还是，网络图片
                if (element.userIcon != null) {
                    if (!element.userIcon.includes('http')) {
                        element.userIcon = url + element.userIcon
                    }
                }
                // 添加
                state.tableData.push(element)
            })
            //JSON.parse 将从后台得到的数据转换为标准JSON格式
            //前台展示的是需要数组，JSON.parse转换后的数据，element-plus可以解析
            state.total = res.data.data.total;
            state.pageSize = res.data.data.size;
            state.currentPage = res.data.data.current;
            state.loading = false
        })
    }
    export default {
        components: {
            ModifyUser
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
                // 表格全部信息
                tableData: [],
                // 编辑信息弹窗状态
                modifyUserDialogFormVisible: false,
                // 当前点击的编辑信息
                userInfo:null,

                total: 0, //总条数
                pageSize: 6, //每页显示行数
                currentPage: 1, //当前页码
                loading: false, // 数据加载
            })

            // 切换页面的执行事件，  val 当前页码
            const changePage = (val) => {
                state.currentPage = val;
                //重新加载数据
                if (state.searchValue !== "") {
                    // 搜索框不为空时调用搜索内容
                    searchContent(state)
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
                ElMessageBox.prompt('请输入关键字进行搜索', '搜索', {
                    confirmButtonText: '搜索',
                    cancelButtonText: '取消',
                    inputErrorMessage: 'Invalid Email',
                }).then(({
                    value
                }) => {
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

            // 修改弹窗
            const modifyUser = (index, row) => {
                state.modifyUserDialogFormVisible = true;
                state.userInfo = row;
            }

            // 审核修改弹窗
            const modifyUserCloseDialogVisible = (visible, data) => {
                state.modifyUserDialogFormVisible = visible;
                if (data != undefined) {
                    //重新加载数据
                    if (state.searchValue !== "") {
                        // 搜索框不为空时调用搜索内容
                        searchContent(state)
                    } else {
                        // 加载全部数据
                        loadData(state);
                    }
                }
            }

            // 增加删除事件
            function useDelete(state) {
                const handleDelete = (index, row) => {
                    //确定删除
                    axios.delete("/user/delete", {
                        params: {
                            userId: row.userId
                        }
                    }).then(res => {
                        if (res.data.code == "0") {
                            //通知提示框
                            ElNotification({
                                title: '成功',
                                message: '已成功删除用户名为“' + row.userName + '的用户',
                                type: 'success',

                            });
                            //重新加载数据
                            if (state.searchValue !== "") {
                                // 搜索框不为空时调用搜索内容
                                searchContent(state)
                            } else {
                                // 加载全部数据
                                loadData(state);
                            }
                        } else {
                            ElNotification({
                                title: '失败',
                                message: '用户删除失败',
                                type: 'error',
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
                changePage,
                ...useDelete(state),
                Nindex,
                modifyUser,
                url,
                refresh,
                modifyUserCloseDialogVisible
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