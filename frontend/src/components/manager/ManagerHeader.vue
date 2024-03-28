<template>
  <div style="background-color: #E6E6FA; margin: 0 -20px">
    <div class="toolbar">
      <Fold style="width: 2em; height: 2em; margin-right: 1935px; cursor: pointer" @click="changeIsCollapse" v-show="!isCollapse"/>
      <Expand style="width: 2em; height: 2em; margin-right: 2070px; cursor: pointer" @click="changeIsCollapse" v-show="isCollapse"/>
      <div style="height: 60px; line-height: 60px; text-align: center; margin-left: -200px">
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
        <span style="margin-left: 15px; top: -10px; font-size: 20px; font-weight: bolder">{{nowUser.form.userName}}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {Setting} from '@element-plus/icons-vue';
import {Expand, Fold} from "@element-plus/icons";
import {useRouter} from "vue-router";
import {useStore} from "@/stores";
import {UserType, UserStatus} from "@/type/dataDefinition";

const router = useRouter();
const store = useStore();
const isCollapse = ref(false);
const emit = defineEmits(['changeIsCollapse']);
const nowUser = reactive({form: store.nowUser.form});

watch(store.nowUser, (newVal) => {
  nowUser.form = newVal.form;
}, {
  deep: true,
});

// 菜单折叠
const changeIsCollapse = () => {
  isCollapse.value = !isCollapse.value;
  emit('changeIsCollapse', isCollapse.value);
}

//  退出
const logout = () => {
  localStorage.removeItem("user_info");
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

<style>
.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style>