<template>
  <a-card id="main-container" title="Contacts App" style="width: 700px; height: 500px">
    <div id="loading-animation" v-if="!loaded">
      <a-result>
        <template #icon>
          <a-icon slot="indicator" type="loading" style="font-size: 48px" spin />
        </template>
      </a-result>
    </div>
    <a-layout v-if="loaded" style="background-color: #fff; min-height: 100%;">
      <a-layout style="background-color: transparent">
        <a-layout-sider style="overflow-y: scroll; height: 350px">
          <a-menu v-if="loaded" v-model="selected">
          <a-menu-item v-for="contact in contacts" :key="contact.id">
            {{ contact.firstName + ' ' + contact.lastName }}
          </a-menu-item>
        </a-menu>
        </a-layout-sider>
        <a-layout-content >Content</a-layout-content>
      </a-layout>
      <a-layout-footer id="footer" style="background-color: transparent">
        <a-button @click="addContact" :loading="addLoading">添加联系人</a-button>
        <div style="height: 30px; line-height: 30px">通讯录 ID：<code>{{ pid }}</code></div>
      </a-layout-footer>
    </a-layout>
  </a-card>
</template>

<script>
import { Card, Menu, Spin, Icon, Result, Layout, Button } from 'ant-design-vue';
import { getContactsInPhonebook, createContact } from '@/api';

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
    AButton: Button
  },
  data() {
    return {
      pid: '',
      contacts: [],
      loaded: false,
      selected: [],
      addLoading: false,
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
  }
}
</script>

<style>
#main-container {
  box-shadow: 3px 3px 10px #ececec;
}

#loading-animation {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

#footer {
  display: flex;
  justify-content: space-between;
  align-content: center;
  padding: 10px;
}
</style>
