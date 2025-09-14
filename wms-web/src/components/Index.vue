<script>
  import Header from "@/components/Header.vue";
  import Aside from "@/components/Aside.vue";
  //import Main from "@/components/Main.vue";

  export default {
    name: "Index",
    computed: {
    },
    components: {Aside, Header},
    data(){
      return {
        isCollapse: false,
        aside_width:"200px",
        icon:'el-icon-s-fold'
      }
    },
    methods: {
      doCollapse(){
        console.log(1111)

        this.isCollapse = !this.isCollapse
        if(this.isCollapse == false){//展开
          this.aside_width='200px'
          this.icon='el-icon-s-fold'
        }else {//关闭
          this.aside_width='64px'
          this.icon='el-icon-s-unfold'
        }
      }
    }
  };
</script>

<template>
  <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside :width="aside_width" style="background-color: rgb(238, 241, 246);height: 100%; margin-left: -1px;margin-top: -1px;">
      <Aside :isCollapse="isCollapse"></Aside>
    </el-aside>

    <el-container style="height: 100%;">
      <el-header style="text-align: right; font-size: 12px;height: 100%;border-bottom: #e2e2e2 1px solid;">
        <Header @doCollapse="doCollapse" :icon="icon"></Header>
      </el-header>

      <el-main style="height: 100%;">
<!--        <Main></Main>-->
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
  .el-header {
    color: #333;
    line-height: 60px;
  }
  .el-main{
    padding: 5px;
  }
  .el-aside {
    color: #333;
  }
</style>