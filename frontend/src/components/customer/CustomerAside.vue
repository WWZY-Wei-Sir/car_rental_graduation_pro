<template>
  <div class="customerAside">
    <el-space>
      <el-menu
          class="el-menu-demo"
          mode="horizontal"
          style="justify-content: center; min-width: 2192px"
          router
      >
        <div style="height: 60px; line-height: 60px; text-align: center">
          <h1 style="font-weight: bolder">汽 车 租 赁 平 台</h1>
        </div>
        <el-menu-item index="1" route="/home" style="margin-left: 550px; font-size: 20px">首页</el-menu-item>
        <el-menu-item index="2" route="/customer/customerRent" style="font-size: 20px">租赁界面</el-menu-item>
        <el-menu-item index="3" route="/customer/currentOrder" style="font-size: 20px">当前订单</el-menu-item>
        <el-menu-item index="4" route="/customer/historyOrder" style="font-size: 20px">历史订单</el-menu-item>
        <el-menu-item index="5" route="/customer/customerMessage" style="font-size: 20px">消息通知</el-menu-item>

        <div style="height: 60px; line-height: 60px; text-align: center; margin-left: 590px">
          <el-dropdown style="top: 22px">
            <el-icon style="margin-right: 8px; margin-top: 1px"
            ><setting
            /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="toPersonal">个人信息</el-dropdown-item>
                <el-dropdown-item @click="logout">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-avatar
              :src="getImageUrl(nowUser.form.url)"
              :size="30"
              style="background-color: white; box-shadow: 0 0 10px grey; margin: 15px 0 0 10px"
          />
          <span style="margin-left: 15px; top: -10px; font-size: 22px; font-weight: bolder">{{nowUser.form.userName}}</span>
        </div>
      </el-menu>
    </el-space>
  </div>
</template>

<script setup lang="ts">
import {Setting} from '@element-plus/icons-vue'
import {useRouter} from "vue-router";
import {useStore} from "@/stores";
import {UserType, UserStatus} from "@/type/dataDefinition";

const router = useRouter();
const store = useStore();
const nowUser = reactive({form: store.nowUser.form});

watch(store.nowUser, (newVal) => {
  nowUser.form = newVal.form;
}, {
  deep: true,
});

//  退出
const logout = () => {
  localStorage.removeItem("user_info");
  store.nowUser.form = {};
  router.push("/entrance");
}

//  跳转到个人信息界面
const toPersonal = () => {
  // @ts-ignore
  store.nowUser.form.userTypeLable = UserType[store.nowUser.form.userType];
  // @ts-ignore
  store.nowUser.form.statusLable = UserStatus[store.nowUser.form.status];
  router.push("/personal");
}

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href
}
</script>

<style scoped>

</style>