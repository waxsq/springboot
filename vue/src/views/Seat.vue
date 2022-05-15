<template>
    <div>
        <el-container>
            <el-header>
                <el-form :inline="true"  class="demo-form-inline">
                    <el-form-item label="日期">
                        <el-select v-model="selectdate" placeholder="请选择日期">
                            <el-option
                                    v-for="item in date"
                                    :key="item"
                                    :label="item"
                                    :value="item"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="时间">
                        <el-select v-model="selecttime" placeholder="请选择时间">
                            <el-option
                                    v-for="item in time"
                                    :key="item"
                                    :label="item"
                                    :value="item"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="阅读室">
                        <el-select v-model="selectclassroom" placeholder="请选择阅读室">
                            <el-option
                                    v-for="item in classroom"
                                    :key="item"
                                    :label="item"
                                    :value="item"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="query">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="select" v-show="user.role != 'ROLE_TEACHER'">保存选座</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="cancel" v-show="user.role != 'ROLE_TEACHER'">取消选座</el-button>
                    </el-form-item>
                </el-form>
            </el-header>
<!--座位-->
            <el-main>
                <el-row type="flex"
                        class="row-bg"
                        justify="center"
                        v-for="(rowIndex,row) in seatList"
                        :key="row">
                    <el-col
                            v-for="(colIndex,col1) in rowIndex"
                            :key="col1"
                            :span="3"
                           >

                        <div v-if="colIndex.uid != uId" style="text-align: center">
                            <img src="../assets/1.png" style="width: 50%" v-show="colIndex.sstatus === '1'"
                                 @click="chioce(colIndex)"
                            >
                            <img src="../assets/2.png" style="width: 50%" v-show="colIndex.sstatus === '0'"
                                 @click="chioce(colIndex)"
                            >
                            <div style="text-align: center">{{row+1}} 排 {{col1+1}} 列</div>
                        </div>
<!--                        如果当前有自己选择的座位-->
                        <div v-if="colIndex.uid == uId" style="text-align: center">
                            <img src="../assets/3.png" style="width: 50%" v-show="colIndex.sstatus === '0'"
                                 @click="chioce(colIndex)"
                            >
                            <div style="text-align: center">{{row+1}} 排 {{col1+1}} 列</div>
                        </div>

                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    import request from "../utils/request";

    export default {
        name: "Seat",
        data(){
            return{
                date:[],
                time:[],
                classroom:[],
                selectdate:'',
                selecttime:'',
                selectclassroom:'',
                seatList:[],
                currentUId:'',
                uId:'',
                row:'',
                col:'',
                //判断重复选择
                flag:false,
                //保存当前座位信息
                seat:{},
                user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")): "",
                timer:null //定时器名称
            }
        },
        created() {
            this.load();
            this.uId = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).uid : ""
        },
        mounted() {
            this.timer = setInterval( () =>
            {
                setTimeout(this.query,0)
            },1000*60) //每一分钟刷新
        },
        beforeDestroy() {
            clearInterval(this.timer);
            this.timer = null;
        },
        watch:{
            selectdate(newItem,oldItem)
            {
                this.selectdate = newItem;
                this.query();
            },
            selecttime(newItem,oldItem)
            {
                this.selecttime = newItem;
                this.query();
            },
            selectclassroom(newItem,oldItem)
            {
                // alert(newItem)
                this.query();
            }
        },
        methods:{
            //初始化函数
            load(){
                //查询可选日期
                this.request.get('/seat/getDate').then(res => {
                    if (res.code == '200')
                    {
                        this.date = res.data;
                        this.$route.query.date != undefined ? this.selectdate = this.$route.query.date: this.selectdate = this.date[0];
                    }
                });
                //查询可选时间段
                this.request.get('/seat/getTime').then(res => {
                    if (res.code == '200')
                    {
                        this.time = res.data;
                        // this.selecttime =this.time[0];
                        this.$route.query.time != undefined ? this.selecttime = this.$route.query.time: this.selecttime =this.time[0];
                    }
                });
                //查询可选教室
                this.request.get('/seat/getClassroom').then(res => {
                    if (res.code == '200')
                    {
                        this.classroom = res.data;
                        // this.selectclassroom = this.classroom[0];
                        this.$route.query.cname != undefined ? this.selectclassroom = this.$route.query.cname: this.selectclassroom =this.classroom[0];
                        // alert(this.selectclassroom)
                    }
                });
                this.getAllSeat();
            },
            getAllSeat(){
                //渲染座位
                this.request.get('/seat/getAll',{
                    params:{
                        date:this.selectdate,
                        time:this.selecttime,
                        classroom:this.selectclassroom
                    }
                }).then(res => {
                    if (res.code == '200')
                    {
                        let array = res.data.seatList;
                        // alert(JSON.stringify(array))
                        let newArr = [];
                        let a=[];
                        let col = res.data.col;
                        let row = res.data.row;
                        let length = 0;
                        this.col = col;
                        this.row = row;
                        for (var i = 0 ; i < row ; i++)
                        {
                            for (var j = 0 ; j < col ;j++)
                            {
                                //查找当前用户是否有选择的座位
                                // alert(array[length].uid +'----'+ this.uId)

                                if (array[length].uid == this.uId)
                                {
                                    this.seat = array[length];
                                    // console.log(JSON.stringify("打印："+JSON.stringify(this.seat)))
                                    console.log(JSON.stringify("打印："+this.seat.uid))
                                    this.flag = true;
                                }
                                newArr.push(array[length++]);
                            }
                            a.push(newArr);
                            newArr = [];
                        }
                        this.seatList = a;
                    }
                    else {
                        this.$message.error("当前教室没有座位")
                    }
                })
            },
            query(){
                //初始化参数
                this.seatList = [];
                this.seat = {};
                this.flag = false;
                this.getAllSeat();
            },
            chioce(item)
            {

                if (this.user.role == 'ROLE_TEACHER')
                {
                    this.$message.error("教师不能选择");
                    return;
                }
                if (item.uid != this.uId && item.sstatus === '0')
                {
                    this.$message.error("当前已经被人选择");
                    return;
                }
                if (item.uid === this.uId && item.sstatus === '0')
                {
                    item.sstatus = '1';
                    item.uid = 0;
                    this.flag = false;
                    this.seat = item;
                    this.$message.error("座位取消成功");
                    return;
                }

                if (item.sstatus == '0' && this.seat.uid == this.uId)
                {
                    this.$message.error("请点击取消按钮");
                    return;
                }
                //判断是否重复选
               if (this.seat.keyword == item.keyword && item.sstatus == '0')
               {
                   //点击取消
                   item.sstatus = '1';
                   item.uid = 0;
                   this.flag = false;
                   this.seat = item;
                   this.$message.error("座位取消成功,请点击保存按钮，否则取消失败");
                   return;
               }
               //判断是否重复选择
               if (this.seat !== null && this.flag)
               {
                   this.$message.error("只能选择一个位置");
                   return;
               }
                //选中状态
                item.sstatus = '0';
                item.uid = this.uId;
                this.seat = item;
                this.flag = true;
                this.$message.success("座位选择成功,请点击保存按钮，否则选择失败");
            },
            select(){
                this.request.post('/seat/save',this.seat).then(res => {
                    if (res.code === '200')
                    {
                        this.$message.success("选择成功");
                        this.seatList = [];
                        this.query();
                    } else this.$message.error(res.msg);
                })
            },
            cancel()
            {
                if (this.seat.uid == null && this.uId != this.seat.uid)
                {
                    this.$message.error("当前没有预约的座位");
                    return;
                }
                this.seatList = [];
                // console.log("取消"+JSON.stringify(this.seat))
                this.request.post('/seat/cancel',this.seat).then(res => {
                    if (res.code === '200')
                    {
                        this.$message.success("取消成功");
                    } else this.$message.error(res.msg);
                    this.query();
                })
            }
        }
    }
</script>

<style scoped>

</style>
