<template>
  <div id="login">
    <a-card id="main-container" title="Contacts App" style="width: 500px">
      <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-form-item label="通讯录 ID" :validate-status="validateStatus" :help="tip">
          <a-input v-model="pid" @blur="verify" :disabled="loading"/>
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 16, offset: 6 }" v-if="displayError">
          <a-alert message="ID 错误" type="error" banner />
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 16, offset: 6 }">
          <div id="button-container">
          <a-button type="primary" @click="login" :disabled="validateStatus === 'error'" :loading="loading">进入通讯录</a-button>
          <a-button @click="register" :loading="registerLoading">新建通讯录</a-button>
          </div>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script>
import { Card, Form, Input, Button, Col, Alert } from 'ant-design-vue';
import { getContactsInPhonebook, createPhonebook } from '@/api';
export default {
  name: 'LoginContainer',
  components: {
    ACard: Card,
    AForm: Form,
    AFormItem: Form.Item,
    AInput: Input,
    AButton: Button,
    ACol: Col,
    AAlert: Alert,
  },
  data() {
    return {
      pid: '',
      validateStatus: 'success',
      tip: '',
      loginDisabled: true,
      loading: false,
      displayError: false,
      registerLoading: false,
    }
  },
  methods: {
    login() {
      const str = this.pid.trim();
      if (!str) return;
      this.loading = true;
      getContactsInPhonebook(str).then((response) => {
        localStorage.setItem('logged-phonebook-id', str);
        this.loading = false;
        this.$router.push(`/phonebook/${str}`)
      }).catch((err) => {
        this.displayError = true;
        this.loading = false;
      });
    },
    verify() {
      const str = this.pid.trim();
      const regex = /^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$/;
      if (str && !regex.test(str)) {
        this.validateStatus = 'error';
        this.tip = '格式错误';
      } else {
        this.validateStatus = 'success';
        this.tip = '';
      }
    },
    register() {
      this.registerLoading = true;
      createPhonebook().then((resp) => {
        this.pid = resp.data.id;
        this.login();
      }).catch((err) => {
        // pass
      })
    }
  },
};
</script>

<style scoped>
#main-container {
  box-shadow: 3px 3px 10px #ececec;
}

#button-container {
  display: flex;
  justify-content: space-between;
}
</style>
