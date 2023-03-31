<template>
  <q-page class="flex flex-center">
    <div class="q-gutter-md">
      <h3>{{ userdata.username }}</h3>
    <q-list top bordered class="rounded-borders" style="min-width: 600px">
      <q-item-label header>{{ $t('tag.many') }}</q-item-label>
      <q-input class="q-pa-md" ref="tagFilterRef" v-model="tagfilter" :label="$t('filter')">
        <template v-slot:append>
          <q-icon v-if="tagfilter !== ''" name="clear" class="cursor-pointer" @click="resetTagFilter" />
        </template>
      </q-input>
      <q-table :rows="taglist" :row-key="name" grid style="max-width: 600px" :loading="tagloading" :filter="tagfilter" :pagination="{ rowsPerPage: 10}">
        <template v-slot:item="props">
          <TagLink :key="props.row.name" suppresselems v-bind="props.row" right-icon="clear" @deleteTag="(id) => removeTag(id)" style="width: 600px"/>
        </template>
        <template v-slot:no-data>
          {{ $t('tag.none') }}
        </template>
      </q-table>
      <div v-if="taginput" class="q-mb-md q-mx-md q-mt-none">
        <TagSelect  url="tags/user-tags" :label="$t('tag.add')" ref="tagSelect"/>
        <q-btn class="q-mr-sm" color="primary" :label="$t('submit')" @click="onTagSubmit"/>
        <q-btn outline color="primary" :label="$t('cancel')" @click="taginput = false"/>
      </div>
      <q-btn v-else class="q-mb-md q-ml-md" color="primary" :label="$t('tag.add')" @click="taginput = true"/>
    </q-list>

    <q-list top bordered class="rounded-borders" style="min-width: 600px">
      <q-item-label header>{{ $t('event.many') }}</q-item-label>
      <q-input class="q-pa-md" ref="eventFilterRef" v-model="eventfilter" :label="$t('filter')">
        <template v-slot:append>
          <q-icon v-if="eventfilter !== ''" name="clear" class="cursor-pointer" @click="resetEventFilter" />
        </template>
      </q-input>
      <q-table :rows="eventlist" :row-key="name" grid style="max-width: 600px" :loading="eventloading" :filter="eventfilter" :pagination="{ rowsPerPage: 10}">
        <template v-slot:item="props">
          <EventLink :key="props.row.name" supressnotag v-bind="props.row" right-icon="clear" @deleteEvent="(id) => removeEvent(id)" style="width: 600px"/>
        </template>
        <template v-slot:no-data>
          {{ $t('event.none') }}
        </template>
      </q-table>
      <div v-if="eventinput" class="q-mb-md q-mx-md q-mt-none">
        <EventSelect :label="$t('event.add')" ref="eventSelect"/>
        <q-btn class="q-mr-sm" color="primary" :label="$t('submit')" @click="onEventSubmit"/>
        <q-btn outline color="primary" :label="$t('cancel')" @click="eventinput = false"/>
      </div>
      <q-btn v-else class="q-mb-md q-ml-md" color="primary" :label="$t('event.add')" @click="eventinput = true"/>
    </q-list>
    </div>
  </q-page>
</template>

<script>
import {defineComponent} from 'vue'
import { api } from 'boot/axios'
import {useRoute} from "vue-router";
import EventLink from "components/EventLink.vue";
import TagLink from "components/TagLink.vue";
import TagSelect from "components/TagSelect.vue";
import EventSelect from "components/EventSelect.vue";

function updateEvents(self) {
  api.put('users', self.userdata).then((response) => {
    self.userdata = response.data
    self.eventlist = self.eventdata.filter(e => self.userdata.eventIds.includes(e.id)).map(e => {
      return {
        name: e.name,
        id: e.id,
        tags: []
      }
    })
  })
}

function updateTags(self) {
  api.put('users', self.userdata).then((response) => {
    self.userdata = response.data
    self.taglist = self.tagdata.filter(t => self.userdata.tagIds.includes(t.id))
  })
}


export default defineComponent({
  data() {
    return {
      tagfilter: '',
      eventfilter: '',
      userdata: {},
      tagdata: [],
      taglist: [],
      eventdata: [],
      eventlist: [],
      tagloading: true,
      eventloading: true,
      eventinput: false,
      taginput: false
    }
  },
  name: 'UserDetailPage',
  components: {
    TagSelect,
    EventLink,
    TagLink,
    EventSelect
  },
  mounted() {
    api.get('users/' + useRoute().params.id).then((response) => {
      this.userdata = response.data
      api.get('tags/user-tags').then((tagresponse) => {
        this.tagdata = tagresponse.data
        this.taglist = this.tagdata.filter(t => response.data.tagIds.includes(t.id))
        this.tagloading = false
      })
      !response.data.eventIds.length ? this.eventloading = false :
      api.get('events').then((eventresponse) => {
        this.eventdata = eventresponse.data
        this.eventlist = this.eventdata.filter(e => response.data.eventIds.includes(e.id)).map(e => {return {
          name: e.name,
          id: e.id,
          tags: []
        }})
        this.eventloading = false
      })
    })
  },
  methods: {
    resetTagFilter() {
      this.tagfilter = ''
    },
    resetEventFilter() {
      this.eventfilter = ''
    },
    onTagSubmit() {
      this.tagloading = true
      this.userdata.tagIds.push(...this.$refs.tagSelect.selected.map(t => t.id))
      updateTags(this)
      this.tagloading = false
    },
    onEventSubmit() {
      this.eventloading = true
      this.userdata.eventIds.push(...this.$refs.eventSelect.selected.map(e => e.id))
      updateEvents(this)
      this.eventloading = false
    },
    removeEvent(id) {
      this.eventloading = true
      this.userdata.eventIds.splice(this.userdata.eventIds.indexOf(id),1)
      updateEvents(this)
      this.eventloading = false
    },
    removeTag(id) {
      this.tagloading = true
      this.userdata.tagIds.splice(this.userdata.tagIds.indexOf(id), 1)
      updateTags(this)
      this.tagloading = false
    }
  }
})
</script>