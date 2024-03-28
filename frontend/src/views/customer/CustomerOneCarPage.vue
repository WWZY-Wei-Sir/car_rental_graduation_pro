<template>
  <div class="customerOneCarPage">
    <el-space style="margin: 20px 0 0 500px">
      <p style="font-size: 35px">汽车详细信息&nbsp;&nbsp;&nbsp;></p>
      <el-button :link="true" type="primary" style="font-size: larger; margin: 5px 0 0 20px" @click="turnToLast">返回上一页</el-button>
      <el-button :link="true" type="primary" style="font-size: larger; margin: 5px 0 0 20px" @click="turnToCustomerRent">返回租赁首页</el-button>
    </el-space>
    <div>
      <el-space>
        <el-card shadow="never" style="top: 20px; width: 630px; height: 420px; border: 2px solid gainsboro; margin: 35px 0 0 365px">
          <img :src="getImageUrl(nowCar.url)" alt="" style="width: 630px; height: 420px; margin: -20px 0 0 -20px"/>
        </el-card>
        <div style="font-size: 25px; color: grey; margin-left: 355px">
          <h1 style="font-size: 30px">{{nowCar.carType}}&nbsp&nbsp&nbsp>&nbsp&nbsp&nbsp{{nowCar.carName}}</h1>
          <h1 style="font-weight: bolder; font-size: 50px; color: black; margin: 15px auto">{{nowCar.carSpecificName}}</h1>
          <div style="background-color: papayawhip; width: 650px; margin-left: -30px">
            <div style="margin-left: 30px">
              <el-space style="margin-top: 20px">
                <h1 style="font-size: 30px">车牌&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</h1>
                <h1 style="font-weight: bolder">{{nowCar.licensePlate}}</h1>
              </el-space>
            </div>
            <div style="margin-left: 30px">
              <el-space>
                <h1 style="font-size: 30px">日租金&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</h1>
                <h1 style="font-weight: bolder; font-style: italic; color: red">￥ {{nowCar.dailyRent}}</h1>
              </el-space>
            </div>
            <div style="margin-left: 30px">
              <el-space>
                <h1 style="font-size: 30px">汽车标志&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</h1>
                <h1 style="font-weight: bolder">{{nowCar.carTag}}</h1>
              </el-space>
            </div>
            <div style="margin-left: 30px; width: 100px">
              <el-space style="margin-bottom: 20px">
                <h1 style="font-size: 30px; width: 130px">汽车配置</h1>
                <div style="left: 80px">
                  <el-tag
                      :key="item"
                      size="large"
                      v-for="item in nowCar.carConfigs"
                      :disable-transitions="false"
                      style="font-size: 20px"
                      type="success"
                  >
                    {{item.detail}}
                  </el-tag>
                </div>
              </el-space>
            </div>
          </div>
          <el-button type="danger" @click="toRentCar" :disabled="nowUserRentCar" style="width: 300px; height: 65px; margin: 30px 0; font-size: larger; display: block; left: 50%; transform: translate(-50%)">
            确认租车
          </el-button>
        </div>
      </el-space>
    </div>
    <div style="border: 2px solid gainsboro; width: 1613px; margin: 10px 0 0 365px">
      <div style="margin: 20px 0 0 25px; padding-bottom: 20px; width: 1560px">
        <h1 style="font-size: 35px; font-weight: bolder">用户评价</h1>
      </div>
      <div style="border-top: 3px solid lightgray">
        <div style="margin: 20px 0 0 25px; padding-bottom: 20px; width: 1560px">
          <h1 style="font-size: 35px">评价该汽车</h1>
          <el-space style="margin-top: 20px">
            <h5 style="font-size: 18px; font-weight: lighter">为该汽车评分</h5>
            <el-rate v-model="commentForm.rate" size="large" allow-half clearable/>
          </el-space>
          <div>
            <el-input v-model="commentForm.comment" type="textarea" placeholder="请输入评价内容" style="width: 500px; font-size: 20px; margin-top: 10px" :autosize="true"></el-input>
          </div>
          <el-button @click="insertComment" type="primary" placeholder="请输入评价内容" style="width: 130px; font-size: 18px; margin-top: 20px">
            发布评论<el-icon class="el-icon--right"><Comment /></el-icon>
          </el-button>

          <div v-infinite-scroll="load" class="infinite-list">
            <div v-for="(item, index) in showList" :key="index" class="infinite-list-item" style="margin: 20px 0 0 0; padding-top: 20px; width: 1560px; border-top: 3px dashed lightgray">
              <h5 style="font-size: 18px; font-weight: bold">用户: {{item.userName}}</h5>
              <el-space>
                <el-rate v-model="item.rate" size="large" allow-half disabled/>
                <h5 style="font-size: 18px; font-weight: bold">评论: </h5>
                <h5 style="font-size: 18px">{{item.comment}}</h5>
                <el-button v-if="item.userId !== nowUserId" @click="preReport(item.id)" type="danger" style="margin-left: 50px">举报</el-button>
              </el-space>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog
        title="举报信息"
        v-model="reportDialogVisible"
        width="30%"
        top="50px"
    >
      <el-form :rules="rules" :model="reportForm" label-width="100px">
        <el-form-item label="举报原因" prop="reason">
          <el-input
              type="textarea"
              maxlength="255"
              show-word-limit
              style="width: 800px; height: 65px; font-size: larger; margin: 5px 50px 0 0"
              autocomplete="off"
              placeholder="请输入举报原因"
              v-model="reportForm.form.reason"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="reportDialogCancel">取 消</el-button>
          <el-button type="primary" @click="toReport">提 交</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog
        title="订单确认"
        v-model="dialogVisible"
        width="30%"
        top="50px"
    >
      <el-card shadow="never" class="avatar-uploader" style="width: 300px; height: 200px; margin: 30px 70px">
        <img :src="newForm.form.url" class="avatar" alt="" style="width: 300px; height: 200px; margin: -20px 0 0 -20px" />
      </el-card>
      <el-form :rules="rules" :model="newForm" label-width="100px">
        <el-form-item label="具体名称" prop="carSpecificName">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newForm.form.carSpecificName"
              autocomplete="off"
              disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="标志" prop="catTag">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newForm.form.carTag"
              autocomplete="off"
              disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="车牌" prop="licensePlate">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newForm.form.licensePlate"
              autocomplete="off"
              disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="驾驶证" prop="driverLicense">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newForm.form.driverLicense"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-space>
          <el-form-item label=" " prop="frontSideImage">
            <el-upload
                class="avatar-uploader"
                action="http://localhost:8090/image/upload"
                :show-file-list="false"
                :on-success="handleFrontSideSuccess"
                :before-upload="beforeAvatarUpload"
                style="margin: 20px 6px"
            >
              <img v-if="newForm.form.frontSideUrl" :src="newForm.form.frontSideUrl" class="avatar" alt="" />
              <el-icon v-else class="avatar-uploader-icon" style="font-size: 13px">驾驶证正面</el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item label=" " prop="reverseSideImage">
            <el-upload
                class="avatar-uploader"
                action="http://localhost:8090/image/upload"
                :show-file-list="false"
                :on-success="handleReverseSideSuccess"
                :before-upload="beforeAvatarUpload"
                style="margin: 20px 6px"
            >
              <img v-if="newForm.form.reverseSideUrl" :src="newForm.form.reverseSideUrl" class="avatar" alt="" />
              <el-icon v-else class="avatar-uploader-icon" style="font-size: 13px">驾驶证背面</el-icon>
            </el-upload>
          </el-form-item>
        </el-space>
      </el-form>
      <div>
        <h2 style="color: red">温馨提示：</h2>
        <h4>请确认好驾驶证相关信息，以便工作人员审核！</h4>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogCancel">取 消</el-button>
          <el-button type="primary" @click="submitNew">提 交</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import type {FormInstance, FormRules, UploadProps} from 'element-plus'
import request from "@/request/request";
import {ElMessage} from "element-plus";
import {useStore} from "@/stores";
import {Comment} from "@element-plus/icons";

const reportItem = () => ({
  form: {
    userId: "",
    commentId: "",
    reason: "",
    finished: false,
  }
})

const newItem = () => ({
  form: {
    customerUserId: "",
    carId: "",
    identityCard: "",
    driverLicense: "",
    status: "",
    frontSideUrl: "",
    frontSideImage: "",
    reverseSideUrl: "",
    reverseSideImage: "",
    id: "",
  }
})

const router = useRouter();
const store = useStore();
const nowUserId = ref(JSON.parse(localStorage.getItem("user_info") || '').id);
const nowUserRentCar = ref(store.nowUser.form.rentCar);
const nowCar = ref({});
const nowCarId = ref(store.chosenCar.id);
const reportDialogVisible = ref(false);
const dialogVisible = ref(false);
const reportForm = reactive(reportItem());
const newForm = reactive(newItem());
const commentForm = reactive({
  userId: "",
  carId: "",
  rate: 0,
  comment: "",
});
const currentPage = ref(1);
const pageSize = ref(2);
const total = ref(0);
const showList = ref([] as any);

//  初始化路由跳转传参数据
const initParams = () => {
  request.get("/carConfig/getCarConfigList", {
    params: {
      carId: nowCarId.value
    }
  }).then((res: any) => {
    if (res.code === 200) {
      store.chosenCar.carConfigs = res.data;
    } else {
      ElMessage.error(res.msg);
    }
    nowCar.value = store.chosenCar;
  })
}
initParams();

//  驾驶证正面图片上传成功后赋值
const handleFrontSideSuccess: UploadProps['onSuccess'] = (res, row) => {
  if (res.code === 200) {
    newForm.form.frontSideUrl = res.data.url;
    newForm.form.frontSideImage = res.data.id;
  } else {
    ElMessage.error(res.msg);
  }
}

//  驾驶证背面图片上传成功后赋值
const handleReverseSideSuccess: UploadProps['onSuccess'] = (res, row) => {
  if (res.code === 200) {
    newForm.form.reverseSideUrl = res.data.url;
    newForm.form.reverseSideImage = res.data.id;
  } else {
    ElMessage.error(res.msg);
  }
}

//  图片上传条件
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  let types = ["image/jpg", "image/jpeg", "image/png"];
  if (!types.includes(rawFile.type)) {
    ElMessage.error("上传头像图片只能是 JPG 格式或 PNG 格式!");
    return false;
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("上传头像图片大小不能超过 2MB!");
    return false;
  }
  return true;
}

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href;
}

//  跳转到租赁首页
const turnToCustomerRent = () => {
  router.push("/customer/customerRent");
}

//  返回上一页
const turnToLast = () => {
  router.go(-1);
}

//  打开举报页面
const preReport = (commentId: string) => {
  Object.assign(reportForm, reportItem());
  reportForm.form.userId = nowUserId;
  reportForm.form.commentId = commentId;
  reportDialogVisible.value = true;
}

//  关闭举报页面
const reportDialogCancel = () => {
  reportDialogVisible.value = false;
}

//  确认举报
const toReport = () => {
  request.post("/report/insertOne", reportForm.form).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("举报成功，已通知管理员进行处理");
    } else {
      ElMessage.error(res.msg);
    }
  })
  reportDialogVisible.value = false;
}

//  打开租赁界面
const toRentCar = () => {
  newForm.form = JSON.parse(JSON.stringify(nowCar.value));
  dialogVisible.value = true;
}

//  关闭新建租赁弹窗
const dialogCancel = () => {
  dialogVisible.value = false;
}

//  确认订单
const submitNew = () => {
  newForm.form.status = "Wrong";
  newForm.form.customerUserId = nowUserId.value;
  newForm.form.carId = nowCarId.value;
  newForm.form.id = "";
  request.post("/order/rent", newForm.form).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("确认订单成功，请及时付款");
      router.push("/customer/currentOrder");
    } else {
      ElMessage.error(res.msg);
    }
  })
}

//  发布评论
const insertComment = () => {
  commentForm.userId = nowUserId.value;
  commentForm.carId = nowCarId.value;
  request.post("/carUserComment/insertOne", commentForm).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("发布评论成功");
      commentForm.rate = 0;
      commentForm.comment = "";
      location.reload();
    } else {
      ElMessage.error(res.msg);
    }
  })
}

//  无限滚动加载新数据
const load = () => {
  if (currentPage.value * pageSize.value <= total.value) {
    ++currentPage.value;
    getData();
  }
}

//  获取数据
const getData = () =>  {
  request.get("/carUserComment/getPages", {
    params: {
      carId: nowCarId.value,
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
.avatar-uploader .avatar {
  width: 80px;
  height: 80px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 80px;
  height: 80px;
  text-align: center;
}
</style>