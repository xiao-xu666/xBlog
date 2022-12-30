// highlight 的样式，依赖包，组件
import 'highlight.js/styles/atom-one-dark.css'
import 'highlight.js/lib/common'
import hljsVuePlugin from '@highlightjs/vue-plugin'

import { createApp } from 'vue'
import router from "./router/index.js"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
// 导入所有ElementPlus图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'
import './css/index.css'
import './css/backstage.css'
import './css/article.css'
import './css/effects02.css'
import hljs from 'highlight.js';
import * as echarts from 'echarts' 


import App from './App.vue'

const app = createApp(App)

// vue3 给原型上挂载属性
app.config.globalProperties.$echarts = echarts;

//创建v-highlight全局指令
app.directive('highlight', function (el) {
	let blocks = el.querySelectorAll('pre code');
	blocks.forEach((block) => {
		hljs.highlightBlock(block)
		// 从这开始是设置行号
		block.innerHTML = `<ol><li>${block.innerHTML.replace(/\n/g, `</li><li class="line">`)}</li></ol>`;
	})
})

// ElementPlus
app.use(ElementPlus, {
	locale: zhCn,
})

// 引入代码高亮，并进行全局注册
app.use(hljsVuePlugin)

// 配置请求的基准URL地址
// axios.defaults.baseURL = "http://192.168.66.100";
// axios.defaults.baseURL = "http://localhost/";
// process.env.VUE_APP_XXX
// axios.defaults.baseURL = '/api'
axios.defaults.baseURL = process.env.VUE_APP_URL;

// 将所有图标进行全局注册
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component(key, component)
}
// 路由
app.use(router)
router.beforeEach((to, from, next) => {
	/* 路由发生变化修改页面title */
	if (to.meta.title) {
		document.title = to.meta.title
	}
	next()
})

app.mount('#app')