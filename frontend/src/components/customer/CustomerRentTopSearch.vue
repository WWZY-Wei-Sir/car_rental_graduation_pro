<template>
  <div class="customerRentTopSearch">
    <el-space style="margin: 20px 0 0 390px">
      <el-avatar
          :src="getImageUrl('../../assets/logo.png')"
          :size="200"
          style="background-color: white; box-shadow: 0 0 10px grey"
      />
      <div>
        <el-input
            style="width: 800px; height: 65px; font-size: larger; margin-left: 235px; border: 3px solid orange"
            autocomplete="off"
            placeholder="搜索  汽车类别  品牌  信息"
            v-model="context"
        ></el-input>
        <el-button type="warning" @click="toSearch(context)" style="width: 100px; height: 65px; font-size: larger; margin-left: -9px">搜索</el-button>
        <div>
          <el-button @click="toSearch('轿车')" :link="true" style="height: 50px; font-size: larger; margin-left: 245px">轿车</el-button>
          <el-button @click="toSearch('SUV')" :link="true" style="height: 50px; font-size: larger; margin-left: 30px">SUV</el-button>
          <el-button @click="toSearch('雷克萨斯')" :link="true" style="height: 50px; font-size: larger; margin-left: 30px">雷克萨斯</el-button>
          <el-button @click="toSearch('保时捷')" :link="true" style="height: 50px; font-size: larger; margin-left: 30px">保时捷</el-button>
          <el-button @click="toSearch" :link="true" style="height: 50px; font-size: larger; margin-left: 30px">特价优惠</el-button>
        </div>
      </div>
    </el-space>
  </div>
</template>

<script setup lang="ts">
import {useStore} from "@/stores";
import {useRouter} from "vue-router";

const router = useRouter();
const store = useStore();
const context = ref("");

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href
}

//  搜索
const toSearch = (e: any) => {
  store.search.form.context = e;
  store.setLastPage(1);
  router.push("/customer/customerSearchPage");
}
</script>

<style>

</style>