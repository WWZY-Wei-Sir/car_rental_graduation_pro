import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from "element-plus"
import "element-plus/dist/index.css"
import App from './App.vue'
import router from './router'
import locale from 'element-plus/lib/locale/lang/zh-cn'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import './assets/main.css'

const app = createApp(App)

app.use(createPinia().use(piniaPluginPersistedstate))
app.use(router)
app.use(ElementPlus, { locale })

app.mount('#app')
