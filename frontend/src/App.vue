<template>
  <div id="app">
    <!-- 全局加载状态 -->
    <el-loading
      v-model:loading="globalLoading"
      :text="loadingText"
      fullscreen
      lock
    />

    <!-- 路由出口 -->
    <router-view v-slot="{ Component }">
      <transition name="fade-transform" mode="out-in">
        <keep-alive :include="cachedViews">
          <component :is="Component" :key="$route.fullPath" />
        </keep-alive>
      </transition>
    </router-view>

    <!-- 全局通知 -->
    <global-notification />
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRoute } from 'vue-router'
import GlobalNotification from '@/components/GlobalNotification.vue'

const userStore = useUserStore()
const route = useRoute()

// 全局加载状态
const globalLoading = ref(false)
const loadingText = ref('加载中...')

// 需要缓存的视图
const cachedViews = ref([])

// 初始化用户状态
userStore.initialize()

// 监听路由变化处理缓存
watch(
  () => route.name,
  (newVal) => {
    if (newVal && route.meta.keepAlive && !cachedViews.value.includes(newVal)) {
      cachedViews.value.push(newVal)
    }
  },
  { immediate: true }
)

// 全局错误处理
const handleGlobalError = (err) => {
  console.error('App error:', err)
  globalLoading.value = false
}

// 暴露全局方法
defineExpose({
  setGlobalLoading: (status, text = '加载中...') => {
    globalLoading.value = status
    loadingText.value = text
  }
})
</script>

<style lang="scss">
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  min-height: 100vh;
  color: #333;
  background-color: #f5f7fa;
}

/* 路由过渡动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

/* 全局滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-thumb {
  background-color: rgba(144, 147, 153, 0.3);
  border-radius: 3px;
}

::-webkit-scrollbar-track {
  background: transparent;
}
</style>