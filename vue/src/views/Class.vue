<template>
    <div>
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入日期" suffix-icon="el-icon-search" v-model="date"></el-input>
            <el-input style="width: 200px" placeholder="请输入时间段" suffix-icon="el-icon-message" class="ml-5" v-model="time"></el-input>
            <el-input style="width: 200px" placeholder="请输入教室名称" suffix-icon="el-icon-position" class="ml-5" v-model="cname"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>


        <el-table :data="tableData"
                  border
                  stripe
                  :header-cell-class-name="'headerBg'"
        >
            <el-table-column prop="date" label="日期" sortable></el-table-column>
            <el-table-column prop="time" label="时间" sortable></el-table-column>
            <el-table-column prop="cname" label="教室名称" sortable>
                <template slot-scope="scope">
                    <el-button type="text" @click="jump(scope.row)">{{scope.row.cname}}</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="freeSeat" label="余座" sortable></el-table-column>
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
                //日期
                date:"",
                //时间段
                time:'',
                //教室名称
                cname:'',
                freeSeat:'',
            }
        },
        created() {
            this.load()
        },
        methods: {
            load() {
                //分页
                this.request.get("/seat/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        date:this.date,
                        time:this.time,
                        cname:this.cname
                    }
                }).then(res => {
                    console.log("打印出数据:"+JSON.stringify(res.data))
                    this.tableData = res.data.records
                    this.total = res.data.total
                })
            },
            reset() {
                this.date = ""
                this.time = ""
                this.cname = ""
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
            jump(row)
            {
                // alert(JSON.stringify(row))
                let date = row.date;
                let time = row.time;
                let cname = row.cname;
                this.$router.push(
                    {
                        path:'/seat',
                        query:{
                            date:date,
                            time:time,
                            cname:cname
                        }
                    }
                )
            }
        }
    }
</script>


<style>
    .headerBg {
        background: #eee!important;
    }
</style>
