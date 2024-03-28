<template>
  <div class="homePage" v-if="nowUser.form.userType === 'Customer'" style="
        margin: 200px 300px;
        padding: 10px;
        border-radius: 10px;
        background-color: white;
        width: auto;
        height: 495px;
        box-shadow: 0 0 7px rgba(0, 21, 41, 0.35);
      ">
    <div style="line-height: 30px; margin: 35px 40px">
      <h1 style="text-align: center; font-size: 35px; font-weight: bolder">欢迎来到&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;汽车租赁管理系统</h1>
      <div style="margin: 30px 0 0 60px">
        <h1 style="color: red; font-size: 30px; margin-left: 13px">致顾客的温馨提示：</h1>
        <div style="margin: 15px 0 0 0">
          <h1 style="margin-top: 10px">（1）顾客租赁时长超过7天时间的，享受九折优惠；超过30天时间的，享受八折优惠；超过180天时间的，享受六折优惠.</h1>
          <h1 style="margin-top: 10px; color: grey">（2）租赁时间从顾客取车后开始算起，直到顾客还车，不足一天的时间按小时收费，不足一小时按一小时计算。</h1>
          <h1 style="margin-top: 10px">（3）顾客超过还车时间未还车，超过的时间按每小时三倍原来的时租金额外收费，不足一小时按一小时计算。</h1>
          <h1 style="margin-top: 10px; color: grey">（4）顾客在工作人员审核信息完成前进行退款，押金可以全额返还；在工作人员审核信息完成后进行退款，押金只能返还一半。</h1>
          <h1 style="margin-top: 10px">（5）顾客在取车前和还车前都可以更改取车地址和还车地址，但是只有一次机会。</h1>
          <h1 style="margin-top: 10px; color: grey">（6）顾客在还车时间结束前，可以申请延长租赁时间，延长时间不得超过七天，每一次延长都会在最后支付金额的基础上加收20%的费用。</h1>
          <h1 style="margin-top: 10px">（7）顾客未按时取车，租赁时间从约定取车当天晚上18:00开始算起，顾客不能提前取车。</h1>
          <h1 style="margin-top: 10px; color: grey">（8）顾客若提前还车，租赁时间仍然计算到约定还车时间的早上6:00。</h1>
        </div>
      </div>
    </div>
  </div>
  <div v-else style="font-size: 20px; margin: 20px">
    <div>
      <el-row :gutter="10">
        <el-col :span="8">
          <el-card shadow="always" style="height: 195px">
            <div>
              <el-space>
                <h1 style="font-weight: bolder">汽车总数量： </h1>
                <h1 style="color: red">{{carCount.form.total}}</h1>
              </el-space>
            </div>
            <div>
              <el-space>
                <h1 style="font-weight: bolder">被租赁的汽车数： </h1>
                <h1 style="color: orange">{{carCount.form.rented}}</h1>
              </el-space>
            </div>
            <div>
              <el-space>
                <h1 style="font-weight: bolder">维修中的汽车数： </h1>
                <h1 style="color: red">{{carCount.form.maintenance}}</h1>
              </el-space>
            </div>
            <div>
              <el-space>
                <h1 style="font-weight: bolder">年检中的汽车数： </h1>
                <h1 style="color: orange">{{carCount.form.inspection}}</h1>
              </el-space>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="always" style="height: 195px">
            <div>
              <el-space>
                <h1 style="font-weight: bolder">订单总数量： </h1>
                <h1 style="color: red">{{orderCount.form.total}}</h1>
              </el-space>
            </div>
            <div>
              <el-space>
                <h1 style="font-weight: bolder">进行中的订单数： </h1>
                <h1 style="color: orange">{{orderCount.form.running}}</h1>
              </el-space>
            </div>
            <div>
              <el-space>
                <h1 style="font-weight: bolder">被取消的订单数： </h1>
                <h1 style="color: red">{{orderCount.form.cancel}}</h1>
              </el-space>
            </div>
            <div>
              <el-space>
                <h1 style="font-weight: bolder">完成的订单数： </h1>
                <h1 style="color: orange">{{orderCount.form.finish}}</h1>
              </el-space>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="always" style="height: 195px">
            <div>
              <el-space>
                <h1 style="font-weight: bolder">用户总数量： </h1>
                <h1 style="color: red">{{userCount.form.total}}</h1>
              </el-space>
            </div>
            <div>
              <el-space>
                <h1 style="font-weight: bolder">顾客总数量： </h1>
                <h1 style="color: orange">{{userCount.form.customer}}</h1>
              </el-space>
            </div>
            <div>
              <el-space>
                <h1 style="font-weight: bolder">封禁中的顾客数： </h1>
                <h1 style="color: red">{{userCount.form.block}}</h1>
              </el-space>
            </div>
            <div>
              <el-space>
                <h1 style="font-weight: bolder">租赁中的顾客数： </h1>
                <h1 style="color: orange">{{userCount.form.rentCar}}</h1>
              </el-space>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <img class="entranceLogo" alt="汽车租赁管理系统 LOGO" src="../assets/managerHomePage.jpg" style="margin-top: 20px; width: 100%; height: 745px"/>
  </div>
</template>
<script setup lang="ts">
import {useStore} from "@/stores";
import request from "@/request/request";
import {ElMessage} from "element-plus";

const store = useStore();
const nowUser = reactive({form: store.nowUser.form} as any);
const carCount = reactive({form: {} as any} as any);
const userCount = reactive({form: {} as any} as any);
const orderCount = reactive({form: {} as any} as any);

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href
}

const getCarCount = () => {
  request.get('/car/getCarCount').then((res: any) => {
    if (res.code === 200) {
      carCount.form = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  })
}

const getUserCount = () => {
  request.get('/user/getUserCount').then((res: any) => {
    if (res.code === 200) {
      userCount.form = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  })
}

const getOrderCount = () => {
  request.get('/order/getOrderCount').then((res: any) => {
    if (res.code === 200) {
      orderCount.form = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  })
}

const initNotCustomer = () => {
  if (nowUser.form.userType !== "Customer") {
    getCarCount();
    getUserCount();
    getOrderCount();
  }
}
initNotCustomer();
</script>
<style>

</style>