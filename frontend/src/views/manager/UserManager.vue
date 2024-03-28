<template>
  <div class="userManager">
    <div style="margin: 20px 35px">
      <el-input
          style="width: 200px"
          :suffix-icon="Message"
          placeholder="请输入邮箱"
          v-model="searchForm.email"
      ></el-input>
      <el-input
          style="width: 200px; margin-left: 15px"
          :suffix-icon="User"
          placeholder="请输入用户名"
          v-model="searchForm.userName"
      ></el-input>
      <el-select v-model="searchForm.statusLabel" placeholder="请选择用户状态" style="margin-left: 15px" clearable>
        <el-option label="正常" value="Normal"></el-option>
        <el-option label="封禁" value="Block"></el-option>
      </el-select>
      <el-select v-model="searchForm.userTypeLabel" placeholder="请选择用户类型" style="margin-left: 15px" clearable>
        <el-option label="管理员" value="Admin"></el-option>
        <el-option label="员工" value="Worker"></el-option>
        <el-option label="顾客" value="Customer"></el-option>
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
                :header-cell-style="{backgroundColor: 'aliceblue', fontSize: '16px', color: 'grey'}">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="email" label="邮箱" width="220" />
        <el-table-column prop="userName" label="用户名" width="150" />
        <el-table-column prop="telephone" label="电话" width="120" />
        <el-table-column prop="statusLabel" label="用户状态" width="120" />
        <el-table-column prop="userTypeLabel" label="用户类型" width="120" />
        <el-table-column prop="createBy" label="创建人" width="120" />
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
                @confirm="del([scope.row.id])"
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
          :page-sizes="[20, 50, 100, 200]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
        title="用户信息"
        v-model="dialogVisible"
        width="30%"
        top="50px"
    >
      <el-form :rules="rules" :model="newOrEditForm" label-width="100px">
        <el-form-item label="邮箱" prop="email">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.email"
              autocomplete="off"
              :readonly="isEdit"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.userName"
              autocomplete="off"
              :readonly="isEdit"
          ></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="telephone">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.telephone"
              autocomplete="off"
              :readonly="isEdit"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户状态" prop="status">
          <el-select v-model="newOrEditForm.form.status" placeholder="请选择用户状态" style="width: 150px; margin: 3px 0" clearable :disabled="!isEdit">
            <el-option label="正常" value="Normal"></el-option>
            <el-option label="封禁" value="Block"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <el-select v-model="newOrEditForm.form.userType" placeholder="请选择用户类型" style="width: 150px; margin: 3px 0" clearable :disabled="isEdit">
            <el-option label="管理员" value="Admin"></el-option>
            <el-option label="员工" value="Worker"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="身份证号码" prop="identityCard">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.identityCard"
              autocomplete="off"
              readonly
          ></el-input>
        </el-form-item>
        <el-space v-if="newOrEditForm.form.userType === 'Customer'">
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
        <el-space v-if="newOrEditForm.form.userType === 'Customer'">
          <h1 style="margin-left: 135px">身份证正面照</h1>
          <h1 style="margin-left: 175px">身份证背面照</h1>
        </el-space>
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
import {CirclePlus, Delete, DocumentDelete, Download, Edit, Message, Refresh, Search, User} from "@element-plus/icons";
import request from "@/request/request";
import moment from "moment";
import type {FormInstance, FormRules} from 'element-plus';
import {ElMessage} from "element-plus";
import {UserStatus, UserType} from "@/type/dataDefinition";

const searchItem = () => ({
  email: "",
  userName: "",
  status: UserStatus.Wrong,
  userType: UserType.Wrong,
  statusLabel: "",
  userTypeLabel: "",
})

const newOrEditItem = () => ({
  form: {
    email: "",
    userName: "",
    telephone: "",
    status: "",
    userType: "",
    createBy: "",
    createTime: "",
    identityCard: "",
    frontSideUrl: "",
    reverseSideUrl: "",
  }
})

const rules = reactive<FormRules>({
  status: [
    {
      required: true,
      message: 'Please input Activity name',
      trigger: 'blur' },
  ],
  userType: [
    {
      required: true,
      message: 'Please select Activity zone',
      trigger: 'blur',
    },
  ],
})

const ruleFormRef = ref<FormInstance>()
const dialogVisible = ref(false);
const currentPage = ref(1);
const pageSize = ref(20);
const total = ref(0);
const tableData = ref([]);
const searchForm = reactive(searchItem());
const newOrEditForm = reactive(newOrEditItem());
const isEdit = ref(false);
const hasSelected = ref([]);
const editUserType = ref("");

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
const dateFormat = (e: any) => {
  return moment(e.createTime).format("YYYY-MM-DD HH:mm:ss");
}

//  条件搜索
const search = () => {
  searchForm.status = searchForm.statusLabel as UserStatus;
  searchForm.userType = searchForm.userTypeLabel as UserType;
  if (searchForm.statusLabel === "") {
    searchForm.status = UserStatus.Wrong;
  }
  if (searchForm.userTypeLabel === "") {
    searchForm.userType = UserType.Wrong;
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
  newOrEditForm.form.status = "Normal";
  dialogVisible.value = true;
}

//  编辑前
const preEdit = (row: any) => {
  isEdit.value = true;
  newOrEditForm.form = JSON.parse(JSON.stringify(row));
  if (row.userType === 'Customer') {
    request.get("/user/getOneIdentityMsg", {
      params: {
        userId: row.id,
      }
    }).then((res: any) => {
      if (res.code === 200) {
        newOrEditForm.form.frontSideUrl = res.data.frontSideUrl;
        newOrEditForm.form.reverseSideUrl = res.data.reverseSideUrl;
        dialogVisible.value = true;
      }
    })
  } else {
    dialogVisible.value = true;
  }
}

//  提交表单
const submitNewOrEdit = () => {
  if (isEdit.value) {
    request.post("/user/chgUser", newOrEditForm.form).then((res: any) => {
      if (res.code === 200) {
        ElMessage.success("修改成功");
      } else {
        ElMessage.error(res.msg);
      }
      dialogCancel();
      getData();
    });
  } else {
    request.post("/user/insertOne", newOrEditForm.form).then((res: any) => {
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
  request.post("/user/delMore", ids).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("删除成功");
    } else {
      ElMessage.error(res.msg);
    }
    dialogCancel();
    getData();
  });
}

//  多选
const handleSelectionChange = (row: any) => {
  hasSelected.value = row.map((item: any) => {
    return item.id;
  });
}

//  获取数据
const getData = () =>  {
  request.get("/user/getPages", {
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
        e.statusLabel = UserStatus[e.status];
        // @ts-ignore
        e.userTypeLabel = UserType[e.userType];
      });
    } else {
      ElMessage.error(res.msg);
    }
  })
}
getData();
</script>

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  /*background-color: var(--el-color-primary-light-7);*/
  color: var(--el-text-color-primary);
  border-bottom: 1px solid grey;
}
.layout-container-demo .el-aside {
  /*color: var(--el-text-color-primary);*/
  /*background: var(--el-color-primary-light-8);*/
}
.layout-container-demo .el-menu {
  border-right: none;
}
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
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

th {
  background-color: aliceblue !important;
}
</style>
