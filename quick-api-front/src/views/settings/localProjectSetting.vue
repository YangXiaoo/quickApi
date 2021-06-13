<template>
  <div class="container">
    开发中...<br>
    <ol>
      <li>
        测试时自动保存页面请求数据
      </li>
      <li>token自动填写</li>
    </ol>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'LocalProjectSetting',
  components: {
  },
  data() {
    return {
      commitHistory: []
    }
  },
  computed: {
    ...mapGetters([
      'localServiceName',
      'groupList',
      'localMethodDataList',
      'localProjectName',
      'description'
    ])
  },
  watch: {
  },
  mounted() {
  },
  created() {
    // this.getCommitHistoryTableData()
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
.container {
  position: relative;
  margin: 10px 10px 10px 10px;
  border-radius: 5px;
}
</style>
