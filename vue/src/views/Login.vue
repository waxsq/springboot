<template>
  <div class="wrapper">
    <div style="margin: 100px auto; background-color: #fff; width: 500px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username" style="width: 70%;margin: 3% auto">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password" style="width: 70%;margin: 3% auto">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item style="width: 70%;margin: 3% auto">
          <div style="display: flex">
            <el-input prefix-icon="el-icon-key" v-model="user.validCode" style="width: 50%;" placeholder="请输入验证码" clearable></el-input>
            <ValidCode @input="createValidCode" />
          </div>
        </el-form-item>
        <el-form-item style="margin: 1% auto; text-align: right;width:70%" >
          <div>
            <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
            <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
          </div>
           </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>

  //每个用户登录的时候，都要重置路由
import {setRoutes} from "@/router";

import ValidCode from "../components/ValidCode";

export default {
  name: "Login",
  components:{
    ValidCode
  },
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 11, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      },
      validCode: ''
    }
  },
  methods: {
    // 接收验证码组件提交的 4位验证码
    createValidCode(data) {
      this.validCode = data
    },
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (!this.user.validCode) {
            this.$message.error("请填写验证码")
            this.$emit("createdCode")
            return
          }
          if(this.user.validCode.toLowerCase() !== this.validCode.toLowerCase()) {
            this.$message.error("验证码错误")
            this.$emit("createdCode")
            return
          }
          this.request.post("/user/login", this.user).then(res => {
            // console.log("登录信息"+JSON.stringify(res));
            if (res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
              //存储菜单信息保存在缓存
              localStorage.setItem("menus", JSON.stringify(res.data.roleMeus))  // 存储用户信息到浏览器

              //当用户登录重新设置权限菜单
              // 动态设置当前用户的路由
              setRoutes()

              this.$message.success("登录成功")

              //如果是用户，直接进入首页
              //   if (res.data.role === 'ROLE_STUDENT') {
              //     this.$router.push("/front/home")
              //   } else {
              //     this.$router.push("/")
              //   }
              // }
              this.$router.push("/")
            }
            //登录失败
            else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
  overflow: hidden;
}
</style>
