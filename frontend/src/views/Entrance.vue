<template>
  <div class="entrance">
    <img class="entranceLogo" alt="汽车租赁管理系统 LOGO" src="../assets/entranceTitle.png"/>
    <div class="container">
      <el-space>
        <p class="loginText" @click="turnToLogin" :style="loginTextCss">登陆界面</p>
        <p class="registerText" @click="turnToRegister" :style="registerTextCss">注册界面</p>
      </el-space>

      <div class="login" v-show="isLogin">
        <div class="loginForm">
          <el-form
              ref="loginFormRef"
              :model="loginForm.form"
              :rules="loginRules"
              label-width="100px"
              style="margin-left: -63px"
              hide-required-asterisk
          >
            <el-form-item label=" " prop="userName">
              <el-input
                  style="width: 300px; margin: 3px 0"
                  v-model="loginForm.form.userName"
                  autocomplete="off"
                  placeholder="用户名或邮箱"
              ></el-input>
            </el-form-item>
            <el-form-item label=" " prop="password">
              <el-input
                  type="password"
                  show-password
                  style="width: 300px; margin: 3px 0"
                  v-model="loginForm.form.password"
                  autocomplete="off"
                  placeholder="密码"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <p class="forgetPwd" @click="preChgPwd">忘记密码，通过邮箱重置</p>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="toLogin(loginFormRef)" style="margin-left: 63px">登录</el-button>
              <el-button @click="clearLogin(loginFormRef)" style="margin-left: 50px">清空</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="register" v-show="!isLogin">
        <el-steps :active="active" finish-status="success" style="margin: 20px">
          <el-step title="填写邮箱密码" />
          <el-step title="完善身份信息" />
          <el-step title="完成注册" />
        </el-steps>
        <div class="registerForm" v-show="active === 0">
          <el-form
              ref="registerFormFirstRef"
              :model="registerForm.form"
              :rules="registerFirstRules"
              label-width="100px"
              style="margin-left: -63px"
              hide-required-asterisk
          >
            <el-form-item label=" " prop="email">
              <el-input
                  style="width: 300px; margin: 3px 0"
                  v-model="registerForm.form.email"
                  autocomplete="off"
                  placeholder="邮箱"
              ></el-input>
            </el-form-item>
            <el-form-item label=" " prop="telephone">
              <el-input
                  style="width: 300px; margin: 3px 0"
                  v-model="registerForm.form.telephone"
                  autocomplete="off"
                  placeholder="电话"
              ></el-input>
            </el-form-item>
            <el-form-item label=" " prop="userName">
              <el-input
                  style="width: 300px; margin: 3px 0"
                  v-model="registerForm.form.userName"
                  autocomplete="off"
                  placeholder="用户名"
              ></el-input>
            </el-form-item>
            <el-form-item label=" " prop="password">
              <el-input
                  type="password"
                  show-password
                  style="width: 300px; margin: 3px 0"
                  v-model="registerForm.form.password"
                  autocomplete="off"
                  placeholder="密码"
              ></el-input>
            </el-form-item>
            <el-form-item label=" " prop="checkPassword">
              <el-input
                  type="password"
                  show-password
                  style="width: 300px; margin: 3px 0"
                  v-model="registerForm.form.checkPassword"
                  autocomplete="off"
                  placeholder="确认密码"
              ></el-input>
            </el-form-item>
            <el-form-item label=" " prop="passCode">
              <el-input
                  style="width: 182px; margin: 3px 0"
                  v-model="registerForm.form.passCode"
                  autocomplete="off"
                  placeholder="验证码"
              ></el-input>
              <el-button class="getPassCode" @click="getPassCode(registerForm.form.email)">获取验证码</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="next(registerFormFirstRef)" style="margin-left: 63px">下一步</el-button>
              <el-button @click="clearRegister(registerFormFirstRef)" style="margin-left: 50px">清空</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="registerForm" v-show="active === 1">
          <el-form
              ref="registerFormSecondRef"
              :model="registerForm.form"
              :rules="registerSecondRules"
              label-width="100px"
              style="margin-left: -63px"
              hide-required-asterisk
          >
            <el-form-item label=" " prop="identityCard">
              <el-input
                  style="width: 300px; margin: 3px 0"
                  v-model="registerForm.form.identityCard"
                  autocomplete="off"
                  placeholder="身份证号码"
              ></el-input>
            </el-form-item>
            <el-space>
              <el-form-item label=" " prop="frontSideImage">
                <el-upload
                    class="avatar-uploader"
                    action="http://localhost:8090/image/upload"
                    :show-file-list="false"
                    :on-success="handleFrontSideSuccess"
                    :before-upload="beforeAvatarUpload"
                    style="margin: 20px 6px"
                >
                  <img v-if="registerForm.form.frontSideUrl" :src="registerForm.form.frontSideUrl" class="avatar" alt="" />
                  <el-icon v-else class="avatar-uploader-icon" style="font-size: 13px">身份证正面</el-icon>
                </el-upload>
              </el-form-item>
              <el-form-item label=" " prop="reverseSideImage">
                <el-upload
                    class="avatar-uploader"
                    action="http://localhost:8090/image/upload"
                    :show-file-list="false"
                    :on-success="handleReverseSideSuccess"
                    :before-upload="beforeAvatarUpload"
                    style="margin: 20px 6px"
                >
                  <img v-if="registerForm.form.reverseSideUrl" :src="registerForm.form.reverseSideUrl" class="avatar" alt="" />
                  <el-icon v-else class="avatar-uploader-icon" style="font-size: 13px">身份证背面</el-icon>
                </el-upload>
              </el-form-item>
            </el-space>
            <el-form-item>
              <el-button type="primary" @click="last" style="margin: 0 -35px 0 15px">上一步</el-button>
              <el-button type="primary" @click="toRegister(registerFormSecondRef)" style="margin-left: 63px">注册</el-button>
              <el-button @click="clearRegisterSecond(registerFormSecondRef)" style="margin-left: 50px">清空</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="registerForm" v-show="active === 2">
          <el-button type="primary" @click="turnToLogin" style="margin: 35px 140px">返回登录页</el-button>
        </div>
      </div>
    </div>

    <div>
      <el-dialog
          title="通过邮箱找回密码"
          v-model="dialogVisible"
          width="25%"
          top="50px"
      >
        <el-form ref="chgPwdFormRef" :rules="chgPwdRules" :model="chgPwdForm.form" label-width="130px" style="margin-left: -65px" hide-required-asterisk>
          <el-form-item label=" " prop="email">
            <el-input
                style="width: 300px; margin: 3px 0"
                v-model="chgPwdForm.form.email"
                autocomplete="off"
                placeholder="邮箱"
            ></el-input>
          </el-form-item>
          <el-form-item label=" " prop="password">
            <el-input
                type="password"
                show-password
                style="width: 300px; margin: 3px 0"
                v-model="chgPwdForm.form.password"
                autocomplete="off"
                placeholder="密码"
            ></el-input>
          </el-form-item>
          <el-form-item label=" " prop="checkPassword">
            <el-input
                type="password"
                show-password
                style="width: 300px; margin: 3px 0"
                v-model="chgPwdForm.form.checkPassword"
                autocomplete="off"
                placeholder="确认密码"
            ></el-input>
          </el-form-item>
          <el-form-item label=" " prop="passCode">
            <el-input
                style="width: 182px; margin: 3px 0"
                v-model="chgPwdForm.form.passCode"
                autocomplete="off"
                placeholder="验证码"
            ></el-input>
            <el-button class="getPassCode" @click="getPassCode(chgPwdForm.form.email)">获取验证码</el-button>
          </el-form-item>

        </el-form>
        <template #footer>
          <div class="dialog-footer" style="margin-top: -25px">
            <el-button @click="dialogClear(chgPwdFormRef)">清空</el-button>
            <el-button type="primary" @click="submitChgPwd(chgPwdFormRef)">确定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script lang="ts" setup>
import request from '@/request/request'
import type {FormInstance, FormRules, UploadProps} from 'element-plus'
import {ElMessage} from 'element-plus'
import {useRouter} from "vue-router";
import {setRoutes} from "@/router";
import {useStore} from "@/stores";
import {identityCardCheck} from "@/type/identityCardCheck";

const validateCheckPwdInRegister = (rule: any, value: any, callback: any) => {
  if (!value) {
    callback(new Error("请输入确认密码"));
    return;
  }
  if (value !== registerForm.form.password) {
    callback(new Error("两次密码不相同"));
    return;
  }
  callback();
}

const validateCheckPwdInChgPwd = (rule: any, value: any, callback: any) => {
  if (!value) {
    callback(new Error("请输入确认密码"));
    return;
  }
  if (value !== chgPwdForm.form.password) {
    callback(new Error("两次密码不相同"));
    return;
  }
  callback();
}

const validateEmail = (rule: any, value: any, callback: any) => {
  if (!value) {
    callback(new Error("请输入邮箱"));
  }
  const regExp = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.(com|cn|net)$/;
  regExp.test(value) ? callback() : callback(new Error("请输入正确的邮箱地址"));
}

const identityCardCheckInForm = (rule: any, value: any, callback: any) => {
  if (!value) {
    callback(new Error("请输入身份证号"));
  }
  let errorMsg = identityCardCheck(value);
  if (errorMsg !== "") {
    callback(new Error(errorMsg));
  }
  callback();
}

const checkPhone = (rule: any, value: any, callback: any) => {
  if (!value) {
    callback(new Error('请输入手机号码'));
  }
  const phoneReg = /^1[3|4|5|7|8][0-9]{9}$/;
  phoneReg.test(value) ? callback() : callback(new Error('请输入正确的手机号码'));
}

const loginRules = reactive<FormRules>({
  userName: [
    {
      required: true,
      message: '请输入用户名',
      trigger: 'blur',
    },
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: 'blur',
    },
  ],
})

const registerFirstRules = reactive<FormRules>({
  email: [
    {
      validator: validateEmail,
      trigger: 'blur'
    },
  ],
  telephone: [
    {
      validator: checkPhone,
      trigger: 'blur'
    }
  ],
  userName: [
    {
      required: true,
      message: '请输入用户名',
      trigger: 'blur',
    },
    {
      min: 4,
      max: 15,
      message: '用户名长度需要在4-15位之间',
      trigger: 'change'
    },
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: 'blur',
    },
    {
      min: 6,
      max: 18,
      message: '密码长度需要在6-18位之间',
      trigger: 'change'
    },
  ],
  checkPassword: [
    {
      validator: validateCheckPwdInRegister,
      trigger: 'blur',
    },
  ],
  passCode: [
    {
      required: true,
      message: '请输入验证码',
      trigger: 'blur',
    },
    {
      len: 6,
      message: '验证码必须为6位',
      trigger: 'change'
    },
  ],
})

const registerSecondRules = reactive<FormRules>({
  identityCard: [
    {
      validator: identityCardCheckInForm,
      trigger: 'blur',
    },
  ],
  frontSideImage: [
    {
      required: true,
      message: '请上传身份证正面照',
      trigger: ['change', 'blur'],
    },
  ],
  reverseSideImage: [
    {
      required: true,
      message: '请上传身份证背面照',
      trigger: ['change', 'blur'],
    },
  ],
})

const chgPwdRules = reactive<FormRules>({
  email: [
    {
      validator: validateEmail,
      trigger: 'blur'
    },
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: 'blur',
    },
    {
      min: 6,
      max: 18,
      message: '密码长度需要在6-18位之间',
      trigger: 'change'
    },
  ],
  checkPassword: [
    {
      validator: validateCheckPwdInChgPwd,
      trigger: 'blur',
    },
  ],
  passCode: [
    {
      required: true,
      message: '请输入验证码',
      trigger: 'blur',
    },
  ]
})

const loginItem = () => ({
  form: {
    userName: "",
    password: "",
  }
})

const registerItem = () => ({
  form: {
    email: "",
    telephone: "",
    userName: "",
    password: "",
    checkPassword: "",
    passCode: "",
    identityCard: "",
    frontSideUrl: "",
    frontSideImage: "",
    reverseSideUrl: "",
    reverseSideImage: "",
  }
})

const chgPwdItem = () => ({
  form: {
    email: "",
    password: "",
    checkPassword: "",
    passCode: "",
  }
})

const router = useRouter();
const store = useStore();
const loginFormRef = ref<FormInstance>();
const registerFormFirstRef = ref<FormInstance>();
const registerFormSecondRef = ref<FormInstance>();
const chgPwdFormRef = ref<FormInstance>();
const loginForm = reactive(loginItem());
const registerForm = reactive(registerItem());
const chgPwdForm = reactive(chgPwdItem());
const loginTextCss = reactive({
  fontWeight: "bold",
  borderBottom: "3px solid red",
})
const registerTextCss = reactive({
  fontWeight: "normal",
  borderBottom: "3px solid white",
})
const isLogin = ref(true);
const dialogVisible = ref(false);
const active = ref(0);

//  注册界面下一步
const next = (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return;
  }
  formEl.validate((valid, fields) => {
    if (valid) {
      ++active.value;
    } else {
      ElMessage.error("填写邮箱密码尚未完成");
    }
  })
}

//  注册界面上一步
const last = () => {
  --active.value;
}

//  身份证正面图片上传成功后赋值
const handleFrontSideSuccess: UploadProps['onSuccess'] = (res, row) => {
  if (res.code === 200) {
    registerForm.form.frontSideUrl = res.data.url;
    registerForm.form.frontSideImage = res.data.id;
  } else {
    ElMessage.error(res.msg);
  }
}

//  身份证背面图片上传成功后赋值
const handleReverseSideSuccess: UploadProps['onSuccess'] = (res, row) => {
  if (res.code === 200) {
    registerForm.form.reverseSideUrl = res.data.url;
    registerForm.form.reverseSideImage = res.data.id;
  } else {
    ElMessage.error(res.msg);
  }
}

//  图片上传条件
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  let types = ["image/jpg", "image/jpeg", "image/png"];
  if (!types.includes(rawFile.type)) {
    ElMessage.error("上传头像图片只能是 JPG 格式或 PNG 格式!");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("上传头像图片大小不能超过 2MB!");
    return false;
  }
  return true;
}

//  切换至登录界面
const turnToLogin = () => {
  clearLogin(loginFormRef.value);
  isLogin.value = true;
  loginTextCss.fontWeight = "bold";
  loginTextCss.borderBottom = "3px solid red";
  registerTextCss.fontWeight = "normal";
  registerTextCss.borderBottom = "3px solid white";
}

//  切换至注册界面
const turnToRegister = () => {
  clearRegister(registerFormFirstRef.value);
  clearRegisterSecond(registerFormSecondRef.value);
  isLogin.value = false;
  loginTextCss.fontWeight = "normal";
  loginTextCss.borderBottom = "3px solid white";
  registerTextCss.fontWeight = "bold";
  registerTextCss.borderBottom = "3px solid red";
}

//  登录
const toLogin = (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return;
  }
  formEl.validate((valid, fields) => {
    if (valid) {
      request.post("/user/login", loginForm.form).then((res: any) => {
        if (res.code === 200) {
          ElMessage.success("登录成功");
          localStorage.setItem("user_info", JSON.stringify(res.data.user_info));
          store.nowUser.form = res.data.user_info;
          setRoutes();
          router.push("/");
        } else {
          ElMessage.error(res.msg);
        }
      })
    } else {
      ElMessage.error("登录输入内容有误");
    }
  })
}

//  清空登录表
const clearLogin = (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return;
  }
  formEl.resetFields();
  // Object.assign(loginForm, loginItem());
}

//  注册
const toRegister = (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return;
  }
  formEl.validate((valid, fields) => {
    if (valid) {
      request.post("/user/register", registerForm.form).then((res: any) => {
        if (res.code === 200) {
          ElMessage.success("注册成功");
          ++active.value;
        } else {
          ElMessage.error(res.msg);
        }
      })
    } else {
      ElMessage.error("身份信息有误");
    }
  })
}

//  清空注册表
const clearRegister = (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return;
  }
  formEl.resetFields();
  // Object.assign(registerForm, registerItem());
}

//  清空注册表第二部分
const clearRegisterSecond = (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return;
  }
  formEl.resetFields(['identityCard', 'frontSideUrl', 'frontSideImage', 'reverseSideUrl', 'reverseSideImage']);
  // Object.assign(registerForm, registerItem());
}

//  发送邮箱验证码
const getPassCode = (email: string) => {
  request.get('/mail/sendPassCode', {
    params: {
      mailbox: email
    }
  }).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("发送验证码成功");
    } else {
      ElMessage.error(res.msg);
    }
  })
};

//  重置密码界面
const preChgPwd = () => {
  dialogVisible.value = true;
  dialogClear(chgPwdFormRef.value);
}

//  清空重置密码表
const dialogClear = (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return;
  }
  formEl.resetFields();
  // Object.assign(chgPwdForm, chgPwdItem());
}

//  提交密码表
const submitChgPwd = (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return;
  }
  formEl.validate((valid, fields) => {
    if (valid) {
      request.post('/user/chgPwd', chgPwdForm.form).then((res: any) => {
        if (res.code === 200) {
          ElMessage.success("修改密码成功！");
        } else {
          ElMessage.error(res.msg);
        }
        dialogVisible.value = false;
      })
    } else {
      ElMessage.error("重置密码输入有误");
    }
  })
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

.entrance {
  position: relative;
  height: 100vh;
  background-image: url("../assets/entranceImage.jpg");
  background-size: 100% 100vh;
  background-repeat: no-repeat;
  margin-left: -501px;
  width: 2219px;
}

.entranceLogo {
  width: 750px;
  height: 500px;
  margin: 30px 0 0 790px;
}

.container {
  position: absolute;
  margin-top: 20px;
  width: 370px;
  height: 400px;
  left: 50%;
  top: 50%;
  transition: all 0.5s;
  transform: translate(-50%, -50%);
  background-color: white;
  opacity: 0.9;
  border-radius: 0 0 30px 30px;
  box-shadow: 0 0 15px 0 rgba(0, 0, 0, 0.2);
}

.loginText, .registerText {
  margin: 15px 40px;
  font-family: STKaiti, serif;
  font-size: 24px;
  cursor: pointer;
  padding-bottom: 10px;
}

.login, .register {
  overflow: hidden;
  margin: 20px 0;
  width: 370px;
  height: 500px;
  background-color: white;
  transition: all 0.5s;
  border-top: 1px dashed #bbbbbb;
  border-radius: 0 0 30px 30px;
}

.login {
  height: 310px;
}

.loginForm {
  margin: 55px 0 0 0;
}

.forgetPwd {
  width: 145px;
  font-family: serif;
  font-size: 13px;
  margin-left: 76px;
  cursor: pointer;
}

.register {
  margin: 20px 0;
}

.registerForm {
  margin: 35px 0 0 0;
}

.getPassCode {
  margin-left: 15px;
}

.avatar-uploader .avatar {
  width: 80px;
  height: 80px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  text-align: center;
}
</style>
