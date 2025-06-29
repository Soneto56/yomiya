//待补充<template>
       <div>
         <!-- 通知列表 -->
         <el-notification
           v-for="(notification, index) in notifications"
           :key="index"
           :title="notification.title"
           :message="notification.message"
           :type="notification.type"
           :duration="notification.duration"
           @close="removeNotification(index)"
         />
       </div>
     </template>

     <script setup>
     import { ref } from 'vue';

     // 存储通知的数组
     const notifications = ref([]);

     // 添加通知的方法
     const addNotification = (title, message, type = 'info', duration = 3000) => {
       notifications.value.push({
         title,
         message,
         type,
         duration
       });
     };

     // 移除通知的方法
     const removeNotification = (index) => {
       notifications.value.splice(index, 1);
     };

     // 暴露添加通知的方法，供其他组件调用
     defineExpose({
       addNotification
     });
     </script>

     <style scoped>
     /* 可以在这里添加自定义样式 */
     </style>