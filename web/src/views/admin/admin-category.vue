<template>


  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-form layout="inline" :model="param">
        <a-form-item>
          <a-input v-model:value="param.name" placeholder="名称">
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
      <div class="about">
        <h1>海洋生物管理</h1>
      </div>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :pagination="false"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" style="width: 20%;height: 20%"/>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
              title="删除后不可恢复，确认删除"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(record.id)">
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
    title="分类表单"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk">
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">

      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
<!--        <a-input v-model:value="category.parent" />-->
        <a-select
            v-model:value="category.parent"
            ref="select"
        >
          <a-select-option value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="category.id ===c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="category.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts">
import { defineComponent ,onMounted,ref} from 'vue';
import axios from 'axios'
import {message, UploadProps} from "ant-design-vue";
import {UploadOutlined} from "@ant-design/icons-vue";
import {Tool} from "@/utils/tool";


export default defineComponent({
  name: 'Admincategory',
  components: {
    UploadOutlined,
  },
  setup(){
    const param = ref();
    param.value={};
    const categorys = ref();
    const pagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父分类',
        dataIndex: 'parent' ,
      },
      {
        title: '顺序',
        dataIndex: 'sort' ,
      },
      {
        title: 'Action',
        key: 'action',
        slots:{customRender:'action'},
      },
    ];

    const level1 = ref();
    /*
    * 数据查询
    * */
    const  handleQuery = (params:any)=>{
      loading.value = true;
      level1.value=[];
      axios.get("/category/allList",{params:{
          name:param.value.name,
          page:params.page,
          size:params.size,
        }}).then((resp)=>{
        loading.value = false;
        const  data = resp.data;
        if(data.success){
          console.log(data.content);
          console.log("原始数组");

          level1.value = [];
          level1.value = Tool.array2Tree(data.content,0);
          console.log("树形结构",level1);
        }else {
          message.error(data.message);
        }
        // categorys.value = data.content;



      });
    };
    /*
    * 表格点击页码时触发
    * */


    function getBase64(file: File) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
      });
    }

    let fileName = "";
    const imgNum = ref(1);
    const previewVisible = ref(false);
    const previewImage = ref('');
    const previewTitle = ref('');
    const fileList = ref([]);

    const uuid = require('uuid');

// 自定义上传方法
    const customRequest = (file:any)=>{
      console.log("customRequest ",file)
      // fileName=uuid.v4()+"-"+file.file.name; // 用uuid和原来的图片名称来命名新的图片名称,防止图片重名
      //let file2 = new File([file.file],fileName)  // 创建新的文件对象 重新赋值图片名称
      //file.file = file2
      console.log("file",file.file);
      // 表单
      const form = new FormData();
      form.append("file",file.file); // 表单添加文件
      axios.post("/category/uploadImage",form,{headers:{ 'Content-Type':'multipart/form-data' }})
          .then(res=>{
            if (res.data.success){
              // 调用组件的方法，设置为成功
              file.onSuccess(res,file.file);
              file.status='done';
              fileName = res.data.content;
            }else {
              file.onError();
              file.status='error'
            }
          })
    }

    const handleCancel = () => {
      previewVisible.value = false;
      previewTitle.value = '';
    };
    // -------- 表单 ---------
    const category = ref();
    // const categoryIds = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      // category.value.category1Id = categoryIds.value[0];
      // category.value.category2Id = categoryIds.value[1];
      console.log("fileName******",fileName);
      category.value.cover = "/image/"+fileName;
      axios.post("/category/save",category.value).then((resp)=>{
        const data = resp.data
        if (data.success){
          modalVisible.value=false;
          modalLoading.value=false;
          //重新加载列表
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize,
          })
        }
      })
      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 2000);
    };
    //编辑
    const edit = (record:any)=>{
      category.value=record;
      modalVisible.value =true;
    };
    //编辑
    const add = ()=>{
      category.value={};
      modalVisible.value =true;
    };

    //删除
    const handleDelete = (id:number)=>{
      axios.get("/category/remove",{params:{id:id}}).then((resp)=>{
        if (resp.data.success){
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize,
          });
        }
      })
    }




    onMounted(()=>{
      handleQuery({
        page:1,
        size:pagination.value.pageSize
      });


    })


    return {
      categorys,
      pagination,
      columns,
      loading,
      level1,
      edit,
      modalVisible,
      modalLoading,
      handleModalOk,

      category,
      add,
      handleDelete,
      imgNum,
      previewVisible,
      previewImage,
      fileList,
      handleCancel,
      previewTitle,
      customRequest,
      param,
      handleQuery,
    }
  }
});
</script>

<style scoped>
/* tile uploaded pictures */
.upload-list-inline :deep(.ant-upload-list-item) {
  float: left;
  width: 200px;
  margin-right: 8px;
}
.upload-list-inline [class*='-upload-list-rtl'] :deep(.ant-upload-list-item) {
  float: right;
}
</style>