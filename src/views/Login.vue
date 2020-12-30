<template>
  <div class="view">
    <login-container />
  </div>
</template>

<script>
import LoginContainer from '@/components/LoginContainer.vue';

export default {
  name: 'Login',
  components: {
    LoginContainer,
  },
};
</script>

<style scoped>
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
