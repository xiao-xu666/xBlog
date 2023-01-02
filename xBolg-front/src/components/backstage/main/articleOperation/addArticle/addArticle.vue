<template>
  <div class="addArticle_box">
    <h3>
      <el-icon style="margin-right: 10px">
        <EditPen />
      </el-icon>
      新建文章
    </h3>
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="24" :lg="17" :xl="17">
        <div class="write_abstract">
          <h4>标题</h4>
          <el-input
            v-model="write.titleValue"
            placeholder="请输入文章标题"
            maxlength="50"
            show-word-limit
            size="large"
            clearable
          />
          <p>标示文章、作品内容的简明语句，填写醒目的</p>
        </div>
        <div class="write_abstract">
          <!-- 富文本编辑器 -->
          <Toolbar
            :editor="editorRef"
            :defaultConfig="toolbarConfig"
            :mode="mode"
          />
          <Editor
            style="height: 420px; overflow-y: hidden"
            v-model="valueHtml"
            :defaultConfig="editorConfig"
            :mode="mode"
            @onCreated="handleCreated"
            @change="change"
            class="article"
          />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :md="24" :lg="7" :xl="7">
        <div class="write_abstract">
          <h4>摘要</h4>
          <el-input
            v-model="write.abstractValue"
            :autosize="{ minRows: 3, maxRows: 5 }"
            maxlength="200"
            show-word-limit
            type="textarea"
            placeholder="请简单概述文章内容"
            clearable
          />
          <p>筛选文章主要内容，让读者初步了解文章</p>
        </div>
        <div class="write_abstract">
          <h4>缩略图</h4>
          <el-input
            size="large"
            v-model="write.thumbnailValue"
            placeholder="请点击上传图片或手动输入图片地址"
          >
            <template #append>
              <el-upload
                ref="uploadEle"
                class="upload-demo"
                :action="uploadURL"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeUploadFunction"
                :show-file-list="false"
              >
                <template #trigger>
                  <el-button>上传图片</el-button>
                </template>
              </el-upload>
            </template>
          </el-input>
          <p>注：点击上传的图片地址由系统生成，修改会导致图片失效</p>
        </div>
        <div class="write_abstract">
          <h4>分类</h4>
          <el-select
            v-model="write.classifyValue"
            placeholder="请选择文章分类"
            size="large"
          >
            <el-option
              v-for="item in classifyInfo"
              :label="item.classifyName"
              :value="item.classifyId"
            />
          </el-select>
          <p>选择分类可让读者更便捷的查找文章</p>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="write_abstract">
              <h4>状态</h4>
              <el-select
                v-model="write.stateValue"
                placeholder="文章状态"
                size="large"
              >
                <el-option label="公开" value="1" />
                <el-option label="私密" value="2" />
              </el-select>
              <p>私密可视为草稿</p>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="write_abstract">
              <h4>评论</h4>
              <el-select
                v-model="write.commentValue"
                placeholder="评论是否开启"
                size="large"
              >
                <el-option label="开启" value="1" />
                <el-option label="关闭" value="2" />
              </el-select>
              <p>开启评论，开启新世界</p>
            </div>
          </el-col>
        </el-row>
        <div
          class="write_abstract"
          style="text-align: center; background: none"
        >
          <el-button text @click="previewArticleDialogFormVisible = true"
            >预览</el-button
          >
          <el-button style="width: 50%" plain color="#2fa7b9" @click="submitAdd"
            >提交</el-button
          >
          <p>注：普通用户提交文章需要等待管理员审核</p>
        </div>
      </el-col>
    </el-row>
  </div>
  <!-- 预览文章 -->
  <PreviewArticle
    :previewArticleDialogFormVisible="previewArticleDialogFormVisible"
    :valueHtml="valueHtml"
    @onPreviewArticleCloseDialog="previewArticleCloseDialog"
  ></PreviewArticle>
</template>
<script>
import "@wangeditor/editor/dist/css/style.css"; // 引入 css

// 预览文章弹窗
import PreviewArticle from "./previewArticle/index.vue";

import { Editor, Toolbar } from "@wangeditor/editor-for-vue";

import {
  ref,
  toRefs,
  onMounted,
  reactive,
  shallowRef,
  onBeforeUnmount,
  watch,
} from "vue";

import { ElMessage, ElNotification, ElMessageBox } from "element-plus";

import axios from "axios";

import {check,fileUpload} from "@/js/fileUploadUtils";

function loadData(state) {
  // 查询文章分类
  axios.get("/showAllClassifyInfo").then((res) => {
    state.classifyInfo = res.data.data;
  });
}

export default {
  components: {
    Editor,
    Toolbar,
    PreviewArticle,
  },
  setup() {
    // 以下是富文本框的相关配置

    // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef();

    // 内容 HTML
    const valueHtml = ref("");

    const toolbarConfig = {};
    const editorConfig = {
      placeholder: "在xBlog，开始您的Write吧~",
      MENU_CONF: {},
    };
    watch(valueHtml.value, function (value, oldvalue) {
      Prism.highlightAll();
    });

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.destroy();
    });

    const handleCreated = (editor) => {
      editorRef.value = editor; // 记录 editor 实例，重要！'
    };

    editorConfig.MENU_CONF["uploadImage"] = {
        async customUpload(file, insertFn) {
            const filePath = process.env.VUE_APP_URL + "/articleImgUpload"
            let formData = new FormData()
            formData.append('files', file)
            let config = {
                    headers:{'Content-Type':'multipart/form-data'}
                };
            axios.post(filePath, formData,config).then((res) => {
                insertFn(res.data.data)
            }).catch((err) => {
                ElMessage.error(`${file.name} 上传失败`);
            })
        },
    };

    editorConfig.MENU_CONF["uploadVideo"] = {
      async customUpload(file, insertFn) {
        check(file, insertFn);
      },
    };
    editorConfig.MENU_CONF["codeSelectLang"] = {
      // 代码语言
      codeLangs: [
        {
          text: "CSS",
          value: "css",
        },
        {
          text: "HTML",
          value: "html",
        },
        {
          text: "XML",
          value: "xml",
        },
        {
          text: "Javascript",
          value: "javascript",
        },
        {
          text: "Typescript",
          value: "typescript",
        },
        {
          text: "JSX",
          value: "jsx",
        },
        {
          text: "Go",
          value: "go",
        },
        {
          text: "PHP",
          value: "php",
        },
        {
          text: "C",
          value: "c",
        },
        {
          text: "Python",
          value: "python",
        },
        {
          text: "Java",
          value: "java",
        },
        {
          text: "C++",
          value: "cpp",
        },
        {
          text: "C#",
          value: "csharp",
        },
        {
          text: "Visual Basic",
          value: "visual-basic",
        },
        {
          text: "SQL",
          value: "sql",
        },
        {
          text: "Ruby",
          value: "ruby",
        },
        {
          text: "Swift",
          value: "swift",
        },
        {
          text: "Bash",
          value: "bash",
        },
        {
          text: "Markdown",
          value: "markdown",
        },
      ],
    };

    // 以上是富文本框的相关配置

    const state = reactive({
      // 当前登录的用户信息
      userInfo: null,
      // 编写信息
      write: {
        titleValue: "",
        abstractValue: "",
        thumbnailValue: "",
        classifyValue: "",
        stateValue: "1",
        commentValue: "1",
      },
      // 分类信息
      classifyInfo: [],
      // 缩略图上传到服务器的路径
      uploadURL: process.env.VUE_APP_URL + "/upload",
      // 预览对话框状态
      previewArticleDialogFormVisible: false,
    });

    //挂载后加载数据
    onMounted(() => {
      const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      state.userInfo = userInfo.data;
      loadData(state);
    });

    // 图片上传格式、大小要求
    const beforeUploadFunction = (rawFile) => {
      if (
        rawFile.type !== "image/jpeg" &&
        rawFile.type !== "image/jpg" &&
        rawFile.type !== "image/png" &&
        rawFile.type !== "image/gif"
      ) {
        ElMessage.error("仅支持图片格式.png | .jpg | .jpeg | .gif ");
        return false;
      } else if (rawFile.size / 1024 / 1024 > 5) {
        ElMessage.error("仅支持大小不超过5MB的图片!");
        return false;
      }
      return true;
    };

    // 图片上传成功后执行的函数
    const handleAvatarSuccess = (response) => {
      state.write.thumbnailValue = response.data;
    };

    // 关闭预览窗口
    const previewArticleCloseDialog = (visible) => {
      state.previewArticleDialogFormVisible = visible;
    };

    // 提交
    const submitAdd = () => {
      // 获取当前下拉框选中的value 对于的label值
      let obj = {};
      obj = state.classifyInfo.find((item) => {
        return item.classifyId === state.write.classifyValue;
      });
      // 判断内容是否填写完整
      if (
        state.write.titleValue != "" &&
        state.write.abstractValue != "" &&
        state.write.classifyValue != "" &&
        state.write.stateValue != "" &&
        state.write.commentValue != ""
      ) {
        // 添加文章
        const params = {
          userId: state.userInfo.userId, // 编写用户编号
          userName: state.userInfo.userName, // 编写用户名称
          articleTitle: state.write.titleValue, // 文章标题
          articleClassifyId: state.write.classifyValue, // 分类编号
          articleClassifyName: obj.classifyName, // 分类名称
          articleDase: state.write.abstractValue, // 文章描述
          articleImgLitimg: state.write.thumbnailValue, // 文章缩略图
          articleContent: valueHtml.value, // 文章内容
          articleState: parseInt(state.write.stateValue), // 文章状态
          articlePass: state.userInfo.userType == 0 ? 2 : 1, // 审核(1待审核，2审核通过，3审核失败) 判断当前用户时普通用户(1)还是管理员(1)
          commentState: parseInt(state.write.commentValue), // 文章评论状态
        };

        axios.post("/article/insertArticleInfo", params).then((res) => {
          if (res.data.code == 0) {
            // 增加分类对应的文章数量  +1
            axios
              .get("/updateArticleNumberByClassifyId", {
                params: {
                  classifyId: state.write.classifyValue,
                },
              })
              .then((res) => {
                // 清空编写的文章内容
                valueHtml.value = "<p></p>";

                state.write = {
                  titleValue: "",
                  abstractValue: "",
                  thumbnailValue: "",
                  classifyValue: "",
                  stateValue: "1",
                  commentValue: "1",
                };

                ElNotification({
                  title: "提示",
                  message:
                    state.userInfo.userType == 0
                      ? "文章发布成功！"
                      : "文章提交成功，等待管理员审核",
                  type: "success",
                });
                window.history.back();
              });
          }
        });
      } else {
        ElMessage.error("除缩略图之外的内容都是必填项哦~");
      }
    };
    return {
      ...toRefs(state),
      beforeUploadFunction,
      handleAvatarSuccess,
      submitAdd,
      // 富文本编辑器
      editorRef,
      valueHtml,
      mode: "default", // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated,
      previewArticleCloseDialog,
    };
  },
};
</script>

<style scoped>
.addArticle_box {
  width: 100%;
  height: auto;
}

.addArticle_box > h3 {
  color: #2fa7b9;
  margin-bottom: 10px;
  padding: 20px 20px;
  background-color: white;
}

.write_abstract {
  background: white;
  text-align: left;
  padding: 10px 20px;
  margin-bottom: 10px;
}

.write_abstract h4 {
  line-height: 45px;
  color: #8f8f8f;
}

.write_abstract p {
  line-height: 35px;
  color: #8f8f8f;
  font-size: 12px;
}

.el-select {
  width: 100%;
}

:deep(.el-input-group__append) {
  width: 70px;
}

/* 富文本编辑器样式 */
:deep(.w-e-bar) {
  background-color: #fff;
  color: #8b8b8b;
  font-size: 15px;
  padding: 15px 5px;
  border-bottom: 1px solid #ececec;
}

:deep(.w-e-bar-divider) {
  background-color: white;
}

:deep(.w-e-bar svg) {
  fill: #8b8b8b;
}

:deep(.w-e-bar-item button) {
  color: #8b8b8b;
}

:global(.w-e-full-screen-container) {
  z-index: 999 !important;
}

:deep(.w-e-bar-item .active) {
  background-color: #e8e8e8;
}
</style>