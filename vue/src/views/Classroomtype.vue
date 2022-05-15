<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
            <el-input style="width: 200px" placeholder="信用分标准" suffix-icon="el-icon-message" class="ml-5" v-model="score"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
        </div>

        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="name" label="名称" width="140"></el-table-column>
            <el-table-column prop="score" label="信用分数限制">
                <template slot-scope="scope">
                    <el-tag  v-if="scope.row.score >= 90 ">{{scope.row.score}}</el-tag>
                    <el-tag type="success" v-if="scope.row.score >= 80 && scope.row.score < 90">{{scope.row.score}}</el-tag>
                    <el-tag type="warning" v-if="scope.row.score >= 70 && scope.row.score < 80 ">{{scope.row.score}}</el-tag>
                    <el-tag type="danger" v-if="scope.row.score >= 60 && scope.row.score < 70 ">{{scope.row.score}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作"  width="500" align="center">
                <template slot-scope="scope">
                    <el-button type="warning" @click="lookStuCourse(scope.row.stuCourses)" v-if="scope.row.role === 'ROLE_USER'">查看预约座位 <i class="el-icon-document"></i></el-button>
                    <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定删除吗？"
                            @confirm="del(scope.row.tid)"
                    >
                        <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-sizes="[2, 5, 10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" >
            <el-form label-width="80px" size="small">
                <el-form-item label="名称">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="信用分数" oninput="value=value.replace(/[^0-9.]/g,'')">
                    <el-input v-model="form.score" autocomplete="off"></el-input>
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
    import request from "../utils/request";

    export default {
        name: "Classroomtype",
        data() {
            return {
                serverIp: serverIp,
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 10,
                name: "",
                score:'',
                form: {},
                dialogFormVisible: false,
                multipleSelection: [],
                roles: []
            }
        },
        created() {
            this.load()
        },
        methods: {
            load() {
                //分页
                this.request.get("/classroomtype/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        name: this.name,
                        score: this.score,
                    }
                }).then(res => {
                    this.tableData = res.data.records
                    this.total = res.data.total
                    // console.log("浏览室信息:"+JSON.stringify(res.data));
                })

            },
            save() {
                this.request.post("/classroomtype", this.form).then(res => {
                    if (res.code === '200') {
                        this.$message.success("保存成功")
                        this.dialogFormVisible = false
                        this.load()
                    } else {
                        this.$message.error("保存失败")
                    }
                })
            },
            handleAdd() {
                //添加初始化
                this.dialogFormVisible = true
                this.form = {}
            },
            handleEdit(row) {
                // console.log("编辑"+JSON.stringify(row));
                //因为这里原来保存的是Object：Object
                this.form = JSON.parse(JSON.stringify(row))
                this.dialogFormVisible = true
            },
            del(tid) {
                // alert("要删除的id"+tid);
                this.request.delete("/classroomtype/" + tid).then(res => {
                    if (res.code === '200') {
                        this.$message.success("删除成功")
                        this.load()
                    } else {
                        this.$message.error(res.msg)
                    }
                })
            },
            //当发生选择的时候，将选择的id选进数组
            handleSelectionChange(val) {
                console.log(val)
                this.multipleSelection = val
            },
            //批量删除按钮事件
            delBatch() {
                let ids = this.multipleSelection.map(v => v.uid)  // [{}, {}, {}] => [1,2,3]
                this.request.post("/user/del/batch", ids).then(res => {
                    if (res.code === '200') {
                        this.$message.success("批量删除成功")
                        this.load()
                    } else {
                        this.$message.error("批量删除失败")
                    }
                })
            },
            reset() {
                this.username = ""
                this.email = ""
                this.address = ""
                this.load()
            },
            //当分页插件的页面容量发生变化
            handleSizeChange(pageSize) {
                console.log(pageSize)
                this.pageSize = pageSize
                this.load()
            },
            //当页码发生变化
            handleCurrentChange(pageNum) {
                console.log(pageNum)
                this.pageNum = pageNum
                this.load()
            },

            exp() {
                window.open(`http://${serverIp}:9090/user/export`)
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
</style>
