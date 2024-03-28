<template>
  <div class="messageManager">
    <div style="margin: 20px">
      <el-table :data="showList"
                stripe
                border
                :header-cell-style="{backgroundColor: 'aliceblue', fontSize: '16px', color: 'grey'}"
      >
        <el-table-column prop="speakerUserName" label="发送信息者" width="220" />
        <el-table-column prop="recipientUserName" label="接受信息者" width="150" />
        <el-table-column prop="words" label="聊天内容" width="150" />
        <el-table-column prop="createBy" label="创建人" width="80" />
        <el-table-column prop="createTime" label="创建时间" width="180" :formatter="dateFormat" />
        <el-table-column prop="operation" label="操作">
          <template #default="scope">
            <el-button type="primary" @click="toSendMsg(scope.row)">
              进入聊天界面
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
        title="聊天界面"
        v-model="dialogVisible"
        width="60%"
        top="50px"
    >
      <el-card>
        <div v-for="(item, index) in showList" :key="item">
          <div v-if="item.speakerUserId === nowUser.form.id" style="margin-left: 1000px">
            <el-space >
              <el-avatar> {{item.speakerUserName}} </el-avatar>
              <h1>{{dateFormat(item)}}</h1>
            </el-space>
            <div>
              <h1>{{item.words}}</h1>
            </div>
          </div>
          <div v-else>
            <el-space>
              <el-avatar> {{item.speakerUserName}} </el-avatar>
              <h1>{{dateFormat(item)}}</h1>
            </el-space>
            <div>
              <h1>{{item.words}}</h1>
            </div>
          </div>
        </div>
        <el-input
            type="textarea"
            maxlength="255"
            show-word-limit
            style="width: 800px; height: 65px; font-size: larger; margin: 100px 120px 0 0"
            autocomplete="off"
            placeholder="请输入聊天信息"
            v-model="words"
        ></el-input>
        <el-button type="primary" @click="sendMsg" style="width: 100px; height: 65px; font-size: larger; margin-left: -80px">发送</el-button>
      </el-card>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogCancel">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import request from "@/request/request";
import {ElMessage} from "element-plus";
import {useStore} from "@/stores";
import moment from "moment/moment";

const router = useRouter();
const store = useStore();
const nowUser = reactive({form: store.nowUser.form});
const recipientUserId = ref("");
const words = ref("");
const showList = ref([] as any);
const dialogVisible = ref(false);

//  时间格式化
const dateFormat = (e: any) => {
  return moment(e.createTime).format("YYYY-MM-DD HH:mm:ss");
}

//  编辑前
const toSendMsg = (row: any) => {
  recipientUserId.value = row.speakerUserId;
  dialogVisible.value = true;
}

//  关闭新建或编辑弹窗
const dialogCancel = () => {
  dialogVisible.value = false;
}

//  发送信息
const sendMsg = () => {
  let sendItem = {
    speakerUserId: nowUser.form.id,
    recipientUserId: recipientUserId.value,
    words: words.value
  }
  request.post("/chatRecord/sendMsg", sendItem).then((res: any) => {
    if (res.code === 200) {
      getData();
      words.value = "";
    } else {
      ElMessage.error(res.msg);
    }
  })
}

//  获取数据
const getData = () =>  {
  request.get("/chatRecord/getMsg", {
    params: {
      userId: ""
    }
  }).then((res: any) => {
    if (res.code === 200) {
      showList.value = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  })
}
getData();

setInterval(getData, 3000);
</script>

<style>

</style>