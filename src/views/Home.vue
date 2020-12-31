<template>
  <div class="view">
    <contacts-container />
  </div>
</template>

<script>
import ContactsContainer from '@/components/ContactsContainer.vue';

export default {
  name: 'Home',
  components: {
    ContactsContainer,
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
