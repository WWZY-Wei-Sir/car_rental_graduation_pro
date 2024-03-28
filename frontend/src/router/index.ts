import {createRouter, createWebHistory} from 'vue-router'
import {UserType} from "@/type/dataDefinition";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/entrance',
      name: 'entrance',
      component: () => import('../views/Entrance.vue')
    },
    {
      path: '/error',
      name: '404',
      component: () => import('../views/Error.vue')
    },
  ]
})

export const setRoutes = () => {
  if (localStorage.getItem("user_info")) {
    let userType = JSON.parse(localStorage.getItem("user_info") || '').userType;

    const MainStructRoute = {
      path: "/",
      name: "首页",
      component: () => import("../views/manager/ManagerStruct.vue"),
      redirect: "/home",
      children: [
        {
          path: "home",
          name: "首页",
          component: () => import("../views/HomePage.vue"),
        },
        {
          path: '/personal',
          name: 'personal',
          component: () => import('@/views/Personal.vue')
        },
      ],
    };

    // 用户顾客路由
    // @ts-ignore
    if (UserType.Customer === UserType[userType]) {
      MainStructRoute.component = () => import("../views/customer/CustomerStruct.vue");
      let customerRent = {
        path: "customer/customerRent",
        name: "顾客界面 / 租赁首页",
        component: () => import("../views/customer/CustomerRent.vue"),
      };
      let customerCarListPage = {
        path: "customer/customerCarListPage",
        name: "顾客界面 / 汽车查看",
        component: () => import("../views/customer/CustomerCarListPage.vue"),
      };
      let customerOneCarPage = {
        path: "customer/customerOneCarPage",
        name: "顾客界面 / 具体汽车信息查看",
        component: () => import("../views/customer/CustomerOneCarPage.vue"),
      };
      let currentOrder = {
        path: "customer/currentOrder",
        name: "顾客界面 / 当前订单",
        component: () => import("../views/customer/CurrentOrder.vue"),
      };
      let historyOrder = {
        path: "customer/historyOrder",
        name: "顾客界面 / 历史订单",
        component: () => import("../views/customer/HistoryOrder.vue"),
      };
      let customerSearchPage = {
        path: "customer/customerSearchPage",
        name: "顾客界面 / 搜索界面",
        component: () => import("../views/customer/CustomerSearchPage.vue"),
      };
      let customerMessage = {
        path: "customer/customerMessage",
        name: "顾客界面 / 聊天界面",
        component: () => import("../views/customer/CustomerMessage.vue"),
      };
      MainStructRoute.children.push(
          customerRent,
          customerCarListPage,
          customerOneCarPage,
          currentOrder,
          historyOrder,
          customerSearchPage,
          customerMessage
      );
    } else {
      MainStructRoute.component = () => import("../views/manager/ManagerStruct.vue");
      let carManager = {
        path: "admin/carManager",
        name: "车辆管理 / 汽车信息",
        component: () => import("../views/manager/CarManager.vue"),
      };
      let orderManager = {
        path: "admin/orderManager",
        name: "订单管理 / 订单信息",
        component: () => import("../views/manager/OrderManager.vue"),
      };
      let messageManager = {
        path: "admin/messageManager",
        name: "交互界面 / 最新记录用户显示",
        component: () => import("../views/manager/MessageManager.vue"),
      };
      MainStructRoute.children.push(
          carManager,
          orderManager,
          messageManager,
      );
      // 管理员路由
      // @ts-ignore
      if (UserType.Admin === UserType[userType]) {
        let userManager = {
          path: "admin/userManager",
          name: "人员管理 / 用户信息",
          component: () => import("../views/manager/UserManager.vue"),
        };
        let reportManager = {
          path: "admin/reportManager",
          name: "人员管理 / 举报处理",
          component: () => import("../views/manager/ReportManager.vue"),
        };
        let carTypeManager = {
          path: "admin/carTypeManager",
          name: "车辆管理 / 汽车类别",
          component: () => import("../views/manager/CarTypeManager.vue"),
        };
        let carNameManager = {
          path: "admin/carNameManager",
          name: "车辆管理 / 汽车品牌",
          component: () => import("../views/manager/CarNameManager.vue"),
        };
        let rotateImageManager = {
          path: "admin/rotateImageManager",
          name: "图片管理 / 轮播图管理",
          component: () => import("../views/manager/RotateImageManager.vue"),
        };
        let echartsManager = {
          path: "admin/echartsManager",
          name: "统计表管理 / 现状图饼状图管理",
          component: () => import("../views/manager/EchartsManager.vue"),
        };
        MainStructRoute.children.push(
            userManager,
            reportManager,
            carTypeManager,
            carNameManager,
            rotateImageManager,
            echartsManager,
        );
      }
    }

    let errorPage = {
          path: '/:pathMatch(.*)',
          redirect: '/error'
    };
    router.addRoute(MainStructRoute);
    router.addRoute(errorPage);

    //判断是否是初次登录
    let nowRoute = router.getRoutes().map((v) => v.name);
    if (!nowRoute.includes("首页")) {
      router.addRoute(MainStructRoute);
    }
  }
}

//刷新页面时自动更新动态路由
setRoutes();

// 全局守卫：登录拦截 本地没有存token,请重新登录
router.beforeEach((to, from, next) => {
  // 判断有没有登录
  if (!localStorage.getItem('user_info') && to.name !== "entrance") {
    next('/entrance');
  } else if (localStorage.getItem('user_info') && to.name === "entrance") {
    next('/');
  } else {
    next();
  }
});

export default router
