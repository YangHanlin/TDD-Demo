<template>
  <div id="login">
    <a-card id="main-container" title="Contacts App" style="width: 500px">
      <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-form-item label="通讯录 ID" :validate-status="validateStatus" :help="tip">
          <a-input v-model="pid" @blur="verify"/>
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 16, offset: 6 }">
          <div id="button-container">
          <a-button type="primary" @click="login" :disabled="validateStatus === 'error'">进入通讯录</a-button>
          <a-button>新建通讯录</a-button>
          </div>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script>
import { Card, Form, Input, Button, Col } from 'ant-design-vue';

export default {
  name: 'LoginContainer',
  components: {
    ACard: Card,
    AForm: Form,
    AFormItem: Form.Item,
    AInput: Input,
    AButton: Button,
    ACol: Col,
  },
  data() {
    return {
      pid: '',
      validateStatus: 'success',
      tip: '',
      loginDisabled: true,
    }
  },
  methods: {
    login() {
      const str = this.pid;
      if (!!str) return;
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
