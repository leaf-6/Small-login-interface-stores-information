<template>
  <div class="todo-container">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" @click="openDialog('create')">
          <el-icon><Plus /></el-icon>
          新增待办
        </el-button>
        <el-select
          v-model="statusFilter"
          placeholder="筛选状态"
          style="width: 150px; margin-left: 20px"
          @change="handleFilterChange"
        >
          <el-option label="全部" value="" />
          <el-option
            v-for="status in Object.values(TodoStatus)"
            :key="status"
            :label="TODO_STATUS_CONFIG[status].label"
            :value="status"
          />
        </el-select>
      </div>
      <div class="toolbar-right">
        <span class="user-info">
          <el-icon><Avatar /></el-icon>
          {{ userStore.username }}
        </span>
        <el-button type="danger" plain @click="handleLogout">
          <el-icon><SwitchButton /></el-icon>
          退出登录
        </el-button>
      </div>
    </div>

    <!-- 待办事项卡片网格 -->
    <div v-loading="loading" class="todo-content">
      <el-row :gutter="20">
        <el-col
          v-for="item in todoList"
          :key="item.id"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="8"
          :xl="6"
        >
          <el-card shadow="hover" class="todo-card">
            <div class="card-content">
              <div class="card-body">
                <h3 class="todo-title">{{ item.title }}</h3>
                <p class="todo-description">
                  {{ truncateDescription(item.description) }}
                </p>
                <div class="todo-meta">
                  <el-tag :type="TODO_STATUS_CONFIG[item.status].type">
                    {{ TODO_STATUS_CONFIG[item.status].label }}
                  </el-tag>
                  <div class="todo-time">
                    <div>创建：{{ formatDate(item.createdAt) }}</div>
                    <div>更新：{{ formatDate(item.updatedAt) }}</div>
                  </div>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" link @click="openDialog('edit', item)">
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button type="danger" link @click="handleDelete(item.id)">
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 空状态 -->
      <el-empty
        v-if="!loading && todoList.length === 0"
        description="暂无待办事项"
      />
    </div>

    <!-- 分页 -->
    <div v-if="todoList.length > 0" class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalElements"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogMode === 'create' ? '新增待办事项' : '编辑待办事项'"
      width="500px"
    >
      <el-form
        ref="todoFormRef"
        :model="todoForm"
        :rules="todoRules"
        label-width="80px"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="todoForm.title"
            placeholder="请输入标题"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="todoForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入描述"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="todoForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option
              v-for="status in Object.values(TodoStatus)"
              :key="status"
              :label="TODO_STATUS_CONFIG[status].label"
              :value="status"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="handleSave">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getTodos, createTodo, updateTodo, deleteTodo } from '@/api/todo'
import { formatDate } from '@/utils/date'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Avatar, SwitchButton } from '@element-plus/icons-vue'
import { TodoStatus, TODO_STATUS_CONFIG } from '@/types'
import type { FormInstance, FormRules } from 'element-plus'
import type { TodoItem, TodoRequest } from '@/types'

const router = useRouter()
const userStore = useUserStore()

// 列表数据
const loading = ref(false)
const todoList = ref<TodoItem[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalElements = ref(0)
const statusFilter = ref<'' | TodoStatus>('')

// 对话框
const dialogVisible = ref(false)
const dialogMode = ref<'create' | 'edit'>('create')
const saveLoading = ref(false)
const currentEditId = ref<number>(0)
const todoFormRef = ref<FormInstance>()
const todoForm = reactive<TodoRequest>({
  title: '',
  description: '',
  status: TodoStatus.PENDING
})

const todoRules: FormRules = {
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { max: 200, message: '标题不能超过200个字符', trigger: 'blur' }
  ],
  description: [
    { max: 1000, message: '描述不能超过1000个字符', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 加载待办列表
const loadTodos = async () => {
  loading.value = true
  try {
    const response = await getTodos({
      page: currentPage.value - 1,
      size: pageSize.value,
      status: statusFilter.value
    })
    todoList.value = response.content
    totalElements.value = response.totalElements
  } catch (error: any) {
    // 错误已在拦截器中显示，这里不再重复提示
  } finally {
    loading.value = false
  }
}

// 截断描述文本
const truncateDescription = (description: string | null): string => {
  if (!description) return '暂无描述'
  if (description.length <= 50) return description
  return description.substring(0, 50) + '...'
}

// 打开对话框
const openDialog = (mode: 'create' | 'edit', item?: TodoItem) => {
  dialogMode.value = mode
  if (mode === 'create') {
    todoForm.title = ''
    todoForm.description = ''
    todoForm.status = TodoStatus.PENDING
    currentEditId.value = 0
  } else if (item) {
    todoForm.title = item.title
    todoForm.description = item.description || ''
    todoForm.status = item.status
    currentEditId.value = item.id
  }
  dialogVisible.value = true
  // 清除表单验证状态
  setTimeout(() => {
    todoFormRef.value?.clearValidate()
  }, 0)
}

// 保存待办事项
const handleSave = async () => {
  if (!todoFormRef.value) return

  await todoFormRef.value.validate(async (valid) => {
    if (valid) {
      saveLoading.value = true
      try {
        if (dialogMode.value === 'create') {
          await createTodo(todoForm)
          ElMessage.success('创建成功')
        } else {
          await updateTodo(currentEditId.value, todoForm)
          ElMessage.success('更新成功')
        }
        dialogVisible.value = false
        loadTodos()
      } catch (error: any) {
        // 错误已在拦截器中显示，这里不再重复提示
      } finally {
        saveLoading.value = false
      }
    }
  })
}

// 删除待办事项
const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这个待办事项吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteTodo(id)
    ElMessage.success('删除成功')
    
    // 如果当前页只有一条数据且不是第一页，则返回上一页
    if (todoList.value.length === 1 && currentPage.value > 1) {
      currentPage.value--
    }
    loadTodos()
  } catch (error: any) {
    // 用户取消或错误已在拦截器中显示，这里不再处理
  }
}

// 状态筛选改变
const handleFilterChange = () => {
  currentPage.value = 1
  loadTodos()
}

// 页码改变
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadTodos()
}

// 每页数量改变
const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadTodos()
}

// 退出登录
const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}

// 组件挂载时加载数据
onMounted(() => {
  loadTodos()
})
</script>

<style scoped>
.todo-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.toolbar-left {
  display: flex;
  align-items: center;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #606266;
}

.todo-content {
  min-height: 400px;
}

.todo-card {
  margin-bottom: 20px;
  min-height: 220px;
}

.todo-card :deep(.el-card__body) {
  height: 100%;
  padding: 20px;
}

.card-content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  min-height: 180px;
}

.card-body {
  flex: 1;
}

.todo-title {
  margin: 0 0 12px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.todo-description {
  margin: 0 0 16px 0;
  font-size: 14px;
  color: #606266;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  min-height: 42px;
}

.todo-meta {
  margin-bottom: 12px;
}

.todo-time {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
  line-height: 1.6;
}

.card-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 响应式布局 - 超宽屏优化 */
@media (max-width: 1920px) {
  .el-col-xl-6 {
    width: 33.333% !important;
  }
}
</style>
