<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd(null)">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              row-key="mid" default-expand-all @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="mid" label="ID" width="80"></el-table-column>
      <el-table-column prop="mname" label="名称"></el-table-column>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column prop="pagePage" label="页面路径"></el-table-column>
      <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
        <template slot-scope="scope">
          <span :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column label="操作"  width="300" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleAdd(scope.row.mid)" v-if="!scope.row.pid">新增子菜单 <i class="el-icon-plus"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.mid)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form
              label-width="80px"
              size="small"
              :rules="rules"
              :model="form"
              ref="dataForm"
      >
        <el-form-item label="名称" prop="mname" >
          <el-input v-model="form.mname" autocomplete="off" placeholder="如xxxx"></el-input>
        </el-form-item>
        <el-form-item label="路径" prop="path" >
          <el-input v-model="form.path" autocomplete="off" placeholder="如/xxxx"></el-input>
        </el-form-item>
        <el-form-item label="页面路径" prop="pagePage" >
          <el-input v-model="form.pagePage" autocomplete="off" placeholder="如xxxx"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value" /> {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "Menu",
  data() {

    // 地址
    var url = (rule, value, callback) => {
      const mal = /^(\/)/
      if (rule.required && !value) {
        return callback(new Error('不能为空'))
      }
      if (value) {
        if (!(mal.test(value))) {
          callback(new Error('请输入正确地址'))
        } else {
          callback()
        }
      }
    }
    // 页面名称
    var page = (rule, value, callback) => {
      const mal = /^[A-Z][A-z0-9]*$/
      if (rule.required && !value) {
        return callback(new Error('不能为空'))
      }
      if (value) {
        if (!(mal.test(value))) {
          callback(new Error('请输入以大写英文字符开头'))
        } else {
          callback()
        }
      }
    }


    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      options: [],
      rules:{
        mname:{required: true, message: '请输入菜单名称', trigger: 'blur'},
        path:[{required: true, message: '请输入菜单路径', trigger: 'blur'},{ validator: url, trigger: 'change' }],
        pagePage:[{required: true, message: '请输入页面名称', trigger: 'blur'},{ validator: page, trigger: 'change' }]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      //根据搜索框，搜索
      this.request.get("/menu", {
        params: {
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data;
        // console.log("菜单数据"+JSON.stringify(res.data))
      })

      // console.log("请求icon");
      // 请求图标的数据
      this.request.get("/menu/icons").then(res => {
        // console.log("打印回去icon的数据"+JSON.stringify(res.data))
        this.options = res.data
      })
    },
    //保存
    save() {
      // console.log("打印要保存的数据"+JSON.stringify(this.form));
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.request.post("/menu", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")
              this.dialogFormVisible = false
              this.load()
            } else {
              this.$message.error(res.msg)
            }
          })
        }})

    },
    //更改菜单的等级（一级和二级）,只要获取其id即可
    handleAdd(pid) {
      // console.log("打印要编辑的id"+pid);
      this.dialogFormVisible = true
      this.form = {}
      if (pid) {
        this.form.pid = pid
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/menu/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/menu/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp() {
      window.open(`http://${serverIp}:9090/role/export`)
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
.fontSize18{
  font-size: 18px;
}
.fontSize12{
  font-size: 12px;
}
</style>
