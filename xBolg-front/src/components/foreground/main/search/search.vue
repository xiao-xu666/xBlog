<template>
  <el-row :gutter="20">
    <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
      <!-- 左侧间隙 -->
    </el-col>
    <el-col :xs="24" :sm="22" :md="22" :lg="18" :xl="16">
      <el-row :gutter="20">
        <el-col class="hidden-sm-and-down" :md="6" :lg="6" :xl="6">
          <!-- 其他内容 -->

          <!-- 搜索功能 -->
          <SearchCard></SearchCard>
          <!-- 文章分类 -->
          <ArticleCategories></ArticleCategories>
          <!-- advertising 广告  传递参数：系统设置中设置好的广告图片、链接 -->
          <Advertising v-if="advertising1" :advertisingImg="advertising1" :advertisingLink="advertisingLink1">
          </Advertising>
          <!-- 站点信息 -->
          <OtherModules></OtherModules>
          <!-- 打赏本站 -->
          <PlayTour></PlayTour>
        </el-col>
        <el-col :xs="24" :sm="24" :md="18" :lg="18" :xl="18">
          <!-- 主要内容 -->
          <MainContent></MainContent>
        </el-col>
      </el-row>
    </el-col>
    <el-col :xs="0" :sm="1" :md="1" :lg="3" :xl="4">
      <!-- 右侧间隙 -->
    </el-col>
  </el-row>
</template>
<script>
  import axios from "axios";
  import {
    onMounted,
    reactive,
    toRefs
  } from "vue";
  // 主要内容
  import MainContent from './content/mainContent/index.vue'

  // 其他内容 搜索功能
  import SearchCard from '../../otherModules/searchCard/index.vue';
  // 其他内容：站点信息
  import OtherModules from '../../otherModules/websiteInfo/index.vue'
  // 其他内容：打赏本站
  import PlayTour from '../../otherModules/playTour/index.vue'
  // 其他内容 文章分类
  import ArticleCategories from '../../otherModules/articleCategories/index.vue';
  // 引入advertising 广告
  import Advertising from '../../otherModules/advertising/index.vue';


  function loadData(state) {
    // 查询系统设置中设置好的精选文章的文章id
    axios.get("/showAllSystemSetup").then(res => {
      // 图片 根url
      const url = process.env.VUE_APP_URL;
      // 缩略图 判断是点击上传的还是，网络图片    本地图片需要加上服务器路径
      if (res.data.data[0].advertising1 != "" && !res.data.data[0].advertising1.includes('http')) {
        res.data.data[0].advertising1 = url + res.data.data[0].advertising1
      }
      state.advertising1 = res.data.data[0].advertising1;
      state.advertisingLink1 = res.data.data[0].advertisingLink1;
    })
  }
  export default {
    components: {
      MainContent,
      OtherModules,
      PlayTour,
      SearchCard,
      ArticleCategories,
      Advertising
    },
    setup(props) {
      //挂载后加载数据
      onMounted(() => {
        loadData(state);
      });

      const state = reactive({
        advertising1: '',
        advertisingLink1: '',
      })

      return {
        ...toRefs(state)
      }
    }

  }
</script>

<style scoped>

</style>