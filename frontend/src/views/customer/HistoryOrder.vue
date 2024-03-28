<template>
  <div class="historyOrder">
    <div v-if="showList.length > 0" v-infinite-scroll="load" class="infinite-list">
      <div v-for="(item, index) in showList" :key="index" class="infinite-list-item" style="margin: 20px 0 0 300px; padding-top: 20px; width: 1560px">
        <el-card>
          <el-space>
            <img :src="getImageUrl(item.url)" alt="" style="width: 400px; height: 260px; margin: -20px 0 -25px -20px" />
            <div style="margin-left: 255px">
              <h1 style="font-size: 45px; font-weight: bolder; color: grey; margin-top: -10px">{{item.carSpecificName}}</h1>
              <h1 style="font-size: 30px; font-weight: bolder">{{item.licensePlate}}</h1>
              <div>
                <el-space>
                  <h1 style="font-size: 20px; font-weight: bolder">起始时间&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</h1>
                  <h1>{{dateFormat(item.takeDate)}}</h1>
                  <h1 style="font-size: 20px; font-weight: bolder; margin-left: 50px">取车地点&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</h1>
                  <h1>{{item.takePlace}}</h1>
                </el-space>
              </div>
              <div>
                <el-space>
                  <h1 style="font-size: 20px; font-weight: bolder">结束时间&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</h1>
                  <h1>{{dateFormat(item.returnDateTrue)}}</h1>
                  <h1 style="font-size: 20px; font-weight: bolder; margin-left: 50px">还车地点&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</h1>
                  <h1>{{item.returnPlace}}</h1>
                </el-space>
              </div>
              <div>
                <el-space>
                  <h1 style="font-size: 20px; font-weight: bolder">所付最终金额&nbsp&nbsp&nbsp</h1>
                  <h1 style="font-weight: bolder; font-style: italic; color: red">￥ {{item.finalAmount}}</h1>
                </el-space>
              </div>
            </div>
            <div style="margin-left: 100px">
              <div>
                <el-tag v-if="item.status === 'CANCEL'" type="warning" size="large" style="font-size: 25px">取消</el-tag>
                <el-tag v-else type="success" size="large" style="font-size: 25px">结束</el-tag>
              </div>
              <el-button type="danger" @click="turnToTheCarPage(item)" style="width: 120px; height: 40px; font-size: 25px; margin-top: 20px">
                查看车辆
              </el-button>
            </div>
          </el-space>
        </el-card>
      </div>
    </div>
    <div v-else>
      <el-card shadow="hover" style="top: 20px; width: 500px; height: 400px; border: 2px solid gainsboro; left: 50%; transform: translate(-50%, 50%)">
        <h1 style="text-align: center; line-height: 350px">暂 无 历 史 订 单 信 息</h1>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import request from "@/request/request";
import {CarStatus, OrderStatus} from "@/type/dataDefinition";
import {ElMessage} from "element-plus";
import {useStore} from "@/stores";

const searchItem = () => ({
  carTypeId: "",
  carNameId: "",
  carSpecificName: "",
  carTag: "",
  licensePlate: "",
  rented: false,
  status: "Normal" as CarStatus,
  statusLabel: "",
})

const router = useRouter();
const store = useStore();
const nowUser = reactive({form: store.nowUser.form});
const currentPage = ref(1);
const pageSize = ref(3);
const total = ref(0);
const showList = ref([] as any);

//  时间格式化
const dateFormat = (e: any) => {
  if (e) {
    return e.substring(0, 10);
  }
}

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href
}

//  无限滚动加载新数据
const load = () => {
  if (currentPage.value * pageSize.value <= total.value) {
    ++currentPage.value;
    getData();
  }
}

//  跳转到指定车辆信息界面
const turnToTheCarPage = (item: any) => {
  request.get("/car/getOne", {
    params: {
      carId: item.carId,
    }
  }).then((res: any) => {
    if (res.code === 200) {
      store.chosenCar = res.data;
      router.push("/customer/customerOneCarPage");
    } else {
      ElMessage.error(res.msg);
    }
  })
}

//  获取数据
const getData = () =>  {
  request.get("/order/getPages", {
    params: {
      userId: nowUser.form.id,
      status: OrderStatus.Wrong,
      current: currentPage.value,
      size: pageSize.value
    }
  }).then((res: any) => {
    if (res.code === 200) {
      showList.value = [...showList.value, ...res.data.page?.records] as [];
      total.value = res.data.page?.total;
    } else {
      ElMessage.error(res.msg);
    }
  })
}
getData();
</script>

<style>

</style>