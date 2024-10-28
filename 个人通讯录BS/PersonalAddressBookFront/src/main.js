// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'

//配置全局后台服务域名，作用：在axios发送后台接口请求时不需要添加后台域名，axios自动根据baseURL添加(相当于postman里面不用再输local...那些了)
axios.defaults.baseURL="http://localhost:8088"

//定义全局axios变量，变量名为$axios
Vue.prototype.$axios = axios

Vue.config.productionTip = false

Vue.use(ElementUI)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
