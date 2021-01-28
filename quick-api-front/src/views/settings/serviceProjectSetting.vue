<template>
  <div class="container">
    <el-card>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="请求地址设置" name="addressSetting">
          <el-form ref="addressRule" :model="addressForm" label-width="200px">
            <el-form-item label="项目" prop="projectName">
              <el-select v-model="addressForm.projectName" placeholder="请选择项目">
                <el-option
                  v-for="projectName of projectNameList"
                  :key="projectName"
                  :label="projectName"
                  :value="projectName"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="是否使用文档地址" prop="addressChoose">
              不使用
              <el-switch v-model="addressForm.addressChoose" />
              使用
            </el-form-item>
            <el-form-item v-show="!addressForm.addressChoose" label="服务地址" prop="address">
              <el-select v-model="addressForm.serviceProjectAddress" allow-create placeholder="请选择项目服务地址">
                <el-option v-for="address of addressList" :key="address" :value="address" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="handleAddressDefaultClick">默认</el-button>
              <el-button type="primary" @click="handleAddressClick">确定</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="Token设置" name="TokenSetting">
          <el-form ref="tokenRule" :model="tokenForm" label-width="200px">
            <el-form-item label="项目" prop="projectName">
              <el-select v-model="tokenForm.projectName" placeholder="请选择项目">
                <el-option
                  v-for="projectName of projectNameList"
                  :key="projectName"
                  :label="projectName"
                  :value="projectName"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="请求地址" prop="tokenUrlName">
              <el-select v-model="tokenForm.tokenUrlName" allow-create placeholder="请选择项目服务地址">
                <el-option v-for="url of methodUrlList" :key="url" :value="url" />
              </el-select>
            </el-form-item>
            <el-form-item>
            <el-form-item label="token关键字" prop="tokenKey">
              <el-input v-model="tokenForm.tokenKey" />
            </el-form-item>
              <el-button type="primary" @click="handleTokenClick">确定</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getRunningService } from '@/api/project'
export default {
  name: 'ServiceProjectSetting',
  components: {
  },
  data() {
    return {
      activeName: 'addressSetting',
      addressForm: {
        projectName: '',
        addressChoose: true,
        serviceProjectAddress: ''
      },
      addressList: [],
      tokenForm: {
        projectName: '',
        tokenUrlName: '',
        tokenKey: ''
      },
      methodUrlList: []
    }
  },
  computed: {
    ...mapGetters([
      'projectRoutes',
      'projectMethodDataList'
    ]),
    projectNameList() {
      return Object.keys(this.projectMethodDataList)
    }
  },
  watch: {
    'addressForm.addressChoose': {
      handler(val) {
        if (!val) {
          this.getAddressList()
        }
      },
      deep: true
    },
    'tokenForm.projectName': {
      handler(val) {
        if (val) {
          this.getMethodUrlList()
        }
      },
      deep: true
    }
  },
  mounted() {
  },
  created() {
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    },
    getAddressList() {
      const data = {
        projectName: this.addressForm.projectName
      }

      getRunningService(data).then(res => {
        if (res.data.code === '000') {
          this.addressList = res.data.data
        }
      })
    },
    getMethodUrlList() {
      this.methodUrlList = this.projectMethodDataList[this.tokenForm.projectName].map(item => {
        return item.name
      })
    },
    handleAddressClick() {
      const data = {
        projectName: this.addressForm.projectName,
        serviceProjectAddress: this.addressForm.serviceProjectAddress
      }

      this.$store.dispatch('serviceProject/setServiceProjectAddress', data)
    },
    handleAddressDefaultClick() {
      //
    },
    handleTokenClick() {
      this.$store.dispatch('serviceProject/setServiceProjectToken', this.tokenForm)
    }
  }
}
</script>
<style lang="scss" scoped>
.container {
  position: relative;
  min-height: 500px;
  margin: 90px 10px 10px 10px;
  border-radius: 5px;

  .history-table {
    margin-top: 20px;
  }
}
</style>
