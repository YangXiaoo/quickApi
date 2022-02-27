<template>
  <div>
    <el-dialog
      title="请求头预置管理"
      :visible.sync="dialogVisible"
      width="45%"
      center
    >
      <div class="presets-container">
        <div v-if="!addPresetVisible" class="presets-list">
          <div class="presets-header">
            Quickly add groups of header key/value pairs to the request. Start typing the name of the preset name and it'll show up in the dropdown list.
            <el-button @click="handleClickAddUserPreset">添加</el-button>
          </div>
          <div class="presets-data">
            <div v-for="(item, key) of presets" :key="key" class="preset-data-item">
              <div class="preset-data-item-left">
                <div class="preset-data-item-name" @click="editPreset(item)">
                  {{ item.name }}
                </div>
              </div>
              <div class="preset-data-item-right">
                <span class="preset-data-item-delete" @click="deleteUserPreset(item)"><i class="el-icon-delete" /></span>
              </div>
            </div>
          </div>
        </div>
        <div v-if="addPresetVisible" class="preset-add">
          <div class="presets-header-tips">
            添加请求头参数
          </div>
          <div class="preset-name">
            <input v-model="preset.name" type="text">
          </div>
          <div class="preset-value">
            <input v-model="preset.value" type="textarea" :rows="2" placeholder="请求头key-value">
          </div>
        </div>
      </div>
      <div v-if="addPresetVisible" slot="footer" class="dialog-footer">
        <el-button @click="closeAddPresetPage">取 消</el-button>
        <el-button type="primary" @click="addUserPreset">新增</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getUserPresets, addUserPresets, deleteUserPresets } from '@/api/presets'

export default {
  name: 'Presets',
  components: { },
  props: {
    trigger: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      dialogVisible: this.trigger,
      addPresetVisible: false,
      presets: [
      ],
      preset: {
        name: '',
        value: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'username'
    ])
  },
  watch: {
    trigger(newValue) {
      this.dialogVisible = true
    }
  },
  mounted() {
    this.getUserPresets()
  },
  methods: {
    handleClickAddUserPreset() {
      this.clearPresetPage()
      this.showAddPresetPage()
    },
    editPreset(item) {
      console.log('editPreset', item)
      this.preset = item
      this.showAddPresetPage()
    },
    deleteUserPreset(item) {
      const param = {
        presetId: item.presetId
      }
      this.removeItemFromPresets(item) // test
      deleteUserPresets(param).then(res => {
        if (res.data.code === '000') {
          this.removeItemFromPresets(item)
        }
      })
    },
    removeItemFromPresets(item) {
      if (!item || !item.presetId) {
        return
      }

      for (let i = 0; i < this.presets.length; ++i) {
        if (this.presets[i].presetId === item.presetId) {
          console.log('removeItemFromPresets', item)
          this.presets.splice(i, 1)
        }
      }
    },
    getUserPresets() {
      const param = {
        username: this.username
      }
      getUserPresets(param).then(res => {
        if (res.data.code === '000') {
          const data = res.data.data
          if (data) {
            this.presets = data
          }
        }
      })
    },
    addUserPreset() {
      if (!this.preset) {
        return
      }
      const param = {
        presetId: this.preset.presetId || '',
        name: this.preset.name,
        value: this.preset.value
      }
      addUserPresets(param).then(res => {
        if (res.data.code === '0000') {
          this.closeAddPresetPage()
          this.getUserPresets()
        }
      })
    },
    showAddPresetPage() {
      this.addPresetVisible = true
    },
    closeAddPresetPage() {
      this.addPresetVisible = false
    },
    clearPresetPage() {
      this.preset = {
        name: '',
        value: ''
      }
    }
  }
}
</script>

<style lang="scss">
.presets-container {
  .presets-list {
    .presets-header {

    }
    .presets-data {
      display: flex;
      flex-flow: column nowrap;
      justify-content: center;
      margin: 8px 0 0 0;
      .preset-data-item {
        flex-flow: row nowrap;
        align-items: center;
        justify-content: space-around;
        padding-bottom: 2px;
        border-bottom: 1px solid black;
        display: flex;
        .preset-data-item-left {
          margin-right: auto;
          .preset-data-item-name {
            cursor: pointer;
          }
        }
        .preset-data-item-right {
          margin-left: auto;
          .preset-data-item-delete {
            cursor: pointer;
          }
        }
      }
    }
  }
}
</style>
