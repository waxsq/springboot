import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        //当前路由信息
        currentPathName: ''
    },
    mutations: {
        setPath (state) {
            state.currentPathName = localStorage.getItem("currentPathName")
    },
        logout() {
            //同时在修改密码的时候，会推出去重新登录
            // 清空缓存,在修改权限的时候，只有对管理员操作的时候才需要退出(在Menu.vue中可以见到)
            localStorage.removeItem("user")
            localStorage.removeItem("menus")
            router.push("/login")

            // 重置路由
            resetRouter()
        }
    }
})

export default store
