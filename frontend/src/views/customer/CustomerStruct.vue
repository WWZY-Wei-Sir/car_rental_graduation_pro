<template>
  <div class="customerStruct">
    <el-container class="layout-container-demo" style="margin-left: -521px; width: 2238px; height: 100vh">
      <el-header>
        <CustomerAside />
      </el-header>

      <el-main style="margin-left: 25px">
        <router-view />
      </el-main>

      <el-footer>
        <h2 style="text-align: center; font-weight: bolder; margin-top: 10px">@2023 汽车租赁管理系统</h2>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import CustomerAside from "../../components/customer/CustomerAside.vue";
</script>

<style scoped>

</style>