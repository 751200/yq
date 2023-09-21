<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.loginName" placeholder="登陆名">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="resetPassword(record)">
              重置密码
            </a-button>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      title="用户表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="登陆名">
        <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item label="密码" v-show="!user.id">
        <a-input v-model:value="user.password" type="password"/>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
      title="重置密码"
      v-model:visible="resetModalVisible"
      :confirm-loading="resetModalLoading"
      @ok="handleResetModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="新密码">
        <a-input v-model:value="user.password" type="password"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts">
import { defineComponent ,onMounted,ref} from 'vue';
import axios from 'axios'
import {Tool} from "@/utils/tool";
import {message} from "ant-design-vue";

declare let hexMd5:any;
declare let KEY:any;


export default defineComponent({
  name: 'AdminUser',
  setup(){
    const param = ref();
    param.value = {};
    const users = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '登陆名',
        dataIndex: 'loginName'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '密码',
        dataIndex: 'password'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /*
    * 数据查询
    * */
    const  handleQuery = (params:any)=>{
      loading.value = true;
      //如果不清空现有数据，则编辑保存重新加载数据后，再点击编辑，则列表显示的还是编辑之前的数据
      users.value = [];
      axios.get("/user/getUserListByPage",{
        params:{
          page:params.page,
          size:params.size,
          loginName:param.value.loginName
        }
      }).then((resp)=>{
        loading.value = false;
        const  data = resp.data;
        if (data.success){
          users.value = data.content.list;

          //重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }

      });
    };
    /*
    * 表格点击页码时触发
    * */
    const handleTableChange =(pagination:any)=>{
      console.log("看看自带分页的参数都有些啥："+pagination);
      handleQuery({
        page:pagination.current,
        size:pagination.pageSize
      });
    };


    // -------- 表单 ---------
    const user = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      const reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$/;
      if(reg.test(user.value.password)) {

        user.value.password = hexMd5(user.value.password + KEY);

        axios.post("/user/save", user.value).then((resp) => {
          const data = resp.data;
          if (data.success) {
            modalVisible.value = false;

            //重新加载列表
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            });
          } else {
            message.error(data.message);
            modalVisible.value = false;
          }
        });
      }else {//if(reg)
        message.error("【密码】至少包含 数字和英文，长度6-32");
        modalLoading.value = false;
      }
    };

    const handleDelete = (id:number)=>{
      axios.get('/user/remove',{params:{id:id}}).then((resp)=>{
        if (resp.data.success){
          //重新加载列表
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize
          });
        }
      })
    }


    //编辑
    const edit = (record:any)=>{
      modalLoading.value = false;
      user.value = Tool.copy(record);
      modalVisible.value =true;
    }

    //新增
    const add = ()=>{
      modalLoading.value = false;
      user.value = {};
      modalVisible.value =true;
    }

    // -------- 重置密码 ---------
    const resetModalVisible = ref(false);
    const resetModalLoading = ref(false);
    const handleResetModalOk = () => {
      resetModalLoading.value = true;

      const reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$/;
      if(reg.test(user.value.password)) {

        console.log("校验成功",user.value.password);
        user.value.password = hexMd5(user.value.password + KEY);

        axios.post("/user/resetPassword", user.value).then((response) => {
          resetModalLoading.value = false;
          const data = response.data; // data = commonResp
          if (data.success) {
            resetModalVisible.value = false;

            // 重新加载列表
            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize,
            });
          } else {
            message.error(data.message);
          }

        });
      }else {//if(reg)
        message.error("【密码】至少包含 数字和英文，长度6-32");
      }
    };

    /**
     * 重置密码
     */
    const resetPassword = (record: any) => {
      resetModalVisible.value = true;
      user.value = Tool.copy(record);
      user.value.password = null;
    };

    onMounted(()=>{
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    })
    return {
      param,
      users,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      edit,
      add,

      user,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleResetModalOk,
      resetPassword,
      handleDelete,
      resetModalLoading,
      resetModalVisible,
    }
  }
});
</script>