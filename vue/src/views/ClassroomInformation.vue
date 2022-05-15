<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入阅览室名称" suffix-icon="el-icon-search" v-model="cName"></el-input>
            <el-input style="width: 200px" placeholder="请输入容量" suffix-icon="el-icon-message" class="ml-5" v-model="size"></el-input>
            <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-position" class="ml-5" v-model="cLocation"></el-input>
            <el-input style="width: 200px" placeholder="请输入阅览室类型" suffix-icon="el-icon-position" class="ml-5" v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>

        <div style="margin: 10px 0">
            <el-button type="primary" @click="handleAdd()">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="primary" @click="newSeat()">给所有阅览室生成教室 <i class="el-icon-circle-plus-outline"></i></el-button>
        </div>

        <el-table :data="tableData"
                  border
                  stripe
                  :header-cell-class-name="'headerBg'"
                  @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="cname" label="阅览室名称" width="120" sortable></el-table-column>
            <el-table-column prop="clocation" label="地址" width="80" sortable></el-table-column>
            <el-table-column prop="size" label="阅览室容量" ></el-table-column>
            <el-table-column prop="row" label="阅览室排数" ></el-table-column>
            <el-table-column prop="col" label="阅览室列数" ></el-table-column>

            <el-table-column prop="name" label="阅览室类型">
                <template slot-scope="scope">
                    <el-tag type="primary" v-if="scope.row.name === 'Ⅰ' ">{{scope.row.name}}</el-tag>
                    <el-tag type="info" v-if="scope.row.name === 'Ⅱ' ">{{scope.row.name}}</el-tag>
                    <el-tag type="info" v-if="scope.row.name === 'Ⅲ' ">{{scope.row.name}}</el-tag>
                    <el-tag type="info" v-if="scope.row.name === 'Ⅳ' ">{{scope.row.name}}</el-tag>
                    <el-tag type="warning" v-if="scope.row.name === 'Ⅴ' ">{{scope.row.name}}</el-tag>
                </template>
            </el-table-column>

            <el-table-column label="操作"  width="300" align="center">
                <template slot-scope="scope">
                    <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
                    <el-button type="success" @click="newSeatByCId(scope.row)">生成座位 <i class="el-icon-edit"></i></el-button>
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定删除吗？"
                            @confirm="del(scope.row.cid)"
                    >
                        <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
                    </el-popconfirm>
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


        <el-dialog title="阅览室信息"
                   :visible.sync="dialogFormVisible"
                   width="30%"
        >
            <el-form label-width="80px"
                     size="small"
                     :rules="rules"
                     :model="form"
                     ref="dataForm"
            >
                <el-form-item label="阅览室名称" prop="cname">
                    <el-input v-model="form.cname" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="地址" prop="clocation">
                    <el-input v-model="form.clocation" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="行" prop="row">
                    <el-input v-model="form.row" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="列" prop="col">
                    <el-input v-model="form.col" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="类型" prop="tid">
                    <el-select clearable v-model="form.tid" placeholder="请选择阅览室等级" style="width: 100%">
                        <el-option v-for="item in names" :key="item.tid" :label="item.name" :value="item.tid"></el-option>
                    </el-select>
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
        name: "ClassroomInformation",

        data() {

            let checkValue = (rule, value, callback) => {
                    if (!value) {
                        return callback(new Error('值不能为空'))
                    }
                    setTimeout(() => {
                            if (value < 1) {
                                callback(new Error('值必须大于1'))
                            } else {
                                callback()
                            }
                    }, 200)}

            return {
                serverIp: serverIp,
                tableData: [],
                total: 0,
                pageNum: 1,
                pageSize: 10,
                cName:'',
                size:'',
                cLocation:'',
                //教室类型
                name:'',
                row:'',
                col:'',
                form: {},
                names:{},
                tid:'',
                dialogFormVisible:false,
                multipleSelection:[],
                rules:{
                    cname:{required: true, message: '请输入阅览室名称', trigger: 'blur'},
                    clocation:{required: true, message: '请输入阅览室地址', trigger: 'blur'},
                    row:[{required: true, message: '请输入行', trigger: 'blur'},{ validator: checkValue, trigger: 'change' }],
                    col:[{required: true, message: '请输入列', trigger: 'blur'},{ validator: checkValue, trigger: 'change' }],
                    tid:{required: true, message: '请选择阅览室类型', trigger: 'blur'}
                }
            }
        },
        created() {
            this.load()
        },
        methods: {
            load() {
                //分页
                this.request.get("/classroom/information/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        cName:this.cName,
                        cSize:this.size,
                        cLocation:this.cLocation,
                        name:this.name
                    }
                }).then(res => {
                    // console.log("打印出数据classroom:"+JSON.stringify(res.data))
                    this.tableData = res.data.records
                    this.total = res.data.total
                });

                //查出阅览室的等级
                this.request.get("/classroomtype/name").then( res => {
                     // alert("阅览室等级"+JSON.stringify(res.data));
                    this.names = res.data;
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
            save() {
                this.$refs.dataForm.validate(valid => {
                    if (valid) {
                        this.request.post("/classroom/information/save", this.form).then(res => {
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
            //当发生选择的时候，将选择的id选进数组
            handleSelectionChange(val) {
                console.log(val)
                this.multipleSelection = val
            },
            del(id) {
                // alert("要删除的id"+id);
                this.request.delete("/classroom/information/" + id).then(res => {
                    if (res.code === '200') {
                        this.$message.success("删除成功")
                        this.load()
                    } else {
                        this.$message.error(res.msg)
                    }
                })
            },
            newSeat(row){
                // alert(JSON.stringify(row));
                this.request.post('/seatList/newSeat').then( res => {
                    if (res.code == '200')
                    {
                        this.$message.error("当前教室生成座位成功")
                    }
                    else {
                        this.$message.error(res.msg);
                    }
                })
            },
            newSeatByCId(row){
                this.request.post('/seat',row).then(res => {
                    if (res.code == '200')
                    {
                        this.$message.error("当前教室生成座位成功")
                    } else {
                        this.$message.error(res.msg);
                    }
                })
            },
            jump(row)
            {
                alert(JSON.stringify(row))
            }
        }
    }
</script>


<style>
    .headerBg {
        background: #eee!important;
    }
</style>
