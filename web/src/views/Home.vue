<template>
  <div class="home">
    <a-layout-content style="padding: 0 50px">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item>Home</a-breadcrumb-item>
        <a-breadcrumb-item>List</a-breadcrumb-item>
        <a-breadcrumb-item>App</a-breadcrumb-item>
      </a-breadcrumb>
      <a-layout style="padding: 24px 0; background: #fff">
        <a-layout-sider width="200" style="background: #fff">
          <a-menu
              mode="inline"
              :style="{ height: '100%', borderRight: 0 }"
              @click="handleclick"
              :defaultSelectedKeys="['welcome']"
          >
            <a-menu-item key="welcome">
              <MailOutlined />
              <span>欢迎</span>
            </a-menu-item>
            <a-sub-menu v-for="item in level1" :key="item.id" >
              <template v-slot:title>
                <span><user-outlined />{{item.name}}</span>
              </template>
              <a-menu-item v-for="child in item.children" :key="child.id">
                <MailOutlined /><span>{{child.name}}</span>
              </a-menu-item>
            </a-sub-menu>
          </a-menu>
        </a-layout-sider>


        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
          <div class="welcome" v-show="isShowWelcome">
            <h1>欢迎使用海洋生物知识库</h1>
          </div>
          <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{gutter:20,column:3}" :data-source="ebooks">

            <template #renderItem="{ item }">
              <a-list-item key="item.name">
                <template #actions>
  <span >
    <component :is="'FileOutlined'" style="margin-right: 8px" />
    {{ item.docCount }}
  </span>
                  <span >
    <component :is="'UserOutlined'" style="margin-right: 8px" />
    {{ item.viewCount }}
  </span>
                  <span >
    <component :is="'LikeOutlined'" style="margin-right: 8px" />
    {{ item.voteCount }}
  </span>
                </template>

                <a-list-item-meta :description="item.description">
                  <template #title>
                    <router-link :to="'/doc?ebookId='+item.id">
                      {{item.name}}
                    </router-link>
                  </template>
                  <template #avatar><a-avatar :src="item.cover" />
                  </template>
                </a-list-item-meta>

              </a-list-item>
            </template>
          </a-list>
        </a-layout-content>
      </a-layout>
    </a-layout-content>
<!--  <a-button type="danger">Danger</a-button>-->
<!--    <img alt="Vue logo" src="../assets/logo.png">-->
<!--    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App"/>-->
  </div>
</template>

<script lang="ts">
import { defineComponent ,onMounted,ref,reactive,toRef} from 'vue';
import axios from 'axios'
import {message} from "ant-design-vue";
import {Tool} from "@/utils/tool";


//
// const listData: any = [];
// for (let i = 0; i < 23; i++) {
//   listData.push({
//     href: 'https://www.antdv.com/',
//     title: `ant design vue part ${i}`,
//     avatar: 'https://joeschmoe.io/api/v1/random',
//     description:
//         'Ant Design, a design language for background applications, is refined by Ant UED Team.',
//     content:
//         'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
//   });
// }

export default defineComponent({
  name: 'HomeView',
  // components: {
  //
  // },
  setup(){
    console.log("setup");
    const  ebooks = ref();
    const  ebooks1 =reactive({books:[]});
    const level1 = ref();
    let categorys:any;

    const  handleQueryCategory = ()=>{
      axios.get("/category/allList").then((resp)=>{
        const  data = resp.data;
        categorys = data.content;
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

    const isShowWelcome = ref(true);
    let category2id = 0;

    const handleQueryEbook = () => {
      axios.get("/ebook/getEbookListByPage",{
        params:{
          page:1,
          size:1000,
          category2Id: category2id,
        }

      }).then(function (resp){
        console.log(resp);
        const data = resp.data.content.list;
        ebooks.value = data;
        // ebooks1.books = data;
      });
    }


    const handleclick = (value:any)=>{
      if(value.key === 'welcome'){
        isShowWelcome.value = true;
      }else {
        category2id = value.key;
        isShowWelcome.value = false;
        handleQueryEbook();
      }
    }



    onMounted(()=>{
      handleQueryCategory();

    })


    return {
      ebooks,
      books : toRef(ebooks1,"books"),
      // listData,
      pagination : {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 3,
      },
      handleQueryCategory,
      level1,
      categorys,
      isShowWelcome,
      handleclick,
      handleQueryEbook,
      category2id,
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
