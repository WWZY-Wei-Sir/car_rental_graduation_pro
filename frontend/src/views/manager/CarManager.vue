<template>
  <div class="carManager">
    <div style="margin: 20px 35px">
      <el-select v-model="searchForm.carTypeId" placeholder="请选择汽车类型" style="margin-left: 15px" clearable>
        <el-option v-for="item in carTypeList" :key="item.id" :label="item.type" :value="item.id"></el-option>
      </el-select>
      <el-select v-model="searchForm.carNameId" placeholder="请选择汽车品牌" style="margin-left: 15px" clearable>
        <el-option v-for="item in carNameList" :key="item.id" :label="item.name" :value="item.id"></el-option>
      </el-select>
      <el-input
          style="width: 200px; margin-left: 15px"
          :suffix-icon="CollectionTag"
          placeholder="请输入具体名称"
          v-model="searchForm.carSpecificName"
      ></el-input>
      <el-input
          style="width: 200px; margin-left: 15px"
          :suffix-icon="TakeawayBox"
          placeholder="请输入标志"
          v-model="searchForm.carTag"
      ></el-input>
      <el-input
          style="width: 200px; margin-left: 15px"
          :suffix-icon="CreditCard"
          placeholder="请输入车牌"
          v-model="searchForm.licensePlate"
      ></el-input>
      <el-select v-model="searchForm.rented" placeholder="请选择租用情况" style="margin-left: 15px" clearable>
        <el-option label="已租用" value="true"></el-option>
        <el-option label="未被租用" value="false"></el-option>
      </el-select>
      <el-select v-model="searchForm.statusLabel" placeholder="请选择汽车状态" style="margin-left: 15px" clearable>
        <el-option label="正常" value="Normal"></el-option>
        <el-option label="维修中" value="Maintenance"></el-option>
        <el-option label="年检中" value="Annual_Inspection"></el-option>
      </el-select>
      <span style="margin-left: 20px">
        <el-button type="primary" @click="search">搜索<el-icon class="el-icon--right"><Search /></el-icon></el-button>
        <el-button type="warning" @click="reset">重置<el-icon class="el-icon--right"><Refresh /></el-icon></el-button>
      </span>
    </div>
    <div style="margin: 10px 20px">
      <el-button type="primary" @click="preInsert"
      >新增<el-icon class="el-icon--right"><CirclePlus /></el-icon></el-button>
      <el-popconfirm
          confirm-button-text="确定"
          cancel-button-text="取消"
          icon-color="red"
          title="是否确定删除？"
          @confirm="del(hasSelected)"
      >
        <template #reference>
          <el-button type="danger" slot="reference" style="margin-left: 10px"
          >批量删除<el-icon class="el-icon--right"><DocumentDelete /></el-icon></el-button>
        </template>
      </el-popconfirm>
      <el-button type="primary" @click="exp"
      >下载<el-icon class="el-icon--right"><Download /></el-icon></el-button>
    </div>
    <div style="margin: 20px">
      <el-table :data="tableData"
                stripe
                border
                @selection-change="handleSelectionChange"
                :header-cell-style="{backgroundColor: 'aliceblue', fontSize: '16px', color: 'grey'}"
                @expand-change="getCarConfigList"
                row-key="id"
                :expand-row-keys="expands"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="expand">
          <template #default="scope">
            <div>
              <div style="margin: 0 300px">
                <el-tag
                    class="mx-1"
                    :key="item"
                    v-for="item in scope.row.carConfigs"
                    closable
                    :disable-transitions="false"
                    @close="handleClose(scope.row.carConfigs, item)"
                    style="margin: 10px 50px" type="warning"
                >
                  {{item.detail}}
                </el-tag>
                <el-input
                    v-if="inputVisible"
                    ref="InputRef"
                    v-model="inputValue"
                    class="ml-1 w-20"
                    size="small"
                    @keyup.enter="handleInputConfirm(scope.row.carConfigs)"
                    @blur="handleInputConfirm(scope.row.carConfigs)"
                    style="width: 115px"
                />
                <el-button v-else size="small" @click="showInput" style="width: 115px">
                  添加一个汽车配置
                </el-button>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="url" label="图片" width="220">
          <template #default="scope">
            <img
                :src="scope.row.url"
                style="width: 160px; height: 160px; margin: 5px; cursor: pointer"
                alt=""
            />
          </template>
        </el-table-column>
        <el-table-column prop="carType" label="类型" width="150" />
        <el-table-column prop="carName" label="品牌" width="150" />
        <el-table-column prop="carSpecificName" label="具体名称" width="150" />
        <el-table-column prop="carTag" label="标志" width="120" />
        <el-table-column prop="licensePlate" label="车牌" width="120" />
        <el-table-column prop="dailyRent" label="日租金/元" width="120" />
        <el-table-column prop="rented" label="租用" width="80" >
          <template #default="scope">
            <el-switch v-model="scope.row.rented" disabled/>
          </template>
        </el-table-column>
        <el-table-column prop="statusLabel" label="状态" width="80" />
        <el-table-column prop="createBy" label="创建人" width="80" />
        <el-table-column prop="createTime" label="创建时间" width="180" :formatter="dateFormat" />
        <el-table-column prop="operation" label="操作">
          <template #default="scope">
            <el-button type="primary" @click="preEdit(scope.row)"
            >编辑<el-icon class="el-icon--right"><Edit /></el-icon></el-button>
            <el-popconfirm
                confirm-button-text="确定"
                cancel-button-text="取消"
                icon-color="red"
                title="是否确定删除？"
                @confirm="del([scope.row])"
            >
              <template #reference>
                <el-button
                    type="danger"
                    style="margin-left: 10px"
                >删除<el-icon class="el-icon--right"><Delete /></el-icon></el-button>
              </template>
            </el-popconfirm>
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
        title="车辆信息"
        v-model="dialogVisible"
        width="30%"
        top="50px"
    >
      <el-upload
          class="avatar-uploader"
          action="http://localhost:8090/image/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          style="margin: 20px 150px"
      >
        <img v-if="newOrEditForm.form.url" :src="newOrEditForm.form.url" class="avatar" alt="" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
      <el-form :rules="rules" :model="newOrEditForm" label-width="100px">
        <el-form-item label="类型" prop="carType">
          <el-select v-model="newOrEditForm.form.carTypeId" placeholder="请选择汽车类型" clearable>
            <el-option v-for="item in carTypeList" :key="item.id" :label="item.type" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="品牌" prop="carName">
          <el-select v-model="newOrEditForm.form.carNameId" placeholder="请选择汽车品牌" clearable>
            <el-option v-for="item in carNameList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="具体名称" prop="carSpecificName">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.carSpecificName"
              autocomplete="off"
              :readonly="isEdit"
          ></el-input>
        </el-form-item>
        <el-form-item label="标志" prop="catTag">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.carTag"
              autocomplete="off"
              :readonly="isEdit"
          ></el-input>
        </el-form-item>
        <el-form-item label="车牌" prop="licensePlate">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.licensePlate"
              autocomplete="off"
              :readonly="isEdit"
          ></el-input>
        </el-form-item>
        <el-form-item label="是否租用" prop="rented">
          <el-select v-model="newOrEditForm.form.rented" placeholder="请选择租用情况" clearable disabled>
            <el-option label="已租用" :value="true"></el-option>
            <el-option label="未被租用" :value="false"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="汽车状态" prop="status">
          <el-select v-model="newOrEditForm.form.status" placeholder="请选择汽车状态" clearable disabled>
            <el-option label="正常" value="Normal"></el-option>
            <el-option label="维修中" value="Maintenance"></el-option>
            <el-option label="年检中" value="Annual_Inspection"></el-option>
          </el-select>
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

<script lang="ts" setup>
import {CirclePlus, Delete, DocumentDelete, Download, Edit, Refresh, Search, CreditCard, CollectionTag, TakeawayBox, Plus} from "@element-plus/icons";
import request from "@/request/request";
import moment from "moment";
import type {FormInstance, FormRules, UploadProps} from 'element-plus';
import {ElInput, ElMessage} from "element-plus";
import {CarStatus} from "@/type/dataDefinition";

const searchItem = () => ({
  carTypeId: "",
  carNameId: "",
  carSpecificName: "",
  carTag: "",
  licensePlate: "",
  rented: "",
  status: CarStatus.Wrong,
  statusLabel: "",
})

const newOrEditItem = () => ({
  form: {
    carTypeId: "",
    carNameId: "",
    imageId: "",
    carSpecificName: "",
    carTag: "",
    licensePlate: "",
    rented: false,
    status: "",
    statusLabel: "",
    url: "",
  }
})

const rules = reactive<FormRules>({

})

const ruleFormRef = ref<FormInstance>()
const dialogVisible = ref(false);
const currentPage = ref(1);
const pageSize = ref(4);
const total = ref(0);
const tableData = ref([]);
const searchForm = reactive(searchItem());
const newOrEditForm = reactive(newOrEditItem());
const isEdit = ref(false);
const hasSelected = ref([]);
const carTypeList = ref([]);
const carNameList = ref([]);
const expands = ref([] as any);
const inputValue = ref('');
const inputVisible = ref(false)
const InputRef = ref<InstanceType<typeof ElInput>>()

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
const dateFormat = (e: any) => {
  return moment(e.createTime).format("YYYY-MM-DD HH:mm:ss");
}

//  控制添加汽车配置的按钮与输入框转换
const showInput = () => {
  inputVisible.value = true;
  nextTick(() => {
    InputRef.value!.input!.focus()
  })
}

//  汽车配置的添加
const handleInputConfirm = (carConfigs: any) => {
  if (inputValue.value) {
    let insertConfigForm = {
      carId: expands.value[0],
      detail: inputValue.value,
    };
    request.post("/carConfig/insertOne", insertConfigForm).then((res: any) => {
      if (res.code === 200) {
        ElMessage.success("添加汽车配置成功");
        carConfigs.push(res.data);
      } else {
        ElMessage.error(res.msg);
      }
      inputVisible.value = false
      inputValue.value = ""
    });
  }
  inputVisible.value = false
  inputValue.value = ""
}

//  汽车配置的删除
const handleClose = (carConfigs: any, item: any) => {
  request.post("/carConfig/deleteOne", item).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("删除汽车配置成功");
      carConfigs.splice(carConfigs.indexOf(item), 1)
    } else {
      ElMessage.error(res.msg);
    }
  });
}

//  获取单个汽车的配置信息，且每次只能打开一项
const getCarConfigList = (row: any) => {
  if (row.id === expands.value[0]) {
    expands.value = [];
    return;
  }

  expands.value = [];
  expands.value.push(row.id);
  if (!row.carConfigs) {
    request.get("/carConfig/getCarConfigList", {
      params: {
        carId: row.id
      }
    }).then((res: any) => {
      if (res.code === 200) {
        row.carConfigs = res.data
      } else {
        ElMessage.error(res.msg);
      }
    })
  }
}

//  图片上传成功后赋值
const handleAvatarSuccess: UploadProps['onSuccess'] = (res, row) => {
  if (res.code === 200) {
    newOrEditForm.form.url = res.data.url;
    newOrEditForm.form.imageId = res.data.id;
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

//  条件搜索
const search = () => {
  searchForm.status = searchForm.statusLabel as CarStatus;
  if (searchForm.statusLabel === "") {
    searchForm.status = CarStatus.Wrong;
  }
  getData();
}

//  重置条件
const reset = () => {
  Object.assign(searchForm, searchItem());
  getData();
}

//  新建前
const preInsert = () => {
  isEdit.value = false;
  Object.assign(newOrEditForm, newOrEditItem());
  newOrEditForm.form.rented = false;
  newOrEditForm.form.status = "Normal";
  dialogVisible.value = true;
}

//  编辑前
const preEdit = (row: any) => {
  isEdit.value = true;
  newOrEditForm.form = JSON.parse(JSON.stringify(row));
  dialogVisible.value = true;
}

//  提交表单
const submitNewOrEdit = () => {
  if (isEdit.value) {
    request.post("/car/chgCar", newOrEditForm.form).then((res: any) => {
      if (res.code === 200) {
        ElMessage.success("修改成功");
      } else {
        ElMessage.error(res.msg);
      }
      dialogCancel();
      getData();
    });
  } else {
    request.post("/car/insertOne", newOrEditForm.form).then((res: any) => {
      if (res.code === 200) {
        ElMessage.success("添加成功");
      } else {
        ElMessage.error(res.msg);
      }
      dialogCancel();
      getData();
    });
  }
}

//  关闭新建或编辑弹窗
const dialogCancel = () => {
  dialogVisible.value = false;
}

//  删除
const del = (ids: []) => {
  ids = ids.filter((e: any) => {
    // @ts-ignore
    return e.rented === false && CarStatus[e.status] === CarStatus.Normal
  }) as [];
  if (ids.length === 0) {
    ElMessage.error("未有空闲车辆");
    return;
  }
  ids = ids.map((e: any) => {
    return e.id;
  }) as [];

  request.post("/car/delMore", ids).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("删除空闲车辆成功");
    } else {
      ElMessage.error(res.msg);
    }
    dialogCancel();
    getData();
  });
}

//  多选
const handleSelectionChange = (row: any) => {
  hasSelected.value = row;
}

//  获取carType 所有汽车类型列表
const getCarTypeList = () => {
  request.get("/carType/getCarTypeList").then((res: any) => {
    if (res.code === 200) {
      carTypeList.value = res.data;
    } else {
      ElMessage.error("汽车类型获取失败，请联系管理员！");
    }
  })
}
getCarTypeList();

//  获取carName 所有汽车品牌列表
const getCarNameList = () => {
  request.get("/carName/getCarNameList").then((res: any) => {
    if (res.code === 200) {
      carNameList.value = res.data;
    } else {
      ElMessage.error("汽车品牌获取失败，请联系管理员！");
    }
  })
}
getCarNameList();

//  获取数据
const getData = () =>  {
  request.get("/car/getPages", {
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
        e.statusLabel = CarStatus[e.status];
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
