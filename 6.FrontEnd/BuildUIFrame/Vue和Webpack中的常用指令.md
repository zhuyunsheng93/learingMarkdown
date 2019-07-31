##npm是nodejs提供的一个前端包模块管理工具
1、 npm install nrm -g    g代表全局安装  nrm是镜像切换工具
2、 nrm ls 查看镜像列表。  
3、 nrm use 镜像名    使用镜像源。  
4、 nrm test npm   测试速度  
5、 安装全局后，因前期配置问题导致指令不能使用，将node安装目录添加环境变量。
##初始化一个npm项目
1、 npm init -y  
2、 npm install 包名 -g 保存全局  -s 保存到本项目  -d 开发环境使用。  
3、 npm install vue-router -s   
##使用vue-router
1、npm install vue-router -s  
```javascript
import Vue from "../node_modules/vue/dist/vue"
import VueRouter from "../node_modules/vue-router/dist/vue-router"
import loginForm from "./js/login"
import registerForm from "./js/register"
import './css/main.css'
Vue.use(VueRouter)
const router = new VueRouter({
    routes: [{path: "/login", component: loginForm}, {path: "/register", component: registerForm}]
});
var vm = new Vue({
    el:"#app",
    components:{
        loginForm,
        registerForm
    },
    router
})
```
2、使用锚点进行显示  
<router-view></router-view>    组件就会在这里进行显示。
##webpck打包
* 入口
* 输出
* 加载器
* 插件  
1、安装  
npm install webpack --save-dev   开发时候用的，就安装到开发,打包的时候不会打到包里  
2、编写配置 webpack.config.js(放到项目的根目录) 配置文件就是指定上述的四点配置  
 
 