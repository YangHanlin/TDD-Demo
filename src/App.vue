<template>
  <a-locale-provider :locale="zh_CN">
    <div id="app">
      <router-view />
    </div>
  </a-locale-provider>
</template>

<script>
// eslint-disable-next-line
import zh_CN from 'ant-design-vue/lib/locale-provider/zh_CN';
import LoginContainer from './components/LoginContainer.vue';

export default {
  name: 'app',
  data() {
    return {
      zh_CN,
    };
  },
  components: {
    LoginContainer,
  },
};
</script>

<style>
</style>
