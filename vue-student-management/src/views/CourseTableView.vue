<template>
  <div class="course-table-container">
    <!-- 顶部导航栏 -->
    <div class="header">
      <div class="header-left">
        <h1>课程列表</h1>
      </div>
      <div class="header-right">
        <span class="user-info">{{ studentName }} ({{ studentId }})</span>
        <button @click="logout" class="logout-btn">
          <i class="fas fa-sign-out-alt"></i> 退出登录
        </button>
      </div>
    </div>

    <!-- 查询条件区域 -->
    <div class="search-section">
      <div class="search-row">
        <div class="search-item">
          <label>课程名称：</label>
          <input
            type="text"
            v-model="searchParams.courseName"
            placeholder="输入课程名称"
            @keyup.enter="searchCourses"
          >
        </div>
        <div class="search-item">
          <label>学分范围：</label>
          <input
            type="number"
            v-model="searchParams.minCredit"
            placeholder="最小学分"
            step="0.5"
          >
          <span>至</span>
          <input
            type="number"
            v-model="searchParams.maxCredit"
            placeholder="最大学分"
            step="0.5"
          >
        </div>
      </div>
      <div class="search-row">
        <div class="search-item">
          <label>学时：</label>
          <input
            type="number"
            v-model="searchParams.hours"
            placeholder="输入学时"
          >
        </div>
        <div class="search-item">
          <label>院系：</label>
          <select v-model="searchParams.departmentId">
            <option value="">全部院系</option>
            <option v-for="dept in departments" :key="dept.id" :value="dept.id">
              {{ dept.name }}
            </option>
          </select>
        </div>
        <div class="search-item">
          <button @click="searchCourses" class="search-btn">
            <i class="fas fa-search"></i> 查询
          </button>
          <button @click="resetSearch" class="reset-btn">
            <i class="fas fa-redo"></i> 重置
          </button>
        </div>
      </div>
    </div>

    <!-- 课程表格 -->
    <div class="course-table">
      <table>
        <thead>
          <tr>
            <th @click="sortBy('courseId')">课程编号
              <i v-if="sortField === 'courseId'" :class="sortIcon"></i>
            </th>
            <th @click="sortBy('courseName')">课程名称
              <i v-if="sortField === 'courseName'" :class="sortIcon"></i>
            </th>
            <th @click="sortBy('credit')">学分
              <i v-if="sortField === 'credit'" :class="sortIcon"></i>
            </th>
            <th @click="sortBy('hours')">学时
              <i v-if="sortField === 'hours'" :class="sortIcon"></i>
            </th>
            <th>所属院系</th>
            <th>课程描述</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="course in paginatedCourses" :key="course.courseId">
            <td>{{ course.courseId }}</td>
            <td>{{ course.courseName }}</td>
            <td>{{ course.credit }}</td>
            <td>{{ course.hours }}</td>
            <td>{{ course.department?.name || '未知院系' }}</td>
            <td class="description-cell">{{ course.description || '无描述' }}</td>
            <td>
              <button
                @click="selectCourse(course)"
                class="select-btn"
                :disabled="isCourseSelected(course)"
              >
                {{ isCourseSelected(course) ? '已选' : '选择' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 分页控件 -->
      <div class="pagination">
        <button
          @click="prevPage"
          :disabled="currentPage === 1"
          class="page-btn"
        >
          上一页
        </button>
        <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
        <button
          @click="nextPage"
          :disabled="currentPage === totalPages"
          class="page-btn"
        >
          下一页
        </button>
        <span class="page-size">
          每页显示：
          <select v-model="pageSize" @change="changePageSize">
            <option value="5">5</option>
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="50">50</option>
          </select>
        </span>
      </div>
    </div>

    <!-- 底部导航 -->
    <div class="footer-nav">
      <button @click="goToCourseSelection" class="nav-btn">
        <i class="fas fa-list-alt"></i> 查看已选课程
      </button>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router';
import { ref, computed, onMounted, watch } from 'vue';

export default {
  name: 'CourseTableView',
  setup() {
    const router = useRouter();

    // 学生信息
    const studentName = ref('张三');
    const studentId = ref('20230001');

    // 课程数据
    const courses = ref([]);
    const selectedCourses = ref([]);
    const departments = ref([
      { id: 1, name: '计算机学院' },
      { id: 2, name: '数学学院' },
      { id: 3, name: '物理学院' },
      { id: 4, name: '化学学院' }
    ]);

    // 查询参数
    const searchParams = ref({
      courseName: '',
      minCredit: null,
      maxCredit: null,
      hours: null,
      departmentId: ''
    });

    // 分页参数
    const currentPage = ref(1);
    const pageSize = ref(10);
    const totalPages = ref(1);
    const sortField = ref('courseId');
    const sortDirection = ref('asc');

    // 计算属性
    const sortIcon = computed(() =>
      sortDirection.value === 'asc' ? 'fas fa-sort-up' : 'fas fa-sort-down'
    );

    const filteredCourses = computed(() => {
      let result = [...courses.value];

      // 按课程名称筛选
      if (searchParams.value.courseName) {
        const keyword = searchParams.value.courseName.toLowerCase();
        result = result.filter(course =>
          course.courseName.toLowerCase().includes(keyword)
        );
      }

      // 按学分范围筛选
      if (searchParams.value.minCredit !== null) {
        result = result.filter(course =>
          course.credit >= parseFloat(searchParams.value.minCredit)
        );
      }
      if (searchParams.value.maxCredit !== null) {
        result = result.filter(course =>
          course.credit <= parseFloat(searchParams.value.maxCredit)
        );
      }

      // 按学时筛选
      if (searchParams.value.hours !== null) {
        result = result.filter(course =>
          course.hours === parseInt(searchParams.value.hours)
        );
      }

      // 按院系筛选
      if (searchParams.value.departmentId) {
        result = result.filter(course =>
          course.department?.id === parseInt(searchParams.value.departmentId)
        );
      }

      // 排序
      result.sort((a, b) => {
        const field = sortField.value;
        const direction = sortDirection.value === 'asc' ? 1 : -1;

        if (a[field] < b[field]) return -1 * direction;
        if (a[field] > b[field]) return 1 * direction;
        return 0;
      });

      return result;
    });

    // 修复：计算属性中不应有副作用
    const paginatedCourses = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      const end = start + pageSize.value;
      return filteredCourses.value.slice(start, end);
    });

    // 新增：单独计算总页数
    const totalPagesComputed = computed(() => {
      return Math.ceil(filteredCourses.value.length / pageSize.value);
    });

    // 方法
    const fetchCourses = async () => {
      try {
        // 模拟数据
        courses.value = [
          // ... 课程数据保持不变 ...
        ];
      } catch (error) {
        console.error('获取课程列表失败:', error);
        alert('获取课程列表失败，请稍后重试');
      }
    };

    const searchCourses = async () => {
      currentPage.value = 1; // 重置到第一页
    };

    const resetSearch = () => {
      searchParams.value = {
        courseName: '',
        minCredit: null,
        maxCredit: null,
        hours: null,
        departmentId: ''
      };
      currentPage.value = 1;
      sortField.value = 'courseId';
      sortDirection.value = 'asc';
    };

    const sortBy = (field) => {
      if (sortField.value === field) {
        sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc';
      } else {
        sortField.value = field;
        sortDirection.value = 'asc';
      }
    };

    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
      }
    };

    const nextPage = () => {
      // 使用计算后的总页数
      if (currentPage.value < totalPagesComputed.value) {
        currentPage.value++;
      }
    };

    const changePageSize = () => {
      currentPage.value = 1; // 重置到第一页
    };

    const selectCourse = (course) => {
      if (!isCourseSelected(course)) {
        selectedCourses.value.push(course);
        alert(`已选择课程: ${course.courseName}`);
      }
    };

    const isCourseSelected = (course) => {
      return selectedCourses.value.some(c => c.courseId === course.courseId);
    };

    const goToCourseSelection = () => {
      router.push('/course-selection');
    };

    const logout = () => {
      router.push('/login');
    };

    // 生命周期钩子
    onMounted(() => {
      fetchCourses();

      // 从本地存储获取学生信息
      const student = JSON.parse(localStorage.getItem('student'));
      if (student) {
        studentName.value = student.name;
        studentId.value = student.id;
      }
    });

    // 监听过滤后课程的变化，更新总页数
    watch(filteredCourses, () => {
      totalPages.value = Math.ceil(filteredCourses.value.length / pageSize.value);
    }, { immediate: true });

    return {
      studentName,
      studentId,
      courses,
      departments,
      searchParams,
      currentPage,
      pageSize,
      totalPages, // 返回响应式变量
      sortField,
      sortDirection,
      sortIcon,
      paginatedCourses,
      searchCourses,
      resetSearch,
      sortBy,
      prevPage,
      nextPage,
      changePageSize,
      selectCourse,
      isCourseSelected,
      goToCourseSelection,
      logout
    };
  }
};
</script>

<style scoped>
.course-table-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Segoe UI', 'Microsoft YaHei', sans-serif;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e0e0e0;
}

.header h1 {
  font-size: 24px;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  font-weight: 500;
  color: #555;
}

.logout-btn {
  background: #f44336;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.logout-btn:hover {
  background: #d32f2f;
}

.search-section {
  background: #f5f5f5;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.search-row {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 10px;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-item label {
  font-weight: 500;
  min-width: 60px;
}

.search-item input, .search-item select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  min-width: 150px;
}

.search-btn, .reset-btn {
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.search-btn {
  background: #4CAF50;
  color: white;
  margin-right: 10px;
}

.search-btn:hover {
  background: #388E3C;
}

.reset-btn {
  background: #9E9E9E;
  color: white;
}

.reset-btn:hover {
  background: #757575;
}

.course-table {
  width: 100%;
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

th, td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  user-select: none;
}

th:hover {
  background-color: #e9ecef;
}

tr:hover {
  background-color: #f5f5f5;
}

.description-cell {
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.select-btn {
  padding: 6px 12px;
  background: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.select-btn:hover:not(:disabled) {
  background: #1976D2;
}

.select-btn:disabled {
  background: #9E9E9E;
  cursor: not-allowed;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-top: 20px;
}

.page-btn {
  padding: 6px 12px;
  background: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-size {
  margin-left: 20px;
}

.page-size select {
  padding: 6px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.footer-nav {
  margin-top: 30px;
  text-align: center;
}

.nav-btn {
  padding: 10px 20px;
  background: #673AB7;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s;
}

.nav-btn:hover {
  background: #5E35B1;
}

@media (max-width: 768px) {
  .search-row {
    flex-direction: column;
  }

  .search-item {
    width: 100%;
  }

  table {
    font-size: 14px;
  }

  th, td {
    padding: 8px 10px;
  }
}
</style>