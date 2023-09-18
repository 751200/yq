<template>


  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <a-button type="primary" @click="add" size="large">
            新增
          </a-button>

          <a-table
              v-if="level1.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :pagination="false"
              :loading="loading"
              size="small"
              :default-expand-all-rows="true"
              >
<!--              @change="handleTableChange"-->
            <template #name = "{text:record}">
              {{record.sort}} {{text}}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>

                <a-popconfirm
                    title="删除后不可恢复,确认删除?"
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
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item >
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item >
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{maxHeight:'400px',overflow:'auto'}"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :field-names="{children:'children',label:'name',value:'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item >
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary"  @click="handlePreviewContent()">
                <EyeOutlined/>内容预览
              </a-button>
            </a-form-item>
            <a-form-item >
              <div id="content"></div>
            </a-form-item>
          </a-form>


        </a-col>
      </a-row>
      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>

<!--  <a-modal-->
<!--    title="分类表单"-->
<!--    v-model:visible="modalVisible"-->
<!--    :confirm-loading="modalLoading"-->
<!--    @ok="handleSave">-->

<!--  </a-modal>-->
</template>
<script lang="ts">
import {createVNode, defineComponent, onMounted, ref} from 'vue';
import axios from 'axios'
import {message, Modal, UploadProps} from "ant-design-vue";
import {ExclamationCircleOutlined, UploadOutlined} from "@ant-design/icons-vue";
import {Tool} from "@/utils/tool";
import {useRoute} from "vue-router";
import E from 'wangeditor';


export default defineComponent({
  name: 'AdminDoc',
  components: {
    UploadOutlined,
  },
  setup(){
    const route = useRoute();
    const docs = ref();
    const pagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });
    const loading = ref(false);
    const  treeSelectData = ref();
    treeSelectData.value=[];
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: 'Action',
        key: 'action',
        slots:{customRender:'action'},
      },
    ];

    const level1 = ref();
    level1.value=[];
    /*
    * 数据查询
    * */
    const  handleQuery = (params:any)=>{
      loading.value = true;
      // level1.value=[];
      axios.get("/doc/allList",{
        params:{
          ebookId:route.query.ebookId,
        }}).then((resp)=>{
        loading.value = false;
        const  data = resp.data;
        if(data.success){
          console.log(data.content);
          console.log("原始数组");

          level1.value = [];
          level1.value = Tool.array2Tree(data.content,0);
          console.log("树形结构",level1);

          treeSelectData.value = Tool.copy(level1.value)||[];
          treeSelectData.value.unshift({id: 0,name: '无'})
        }else {
          message.error(data.message);
        }
        // docs.value = data.content;



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
    // const previewImage = ref('');
    const previewTitle = ref('');
    const fileList = ref([]);

    const uuid = require('uuid');

// 自定义上传方法
//     const customRequest = (file:any)=>{
//       console.log("customRequest ",file)
//       // fileName=uuid.v4()+"-"+file.file.name; // 用uuid和原来的图片名称来命名新的图片名称,防止图片重名
//       //let file2 = new File([file.file],fileName)  // 创建新的文件对象 重新赋值图片名称
//       //file.file = file2
//       console.log("file",file.file);
//       // 表单
//       const form = new FormData();
//       form.append("file",file.file); // 表单添加文件
//       axios.post("/doc/uploadImage",form,{headers:{ 'Content-Type':'multipart/form-data' }})
//           .then(res=>{
//             if (res.data.success){
//               // 调用组件的方法，设置为成功
//               file.onSuccess(res,file.file);
//               file.status='done';
//               fileName = res.data.content;
//             }else {
//               file.onError();
//               file.status='error'
//             }
//           })
//     }

    const handleCancel = () => {
      previewVisible.value = false;
      previewTitle.value = '';
    };
    // -------- 表单 ---------

    const doc = ref();
    doc.value={
      ebookId:route.query.ebookId
    };
    // const docIds = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);


    const editor = new E('#content');
    editor.config.uploadImgShowBase64 = true;
    editor.config.zIndex = 0;

    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save",doc.value).then((resp)=>{
        modalLoading.value=false;
        const data = resp.data
        if (data.success){
            message.success("保存成功")
          //重新加载列表
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize,
          })
        }
      })
    };
    //编辑
    const edit = (record:any)=>{
      //清空
      editor.txt.html("");
      modalVisible.value =true;
      doc.value=Tool.copy(record);
      handleQueryContent();


      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value,record.id);

      treeSelectData.value.unshift({id:0,name:'无'});
    };
    //编辑
    const add = ()=>{
      //清空
      editor.txt.html("");
      modalVisible.value =true;
      doc.value={
        ebookId:route.query.ebookId,
      };
      treeSelectData.value = Tool.copy(level1.value)||[];

      treeSelectData.value.unshift({id:0,name:'无'});
    };

    //删除
    const handleDelete = (id:number)=>{
      // 清空数组，否则多次删除时，数组会一直增加
      ids.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value,id);
      Modal.confirm({
        title: '重要提醒',
        icon: createVNode(ExclamationCircleOutlined),
        content: '将删除：【' + deleteNames.join("，") + "】删除后不可恢复，确认删除？",
        onOk() {
          // console.log(ids)
          axios.get('/doc/remove?ids='+ids.join(",")).then((response) => {
            const data = response.data; // data = commonResp
            if (data.success) {
              // 重新加载列表
              handleQuery({
                page:pagination.value.current,
                size:pagination.value.pageSize,
              });
            } else {
              message.error(data.message);
            }
          });
        },
      });
    }


    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    /**
     * 查找整根树枝
     */
    const ids:Array<string> = [];
    const deleteNames : Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("delete", node);
          // 将目标ID放入结果集ids
          // node.disabled = true;
          ids.push(id);
          deleteNames.push(node.name);

          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/findContentById/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content)
        } else {
          message.error(data.message);
        }
      });
    };

    //--------------富文本预览
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () =>{
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () =>{
      drawerVisible.value = false;
    };

    onMounted(()=>{
      editor.create();
      handleQuery({
        route
      });


    })


    return {
      docs,
      pagination,
      columns,
      loading,
      level1,
      edit,
      modalVisible,
      modalLoading,
      handleSave,

      doc,
      add,
      handleDelete,
      imgNum,
      previewVisible,
      fileList,
      handleCancel,
      previewTitle,
      // customRequest,

      treeSelectData,
      handleQueryContent,

      onDrawerClose,
      handlePreviewContent,
      previewHtml,
      drawerVisible,
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