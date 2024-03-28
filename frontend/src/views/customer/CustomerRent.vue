<template>
  <div class="customerRent">
    <CustomerRentTopSearch />
    <div style="top: 65px; text-align: center">
      <el-space v-for="(item, i) in carTypeImg">
        <img :src="getImageUrl(item.url)" alt="" @click="turnToCarTypePage(item.nowCarType, item.nowCarTypeId)" style="margin-left: 100px; cursor: pointer">
      </el-space>
    </div>
    <div style="top: 115px">
      <el-carousel :interval="5000" arrow="always" style="margin-left: 500px; width: 1235px" height="650px">
        <el-carousel-item v-for="item in rotateImages" :key="item">
          <img :src="getImageUrl(item.url)" @click="turnToLink(item)" alt="" style="width: 100%; height: 100%; cursor: pointer" />
        </el-carousel-item>
      </el-carousel>
      <div style="font-size: 26px; margin-top: 100px">
        <el-space style="margin-left: 1030px">
          <h1>轿车</h1>
          <el-button @click="toSearch('轿车')" :link="true" style="margin: 20px 0 0 15px; color: red">
            <h1>更多信息</h1>
          </el-button>
        </el-space>
        <el-space style="margin-left: 280px">
          <el-card shadow="hover" style="width: 400px; height: 700px; border: 2px solid gainsboro; margin: 20px 0 0 113px" v-for="item in sedanCar">
            <img :src="getImageUrl(item.url)" alt="" style="width: 400px; height: 280px; margin: -20px 0 0 -20px" />
            <h1 style="top: 20px">{{item.carSpecificName}}</h1>
            <el-space style="top: 25px">
              <h3 style="font-size: 20px">{{item.carTag}}</h3>
              <el-tag effect="dark" size="large" style="font-size: 18px; margin-left: 100px">
                {{item.carName}}
              </el-tag>
            </el-space>
            <h1 style="width: 500px; height: 65px; font-size: 30px; font-weight: bolder; font-style: italic; color: red; margin: 40px 0 8px -80px; text-align: center">
              ￥ {{item.dailyRent}}
            </h1>
            <el-button type="danger" @click="turnToTheCarPage(item)" style="width: 300px; height: 65px; font-size: 30px; display: block; left: 50%; transform: translate(-50%)">
              查看详情
            </el-button>
          </el-card>
        </el-space>
      </div>
      <div style="font-size: 26px; margin-top: 100px">
        <el-space style="margin-left: 1030px">
          <h1>SUV</h1>
          <el-button @click="toSearch('SUV')" :link="true" style="margin: 20px 0 0 15px; color: red">
            <h1>更多信息</h1>
          </el-button>
        </el-space>
        <el-space style="margin-left: 280px">
          <el-card shadow="hover" style="width: 400px; height: 700px; border: 2px solid gainsboro; margin: 20px 0 0 113px" v-for="item in suv">
            <img :src="getImageUrl(item.url)" alt="" style="width: 400px; height: 280px; margin: -20px 0 0 -20px" />
            <h1 style="top: 20px">{{item.carSpecificName}}</h1>
            <el-space style="top: 25px">
              <h3 style="font-size: 20px">{{item.carTag}}</h3>
              <el-tag effect="dark" size="large" style="font-size: 18px; margin-left: 100px">
                {{item.carName}}
              </el-tag>
            </el-space>
            <h1 style="width: 500px; height: 65px; font-size: 30px; font-weight: bolder; font-style: italic; color: red; margin: 40px 0 8px -80px; text-align: center">
              ￥ {{item.dailyRent}}
            </h1>
            <el-button type="danger" @click="turnToTheCarPage(item)" style="width: 300px; height: 65px; font-size: 30px; display: block; left: 50%; transform: translate(-50%)">
              查看详情
            </el-button>
          </el-card>
        </el-space>
      </div>
      <div style="font-size: 26px; margin-top: 100px">
        <el-space style="margin-left: 1030px">
          <h1>MPV</h1>
          <el-button @click="toSearch('MPV')" :link="true" style="margin: 20px 0 0 15px; color: red">
            <h1>更多信息</h1>
          </el-button>
        </el-space>
        <el-space style="margin-left: 280px">
          <el-card shadow="hover" style="width: 400px; height: 700px; border: 2px solid gainsboro; margin: 20px 0 0 113px" v-for="item in mpv">
            <img :src="getImageUrl(item.url)" alt="" style="width: 400px; height: 280px; margin: -20px 0 0 -20px" />
            <h1 style="top: 20px">{{item.carSpecificName}}</h1>
            <el-space style="top: 25px">
              <h3 style="font-size: 20px">{{item.carTag}}</h3>
              <el-tag effect="dark" size="large" style="font-size: 18px; margin-left: 100px">
                {{item.carName}}
              </el-tag>
            </el-space>
            <h1 style="width: 500px; height: 65px; font-size: 30px; font-weight: bolder; font-style: italic; color: red; margin: 40px 0 8px -80px; text-align: center">
              ￥ {{item.dailyRent}}
            </h1>
            <el-button type="danger" @click="turnToTheCarPage(item)" style="width: 300px; height: 65px; font-size: 30px; display: block; left: 50%; transform: translate(-50%)">
              查看详情
            </el-button>
          </el-card>
        </el-space>
      </div>
      <div style="font-size: 26px; margin-top: 100px">
        <el-space style="margin-left: 1030px">
          <h1>跑车</h1>
          <el-button @click="toSearch('跑车')" :link="true" style="margin: 20px 0 0 15px; color: red">
            <h1>更多信息</h1>
          </el-button>
        </el-space>
        <el-space style="margin-left: 280px">
          <el-card shadow="hover" style="width: 400px; height: 700px; border: 2px solid gainsboro; margin: 20px 0 0 113px" v-for="item in sportsCar">
            <img :src="getImageUrl(item.url)" alt="" style="width: 400px; height: 280px; margin: -20px 0 0 -20px" />
            <h1 style="top: 20px">{{item.carSpecificName}}</h1>
            <el-space style="top: 25px">
              <h3 style="font-size: 20px">{{item.carTag}}</h3>
              <el-tag effect="dark" size="large" style="font-size: 18px; margin-left: 100px">
                {{item.carName}}
              </el-tag>
            </el-space>
            <h1 style="width: 500px; height: 65px; font-size: 30px; font-weight: bolder; font-style: italic; color: red; margin: 40px 0 8px -80px; text-align: center">
              ￥ {{item.dailyRent}}
            </h1>
            <el-button type="danger" @click="turnToTheCarPage(item)" style="width: 300px; height: 65px; font-size: 30px; display: block; left: 50%; transform: translate(-50%)">
              查看详情
            </el-button>
          </el-card>
        </el-space>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import CustomerRentTopSearch from '../../components/customer/CustomerRentTopSearch.vue';
import request from "@/request/request";
import {ElMessage} from "element-plus";
import {useStore} from "@/stores";

const router = useRouter();
const store = useStore();
const sedanCar = ref([]);
const suv = ref([]);
const mpv = ref([]);
const sportsCar = ref([]);
const carTypeImgPath = "/customer/customerCarListPage";
const carTypeImg = ref([
  {
    url: "../../assets/carType/轿车.jpg",
    nowCarType: "轿车",
    nowCarTypeId: "",
  },
  {
    url: "../../assets/carType/SUV.jpg",
    nowCarType: "SUV",
    nowCarTypeId: "",
  },
  {
    url: "../../assets/carType/MPV.jpg",
    nowCarType: "MPV",
    nowCarTypeId: "",
  },
  {
    url: "../../assets/carType/跑车.jpg",
    nowCarType: "跑车",
    nowCarTypeId: "",
  },
  {
    url: "../../assets/carType/皮卡.jpg",
    nowCarType: "皮卡",
    nowCarTypeId: "",
  },
  {
    url: "../../assets/carType/微面.jpg",
    nowCarType: "微面",
    nowCarTypeId: "",
  },
  {
    url: "../../assets/carType/微卡.jpg",
    nowCarType: "微卡",
    nowCarTypeId: "",
  },
  {
    url: "../../assets/carType/轻客.jpg",
    nowCarType: "轻客",
    nowCarTypeId: "",
  },
]);

const rotateImages = ref([
  {
    url: "../../assets/rotate/1.jpg",
  },
  {
    url: "../../assets/rotate/2.jpg",
  },
  {
    url: "../../assets/rotate/3.jpg",
  },
  {
    url: "../../assets/rotate/4.jpg",
  },
]);

//  初始化获取轮播图
const getRotateImages = () => {
  request.get("/rotateImage/getList").then((res: any) => {
    if (res.code === 200) {
      rotateImages.value = res.data;
    } else {
      ElMessage.error("轮播图获取失败，请联系管理员！");
    }
  })
}
getRotateImages();

//  轮播图点击跳转
const turnToLink = (item: any) => {
  if (item.link === "http://localhost:8000/customer/customerSearchPage") {
    store.search.form.context = item.context;
    store.setLastPage(1);
    router.push("/customer/customerSearchPage");
  } else if (item.link === "http://localhost:8000/customer/customerOneCarPage") {
    request.get("/car/getOneByName", {
      params: {
        specificName: item.context,
      }
    }).then((res: any) => {
      if (res.code === 200) {
        store.chosenCar = res.data;
        router.push("/customer/customerOneCarPage");
      } else {
        ElMessage.error("通过具体名称获取汽车信息失败，请联系管理员！");
      }
    })
  }
}

//  获取图片
const getImageUrl = (url: any) => {
  return new URL(url, import.meta.url).href
}

//  跳转到指定车类型的界面
const turnToCarTypePage = (nowCarType: any, nowCarTypeId: any) => {
  store.chosenCarType = {
    nowCarType: nowCarType, nowCarTypeId: nowCarTypeId
  }
  store.setLastPage(1);
  router.push(carTypeImgPath);
}

//  跳转到指定车辆信息界面
const turnToTheCarPage = (car: any) => {
  store.chosenCar = car;
  router.push("/customer/customerOneCarPage");
}


//  搜索
const toSearch = (e: any) => {
  store.search.form.context = e;
  store.setLastPage(1);
  router.push("/customer/customerSearchPage");
}

//  获取carType 所有汽车类型列表
const getCarTypeList = () => {
  request.get("/carType/getCarTypeList").then((res: any) => {
    if (res.code === 200) {
      carTypeImg.value.forEach((item: any) => {
        item.nowCarTypeId = res.data.filter((e: any) => {
          return e.type === item.nowCarType;
        })[0].id;
      });
    } else {
      ElMessage.error("汽车类型获取失败，请联系管理员！");
    }
  })
}
getCarTypeList();

//  分别随机获取轿车、SUV、MPV、跑车四种类型的三条数据列表
const getRandomFor3Cars = async (item: string) => {
  await request.get("/car/getRandomFor3Cars", {
    params: {
      carType: item
    }
  }).then((res: any) => {
    if (res.code === 200) {
      if (item === "轿车") {
        sedanCar.value = res.data;
      } else if (item === "SUV") {
        suv.value = res.data;
      } else if (item === "MPV") {
        mpv.value = res.data;
      } else {
        sportsCar.value = res.data;
      }
    } else {
      ElMessage.error(res.msg);
    }
  })
}
getRandomFor3Cars("轿车");
getRandomFor3Cars("SUV");
getRandomFor3Cars("MPV");
getRandomFor3Cars("跑车");
</script>

<style>

</style>