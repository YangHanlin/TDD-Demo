<template>
  <a-card id="main-container" title="Contacts App" style="width: 700px; height: 500px">
    <div class="loading-animation" v-if="!loaded">
      <a-result>
        <template #icon>
          <a-icon slot="indicator" type="loading" style="font-size: 48px" spin />
        </template>
      </a-result>
    </div>
    <a-layout v-if="loaded" style="background-color: #fff; min-height: 100%;">
      <a-layout style="background-color: transparent; height: 350px">
        <a-layout-sider style="overflow-y: scroll; background-color: transparent">
          <a-menu v-if="loaded" v-model="selected">
          <a-menu-item v-for="contact in contacts" :key="contact.id">
            {{ contact.firstName + ' ' + contact.lastName }}
          </a-menu-item>
        </a-menu>
        </a-layout-sider>
        <a-layout-content style="display: flex; align-content: center; justify-content: center" v-if="selected.length === 0">
          <a-empty :description="false" style="margin: auto" />
        </a-layout-content>
        <div v-if="selected.length !== 0" style="width: 100%; overflow-y: scroll;">
          <div class="loading-animation-2" v-if="!itemLoaded">
            <a-result>
              <template #icon>
                <a-icon slot="indicator" type="loading" style="font-size: 48px" spin />
              </template>
            </a-result>
          </div>
          <div v-else>
            <a-form :label-col="{ span: 6, offset: 2 }" :wrapper-col="{ span: 14 }" layout="horizontal">
              <a-form-item label="名" >
                <a-input v-model="item.firstName" />
              </a-form-item>
              <a-form-item label="姓" >
                <a-input v-model="item.lastName" />
              </a-form-item>
              <a-form-item label="电话号码" >
                <a-input v-model="item.phone" />
              </a-form-item>
              <a-form-item label="电子邮箱" >
                <a-input v-model="item.email" />
              </a-form-item>
              <a-form-item label="地址" >
                <a-input v-model="item.address" />
              </a-form-item>
              <a-form-item label="备注" >
                <a-input v-model="item.note" />
              </a-form-item>
            </a-form>
            <a-form-item :wrapper-col="{ span: 14, offset: 8 }" v-if="displayError">
              <a-alert message="提交错误" type="error" banner />
            </a-form-item>
            <div style="display: flex; justify-content: space-between; padding: 10px">
              <a-button :loading="deleting" @click="deleteItem">删除</a-button>
              <a-space>
                <a-button :loading="canceling" @click="cancelItem">取消</a-button>
                <a-button :loading="saving" @click="submitItem" type="primary">保存</a-button>
              </a-space>
            </div>
          </div>
        </div>
      </a-layout>
      <a-layout-footer id="footer" style="background-color: transparent">
        <a-button @click="addContact" :loading="addLoading">添加联系人</a-button>
        <div style="height: 30px; line-height: 30px">通讯录 ID：<code>{{ pid }}</code>
        <a-button type="link" @click="logout">退出</a-button></div>
      </a-layout-footer>
    </a-layout>
  </a-card>
</template>

<script>
import { Card, Menu, Spin, Icon, Result, Layout, Button, Empty, Form, Input, Space, Alert } from 'ant-design-vue';
import { getContactsInPhonebook, createContact, getContactById, updateContact, deleteContact } from '@/api';

export default {
  name: 'ContactsContainer',
  components: {
    ACard: Card,
    AMenu: Menu,
    AMenuItem: Menu.Item,
    ASpin: Spin,
    AIcon: Icon,
    AResult: Result,
    ALayout: Layout,
    ALayoutSider: Layout.Sider,
    ALayoutFooter: Layout.Footer,
    ALayoutContent: Layout.Content,
    AButton: Button,
    AEmpty: Empty,
    AForm: Form,
    AFormItem: Form.Item,
    AInput: Input,
    ASpace: Space,
    AAlert: Alert,
  },
  data() {
    return {
      pid: '',
      contacts: [],
      loaded: false,
      selected: [],
      addLoading: false,
      itemLoaded: false,
      item: {},
      deleting: false,
      canceling: false,
      saving: false,
      displayError: false,
    }
  },
  mounted() {
    const pid = localStorage.getItem('logged-phonebook-id');
    if (!pid) {
      this.$router.push('/');
    }
    this.pid = pid;
    getContactsInPhonebook(pid).then((resp) => {
      this.contacts = resp.data.contacts;
      this.loaded = true;
    }).catch((err) => {
      console.error('retriving', err);
      // nothing to do
    })
  },
  methods: {
    addContact() {
      this.addLoading = true;
      createContact(this.pid, {
        firstName: 'John',
        lastName: 'Doe',
        phone: '+8613012345678'
      }).then((resp) => {
        const key = resp.data.id;
        getContactsInPhonebook(this.pid).then((resp) => {
          this.contacts = resp.data.contacts;
          this.selected = [key];
        }).catch((err) => {
          console.error('retriving', err);
          // nothing to do
        });
        this.addLoading = false;
      }).catch ((err) => {
        console.error('add error', err);
        this.addLoading = false;
      })
    },
    logout() {
      localStorage.removeItem('logged-phonebook-id');
      this.$router.push('/');
    },
    deleteItem() {
      this.deleting = true;
      this.displayError = false;
      deleteContact(this.pid, this.item.id).then((resp) => {
        getContactsInPhonebook(this.pid).then((resp) => {
          this.contacts = resp.data.contacts;
          this.selected = [];
          this.deleting = false;
        }).catch((err) => {
          this.deleting = false;
          console.error('retriving', err);
          // nothing to do
        });
      }).catch((err) => {
        this.deleting = false;
        console.error('delete', err);
        // nothing to do
      });
    },
    cancelItem() {
      this.canceling = true;
      this.displayError = false;
      getContactById(this.pid, this.item.id).then((resp) => {
        this.item = resp.data;
        this.canceling = false;
      }).catch((err) => {
        // nothing to do
        console.error('load error', err);
        this.canceling = false;
      });
    },
    submitItem() {
      this.saving = true;
      this.displayError = false;
      updateContact(this.pid, this.item.id, this.item).then((resp) => {
        getContactsInPhonebook(this.pid).then((resp) => {
          this.contacts = resp.data.contacts;
          this.selected = [this.item.id];
          this.saving = false;
        }).catch((err) => {
          this.saving = false;
          console.error('retriving', err);
          // nothing to do
        });
      }).catch((err) => {
        this.displayError = true;
        this.saving = false;
      });
    }
  },
  watch: {
    selected(val) {
      this.itemLoaded = false;
      getContactById(this.pid, val[0]).then((resp) => {
        this.item = resp.data;
        this.itemLoaded = true;
      }).catch((err) => {
        // nothing to do
        console.error('load error', err);
      });
    }
  },
}
</script>

<style>
#main-container {
  box-shadow: 3px 3px 10px #ececec;
}

.loading-animation {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.loading-animation-2 {
  position: absolute;
  top: 50%;
  left: 65%;
  transform: translate(-50%, -50%);
}

#footer {
  display: flex;
  justify-content: space-between;
  align-content: center;
  padding: 10px;
}
</style>
