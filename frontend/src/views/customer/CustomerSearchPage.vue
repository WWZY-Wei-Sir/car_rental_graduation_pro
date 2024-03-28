<template>
  <div class="customerSearchPage">
    <CustomerRentTopSearch />
    <el-space style="margin: 20px 0 0 500px">
      <p style="font-size: 35px">{{store.search.form.context}}&nbsp;&nbsp;&nbsp;></p>
      <el-button :link="true" type="primary" style="font-size: larger; margin: 5px 0 0 20px" @click="turnToCustomerRent">返回租赁首页</el-button>
    </el-space>
    <el-card shadow="hover" style="top: 20px; width: 500px; height: 400px; border: 2px solid gainsboro; left: 50%; transform: translate(-50%)" v-if="showComponent">
      <h1 style="text-align: center; line-height: 350px">暂 无 符 合 条 件 的 车 辆 信 息</h1>
    </el-card>
    <el-space>
      <el-card shadow="hover" style="width: 565px; height: 630px; border: 2px solid gainsboro; margin: 20px 0 0 113px" v-for="item in tableData">
        <img :src="getImageUrl(item.url)" alt="" style="width: 565px; height: 350px; margin: -20px 0 0 -20px" />
        <h1 style="top: 20px">{{item.carSpecificName}}</h1>
        <el-space style="top: 25px">
          <h3 style="font-size: 20px">{{item.carTag}}</h3>
          <el-tag effect="dark" size="large" style="font-size: 18px; margin-left: 100px">
            {{item.carName}}
          </el-tag>
        </el-space>
        <h1 style="width: 500px; height: 65px; font-size: 30px; font-weight: bolder; font-style: italic; color: red; margin: 40px 0 8px 0; text-align: center">
          ￥ {{item.dailyRent}}
        </h1>
        <el-button type="danger" @click="turnToTheCarPage(item)" style="width: 300px; height: 65px; font-size: 30px; display: block; left: 50%; transform: translate(-50%)">
          查看详情
        </el-button>
      </el-card>
    </el-space>
    <div class="demo-pagination-block" v-if="!showComponent">
      <el-pagination
          background
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          style="font-size: 20px; top: 55px; right: 100px; float: right"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {useRouter, useRoute} from "vue-router";
import CustomerRentTopSearch from '../../components/customer/CustomerRentTopSearch.vue';
import request from "@/request/request";
import {CarStatus} from "@/type/dataDefinition";
import {ElMessage} from "element-plus";
import {useStore} from "@/stores";

const router = useRouter();
const store = useStore();
const nowCarType = ref("");
const nowCarTypeId = ref("");
const currentPage = ref(1);
const pageSize = ref(3);
const total = ref(0);
const tableData = ref([]);
const showComponent = ref(false);

//  初始化路由跳转传参数据
const initParams = () => {
  console.log(store.chosenCarType)
  nowCarType.value = store.chosenCarType.nowCarType;
  nowCarTypeId.value = store.chosenCarType.nowCarTypeId;
  currentPage.value = store.getLastPage();
}
initParams();

//  表格切换每页展示数据数
const handleSizeChange = (size: number) => {
  pageSize.value = size;
  getData();
}

//  表格切换到第几页
const handleCurrentChange = (current: number) => {
  currentPage.value = current;
  getData();
}

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href
}

//  跳转到租赁首页
const turnToCustomerRent = () => {
  store.setLastPage(currentPage.value);
  router.push("/customer/customerRent");
}

//  跳转到指定车辆信息界面
const turnToTheCarPage = (car: any) => {
  store.chosenCar = car;
  store.setLastPage(currentPage.value);
  router.push("/customer/customerOneCarPage");
}

watch(store.search.form, (newVal) => {
  getData();
}, {
  deep: true,
})

//  获取数据
const getData = () =>  {
  request.get("/car/getSearchPages", {
    params: {
      context: store.search.form.context,
      current: currentPage.value,
      size: pageSize.value
    }
  }).then((res: any) => {
    if (res.code === 200) {
      tableData.value = res.data.page?.records;
      total.value = res.data.page?.total;
      tableData.value.forEach((e: any) => {
        // @ts-ignore
        e.statusLabel = CarStatus[e.status];
      });
      showComponent.value = total.value === 0;
    } else {
      ElMessage.error(res.msg);
    }
  })
}
getData();
</script>

<style>

</style>