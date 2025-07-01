<template>
  <div class="app-container">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="header-left">
        <button class="back-btn" @click="goBack">
          <i class="fas fa-arrow-left"></i> 返回课程列表
        </button>
        <div class="logo">
          <div class="logo-icon">
            <i class="fas fa-book-open"></i>
          </div>
          <div class="logo-text">
            <h1>学生选课系统</h1>
            <p>选课管理平台</p>
          </div>
        </div>
      </div>

      <div class="user-info">
        <div class="user-details">
          <div class="user-name">{{ studentName }}</div>
          <div class="user-id">学号: {{ studentId }}</div>
        </div>
        <button class="logout-btn" @click="logout">
          <i class="fas fa-sign-out-alt"></i> 退出
        </button>
      </div>
    </header>

    <div class="main-content">
      <!-- 左侧选课列表 -->
      <div class="course-selections">
        <div class="section-header">
          <h2 class="section-title">选课记录管理</h2>
          <div class="header-buttons">
            <button class="back-btn secondary" @click="goBack">
              <i class="fas fa-book"></i> 返回课程
            </button>
            <button class="add-btn" @click="showAddForm">
              <i class="fas fa-plus"></i> 添加选课
            </button>
          </div>
        </div>

        <div class="table-container">
          <table>
            <thead>
              <tr>
                <th>选课ID</th>
                <th>课程名称</th>
                <th>任课老师</th>
                <th>学分</th>
                <th>选课时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="selection in courseSelections" :key="selection.selectionId">
                <td>{{ selection.selectionId }}</td>
                <td>{{ selection.courseName }}</td>
                <td>{{ selection.teacher }}</td>
                <td>{{ selection.credit }}</td>
                <td>{{ formatDate(selection.selectionTime) }}</td>
                <td class="actions">
                  <button class="edit-btn" @click="editSelection(selection)">
                    <i class="fas fa-edit"></i> 编辑
                  </button>
                  <button class="delete-btn" @click="deleteSelection(selection.selectionId)">
                    <i class="fas fa-trash"></i> 删除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="pagination">
          <button class="page-btn" @click="prevPage" :disabled="currentPage === 0">
            <i class="fas fa-chevron-left"></i> 上一页
          </button>
          <span class="page-info">第 {{ currentPage + 1 }} 页 / 共 {{ totalPages }} 页</span>
          <button class="page-btn" @click="nextPage" :disabled="currentPage === totalPages - 1">
            下一页 <i class="fas fa-chevron-right"></i>
          </button>
          <div class="page-size">
            <span>每页:</span>
            <select v-model="pageSize" @change="changePageSize">
              <option value="5">5</option>
              <option value="10">10</option>
              <option value="20">20</option>
              <option value="50">50</option>
            </select>
          </div>
        </div>
      </div>

      <!-- 右侧表单 -->
      <div class="form-container" v-if="showForm">
        <h3 class="form-title">{{ formTitle }}</h3>
        <form @submit.prevent="handleSubmit">
          <!-- 课程检索区域 -->
          <div class="search-section">
            <div class="form-group">
              <label for="searchCourseId">课程号检索</label>
              <div class="input-with-button">
                <input
                  type="text"
                  id="searchCourseId"
                  class="form-control"
                  v-model="searchCourseId"
                  placeholder="输入课程号进行检索"
                >
                <button
                  type="button"
                  class="search-btn"
                  @click="searchCourse"
                  :disabled="searching"
                >
                  <i class="fas fa-search"></i>
                  {{ searching ? '检索中...' : '检索课程' }}
                </button>
              </div>
            </div>

            <div v-if="courseNotFound" class="error-message">
              <i class="fas fa-exclamation-triangle"></i>
              未找到该课程，请创建新课程
            </div>
          </div>

          <!-- 课程信息区域 -->
          <div class="form-group">
            <label for="courseId">课程号</label>
            <input
              type="text"
              id="courseId"
              class="form-control"
              v-model="formData.courseId"
              placeholder="输入课程号"
              required
            >
          </div>

          <div class="form-group">
            <label for="courseName">课程名称</label>
            <input
              type="text"
              id="courseName"
              class="form-control"
              v-model="formData.courseName"
              placeholder="输入课程名称"
              required
            >
          </div>

          <div class="form-group">
            <label for="teacher">任课老师</label>
            <input
              type="text"
              id="teacher"
              class="form-control"
              v-model="formData.teacher"
              placeholder="输入任课老师"
              required
            >
          </div>

          <div class="form-group">
            <label for="credit">学分</label>
            <input
              type="number"
              id="credit"
              class="form-control"
              v-model="formData.credit"
              placeholder="输入学分"
              step="0.5"
              min="0.5"
              max="10"
              required
            >
          </div>

          <div class="form-group">
            <label for="hours">学时</label>
            <input
              type="number"
              id="hours"
              class="form-control"
              v-model="formData.hours"
              placeholder="输入学时"
              min="1"
              max="100"
              required
            >
          </div>

          <div class="form-group">
            <label for="selectionTime">选课时间</label>
            <input
              type="datetime-local"
              id="selectionTime"
              class="form-control"
              v-model="formData.selectionTime"
              required
            >
          </div>

          <div class="btn-group">
            <button type="submit" class="submit-btn">
              <i class="fas fa-check"></i> {{ submitButtonText }}
            </button>
            <button type="button" class="cancel-btn" @click="cancelForm">
              <i class="fas fa-times"></i> 取消
            </button>
          </div>
        </form>

        <!-- 统计卡片 -->
        <div class="stats-cards">
          <div class="stat-card stat-1">
            <div class="stat-icon">
              <i class="fas fa-book"></i>
            </div>
            <div class="stat-text">
              <h3>{{ selectedCoursesCount }}</h3>
              <p>已选课程</p>
            </div>
          </div>

          <div class="stat-card stat-2">
            <div class="stat-icon">
              <i class="fas fa-chalkboard-teacher"></i>
            </div>
            <div class="stat-text">
              <h3>{{ uniqueTeachers }}</h3>
              <p>任课老师数</p>
            </div>
          </div>

          <div class="stat-card stat-3">
            <div class="stat-icon">
              <i class="fas fa-clock"></i>
            </div>
            <div class="stat-text">
              <h3>{{ totalHours }}</h3>
              <p>总学时</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部返回按钮 -->
    <div class="bottom-nav">
      <button class="back-btn large" @click="goBack">
        <i class="fas fa-arrow-circle-left"></i> 返回课程列表
      </button>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'CourseSelectionView',
  setup() {
    const router = useRouter();

    // 学生信息
    const studentName = ref('张同学');
    const studentId = ref('20230001');

    // 状态管理
    const courseSelections = ref([]);
    const currentPage = ref(0);
    const pageSize = ref(10);
    const totalPages = ref(1);
    const showForm = ref(false);
    const isEditing = ref(false);
    const searching = ref(false);
    const courseNotFound = ref(false);
    const searchCourseId = ref('');

    // 课程信息
    const allCourses = ref([
      { courseId: 'CS101', courseName: '计算机科学导论', teacher: '王教授', credit: 3.0, hours: 48 },
      { courseId: 'MATH201', courseName: '高等数学', teacher: '李教授', credit: 4.0, hours: 64 },
      { courseId: 'PHY301', courseName: '大学物理', teacher: '张教授', credit: 3.5, hours: 56 },
      { courseId: 'CS202', courseName: '数据结构', teacher: '刘教授', credit: 3.5, hours: 56 },
      { courseId: 'CHEM101', courseName: '基础化学', teacher: '陈教授', credit: 2.5, hours: 40 }
    ]);

    // 表单数据
    const formData = reactive({
      selectionId: null,
      courseId: '',
      courseName: '',
      teacher: '',
      credit: '',
      hours: '',
      studentId: '',
      studentName: '',
      selectionTime: ''
    });

    // 计算属性
    const formTitle = computed(() =>
      isEditing.value ? '编辑选课记录' : '添加新选课'
    );

    const submitButtonText = computed(() =>
      isEditing.value ? '更新选课' : '添加选课'
    );

    const selectedCoursesCount = computed(() => courseSelections.value.length);

    const uniqueTeachers = computed(() => {
      const teachers = new Set();
      courseSelections.value.forEach(selection => {
        teachers.add(selection.teacher);
      });
      return teachers.size;
    });

    const totalHours = computed(() => {
      return courseSelections.value.reduce((sum, selection) => {
        return sum + (parseInt(selection.hours) || 0);
      }, 0);
    });

    // 方法
    const fetchCourseSelections = async () => {
      try {
        // 模拟数据
        courseSelections.value = [
          { selectionId: 1001, courseId: 'CS101', courseName: '计算机科学导论', teacher: '王教授', credit: 3.0, hours: 48, studentId: '20230001', studentName: '张三', selectionTime: '2023-09-15T10:30:00' },
          { selectionId: 1002, courseId: 'MATH201', courseName: '高等数学', teacher: '李教授', credit: 4.0, hours: 64, studentId: '20230001', studentName: '张三', selectionTime: '2023-09-16T14:20:00' }
        ];

        totalPages.value = 1; // 模拟总页数

      } catch (error) {
        console.error('获取选课记录失败:', error);
        alert('获取选课记录失败，请稍后重试');
      }
    };

    // 检索课程
    const searchCourse = () => {
      if (!searchCourseId.value) {
        alert('请输入课程号进行检索');
        return;
      }

      searching.value = true;
      courseNotFound.value = false;

      // 模拟API调用延迟
      setTimeout(() => {
        const foundCourse = allCourses.value.find(
          course => course.courseId === searchCourseId.value
        );

        if (foundCourse) {
          // 填充表单
          Object.assign(formData, {
            courseId: foundCourse.courseId,
            courseName: foundCourse.courseName,
            teacher: foundCourse.teacher,
            credit: foundCourse.credit,
            hours: foundCourse.hours
          });
          courseNotFound.value = false;
        } else {
          // 课程不存在，准备创建
          formData.courseId = searchCourseId.value;
          formData.courseName = '';
          formData.teacher = '';
          formData.credit = '';
          formData.hours = '';
          courseNotFound.value = true;
        }

        searching.value = false;
      }, 800);
    };

    const showAddForm = () => {
      resetForm();
      isEditing.value = false;
      showForm.value = true;
      searchCourseId.value = '';
      courseNotFound.value = false;
    };

    const editSelection = (selection) => {
      Object.assign(formData, {
        selectionId: selection.selectionId,
        courseId: selection.courseId,
        courseName: selection.courseName,
        teacher: selection.teacher,
        credit: selection.credit,
        hours: selection.hours,
        studentId: selection.studentId,
        studentName: selection.studentName,
        selectionTime: formatDateTimeLocal(selection.selectionTime)
      });
      isEditing.value = true;
      showForm.value = true;
    };

    const handleSubmit = async () => {
      // 验证表单数据
      if (!validateForm()) return;

      try {
        if (isEditing.value) {
          // 更新选课记录
          const index = courseSelections.value.findIndex(
            s => s.selectionId === formData.selectionId
          );
          if (index !== -1) {
            courseSelections.value[index] = { ...formData };
          }
          alert('选课记录更新成功！');
        } else {
          // 添加新选课
          const newSelection = {
            selectionId: generateSelectionId(),
            ...formData
          };
          courseSelections.value.push(newSelection);

          // 如果是新课程，添加到课程列表
          if (courseNotFound.value) {
            const newCourse = {
              courseId: formData.courseId,
              courseName: formData.courseName,
              teacher: formData.teacher,
              credit: parseFloat(formData.credit),
              hours: parseInt(formData.hours)
            };
            allCourses.value.push(newCourse);
            alert('新课程创建并选课成功！');
          } else {
            alert('选课成功！');
          }
        }

        fetchCourseSelections();
        showForm.value = false;

      } catch (error) {
        console.error('操作失败:', error);
        alert('操作失败，请稍后重试');
      }
    };

    // 生成选课ID
    const generateSelectionId = () => {
      const maxId = Math.max(...courseSelections.value.map(s => s.selectionId), 0);
      return maxId + 1;
    };

    // 表单验证
    const validateForm = () => {
      if (!formData.courseId) {
        alert('请输入课程号');
        return false;
      }

      if (!formData.courseName) {
        alert('请输入课程名称');
        return false;
      }

      if (!formData.teacher) {
        alert('请输入任课老师');
        return false;
      }

      if (!formData.credit || parseFloat(formData.credit) <= 0) {
        alert('请输入有效的学分');
        return false;
      }

      if (!formData.hours || parseInt(formData.hours) <= 0) {
        alert('请输入有效的学时');
        return false;
      }

      return true;
    };

    const deleteSelection = async (id) => {
      if (!confirm('确定要删除这条选课记录吗？')) return;

      try {
        courseSelections.value = courseSelections.value.filter(
          s => s.selectionId !== id
        );
        alert(`选课记录 #${id} 删除成功！`);
        fetchCourseSelections();
      } catch (error) {
        console.error('删除失败:', error);
        alert('删除选课记录失败');
      }
    };

    const cancelForm = () => {
      showForm.value = false;
    };

    const resetForm = () => {
      Object.assign(formData, {
        selectionId: null,
        courseId: '',
        courseName: '',
        teacher: '',
        credit: '',
        hours: '',
        studentId: studentId.value,
        studentName: studentName.value,
        selectionTime: ''
      });
    };

    const prevPage = () => {
      if (currentPage.value > 0) {
        currentPage.value--;
        fetchCourseSelections();
      }
    };

    const nextPage = () => {
      if (currentPage.value < totalPages.value - 1) {
        currentPage.value++;
        fetchCourseSelections();
      }
    };

    const changePageSize = () => {
      currentPage.value = 0;
      fetchCourseSelections();
    };

    const formatDate = (dateString) => {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleString('zh-CN');
    };

    const formatDateTimeLocal = (dateString) => {
      if (!dateString) return '';
      const date = new Date(dateString);
      return new Date(date.getTime() - date.getTimezoneOffset() * 60000)
        .toISOString()
        .slice(0, 16);
    };

    const logout = () => {
      if (confirm('确定要退出系统吗？')) {
        router.push('/login');
      }
    };

    // 返回课程列表
    const goBack = () => {
      router.push('/course-table');
    };

    // 初始化表单
    const initFormData = () => {
      resetForm();
      formData.studentId = studentId.value;
      formData.studentName = studentName.value;
      formData.selectionTime = new Date().toISOString().slice(0, 16);
    };

    // 生命周期钩子
    onMounted(() => {
      fetchCourseSelections();
      initFormData();

      // 从本地存储获取学生信息
      const student = JSON.parse(localStorage.getItem('student'));
      if (student) {
        studentName.value = student.name;
        studentId.value = student.id;
      }
    });

    return {
      studentName,
      studentId,
      courseSelections,
      currentPage,
      pageSize,
      totalPages,
      showForm,
      formData,
      formTitle,
      submitButtonText,
      selectedCoursesCount,
      uniqueTeachers,
      totalHours,
      searchCourseId,
      searching,
      courseNotFound,
      fetchCourseSelections,
      showAddForm,
      editSelection,
      handleSubmit,
      deleteSelection,
      cancelForm,
      prevPage,
      nextPage,
      changePageSize,
      formatDate,
      logout,
      goBack,
      searchCourse
    };
  }
};
</script>

<style scoped>
/* 新增样式 */
.search-section {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 20px;
}

.input-with-button {
  display: flex;
  gap: 10px;
}

.search-btn {
  background: #4c6ef5;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s;
  min-width: 120px;
  justify-content: center;
}

.search-btn:disabled {
  background: #a5b4fc;
  cursor: not-allowed;
}

.search-btn:hover:not(:disabled) {
  background: #3b5bdb;
  transform: translateY(-2px);
}

.error-message {
  background: #fff5f5;
  color: #e53e3e;
  padding: 10px;
  border-radius: 6px;
  margin-top: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
  border-left: 3px solid #e53e3e;
}


<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Segoe UI', 'Microsoft YaHei', sans-serif;
}

body {
  background-color: #f5f7fa;
  color: #333;
  line-height: 1.6;
}

.app-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  position: relative;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #1a2980, #26d0ce);
  padding: 15px 25px;
  border-radius: 12px;
  color: white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.back-btn {
  background: rgba(255, 255, 255, 0.15);
  border: none;
  color: white;
  padding: 8px 15px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-2px);
}

.back-btn i {
  font-size: 16px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 15px;
}

.logo-icon {
  font-size: 24px;
  background: rgba(255, 255, 255, 0.2);
  width: 45px;
  height: 45px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-text h1 {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 5px;
}

.logo-text p {
  font-size: 13px;
  opacity: 0.9;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-details {
  text-align: right;
}

.user-name {
  font-weight: 600;
  font-size: 16px;
}

.user-id {
  font-size: 13px;
  opacity: 0.9;
}

.logout-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  padding: 8px 15px;
  border-radius: 50px;
  cursor: pointer;
  transition: background 0.3s;
  display: flex;
  align-items: center;
  gap: 8px;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 主要内容区 */
.main-content {
  display: grid;
  grid-template-columns: 1fr 350px;
  gap: 25px;
  flex: 1;
}

/* 选课列表 */
.course-selections {
  background: white;
  border-radius: 12px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.section-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
}

.header-buttons {
  display: flex;
  gap: 12px;
}

.add-btn {
  background: linear-gradient(135deg, #1a2980, #26d0ce);
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 10px rgba(26, 41, 128, 0.3);
}

.back-btn.secondary {
  background: #5a67d8;
}

.back-btn.secondary:hover {
  background: #4c51bf;
}

.table-container {
  overflow-x: auto;
  padding: 0 20px 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

th {
  background-color: #f8fafc;
  color: #5a6a85;
  font-weight: 600;
  user-select: none;
}

tr:hover {
  background-color: #f8fafc;
}

.actions {
  display: flex;
  gap: 10px;
}

.edit-btn, .delete-btn {
  padding: 6px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 5px;
}

.edit-btn {
  background: #e3f2fd;
  color: #1976d2;
}

.edit-btn:hover {
  background: #bbdefb;
}

.delete-btn {
  background: #ffebee;
  color: #d32f2f;
}

.delete-btn:hover {
  background: #ffcdd2;
}

/* 分页 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  gap: 15px;
}

.page-btn {
  background: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 8px 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled) {
  background: #f0f4f8;
  border-color: #cbd5e0;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #666;
}

.page-size {
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-size select {
  padding: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

/* 添加/编辑表单 */
.form-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
  padding: 20px;
  position: sticky;
  top: 20px;
}

.form-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #2c3e50;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #4a5568;
}

.form-control {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 15px;
  transition: border-color 0.3s;
}

.form-control:focus {
  outline: none;
  border-color: #4299e1;
  box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.1);
}

.btn-group {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.submit-btn, .cancel-btn {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s;
}

.submit-btn {
  background: linear-gradient(135deg, #1a2980, #26d0ce);
  color: white;
}

.submit-btn:hover {
  opacity: 0.9;
  transform: translateY(-2px);
}

.cancel-btn {
  background: #f1f5f9;
  color: #4a5568;
}

.cancel-btn:hover {
  background: #e2e8f0;
}

/* 统计卡片 */
.stats-cards {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
  margin-top: 25px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-1 .stat-icon {
  background: #e3f2fd;
  color: #1976d2;
}

.stat-2 .stat-icon {
  background: #e8f5e9;
  color: #388e3c;
}

.stat-3 .stat-icon {
  background: #f3e5f5;
  color: #9c27b0;
}

.stat-text h3 {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 5px;
}

.stat-text p {
  color: #718096;
  font-size: 14px;
}

/* 底部导航 */
.bottom-nav {
  margin-top: 30px;
  text-align: center;
  padding: 20px 0;
}

.back-btn.large {
  background: linear-gradient(135deg, #4c51bf, #667eea);
  color: white;
  border: none;
  padding: 12px 25px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s;
}

.back-btn.large:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(76, 81, 191, 0.3);
}

/* 响应式设计 */
@media (max-width: 900px) {
  .main-content {
    grid-template-columns: 1fr;
  }

  .form-container {
    position: static;
  }
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    text-align: center;
    gap: 15px;
    padding: 15px;
  }

  .header-left {
    flex-direction: column;
    gap: 10px;
  }

  .user-info {
    width: 100%;
    justify-content: center;
  }

  .user-details {
    text-align: center;
  }

  .section-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }

  .header-buttons {
    width: 100%;
    flex-wrap: wrap;
  }

  .actions {
    flex-direction: column;
  }

  .pagination {
    flex-wrap: wrap;
  }
}

@media (max-width: 480px) {
  .app-container {
    padding: 15px;
  }

  .back-btn {
    padding: 6px 10px;
    font-size: 14px;
  }

  .logo-text h1 {
    font-size: 18px;
  }

  .logo-icon {
    width: 40px;
    height: 40px;
    font-size: 20px;
  }

  .section-title {
    font-size: 18px;
  }

  .add-btn, .back-btn.secondary {
    padding: 6px 12px;
    font-size: 14px;
  }

  th, td {
    padding: 10px;
    font-size: 14px;
  }

  .edit-btn, .delete-btn {
    padding: 5px 8px;
    font-size: 13px;
  }

  .back-btn.large {
    width: 100%;
    padding: 12px;
    font-size: 15px;
  }
}
</style>