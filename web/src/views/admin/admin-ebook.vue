<template>


  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
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
      </p>
      <div class="about">
        <h1>海洋生物管理</h1>
      </div>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" style="width: 20%;height: 20%"/>
        </template>
        <template v-slot:category="{text,record}">
          <span>{{getCategoryName(record.category1Id)}}/{{getCategoryName(record.category2Id)}}</span>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <router-link :to="'/admin/doc?ebookId='+record.id">
              <a-button type="primary">
                文档管理
              </a-button>
            </router-link>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
              title="删除后不可恢复，确认删除"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(record.id)">
              <a-button type="primary">
              删除
              </a-button>
            </a-popconfirm>

          </a-space>
        </template>
      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal
    title="海洋生物表单"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk">
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">

      <a-form-item label="封面">
        <!--          上传封面-->
        <div class="clearfix">
          <a-upload
              v-model:file-list="fileList"
              list-type="picture-card"
              :customRequest="customRequest"
          >
            <div v-if="fileList.length < imgNum">
              <plus-outlined />
              <div style="margin-top: 8px">Upload</div>
            </div>
          </a-upload>
          <a-modal :visible="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </div>
      </a-form-item>

      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
<!--      <a-form-item label="分类一">-->
<!--        <a-input v-model:value="ebook.category1Id" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="分类二">-->
<!--        <a-input v-model:value="ebook.category2Id" />-->
<!--      </a-form-item>-->
      <a-form-item label="分类">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{label:'name',value:'id' ,children:'children'}"
            :options="level1"/>
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
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
  name: 'Adminebook',
  components: {
    UploadOutlined,
  },
  setup(){
    const param = ref();
    param.value={};


    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
        slots: { customRender: 'cover' }
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类',
        slots:{ customRender:'category'} ,
      },
      // {
      //   title: '分类二',
      //   dataIndex: 'category2Id' ,
      // },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
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
      axios.get("/ebook/getEbookListByPage",{
        params:{
          page:params.page,
          size:params.size,
          name:param.value.name,
        }

      }).then((resp)=>{
        loading.value = false;
        const  data = resp.data;
        if(data.success) {
          ebooks.value = data.content.list;

          //重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        }else {
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
      axios.post("/ebook/uploadImage",form,{headers:{ 'Content-Type':'multipart/form-data' }})
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
    const ebook = ref();
    const categoryIds = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];
      console.log("fileName******",fileName);
      ebook.value.cover = "/image/"+fileName;
      axios.post("/ebook/save",ebook.value).then((resp)=>{
        const data = resp.data
        if (data.success){
          modalVisible.value=false;
          modalLoading.value=false;
          //重新加载列表
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize,
          })
        }else {
          message.error((data.message));
        }
      })
      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 2000);
    };
    //编辑
    const edit = (record:any)=>{
      ebook.value=Tool.copy(record);
      modalVisible.value =true;
      categoryIds.value = [ebook.value.category1Id,ebook.value.category2Id]
    };
    //编辑
    const add = ()=>{
      ebook.value={};
      modalVisible.value =true;
    };

    //删除
    const handleDelete = (id:number)=>{
      axios.get("/ebook/remove",{params:{id:id}}).then((resp)=>{
        if (resp.data.success){
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize,
          });
        }
      })
    }

    const level1 = ref();
    let categorys:any;
    /*
    * 数据查询
    * */
    const  handleQueryCategory = ()=>{
      loading.value = true;
      axios.get("/category/allList").then((resp)=>{
        loading.value = false;
        const  data = resp.data;
        categorys = data.content;

        if(data.success){
          console.log(data.content);
          console.log("原始数组");

          level1.value = [];
          level1.value = Tool.array2Tree(data.content,0);
          console.log("树形结构",level1);
          handleQuery({
            page:1,
            size:pagination.value.pageSize
          });
        }else {
          message.error(data.message);
        }
        // categorys.value = data.content;



      });
    };
    const getCategoryName = (cid:number)=>{
      let result = "";
      categorys.forEach((item:any)=>{
        if (item.id === cid){
          result = item.name;
        }

      });
      return result;
    }



    onMounted(()=>{
      handleQueryCategory();




    })


    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,

      edit,
      modalVisible,
      modalLoading,
      handleModalOk,

      ebook,
      add,
      handleDelete,
      imgNum,
      previewVisible,
      previewImage,
      fileList,
      handleCancel,
      previewTitle,
      customRequest,

      categoryIds,
      level1,
      getCategoryName,
      categorys,

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