// 完成和路由相关更多工作
import { createRouter, createWebHashHistory, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/login",
    name: "登录",
    meta: { title: 'xBlog - 登录' },
    component: () => require.ensure([], (require) => require("../components/login/index.vue"))
  }, {
    path: "/register",
    name: "注册",
    meta: { title: 'xBlog - 注册' },
    component: () => require.ensure([], (require) => require('../components/register/index.vue'))
  }, {
    path: "/resetPassword",
    name: "忘记密码",
    meta: { title: 'xBlog - 忘记密码' },
    component: () => require.ensure([], (require) => require('../components/resetPassword/index.vue'))
  },
  // 前台路由
  {
    path: "/foreground",
    name: "首页",
    meta: { title: 'xBlog - 小许的个人博客' },
    component: () => require.ensure([], (require) => require('../components/foreground/main.vue')),
    redirect: "/",
    children: [{
      path: "/",
      name: "首页",
      meta: { title: 'xBlog - 首页' },
      component: () => require.ensure([], (require) => require('../components/foreground/main/home/home.vue')),
    },{
      // 搜索
      path: "/search",
      component: () => require.ensure([], (require) => require('../components/foreground/main/search/search.vue'))
    }, {
      // 文章分类
      path: "/classify/:id",
      component: () => require.ensure([], (require) => require('../components/foreground/main/classify/classify.vue'))
    }, {
      // 文章详情
      path: "/articleDetails/:id",
      component: () => require.ensure([], (require) => require('../components/foreground/main/articleDetails/articleDetails.vue'))
    }, {
      path: "/article",
      name: "精品文章",
      meta: { title: 'xBlog - 精品文章' },
      component: () => require.ensure([], (require) => require('../components/foreground/main/article/article.vue')),

    }, {
      path: "/website",
      name: "学习网站",
      meta: { title: 'xBlog - 学习网站' },
      component: () => require.ensure([], (require) => require('../components/foreground/main/website/website.vue'))
    }, {
      path: "/message",
      name: "留言",
      meta: { title: 'xBlog - 留言' },
      component: () => require.ensure([], (require) => require('../components/foreground/main/message/message.vue'))
    }, {
      path: "/about",
      name: "关于",
      meta: { title: 'xBlog - 关于' },
      component: () => require.ensure([], (require) => require('../components/foreground/main/about/about.vue'))
    }]
  }, {
    path: "/backstage",
    name: "backstage",
    meta: { title: 'xBlog - 后台首页', chineseName: '首页', icon: 'House' },
    component: () => require.ensure([], (require) => require('../components/backstage/main.vue')),
    redirect: "/backstageHome",
    children: [{
      path: "/backstageHome",
      name: "backstageHome",
      meta: { title: 'xBlog - 后台首页', chineseName: '后台首页', icon: 'House' },
      component: () => require.ensure([], (require) => require('../components/backstage/main/backstageHome/backstageHome.vue'))
    }, {
      path: "/addArticle",
      name: "addArticle",
      meta: { title: 'xBlog - 新建文章', chineseName: '新建文章', icon: 'EditPen' },
      component: () => require.ensure([], (require) => require('../components/backstage/main/articleOperation/addArticle/addArticle.vue'))
    }, {
      path: "/articleManager",
      name: "articleManager",
      meta: { title: 'xBlog - 文章管理', chineseName: '文章管理', icon: 'Edit' },
      component: () => require.ensure([], (require) => require('../components/backstage/main/articleOperation/articleManager/articleManager.vue'))
    }, {
      path: "/classifyManager",
      name: "classifyManager",
      meta: { title: 'xBlog - 分类管理', chineseName: '分类管理', icon: 'Discount' },
      component: () => require.ensure([], (require) => require('../components/backstage/main/articleOperation/classifyManager/classifyManager.vue'))
    }, {
      path: "/labelManager",
      name: "labelManager",
      meta: { title: 'xBlog - 标签管理', chineseName: '标签管理' },
      component: () => require.ensure([], (require) => require('../components/backstage/main/articleOperation/labelManager/labelManager.vue'))
    }, {
      // 评论管理
      path: '/commentManager',
      name: '评论管理',
      meta: { title: 'xBlog - 评论管理', chineseName: '评论管理' },
      component: () => require.ensure([], (require) => require("../components/backstage/main/commentManager/commentManager.vue")),
    }, {
      path: "/userManager",
      name: "userManager",
      meta: { title: 'xBlog - 用户管理', chineseName: '用户管理' },
      component: () => require.ensure([], (require) => require('../components/backstage/main/userManager/userManager.vue'))
    }, {
      // 链接管理
      path: '/linkManager',
      name: '链接管理',
      meta: { title: 'xBlog - 链接管理', chineseName: '链接管理' },
      component: () => require.ensure([], (require) => require("../components/backstage/main/linkManager/linkManager.vue")),
    }, {
      // 个人设置
      path: '/personSetup',
      name: '个人设置',
      meta: { title: 'xBlog - 个人设置', chineseName: '个人设置' },
      component: () => require.ensure([], (require) => require("../components/backstage/main/personSetup/personSetup.vue")),
    }, {
      // 系统设置
      path: '/systemSetup',
      name: '系统设置',
      meta: { title: 'xBlog - 系统设置', chineseName: '系统设置' },
      component: () => require.ensure([], (require) => require("../components/backstage/main/systemSetup/systemSetup.vue")),
    },]
  }, {
    // 404
    path: '/:pathMatch(.*)*',
    name: "404",
    meta: { title: 'xBlog - 404' },
    component: () => require.ensure([], (require) => require('../components/state/404/404.vue'))
  }, {
    // 403
    path: '/403',
    name: "403",
    meta: { title: 'xBlog - 403' },
    component: () => require.ensure([], (require) => require('../components/state/403/403.vue'))
  }
];

const router = createRouter({
  // 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
  history: createWebHistory(),
  routes,
});
// 点击路由回到顶部
router.afterEach((to,from,next) => {
  window.scrollTo(0,0);
})
// 白名单
const whiteList = ['/systemSetup', '/linkManager', "/userManager", "/classifyManager", "/labelManager", '/personSetup', "/backstage", '/commentManager', "/articleManager", "/addArticle", "/backstageHome",];
const adminList = ['/systemSetup', '/linkManager', "/userManager", "/classifyManager", "/labelManager",];
router.beforeEach(async (to, from, next) => {
  // 验证是否登录
  const tokenStr = JSON.parse(sessionStorage.getItem("userInfo"));
  // 登录还访问登录界面 就跳转到网站首页
  if (tokenStr != null && to.path === "/login") {
    return next("/")
  } else {
    // 如果to.path 不包含在白名单内，放行
    if (!whiteList.includes(to.path)) {
      next(); // 放行
    } else {
      if (!tokenStr) {
        return next("/login")
      } else {
        const userInfo = tokenStr.data; // 获取登录的用户信息
        if (adminList.includes(to.path) && userInfo.userType === 1) {
          // 后台部分界面普通用户不允许访问，比如：用户管理
          return next("/403")
        } else {
          next();
        }
      }
    }
  }

})

export default router;