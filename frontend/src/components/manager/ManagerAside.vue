<template>
  <div>
    <el-menu
        class="el-menu-vertical-demo"
        style="min-height: 100vh; overflow-x: hidden"
        background-color="#F5F5DC"
        text-color="black"
        active-text-color="#A52A2A"
        :collapse="isCollapse"
        :collapse-transition="false"
        unique-opened
        router
    >
      <div style="height: 60px; line-height: 60px; text-align: center">
        <img
            src="../../assets/logo.png"
            alt=""
            style="
                width: 50px;
                position: relative;
                top: 15px;
                margin-right: 5px;
                background-color: transparent;
              "
        />
        <b style="color: #DC143C" v-show="!isCollapse">汽车租赁管理系统</b>
      </div>
      <el-menu-item index="1" route="/">
        <el-icon><icon-menu /></el-icon>
        <template #title>主 页</template>
      </el-menu-item>
      <el-sub-menu index="2">
        <template #title>
          <el-icon><Bicycle /></el-icon>
          <span>汽 车 管 理</span>
        </template>
        <el-menu-item index="2-1" route="/admin/carTypeManager" v-if="nowUser.form.userType === 'Admin'">
          <template #title>汽 车 类 别</template>
        </el-menu-item>
        <el-menu-item index="2-2" route="/admin/carNameManager" v-if="nowUser.form.userType === 'Admin'">
          <template #title>汽 车 品 牌</template>
        </el-menu-item>
        <el-menu-item index="2-3" route="/admin/carManager">
          <template #title>汽 车 信 息</template>
        </el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="3">
        <template #title>
          <el-icon><UserFilled /></el-icon>
          <span>用 户 管 理</span>
        </template>
        <el-menu-item index="3-1" route="/admin/userManager" v-if="nowUser.form.userType === 'Admin'">
          <template #title>用 户 信 息</template>
        </el-menu-item>
        <el-menu-item index="3-2" route="/admin/reportManager" v-if="nowUser.form.userType === 'Admin'">
          <template #title>举 报 处 理</template>
        </el-menu-item>
      </el-sub-menu>
      <el-menu-item index="4" route="/admin/orderManager">
        <el-icon><List /></el-icon>
        <template #title>订 单 管 理</template>
      </el-menu-item>
      <el-menu-item index="5" route="/admin/rotateImageManager">
        <el-icon><Film /></el-icon>
        <template #title>轮 播 图 管 理</template>
      </el-menu-item>
      <el-menu-item index="6" route="/admin/messageManager">
        <el-icon><Bell /></el-icon>
        <template #title>交 互 界 面</template>
      </el-menu-item>
      <el-menu-item index="7" route="/admin/echartsManager">
        <el-icon><DataAnalysis /></el-icon>
        <template #title>图 表 管 理</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script lang="ts" setup>
import {Menu as IconMenu,} from '@element-plus/icons-vue';
import {Bell, Bicycle, DataAnalysis, Film, List, UserFilled} from "@element-plus/icons";
import {useStore} from "@/stores";

defineProps({
  isCollapse: Boolean,
})

const store = useStore();
const nowUser = reactive({form: store.nowUser.form} as any);
</script>

<style scoped>

</style>