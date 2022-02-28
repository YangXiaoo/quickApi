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
          <div class="presets-tips">
            配置名称
          </div>
          <el-input v-model="preset.name" class="preset-name" type="text" />
          <div class="preset-value">

            <div class="presets-tips">
              请求头参数
            </div>
            <!-- <el-input v-model="preset.value" type="textarea" :rows="2" placeholder="请求头key-value" /> -->
            <vue-json-editor v-model="preset.value" :show-btns="false" :mode="'code'" lang="zh" />
          </div>
        </div>
      </div>
      <div v-if="addPresetVisible" slot="footer" class="dialog-footer">
        <el-button @click="closeAddPresetPage">返回</el-button>
        <el-button type="primary" @click="addUserPreset">{{ btnName }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getUserHeaderPresets, addUserHeaderPresets, deleteUserHeaderPresets } from '@/api/presets'
import vueJsonEditor from 'vue-json-editor'

export default {
  name: 'HeaderPresets',
  components: { vueJsonEditor },
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
      },
      btnName: '新增'
    }
  },
  computed: {
    ...mapGetters([
      'loginName'
    ])
  },
  watch: {
    trigger(newValue) {
      this.dialogVisible = true
      this.clearPresetPage()
      this.closeAddPresetPage()
      this.getUserHeaderPresets()
    }
  },
  mounted() {
    this.getUserHeaderPresets()
  },
  methods: {
    handleClickAddUserPreset() {
      this.clearPresetPage()
      this.showAddPresetPage()
    },
    editPreset(item) {
      this.preset = item
      this.btnName = '更新'
      this.showAddPresetPage()
    },
    deleteUserPreset(item) {
      const param = {
        presetId: item.presetId
      }
      this.removeItemFromPresets(item) // test
      deleteUserHeaderPresets(param).then(res => {
        if (res.data.code === '000') {
          this.removeItemFromPresets(item)
          this.refresh()
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
    getUserHeaderPresets() {
      const param = {
        userName: this.loginName
      }
      getUserHeaderPresets(param).then(res => {
        if (res.data.code === '000') {
          const data = res.data.data
          if (data) {
            this.presets = data
            this.presets.forEach(item => {
              item.value = JSON.parse(item.value)
            })
          }
          this.refresh()
        }
      })
    },
    addUserPreset() {
      if (!this.preset) {
        return
      }
      const param = {
        presetId: this.preset.presetId || '',
        userName: this.loginName,
        name: this.preset.name,
        value: JSON.stringify(this.preset.value)
      }
      addUserHeaderPresets(param).then(res => {
        if (res.data.code === '000') {
          this.refresh()
          this.closeAddPresetPage()
          this.getUserHeaderPresets()
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
      this.btnName = '新增'
    },
    refresh() {
      this.$emit('refresh', this.presets)
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
        margin-bottom: 8px;
        border-bottom: 1px solid #EBEEF5FF;
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

  .preset-add {
    .presets-tips {
      margin: 4px;
      font-size: 14px;
    }
    .preset-name {
      margin: 4px 0 0 0;
    }
    .preset-value {

    }
  }
}
</style>
