<template>
  <div class="currentOrder">
    <div v-if="nowUser.form.rentCar">
      <el-card class="box-card" style="width: 600px; height: 800px; left: 50%; transform: translate(-50%, 10%)">
        <el-card shadow="never" style="top: 20px; width: 315px; height: 210px; border: 2px solid gainsboro; margin: 35px 0 50px 120px">
          <img :src="getImageUrl(nowOrder.form.url)" alt="" style="width: 315px; height: 210px; margin: -20px 0 0 -20px"/>
        </el-card>
        <h1 style="font-weight: bolder; font-size: 40px; color: black; text-align: center">{{nowOrder.form.carSpecificName}}</h1>
        <h1 style="font-weight: bolder; font-size: 35px; color: grey; text-align: center">{{nowOrder.form.licensePlate}}</h1>
        <div style="margin-top: 15px">
          <el-space>
            <h1 style="font-weight: bolder">日租金:&nbsp;&nbsp;&nbsp;</h1>
            <h2 style="font-weight: bolder; font-style: italic; color: red">￥ {{nowOrder.form.dailyRent}}</h2>
          </el-space>
        </div>
        <div v-if="nowOrder.form.statusLabel !== OrderStatus.WAIT_DEPOSIT">
          <div style="margin-top: 15px">
            <el-space>
              <h1 style="font-weight: bolder">取车时间:&nbsp;&nbsp;</h1>
              <h2 style="font-weight: bolder; font-style: italic; color: red">{{dateFormat(nowOrder.form.takeDate)}}</h2>
              <h1 style="font-weight: bolder; margin-left: 10px">还车时间:&nbsp;&nbsp;</h1>
              <h2 style="font-weight: bolder; font-style: italic; color: red">{{dateFormat(nowOrder.form.returnDateTrue)}}</h2>
            </el-space>
          </div>
          <div style="margin-top: 15px">
            <el-space>
              <h1 style="font-weight: bolder">取车地点:</h1>
              <h2 style="font-weight: bolder; font-style: italic; color: red">{{nowOrder.form.takePlace}}</h2>
              <h1 style="font-weight: bolder; margin-left: 10px">还车地点:</h1>
              <h2 style="font-weight: bolder; font-style: italic; color: red">{{nowOrder.form.returnPlace}}</h2>
            </el-space>
          </div>
          <div v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_REMAINING" style="margin-top: 15px">
            <el-space>
              <h1 style="font-weight: bolder">待付总金额:&nbsp;&nbsp;&nbsp;</h1>
              <h2 style="font-weight: bolder; font-style: italic; color: red">￥ {{nowOrder.form.finalAmount}}</h2>
            </el-space>
          </div>
        </div>
        <div v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_TAKE_CAR" style="margin-top: 15px">
          <el-select :disabled="nowOrder.form.takePlaceChange" v-model="changePlace" placeholder="请选择取车地点" style="width: 180px; margin: 3px 20px" clearable size="large">
            <el-option label="番禺广场" value="番禺广场"></el-option>
            <el-option label="广州南站" value="广州南站"></el-option>
            <el-option label="白云机场" value="白云机场"></el-option>
            <el-option label="天河客运站" value="天河客运站"></el-option>
            <el-option label="东圃客运站" value="东圃客运站"></el-option>
            <el-option label="保利国际点" value="保利国际点"></el-option>
            <el-option label="客村店" value="客村店"></el-option>
            <el-option label="南沙金洲服务点" value="南沙金洲服务点"></el-option>
            <el-option label="大石地铁服务点" value="大石地铁服务点"></el-option>
            <el-option label="祈福新村店" value="祈福新村店"></el-option>
            <el-option label="江南大道店" value="江南大道店"></el-option>
            <el-option label="嘉禾望岗店" value="嘉禾望岗店"></el-option>
            <el-option label="黄浦开发区" value="黄浦开发区"></el-option>
          </el-select>
          <el-popconfirm
              confirm-button-text="确定"
              cancel-button-text="取消"
              icon-color="orange"
              title="是否确定更改？"
              @confirm="toChangePlace(true)"
          >
            <template #reference>
              <el-button
                  type="primary"
                  :disabled="nowOrder.form.takePlaceChange"
                  style="font-size: larger; margin: 3px 0"
              >确认修改</el-button>
            </template>
          </el-popconfirm>
        </div>
        <div v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_RETURN_CAR">
          <div style="margin-top: 15px">
            <el-select :disabled="nowOrder.form.returnPlaceChange" v-model="changePlace" placeholder="请选择还车地点" style="width: 180px; margin: 3px 20px" clearable size="large">
              <el-option label="番禺广场" value="番禺广场"></el-option>
              <el-option label="广州南站" value="广州南站"></el-option>
              <el-option label="白云机场" value="白云机场"></el-option>
              <el-option label="天河客运站" value="天河客运站"></el-option>
              <el-option label="东圃客运站" value="东圃客运站"></el-option>
              <el-option label="保利国际点" value="保利国际点"></el-option>
              <el-option label="客村店" value="客村店"></el-option>
              <el-option label="南沙金洲服务点" value="南沙金洲服务点"></el-option>
              <el-option label="大石地铁服务点" value="大石地铁服务点"></el-option>
              <el-option label="祈福新村店" value="祈福新村店"></el-option>
              <el-option label="江南大道店" value="江南大道店"></el-option>
              <el-option label="嘉禾望岗店" value="嘉禾望岗店"></el-option>
              <el-option label="黄浦开发区" value="黄浦开发区"></el-option>
            </el-select>
            <el-popconfirm
                confirm-button-text="确定"
                cancel-button-text="取消"
                icon-color="orange"
                title="是否确定更改？"
                @confirm="toChangePlace(false)"
            >
              <template #reference>
                <el-button
                    type="primary"
                    :disabled="nowOrder.form.returnPlaceChange"
                    style="font-size: larger; margin: 3px 0"
                >确认修改</el-button>
              </template>
            </el-popconfirm>
          </div>
          <div style="margin-top: 15px">
            <el-date-picker
                v-model="changeReturnDate"
                type="date"
                placeholder="选择还车日期"
                size="large"
                style="width: 180px; margin: 3px 20px"
                clearable
            />
            <el-popconfirm
                confirm-button-text="确定"
                cancel-button-text="取消"
                icon-color="red"
                title="是否确定申请延期还车？"
                @confirm="toChangeReturnDate"
            >
              <template #reference>
                <el-button
                    type="danger"
                    style="font-size: larger; margin: 3px 0"
                >延长还车时间</el-button>
              </template>
            </el-popconfirm>
          </div>
        </div>
        <div v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_DEPOSIT" style="margin-top: 15px">
          <el-space>
            <h1 style="font-weight: bolder">需付定金:&nbsp;&nbsp;&nbsp;</h1>
            <h2 style="font-weight: bolder; font-style: italic; color: red">￥ {{(nowOrder.form.dailyRent * 3).toFixed(2)}}</h2>
          </el-space>
        </div>
        <div v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_DEPOSIT" style="margin-top: 15px">
          <el-space>
            <h1 style="font-weight: bolder">取车日期:&nbsp;&nbsp;</h1>
            <el-date-picker
                v-model="editForm.form.takeDate"
                type="date"
                placeholder="选择日期"
                size="large"
                style="width: 150px"
            />
            <h1 style="font-weight: bolder; margin-left: 5px">还车日期:&nbsp;&nbsp;</h1>
            <el-date-picker
                v-model="editForm.form.returnDateTrue"
                type="date"
                placeholder="选择日期"
                size="large"
                style="width: 150px"
            />
          </el-space>
          <el-space style="margin-top: 10px">
            <h1 style="font-weight: bolder">取车地点:&nbsp;&nbsp;</h1>
            <el-select v-model="editForm.form.takePlace" placeholder="请选择地点" style="width: 150px; margin: 3px 0" clearable size="large">
              <el-option label="番禺广场" value="番禺广场"></el-option>
              <el-option label="广州南站" value="广州南站"></el-option>
              <el-option label="白云机场" value="白云机场"></el-option>
              <el-option label="天河客运站" value="天河客运站"></el-option>
              <el-option label="东圃客运站" value="东圃客运站"></el-option>
              <el-option label="保利国际点" value="保利国际点"></el-option>
              <el-option label="客村店" value="客村店"></el-option>
              <el-option label="南沙金洲服务点" value="南沙金洲服务点"></el-option>
              <el-option label="大石地铁服务点" value="大石地铁服务点"></el-option>
              <el-option label="祈福新村店" value="祈福新村店"></el-option>
              <el-option label="江南大道店" value="江南大道店"></el-option>
              <el-option label="嘉禾望岗店" value="嘉禾望岗店"></el-option>
              <el-option label="黄浦开发区" value="黄浦开发区"></el-option>
            </el-select>
            <h1 style="font-weight: bolder; margin-left: 5px">还车地点:&nbsp;&nbsp;</h1>
            <el-select v-model="editForm.form.returnPlace" placeholder="请选择地点" style="width: 150px; margin: 3px 0" clearable size="large">
              <el-option label="番禺广场" value="番禺广场"></el-option>
              <el-option label="广州南站" value="广州南站"></el-option>
              <el-option label="白云机场" value="白云机场"></el-option>
              <el-option label="天河客运站" value="天河客运站"></el-option>
              <el-option label="东圃客运站" value="东圃客运站"></el-option>
              <el-option label="保利国际点" value="保利国际点"></el-option>
              <el-option label="客村店" value="客村店"></el-option>
              <el-option label="南沙金洲服务点" value="南沙金洲服务点"></el-option>
              <el-option label="大石地铁服务点" value="大石地铁服务点"></el-option>
              <el-option label="祈福新村店" value="祈福新村店"></el-option>
              <el-option label="江南大道店" value="江南大道店"></el-option>
              <el-option label="嘉禾望岗店" value="嘉禾望岗店"></el-option>
              <el-option label="黄浦开发区" value="黄浦开发区"></el-option>
            </el-select>
          </el-space>
        </div>
        <el-button type="warning" size="large" style="font-size: 20px; margin: 20px 0 0 50px">
          <h1 style="font-weight: bolder">{{nowOrder.form.statusLabel}}</h1>
        </el-button>
        <el-button v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_DEPOSIT" @click="preCharge(nowOrder.form.status)" type="primary" style="font-size: larger; margin: 20px 0 0 65px">支付定金</el-button>
        <el-button v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_REMAINING" @click="preCharge(nowOrder.form.status)" type="primary" style="font-size: larger; margin: 20px 0 0 65px">支付最终金额</el-button>
        <template v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_TAKE_CAR">
          <el-popconfirm
              confirm-button-text="确定"
              cancel-button-text="取消"
              icon-color="blue"
              title="是否确定取车？"
              @confirm="toTakeCar"
          >
            <template #reference>
              <el-button
                  type="success"
                  style="font-size: larger; margin: 20px 0 0 65px"
              >确认取车</el-button>
            </template>
          </el-popconfirm>
        </template>
        <template v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_RETURN_CAR">
          <el-popconfirm
              confirm-button-text="确定"
              cancel-button-text="取消"
              icon-color="blue"
              title="是否确定还车？"
              @confirm="toReturnCar"
          >
            <template #reference>
              <el-button
                  type="success"
                  style="font-size: larger; margin: 20px 0 0 65px"
              >确认还车</el-button>
            </template>
          </el-popconfirm>
        </template>
        <el-button v-if="nowOrder.form.statusLabel === OrderStatus.WAIT_DEPOSIT || nowOrder.form.statusLabel === OrderStatus.WAIT_AUDIT
          || nowOrder.form.statusLabel === OrderStatus.WAIT_TAKE_CAR" @click="preCancle" type="danger" style="font-size: larger; margin: 20px 0 0 20px">取消订单</el-button>
      </el-card>
    </div>
    <div v-else>
      <el-card shadow="hover" style="top: 20px; width: 500px; height: 400px; border: 2px solid gainsboro; left: 50%; transform: translate(-50%, 50%)">
        <h1 style="text-align: center; line-height: 350px">暂 无 当 前 订 单 信 息</h1>
      </el-card>
    </div>

    <el-dialog
        title="安全信息确认"
        v-model="dialogVisible"
        width="30%"
        top="50px"
    >
      <el-form :rules="rules" :model="editForm" label-width="100px">
        <el-form-item label="用户密码:" prop="password">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="editForm.form.password"
              autocomplete="off"
              type="password"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogCancel">取 消</el-button>
          <el-button type="primary" @click="submitNewOrEdit">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import request from "@/request/request";
import {useStore} from "@/stores";
import type {FormInstance, FormRules, UploadProps} from 'element-plus';
import {Plus} from "@element-plus/icons";
import {ElMessage} from "element-plus";
import {CarStatus, OrderStatus, UserStatus, UserType} from "@/type/dataDefinition";
import {useRouter} from "vue-router";
import moment from "moment/moment";

const router = useRouter();
const store = useStore();
const nowUser = reactive({form: store.nowUser.form});
const nowOrder = reactive({form: {} as any} as any);
const dialogVisible = ref(false);
const changePlace = ref("");
const changeReturnDate = ref("");
const editForm = reactive({
  form: {
    id: "",
    customerUserId: null as any,
    carId: "",
    password: "",
    toCancel: false,
    chargeAmount: 0 as number,
    finalAmount: 0 as number,
    status: "",
    takeDate: null as any,
    returnDate: null as any,
    returnDateTrue: null as any,
    takePlace: null,
    returnPlace: null,
  }
});

//  时间格式化
const dateFormat = (e: any) => {
  if (e) {
    return e.substring(0, 10);
  }
}

//  修改取车或还车地点  真为取车 假为还车
const toChangePlace = (flag: boolean) => {
  if (!changePlace.value) {
    ElMessage.error("更改地点不能为空!");
    return;
  }
  let submitForm = {
    id: nowOrder.form.id,
    takePlace: null as any,
    returnPlace: null as any,
    takePlaceChange: null as any,
    returnPlaceChange: null as any,
  }
  if (flag) {
    submitForm.takePlace = changePlace.value;
    submitForm.takePlaceChange = true;
  } else {
    submitForm.returnPlace = changePlace.value;
    submitForm.returnPlaceChange = true;
  }
  request.post("/order/changePlace", submitForm).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("修改地点成功");
    } else {
      ElMessage.error(res.msg);
    }
    changePlace.value = "";
    getData();
  });
}

//  修改取车或还车地点  真为取车 假为还车
const toChangeReturnDate = () => {
  if (!changeReturnDate.value) {
    ElMessage.error("更改还车时间不能为空!");
    return;
  }
  let changeReturnDateTime = new Date(changeReturnDate.value).getTime();
  let returnDateTime = new Date(nowOrder.form.returnDate).getTime();
  if (changeReturnDateTime < returnDateTime) {
    ElMessage.error("申请延长租赁时间不得晚于约定还车时间");
    return;
  }
  if ((changeReturnDateTime - returnDateTime) / 24 / 60 / 60 / 1000 > 7) {
    ElMessage.error("申请延长租赁时间不得超过7天");
    return;
  }

  let newDate = new Date(changeReturnDateTime + 8 * 60 * 60 * 1000);
  let submitForm = {
    id: nowOrder.form.id,
    returnDate: newDate,
    returnDateTrue: newDate,
    delayTimes: nowOrder.form.delayTimes + 1,
  }
  request.post("/order/changeReturnDate", submitForm).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("延长租赁时间成功");
    } else {
      ElMessage.error(res.msg);
    }
    changeReturnDate.value = "";
    getData();
  });
}

//  支付前
const preCharge = (status: any) => {
  editForm.form.id = nowOrder.form.id;
  editForm.form.customerUserId = nowUser.form.id;
  editForm.form.carId = nowOrder.form.carId;
  editForm.form.password = "";
  editForm.form.toCancel = false;
  editForm.form.chargeAmount = Number((nowOrder.form.dailyRent * 3).toFixed(2));
  editForm.form.finalAmount = nowOrder.form.finalAmount;
  editForm.form.status = status;
  dialogVisible.value = true;
}

//  取消前
const preCancle = () => {
  editForm.form.id = nowOrder.form.id;
  editForm.form.customerUserId = nowUser.form.id;
  editForm.form.carId = nowOrder.form.carId;
  editForm.form.password = "";
  editForm.form.toCancel = true;
  editForm.form.chargeAmount = Number((nowOrder.form.dailyRent * 3).toFixed(2));
  editForm.form.status = nowOrder.form.status;
  dialogVisible.value = true;
}

//  关闭支付或取消弹窗
const dialogCancel = () => {
  dialogVisible.value = false;
}

//  支付或取消验证密码
const submitNewOrEdit = () => {
  if (editForm.form.toCancel) {
    request.post("/order/cancel", editForm.form).then((res: any) => {
      if (res.code === 200) {
        ElMessage.success("订单已取消");
        getNowUser(store.nowUser.form.id);
        router.push("/");
      } else {
        ElMessage.error(res.msg);
      }
      dialogCancel();
      getData();
    });
  } else {
    if (editForm.form.status === 'WAIT_DEPOSIT') {
      let takeDateTime = new Date(editForm.form.takeDate).getTime();
      let returnDateTime = new Date(editForm.form.returnDateTrue).getTime();
      let nowDateTime = new Date().getTime();
      if (takeDateTime < nowDateTime || returnDateTime < nowDateTime) {
        ElMessage.error("取车时间和还车时间不得晚于当前时间");
      } else if (returnDateTime <= takeDateTime) {
        ElMessage.error("取车时间不得晚于还车时间且不能为同一日");
      } else {
        //  转换UTC时间成local时间
        editForm.form.takeDate = new Date(takeDateTime + 14 * 60 * 60 * 1000);
        editForm.form.returnDate = editForm.form.returnDateTrue = new Date(returnDateTime + 26 * 60 * 60 * 1000);
        request.post("/order/chargeDeposit", editForm.form).then((res: any) => {
          if (res.code === 200) {
            ElMessage.success("支付定金成功");
            getNowUser(store.nowUser.form.id);
          } else {
            ElMessage.error(res.msg);
          }
          dialogCancel();
          getData();
        });
      }
    } else {
      request.post("/order/chargeRemaining", editForm.form).then((res: any) => {
        if (res.code === 200) {
          ElMessage.success("支付定金成功");
          getNowUser(store.nowUser.form.id);
        } else {
          ElMessage.error(res.msg);
        }
        dialogCancel();
        getData();
      });
    }
  }
}

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href
}

//  取车
const toTakeCar = () => {
  editForm.form = nowOrder.form;
  request.post("/order/takeCar", editForm.form).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("待工作人员审核");
    } else {
      ElMessage.error(res.msg);
    }
    getData();
  });
}

//  还车
const toReturnCar = () => {
  editForm.form = nowOrder.form;
  request.post("/order/returnCar", editForm.form).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("还车成功，等待给出最终需支付金额");
    } else {
      ElMessage.error(res.msg);
    }
    getData();
  });
}

//  获取用户最新信息
const getNowUser = (id: string) => {
  request.get("/user/getOne", {params: {userId: id}}).then((res: any) => {
    if (res.code === 200) {
      store.nowUser.form = res.data;
      // @ts-ignore
      store.nowUser.form.statusLabel = UserStatus[res.data.status];
      // @ts-ignore
      store.nowUser.form.userTypeLabel = UserType[res.data.userType];
      nowUser.form = store.nowUser.form;
    } else {
      ElMessage.error(res.msg);
    }
  });
}
getNowUser(store.nowUser.form.id);

//  获取数据
const getData = () =>  {
  request.get("/order/getOne", {
    params: {
      userId: nowUser.form.id,
    }
  }).then((res: any) => {
    if (res.code === 200) {
      nowOrder.form = res.data;
      // @ts-ignore
      nowOrder.form.statusLabel = OrderStatus[nowOrder.form.status];
    } else {
      ElMessage.error(res.msg);
    }
  })
}
getData();
</script>

<style>

</style>