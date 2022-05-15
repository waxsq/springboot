<template>
  <div>
    <el-card style="width: 500px;">
      <el-form label-width="80px" size="small" :rules="rules" ref="dataForm" :model="form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="身份">
          <el-input v-model="form.role" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="信用分" v-if="form.role == 'ROLE_USER'">
          <el-input v-model="form.score" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学号" v-if="form.role == 'ROLE_USER'">
          <el-input v-model="form.unumber" disabled autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱"  prop="email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>

</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "Person",
  data() {

    // 设置手机号的验证规则
    const phone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入联系方式'))
      } else {
        const reg = /^1[3|4|5|6|7|8][0-9]\d{8}$/
        // const phoneReg = /^1[34578]\d{9}$/
        if (reg.test(value)) {
          callback()
        } else {
          return callback(new Error('请输入正确的电话'))
        }
      }
    }
    // 邮箱
    var email = (rule, value, callback) => {
      const mal = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/
      // const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
      if (rule.required && !value) {
        return callback(new Error('不能为空'))
      }
      if (value) {
        if (!(mal.test(value))) {
          callback(new Error('请输入正确邮箱'))
        } else {
          callback()
        }
      }
    }



    return {
      serverIp: serverIp,
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      username:'',
      email:'',
      phone:'',
      rules:{
        email: [
          { required: true, trigger: 'blur' },
          { validator: email, trigger: 'blur' }
        ],
        phone:[
          { required: true, trigger: 'blur' },
          { validator: phone, trigger: 'blur' }
        ],
        username:[
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.getUser().then(res => {
      // console.log(res)
      this.form = res
    })
  },
  methods: {
    async getUser() {
      return (await this.request.get("/user/username/" + this.user.username)).data
    },
    save() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.request.post("/user/refreshUserInformation", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")

              //写回内存
              this.user = JSON.parse(localStorage.getItem("user"));
              this.user.username = this.form.username;
              localStorage.setItem('user',JSON.stringify(this.user));

              // 触发父级更新User的方法
              this.$emit("refreshUser")

              // 更新浏览器存储的用户信息
              this.getUser().then(res => {
                res.token = JSON.parse(localStorage.getItem("user")).token
                localStorage.setItem("user", JSON.stringify(res))
              })

            } else {
              this.$message.error("保存失败")
            }
          })

        }
      })

    },
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}
.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
