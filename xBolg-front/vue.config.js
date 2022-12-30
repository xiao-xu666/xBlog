const { defineConfig } = require('@vue/cli-service')

// 引入
const glob = require('glob')

const CompressionPlugin = require('compression-webpack-plugin');//引入gzip压缩插件

function getEntry() {
    let entrys = {}
    // 获取pages目录下后缀为html的路径
    glob.sync('./src/pages/**?/*.html').forEach(item => {
        // 获取页面匹配名称
        let urlArr = item.split('/').splice(-3)
        entrys[urlArr[1]] = {
            entry: 'src/pages/' + urlArr[1] + '/index.js',
            template: 'src/pages/' + urlArr[1] + '/index.html',
            filename: urlArr[1] + '.html',
            title: 'pages-' + urlArr[1]
        }
    })
    return entrys
}

let pages = getEntry()

module.exports = defineConfig({
    devServer: {
        port: 80,
        host: 'localhost',
        https: false,
        open: true,
        allowedHosts: 'all',
        proxy: {
            '/api': {
                target: 'http://localhost:8080/',
                changeOrigin: true,
                pathRewrite: {
                    '/api': ''
                }
            }
        }
    },
    transpileDependencies: true,
    lintOnSave: false,
    productionSourceMap: false,
    configureWebpack: (config) => {
        if (process.env.NODE_ENV === 'production') {
            config.plugins.push(
                new CompressionPlugin({
                    // gzip压缩配置
                    test: /\.js$|\.html$|\.css/, // 匹配文件名
                    threshold: 10240, // 对超过10kb的数据进行压缩
                    deleteOriginalAssets: false, // 是否删除原文件
                })
            )
        }
    }
})
