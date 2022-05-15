import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

//要导出去，因为给登录界面使用
// 注意：刷新页面会导致页面路由重置
export const setRoutes = () => {

  // (步骤二)
  //这里从缓存中回去数据
  const storeMenus = localStorage.getItem("menus");


  // (步骤三)
  //如果当前有缓存
  //避免刷新的时候，重复加载路由
  if (storeMenus) {

    // 获取当前的路由对象名称数组
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Manage')) {
      // 拼装动态路由

      //_____________________________________________________________
      const manageRoute = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
          { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
          { path: 'password', name: '修改密码', component: () => import('../views/Password.vue')}
        ] }
        //_________________________________________________________________

      // (步骤四)
      const menus = JSON.parse(storeMenus)
      //这里完成路由动态拼接
      menus.forEach(item => {
        if (item.path) {
          // 当且仅当path不为空的时候才去设置路由
          //一级路由的情况
          let itemMenu = { path: item.path.replace("/", ""), name: item.mname, component: () => import('../views/' + item.pagePage + '.vue')}
          manageRoute.children.push(itemMenu)
          //开始二级路由
        } else if(item.children.length) {
          item.children.forEach(item => {
            if (item.path) {
              //因为数据库的item.path字段携带’/‘，但是这里的根目录是’/‘，会自动带上，所以需要去掉
              let itemMenu = { path: item.path.replace("/", ""), name: item.mname, component: () => import('../views/' + item.pagePage + '.vue')}
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      // 动态添加到现在的路由对象中去
      router.addRoute(manageRoute)
    }

  }
}

// (步骤五)
// 重置我就再set一次路由,避免刷新都是路由
setRoutes()

//这里必须带上(步骤一)
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
  store.commit("setPath")





  //意外情况
  // 未找到路由的情况
  //to.matched，表示直接遍历数组，
  // 该数组中保存着匹配到的所有路由信息
  //当访问一个路径的时候，数组长度加一
  //保存所有匹配到的路由信息
  if (!to.matched.length) {
    //从本地缓存中获取到当前用户所拥有的菜单
    const storeMenus = localStorage.getItem("menus")
    //如果当前路径和storeMenus中的数据不匹配，则进入next("/404")
    if (storeMenus) {
      next("/404")
    } else {
      // 跳回登录页面
      next("/login")
    }
  }
  // 其他的情况都放行
  next()

})

export default router
