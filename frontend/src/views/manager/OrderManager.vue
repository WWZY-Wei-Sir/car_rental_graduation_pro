<template>
  <div class="orderManager">
    <div style="margin: 20px 35px">
      <el-select v-model="searchForm.statusLabel" placeholder="请选择订单状态" style="margin-left: 15px" clearable>
        <el-option label="待下定金" value="WAIT_DEPOSIT"></el-option>
        <el-option label="待安置车辆" value="WAIT_SET_CAR"></el-option>
        <el-option label="待拿车" value="WAIT_TAKE_CAR"></el-option>
        <el-option label="待还车" value="WAIT_RETURN_CAR"></el-option>
        <el-option label="待工作人员检查" value="WAIT_WORKER_CAL"></el-option>
        <el-option label="待付剩余金额" value="WAIT_REMAINING"></el-option>
        <el-option label="结束" value="FINISH"></el-option>
        <el-option label="取消" value="CANCEL"></el-option>
      </el-select>
      <span style="margin-left: 20px">
        <el-button type="primary" @click="search">搜索<el-icon class="el-icon--right"><Search /></el-icon></el-button>
        <el-button type="warning" @click="reset">重置<el-icon class="el-icon--right"><Refresh /></el-icon></el-button>
      </span>
    </div>
    <div style="margin: 10px 20px">
      <el-button type="primary" @click="exp"
      >下载<el-icon class="el-icon--right"><Download /></el-icon></el-button>
    </div>
    <div style="margin: 20px">
      <el-table :data="tableData"
                stripe
                border
                :header-cell-style="{backgroundColor: 'aliceblue', fontSize: '16px', color: 'grey'}"
      >
        <el-table-column prop="url" label="图片" width="220">
          <template #default="scope">
            <img
                :src="scope.row.url"
                style="width: 160px; height: 160px; margin: 5px; cursor: pointer"
                alt=""
            />
          </template>
        </el-table-column>
        <el-table-column prop="carSpecificName" label="汽车具体名称" width="220" />
        <el-table-column prop="licensePlate" label="车牌" width="150" />
        <el-table-column prop="dailyRent" label="日租金/元" width="120" />
        <el-table-column prop="finalAmount" label="总金额/元" width="120" />
        <el-table-column prop="userName" label="租赁用户" width="100" />
        <el-table-column prop="statusLabel" label="订单状态" width="135">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 'CANCEL'" type="warning" size="large" style="font-size: 13px">{{scope.row.statusLabel}}</el-tag>
            <el-tag v-else-if="scope.row.status === 'FINISH'" type="success" size="large" style="font-size: 13px">{{scope.row.statusLabel}}</el-tag>
            <el-tag v-else type="primary" size="large" style="font-size: 13px">{{scope.row.statusLabel}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="takeDate" label="取车时间" width="180" :formatter="dateFormatTakeDate" />
        <el-table-column prop="returnDateTrue" label="还车时间" width="180" :formatter="dateFormatReturnDateTrue" />
        <el-table-column prop="takePlace" label="取车地点" width="150" />
        <el-table-column prop="returnPlace" label="还车还车" width="150" />
        <el-table-column prop="operation" label="操作">
          <template #default="scope">
            <el-button v-if="scope.row.status !== 'WAIT_DEPOSIT' && scope.row.status !== 'WAIT_AUDIT' && scope.row.status !== 'WAIT_WORKER_CAL'" @click="toAudit(scope.row)">
              查看
            </el-button>
            <el-button v-if="scope.row.status === 'WAIT_AUDIT'" type="primary" @click="toAudit(scope.row)">
              审核租车信息
            </el-button>
            <template v-if="scope.row.status === 'WAIT_CHECK_FILE'">
              <el-popconfirm
                  confirm-button-text="确定"
                  cancel-button-text="取消"
                  icon-color="blue"
                  title="是否确认无误？"
                  @confirm="toCheckFile(scope.row)"
              >
                <template #reference>
                  <el-button
                      type="success"
                      style="font-size: larger"
                  >检查文件</el-button>
                </template>
              </el-popconfirm>
            </template>
            <el-button v-if="scope.row.status === 'WAIT_WORKER_CAL'" type="danger" @click="toCalAmount(scope.row)">
              计算订单最终需支付金额
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="demo-pagination-block" style="margin: 5px 35px; float: right">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[4, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
        title="订单检查信息"
        v-model="dialogVisible"
        width="30%"
        top="50px"
    >
      <el-form :rules="rules" :model="newOrEditForm" label-width="100px">
        <el-form-item label="具体名车" prop="carSpecificName">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.carSpecificName"
              autocomplete="off"
              disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="车牌" prop="licensePlate">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.licensePlate"
              autocomplete="off"
              disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="租车用户" prop="userName">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.userName"
              autocomplete="off"
              disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="取车时间" prop="takeDate">
          <h1>{{dateFormatInForm(newOrEditForm.form.takeDate)}}</h1>
        </el-form-item>
        <el-form-item label="还车时间" prop="returnDate">
          <h1>{{dateFormatInForm(newOrEditForm.form.returnDate)}}</h1>
        </el-form-item>
        <el-form-item label="取车时间" prop="takeDate">
          <h1>{{newOrEditForm.form.takePlace}}</h1>
        </el-form-item>
        <el-form-item label="还车时间" prop="returnDate">
          <h1>{{newOrEditForm.form.returnPlace}}</h1>
        </el-form-item>
        <el-form-item label="总金额" prop="finalAmount">
          <h1>￥ {{newOrEditForm.form.finalAmount}}</h1>
        </el-form-item>
        <el-form-item v-if="newOrEditForm.form.status === 'WAIT_WORKER_CAL'" label="额外费用" prop="chargeAmount">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.chargeAmount"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item v-if="newOrEditForm.form.status === 'WAIT_AUDIT' || newOrEditForm.form.status === 'WAIT_WORKER_CAL'" label="密码" prop="password">
          <el-input
              type="password"
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.password"
              autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-space>
          <el-form-item label=" " prop="frontSideUrl">
            <img
                :src="getImageUrl(newOrEditForm.form.frontSideUrl)"
                style="width: 160px; height: 160px; margin: 5px; cursor: pointer"
                alt=""
            />
          </el-form-item>
          <el-form-item label=" " prop="reverseSideUrl">
            <img
                :src="getImageUrl(newOrEditForm.form.reverseSideUrl)"
                style="width: 160px; height: 160px; margin: 5px; cursor: pointer"
                alt=""
            />
          </el-form-item>
        </el-space>
        <el-space>
          <h1 style="margin-left: 135px">驾驶证正面照</h1>
          <h1 style="margin-left: 175px">驾驶证背面照</h1>
        </el-space>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogCancel">取 消</el-button>
          <el-button v-if="newOrEditForm.form.status === 'WAIT_WORKER_CAL'" type="danger" @click="calAmount">确认最终金额</el-button>
          <el-button v-if="newOrEditForm.form.status === 'WAIT_AUDIT'" type="primary" @click="audit">审 核</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {CirclePlus, Delete, DocumentDelete, Download, Edit, Refresh, Search, CreditCard, CollectionTag, TakeawayBox, Plus} from "@element-plus/icons";
import request from "@/request/request";
import moment from "moment";
import type {FormInstance, FormRules, UploadProps} from 'element-plus'
import {ElInput, ElMessage} from "element-plus";
import {CarStatus, OrderStatus} from "@/type/dataDefinition";
import {useStore} from "@/stores";

const searchItem = () => ({
  userId: "",
  status: OrderStatus.Wrong,
  statusLabel: "",
})

const newOrEditItem = () => ({
  form: {
    id: "",
    workerId: "",
    carId: "",
    carSpecificName: "",
    licensePlate: "",
    chargeAmount: 0 as number,
    dailyRent: 0 as number,
    takeDate: "",
    returnDate: "",
    frontSideUrl: "",
    reverseSideUrl: "",
  }
})

const rules = reactive<FormRules>({

})

const store = useStore();
const ruleFormRef = ref<FormInstance>()
const nowUser = reactive({form: store.nowUser.form});
const dialogVisible = ref(false);
const currentPage = ref(1);
const pageSize = ref(4);
const total = ref(0);
const tableData = ref([]);
const searchForm = reactive(searchItem());
const newOrEditForm = reactive(newOrEditItem());

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href
}

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

//  时间格式化
const dateFormatTakeDate = (e: any) => {
  if (e.takeDate) {
    return moment(e.takeDate).format("YYYY-MM-DD HH:mm:ss");
  }
}

//  时间格式化
const dateFormatReturnDateTrue = (e: any) => {
  if (e.returnDateTrue) {
    return moment(e.returnDateTrue).format("YYYY-MM-DD HH:mm:ss");
  }
}

//  时间格式化  表格中
const dateFormatInForm = (e: any) => {
  if (e) {
    return e.substring(0, 10) + ' ' + e.substring(11);
  }
}

//  检查文件
const toCheckFile = (item: any) => {
  request.post("/order/checkFile", item).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("检查文件成功");
    } else {
      ElMessage.error(res.msg);
    }
    getData();
  });
}

//  条件搜索
const search = () => {
  searchForm.status = searchForm.statusLabel as OrderStatus;
  if (searchForm.statusLabel === "") {
    searchForm.status = OrderStatus.Wrong;
  }
  getData();
}

//  重置条件
const reset = () => {
  Object.assign(searchForm, searchItem());
  getData();
}

//  员工审核租车信息完毕
const toAudit = (item: any) => {
  Object.assign(newOrEditForm, newOrEditItem());
  newOrEditForm.form = JSON.parse(JSON.stringify(item));
  request.get("/order/getOneDriverMsg", {
    params: {
      orderId: item.id,
    }
  }).then((res: any) => {
    if (res.code === 200) {
      newOrEditForm.form.frontSideUrl = res.data.frontSideUrl;
      newOrEditForm.form.reverseSideUrl = res.data.reverseSideUrl;
      dialogVisible.value = true;
    } else {
      ElMessage.error(res.msg);
    }
  })
}

//  员工确认审核
const audit = () => {
  newOrEditForm.form.workerId = nowUser.form.id;
  request.post("/order/audit", newOrEditForm.form).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("审核完成");
    } else {
      ElMessage.error(res.msg);
    }
    dialogVisible.value = false;
    getData();
  })
}

//  员工计算最终需支付金额，打开界面前
const toCalAmount = (item: any) => {
  newOrEditForm.form = JSON.parse(JSON.stringify(item));
  request.get("/order/getOneDriverMsg", {
    params: {
      orderId: item.id,
    }
  }).then((res: any) => {
    if (res.code === 200) {
      newOrEditForm.form.frontSideUrl = res.data.frontSideUrl;
      newOrEditForm.form.reverseSideUrl = res.data.reverseSideUrl;
      dialogVisible.value = true;
    } else {
      ElMessage.error(res.msg);
    }
  })
}

//  关闭新建或编辑弹窗
const dialogCancel = () => {
  dialogVisible.value = false;
}

//  计算最终金额
const calAmount = () => {
  if (!newOrEditForm.form.chargeAmount || newOrEditForm.form.chargeAmount < 0) {
    ElMessage.error("额外费用不得为空或负数");
    return;
  }
  newOrEditForm.form.workerId = nowUser.form.id;
  newOrEditForm.form.chargeAmount -= Number((newOrEditForm.form.dailyRent * 3).toFixed(2));
  request.post("/order/calAmount", newOrEditForm.form).then((res: any) => {
    newOrEditForm.form.chargeAmount = 0;
    if (res.code === 200) {
      ElMessage.success("最终应加金额已加上");
    } else {
      ElMessage.error(res.msg);
    }
    dialogCancel();
    getData();
  })
}

//  获取数据
const getData = () =>  {
  request.get("/order/getPages", {
    params: {
      ...searchForm,
      current: currentPage.value,
      size: pageSize.value
    }
  }).then((res: any) => {
    if (res.code === 200) {
      tableData.value = res.data.page?.records;
      total.value = res.data.page?.total;
      tableData.value.forEach((e: any) => {
        // @ts-ignore
        e.statusLabel = OrderStatus[e.status];
      });
    } else {
      ElMessage.error(res.msg);
    }
  })
}
getData();
</script>

<style>
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

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
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
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
