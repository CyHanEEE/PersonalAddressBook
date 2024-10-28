<template>
  <div>
    <el-form :inline="true">
        <el-form-item>
          <el-input v-model="name" placeholder="姓名" clearable></el-input>
        </el-form-item>
		
		<el-form-item>
		  <el-input v-model="address" placeholder="地址" clearable></el-input>
		</el-form-item>
		
		<el-form-item>
		  <el-input type="number" v-model="phone" placeholder="电话号码" clearable></el-input>
		</el-form-item>

        <el-form-item>
          <el-button type="primary" @click="getContactList">查询</el-button>
        </el-form-item>
    </el-form>
	
	<el-table
	      :data="contactList"
	      border
	      style="width: 100%">
	      <el-table-column fixed prop="id" label="序号" >
	      </el-table-column>
	      <el-table-column prop="name" label="姓名">
	      </el-table-column>
	      <el-table-column prop="address" label="地址">
	      </el-table-column>
	      <el-table-column prop="phone" label="手机号">
	      </el-table-column>
	      <el-table-column fixed="right" label="操作">
	        <template slot-scope="scope">
	         <el-button type="text" size="small" @click="updateContactDraw(scope.row)">修改</el-button>
	         <el-button type="text" size="small" @click="deleteContact(scope.row.id)">删除</el-button>
	        </template>
	      </el-table-column>
	    </el-table>

    <!-- 抽屉 -->
    <el-drawer
      title="修改联系人"
      :visible.sync="drawerVisible"
	  @update:visible="handleDrawerVisibleChange">
	  
		<el-form :inline="true">
		  
        <el-form-item label="姓名" label-width="100px">
          <el-input v-model="drawContactData.name" autocomplete="off"
          :style="{width: '200px'}" clearable></el-input>
        </el-form-item>

		<el-form-item label="地址" label-width="100px">
		  <el-input v-model="drawContactData.address" autocomplete="off"
		  :style="{width: '250px'}" clearable></el-input>
		</el-form-item>

        <el-form-item label="电话号码" label-width="100px">
          <el-input v-model="drawContactData.phone" autocomplete="off"
          :style="{width: '200px'}" clearable
          type="text" maxlength="11" show-word-limit></el-input>
        </el-form-item>
		<el-form-item>
			<el-button type="primary" @click="updateContact">保 存</el-button>
		</el-form-item>
		
      </el-form>
			
    </el-drawer>


    <!-- 分页组件 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNumber"
      :page-sizes="[5, 10, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>


    <el-button type="primary" class="addContactButton" @click="showAddContactDialog">添加联系人</el-button>


    <!-- 增加联系人表单 -->
    <el-dialog title="添加联系人" :visible.sync="addContactFormVisible">
      <el-form :model="addContact" :inline="true">
		  
        <el-form-item label="姓名" label-width="120px">
          <el-input v-model="addContact.name" autocomplete="off"
          :style="{width: '200px'}" clearable></el-input>
        </el-form-item>

		<el-form-item label="地址" label-width="120px">
		  <el-input v-model="addContact.address" autocomplete="off"
		  :style="{width: '400px'}" clearable></el-input>
		</el-form-item>

        <el-form-item label="电话号码" label-width="120px">
          <el-input v-model="addContact.phone" autocomplete="off"
          :style="{width: '200px'}" clearable
          type="text" maxlength="11" show-word-limit></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelAddContact">取 消</el-button>
        <el-button type="primary" @click="saveContact">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  export default {
      data() {
        return {
		  pageNumber:1,               // 当前页数
		  pageSize: 5,                // 每页大小
		  total: 0,                   // 总共的数据条数
		  contactList:[],             // 联系人列表
		  name:'',                    // 姓名
		  address:'',                 // 地址
		  phone:'',                   // 电话号码
		  addContactFormVisible: false, // 增加联系人表单默认为false
		  addContact: {},             // 增加联系人的数据
		  drawerVisible: false,       // 抽屉默认不可见
		  drawContactData:'',         // 抽屉中的数据
		  
        };
      },


      methods:{
        // 获取联系人列表
        getContactList(){
          var params = {};
          params.pageNumber = this.pageNumber;
          params.pageSize = this.pageSize;
          // 如果输入了orderId，则作为参数传入
		  if(this.name != ''){
			  params.name = this.name;
			  params.pageNumber = 1;  // 防止在非第一页的地方查询，导致显示没有数据
		  }
          if(this.address != ''){
            params.address  = this.address;
			params.pageNumber = 1;
          }
		  if(this.phone != ''){
			  params.phone = this.phone;
			  params.pageNumber = 1;
		  }
          this.$axios.get('/personalAddressBook/contactPage', {params: params})
          .then(result => {
            var data = result.data;
            this.contactList = data.data.rows;
            this.total = data.data.total;
          })
          .catch(e=>{
            console.log(e);
          })
        },

        // 更改pageSize
        handleSizeChange(val){
          this.pageSize = val;
          this.getContactList();
        },

        // 更改pageNumber
        handleCurrentChange(val){
          this.pageNumber = val;
          this.getContactList();
        },

	
		// 打开修改抽屉并初始化数据
		updateContactDraw(row){
			this.drawerVisible = true;
			this.drawContactData = row;
		},
		
		// 当抽屉的可见性改变时，newValue 会是 true 或 false
		handleDrawerVisibleChange(newValue) {
		      
		      if (!newValue) {
				this.getContactList();  // 关闭时，重置List
		      }
		    },
			
		// 保存修改
		updateContact(){
			if(this.drawContactData.name == null ||
				this.drawContactData.address == null||
				this.drawContactData.phone == null || 
				this.drawContactData.name === ''||
				this.drawContactData.address === ''||
				this.drawContactData.phone === ''){
					this.$message.error('请完整输入联系人信息');
			}else{
				this.phoneExist(this.drawContactData.phone)
				  .then(exists => {
				    if (exists) {
				      this.$message.error('该手机号已存在！');
				    } else {
						this.$axios.post('/personalAddressBook/updateContact', this.drawContactData)
						.then(result => {
							var data = result.data;
							if(data.code == 200){
								this.$message.success(data.msg);
								this.getContactList();
								this.drawerVisible = false;
							}else{
								this.$message.warning(data.msg);
							}
						})
						.catch(e => {
							console.log(e);
						})
				    }
				  })
				  .catch(e => {
				    console.log(e);
				  });
		
			}
			
		},

        // 删除联系人
        deleteContact(id){
          this.$confirm('确定删除联系人吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                  }).then(() => {
                    this.$axios.get('/personalAddressBook/deleteContact', {params:{
                      "id": id
                      },
                    })
                    .then(result => {
                      var data = result.data;
                      if(data.code == 200){
                        this.$message.success(data.msg);
                        this.getContactList();
                      }else{
                        this.$message.warning(data.msg);
                      }
                    })
                    .catch(e => {
                      console.log(e);
                    })
                  }).catch(() => {
                    this.$message({
                      type: 'info',
                      message: '已取消删除'
                    });
                  });
        },
		
		showAddContactDialog(){
			this.addContactFormVisible = true;
		},
	
        // 取消增加联系人
        cancelAddContact(){
          this.addContact = {};  // 清空增加信息
          this.addContactFormVisible = false;
        },

        // 增加联系人
        saveContact(){
			if(this.addContact.name == null || 
				this.addContact.address == null||
				this.addContact.phone == null || 
				this.addContact.name === ''||
				this.addContact.address === ''||
				this.addContact.phone === ''){
					this.$message.error('请完整输入联系人信息');
			}
			else{
				this.phoneExist(this.addContact.phone)
				  .then(exists => {
				    if (exists) {
				      this.$message.error('该手机号已存在！');
				    } else {
						this.$axios.post('/personalAddressBook/addContact', this.addContact)
						.then(result => {
							var data = result.data;
							if(data.code == 200){
								this.$message.success(data.msg);
								this.getContactList();

							}else{
								this.$message.warning(data.msg);
							}
						})
						.catch(e => {
							console.log(e);
						})

						this.addContactFormVisible = false;
						this.addContact = {};
				    }
				  })
				  .catch(e => {
				    console.log(e);
				  });
				  
				
			}
			
        },
		
		async phoneExist(phone) {
		  return this.$axios.get('/personalAddressBook/phoneExist', {
		    params: {
		      phone: phone
		    }
		  })
		  .then(result => {
		    var data = result.data;
		    if (data.code === 200) {
				return data.data;
		    }
		    // 如果 code 不是 200，你可能想要处理错误或者返回 false
		    return false;
		  })
		  .catch(e => {
		    console.log(e);
		    // 在捕获到错误时，你可能想要返回 false 或者抛出错误
		    throw e; // 抛出错误，让调用者通过 .catch 处理
		  });
		},
      },
	  
	  handleKeyUp(){
		  const input = this.$refs.input;
			input.phone = input.phone.replace(/[^0-9]/g, '');
	  },

      mounted(){
        this.getContactList();
      }
    };
</script>

<style scoped>
  .el-descriptions__title {
      left: 200px;
      position: absolute;
  }

  .addOrderButton{
    margin: 20px;
  }
</style>
