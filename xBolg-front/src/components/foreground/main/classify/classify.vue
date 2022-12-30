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
  // 主要内容
  import MainContent from './content/mainContent/index.vue'

  // 其他内容 搜索功能
  import SearchCard from '../../otherModules/searchCard/index.vue';
  // 其他内容 文章分类
  import ArticleCategories from '../../otherModules/articleCategories/index.vue';
  // 其他内容：站点信息
  import OtherModules from '../../otherModules/websiteInfo/index.vue'
  // 其他内容：打赏本站
  import PlayTour from '../../otherModules/playTour/index.vue'

  export default {
    components: {
      MainContent,
      OtherModules,
      PlayTour,
      SearchCard,
      ArticleCategories
    }
  }
</script>

<style scoped>

</style>