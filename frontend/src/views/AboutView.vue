

<template>
  <div class="about">
    <h1>This is an about page</h1>
    <el-button @click="qwer" type="primary">qwer{{a}}</el-button>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { Menu as IconMenu, Message, Setting } from '@element-plus/icons-vue'
import request from '@/request/request'
import {ElMessage} from 'element-plus'

const  qwer=async()=> {
  let postForm = {
    userName: "321951493@qq.com",
    password: "123456"
  };
  request.post('/user/login', postForm).then((res) => {
    ElMessage.success("登录成功")
    console.log(res);
  })
  // const res: result = await request.post('/user/login', postForm);
  // if (res.code !== 200) {
  //   ElMessage.error(res.msg);
  //   return;
  // }
  // console.log(res)
}
</script>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
