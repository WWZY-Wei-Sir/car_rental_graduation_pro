<template>
  <div class="userTypeManager">
    <div style="margin: 20px 35px">
      <el-input
          style="width: 200px"
          :suffix-icon="Message"
          placeholder="请输入类型"
          v-model="searchForm.name"
      ></el-input>
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
        <el-table-column prop="name" label="汽车品牌" width="220" />
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
          :small="small"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
        title="汽车品牌信息"
        v-model="dialogVisible"
        width="30%"
        top="50px"
    >
      <el-form :rules="rules" :model="newOrEditForm" label-width="100px">
        <el-form-item label="汽车品牌" prop="type">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.name"
              autocomplete="off"
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

<script lang="ts" setup>
import {ref} from 'vue';
import {CirclePlus, Delete, DocumentDelete, Download, Edit, Message, Refresh, Search} from "@element-plus/icons";
import request from "@/request/request";
import moment from "moment";
import type {FormInstance, FormRules} from 'element-plus';
import {ElMessage} from "element-plus";

const searchItem = () => ({
  name: "",
})

const newOrEditItem = () => ({
  form: {
    name: "",
  }
})

const rules = reactive<FormRules>({

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
    request.post("/carName/chgCarName", newOrEditForm.form).then((res: any) => {
      if (res.code === 200) {
        ElMessage.success("修改成功");
      } else {
        ElMessage.error(res.msg);
      }
      dialogCancel();
      getData();
    });
  } else {
    request.post("/carName/insertOne", newOrEditForm.form).then((res: any) => {
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
  request.post("/carName/delMore", ids).then((res: any) => {
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
  request.get("/carName/getPages", {
    params: {
      ...searchForm,
      current: currentPage.value,
      size: pageSize.value
    }
  }).then((res: any) => {
    if (res.code === 200) {
      tableData.value = res.data.page?.records;
      total.value = res.data.page?.total;
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
