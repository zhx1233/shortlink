<template>
  <div class="login-page" :class="{ 'is-register': !isLogin }">
    <div ref="vantaRef" class="vanta"></div>
    <div class="grain"></div>

    <main class="login-stage">
      <section class="story-panel">
        <div class="brand-kicker">
          <iconify-icon icon="solar:link-bold-duotone"></iconify-icon>
          <span>短链工坊</span>
        </div>

        <div class="copy-stack">
          <p class="eyebrow">别再贴长链接了</p>
          <h1>三秒收短。</h1>
          <h1>数据别装睡。</h1>
          <p>今天第 28 条链接。</p>
          <p>该看谁点了。</p>
        </div>

        <div class="desk-visual" aria-label="短链数据工作台">
          <div class="desk-topline">
            <span>link desk</span>
            <strong>028</strong>
          </div>
          <div class="paper-stack">
            <div class="paper-slip primary-slip">
              <span>https://long-long-long...</span>
              <strong>s.l/8K2m</strong>
            </div>
            <div class="paper-slip">
              <span>活动页</span>
              <strong>723 次</strong>
            </div>
          </div>
          <div class="mini-chart">
            <i style="height: 34%"></i>
            <i style="height: 58%"></i>
            <i style="height: 46%"></i>
            <i style="height: 78%"></i>
            <i style="height: 64%"></i>
          </div>
          <div class="desk-stamp">已收短</div>
          <span class="desk-caption">链接缩短后，终于像个人样。</span>
        </div>

        <div class="signal-card">
          <span>实时点击</span>
          <strong>1,284</strong>
          <small>比昨天多 17%</small>
        </div>

        <div class="metric-rail" aria-label="短链实时数据">
          <div>
            <span>峰值</span>
            <strong>20:31</strong>
          </div>
          <div>
            <span>快过期</span>
            <strong>3 条</strong>
          </div>
          <div>
            <span>队列</span>
            <strong>正常</strong>
          </div>
          <div>
            <span>拦截</span>
            <strong>9 次</strong>
          </div>
        </div>
      </section>

      <section class="form-panel">
        <div class="form-ruler" aria-hidden="true">
          <i></i>
          <i></i>
          <i></i>
          <i></i>
          <i></i>
        </div>
        <div class="form-stamp">
          <span>NO.028</span>
          <strong>LINK DESK</strong>
        </div>
        <div class="mode-tabs" aria-label="登录注册切换">
          <button :class="{ active: isLogin }" type="button" @click="isLogin = true">登录</button>
          <button :class="{ active: !isLogin }" type="button" @click="changeLogin">注册</button>
        </div>

        <div class="form-copy">
          <span>{{ isLogin ? '回来干活' : '开个新工位' }}</span>
          <h2>{{ isLogin ? '先把门打开。' : '填完就能用。' }}</h2>
          <p>{{ isLogin ? '账号别输错。' : '别起太长名字。' }}</p>
        </div>

        <div class="form-switcher">
          <Transition name="auth-slide" mode="out-in">
          <el-form
            v-if="isLogin"
            ref="loginFormRef1"
            key="login"
            class="auth-form"
            :model="loginForm"
            :rules="loginFormRule"
            label-position="top"
          >
            <el-form-item prop="username" label="用户名">
              <el-input v-model="loginForm.username" placeholder="比如 admin" maxlength="11" clearable>
                <template #prefix>
                  <iconify-icon icon="solar:user-rounded-bold"></iconify-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password" label="密码">
              <el-input v-model="loginForm.password" type="password" clearable placeholder="8 到 15 位" show-password>
                <template #prefix>
                  <iconify-icon icon="solar:lock-password-bold"></iconify-icon>
                </template>
              </el-input>
            </el-form-item>

            <div class="action-row">
              <el-checkbox v-model="checked">记住密码</el-checkbox>
              <el-button :loading="loading" type="primary" @click="login(loginFormRef1)">
                进去看看
              </el-button>
            </div>
          </el-form>

          <el-form
            v-else
            ref="loginFormRef2"
            key="register"
            class="auth-form register-form"
            :model="addForm"
            :rules="addFormRule"
            label-position="top"
          >
            <div class="form-grid">
              <el-form-item prop="username" label="用户名">
                <el-input v-model="addForm.username" placeholder="11 字以内" maxlength="11" clearable>
                  <template #prefix>
                    <iconify-icon icon="solar:user-id-bold"></iconify-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item prop="realName" label="姓名">
                <el-input v-model="addForm.realName" placeholder="写真实点" clearable>
                  <template #prefix>
                    <iconify-icon icon="solar:badge-bold"></iconify-icon>
                  </template>
                </el-input>
              </el-form-item>
            </div>

            <el-form-item prop="mail" label="邮箱">
              <el-input v-model="addForm.mail" placeholder="name@example.com" clearable>
                <template #prefix>
                  <iconify-icon icon="solar:letter-bold"></iconify-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="phone" label="手机号">
              <el-input v-model="addForm.phone" placeholder="11 位手机号" clearable>
                <template #prefix>
                  <iconify-icon icon="solar:phone-bold"></iconify-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password" label="密码">
              <el-input v-model="addForm.password" type="password" clearable placeholder="8 到 15 位" show-password>
                <template #prefix>
                  <iconify-icon icon="solar:key-minimalistic-bold"></iconify-icon>
                </template>
              </el-input>
            </el-form-item>

            <div class="action-row solo">
              <el-button :loading="loading" type="primary" @click="addUser(loginFormRef2)">
                马上开工
              </el-button>
            </div>
          </el-form>
          </Transition>
        </div>
      </section>

    </main>
  </div>

  <el-dialog v-model="isWC" title="人机验证" width="420px" :before-close="handleClose" class="verify-dialog">
    <div class="verification-flex">
      <span>扫码关注后，回复 <strong>link</strong>。</span>
      <span>验证码马上来。</span>
      <img class="img" src="@/assets/png/公众号二维码.png" alt="公众号二维码">
      <el-form class="form" :model="verification" :rules="verificationRule" ref="verificationRef">
        <el-form-item prop="code" label="验证码">
          <el-input v-model="verification.code" placeholder="填这里" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="isWC = false">取消</el-button>
        <el-button type="primary" @click="verificationLogin(verificationRef)">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { setToken, setUsername, getUsername } from '@/core/auth.js'
import { ref, reactive, onMounted, onBeforeUnmount, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as THREE from 'three'
import WAVES from 'vanta/src/vanta.waves'
const { proxy } = getCurrentInstance()
const API = proxy.$API
const loginFormRef1 = ref()
const loginFormRef2 = ref()
const router = useRouter()
const loginForm = reactive({
  username: 'admin',
  password: 'admin123456',
})
const addForm = reactive({
  username: '',
  password: '',
  realName: '',
  phone: '',
  mail: ''
})

const addFormRule = reactive({
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    {
      pattern: /^1[3|5|7|8|9]\d{9}$/,
      message: '请输入正确的手机号',
      trigger: 'blur'
    },
    { min: 11, max: 11, message: '手机号必须是11位', trigger: 'blur' }
  ],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 15, message: '密码长度请在八位以上', trigger: 'blur' }
  ],
  mail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    {
      pattern: /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
      message: '请输入正确的邮箱号',
      trigger: 'blur'
    }
  ],
  realName: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
  ]
})
const loginFormRule = reactive({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 15, message: '密码长度请在八位以上', trigger: 'blur' }
  ],
})
// 注册
const addUser = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res1 = await API.user.hasUsername({ username: addForm.username })
        if (res1?.data?.code === '0' && res1?.data?.data) {
          ElMessage.warning('用户名已存在！')
          return
        }

        const res2 = await API.user.addUser(addForm)
        if (res2?.data?.code !== '0') {
          ElMessage.warning(res2?.data?.message || '注册失败')
          return
        }

        const res3 = await API.user.login({ username: addForm.username, password: addForm.password })
        if (res3?.data?.code !== '0') {
          ElMessage.warning(res3?.data?.message || '登录失败')
          return
        }

        const token = res3?.data?.data?.token
        if (token) {
          setToken(token)
          setUsername(addForm.username)
          localStorage.setItem('token', token)
          localStorage.setItem('username', addForm.username)
        }
        ElMessage.success('注册登录成功！')
        router.push('/home')
      } catch (e) {
        ElMessage.error(e?.response?.data?.message || e?.message || '请求失败')
      } finally {
        loading.value = false
      }
    } else {
      return false
    }
  })

}
// 公众号验证码
const isWC = ref(false)
const verificationRef = ref()
const verification = reactive({
  code: ''
})
const verificationRule = reactive({
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
})
const handleClose = () => {
  isWC.value = false
}
const verificationLogin = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      loading.value = true
      const tempPassword = loginForm.password
      loginForm.password = verification.code
      try {
        const res1 = await API.user.login(loginForm)
        if (res1?.data?.code === '0') {
          const token = res1?.data?.data?.token
          if (token) {
            setToken(token)
            setUsername(loginForm.username)
            localStorage.setItem('token', token)
            localStorage.setItem('username', loginForm.username)
          }
          ElMessage.success('登录成功！')
          router.push('/home')
        } else if (res1?.data?.message === '用户已登录') {
          const cookiesUsername = getUsername()
          if (cookiesUsername === loginForm.username) {
            ElMessage.success('登录成功！')
            router.push('/home')
          } else {
            ElMessage.warning('用户已在别处登录，请勿重复登录！')
          }
        } else {
          ElMessage.error(res1?.data?.message || '验证码错误')
        }
      } catch (e) {
        ElMessage.error(e?.response?.data?.message || e?.message || '请求失败')
      } finally {
        loginForm.password = tempPassword
        loading.value = false
      }
    }
  })
}
// 登录
const login = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res1 = await API.user.login(loginForm)
        if (res1?.data?.code === '0') {
          const token = res1?.data?.data?.token
          if (token) {
            setToken(token)
            setUsername(loginForm.username)
            localStorage.setItem('token', token)
            localStorage.setItem('username', loginForm.username)
          }
          ElMessage.success('登录成功！')
          router.push('/home')
        } else if (res1?.data?.message === '用户已登录') {
          const cookiesUsername = getUsername()
          if (cookiesUsername === loginForm.username) {
            ElMessage.success('登录成功！')
            router.push('/home')
          } else {
            ElMessage.warning('用户已在别处登录，请勿重复登录！')
          }
        } else {
          ElMessage.error(res1?.data?.message || '登录失败')
        }
      } catch (e) {
        ElMessage.error(e?.response?.data?.message || e?.message || '请求失败')
      } finally {
        loading.value = false
      }
    } else {
      return false
    }
  })
}

const loading = ref(false)
// 是否记住密码
const checked = ref(true)
const vantaRef = ref()
// 动态背景
let vantaEffect = null
onMounted(() => {
  vantaEffect = WAVES({
    el: vantaRef.value,
    THREE: THREE,
    mouseControls: true,
    touchControls: true,
    gyroControls: false,
    minHeight: 200.0,
    minWidth: 200.0,
    scale: 1.0,
    scaleMobile: 1.0,
    color: 0x162016,
    shininess: 18,
    waveHeight: 16,
    waveSpeed: 0.45,
    zoom: 0.86
  })
})
onBeforeUnmount(() => {
  if (vantaEffect) {
    vantaEffect.destroy()
  }
})
// 展示登录还是展示注册
const isLogin = ref(true)
const changeLogin = () => {
  let domain = window.location.host
  if (domain === 'shortlink.magestack.cn' || domain === 'shortlink.nageoffer.com') {
    ElMessage.warning('演示环境暂不支持注册')
    return
  }
  isLogin.value = false
}
</script>

<style lang="less" scoped>
.login-page {
  --el-color-primary: #be5f35;
  --el-color-primary-light-3: #d07a4d;
  --el-color-primary-light-5: #df9a6e;
  --el-color-primary-light-7: #efc3a7;
  --el-color-primary-light-9: #f8eadb;
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  color: #271b13;
  background:
    radial-gradient(circle at 12% 18%, rgba(255, 214, 116, 0.34), transparent 28%),
    linear-gradient(126deg, #11180f 0%, #29331d 38%, #cf744a 100%);
}

.vanta,
.grain {
  position: absolute;
  inset: 0;
}

.vanta {
  z-index: 0;
  opacity: 0.58;
}

.grain {
  z-index: 1;
  pointer-events: none;
  opacity: 0.27;
  mix-blend-mode: overlay;
  background-image:
    radial-gradient(circle at 1px 1px, rgba(255, 255, 255, 0.52) 1px, transparent 0),
    linear-gradient(98deg, rgba(255, 255, 255, 0.12), rgba(0, 0, 0, 0.18));
  background-size: 9px 9px, 100% 100%;
}

.login-stage {
  position: relative;
  z-index: 2;
  display: grid;
  grid-template-columns: minmax(560px, 0.88fr) minmax(390px, 0.52fr);
  gap: clamp(28px, 6vw, 92px);
  min-height: 100vh;
  width: min(1180px, calc(100vw - 48px));
  margin: 0 auto;
  padding: clamp(28px, 6vh, 72px) 0;
  align-items: center;
}

.story-panel {
  position: relative;
  min-height: 620px;
  padding: 22px 0 0 18px;
}

.brand-kicker {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  color: #ffe0b4;
  border: 1px solid rgba(255, 224, 180, 0.42);
  background: rgba(23, 19, 13, 0.48);
  transform: rotate(-1.8deg);

  iconify-icon {
    font-size: 22px;
    color: #f1b24a;
  }

  span {
    font-size: 15px;
    letter-spacing: 0;
  }
}

.copy-stack {
  margin-top: 56px;
  width: min(42vw, 420px);
  color: #fff4df;

  .eyebrow {
    margin-bottom: 18px;
    color: #f5c16f;
    font-size: 16px;
  }

  h1 {
    font-size: clamp(48px, 5.8vw, 76px);
    line-height: 1.03;
    font-weight: 900;
    letter-spacing: 0;
  }

  p {
    margin-top: 12px;
    max-width: 210px;
    color: rgba(255, 244, 223, 0.76);
    font-size: 17px;
    line-height: 1.45;
  }
}

.desk-visual {
  position: absolute;
  right: 2px;
  bottom: 62px;
  width: min(34vw, 286px);
  min-height: 214px;
  padding: 14px 14px 16px;
  color: #2b1b10;
  background:
    linear-gradient(145deg, #f6e6c8, #dfb875),
    radial-gradient(circle at 80% 12%, rgba(190, 95, 53, 0.34), transparent 28%);
  box-shadow: 18px 24px 0 rgba(0, 0, 0, 0.2);
  transform: rotate(3deg) translateX(10px);
  transition:
    transform 520ms cubic-bezier(.19, 1, .22, 1),
    box-shadow 520ms cubic-bezier(.19, 1, .22, 1),
    filter 520ms cubic-bezier(.19, 1, .22, 1);
  overflow: hidden;

  &::before {
    content: "";
    position: absolute;
    inset: 0;
    opacity: 0.24;
    background-image:
      linear-gradient(90deg, rgba(43, 27, 16, 0.1) 1px, transparent 1px),
      linear-gradient(rgba(43, 27, 16, 0.09) 1px, transparent 1px);
    background-size: 22px 22px;
  }
}

.desk-visual:hover {
  filter: saturate(1.04);
  box-shadow: 24px 30px 0 rgba(0, 0, 0, 0.24);
  transform: rotate(2deg) translate(14px, -8px);
}

.desk-topline,
.paper-stack,
.mini-chart,
.desk-stamp,
.desk-caption {
  position: relative;
  z-index: 1;
}

.desk-topline {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  color: rgba(43, 27, 16, 0.72);
  text-transform: uppercase;

  span {
    max-width: 70px;
    font-size: 12px;
    line-height: 1.05;
  }

  strong {
    color: #be5f35;
    font-size: 36px;
    line-height: 0.8;
  }
}

.paper-stack {
  display: grid;
  gap: 10px;
  width: 76%;
  margin-top: 18px;
}

.paper-slip {
  padding: 10px;
  background: rgba(255, 252, 243, 0.86);
  border: 1px solid rgba(43, 27, 16, 0.18);
  box-shadow: 6px 7px 0 rgba(43, 27, 16, 0.12);
  transform: rotate(-2deg);

  span,
  strong {
    display: block;
  }

  span {
    color: rgba(43, 27, 16, 0.54);
    font-size: 12px;
  }

  strong {
    margin-top: 4px;
    color: #25170e;
    font-size: 17px;
    letter-spacing: 0;
  }
}

.primary-slip {
  transform: rotate(1.4deg);
}

.mini-chart {
  position: absolute;
  right: 16px;
  bottom: 50px;
  display: flex;
  align-items: flex-end;
  gap: 5px;
  width: 76px;
  height: 72px;
  padding: 10px;
  background: #2d2117;

  i {
    flex: 1;
    background: #f1b24a;
  }
}

.desk-stamp {
  position: absolute;
  right: 16px;
  top: 76px;
  width: 66px;
  height: 66px;
  display: grid;
  place-items: center;
  border: 2px solid #be5f35;
  border-radius: 50%;
  color: #be5f35;
  font-size: 16px;
  font-weight: 900;
  transform: rotate(-12deg);
}

.desk-caption {
  display: block;
  margin-top: 18px;
  color: #4c3522;
  font-size: 13px;
}

.signal-card {
  position: absolute;
  left: 18px;
  bottom: 146px;
  display: grid;
  gap: 2px;
  width: 166px;
  padding: 16px 18px;
  color: #21140c;
  background: #f1b24a;
  box-shadow: -10px 12px 0 rgba(255, 244, 223, 0.14);
  transform: rotate(-4deg) translateX(-10px);

  span,
  small {
    font-size: 13px;
  }

  strong {
    font-size: 34px;
    line-height: 1;
  }
}

.metric-rail {
  position: absolute;
  left: 18px;
  right: 42px;
  bottom: 34px;
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 0;
  width: min(520px, calc(100% - 60px));
  color: rgba(255, 244, 223, 0.76);
  border: 1px solid rgba(255, 224, 180, 0.22);
  background:
    linear-gradient(90deg, rgba(23, 19, 13, 0.54), rgba(23, 19, 13, 0.24)),
    repeating-linear-gradient(90deg, transparent 0 23px, rgba(255, 244, 223, 0.04) 23px 24px);
  box-shadow: 10px 12px 0 rgba(0, 0, 0, 0.12);
  transform: rotate(-1deg);

  div {
    display: grid;
    gap: 4px;
    min-width: 0;
    padding: 12px 14px;
    border-right: 1px solid rgba(255, 224, 180, 0.16);

    &:last-child {
      border-right: 0;
    }
  }

  span {
    color: rgba(255, 224, 180, 0.62);
    font-size: 12px;
  }

  strong {
    color: #fff4df;
    font-size: 18px;
    line-height: 1;
    white-space: nowrap;
  }
}

.form-panel {
  position: relative;
  justify-self: end;
  width: min(100%, 440px);
  padding: clamp(28px, 4vw, 40px);
  border: 1px solid rgba(50, 35, 22, 0.3);
  background:
    linear-gradient(145deg, rgba(255, 246, 229, 0.94), rgba(230, 200, 155, 0.88)),
    radial-gradient(circle at 80% 10%, rgba(201, 92, 48, 0.22), transparent 30%);
  box-shadow:
    16px 18px 0 rgba(45, 33, 23, 0.34),
    34px 38px 0 rgba(0, 0, 0, 0.18),
    0 36px 60px rgba(0, 0, 0, 0.22);
  transform: translate(28px, 18px) rotate(-0.7deg);
}

.form-panel::after {
  content: "";
  position: absolute;
  left: 24px;
  right: -18px;
  bottom: -24px;
  height: 26px;
  background: linear-gradient(90deg, rgba(31, 22, 14, 0.32), rgba(31, 22, 14, 0.06));
  transform: skewX(-18deg);
  filter: blur(1px);
  z-index: -1;
}

.form-ruler {
  position: absolute;
  top: 17px;
  left: 24px;
  right: 148px;
  display: flex;
  align-items: flex-end;
  gap: 10px;
  height: 12px;
  opacity: 0.42;

  i {
    width: 1px;
    height: 8px;
    background: #7a5135;

    &:nth-child(2n) {
      height: 12px;
    }
  }
}

.form-stamp {
  position: absolute;
  top: -16px;
  right: 26px;
  display: grid;
  gap: 1px;
  min-width: 104px;
  padding: 8px 12px;
  color: #fff4df;
  background: #2d2117;
  box-shadow: 6px 6px 0 rgba(190, 95, 53, 0.24);
  transform: rotate(2.5deg);

  span {
    color: #f1b24a;
    font-size: 11px;
    line-height: 1;
  }

  strong {
    font-size: 13px;
    line-height: 1;
    letter-spacing: 0;
  }
}

.mode-tabs {
  display: inline-grid;
  grid-template-columns: 1fr 1fr;
  gap: 6px;
  padding: 5px;
  background: rgba(41, 31, 21, 0.12);

  button {
    min-width: 84px;
    height: 38px;
    border: 0;
    color: #6d4b34;
    background: transparent;
    transition:
      transform 420ms cubic-bezier(.19, 1, .22, 1),
      background-color 420ms cubic-bezier(.19, 1, .22, 1);

    &.active {
      color: #fff7ea;
      background: #2d2117;
      transform: translateY(-2px);
    }
  }
}

.form-copy {
  margin: 36px 0 28px;

  span {
    color: #ad5c31;
    font-size: 15px;
    font-weight: 700;
  }

  h2 {
    margin-top: 5px;
    color: #20140c;
    font-size: 39px;
    line-height: 1.04;
    font-weight: 900;
  }

  p {
    margin-top: 8px;
    color: #71513b;
    font-size: 15px;
  }
}

.form-switcher {
  min-height: 286px;
  overflow: hidden;
}

.auth-slide-enter-active,
.auth-slide-leave-active {
  transition:
    opacity 360ms cubic-bezier(.19, 1, .22, 1),
    transform 460ms cubic-bezier(.19, 1, .22, 1),
    filter 460ms cubic-bezier(.19, 1, .22, 1);
}

.auth-slide-enter-from {
  opacity: 0;
  filter: blur(3px);
  transform: translate(24px, 10px) rotate(1.2deg);
}

.auth-slide-leave-to {
  opacity: 0;
  filter: blur(3px);
  transform: translate(-18px, -8px) rotate(-1deg);
}

.auth-form {
  :deep(.el-form-item) {
    margin-bottom: 18px;
  }

  :deep(.el-form-item__label) {
    margin-bottom: 7px;
    color: #4d3524;
    font-weight: 700;
    line-height: 1.1;
  }

  :deep(.el-input__wrapper) {
    min-height: 48px;
    border-radius: 0;
    border: 1px solid rgba(61, 42, 27, 0.42);
    background: rgba(255, 252, 243, 0.88);
    box-shadow: none;
    transition:
      transform 360ms cubic-bezier(.19, 1, .22, 1),
      border-color 360ms cubic-bezier(.19, 1, .22, 1),
      box-shadow 360ms cubic-bezier(.19, 1, .22, 1);
  }

  :deep(.el-input__wrapper:hover) {
    border-color: rgba(190, 95, 53, 0.58);
    box-shadow: 4px 4px 0 rgba(45, 33, 23, 0.09);
    transform: translateY(-1px);
  }

  :deep(.el-input__wrapper.is-focus) {
    border-color: #be5f35;
    box-shadow: 6px 6px 0 rgba(190, 95, 53, 0.2);
  }

  :deep(.el-input__inner) {
    color: #3a2618;
    font-weight: 700;
  }

  :deep(.el-input__inner::placeholder) {
    color: rgba(92, 59, 37, 0.68);
    font-weight: 600;
  }

  :deep(.el-input__password) {
    color: #8f5a37;
  }

  :deep(.el-input__prefix) {
    margin-right: 6px;
    color: #9b5b31;
    font-size: 18px;
  }
}

.form-grid {
  display: grid;
  grid-template-columns: 1.18fr 0.82fr;
  gap: 14px;
}

.action-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  margin-top: 28px;

  &.solo {
    justify-content: flex-end;
  }

  :deep(.el-checkbox__label) {
    color: #694a35;
  }

  :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
    border-color: #2d2117;
    background: #2d2117;
  }

  :deep(.el-button) {
    min-width: 124px;
    height: 46px;
    border-radius: 0;
    color: #fff7ea;
    background: #be5f35;
    box-shadow: 7px 7px 0 #2d2117;
    transition:
      transform 420ms cubic-bezier(.19, 1, .22, 1),
      box-shadow 420ms cubic-bezier(.19, 1, .22, 1),
      background-color 420ms cubic-bezier(.19, 1, .22, 1);
  }

  :deep(.el-button:hover) {
    background: #9e4725;
    transform: translate(2px, 2px) scale(1.035);
    box-shadow: 4px 4px 0 #2d2117;
  }

  :deep(.el-button:active) {
    background: #7f351c;
    transform: translate(7px, 7px) scale(0.985);
    box-shadow: 0 0 0 #2d2117;
  }
}

.verify-dialog {
  strong {
    color: #be5f35;
  }
}

.verification-flex {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  color: #3c2b1e;

  .img {
    width: 180px;
    margin: 16px auto;
    align-self: center;
  }

  .form {
    width: 100%;
  }
}

@media (max-width: 920px) {
  .login-stage {
    grid-template-columns: 1fr;
    width: min(100vw - 32px, 560px);
    padding: 26px 0 38px;
  }

  .story-panel {
    min-height: 410px;
    padding-left: 0;
  }

  .copy-stack {
    margin-top: 34px;

    h1 {
      font-size: 48px;
    }
  }

  .desk-visual {
    right: 0;
    bottom: 22px;
    width: 220px;
    min-height: 190px;
  }

  .paper-stack {
    width: 70%;
    margin-top: 14px;
  }

  .mini-chart {
    right: 14px;
    bottom: 44px;
    width: 58px;
    height: 62px;
  }

  .desk-stamp {
    top: 72px;
    right: 14px;
    width: 58px;
    height: 58px;
    font-size: 14px;
  }

  .signal-card {
    bottom: 92px;
    left: 10px;
  }

  .metric-rail {
    left: 0;
    right: auto;
    bottom: 0;
    width: min(100%, 480px);
  }

  .form-panel {
    justify-self: start;
    transform: rotate(-0.4deg);
  }
}

@media (max-width: 520px) {
  .login-page {
    overflow: auto;
  }

  .story-panel {
    min-height: 330px;
  }

  .copy-stack h1 {
    font-size: 38px;
  }

  .copy-stack p {
    font-size: 15px;
  }

  .desk-visual {
    width: 170px;
    min-height: 166px;
    padding: 12px;
  }

  .desk-topline strong {
    font-size: 28px;
  }

  .paper-slip {
    padding: 9px;

    strong {
      font-size: 14px;
    }
  }

  .mini-chart,
  .desk-stamp {
    display: none;
  }

  .signal-card {
    width: 142px;
    bottom: 72px;
  }

  .metric-rail {
    grid-template-columns: repeat(2, minmax(0, 1fr));
    width: 100%;

    div {
      padding: 10px 12px;
    }
  }

  .form-panel {
    padding: 22px 18px;
    box-shadow: 12px 14px 0 rgba(0, 0, 0, 0.2);
  }

  .form-copy h2 {
    font-size: 31px;
  }

  .form-grid {
    grid-template-columns: 1fr;
    gap: 0;
  }

  .action-row {
    align-items: stretch;
    flex-direction: column;

    &.solo {
      align-items: flex-end;
    }
  }

}
</style>
