<template>


  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-button type="primary" @click="add()" size="lage">
          添加
        </a-button>
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
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-button type="primary" @click="edit(record)">
              删除
            </a-button>

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
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类一">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2Id" />
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



export default defineComponent({
  name: 'Adminebook',
  // components: {
  //
  // },
  setup(){
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
        title: '分类一',
        dataIndex: 'category1Id' ,
      },
      {
        title: '分类二',
        dataIndex: 'category2Id' ,
      },
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
      axios.get("/getebookListByPage",{
        params:{
          page:params.page,
          size:params.size
        }

      }).then((resp)=>{
        loading.value = false;
        const  data = resp.data;
        ebooks.value = data.content.list;

        //重置分页按钮
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
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
    const ebook = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/save",ebook.value).then((resp)=>{
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
      ebook.value=record;
      modalVisible.value =true;
    };
    //编辑
    const add = ()=>{
      ebook.value={};
      modalVisible.value =true;
    };

    onMounted(()=>{
      handleQuery({
        page:1,
        size:pagination.value.pageSize
      });


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
    }
  }
});
</script>