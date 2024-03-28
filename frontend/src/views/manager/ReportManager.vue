<template>
  <div class="reportManager">
    <div style="margin: 20px 35px">
      <el-select v-model="searchForm.finished" placeholder="请选择是否处理" style="margin-left: 15px" clearable>
        <el-option label="是" :value="true"></el-option>
        <el-option label="否" :value="false"></el-option>
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
                @selection-change="handleSelectionChange"
                :header-cell-style="{backgroundColor: 'aliceblue', fontSize: '16px', color: 'grey'}">
        <el-table-column prop="userName" label="举报人" width="120" />
        <el-table-column prop="publisherName" label="被举报人" width="120" />
        <el-table-column prop="reason" label="举报原因" width="395" />
        <el-table-column prop="comment" label="被举报评论" width="395" />
        <el-table-column prop="createTime" label="创建时间" width="180" :formatter="dateFormatCreateTime" />
        <el-table-column prop="createTime" label="创建时间" width="180" :formatter="dateFormatUpdateTime" />
        <el-table-column prop="finished" label="是否已处理" width="135">
          <template #default="scope">
            <el-tag v-if="scope.row.finished === false" type="danger" size="large" style="font-size: 13px">否</el-tag>
            <el-tag v-else type="primary" size="large" style="font-size: 13px">是</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operation" label="操作">
          <template #default="scope">
            <el-space v-if="scope.row.finished === false">
              <el-popconfirm
                  confirm-button-text="确定"
                  cancel-button-text="取消"
                  icon-color="red"
                  title="是否确定封禁该用户？"
                  @confirm="toLock(scope.row)"
              >
                <template #reference>
                  <el-button
                      type="danger"
                      style="margin-left: 10px"
                  >封禁<el-icon class="el-icon--right"><Lock /></el-icon></el-button>
                </template>
              </el-popconfirm>
              <el-popconfirm
                  confirm-button-text="确定"
                  cancel-button-text="取消"
                  icon-color="blue"
                  title="是否确定忽略该举报？"
                  @confirm="toIgnore(scope.row)"
              >
                <template #reference>
                  <el-button
                      style="margin-left: 10px"
                  >忽略<el-icon class="el-icon--right"><WindPower /></el-icon></el-button>
                </template>
              </el-popconfirm>
            </el-space>
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
  </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue';
import {Download, Lock, Refresh, Search, WindPower} from "@element-plus/icons";
import request from "@/request/request";
import moment from "moment";
import type {FormInstance, FormRules} from 'element-plus';
import {ElMessage} from "element-plus";

const searchItem = () => ({
  finished: "" as any,
})

const rules = reactive<FormRules>({

})

const ruleFormRef = ref<FormInstance>()
const currentPage = ref(1);
const pageSize = ref(20);
const total = ref(0);
const tableData = ref([]);
const searchForm = reactive(searchItem());

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

//  创建时间格式化
const dateFormatCreateTime = (e: any) => {
  return moment(e.createTime).format("YYYY-MM-DD HH:mm:ss");
}

//  更新时间格式化
const dateFormatUpdateTime = (e: any) => {
  return moment(e.updateTime).format("YYYY-MM-DD HH:mm:ss");
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

//  封禁被举报用户
const toLock = (item: any) => {
  request.post("/report/lock", item).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("封禁操作成功");
    } else {
      ElMessage.error(res.msg);
    }
    getData();
  })
}

//  忽略该举报信息
const toIgnore = (item: any) => {
  request.post("/report/ignore", item).then((res: any) => {
    if (res.code === 200) {
      ElMessage.success("忽略操作成功");
    } else {
      ElMessage.error(res.msg);
    }
    getData();
  })
}

//  获取数据
const getData = () =>  {
  request.get("/report/getPages", {
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

</style>
