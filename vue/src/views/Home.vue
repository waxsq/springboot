<template>
  <div style="color: #666;font-size: 14px;">

    <div style="padding-bottom: 20px; text-align: center" >
      <b>欢迎你！{{ user.username }}</b>
    </div>

    <div v-if="user.role == 'ROLE_USER'">

      <div>

        <h1>预约记录</h1>
        <el-divider></el-divider>
        <div>
          <el-card shadow="always" v-for="record in dataList" :key="record.id" style="text-align: center;margin-bottom: 2%" v-if="record.status == '有效'">
            <el-row :gutter="20">
              <el-col :span="6">
              <div class="grid-content bg-purple">
                时间：{{record.time}}
              </div></el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple">
                  阅读室名称：{{record.cname}}
                </div></el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple">
                  座位：{{record.seat}}
                </div></el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple">
                  有效性：{{record.status}}
                </div></el-col>
            </el-row>
          </el-card>
        </div>

      </div>

      <div style="margin-top: 5%">
        <h1>违规情况</h1>
        <el-divider></el-divider>
        <div>
          <el-card shadow="always" v-for="record in pnList" :key="record.id" style="text-align: center; margin-bottom: 2%">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="grid-content bg-purple">
                  时间：{{record.datatime}}
                </div></el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple">
                  违规信息：{{record.situation}}
                </div></el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple">
                  扣除分数：{{record.rpScore}}
                </div></el-col>
            </el-row>
          </el-card>
        </div>
      </div>
    </div>

    <div v-if="user.role != 'ROLE_USER'">
        <el-calendar v-model="value">
        </el-calendar>
    </div>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      value:new Date(),
      dataList:[],
      pnList:[]
    }
  },
  created() {
   if ((localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}).role == "ROLE_USER")
   {
     this.load();
   }
  },
  methods:{
    load()
    {
      //初始化加载数据
      //加载有效预约信息
      this.request.get('/record/history/'+this.user.uid).then(res => {
        if (res.code == '200')
        {
          this.dataList = res.data.records;
          // alert(JSON.stringify(this.dataList.records))
        } else this.dataList = "没有预约信息";
      })

      //加载违纪信息
      this.request.get('/rewardpunishment/getUserByUId/'+this.user.uid).then(res =>{
        if (res.code == '200')
        {
          this.pnList = res.data;
          // alert(JSON.stringify(this.pnList))
        } else this.pnList = "没有违纪记录";
      })
    }
  }
}
</script>
