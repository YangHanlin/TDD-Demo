<template>
  <div class="view">
  <a-result status="warning" title="Error">
    <template #extra>
      <a-button type="primary" @click="backHome">
        主页
      </a-button>
    </template>
  </a-result>
  </div>
</template>

<script>
import { Result, Button } from 'ant-design-vue';

export default {
  name: 'Error',
  components: {
    AResult: Result,
    AButton: Button,
  },
  methods: {
    backHome() {
      this.$router.push('/');
    }
  }
}
</script>

<style>
.view {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
