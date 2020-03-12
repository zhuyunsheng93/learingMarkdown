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
<<<<<<< HEAD
* 插件  
1、安装  
npm install webpack --save-dev   开发时候用的，就安装到开发,打包的时候不会打到包里  
2、编写配置 webpack.config.js(放到项目的根目录) 配置文件就是指定上述的四点配置    
```javascript
const HtmlWebpackPlugin = require('html-webpack-plugin'); //不引入这个插件的话，打包不会将index.html包含，加入这个插件后，还会将压缩的js自动引入到网页中。
module.exports = {
    entry: './src/main.js',  //指定打包的入口文件
    output: {
        path: __dirname + '/dist',  // 注意：__dirname表示webpack.config.js所在目录的绝对路径
        filename: 'build.js'		   //输出文件
    },
    module: {
        rules: [
            {
                test: /\.css$/, // 通过正则表达式匹配所有以.css后缀的文件
                use: [ // 要使用的加载器，这两个顺序一定不要乱
                    'style-loader',
                    'css-loader'
                ]
            }
        ]
    },
    plugins:[
        new HtmlWebpackPlugin({
            title: '首页',  //生成的页面标题<head><title>首页</title></head>
            filename: 'index.html', // dist目录下生成的文件名
            template: './src/index.html' // 我们原来的index.html，作为模板
        })
    ]
}
```
3、执行打包  `npm webpack --config webpack.config.js`  
4、将webpack命令添加到package.json中,就可以使用npm run build，来打包。
```json
"script" :{
"build":"webpck"
}
```
5、热更新（每次更改样式，html就会重新编译，展示，适合开发）  
   * npm install webpack-dev-server --save-dev  
   * 修改package.json 中的script
       * inline：自动刷新
       
       * hot：热加载
       
       * port：指定端口
       
       * open：自动在默认浏览器打开
       
       * host：可以指定服务器的 ip，不指定则为127.0.0.1
   ```json
"scripts":{
"dev":"webpck-dev-server --inline --hot --open --port 8080 --hot 127.0.0.1"
},
```
##vue-cli
用来快速构建项目，不用自己一一添加前端依赖。  
1、 npm install -g vue-cli  
2、 vue init webpakck   (快速搭建一个webpack项目)

 
 