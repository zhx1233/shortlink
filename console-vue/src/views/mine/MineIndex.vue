<template>
  <div class="mine-page">
    <div class="options-box">
      <div class="account-title">
        <iconify-icon icon="solar:user-id-bold"></iconify-icon>
        <span>账号设置</span>
      </div>
      <div class="account-note">
        <strong>资料别乱写。</strong>
        <span>系统会记住你。</span>
      </div>
    </div>
    <div class="main-box">
      <el-descriptions
        class="margin-top content-box"
        title="个人信息"
        :column="1"
        :size="size"
        border
      >
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <user />
              </el-icon>
              用户名
            </div>
          </template>
          <span v-if="!dialogVisible">{{ userInfo?.data?.data?.username }}</span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <iphone />
              </el-icon>
              手机号
            </div>
          </template>
          <span>{{ userInfo?.data?.data?.phone }}</span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <tickets />
              </el-icon>
              姓名
            </div>
          </template>
          <span>{{ userInfo?.data?.data?.realName }}</span>
        </el-descriptions-item>
        <el-descriptions-item>
          <template #label>
            <div class="cell-item">
              <el-icon :style="iconStyle">
                <Message />
              </el-icon>
              邮箱
            </div>
          </template>
          <span>{{ userInfo?.data?.data?.mail }}</span>
        </el-descriptions-item>
      </el-descriptions>
      <el-button class="edit-profile" type="primary" @click="dialogVisible = !dialogVisible">修改个人信息</el-button>
    </div>
  </div>
  <!-- 修改信息 -->
  <el-dialog v-model="dialogVisible" title="修改个人信息" width="60%" :before-close="handleClose">
    <div class="register" :class="{ hidden: isLogin }">
      <el-form
        ref="loginFormRef"
        :model="userInfoForm"
        label-width="50px"
        class="form-container"
        width="width"
        :rules="formRule"
      >
        <el-form-item prop="username">
          <el-input
            v-model="userInfoForm.username"
            placeholder="请输入用户名"
            maxlength="11"
            show-word-limit
            disabled
          >
            <template v-slot:prepend> 用户名 </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="mail">
          <el-input v-model="userInfoForm.mail" placeholder="请输入邮箱" show-word-limit clearable>
            <template v-slot:prepend> 邮<span class="second-font">箱</span> </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input
            v-model="userInfoForm.phone"
            placeholder="请输入手机号"
            show-word-limit
            clearable
          >
            <template v-slot:prepend> 手机号 </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="realName">
          <el-input
            v-model="userInfoForm.realName"
            placeholder="请输入姓名"
            show-word-limit
            clearable
          >
            <template v-slot:prepend> 姓<span class="second-font">名</span> </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="userInfoForm.password"
            placeholder="默认密码，如需修改可输入新密码"
            show-word-limit
            clearable
          >
            <template v-slot:prepend> 密<span class="second-font">码</span> </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <div style="width: 100%; display: flex; justify-content: flex-end">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="changeUserInfo(loginFormRef)"> 提交 </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>

<script setup>
import { getCurrentInstance, ref, reactive } from 'vue'
import { getUsername } from '@/core/auth'
import { cloneDeep } from 'lodash'
import { ElMessage } from 'element-plus'
const loginFormRef = ref()
const { proxy } = getCurrentInstance()
// eslint-disable-next-line no-unused-vars
const API = proxy.$API
const userInfo = ref()
const userInfoForm = ref() // 修改信息
const size = ref('default')
const iconStyle = { color: '#9b5b31' }
const isLogin = ref(false)
const getUserInfo = async () => {
  const username = getUsername()
  userInfo.value = await API.user.queryUserInfo(username)
  userInfoForm.value = cloneDeep(userInfo.value.data?.data)
  // console.log(userInfoForm.value)
}
getUserInfo()
// 修改信息
const dialogVisible = ref(false)
const handleClose = () => {
  dialogVisible.value = false
}
const formRule = reactive({
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    {
      pattern: /^1[3|5|7|8|9]\d{9}$/,
      message: '请输入正确的手机号',
      trigger: 'blur'
    },
    { min: 11, max: 11, message: '手机号必须是11位', trigger: 'blur' }
  ],
  username: [{ required: true, message: '请输入您的用户名', trigger: 'blur' }],
  mail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    {
      pattern: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
      message: '请输入正确的邮箱号',
      trigger: 'blur'
    }
  ],
  password: [
    { required: false, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 15, message: '密码长度请在八位以上', trigger: 'blur' }
  ],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
})
const changeUserInfo = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      await API.user.editUser(userInfoForm.value).then((res) => {
        if (res?.data?.code !== '0') {
          ElMessage.error(res.data.message)
        } else {
          getUserInfo()
          dialogVisible.value = false
          ElMessage.success('修改成功!')
        }
      })
    } else {
      return false
    }
  })
}
</script>

<style lang="scss" scoped>
.mine-page {
  display: flex;
  height: 100%;
  width: 100%;
  color: #281b12;
}

.main-box {
  position: relative;
  flex: 1;
  min-width: 0;
  margin-left: 18px;
  padding: 20px;
  background:
    linear-gradient(145deg, rgba(255, 248, 236, 0.96), rgba(235, 207, 165, 0.9)),
    radial-gradient(circle at 88% 8%, rgba(190, 95, 53, 0.18), transparent 32%);
  border: 1px solid rgba(255, 224, 180, 0.34);
  box-shadow: 18px 20px 0 rgba(0, 0, 0, 0.14);
  display: flex;
  flex-direction: column;
}

.content-box {
  flex: 1;
  padding: 22px;
  background: rgba(255, 252, 243, 0.62);
  border: 1px solid rgba(61, 42, 27, 0.16);
}

.register {
  padding-right: 30px;
}

:deep(.el-descriptions__label) {
  width: 200px !important;
  color: #4d3524;
  background: #f4e6ce !important;
}

:deep(.el-descriptions__content) {
  color: #281b12;
  background: rgba(255, 252, 243, 0.7) !important;
}

.second-font {
  margin-left: 13px;
}

.options-box {
  position: relative;
  height: 100%;
  width: 224px;
  padding: 22px 18px;
  color: #fff4df;
  border: 1px solid rgba(255, 224, 180, 0.18);
  background:
    linear-gradient(180deg, rgba(31, 28, 18, 0.9), rgba(45, 33, 23, 0.86)),
    radial-gradient(circle at 28% 0%, rgba(190, 95, 53, 0.24), transparent 34%);
  box-shadow: 12px 14px 0 rgba(0, 0, 0, 0.18);
}

.account-title {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #fff4df;
  font-size: 17px;
  font-weight: 800;

  iconify-icon {
    color: #f1b24a;
    font-size: 25px;
  }
}

.account-note {
  display: grid;
  gap: 6px;
  margin-top: 42px;
  padding: 16px;
  color: #271b13;
  background: #f1b24a;
  transform: rotate(-2deg);

  strong {
    font-size: 18px;
  }

  span {
    font-size: 13px;
  }
}

.edit-profile {
  position: absolute;
  left: 42px;
  top: 260px;
  height: 42px;
  min-width: 128px;
  border-radius: 0;
  border: 0;
  color: #fff7ea;
  background: #be5f35;
  box-shadow: 7px 7px 0 #2d2117;
  transition:
    transform 420ms cubic-bezier(.19, 1, .22, 1),
    box-shadow 420ms cubic-bezier(.19, 1, .22, 1);
}

.edit-profile:hover {
  background: #a94e28;
  transform: translate(3px, 3px);
  box-shadow: 3px 3px 0 #2d2117;
}

:deep(.el-descriptions__body) {
  width: 500px;
}

@media (max-width: 900px) {
  .mine-page {
    flex-direction: column;
    overflow: auto;
  }

  .options-box {
    width: 100%;
  }

  .main-box {
    margin-left: 0;
    margin-top: 16px;
    min-height: 480px;
  }
}
</style>
