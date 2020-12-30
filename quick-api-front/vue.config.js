module.exports = {
    devServer: {
      proxy: {
        '/api': {// 匹配所有以 '/api'开头的请求路径
          target: 'http://localhost:8899',// 代理目标的基础路径
          ws: true,// 支持跨域
          changeOrigin: true,
          pathRewrite: {// 重写路径: 去掉路径中开头的'/api'
            '^/api': ''
          }
        }
      }
    }
  }
  