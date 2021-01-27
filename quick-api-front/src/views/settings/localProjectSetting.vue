<template>
  <div class="container">
    开发中...

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
    this.getCommitHistoryTableData()
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
.container {
  position: relative;
  margin: 90px 10px 10px 10px;
  border-radius: 5px;
}
</style>
