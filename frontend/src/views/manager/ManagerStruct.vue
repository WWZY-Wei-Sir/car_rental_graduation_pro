<template>
  <div class="managerStruct">
    <el-container class="layout-container-demo" style="margin-left: -502px; width: 2219px; height: 100vh">
      <el-aside :width="isCollapse ? '64px':'200px'" style="
      background-color: rgb(238, 241, 246);
      box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
      ">
        <el-scrollbar>
          <ManagementAside :isCollapse="isCollapse"></ManagementAside>
        </el-scrollbar>
      </el-aside>

      <el-container style="margin-left: 2px">
        <el-header style="text-align: right; font-size: 12px">
          <ManagerHeader @changeIsCollapse="changeIsCollapse" />
        </el-header>

        <el-main>
          <el-scrollbar>
            <router-view />
          </el-scrollbar>
        </el-main>

        <el-footer>
          <h2 style="text-align: center; font-weight: bolder; margin-top: 10px">@2023 汽车租赁管理系统</h2>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue';
import ManagementAside from "../../components/manager/ManagerAside.vue";
import ManagerHeader from "@/components/manager/ManagerHeader.vue";

const isCollapse = ref(false);

const changeIsCollapse = (v: any) => {
  isCollapse.value = v;
}
</script>

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  color: var(--el-text-color-primary);
  border-bottom: 1px solid grey;
}
.layout-container-demo .el-menu {
  border-right: none;
}
.layout-container-demo .el-main {
  padding: 0;
}
.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>
