<template>
  <div class="customerMessage">
    <el-card>
      <div v-for="(item, index) in showList" :key="item">
        <div v-if="item.speakerUserId === nowUser.form.id" style="margin-left: 1800px">
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
const words = ref("");
const showList = ref([] as any);

//  时间格式化
const dateFormat = (e: any) => {
  return moment(e.createTime).format("YYYY-MM-DD HH:mm:ss");
}

//  发送信息
const sendMsg = () => {
  let sendItem = {
    speakerUserId: nowUser.form.id,
    recipientUserId: null,
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
      userId: nowUser.form.id
    }
  }).then((res: any) => {
    if (res.code === 200) {
      showList.value = res.data;
      console.log(showList)
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