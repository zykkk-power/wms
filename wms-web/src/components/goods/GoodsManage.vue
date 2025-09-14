<script xmlns:el-col="http://www.w3.org/1999/html">
import SelectUser from "@/components/user/SelectUser.vue";
export default {
  name: "GoodsManage",
  components: {
    SelectUser
  },
  data() {
    let checkCount = (rule, value, callback) => {
      if(value>9999){
        callback(new Error('数量输⼊过⼤'));
      }else{
        callback();
      }
    };

    return {
      user : JSON.parse(sessionStorage.getItem("CurUser")),
      storageData:[],
      goodstypeData:[],
      tableData:[],
      pageSize:10,
      pageNum:1,
      total:0,
      name:'',
      storage:'',
      goodstype:'',
      centerDialogVisible:false,
      inDialogVisible:false,
      innerVisible:false,
      currentRow: {},
      tempUser:{},
      form:{
        id:'',
        name:'',
        storage:'',
        goodstype:'',
        count:'',
        remark:''
      },
      form1:{
        goods:'',
        goodsname:'',
        count:'',
        username:'',
        userid:'',
        adminId:'',
        createtime:'',
        remark:'',
        action:'1',
      },
      rules1:{
      },
      rules:{
        name: [
          {required: true, message: '请输入物品名', trigger: 'blur'},
        ],
        goodstype: [
          {required: true, message: '请选择分类', trigger: 'blur'},
        ],
        storage: [
          {required: true, message: '请选择仓库', trigger: 'blur'},
        ],
        count: [
          {required: true, message: '请输⼊数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
          {validator:checkCount,trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    doselectUser(val){
      console.log(val)
      this.tempUser = val
    },
    confirmUser(){
      this.form1.username = this.tempUser.name
      this.form1.userid = this.tempUser.id

      this.innerVisible = false
    },
    fomatStorage(row){
      let temp = this.storageData.find(item=>{
        return item.id == row.storage
      })

      return temp && temp.name
    },
    fomatGoodsType(row){
      let temp = this.goodstypeData.find(item=>{
        return item.id == row.goodstype
      })

      return temp && temp.name
    },
    add(){
      this.centerDialogVisible=true;
      this.$nextTick(() => {
        this.resetForm()
        this.form.id = ''
      })
    },
    selectuser(){
      this.innerVisible=true
    },
    inGoods(){
      if(!this.currentRow.id){
        alert('请选择商品')
        return
      }
      this.inDialogVisible=true;
      this.$nextTick(() => {
        this.resetInForm()
      })

      this.form1.goods = this.currentRow.id
      this.form1.goodsname = this.currentRow.name
      this.form1.id = this.currentRow.id
      this.form1.adminId = this.user.id
      this.form1.createtime = new Date().toISOString();
      this.form1.action='1'
    },
    outGoods(){
      if(!this.currentRow.id){
        alert('请选择商品')
        return
      }
      this.inDialogVisible=true;
      this.$nextTick(() => {
        this.resetInForm()
      })

      this.form1.goods = this.currentRow.id
      this.form1.goodsname = this.currentRow.name
      this.form1.id = this.currentRow.id
      this.form1.adminId = this.user.id
      this.form1.createtime = new Date().toISOString();
      this.form1.action='2'
    },
    resetInForm(){
      this.$refs.form1.resetFields();
    },
    del(id){
      this.$axios.get(this.$httpUrl+'/goods/del?id='+id).then(res=>res.data).then(res => {
        console.log(res);
        if(res.code==200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible=false;
          this.loadPost();
        }else {
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }
      })
    },
    mod(row){
      console.log(row);
      this.centerDialogVisible=true;
      this.$nextTick(() => {
        this.form.id=row.id;
        this.form.name=row.name;
        this.form.storage=row.storage;
        this.form.goodstype=row.goodstype;
        this.form.count = row.count;
        this.form.remark=row.remark;
      })
    },
    resetForm() {
      this.$refs.form.resetFields();
    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/goods/save',this.form).then(res=>res.data).then(res => {
        console.log(res);
        if(res.code==200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible=false;
          this.loadPost();
          this.resetForm();
        }else {
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }
      })
    },
    doInGoods(){
      this.$axios.post(this.$httpUrl+'/record/save',this.form1).then(res=>res.data).then(res => {
        console.log("提交的表单数据:", this.form1);
        if(res.code==200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.inDialogVisible=false;
          this.loadPost();
          this.resetInForm();
        }else {
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/goods/update',this.form).then(res=>res.data).then(res => {
        console.log(res);
        if(res.code==200){
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible=false;
          this.loadPost();
          this.resetForm();
        }else {
          this.$message({
            message: '操作失败！',
            type: 'error'
          });
        }
      })
    },
    save(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.id){
            this.doMod();
          }else {
            this.doSave();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum = 1;
      this.pageSize = val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum = val;
      this.loadPost();
    },
    handleCurrentChange1(val) {
      this.currentRow = val;
    },
    resetParam(){
      this.name='';
      this.storage='';
      this.goodstype='';
    },

    loadPost(){
      this.$axios.post(this.$httpUrl+'/goods/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          goodstype:this.goodstype+'',
          storage:this.storage+'',
        }
      }).then(res=>res.data).then(res => {
        console.log(res);
        if(res.code==200){
          this.tableData=res.data;
          this.total=res.total;
        }else {
          alert('获取数据失败')
        }
      })
    },
    loadStorage(){
      this.$axios.get(this.$httpUrl+'/storage/list').then(res=>res.data).then(res => {
        console.log(res);
        if(res.code==200){
          this.storageData=res.data;
        }else {
          alert('获取数据失败')
        }
      })
    },
    loadGoodsType(){
      this.$axios.get(this.$httpUrl+'/goodstype/list').then(res=>res.data).then(res => {
        console.log(res);
        if(res.code==200){
          this.goodstypeData=res.data;
        }else {
          alert('获取数据失败')
        }
      })
    }
  },
  beforeMount() {
    this.loadPost();
    this.loadStorage();
    this.loadGoodsType();
  }
};
</script>

<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width: 200px "
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="storage" filterable placeholder="请选择仓库" style="margin-left: 5px">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="goodstype" filterable placeholder="请选择物品类型" style="margin-left: 5px">
        <el-option
            v-for="item in goodstypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 5px" @click="loadPost">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="success" @click="add" v-if="user.roleId!=2">新增</el-button>
      <el-button type="success" @click="inGoods" v-if="user.roleId!=2">入库</el-button>
      <el-button type="success" @click="outGoods" v-if="user.roleId!=2">出库</el-button>
    </div>


    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f3f3', color:'#555'}"
              border
              highlight-current-row
              @current-change="handleCurrentChange1"
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="name" label="物品名" width="80">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" width="80" :formatter="fomatStorage">
      </el-table-column>
      <el-table-column prop="goodstype" label="分类" width="80" :formatter="fomatGoodsType">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="80">
      </el-table-column>
      <el-table-column prop="remark" label="备注" width="180">
      </el-table-column>
      <el-table-column prop="operate" label="操作" v-if="user.roleId!=2">
        <template slot-scope="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
              style="margin-left: 5px"
          >
            <el-button slot="reference" size="small" type="danger" >删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>


    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>


    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="物品名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="仓库" prop="storage">
          <el-col :span="20">
            <el-select v-model="form.storage" filterable placeholder="请选择仓库" style="margin-left: 5px">
              <el-option
                  v-for="item in storageData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>

          </el-col>
        </el-form-item>
        <el-form-item label="分类" prop="goodstype">
          <el-col :span="20">
            <el-select v-model="form.goodstype" filterable placeholder="请选择物品类型" style="margin-left: 5px">
              <el-option
                  v-for="item in goodstypeData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.remark"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="centerDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
        title="提示"
        :visible.sync="inDialogVisible"
        width="30%"
        center>

      <el-dialog
          width="60%"
          title="用户选择"
          :visible.sync="innerVisible"
          append-to-body>
        <SelectUser @doselectUser="doselectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmUser">确 定</el-button>
        </span>
      </el-dialog>

      <el-form ref="form1" :rules="rules1" :model="form1" label-width="80px">
        <el-form-item label="物品名">
          <el-col :span="20">
            <el-input v-model="form1.goodsname" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="申请人" prop="name">
          <el-col :span="20">
            <el-input v-model="form1.username" readonly @click.native="selectuser"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form1.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form1.remark"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="inDialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="doInGoods">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>