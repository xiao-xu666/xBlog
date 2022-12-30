<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <h3>
                    <el-icon style="margin-right: 10px;">
                        <Edit />
                    </el-icon>
                    文章管理
                </h3>
                <!-- 小于768px -->
                <div class="hidden-sm-and-down">
                    <el-row :gutter="30">
                        <el-col :span="3">

                        </el-col>
                        <el-col :span="11">
                            <el-input :prefix-icon="Search" v-model="searchValue" @keyup.enter.native="search"
                                placeholder="关键字搜索(回车)" />
                        </el-col>
                        <el-col :span="7">
                            <el-select v-model="auditValue" placeholder="审核筛选">
                                <el-option label="全部" value="0" />
                                <el-option label="待审核" value="1" />
                                <el-option label="已通过" value="2" />
                                <el-option label="已拒绝" value="3" />
                            </el-select>
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
                            <span class="el-dropdown-link" style="color:#2fa7b9;font-size: 25xp;" title="菜单">
                                <span class="el-dropdown-link" style="color:#2fa7b9;font-size: 16px;margin-right: 15px;"
                                    title="菜单">
                                    <el-icon>
                                        <Grid />
                                    </el-icon>
                                </span>
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
                                        <el-button plain style="width: 100%;" color="#2fa7b9" @click="openSearch">
                                            搜索内容
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
            <el-table element-loading-text="玩命加载中..." v-loading="loading" :data="tableData"
                style="width: 100%;text-align: center;" :cell-style="{textAlign:'center'}"
                :row-class-name="rowClassName"
                :header-cell-style="{fontSize: '15px',background:'#2fa7b9','color':'white',textAlign:'center' }">
                <!-- <el-table-column prop="articleId" label="编号" width="100"  /> -->
                <el-table-column label="序号" width="100" type="index" :index="Nindex" />
                <!-- <el-table-column prop="userName" class="highlight" label="作者" /> -->
                <el-table-column label="作者">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.userName" placement="top" effect="light">
                            <span class="highlight">{{scope.row.userName}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="标题">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.articleTitle" placement="top" effect="light">
                            <span class="highlight">{{scope.row.articleTitle}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="articleTitle" label="文章标题" class="articleTitle" /> -->
                <el-table-column label="缩略图">
                    <template #default="scope">
                        <el-image v-if="scope.row.articleImgLitimg" style="width:70px" :src="scope.row.articleImgLitimg"
                            fit="cover" preview-teleported="true" :preview-src-list="[scope.row.articleImgLitimg]">
                        </el-image>
                        <span v-if="!scope.row.articleImgLitimg">无</span>
                    </template>
                </el-table-column>
                <!-- <el-table-column prop="articleClassifyName" class="highlight" label="分类" /> -->
                <el-table-column label="分类">
                    <template #default="scope">
                        <el-tooltip :content="scope.row.articleClassifyName" placement="top" effect="light">
                            <span class="highlight">{{scope.row.articleClassifyName}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="文章状态">
                    <template #default="scope">
                        <div v-if="scope.row.articleState == 1">所有人可见</div>
                        <div v-if="scope.row.articleState == 2">仅自己可见</div>
                    </template>
                </el-table-column>
                <el-table-column label="评论状态">
                    <template #default="scope">
                        <div v-if="scope.row.commentState == 1">开启</div>
                        <div v-if="scope.row.commentState == 2">关闭</div>
                    </template>
                </el-table-column>
                <el-table-column prop="click" label="浏览量" />
                <el-table-column label="审核状态">
                    <template #default="scope">
                        <div v-if="scope.row.articlePass == 1" style="color: #E6A23C;">待审核</div>
                        <div v-if="scope.row.articlePass == 2" style="color: #67C23A;">已通过</div>
                        <div v-if="scope.row.articlePass == 3" style="color: #F56C6C;">已拒绝</div>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                    <template #default="scope">
                        <el-button size="small" @click="openPreview(scope.$index, scope.row)"
                            style="margin: 0 0 10px 10px;">预览</el-button>
                        <el-dropdown trigger="click">
                            <span class="el-dropdown-link">
                                <el-button size="small" v-if="userInfo.userType==0" style="margin: 0 0 10px 10px;">审核
                                </el-button>
                            </span>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item @click="audit(scope.row,1)">
                                        <el-icon>
                                            <Finished />
                                        </el-icon>待审核
                                    </el-dropdown-item>
                                    <el-dropdown-item @click="audit(scope.row,2)">
                                        <el-icon><Select /></el-icon>通过
                                    </el-dropdown-item>
                                    <el-dropdown-item @click="audit(scope.row,3)">
                                        <el-icon>
                                            <CloseBold />
                                        </el-icon>拒绝
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                        <el-button size="small" @click="openCompile(scope.$index, scope.row)"
                            style="margin: 0 0 10px 10px;">编辑</el-button>
                        <el-popconfirm confirm-button-text="确定" cancel-button-text="取消" :icon="Delete"
                            icon-color="#626AEF" :title="'确定删除“'+scope.row.articleTitle+'”文章吗？'"
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
    <!-- 预览文章 -->
    <PreviewArticle :previewArticleDialogFormVisible="previewArticleDialogFormVisible" :valueHtml="currentArticles"
        @onPreviewArticleCloseDialog="previewArticleCloseDialog"></PreviewArticle>
    <!-- 编辑文章 -->
    <CompileArticle :compileArticleDialogFormVisible="compileArticleDialogFormVisible" :valueHtml="currentArticles"
        @onCompileArticleCloseDialog="compileArticleCloseDialog">

    </CompileArticle>
</template>

<script>
    // 预览文章弹窗
    import PreviewArticle from './previewArticle/index.vue'
    // 编辑文字弹窗
    import CompileArticle from './compileArticle/index.vue'
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
     * @desc 格式化日期字符串
     * @param { Nubmer} - 时间戳
     * @returns { String } 格式化后的日期字符串
     */
    function formatDate(timestamp) {
        // 补全为13位
        let arrTimestamp = (timestamp + '').split('');
        for (let start = 0; start < 13; start++) {
            if (!arrTimestamp[start]) {
                arrTimestamp[start] = '0';
            }
        }
        timestamp = arrTimestamp.join('') * 1;
        let minute = 1000 * 60;
        let hour = minute * 60;
        let day = hour * 24;
        let month = day * 30;
        let now = new Date().getTime();
        let diffValue = now - timestamp;
        // 如果本地时间反而小于变量时间
        if (diffValue < 0) {
            return '不久前';
        }
        // 计算差异时间的量级
        let monthC = diffValue / month;
        let weekC = diffValue / (7 * day);
        let dayC = diffValue / day;
        let hourC = diffValue / hour;
        let minC = diffValue / minute;
        // 数值补0方法
        let zero = function (value) {
            if (value < 10) {
                return '0' + value;
            }
            return value;
        };
        // 使用
        if (monthC > 4) {
            // 超过1年，直接显示年月日
            return (function () {
                let date = new Date(timestamp);
                return date.getFullYear() + '年' + zero(date.getMonth() + 1) + '月' + zero(date.getDate()) + '日';
            })();
        } else if (monthC >= 1) {
            return parseInt(monthC) + '月前';
        } else if (weekC >= 1) {
            return parseInt(weekC) + '周前';
        } else if (dayC >= 1) {
            return parseInt(dayC) + '天前';
        } else if (hourC >= 1) {
            return parseInt(hourC) + '小时前';
        } else if (minC >= 1) {
            return parseInt(minC) + '分钟前';
        }
        return '刚刚';
    }

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
        axios.get("/article/page", {
            params
        }).then(res => {
            // 先清空数据
            state.tableData = [];
            res.data.data.records.forEach(element => {
                // 时间戳格式化
                element.publishTime = formatDate(element.publishTime);
                // 文章中图片改为动态的
                // if (element.articleContent.includes('http://localhost:80/')) {
                //     element.articleContent = element.articleContent.replaceAll('http://localhost:80/',
                //         process.env
                //         .VUE_APP_URL);
                // } else if (element.articleContent.includes('http://localhost/')) {
                //     element.articleContent = element.articleContent.replaceAll('http://localhost/',
                //         process.env
                //         .VUE_APP_URL);
                // } else if (element.articleContent.includes('http://192.168.195.68:80/')) {
                //     element.articleContent = element.articleContent.replaceAll(
                //         'http://192.168.195.68:80/', process.env
                //         .VUE_APP_URL);
                // } else if (element.articleContent.includes('http://192.168.195.68/')) {
                //     element.articleContent = element.articleContent.replaceAll('http://192.168.195.68/',
                //         process.env
                //         .VUE_APP_URL);
                // }
                // 图片 根url
                const url = process.env.VUE_APP_URL;
                // 缩略图 判断是点击上传的还是，网络图片
                if (element.articleImgLitimg != "" && !element.articleImgLitimg.includes('http')) {
                    element.articleImgLitimg = url + element.articleImgLitimg
                }

                // 添加
                state.tableData.push(element)
            });
            //JSON.parse 将从后台得到的数据转换为标准JSON格式
            //前台展示的是需要数组，JSON.parse转换后的数据，element-plus可以解析
            // state.tableData = res.data.data.list;
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

        state.loading = true;

        var params = {
            'currentPage': state.currentPage,
            'pageSize': state.pageSize,
            'userType': state.userInfo.userType,
            'userId': state.userInfo.userId,
            'articleTitle': state.searchValue
        }
        axios.get("/article/page", {
            params
        }).then(res => {
            // 先清空数据
            state.tableData = [];
            res.data.data.records.forEach(element => {
                // 时间戳格式化
                element.publishTime = formatDate(element.publishTime);
                // 文章中图片改为动态的
                if (element.articleContent.includes('http://localhost:80/')) {
                    element.articleContent = element.articleContent.replaceAll(
                        'http://localhost:80/',
                        process.env
                        .VUE_APP_URL);
                } else if (element.articleContent.includes('http://localhost/')) {
                    element.articleContent = element.articleContent.replaceAll(
                        'http://localhost/',
                        process.env
                        .VUE_APP_URL);
                } else if (element.articleContent.includes('http://192.168.195.68:80/')) {
                    element.articleContent = element.articleContent.replaceAll(
                        'http://192.168.195.68:80/', process.env
                        .VUE_APP_URL);
                } else if (element.articleContent.includes('http://192.168.195.68/')) {
                    element.articleContent = element.articleContent.replaceAll(
                        'http://192.168.195.68/',
                        process.env
                        .VUE_APP_URL);
                }
                // 图片 根url
                const url = process.env.VUE_APP_URL;
                // 缩略图 判断是点击上传的还是，网络图片
                if (element.articleImgLitimg != "" && !element.articleImgLitimg.includes(
                        'http')) {
                    element.articleImgLitimg = url + element.articleImgLitimg
                }

                // 添加
                state.tableData.push(element)
            });
            //JSON.parse 将从后台得到的数据转换为标准JSON格式
            //前台展示的是需要数组，JSON.parse转换后的数据，element-plus可以解析
            // state.tableData = res.data.data.list;
            state.total = res.data.data.total;
            state.pageSize = res.data.data.size;
            state.currentPage = res.data.data.current;

            state.loading = false;
        })
    }

    /**
     * @desc 加载审核筛选数据
     */
    function auditScreen(state) {
        state.loading = true;
        // 待审核、已拒绝
        var params = {
            'currentPage': state.currentPage,
            'pageSize': state.pageSize,
            'userType': state.userInfo.userType,
            'userId': state.userInfo.userId,
            'articlePass': state.auditValue
        }
        axios.get("/article/page", {
            params
        }).then(res => {
            // 先清空数据
            state.tableData = [];
            res.data.data.records.forEach(element => {
                // 时间戳格式化
                element.publishTime = formatDate(element.publishTime);
                // 图片 根url
                const url = process.env.VUE_APP_URL;
                // 缩略图 判断是点击上传的还是，网络图片
                if (element.articleImgLitimg != "" && !element.articleImgLitimg
                    .includes('http') && !element.articleImgLitimg
                    .includes('https')) {
                    element.articleImgLitimg = url + element.articleImgLitimg
                }
                // 添加
                state.tableData.push(element)
            });
            //JSON.parse 将从后台得到的数据转换为标准JSON格式
            //前台展示的是需要数组，JSON.parse转换后的数据，element-plus可以解析
            // state.tableData = res.data.data.list;
            state.total = res.data.data.total;
            state.pageSize = res.data.data.pageSize;
            state.currentPage = res.data.data.currentPage;

            state.loading = false;
        })
    }
    export default {
        components: {
            PreviewArticle,
            CompileArticle
        },
        setup(props) {
            //挂载后加载数据
            onMounted(() => {
                loadData(state);
            });

            // 图片 根url
            const url = process.env.VUE_APP_URL;
            const state = reactive({
                // 当前登录的用户信息
                userInfo: null,
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
                pageSize: 5, //每页显示行数
                currentPage: 1, //当前页码
                // 当前审核链接的信息
                auditLinkInfo: {},
                // 预览文章弹窗状态
                previewArticleDialogFormVisible: false,
                // 编辑文章弹窗状态
                compileArticleDialogFormVisible: false,
                // 当前点击预览的文章信息
                currentArticles: null,
                loading: false, // 数据加载
            })

            // 审核链接
            const auditLink = (index, row) => {
                state.dialogFormVisible.auditLinkDialogFormVisible = true;
                state.auditLinkInfo = row;
            }

            // 审核链接关闭窗口
            const auditLinkCloseDialogVisible = (visible, data) => {
                state.dialogFormVisible.auditLinkDialogFormVisible = visible;
                if (data != undefined) {
                    //重新获取数据
                    loadData(state);
                }
            }

            // 打开预览文章窗口
            const openPreview = (index, row) => {
                state.previewArticleDialogFormVisible = true;
                state.currentArticles = row
            }

            // 关闭预览文章窗口
            const previewArticleCloseDialog = (visible) => {
                state.previewArticleDialogFormVisible = visible;
            }

            // 打开编辑文章窗口
            const openCompile = (index, row) => {
                state.compileArticleDialogFormVisible = true;
                state.currentArticles = row
            }

            // 关闭编辑文章窗口
            const compileArticleCloseDialog = (visible, data) => {
                state.compileArticleDialogFormVisible = visible;
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

            // 切换页面的执行事件，  val 当前页码
            const changePage = (val) => {
                state.currentPage = val;
                // 搜索框不为空时调用搜索内容
                if (state.searchValue !== "") {
                    searchContent(state)
                } else if (state.auditValue !== 0 && state.auditValue != null) {
                    // 审核筛选
                    auditScreen(state)
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

            // 监听下拉框内容的改变  审核是阿悬
            watch(() => state.auditValue, (val, preVal) => {
                if (val) {
                    state.searchValue = ""
                    if (state.auditValue == 0) {
                        // 全部
                        loadData(state);
                    } else {
                        auditScreen(state)
                    }
                }
            })

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


            // 文章审核  row 当前点击的文章信息  articlePass审核状态 1 2 3   
            const audit = (row, articlePass) => {
                if (row.articlePass !== articlePass) {
                    var params = {
                        articleId: row.articleId,
                        articlePass: articlePass
                    }
                    axios.get("/article/updateArticlePass", {
                        params
                    }).then(res => {
                        if (res.data.code == "0") {
                            //通知提示框
                            ElNotification({
                                title: '提示',
                                message: '审核成功',
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
                            ElMessage.error('审核失败！')
                        }
                    })
                } else {
                    ElNotification({
                        title: '提示',
                        message: '小样，好像没有任何改变哦！',
                        type: 'success',
                    });
                }

            }

            // 删除文章事件
            function useDelete(state) {
                const handleDelete = (index, row) => {
                    //确定删除
                    axios.delete("/article/delete", {
                        params: {
                            articleId: row.articleId
                        }
                    }).then(res => {
                        if (res.data.code == "0") {
                            // 同时删除文章对应的评论   根据文章id删除
                            axios.delete("/comment/deleteByArticleId", {
                                params: {
                                    articleId: row.articleId
                                }
                            })
                            // 同时减少分类对应的文章数量
                            axios.get("/removeArticleNumberByClassifyId", {
                                params: {
                                    classifyId: row.articleClassifyId
                                }
                            })
                            //通知提示框
                            ElNotification({
                                title: '提示',
                                message: '已成功删除“' + row.articleTitle + '”文章',
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
                            ElMessage.error('文章删除失败')
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
                auditLink,
                auditLinkCloseDialogVisible,
                openPreview,
                previewArticleCloseDialog,
                openCompile,
                compileArticleCloseDialog,
                changePage,
                ...useDelete(state),
                Nindex,
                url,
                refresh,
                audit
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