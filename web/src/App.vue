<template>
  <a-layout>
    <TheHeader></TheHeader>
      <router-view/>
    <TheFooter></TheFooter>
  </a-layout>
</template>
<script lang="ts">
import { UserOutlined, LaptopOutlined, NotificationOutlined } from '@ant-design/icons-vue';
import { defineComponent, ref } from 'vue';
import TheHeader from "@/components/the-header.vue";
import TheFooter from "@/components/the-footer.vue";

export default defineComponent({
  components: {
    TheFooter,
    TheHeader,
    UserOutlined,
    LaptopOutlined,
    NotificationOutlined,
  },
  setup() {
    return {
      selectedKeys1: ref<string[]>(['2']),
      selectedKeys2: ref<string[]>(['1']),
      openKeys: ref<string[]>(['sub1']),
    };
  },
});
</script>
<style>
#components-layout-demo-top-side .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}
​
.ant-row-rtl #components-layout-demo-top-side .logo {
  float: right;
  margin: 16px 0 16px 24px;
}
​
.site-layout-background {
  background: #fff;
}
</style>


<!--<template>-->
<!--  <div id="nav">-->
<!--    <router-link to="/">Home</router-link> |-->
<!--    <router-link to="/about">About</router-link>-->
<!--  </div>-->
<!--  <router-view/>-->
<!--</template>-->

<!--<style>-->
<!--#app {-->
<!--  font-family: Avenir, Helvetica, Arial, sans-serif;-->
<!--  -webkit-font-smoothing: antialiased;-->
<!--  -moz-osx-font-smoothing: grayscale;-->
<!--  text-align: center;-->
<!--  color: #2c3e50;-->
<!--}-->

<!--#nav {-->
<!--  padding: 30px;-->
<!--}-->

<!--#nav a {-->
<!--  font-weight: bold;-->
<!--  color: #2c3e50;-->
<!--}-->

<!--#nav a.router-link-exact-active {-->
<!--  color: #42b983;-->
<!--}-->
<!--</style>-->
