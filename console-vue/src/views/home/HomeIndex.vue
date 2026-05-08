<template>
  <div class="common-layout">
    <el-container>
      <el-header height="72px" style="padding: 0">
        <div class="header">
          <div @click="toMySpace" class="logo">
            <iconify-icon icon="solar:link-bold-duotone"></iconify-icon>
            <div>
              <strong>短链工坊</strong>
              <span>别让长链接乱跑</span>
            </div>
          </div>
          <nav class="route-tabs">
            <button :class="{ active: route.path.includes('/space') }" type="button" @click="toMySpace">
              <iconify-icon icon="solar:widget-5-bold"></iconify-icon>
              工作台
            </button>
            <button :class="{ active: route.path.includes('/account') }" type="button" @click="toMine">
              <iconify-icon icon="solar:user-rounded-bold"></iconify-icon>
              账号
            </button>
          </nav>
          <div class="header-actions">
            <a
              class="link-span"
              target="_blank"
              href="https://nageoffer.com/shortlink/"
              >文档</a
            >
            <a
              class="link-span"
              target="_blank"
              href="https://nageoffer.com/planet/group/"
              >沟通群</a
            >
            <a
                class="link-span"
                target="_blank"
                href="https://nageoffer.com/shortlink/video/"
            >视频</a
            >
            <a
                class="link-span"
                target="_blank"
                href="http://shortlink.nageoffer.com"
            >演示</a
            >
            <el-dropdown>
              <div class="user-chip">
                <iconify-icon icon="solar:verified-check-bold"></iconify-icon>
                <span class="name-span">{{username}}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="toMine">个人信息</el-dropdown-item>
                  <el-dropdown-item divided @click="logout">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main style="padding: 0">
        <div class="content-box">
          <div class="content-grain"></div>
          <RouterView class="content-space" />
        </div>
      </el-main>
      <!-- <el-container>
        <el-aside width="180px">
          <el-menu
            active-text-color="#073372"
            background-color="#0e5782"
            class="el-menu-vertical-demo"
            :default-active="getLasteRoute(route.path)"
            text-color="#fff"
            @select="handleSelect"
          >
            <template v-for="item in menuInfos" :key="item.name">
              <el-menu-item :index="item.path">
                <el-icon><icon-menu /></el-icon>
                <span>{{ item.name }}</span>
              </el-menu-item>
            </template>
          </el-menu></el-aside
        >

      </el-container> -->
    </el-container>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { removeKey, removeUsername, getToken, getUsername } from '@/core/auth.js'
import { ElMessage } from 'element-plus'
const { proxy } = getCurrentInstance()
const API = proxy.$API
// 当当前路径和菜单不匹配时，菜单不会被选中
const router = useRouter()
const route = useRoute()
const toMine = () => {
  router.push('/home' + '/account')
}
// 登出
const logout = async () => {
  const token = getToken()
  const username = getUsername()
  // 请求登出的接口
  await API.user.logout({ token, username })
  // 删除cookies中的token和username
  removeUsername()
  removeKey()
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
  ElMessage.success('成功退出！')
}
// 点击左上方的图片跳转到我的空间
const toMySpace = () => {
  router.push('/home' + '/space')
}
const username = ref('')
onMounted(async () => {
  const actualUsername = getUsername()
  await API.user.queryUserInfo(actualUsername)
  // firstName.value = res?.data?.data?.realName?.split('')[0]
  username.value = truncateText(actualUsername, 8)
})

// 辅助函数，用于截断文本
const truncateText = (text, maxLength) => {
  return text.length > maxLength ? text.slice(0, maxLength) + '...' : text
}
</script>

<style lang="scss" scoped>
.el-container {
  height: 100vh;

  .el-aside {
    border: 0;
    background-color: #0e5782;

    ul {
      border: 0px;
    }
  }

  .el-main {
    background:
      radial-gradient(circle at 8% 12%, rgba(241, 178, 74, 0.18), transparent 28%),
      linear-gradient(122deg, #161d13 0%, #2a321f 40%, #b6623c 100%);
  }
}

.header {
  color: #fff4df;
  background:
    linear-gradient(94deg, rgba(22, 29, 19, 0.98), rgba(44, 36, 24, 0.96)),
    radial-gradient(circle at 82% 0%, rgba(190, 95, 53, 0.3), transparent 34%);
  border-bottom: 1px solid rgba(255, 224, 180, 0.22);
  padding: 0 clamp(16px, 3vw, 34px);
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;

  .block {
    cursor: pointer;
    display: flex;
    align-items: center;
    border: 0px;
  }
}

.content-box {
  position: relative;
  height: calc(100vh - 72px);
  padding: 18px;
  overflow: hidden;
}

.content-grain {
  position: absolute;
  inset: 0;
  pointer-events: none;
  opacity: 0.22;
  mix-blend-mode: overlay;
  background-image:
    radial-gradient(circle at 1px 1px, rgba(255, 255, 255, 0.55) 1px, transparent 0),
    linear-gradient(110deg, rgba(255, 255, 255, 0.16), rgba(0, 0, 0, 0.22));
  background-size: 10px 10px, 100% 100%;
}

.content-space {
  position: relative;
  z-index: 1;
}

:deep(.el-tooltip__trigger:focus-visible) {
  outline: unset;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;

  iconify-icon {
    font-size: 32px;
    color: #f1b24a;
  }

  div {
    display: grid;
    gap: 1px;
  }

  strong {
    color: #fff4df;
    font-size: 18px;
    line-height: 1;
  }

  span {
    color: rgba(255, 244, 223, 0.62);
    font-size: 12px;
  }
}

.logo:hover {
  color: #fff;
}

.route-tabs {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 5px;
  background: rgba(255, 244, 223, 0.08);
  border: 1px solid rgba(255, 224, 180, 0.18);

  button {
    display: inline-flex;
    align-items: center;
    gap: 7px;
    height: 38px;
    padding: 0 14px;
    border: 0;
    color: rgba(255, 244, 223, 0.7);
    background: transparent;
    transition:
      transform 420ms cubic-bezier(.19, 1, .22, 1),
      background-color 420ms cubic-bezier(.19, 1, .22, 1);

    iconify-icon {
      font-size: 18px;
    }

    &.active {
      color: #281b12;
      background: #f1b24a;
      transform: translateY(-2px);
    }
  }
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.link-span {
  color: rgba(255, 244, 223, 0.68);
  padding: 6px 4px;
  font-size: 14px;
  cursor: pointer;
  text-decoration: none;
}

.link-span:hover {
  color: #f1b24a;
}

.user-chip {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  height: 36px;
  padding: 0 12px;
  color: #fff4df;
  border: 1px solid rgba(255, 224, 180, 0.24);
  background: rgba(255, 244, 223, 0.08);
  cursor: pointer;

  iconify-icon {
    color: #be5f35;
    font-size: 18px;
  }
}

.name-span {
  max-width: 88px;
  color: #fff4df;
  font-size: 13px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.avatar {
  transform: translateY(-2px);
}

@media (max-width: 900px) {
  .header {
    height: auto;
    min-height: 92px;
    align-items: flex-start;
    flex-wrap: wrap;
    padding-top: 12px;
    padding-bottom: 12px;
  }

  .header-actions {
    width: 100%;
    overflow-x: auto;
  }

  .content-box {
    height: calc(100vh - 92px);
    padding: 12px;
  }
}
</style>
