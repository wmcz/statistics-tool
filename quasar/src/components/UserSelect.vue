<template>
  <q-select menu-self="bottom start" menu-anchor="top start" :label="$t('user.many') + $t('optional')" multiple use-chips use-input counter v-model="selected" :options="useroptions" option-value="id" option-label="username" @filter="filterUsers"/>
</template>

<script>
import {api} from "boot/axios";
import {getErrorMessage} from "src/util";

export default {
  name: "UserSelect",
  data() {
    return {
      userdata: null,
      useroptions: null,
      selected: []
    }
  },
  methods: {
    filterUsers(val, update, abort) {
      const self = this
      if (self.userdata === null) {
        update(() => {
          api
            .get('/users')
            .then((response) => {
              self.userdata = response.data.map(u => {return {
                username: u.username,
                id: u.id
              }})
              self.useroptions = self.userdata})
            .catch(error => this.$q.notify(this.$t(getErrorMessage(error))))})
      } else {
        update(() => self.useroptions = self.userdata.filter((u) => u.username.toLowerCase().includes(val.toLowerCase())))
      }
    }
  }
}
</script>
