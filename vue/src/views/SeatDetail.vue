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
                  :default-sort = "{prop: 'time', order: 'descending'}">
            <el-table-column prop="time" label="预约时间" sortable></el-table-column>
            <el-table-column prop="cname" label="教室名称"></el-table-column>
            <el-table-column prop="seat" label="座位"></el-table-column>
            <el-table-column prop="username" label="当前用户"></el-table-column>
            <el-table-column prop="status" label="是否生效">
                <template slot-scope="scope">
                    <el-tag
                            :type="scope.row.status === '无效' ? 'primary' : 'success'"
                            disable-transitions>{{scope.row.status}}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作"  width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="success" v-if="scope.row.status == '有效'" @click="jump(scope.row)">查看当前预约座位 <i class="el-icon-edit"></i></el-button>
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
    </div>
</template>

<script>
    import {serverIp} from "../../public/config";
    import request from "../utils/request";
    import * as Util from '../utils/utiltool.js';

    export default {
        name: "SeatDetail",
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
                uId:''
            }
        },
        created() {
            // alert (JSON.stringify(localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : ""));
            this.load()
        },
        methods: {
            load() {
                //分页
                this.request.get("/record/page", {
                    params: {
                        pageNum: this.pageNum,
                        pageSize: this.pageSize,
                        date:this.date,
                        time:this.time,
                        cname:this.cname,
                        uId:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).uid : ""
                    }
                }).then(res => {
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
            jump(item){
              // alert(JSON.stringify(item))
              //   alert(item.time.substring(0,10))
              //   alert(item.time.substring(11,20))
                let date = item.time.substring(0,10);
                let time = item.time.substring(11,20);
                let cname = item.cname;
                this.$router.push({path:'/seat',
                    query: {date:date, time:time,cname:cname}})
            }
        }
    }
</script>


<style>
    .headerBg {
        background: #eee!important;
    }
</style>
