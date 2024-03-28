<template>
  <div class="rotateImageManager">
    <div style="margin: 20px 20px">
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
        <el-table-column prop="url" label="轮播图片" width="220">
          <template #default="scope">
            <img
                :src="scope.row.url"
                style="width: 160px; height: 160px; margin: 5px; cursor: pointer"
                alt=""
            />
          </template>
        </el-table-column>
        <el-table-column prop="link" label="跳转地址" width="500" />
        <el-table-column prop="context" label="搜索内容" width="300" />
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
          :page-sizes="[4, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
        title="轮播图信息"
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
        <el-form-item label="跳转地址" prop="link">
          <el-select v-model="newOrEditForm.form.link" placeholder="请选择跳转地址" clearable>
            <el-option label="搜索列表地址" value="http://localhost:8000/customer/customerSearchPage"></el-option>
            <el-option label="汽车详情地址" value="http://localhost:8000/customer/customerOneCarPage"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="搜索内容" prop="context">
          <el-input
              style="width: 300px; margin: 3px 0"
              v-model="newOrEditForm.form.context"
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
import {CirclePlus, Delete, DocumentDelete, Download, Edit, Plus} from "@element-plus/icons";
import request from "@/request/request";
import moment from "moment";
import type {FormInstance, FormRules, UploadProps} from 'element-plus';
import {ElMessage} from "element-plus";

const searchItem = () => ({
  type: "",
})

const newOrEditItem = () => ({
  form: {
    link: "",
    url: "",
    imageId: "",
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
  console.log(newOrEditForm)
  dialogVisible.value = true;
}

//  提交表单
const submitNewOrEdit = () => {
  if (isEdit.value) {
    request.post("/rotateImage/chgOne", newOrEditForm.form).then((res: any) => {
      if (res.code === 200) {
        ElMessage.success("修改成功");
      } else {
        ElMessage.error(res.msg);
      }
      dialogCancel();
      getData();
    });
  } else {
    request.post("/rotateImage/insertOne", newOrEditForm.form).then((res: any) => {
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
  request.post("/rotateImage/delMore", ids).then((res: any) => {
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
  request.get("/rotateImage/getPages", {
    params: {
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
