module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端实际端口
        changeOrigin: true,
        pathRewrite: { '^/api': '' }
      }
    }
  }
}