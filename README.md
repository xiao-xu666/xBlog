<p align=center>
  <a href="http://www.moguit.cn">
    <img src="https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/logo.png" style="zoom:50%;" alt="xBlog博客" style="width:200px;height:200px">
  </a>
</p>
<p align=center style="color:#40485B;font-weight:900">
   xBlog博客——小许的实践博客，一个基于MVC架构的SSM前后端分离博客系统
</p>
<p align="center">
<a target="_blank" href="https://github.com/xiao-xu666/xBlog">
    	<img src="https://img.shields.io/badge/MySQL-8.0.29-green.svg" ></img>
		<img src="https://img.shields.io/badge/JDK-1.8+-green.svg" ></img>
        <img src="https://img.shields.io/badge/nodejs-14.x-green" ></img>
        <img src="https://img.shields.io/badge/springboot-2.7.5.RELEASE-green" ></img>
        <img src="https://img.shields.io/badge/vue-3.2.13-green" ></img>
		<img src="https://img.shields.io/badge/wangeditor-5.1.13-green" ></img>
        <img src="https://img.shields.io/badge/elementUi-2.0.6-brightgreen" ></img>
        <img src="https://img.shields.io/badge/mybatis--plus-3.1.2-green" ></img>
        <img src="https://img.shields.io/github/stars/xiao-xu666/xBlog.svg" ></img>
</a></p>



​											[项目介绍](#项目介绍) |[运行配置](#运行配置)| [站点演示](#站点演示) | [项目特点](#项目特点) |[项目地址](#项目地址)| [技术架构](#技术选型)  |[环境搭建](#环境搭建)| [项目截图](#网站截图)

##  前言

[**小许**]([xiao-xu666 (github.com)](https://github.com/xiao-xu666)) 是一个从二本院校一路摸滚翻爬上来的程序员。目前就读于北京科技大学天津学院，是计算机科学与技术专业的一个科班生。这两天改编了一个博客系统并把该项目起名为xBlog，未来将会在个人博客上持续性的输出很多原创小知识以及学习资源，欢迎各位小伙伴关注我，和我一起共同学习，同时我也希望各位小伙伴能够给**xBlog博客**项目多多 **Star** 支持，您的**点赞**就是我维护的动力！

## 项目介绍

xBlog博客，一个**一个基于MVC架构的SSM前后端分离博客系统**。**Web** 端使用 **Vue** + **ElementUi** ，后端使用 **SpringCloud** + **SpringBoot** + **Mybatis-plus**进行开发，使用**redis**+**mailSender** 做登录验证和邮箱注册，使用 **Mybatis-plus** 作为持久化操作核心，使用 **Git**完成博客的持续集成，使用 **MySQL** 收集博客数据，文件支持**上传本地**、**阿里云OOS** .

- xBlog博客大部分功能是我个人进行开发的，因能力有限，其中很多技术都是一边学习一边使用的，可以说xBlog博客也是一个我用来熟悉技术的项目，所以很多地方可能考虑不周，故有能改正的地方，还请各位老哥能够指出~
- 现在挺多是SSM或者SSH的博客管理系统，想用 **Spring** + **SpringBoot**  + **Vue** 的前后端分离架构进行尝试项目的构建，里面很多功能可能只是**为了满足自己的学习需求**而引入的，大家可以**根据自己服务器配置来选择启动的服务**，因此本博客也是一个非常好的 **Spring**、**SpringBoot**以及 **Vue** 技术的入门学习项目。

## 运行配置

最低配置：1核1G (有可能会很卡)

推荐配置：2核2G (博主自己使用的)

## 站点演示

> 【博主自己搭建的项目】：http://8.130.39.152/
>

## 项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过 **Json** 进行数据交互，前端再也不用关注后端技术
- 页面交互使用 **Vue3.x**，极大的提高了开发效率。
- 引入阿里云OOS对象存储，同时支持本地文件存储
- 引入基础的权限管理，灵活的控制管理员和用户权限，按钮级别的细粒度权限控制，满足绝大部分的权限需求(大部分前端鉴权)
- 采用自研的分片上传模块，实现大文件的分片传输
- 支持富文本编辑器([WangEditor]([wangEditor](https://www.wangeditor.com/)))随心切换

## 项目地址

目前项目托管在 **Gitee** 和 **Github** 平台上中，欢迎大家 **Star** 和 **Fork** 支持~

- Gitee地址：[xBlog: 一款基于SSM的开源个人博客 (gitee.com)](https://gitee.com/borther-xu/xBlog)
- Github地址：[xiao-xu666/xBlog: 一款基于SSM的开源个人博客 (github.com)](https://github.com/xiao-xu666/xBlog)

## 技术架构

### 后端技术

|     技术     |          说明           |                             官网                             |
| :----------: | :---------------------: | :----------------------------------------------------------: |
|     Java     |      底层实现语言       | [ https://www.java.com/zh-CN/](https://www.java.com/zh-CN/)  |
| Spring Boot  |         MVC框架         | [ https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| MyBatis-Plus |         ORM框架         |                   https://mp.baomidou.com/                   |
|    MySQL     |       持久层数据        |       [https://www.mysql.com/](https://www.mysql.com/)       |
|    Redis     |       分布式缓存        |                      https://redis.io/                       |
|    Druid     |      数据库连接池       | [ https://github.com/alibaba/druid](https://github.com/alibaba/druid) |
|    阿里云    |    阿里云 - 对象储存    |                   https://www.aliyun.com/                    |
|    SLF4J     |        日志框架         |                    http://www.slf4j.org/                     |
|    Lombok    |    简化对象封装工具     | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
|   fastjson   |   Javabean序列号工具    | [https://github.com/alibaba/fastjson](https://github.com/alibaba/fastjson) |
|  mailSender  |      邮件发送工具       |                                                              |
|    Nginx     | HTTP和反向代理web服务器 |                      http://nginx.org/                       |

### 前端技术

|         技术          |         说明          |                             官网                             |
| :-------------------: | :-------------------: | :----------------------------------------------------------: |
|        Vue.js         |       前端框架        |                      https://vuejs.org/                      |
|      Vue-router       |       路由框架        |                  https://router.vuejs.org/                   |
|         Vuex          |   全局状态管理框架    |                   https://vuex.vuejs.org/                    |
|        Element        |      前端ui框架       |    [ https://element.eleme.io](https://element.eleme.io/)    |
|         Axios         |     前端HTTP框架      | [ https://github.com/axios/axios](https://github.com/axios/axios) |
|        Echarts        |       图表框架        |                      www.echartsjs.com                       |
|      wangEditor       |     富文本编辑器      |                 https://www.wangeditor.com/                  |
|     Highlight.js      |   代码语法高亮插件    |         https://github.com/highlightjs/highlight.js          |
|      vue-cropper      |     图片裁剪组件      |           https://github.com/xyxiao001/vue-cropper           |
| vue-image-crop-upload |  vue图片剪裁上传组件  |      https://github.com/dai-siki/vue-image-crop-upload       |
|   vue-emoji-comment   | Vue Emoji表情评论组件 |       https://github.com/pppercyWang/vue-emoji-comment       |
|        md5-hex        |      MD5生成工具      | [https://github.com/sindresorhus/md5-hex](https://github.com/sindresorhus/md5-hex) |

## 环境搭建

### 开发工具

|     工具     |       说明        |                             官网                             |
| :----------: | :---------------: | :----------------------------------------------------------: |
|     IDEA     |    Java开发IDE    |           https://www.jetbrains.com/idea/download            |
|   WebStorm   |    前端开发IDE    |             https://www.jetbrains.com/webstorm/              |
| RedisDesktop |  Redis可视化工具  | [ https://redisdesktop.com/download](https://redisdesktop.com/download) |
| SwitchHosts  |   本地Host管理    |             https://oldj.github.io/SwitchHosts/              |
|   X-shell    | Linux远程连接工具 |               https://xshell.en.softonic.com/                |
|    X-ftp     | Linux文件传输工具 |         https://www.netsarang.com/zh/all-downloads/          |
|    navcat    |  数据库连接工具   |                   https://navicat.com.cn/                    |

### 开发环境

| 工具  | 版本号 |                             下载                             |
| :---: | :----: | :----------------------------------------------------------: |
|  JDK  |  1.8   | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
| Maven | 3.3.0+ |                   http://maven.apache.org/                   |
| MySQL |  8.0   |                    https://www.mysql.com/                    |
| Nginx |  1.10  |              http://nginx.org/en/download.html               |
| Redis | 3.3.0  |                  https://redis.io/download                   |

## 网站截图

| ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/1.png) | ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/2.png) |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/3.png) | ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/4.png) |
| ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/5.png) | ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/6.png) |
| ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/7.png)! | ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/8.png) |
| ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/9.png) | ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/10.png) |
| ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/11.png) | ![](https://xiaoxu-xblog.oss-cn-beijing.aliyuncs.com/readme/12.png) |
