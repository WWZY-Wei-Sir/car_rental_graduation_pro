<template>
  <div class="personal">
    <el-card class="box-card" style="width: 600px; height: 800px; left: 50%; transform: translate(-50%, 15%)">
      <el-avatar
          :src="getImageUrl(nowUser.form.url)"
          :size="200"
          style="background-color: white; box-shadow: 0 0 10px grey; margin: 20px 0 30px 175px"
      />
      <div style="margin-top: 15px">
        <el-space>
          <h1 style="font-weight: bolder">身份证:&nbsp;&nbsp;&nbsp;</h1>
          <h2>{{showIdentityCard(nowUser.form.identityCard)}}</h2>
        </el-space>
      </div>
      <div style="margin-top: 15px">
        <el-space>
          <h1 style="font-weight: bolder">用户名:&nbsp;&nbsp;&nbsp;</h1>
          <h2>{{nowUser.form.userName}}</h2>
        </el-space>
      </div>
      <div style="margin-top: 15px">
        <el-space>
          <h1 style="font-weight: bolder">用户邮箱:&nbsp;&nbsp;&nbsp;</h1>
          <h2>{{nowUser.form.email}}</h2>
        </el-space>
      </div>
      <div style="margin-top: 15px">
        <el-space>
          <h1 style="font-weight: bolder">用户电话:&nbsp;&nbsp;&nbsp;</h1>
          <h2>{{nowUser.form.telephone}}</h2>
        </el-space>
      </div>
      <div v-if="nowUser.form.userType === 'Customer'" style="margin-top: 15px">
        <el-space>
          <h1 style="font-weight: bolder">余额:&nbsp;&nbsp;&nbsp;</h1>
          <h2 style="font-weight: bolder; font-style: italic; color: red">￥ {{nowUser.form.balance}}</h2>
          <el-button type="danger" @click="preCharge" size="large" style="margin-left: 100px; font-size: 18px">充值</el-button>
        </el-space>
      </div>
      <div style="margin-top: 15px">
        <el-space>
          <h1 style="font-weight: bolder">用户类型:&nbsp;&nbsp;&nbsp;</h1>
          <el-tag size="large" style="font-size: large">{{nowUser.form.userTypeLabel}}</el-tag>
        </el-space>
      </div>
      <div style="margin-top: 15px">
        <el-space>
          <h1 style="font-weight: bolder">用户状态:&nbsp;&nbsp;&nbsp;</h1>
          <el-tag type="warning" size="large" style="font-size: large">{{nowUser.form.statusLabel}}</el-tag>
        </el-space>
      </div>
      <el-button @click="preChgPwd" type="warning" size="large" style="font-size: larger; margin-top: 50px">修改密码</el-button>
      <el-button @click="preEdit" type="primary" size="large" style="font-size: larger; margin: 50px 0 0 100px">详情信息</el-button>
    </el-card>

    <el-dialog
        title="充值页面"
        v-model="chargeDialogVisible"
        width="30%"
        top="50px"
    >
      <el-form :rules="rules" :model="chargeForm" label-width="100px">
        <el-form-item label="邮箱:" prop="email">
          <span style="width: 300px; font-size: large">{{chargeForm.form.email}}</span>
        </el-form-item>
        <el-form-item label="充值金额" prop="balance">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="chargeForm.form.balance"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="chargeForm.form.password"
              autocomplete="off"
              type="password"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="chargeDialogCancel">取 消</el-button>
          <el-button type="primary" @click="submitCharge">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
        title="个人信息"
        v-model="dialogVisible"
        width="30%"
        top="50px"
    >
      <el-upload
          class="avatar-uploader"
          action="http://localhost:8090/image/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          style="margin: 20px 150px"
      >
        <img v-if="editForm.form.url" :src="editForm.form.url" class="avatar" alt="" style="width: 320px; height: 260px" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
      <el-form :rules="rules" :model="editForm" label-width="100px">
        <el-form-item label="身份证:" prop="identityCard">
          <span style="width: 300px; font-size: large">{{showIdentityCard(editForm.form.identityCard)}}</span>
        </el-form-item>
        <el-form-item label="用户名:" prop="userName">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="editForm.form.userName"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户邮箱:" prop="email">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="editForm.form.email"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户电话" prop="telephone">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="editForm.form.telephone"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item v-if="nowUser.form.userType === 'Customer'" label="余额" prop="balance">
          <span style="width: 300px; font-size: large">{{editForm.form.balance}}</span>
        </el-form-item>
        <el-form-item label="验证码" prop="passCode">
          <el-input
              style="width: 180px; margin: 3px 0"
              v-model="editForm.form.passCode"
              autocomplete="off"
          ></el-input>
          <el-button type="primary" class="getPassCode" @click="getPassCode(editForm.form.email)" style="margin-left: 18px">获取验证码</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogCancel">取 消</el-button>
          <el-button type="primary" @click="submitNewOrEdit">确 定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
        title="通过邮箱修改密码"
        v-model="chgPwdDialogVisible"
        width="30%"
        top="50px"
    >
      <el-form :rules="rules" :model="chgPwdForm" label-width="100px">
        <el-form-item label="邮箱:" prop="email">
          <span style="width: 300px; font-size: large">{{chgPwdForm.form.email}}</span>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="chgPwdForm.form.password"
              autocomplete="off"
              type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="chgPwdForm.form.checkPassword"
              autocomplete="off"
              type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="passCode">
          <el-input
              style="width: 180px; margin: 3px 0"
              v-model="chgPwdForm.form.passCode"
              autocomplete="off"
          ></el-input>
          <el-button type="warning" class="getPassCode" @click="getPassCode(chgPwdForm.form.email)" style="margin-left: 18px">获取验证码</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="chgPwdDialogCancel">取 消</el-button>
          <el-button type="primary" @click="submitChgPwd">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import request from "@/request/request";
import {useStore} from "@/stores";
import type {FormInstance, FormRules, UploadProps} from 'element-plus';
import {Plus} from "@element-plus/icons";
import {ElMessage} from "element-plus";
import {UserStatus, UserType} from "@/type/dataDefinition";
import router from "@/router";

const chargeItem = () => ({
  form: {
    id: "",
    email: "",
    balance: "" as any,
    password: "",
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

const store = useStore();
const nowUser = reactive({form: {} as any} as any);
const editForm = reactive({form: {} as any} as any);
const dialogVisible = ref(false);
const chgPwdDialogVisible = ref(false);
const chargeDialogVisible = ref(false);
const chargeForm = reactive(chargeItem());
const chgPwdForm = reactive(chgPwdItem());

//  身份证显示策略
const showIdentityCard = (e: string) => {
  if (e) {
    return e.substring(0, 3) + "***********" + e.substring(14, 18);
  }
}

//  获取用户最新信息
const getNowUser = (id: string) => {
  request.get("/user/getOne", {params: {userId: id}}).then((res: any) => {
    if (res.code === 200) {
      store.nowUser.form = res.data;
      // @ts-ignore
      store.nowUser.form.statusLabel = UserStatus[res.data.status];
      // @ts-ignore
      store.nowUser.form.userTypeLabel = UserType[res.data.userType];
      nowUser.form = store.nowUser.form;
    } else {
      ElMessage.error(res.msg);
    }
  });
}
getNowUser(store.nowUser.form.id);

//  初始化数据
const initParams = () => {
  nowUser.form = store.nowUser.form;
}
initParams();

//  充值编辑前
const preCharge = () => {
  Object.assign(chargeForm, chargeItem());
  chargeForm.form.id = nowUser.form.id;
  chargeForm.form.email = nowUser.form.email;
  chargeDialogVisible.value = true;
}

//  关闭充值编辑弹窗
const chargeDialogCancel = () => {
  chargeDialogVisible.value = false;
}

//  编辑前
const preEdit = () => {
  editForm.form = JSON.parse(JSON.stringify(nowUser.form));
  dialogVisible.value = true;
}

//  关闭编辑弹窗
const dialogCancel = () => {
  dialogVisible.value = false;
}

//  修改密码编辑前
const preChgPwd = () => {
  Object.assign(chgPwdForm, chgPwdItem());
  chgPwdForm.form.email = nowUser.form.email;
  chgPwdDialogVisible.value = true;
}

//  关闭修改密码弹窗
const chgPwdDialogCancel = () => {
  chgPwdDialogVisible.value = false;
}

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href
}

//  图片上传成功后赋值
const handleAvatarSuccess: UploadProps['onSuccess'] = (res, row) => {
  if (res.code === 200) {
    editForm.form.url = res.data.url;
    editForm.form.imageId = res.data.id;
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

//  提交表单
const submitNewOrEdit = () => {
  editForm.form.balance = Number(editForm.form.balance) + Number(nowUser.form.balance);
  request.post("/user/chgUserByUser", editForm.form).then((res: any) => {
    if (res.code === 200) {
      ++editForm.form.version;
      nowUser.form = JSON.parse(JSON.stringify(editForm.form));
      store.nowUser.form = JSON.parse(JSON.stringify(editForm.form));
      ElMessage.success("修改成功");
    } else {
      ElMessage.error(res.msg);
    }
    dialogCancel();
  });
}

//  提交充值表单
const submitCharge = () => {
  if (!chargeForm.form.balance) {
    ElMessage.error("充值金额不得为空");
    return;
  }
  if (chargeForm.form.balance <= 0) {
    ElMessage.error("充值金额不得为0或负数");
    return;
  }
  chargeForm.form.balance = Number(chargeForm.form.balance) + Number(nowUser.form.balance);
  request.post("/user/charge", chargeForm.form).then((res: any) => {
    if (res.code === 200) {
      store.nowUser.form.balance = chargeForm.form.balance;
      ElMessage.success("充值成功");
    } else {
      ElMessage.error(res.msg);
    }
    chargeForm.form.balance = "";
    chargeDialogCancel();
  });
  chargeForm.form.balance = "";
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

//  提交密码表
const submitChgPwd = () => {
  request.post('/user/chgPwd', chgPwdForm.form).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("修改密码成功，请重新登录！");
      localStorage.removeItem("user_info");
      router.push("/");
    } else {
      ElMessage.error(res.msg);
    }
  })
}
</script>

<style>

</style>