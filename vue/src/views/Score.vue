<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入用户名" suffix-icon="el-icon-search" v-model="username"></el-input>
            <el-input style="width: 200px" placeholder="请输入学号" suffix-icon="el-icon-message" class="ml-5" v-model="uNumber"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <el-table :data="tableData"
                  border
                  stripe
                  :header-cell-class-name="'headerBg'"
                  @selection-change="handleSelectionChange"
                  style="margin-top: 5%"
        >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="unumber" label="学号" width="140" sortable></el-table-column>
            <el-table-column prop="username" label="用户名" width="140" sortable></el-table-column>
            <el-table-column prop="score" label="信用分" sortable></el-table-column>
            <el-table-column label="操作"  width="300" align="center">
                <template slot-scope="scope">
                    <el-button type="warning" @click="lookStuCourse(scope.row)">查看扣分详情 <i class="el-icon-document"></i></el-button>
                    <el-button type="info" @click="add(scope.row)">添加违章<i class="el-icon-document"></i></el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--        分页插件-->
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
        <!--弹框-->
        <el-dialog title="扣分详情" :visible.sync="dialogFormVisible" width="60%" >
            <el-table :data="form"
                      border
                      stripe
                      :header-cell-class-name="'headerBg'"
                      @selection-change="handleSelectionChange"
                      style="margin-top: 5%"
            >
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="datatime" label="时间" width="140"></el-table-column>
                <el-table-column prop="situation" label="事项" width="140"></el-table-column>
                <el-table-column prop="rpScore" label="扣分"></el-table-column>
                <el-table-column label="操作"  width="300" align="center">
                    <template slot-scope="scope">
                        <el-button type="warning" @click="del(scope.row)">删除<i class="el-icon-document"></i></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>


        <el-dialog title="新增违章" :visible.sync="dv" >

            <el-form size="small" label-width="80px"
                     style="width: 50%"
                     :rules="rules"
                     ref="dataForm"
                     :model="punishment"
            >
                <el-form-item label="时间" prop="datatime">
                    <el-date-picker
                            v-model="punishment.datatime"
                            type="datetime"
                            value-format="yyyy-MM-dd HH:mm"
                            placeholder="选择日期时间"
                    >
                    </el-date-picker>
                </el-form-item>

                <el-form-item label="扣除分数" prop="rpScore">
                    <el-input v-model="punishment.rpScore" autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item label="原因">
                    <el-input v-model="punishment.situation" autocomplete="off"
                              type="textarea"
                              autosize
                              placeholder="请输入内容"
                    ></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dv = false">取 消</el-button>
                <el-button type="primary" @click="savePunishment">确 定</el-button>
            </div>
        </el-dialog>



    </div>
</template>

<script>
    import {serverIp} from "../../public/config";
    import request from "../utils/request";
    import * as Util from '../utils/utiltool.js';

    export default {
        name: "Class",
        data() {
            return {
                serverIp: serverIp,
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 10,
                form: [],
                dialogFormVisible: false,
                uNumber:'',
                username:'',
                score:'',
                dv:false,
                punishment:{},
               rules:{
                    datatime:[
                        {required: true, trigger: 'blur'}
                    ],
                   rpScore:[
                       { required: true, message: '请输入正整数', trigger: 'change' },
                       {
                           validator(rule,value,callback){
                               if(Number.isInteger(Number(value)) && Number(value) > 0){
                                   callback();
                               }else{
                                   callback(new Error("请输入有效数字"));
                               }
                           },
                           trigger: 'blur',
                       }
                   ]
               }
            }
        },
        created() {
            this.load()
        },
        watch:{
            dialogFormVisible(){
                this.load();
            }
        },
        methods: {
            load() {
                //分页
                this.request.get("/rewardpunishment/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        uNumber:this.uNumber,
                        username: this.username,
                    }
                }).then(res => {
                    // console.log("打印出数据:"+JSON.stringify(res.data))
                    this.tableData = res.data.records
                    this.total = res.data.total
                })
            },
            lookStuCourse(row){
                // alert(row.rewardPunishmentList)
                this.form = row.rewardPunishmentList;
                this.dialogFormVisible = true;
            },
            add(row) {
                // console.log("添加违章的id"+JSON.stringify(row));
                //首先弹出框(注意无法修改)
                this.dv = true
                this.punishment = {};
                this.punishment.uid = row.uid
            },
            savePunishment(){
                // alert(JSON.stringify(this.punishment));
                this.$refs.dataForm.validate(valid => {
                    if (valid) {
                        this.request.post('rewardpunishment/save',this.punishment).then(res => {
                            if (res.code === '200')
                            {
                                this.load();
                                this.$message.success("添加成功")
                            } else this.$message.success("添加失败")
                        });
                        this.dv = false;
                    }})

            },
            del(row) {
                // alert("要删除的id"+row.rpId);
                this.request.delete('rewardpunishment/'+row.rpId+'/'+row.rpScore+'/'+row.uid).then(res =>{
                    if (res.code === '200')
                    {
                        //直接刷新
                        this.request.get("rewardpunishment/"+row.uid).then( res => {
                            if (res.code == '200')
                            {
                                this.form = {};
                                this.form = res.data;
                            }
                            // alert(res.data)
                        })
                        this.$message.success("删除成功");
                    }
                    else  this.$message.success("删除失败");
                })
                this.load();
            },

            //当发生选择的时候，将选择的id选进数组
            handleSelectionChange(val) {
                // console.log(val)
                this.multipleSelection = val
            },
            //批量删除按钮事件
            delBatch() {
                let ids = this.multipleSelection.map(v => v.cid)  // [{}, {}, {}] => [1,2,3]
                // console.log("ids"+ids)
                this.request.post("/classroom/del/batch", ids).then(res => {
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
                this.uNumber = ''
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
            }
        }
    }
</script>


<style>
    .headerBg {
        background: #eee!important;
    }
</style>
